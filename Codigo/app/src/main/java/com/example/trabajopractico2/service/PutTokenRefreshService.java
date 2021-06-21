package com.example.trabajopractico2.service;

import com.example.trabajopractico2.dto.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface PutTokenRefreshService {

    String API_ROUTE = "api/refresh";

    @PUT(API_ROUTE)
    Call<TokenResponse> refresh(@Header("Token_Refresh") String Token_Refresh);
}
