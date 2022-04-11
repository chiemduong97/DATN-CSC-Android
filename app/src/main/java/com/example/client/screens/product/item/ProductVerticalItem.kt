package com.example.client.screens.product.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.models.cart.CartProductModel
import com.example.client.models.product.ProductModel
import java.text.NumberFormat
import java.util.*

class ProductVerticalItem(var context: Context, var items: List<ProductModel>, var cartProducts: List<CartProductModel>, var onClickItem: (item: ProductModel) -> Unit) : RecyclerView.Adapter<ProductItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_vertical, null))
    }

    override fun onBindViewHolder(viewholder: ProductItemViewHolder, position: Int) {
        viewholder.apply {
            val item: ProductModel = items[position]
            imvAvatar?.let {
                Glide.with(context).asBitmap().placeholder(R.drawable.subject_default).load(item.avatar).into(it)
            }
            tvProductName?.text = item.name
            tvProductPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.price)
            if (item.quantity > 0) {
                tvProductQuantity?.text = context.getString(R.string.text_product_quantity).replace("%s", item.quantity.toString())
            } else {
                tvProductPrice?.text = context.getString(R.string.text_product_quantity_0)
            }
            rllQuantity?.visibility = View.GONE
            cartProducts.forEach {
                if (it.product.id == item.id && it.quantity > 0) {
                    tvQuantity?.run {
                        rllQuantity?.visibility = View.VISIBLE
                        text = context.getString(R.string.text_cart_product_quantity).replace("%s", it.quantity.toString())
                    }
                }
            }
            itemView.setOnClickListener {
                onClickItem.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}