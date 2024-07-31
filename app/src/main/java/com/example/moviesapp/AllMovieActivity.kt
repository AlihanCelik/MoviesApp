package com.example.moviesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.Adapter.ExploreMoviesAdapter
import com.example.moviesapp.RcwScrollListener.EndlessRecyclerViewScrollListener
import com.example.moviesapp.Utils.MovieUtils
import com.example.moviesapp.databinding.ActivityAllMovieBinding

class AllMovieActivity : AppCompatActivity() {
    private val binding: ActivityAllMovieBinding by lazy {
        ActivityAllMovieBinding.inflate(layoutInflater)
    }
    private lateinit var movieAdapter: ExploreMoviesAdapter
    private var currentPage = 1
    private val totalPageCount = 25
    var category:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        movieAdapter = ExploreMoviesAdapter(arrayListOf())
        val layoutManager = GridLayoutManager(this, 3)
        binding.backBtn.setOnClickListener {
            finish()
        }
        category=intent.getStringExtra("category")
        if(category==null || category=="All"){
            binding.categoryTxt.text = "All"
        }else{
            binding.categoryTxt.text = category
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
            if(category==null || category=="All"){
                movieAdapter.addMovies(movies)
            }else{
                val adventureMovies = movies.filter { it.genres.contains(category) }
                movieAdapter.addMovies(adventureMovies)
            }

        }, { error ->
        })

    }
}