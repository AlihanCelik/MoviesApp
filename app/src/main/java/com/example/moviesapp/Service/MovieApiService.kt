package com.example.moviesapp.Service

import com.example.moviesapp.api.ExampleJson2KtKotlin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movies")
    fun getMovies(@Query("page") page: Int): Call<ExampleJson2KtKotlin>
}