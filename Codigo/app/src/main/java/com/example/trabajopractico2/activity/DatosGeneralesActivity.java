package com.example.trabajopractico2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.trabajopractico2.R;
import com.example.trabajopractico2.utilidades.Scrapping;

public class DatosGeneralesActivity extends AppCompatActivity {

    TextView numContagiados;
    TextView numContagiadosAct;
    TextView numFallecidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datos_generales);

        numContagiados = findViewById(R.id.txtNumContagiados);
        numContagiados.setText("4.258.394");

        numContagiadosAct = findViewById(R.id.txtNumContagiadosAct);
        numContagiadosAct.setText("302.955");

        numFallecidos = findViewById(R.id.txtNumFallecidos);
        numFallecidos.setText("88.742");
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Scrapping.prueba(DatosGeneralesActivity.this);
    }
}
