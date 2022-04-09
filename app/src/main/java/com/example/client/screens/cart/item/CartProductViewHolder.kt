package com.example.client.screens.cart.item

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R

class CartProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var tvProductName:TextView? = itemView.findViewById(R.id.tv_product_name)
    var tvPrice:TextView? = itemView.findViewById(R.id.tv_price)
    var btnMinus: ImageButton? = itemView.findViewById(R.id.btn_minus)
    var btnPlus: ImageButton? = itemView.findViewById(R.id.btn_plus)
    var tvQuantity: TextView? =  itemView.findViewById(R.id.tv_quantity)
}