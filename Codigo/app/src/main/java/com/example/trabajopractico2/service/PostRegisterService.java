package com.example.trabajopractico2.service;

import com.example.trabajopractico2.dto.RegisterRequest;
import com.example.trabajopractico2.dto.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostRegisterService {

    String API_ROUTE = "api/register";

    @POST(API_ROUTE)
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
