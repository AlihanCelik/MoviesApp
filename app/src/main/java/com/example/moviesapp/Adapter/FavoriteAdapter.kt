package com.example.moviesapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.AllMovieActivity
import com.example.moviesapp.DetailMovieActivity
import com.example.moviesapp.Entities.FavMovies
import com.example.moviesapp.R
import com.example.moviesapp.api.Data
import com.example.moviesapp.api.DetailMovie

class FavoriteAdapter(private var movies: MutableList<DetailMovie>

) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>(){
    class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_fav_pic)
        val textView1: TextView = itemView.findViewById(R.id.item_fav_title)
        val textView2: TextView = itemView.findViewById(R.id.item_fav_imdb)
        val textView3: TextView = itemView.findViewById(R.id.item_fav_time)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_myfavorite, parent, false)
        )
    }
    fun addFav(newgenres: List<DetailMovie>) {
        movies.addAll(newgenres)
        notifyDataSetChanged()
    }
    fun clearFav() {
        movies.clear()
        notifyDataSetChanged()
    }

    // Update existing movies with new list
    fun updateFav(newMovies: List<DetailMovie>) {
        clearFav()
        addFav(newMovies)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.textView1.text = movies[position].title
        holder.textView2.text = movies[position].imdbRating
        holder.textView3.text = movies[position].runtime
        Glide.with(holder.itemView.context).load(movies[position].poster)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder).into(holder.imageView)
        holder.itemView.findViewById<LinearLayout>(R.id.item_fav_l).setOnClickListener {
            val intent=Intent(holder.itemView.context,DetailMovieActivity::class.java)
            intent.putExtra("movie_id",movies[position].id)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }
}