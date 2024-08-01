package com.example.moviesapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.AllMovieActivity
import com.example.moviesapp.DetailMovieActivity
import com.example.moviesapp.R
import com.example.moviesapp.api.Data

class FavoriteAdapter(private var movies: MutableList<Data>

) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(){
    class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.item_fav_title)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_myfavorite, parent, false)
        )
    }
    fun addGenres(newgenres: List<Data>) {
        movies.addAll(newgenres)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

        holder.textView.text=movies[position].title
        holder.itemView.findViewById<LinearLayout>(R.id.item_fav_l).setOnClickListener {
            val intent=Intent(holder.itemView.context, DetailMovieActivity::class.java)
            intent.putExtra("movie_id",movies[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}