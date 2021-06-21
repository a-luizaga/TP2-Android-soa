package com.example.trabajopractico2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabajopractico2.R;
import com.example.trabajopractico2.dto.RegisterRequest;
import com.example.trabajopractico2.dto.RegisterResponse;
import com.example.trabajopractico2.service.PostRegisterService;
import com.example.trabajopractico2.utilidades.Utilidades;
import com.example.trabajopractico2.utilidades.VariablesGlobales;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterActivity extends AppCompatActivity {

    EditText txtAmbiente;
    EditText txtNombre;
    EditText txtApellido;
    EditText txtDni;
    EditText txtEmail;
    EditText txtContrasena;
    EditText txtComision;
    EditText txtGrupo;
    Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnRegistro = findViewById(R.id.buttonRegistro);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Utilidades.hayConexionInternet(RegisterActivity.this)){
                    if(validarCamposRegistro()){
                        registrarseEnElServidor();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean validarCamposRegistro() {

        txtAmbiente = findViewById(R.id.txtAmbienteRegistro);
        txtNombre = findViewById(R.id.txtNombreRegistro);
        txtApellido = findViewById(R.id.txtApellidoRegistro);
        txtDni = findViewById(R.id.txtDNIRegistro);
        txtEmail = findViewById(R.id.txtEmailRegistro);
        txtContrasena = findViewById(R.id.txtPasswordRegistro);
        txtComision = findViewById(R.id.txtComisionRegistro);
        txtGrupo = findViewById(R.id.txtGrupoRegistro);

        if (txtAmbiente.getText().toString().compareTo("TEST") != 0 && txtAmbiente.getText().toString().compareTo("PROD") != 0) {
            Toast.makeText(this, "Campo Ambiente no es TEST ni PROD", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtNombre.getText().toString().isEmpty()) {
            Toast.makeText(this, "Campo Nombre esta vacio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtApellido.getText().toString().isEmpty()) {
            Toast.makeText(this, "Campo Apellido esta vacio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtDni.getText().toString().isEmpty() ||
                Integer.parseInt(txtDni.getText().toString().trim()) < 0 ||
                Integer.parseInt(txtDni.getText().toString()) > 99999999) {
            Toast.makeText(this, "Campo DNI incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtEmail.getText().toString().isEmpty() || !txtEmail.getText().toString().contains("@")) {
            Toast.makeText(this, "Campo Email no valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtContrasena.getText().toString().length() < 8) {
            Toast.makeText(this, "Campo contraseña debe contener al menos 8 digitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtComision.getText().toString().isEmpty() ||
                Integer.parseInt(txtComision.getText().toString().trim()) != 2900 &&
                        Integer.parseInt(txtComision.getText().toString()) != 3900) {
            Toast.makeText(this, "Campo Comision debe ser 2900 o 3900", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtGrupo.getText().toString().isEmpty() ||
                Integer.parseInt(txtGrupo.getText().toString().trim()) < 0 ||
                Integer.parseInt(txtGrupo.getText().toString()) > 11) {
            Toast.makeText(this, "Campo Grupo incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void registrarseEnElServidor() {

        RegisterRequest request = new RegisterRequest();
        request.setEnv(txtAmbiente.getText().toString());
        request.setName(txtNombre.getText().toString());
        request.setLastname(txtApellido.getText().toString());
        request.setDni(Long.parseLong(txtDni.getText().toString()));
        request.setEmail(txtEmail.getText().toString());
        request.setPassword(txtContrasena.getText().toString());
        request.setCommission(Long.parseLong(txtComision.getText().toString()));
        request.setGroup(Long.parseLong(txtGrupo.getText().toString()));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VariablesGlobales.servidor)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostRegisterService service = retrofit.create(PostRegisterService.class);

        Call<RegisterResponse> call = service.register(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Registro realizado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                    VariablesGlobales.tokenServidor = response.body().getToken();
                    VariablesGlobales.tokenRefreshServidor = response.body().getToken_refresh();
                    intent.putExtra("Token", response.body().getToken());
                    intent.putExtra("TokenRefresh", response.body().getToken_refresh());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "No se ha podido registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}