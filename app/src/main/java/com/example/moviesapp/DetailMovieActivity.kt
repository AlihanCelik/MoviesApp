package com.example.moviesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviesapp.Adapter.GenresAdapter
import com.example.moviesapp.Utils.MovieUtils
import com.example.moviesapp.api.DetailMovie
import com.example.moviesapp.api.Genres
import com.example.moviesapp.databinding.ActivityDetailMovieBinding

class DetailMovieActivity : AppCompatActivity() {
    private val binding :ActivityDetailMovieBinding by lazy {
        ActivityDetailMovieBinding.inflate(layoutInflater)
    }
    private lateinit var genresAdapter: GenresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }

        var movie_id=intent.getIntExtra("movie_id",-1)
        if (movie_id != -1) {
            getDetailMovie(movie_id)
        } else {
            Toast.makeText(this, "Film bulunamadı...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun getDetailMovie(movieId: Int) {
        MovieUtils.getDetailMovie(movieId, { detailMovie ->
            runOnUiThread {
                displayMovieDetails(detailMovie)
            }
        }, { error ->
            runOnUiThread {
                Toast.makeText(this, "Film detayları alınamadı", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayMovieDetails(detailMovie: DetailMovie) {
        binding.textView.text = detailMovie.title
        binding.detailImdb.text = detailMovie.imdbRating
        binding.detailTime.text= detailMovie.runtime
        binding.detailDate.text=detailMovie.released
        binding.detailActors.text=detailMovie.actors
        binding.detailAwards.text=detailMovie.awards
        binding.detailDirctor.text=detailMovie.director
        binding.detailSummary.text=detailMovie.plot
        binding.detailCountry.text=detailMovie.country
        binding.detailWriter.text=detailMovie.writer
        Glide.with(this).load(detailMovie.poster).placeholder(R.drawable.wide)
            .error(R.drawable.wide3).into(binding.imageView)
        val genresList = detailMovie.genres.map { genreName -> Genres(name = genreName) }.toMutableList()
        genresAdapter= GenresAdapter(genresList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = genresAdapter
    }
}
