package com.example.moviesapp.Utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.moviesapp.api.Data
import com.example.moviesapp.api.ExampleJson2KtKotlin
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

object MovieUtils {
    private const val BASE_URL = "https://moviesapi.ir/api/v1/movies"
    private val client = OkHttpClient()

    fun getMovies(page: Int, onSuccess: (List<Data>) -> Unit, onFailure: (Throwable) -> Unit) {
        val request = Request.Builder()
            .url("$BASE_URL?page=$page")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    onFailure(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.use { responseBody ->
                    val responseString = responseBody.string()
                    Log.d("MovieUtils", "API response: $responseString")

                    val moviesResponse = Gson().fromJson(responseString, ExampleJson2KtKotlin::class.java)
                    Handler(Looper.getMainLooper()).post {
                        onSuccess(moviesResponse.data)
                    }
                } ?: Handler(Looper.getMainLooper()).post {
                    onFailure(Exception("Response body is null"))
                }
            }
        })
    }
}
