package com.example.moviesapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.api.Data

class ExploreMoviesAdapter(private var movies: MutableList<Data>) : RecyclerView.Adapter<ExploreMoviesAdapter.ExploreMoviesViewHolder>() {
    class ExploreMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_ly2_pic)
        val textView: TextView = itemView.findViewById(R.id.item_ly2_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreMoviesViewHolder {
        return ExploreMoviesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_ly2, parent, false)
        )
    }

    fun addMovies(newMovies: List<Data>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ExploreMoviesViewHolder, position: Int) {
        val movie = movies[position]
        if (movie.title.isNullOrEmpty()) {
            Log.d("ExploreMoviesAdapter", "aaaaaaaaaaa")
        }
        Log.d("ExploreMoviesAdapter", "bbbbbbbbb")
        holder.textView.text = movie.title
        Glide.with(holder.itemView.context).load(movie.poster).placeholder(R.drawable.wide)
            .error(R.drawable.wide3).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        Log.d("ExploreMoviesAdapter", "getItemCount called, item count: ${movies.size}")
        return movies.size
    }
}
