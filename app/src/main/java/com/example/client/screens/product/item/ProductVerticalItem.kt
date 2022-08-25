package com.example.client.screens.product.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.models.product.ProductModel
import java.text.NumberFormat
import java.util.*

class ProductVerticalItem(
        private val context: Context,
        private val items: List<ProductModel>,
        private val onClickItem: (item: ProductModel) -> Unit,
) : RecyclerView.Adapter<ProductItemVerticalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemVerticalViewHolder {
        return ProductItemVerticalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_vertical, null))
    }

    override fun onBindViewHolder(viewholder: ProductItemVerticalViewHolder, position: Int) {
        viewholder.apply {
            val item: ProductModel = items[position]
            imvAvatar?.let {
                Glide.with(context).asBitmap().placeholder(R.drawable.ic_category_default).load(item.avatar).into(it)
            }
            tvProductName?.text = item.name
            tvProductPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.price)
            if (item.quantity > 0) {
                tvProductQuantity?.text = context.getString(R.string.text_product_quantity, item.quantity)
            } else {
                tvProductQuantity?.text = context.getString(R.string.text_product_quantity_0)
            }
            tvQuantity?.run {
                item.addToCart.let {
                    if (it > 0) {
                        visibility = View.VISIBLE
                        text = context.getString(R.string.text_cart_product_quantity, it)
                    } else visibility = View.GONE
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

class ProductItemVerticalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imvAvatar: ImageView? = itemView.findViewById(R.id.view_icon)
    val tvProductName: TextView? = itemView.findViewById(R.id.tv_name)
    val tvProductPrice: TextView? = itemView.findViewById(R.id.tv_price)
    val tvProductQuantity: TextView? = itemView.findViewById(R.id.tv_quantity)
    val tvQuantity: TextView? = itemView.findViewById(R.id.tv_cart_quantity)
}