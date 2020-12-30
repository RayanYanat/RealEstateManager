package com.example.realestatemanager.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.database.EstateEntity
import kotlinx.android.synthetic.main.esate_item.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.EsateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.EsateViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.EsateViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class EsateViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemType = view.list_item_type
        val itemPrice = view.list_item_price
        val itemCity = view.list_item_city
      //  val itemMainPic = view.list_item_main_pic

        fun bind (data : EstateEntity){
            itemType.text = data.type
            itemPrice.text = data.price
            itemCity.text = data.city
        }
    }
}