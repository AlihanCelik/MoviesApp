package com.example.moviesapp.Retrofit

import com.example.moviesapp.Service.MovieApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient {
    private const val BASE_URL="https://moviesapi.ir/api/v1/"
    val instance:Retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
    }
    val api :MovieApiService by lazy {
        instance.create(MovieApiService::class.java)
    }
}