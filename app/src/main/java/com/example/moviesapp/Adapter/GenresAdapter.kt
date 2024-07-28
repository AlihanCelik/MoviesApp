package com.example.moviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.api.Genres


class GenresAdapter(private var genres: MutableList<Genres>

) : RecyclerView.Adapter<GenresAdapter.GenresViewHolder>(){
    class GenresViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.item_category_txt)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        return GenresViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
        )
    }
    fun addGenres(newgenres: List<Genres>) {
        genres.addAll(newgenres)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {

        holder.textView.text=genres[position].name
    }

    override fun getItemCount(): Int {
        return genres.size
    }
}