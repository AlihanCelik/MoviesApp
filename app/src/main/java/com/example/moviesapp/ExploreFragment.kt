package com.example.moviesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.moviesapp.Adapter.ExploreMoviesAdapter
import com.example.moviesapp.Adapter.GenresAdapter
import com.example.moviesapp.Adapter.SliderAdapter
import com.example.moviesapp.Entities.PictureEntities
import com.example.moviesapp.Utils.MovieUtils
import com.example.moviesapp.api.Data
import com.example.moviesapp.databinding.FragmentExploreBinding
import kotlin.math.abs

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    private lateinit var bestMovieAdapter: ExploreMoviesAdapter
    private lateinit var moviesAdapter: ExploreMoviesAdapter
    private lateinit var searchAdapter: ExploreMoviesAdapter
    private lateinit var genresAdapter: GenresAdapter

    private var currentPage = 1
    private val totalPages = 25
    private var searchQuery = ""
    private val runnable = Runnable {
        val nextItem = (binding.viewPager.currentItem + 1) % binding.viewPager.adapter!!.itemCount
        binding.viewPager.currentItem = nextItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        handler = Handler(Looper.getMainLooper())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bestMovieAdapter= ExploreMoviesAdapter(arrayListOf())
        genresAdapter= GenresAdapter(arrayListOf())
        moviesAdapter= ExploreMoviesAdapter(arrayListOf())

        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar1.visibility = View.VISIBLE
        binding.progressBar2.visibility = View.VISIBLE
        binding.progressBar3.visibility = View.VISIBLE

        val pictureList = mutableListOf(
            PictureEntities("https://github.com/worldsat/project155/blob/main/wide3.jpg?raw=true"),
            PictureEntities("https://github.com/worldsat/project155/blob/main/wide1.jpg?raw=true"),
            PictureEntities("https://github.com/worldsat/project155/blob/main/wide.jpg?raw=true")
        )

        val sliderAdapter = SliderAdapter(requireContext(), pictureList)
        binding.viewPager.clipToPadding=false
        binding.viewPager.offscreenPageLimit=3
        binding.viewPager.adapter = sliderAdapter
        binding.viewPager.clipChildren=false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS

        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding.viewPager.setPageTransformer(transformer)
        binding.viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable,30000)
                if (position == sliderAdapter.itemCount - 1) {
                    handler.postDelayed({
                        binding.viewPager.currentItem = 0
                    }, 30000)
                }
            }
        })
        binding.viewPager.post {
            if (pictureList.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE

            }
            binding.viewPager.currentItem = pictureList.size / 2
        }

        binding.recyclerView1.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView1.adapter = bestMovieAdapter
        getMovies(1, bestMovieAdapter,binding.progressBar1)
        binding.recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView2.adapter = genresAdapter
        getGenres()
        binding.recyclerView3.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView3.adapter = moviesAdapter
        getMovies(2, moviesAdapter,binding.progressBar3)
        binding.seeAllBtn.setOnClickListener {
            val intent=Intent(context,AllMovieActivity::class.java)
            startActivity(intent)
        }
        binding.searchRecyclerView.layoutManager = GridLayoutManager(context,3)
        binding.searchRecyclerView.adapter = searchAdapter

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString().trim()
                if (searchQuery.isNotEmpty()) {
                    filterMoviesByTitle(searchQuery)
                } else {
                    searchAdapter.clearMovies()
                    binding.searchRecyclerView.visibility = View.GONE
                }
            }
        })



    }
    private fun filterMoviesByTitle(query: String, movies: List<Data>? = null) {
        val filteredMovies = movies?.filter { it.title?.contains(query, ignoreCase = true) == true }
        filteredMovies?.let {
            searchAdapter.updateMovies(it)
            binding.searchRecyclerView.visibility = View.VISIBLE
        }
    }


    private fun getMovies(page: Int, adapter: ExploreMoviesAdapter,progressBar: ProgressBar) {
        MovieUtils.getMovies(page, { movies ->
            adapter.addMovies(movies)
            progressBar.visibility = View.GONE
        }, { error ->
            Log.e("ExploreFragment", "Error loading movies", error)
            progressBar.visibility = View.GONE
        })
    }
    private fun getGenres(){
        MovieUtils.getGenres({ genresList ->
            genresAdapter.addGenres(genresList)
            _binding?.progressBar2?.visibility = View.GONE
        }, { error ->

            Log.e("ExploreFragment", "Error: ${error.message}")
            _binding?.progressBar2?.visibility = View.GONE
        })
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,30000)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}