package com.example.moviesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.moviesapp.Adapter.DetailImgAdapter
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
    private lateinit var imgAdapter: DetailImgAdapter
    private var fav=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }
        var firebaseHelper=FirebaseHelper(this)
        var movie_id=intent.getIntExtra("movie_id",-1)
        if (movie_id != -1) {
            getDetailMovie(movie_id)
        } else {
            Toast.makeText(this, "Film bulunamadı...", Toast.LENGTH_SHORT).show()
            finish()
        }
        firebaseHelper.isFavoriteMovie(movie_id){isFavorite->
            if(isFavorite){
                fav=true
                binding.favImg.setImageResource(R.drawable.baseline_favorite_24)

            }else{
                fav=false
                binding.favImg.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }
        binding.favBtn.setOnClickListener {
            if(!fav){
                firebaseHelper.AddFavoleriMovies(movie_id){success->
                    if (success) {
                        fav=true
                        binding.favImg.setImageResource(R.drawable.baseline_favorite_24)
                    }
                }
            }else{
                firebaseHelper.removeFavoriteMovie(movie_id){success->
                    if (success) {
                        fav=false
                        binding.favImg.setImageResource(R.drawable.baseline_favorite_border_24)
                    }
                }
            }
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
        Glide.with(this).load(detailMovie.poster)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder).into(binding.imageView)
        val genresList = detailMovie.genres.map { genreName -> Genres(name = genreName) }.toMutableList()
        genresAdapter= GenresAdapter(genresList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = genresAdapter

        imgAdapter= DetailImgAdapter(detailMovie.images)
        binding.recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView2.adapter = imgAdapter

    }
}
