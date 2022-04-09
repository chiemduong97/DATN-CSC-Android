package com.example.client.screens.product.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.models.product.ProductModel
import java.text.NumberFormat
import java.util.*

class ProductVerticalItem(private var context: Context, private var items: List<ProductModel>, private var onClickItem: (item: ProductModel) -> Unit) : RecyclerView.Adapter<ProductItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_vertical, null))
    }

    override fun onBindViewHolder(viewholder: ProductItemViewHolder, position: Int) {
        viewholder.apply {
            val item: ProductModel = items[position]
            avatar?.let {
                Glide.with(context).asBitmap().placeholder(R.drawable.subject_default).load(item.avatar).into(it)
            }
            name?.text = item.name ?: "Tên sản phẩm"
            price?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.price
                    ?: 1000000.0)
            rate.text = "5/5"
            itemView.setOnClickListener {
                onClickItem.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}