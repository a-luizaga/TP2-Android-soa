package com.example.trabajopractico2.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.trabajopractico2.R;
import com.example.trabajopractico2.utilidades.Utilidades;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import android.graphics.Color;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DosisUnoActivity extends AppCompatActivity
{
    LineChartView lineChartView;
    String[] axisData = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio"};
    int[] yAxisData = {1, 2, 7, 15, 21, 30};

    private SensorManager sensorManager;
    private Sensor sensorAcelerometro;
    private SensorEventListener sensorEventListener;

    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private String token;
    private String tokenRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dosisuno);

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
        yAxis.setName("Porcentaje de argentinos con 1 dosis contra covid-19");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 50;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        token = getIntent().getStringExtra("Token");
        tokenRefresh = getIntent().getStringExtra("TokenRefresh");
    }

    @Override
    protected void onResume(){
        super.onResume();

        sensorAcelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensorAcelerometro != null){

            sensorEventListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];
                    mAccelLast = mAccelCurrent;
                    mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
                    float delta = mAccelCurrent - mAccelLast;
                    mAccel = mAccel * 0.9f + delta;
                    if (mAccel > 12) {

                        Toast.makeText(DosisUnoActivity.this, "Shake event detected", Toast.LENGTH_SHORT).show();
                        Utilidades.registrarEventoRetrofit("TEST","Agitar Detectado", "Se realizo cambio de grafico por agitar el telefono", token);
                        Intent intent = new Intent(DosisUnoActivity.this, DosisDosActivity.class);
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
        sensorManager.registerListener(sensorEventListener, sensorAcelerometro, 2000*1000);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(sensorEventListener);
    }
}