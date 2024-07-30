package com.example.moviesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.moviesapp.databinding.ActivityDetailMovieBinding

class DetailMovieActivity : AppCompatActivity() {
    private val binding :ActivityDetailMovieBinding by lazy {
        ActivityDetailMovieBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}