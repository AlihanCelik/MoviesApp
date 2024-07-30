package com.example.moviesapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.Adapter.ExploreMoviesAdapter
import com.example.moviesapp.Utils.MovieUtils
import com.example.moviesapp.databinding.ActivityAllMovieBinding

class AllMovieActivity : AppCompatActivity() {
    private val binding: ActivityAllMovieBinding by lazy {
        ActivityAllMovieBinding.inflate(layoutInflater)
    }
    private lateinit var movieAdapter: ExploreMoviesAdapter
    private var currentPage = 1
    private val totalPageCount = 25
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        movieAdapter = ExploreMoviesAdapter(arrayListOf())
        val layoutManager = GridLayoutManager(this, 3)
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.recyclerView.apply {
            adapter = movieAdapter
            this.layoutManager = layoutManager
            addOnScrollListener(object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    if (page <= totalPageCount) {
                        loadMoreMovies(page)
                    }
                }
            })
        }
        loadMoreMovies(currentPage)
    }

    private fun loadMoreMovies(page: Int) {
        MovieUtils.getMovies(page, { movies ->
            movieAdapter.addMovies(movies)
        }, { error ->
        })

    }
}