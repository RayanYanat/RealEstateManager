package com.example.realestatemanager.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import kotlinx.android.synthetic.main.esate_item.view.*

class RecyclerAdapter(private val listEstate: List<EstateEntity>) : RecyclerView.Adapter<RecyclerAdapter.EsateViewHolder>() {

   private lateinit var  mData: List<EstateEntity>

    fun setResults (data: List<EstateEntity>) {
       mData = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.EsateViewHolder {
        return EsateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.esate_item,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.EsateViewHolder, position: Int) {
        var estateItem = mData[position]

        holder.itemCity.text = estateItem.city

        holder.itemPrice.text = estateItem.price

        holder.itemType.text = estateItem.type
    }

    override fun getItemCount(): Int {
        return if (listEstate.isNotEmpty()) listEstate.size else 0
    }

    class EsateViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemType = view.list_item_type
        val itemPrice = view.list_item_price
        val itemCity = view.list_item_city
      //  val itemMainPic = view.list_item_main_pic

        fun bind(data: EstateEntity){
            itemType.text = data.type
            itemPrice.text = data.price
            itemCity.text = data.city
        }
    }
}