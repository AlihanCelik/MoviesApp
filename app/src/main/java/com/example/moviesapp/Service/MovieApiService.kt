package com.example.moviesapp.Service

import com.example.moviesapp.api.DetailMovie
import com.example.moviesapp.api.ExampleJson2KtKotlin
import com.example.moviesapp.api.Genres
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movies")
    fun getMovies(@Query("page") page: Int): Call<ExampleJson2KtKotlin>

    @GET("genres")
    fun getGenres(): Call<List<Genres>>

    @GET("movies/{id}")
    fun getDetailMovie(@Path("id") id: Int): Call<DetailMovie>
}