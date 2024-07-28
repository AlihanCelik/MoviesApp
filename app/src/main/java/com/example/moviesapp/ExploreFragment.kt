package com.example.moviesapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.moviesapp.databinding.FragmentExploreBinding
import kotlin.math.abs

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    private lateinit var bestMovieAdapter: ExploreMoviesAdapter
    private lateinit var genresAdapter: GenresAdapter

    private var currentPage = 1
    private val totalPages = 25
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
        getMovies(1, bestMovieAdapter)

        binding.recyclerView2.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView2.adapter = genresAdapter
        getGenres()



    }
    private fun getMovies(page: Int, adapter: ExploreMoviesAdapter) {
        MovieUtils.getMovies(page, { movies ->
            adapter.addMovies(movies)
            _binding?.progressBar1?.visibility = View.GONE
        }, { error ->
            Log.e("ExploreFragment", "Error loading movies", error)
            _binding?.progressBar1?.visibility = View.GONE
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