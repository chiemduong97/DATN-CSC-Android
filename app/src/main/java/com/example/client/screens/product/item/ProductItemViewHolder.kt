package com.example.client.screens.product.item

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R

class ProductItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imvAvatar: ImageView? = itemView.findViewById(R.id.imv_avatar)
    val tvProductName: TextView? = itemView.findViewById(R.id.tv_product_name)
    val tvProductPrice: TextView? = itemView.findViewById(R.id.tv_product_price)
    val tvProductQuantity: TextView? = itemView.findViewById(R.id.tv_product_quantity)
    val tvQuantity: TextView? = itemView.findViewById(R.id.tv_cart_quantity)
    val rllQuantity: RelativeLayout? = itemView.findViewById(R.id.rll_cart_quantity)
}