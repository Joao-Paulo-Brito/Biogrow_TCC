package com.example.biogrow.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Endereço padrão para acessar o servidor Uvicorn local
    private const val BASE_URL = "http://10.0.2.2:8000/"

    val api: API by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(API::class.java)
    }
}