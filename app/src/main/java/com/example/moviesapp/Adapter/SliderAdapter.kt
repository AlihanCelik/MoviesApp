package com.example.moviesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Entities.PictureEntities
import com.example.moviesapp.R

class SliderAdapter(private var context: Context,
                    private var sliderAdapter :MutableList<PictureEntities>

) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>(){
    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_ly1_pic)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ly1, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val picture = sliderAdapter[position]
        Glide.with(context)
            .load(picture.image)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return sliderAdapter.size
    }
}