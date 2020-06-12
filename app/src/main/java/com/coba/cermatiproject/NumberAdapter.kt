package com.coba.cermatiproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.coba.cermatiproject.model.Item
import com.coba.cermatiproject.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NumberAdapter(val activity: MainActivity, var list:MutableList<Item>) : RecyclerView.Adapter<NumberAdapter.ItemViewHolder>() {
    val TAG = "NumberAdapter"
    private var mTempList: MutableList<Item> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_list,parent,false),activity)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.filledComponent(list[position])
    }

    class ItemViewHolder(v: View,val ctx: MainActivity) : RecyclerView.ViewHolder(v){
        val textViewItem = v.findViewById<TextView>(R.id.textViewItem)
        val imageViewItem = v.findViewById<ImageView>(R.id.imageView)

        //filled component item view from list object
        fun filledComponent(item: Item){
            textViewItem.text = item.login
            Glide
                .with(ctx)
                .load(item.avatarUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageViewItem);
        }
    }
}