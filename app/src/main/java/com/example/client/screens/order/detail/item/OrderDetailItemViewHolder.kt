package com.example.client.screens.order.detail.item

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R

class OrderDetailItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvQuantity: TextView? = itemView.findViewById(R.id.tv_quantity)
    val tvProductName: TextView? = itemView.findViewById(R.id.tv_product_name)
    val tvPrice: TextView? = itemView.findViewById(R.id.tv_price)
}