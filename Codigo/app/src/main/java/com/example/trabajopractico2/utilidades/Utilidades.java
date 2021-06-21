package com.example.trabajopractico2.utilidades;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.view.View;

import com.example.trabajopractico2.activity.MenuActivity;
import com.example.trabajopractico2.dto.EventoRequest;
import com.example.trabajopractico2.dto.EventoResponse;
import com.example.trabajopractico2.dto.TokenResponse;
import com.example.trabajopractico2.service.PostTipoEventoService;
import com.example.trabajopractico2.service.PutTokenRefreshService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utilidades {

    public static boolean hayConexionInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if (capabilities != null) {
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
        }

        return false;
    }

    public static int estadoBateria(MenuActivity ma)
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = ma.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        return (int)batteryPct;
    }

    public static void registrarEventoRetrofit(String env, String type_events, String description, String token){
        String url = "http://so-unlam.net.ar/api/";

        EventoRequest request = new EventoRequest();
        request.setEnv(env);
        request.setDescription(description);
        request.setType_events(type_events);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostTipoEventoService service = retrofit.create(PostTipoEventoService.class);

        Call<EventoResponse> call = service.event(token, request);

        call.enqueue(new Callback<EventoResponse>() {
            @Override
            public void onResponse(Call<EventoResponse> call, Response<EventoResponse> response) { }

            @Override
            public void onFailure(Call<EventoResponse> call, Throwable t) { }
        });
    }

    public static void actualizarTokenRetrofit(String tokenRefresh){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VariablesGlobales.servidor)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PutTokenRefreshService service = retrofit.create(PutTokenRefreshService.class);

        Call<TokenResponse> call = service.refresh(tokenRefresh);

        call.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                VariablesGlobales.tokenServidor = response.body().getToken();
                VariablesGlobales.tokenRefreshServidor = response.body().getToken_refresh();
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) { }
        });
    }
}


