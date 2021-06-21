package com.example.trabajopractico2.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trabajopractico2.R;
import com.example.trabajopractico2.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class DosisDosActivity extends AppCompatActivity
{
    LineChartView lineChartView;
    String[] axisData = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"};
    int[] yAxisData = {0, 1, 2, 2, 6, 8};

    private SensorManager sensorManager;
    private Sensor sensorProximidad;
    private SensorEventListener sensorEventListener;
    private String distancia;

    private String token;
    private String tokenRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosisdos);

        lineChartView = findViewById(R.id.chart);

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }

        List lines = new ArrayList();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
        yAxis.setName("Porcentaje de argentinos con 2 dosis contra covid-19");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 20;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

        // Primero se obtiene una referencia al servicio del sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        token = getIntent().getStringExtra("Token");
        tokenRefresh = getIntent().getStringExtra("TokenRefresh");
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorProximidad =sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensorProximidad != null){

            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {

                    if(event.values[0]<sensorProximidad.getMaximumRange()){

                        Toast.makeText(DosisDosActivity.this, "Proximidad detectada", Toast.LENGTH_SHORT).show();
                        Utilidades.registrarEventoRetrofit("TEST","Proximidad Detectada", "Se realizo cambio de grafico por proximidad de objeto", token);
                        Intent intent = new Intent(DosisDosActivity.this, DosisUnoActivity.class);
                        intent.putExtra("Token", token);
                        intent.putExtra("TokenRefresh", tokenRefresh);
                        startActivity(intent);
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };
        }

        sensorManager.registerListener(sensorEventListener, sensorProximidad, 2000*1000);
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}