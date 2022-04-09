package com.example.client.screens.product.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R

class ProductItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val avatar: ImageView? = itemView.findViewById(R.id.avatar)
    val name: TextView? = itemView.findViewById(R.id.name)
    val price: TextView? = itemView.findViewById(R.id.price)
    val rate: TextView = itemView.findViewById(R.id.rate)
}