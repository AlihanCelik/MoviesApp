package com.example.moviesapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.moviesapp.Adapter.ExploreMoviesAdapter
import com.example.moviesapp.Adapter.SliderAdapter
import com.example.moviesapp.Entities.PictureEntities
import com.example.moviesapp.Utils.MovieUtils
import com.example.moviesapp.databinding.FragmentExploreBinding
import kotlin.math.abs

class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private lateinit var handler: Handler
    private lateinit var BestMovieAdapter: ExploreMoviesAdapter

    private var currentPage = 1
    private val totalPages = 25
    private val runnable = Runnable {
        binding.viewPager.currentItem=binding.viewPager.currentItem+1
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

        val pictureList = mutableListOf(
            PictureEntities("https://github.com/worldsat/project155/blob/main/wide3.jpg?raw=true"),
            PictureEntities("https://github.com/worldsat/project155/blob/main/wide1.jpg?raw=true"),
            PictureEntities("https://github.com/worldsat/project155/blob/main/wide.jpg?raw=true")
        )
        val adapter = SliderAdapter(requireContext(), pictureList)
        binding.viewPager.clipToPadding=false
        binding.viewPager.offscreenPageLimit=3
        binding.viewPager.adapter = adapter
        binding.viewPager.clipChildren=false
        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_ALWAYS
        binding.progressBar.visibility = View.VISIBLE
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
            }
        })
        binding.viewPager.post {
            if (pictureList.isNotEmpty()) {
                binding.progressBar.visibility = View.GONE

            }
            binding.viewPager.currentItem = pictureList.size / 2
        }

        BestMovieAdapter= ExploreMoviesAdapter(arrayListOf())
        binding.recyclerView1.adapter=BestMovieAdapter
        getMovies(1,BestMovieAdapter)


    }
    private fun getMovies(page: Int, adapter: ExploreMoviesAdapter) {
        MovieUtils.getMovies(page, { movies ->
            Toast.makeText(requireContext(),movies.size, Toast.LENGTH_SHORT).show()
            adapter.addMovies(movies)
        }, { error ->
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