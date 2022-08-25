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
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import java.text.NumberFormat
import java.util.*

class ProductHorizontalItem(
        private val context: Context,
        private val items: List<ProductModel>,
        private val category: CategoryModel,
        private val onClickItem: (item: ProductModel) -> Unit,
        private val onAddToCart: (item: ProductModel) -> Unit,
        private val onSeeMore: (category: CategoryModel) -> Unit
) : RecyclerView.Adapter<ProductItemHorizontalViewHolder>() {
    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_MORE = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItemHorizontalViewHolder {
        return if (viewType == TYPE_ITEM) ProductItemHorizontalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_horizontal, null))
        else ProductSeeMoreItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product_see_more, null))

    }

    override fun onBindViewHolder(viewholder: ProductItemHorizontalViewHolder, position: Int) {
        viewholder.apply {
            when (getItemViewType(position)) {
                TYPE_ITEM -> {
                    val item: ProductModel = items[position]
                    imvAvatar?.let {
                        Glide.with(context).asBitmap().placeholder(R.drawable.ic_category_default).load(item.avatar).into(it)
                    }
                    tvName?.text = item.name
                    tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.price)
                    if (item.quantity > 0) {
                        tvQuantity?.text = context.getString(R.string.text_product_quantity, item.quantity)
                    } else {
                        tvQuantity?.text = context.getString(R.string.text_product_quantity_0)
                    }

                    tvAddToCart?.setOnClickListener {
                        onAddToCart.invoke(item)
                    }

                    itemView.setOnClickListener {
                        onClickItem.invoke(item)
                    }
                }
                TYPE_MORE -> {
                    (this as ProductSeeMoreItemViewHolder).apply {
                        tvDescription?.text = context.getString(R.string.product_see_more, category.name)

                    }
                    itemView.setOnClickListener {
                        onSeeMore.invoke(category)
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size - 1) {
            TYPE_MORE
        } else {
            TYPE_ITEM
        }
    }

}

open class ProductItemHorizontalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imvAvatar: ImageView? = itemView.findViewById(R.id.view_icon)
    val tvName: TextView? = itemView.findViewById(R.id.tv_name)
    val tvPrice: TextView? = itemView.findViewById(R.id.tv_price)
    val tvQuantity: TextView? = itemView.findViewById(R.id.tv_quantity)
    val tvAddToCart: TextView? = itemView.findViewById(R.id.tv_add_to_cart)
}

class ProductSeeMoreItemViewHolder(itemView: View): ProductItemHorizontalViewHolder(itemView) {
    val tvDescription: TextView? = itemView.findViewById(R.id.tv_description)
}