package com.example.client.screens.category.item

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
import com.example.client.models.category.MAX_ITEM_CATEGORY

class HomeCategoryItem(
        private val context: Context,
        private val items: List<CategoryModel>,
        private val onClickItem: (category: CategoryModel) -> Unit,
        private val onClickSeeMore: () -> Unit
) : RecyclerView.Adapter<HomeCategoryItemViewHolder>() {
    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_MORE = 0
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryItemViewHolder {
        return if (viewType == TYPE_ITEM) HomeCategoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category, null))
        else CategorySeeMoreItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_category_see_more, null))
    }

    override fun onBindViewHolder(viewholder: HomeCategoryItemViewHolder, position: Int) {
        viewholder.apply {
            when (getItemViewType(position)) {
                TYPE_ITEM -> {
                    val item = items[position]
                    imvAvatar?.let { Glide.with(context).asBitmap().placeholder(R.drawable.ic_category_default).load(item.avatar).into(it) }
                    tvName?.text = item.name
                    itemView.setOnClickListener {
                        onClickItem.invoke(item)
                    }
                }
                TYPE_MORE -> {
                    itemView.setOnClickListener {
                        onClickSeeMore.invoke()
                    }
                }
            }

        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (position == MAX_ITEM_CATEGORY - 1 && items.size == MAX_ITEM_CATEGORY) {
            TYPE_MORE
        } else {
            TYPE_ITEM
        }
    }
}

open class HomeCategoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imvAvatar: ImageView? = itemView.findViewById(R.id.imv_avatar)
    val tvName: TextView? = itemView.findViewById(R.id.tv_name)
}

class CategorySeeMoreItemViewHolder(itemView: View): HomeCategoryItemViewHolder(itemView)