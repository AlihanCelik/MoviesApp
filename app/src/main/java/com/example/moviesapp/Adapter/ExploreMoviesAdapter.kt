package com.example.moviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.api.Data

class ExploreMoviesAdapter(private var movies: MutableList<Data>

) : RecyclerView.Adapter<ExploreMoviesAdapter.BestMoviesViewHolder>(){
    class BestMoviesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_ly2_pic)
        val textView:TextView=itemView.findViewById(R.id.item_ly2_txt)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestMoviesViewHolder {
        return BestMoviesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ly2, parent, false)
        )
    }
    fun addMovies(newMovies: List<Data>) {
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BestMoviesViewHolder, position: Int) {
        val movie = movies[position]
        Toast.makeText(holder.itemView.context,movie.title,Toast.LENGTH_SHORT).show()
        holder.textView.text = movie.title
        Glide.with(holder.itemView.context).load(movie.poster).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}