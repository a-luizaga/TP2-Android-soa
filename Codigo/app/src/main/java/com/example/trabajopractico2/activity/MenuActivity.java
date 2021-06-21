package com.example.trabajopractico2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.trabajopractico2.R;
import com.example.trabajopractico2.utilidades.Utilidades;
import com.example.trabajopractico2.utilidades.VariablesGlobales;

import java.util.Timer;
import java.util.TimerTask;

public class MenuActivity extends AppCompatActivity {

    private Button buttonDosisUno;
    private Button buttonDosisDos;
    private Button buttonDatosGenerales;
    private Button buttonHistorialSensores;

    private String token;
    private String tokenRefresh;

    final Handler handler = new Handler();
    Timer timer = new Timer();

    TextView txtBateria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        token = getIntent().getStringExtra("Token");
        tokenRefresh = getIntent().getStringExtra("TokenRefresh");
    }

    @Override
    protected void onStart() {
        super.onStart();

        txtBateria = findViewById(R.id.txtPorcentajeBateria);
        txtBateria.setText(String.valueOf(Utilidades.estadoBateria(this)) + "%");
    }

    @Override
    protected void onResume() {
        super.onResume();

        buttonDosisUno = findViewById(R.id.btnDosisUno);
        buttonDosisUno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utilidades.registrarEventoRetrofit(VariablesGlobales.env,"Boton Dosis 1 Apretado", "Se ingreso al grafico de dosis 1", token);
                abrirDosisUno(v);
            }
        });

        buttonDosisDos = findViewById(R.id.btnDosisDos);
        buttonDosisDos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utilidades.registrarEventoRetrofit(VariablesGlobales.env,"Boton Dosis 2 Apretado", "Se ingreso al grafico de dosis 2", token);
                abrirDosisDos(v);
            }
        });

        buttonDatosGenerales = findViewById(R.id.btnDatosGenerales);
        buttonDatosGenerales.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utilidades.registrarEventoRetrofit(VariablesGlobales.env,"Boton Datos Generales Apretado", "Se ingreso a los datos generales", token);
                abrirDatosGenerales(v);
            }
        });

        buttonDatosGenerales = findViewById(R.id.btnDatosGenerales);
        buttonDatosGenerales.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Utilidades.registrarEventoRetrofit(VariablesGlobales.env,"Boton Datos Historial Sensores Apretado", "Se ingreso al historial de sensores", token);
                abrirDatosGenerales(v);
            }
        });

        TimerTask actualizarBateria = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new BateriaSegundoPlano().execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(actualizarBateria, 0, 30000); //cada 30 segundos actualizo estado de la bateria

        /*TimerTask actualizacionDeToken = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new ActualizarToken().execute();
                        } catch (Exception e) {
                            Log.e("error", e.getMessage());
                        }
                    }
                });
            }
        };

        timer.schedule(actualizacionDeToken, 0, 1740000);*/ //actualizo token cada 29 minutos
    }

    public void abrirDosisUno(View view) {
        Intent intent = new Intent(this, DosisUnoActivity.class);
        intent.putExtra("Token", token);
        intent.putExtra("TokenRefresh", tokenRefresh);
        startActivity(intent);
    }

    public void abrirDosisDos(View view) {
        Intent intent = new Intent(this, DosisDosActivity.class);
        intent.putExtra("Token", token);
        intent.putExtra("TokenRefresh", tokenRefresh);
        startActivity(intent);
    }

    public void abrirDatosGenerales(View view) {
        Intent intent = new Intent(this, DatosGeneralesActivity.class);
        intent.putExtra("Token", token);
        intent.putExtra("TokenRefresh", tokenRefresh);
        startActivity(intent);
    }

    private class BateriaSegundoPlano extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = MenuActivity.this.registerReceiver(null, ifilter);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level * 100 / (float)scale;

            return (int)batteryPct;
        }

        @Override
        protected void onPostExecute(Integer bateria) {
            txtBateria = findViewById(R.id.txtPorcentajeBateria);
            txtBateria.setText(bateria.toString() + "%");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class ActualizarToken extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Utilidades.actualizarTokenRetrofit(tokenRefresh);
            return VariablesGlobales.tokenServidor;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}






