package com.example.realestatemanager.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import kotlinx.android.synthetic.main.esate_item.view.*
import java.io.File

class RecyclerAdapter(private val listEstate: List<EstateEntity>, val listener: ItemClickListener) : RecyclerView.Adapter<RecyclerAdapter.EsateViewHolder>() {

   private lateinit var  mData: List<EstateEntity>

    fun setResults (data: List<EstateEntity>) {
       mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EsateViewHolder {
        return EsateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.esate_item,parent,false))
    }

    override fun onBindViewHolder(holder: EsateViewHolder, position: Int) {
        var estateItem = mData[position]

        var mainImage = estateItem.photo

        if (mainImage != null) {
            mainImage = mainImage.replace("[", "")
            mainImage = mainImage.replace("]", "")
            val listUriImage = mainImage.split(",")
            holder.itemMainPic.setImageURI(Uri.parse(listUriImage[0]))
        }

        holder.itemView.setOnClickListener(){
            listener.onItemClickListener(estateItem)
        }

        holder.itemCity.text = estateItem.city

        holder.itemPrice.text = estateItem.price.toString()

        holder.itemType.text = estateItem.type


       // Glide.with(holder.itemView).load(estateItem.photo).apply(RequestOptions().centerCrop()).into(holder.itemMainPic)
    }

    override fun getItemCount(): Int {
        return if (listEstate.isNotEmpty()) listEstate.size else 0
    }

    class EsateViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemType = view.list_item_type
        val itemPrice = view.list_item_price
        val itemCity = view.list_item_city
        val itemMainPic = view.list_item_main_pic

    }

    interface ItemClickListener{
        fun onItemClickListener(estate: EstateEntity)
    }
}