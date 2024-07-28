package com.example.moviesapp.Utils


import com.example.moviesapp.Retrofit.RetrofitClient
import com.example.moviesapp.api.Data
import com.example.moviesapp.api.ExampleJson2KtKotlin
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
    }
}
