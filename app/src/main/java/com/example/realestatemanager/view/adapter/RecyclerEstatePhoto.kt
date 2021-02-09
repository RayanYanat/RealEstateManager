package com.example.realestatemanager.view.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.realestatemanager.R
import kotlinx.android.synthetic.main.estate_image_item.view.*

class RecyclerEstatePhoto (private val listEstateImage: List<Uri>): RecyclerView.Adapter<RecyclerEstatePhoto.EstateImageViewHolder>() {

    private lateinit var  mData: List<Uri>

    fun setResults (data: List<Uri>) {
        mData = data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EstateImageViewHolder {
        return EstateImageViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.estate_image_item, parent, false))
    }

    override fun onBindViewHolder(holder: EstateImageViewHolder, position: Int) {
        val estateItem = mData[position]
        Log.d("TAG", "currentUri : $estateItem ")
        Glide.with(holder.itemView).load(estateItem).into(holder.estateImage)
    }

    override fun getItemCount(): Int {
        return if (listEstateImage.isNotEmpty()) listEstateImage.size else 0
    }

    class EstateImageViewHolder(view: View): RecyclerView.ViewHolder(view){
        val estateImage = view.image_estate_detail!!


    }
}