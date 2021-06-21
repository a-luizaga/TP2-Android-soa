package com.example.trabajopractico2.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabajopractico2.R;
import com.example.trabajopractico2.dto.LoginRequest;
import com.example.trabajopractico2.dto.LoginResponse;
import com.example.trabajopractico2.service.PostLoginService;
import com.example.trabajopractico2.utilidades.Utilidades;
import com.example.trabajopractico2.utilidades.VariablesGlobales;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText txtEmail;
    EditText txtContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        txtEmail = findViewById(R.id.textEmail);
        txtContrasena = findViewById(R.id.textPassword);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        leerPreferencias();

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                registrarse(v);
            }
        });

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                iniciarSesion(v);
            }
        });
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    public void registrarse(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void iniciarSesion(View view) {
        if(Utilidades.hayConexionInternet(this)){
            if(validarCamposInicioSesion()){
                guardarPreferencias(txtEmail.getText().toString(), txtContrasena.getText().toString());
                iniciarSesionEnElServidor();
            }
        }
        else{
            Toast.makeText(this, "No hay conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarSesionEnElServidor() {

        LoginRequest request = new LoginRequest();
        request.setEmail(txtEmail.getText().toString());
        request.setPassword(txtContrasena.getText().toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VariablesGlobales.servidor)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostLoginService service = retrofit.create(PostLoginService.class);

        Call<LoginResponse> call = service.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Conexion Exitosa", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    VariablesGlobales.tokenServidor = response.body().getToken();
                    VariablesGlobales.tokenRefreshServidor = response.body().getToken_refresh();
                    intent.putExtra("Token", response.body().getToken());
                    intent.putExtra("TokenRefresh", response.body().getToken_refresh());
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Email o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error Inesperado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean validarCamposInicioSesion() {

        txtEmail = findViewById(R.id.textEmail);
        txtContrasena = findViewById(R.id.textPassword);

        if (txtEmail.getText().toString().isEmpty() || !txtEmail.getText().toString().contains("@")) {
            Toast.makeText(LoginActivity.this, "Campo Email no valido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txtContrasena.getText().toString().length() < 8) {
            Toast.makeText(LoginActivity.this, "Campo contraseña debe contener al menos 8 digitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void guardarPreferencias(String email, String pass){

        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("Email", email);
        editor.putString("Password", pass);
        editor.apply();
    }

    public void leerPreferencias(){
        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        String email = preferencias.getString("Email", null);
        String pass = preferencias.getString("Password", null);

        if(email != null && pass != null){
            txtEmail.setText(email);
            txtContrasena.setText(pass);
        }
    }
}


