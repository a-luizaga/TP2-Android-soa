package com.example.trabajopractico2.utilidades;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Scrapping {

    public static final String url = "https://www.lanacion.com.ar/sociedad/en-detalle-infectados-fallecidos-coronavirus-argentina-nid2350330/";

    public static void prueba(Activity act){

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Toast.makeText(act,"Me conecte a la URL", Toast.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
