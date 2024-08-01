package com.example.moviesapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.Adapter.ExploreMoviesAdapter
import com.example.moviesapp.Adapter.FavoriteAdapter
import com.example.moviesapp.Entities.FavMovies
import com.example.moviesapp.Utils.MovieUtils
import com.example.moviesapp.api.Data
import com.example.moviesapp.api.DetailMovie
import com.example.moviesapp.databinding.FragmentExploreBinding
import com.example.moviesapp.databinding.FragmentFavoritesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favAdapter:FavoriteAdapter
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val favoriteMovies = arrayListOf<DetailMovie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favAdapter=FavoriteAdapter(arrayListOf())
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterMovies(s.toString())
            }
        })

        binding.favRecyclerView.adapter=favAdapter
        binding.favRecyclerView.layoutManager = GridLayoutManager(context, 2)
        database.child("Users").child(auth.currentUser!!.uid).child("FavoriteFilms").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteMovies.clear()
                for(ds in snapshot.children ){
                    val id=ds.getValue(FavMovies::class.java)
                    val movie_id= id?.Film_id
                    if(movie_id!=null){
                        MovieUtils.getMovieById(movie_id.toString()) { movie ->
                            if (movie != null) {
                                favoriteMovies.add(movie)
                                favAdapter.updateFav(favoriteMovies)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }
    private fun filterMovies(query: String) {
        val filteredList = favoriteMovies.filter {
            it.title!!.contains(query, ignoreCase = true)
        }
        favAdapter.updateFav(filteredList)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}