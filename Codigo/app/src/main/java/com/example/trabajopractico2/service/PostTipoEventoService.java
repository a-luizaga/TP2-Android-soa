package com.example.trabajopractico2.service;

import com.example.trabajopractico2.dto.EventoRequest;
import com.example.trabajopractico2.dto.EventoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PostTipoEventoService {

    String API_ROUTE = "api/event";

    @POST(API_ROUTE)
    Call<EventoResponse> event(@Header("Token") String Token, @Body EventoRequest request);
}
