package com.example.trabajopractico2.service;

import com.example.trabajopractico2.dto.LoginRequest;
import com.example.trabajopractico2.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostLoginService {

    String API_ROUTE = "api/login";

    @POST(API_ROUTE)
    Call<LoginResponse> login(@Body LoginRequest request);
}
