package com.example.moviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R

class DetailImgAdapter(private var imgList :MutableList<String>

) : RecyclerView.Adapter<DetailImgAdapter.DetailImgViewHolder>(){
    class DetailImgViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_detail_pic)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailImgViewHolder {
        return DetailImgViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_detail_img, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailImgViewHolder, position: Int) {
        val picture = imgList[position]
        Glide.with(holder.imageView.context)
            .load(picture)
            .into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return imgList.size
    }
}