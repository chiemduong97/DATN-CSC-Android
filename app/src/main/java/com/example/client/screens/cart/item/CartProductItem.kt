package com.example.client.screens.cart.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.cart.CartProductModel
import java.text.NumberFormat
import java.util.*

class CartProductItem(var context: Context?,var listProduct: List<CartProductModel>, var onPlus:(item:CartProductModel) -> Unit, var onMinus:(item:CartProductModel) -> Unit): RecyclerView.Adapter<CartProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_product, null))
    }

    override fun onBindViewHolder(viewHolder: CartProductViewHolder, position: Int) {
        viewHolder.apply {
            val item = listProduct[position]
            tvProductName?.text = item.product.name
            tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getPrice())
            tvQuantity?.text = item.quantity.toString()
            btnPlus?.setOnClickListener {
                onPlus.invoke(item)
                tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getPrice())
                tvQuantity?.text = item.quantity.toString()
            }
            btnMinus?.setOnClickListener {
                onMinus.invoke(item)
                tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getPrice())
                tvQuantity?.text = item.quantity.toString()
            }
        }
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

}