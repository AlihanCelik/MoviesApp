package com.example.moviesapp.Utils


import com.example.moviesapp.Retrofit.RetrofitClient
import com.example.moviesapp.api.Data
import com.example.moviesapp.api.ExampleJson2KtKotlin
import com.example.moviesapp.api.Genres
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieUtils {
    companion object {
        fun getMovies(page: Int, callback: (List<Data>) -> Unit, errorCallback: (Throwable) -> Unit) {
            RetrofitClient.api.getMovies(page).enqueue(object : Callback<ExampleJson2KtKotlin> {
                override fun onResponse(call: Call<ExampleJson2KtKotlin>, response: Response<ExampleJson2KtKotlin>) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            callback(it)
                        } ?: errorCallback(Throwable("Data is null"))
                    } else {
                        errorCallback(Throwable("Response not successful"))
                    }
                }

                override fun onFailure(call: Call<ExampleJson2KtKotlin>, t: Throwable) {
                    errorCallback(t)
                }
            })
        }

        fun getGenres(callback: (List<Genres>) -> Unit, errorCallback: (Throwable) -> Unit) {
            RetrofitClient.api.getGenres().enqueue(object : Callback<List<Genres>> {
                override fun onResponse(call: Call<List<Genres>>, response: Response<List<Genres>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { genresList ->
                            val allGenre = Genres(id = 0, name = "All")
                            val updatedGenresList = listOf(allGenre) + genresList
                            callback(updatedGenresList)
                        } ?: errorCallback(Throwable("Data is null"))
                    } else {
                        errorCallback(Throwable("Response not successful"))
                    }
                }

                override fun onFailure(call: Call<List<Genres>>, t: Throwable) {
                    errorCallback(t)
                }
            })
        }
    }
}
