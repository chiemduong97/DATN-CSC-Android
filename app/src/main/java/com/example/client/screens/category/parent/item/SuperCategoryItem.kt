package com.example.client.screens.category.parent.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.models.category.CategoryModel

class SuperCategoryItem(
        private val context: Context,
        private val items: List<CategoryModel>,
        private val onClickItem: (category: CategoryModel) -> Unit,
) : RecyclerView.Adapter<SuperCategoryItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperCategoryItemViewHolder {
        return SuperCategoryItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_super_category, null))
    }

    override fun onBindViewHolder(viewholder: SuperCategoryItemViewHolder, position: Int) {
        viewholder.apply {
            val item = items[position]
            imvAvatar?.let { Glide.with(context).asBitmap().placeholder(R.drawable.ic_category_default).load(item.avatar).into(it) }
            tvName?.text = item.name
            lnlSuperCategory?.setOnClickListener {
                if (item.selected) return@setOnClickListener
                item.selected = true
                setBackground(viewholder, true)
                onClickItem.invoke(item)
            }
            setBackground(viewholder, item.selected)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

private fun setBackground(holder: SuperCategoryItemViewHolder, isSelected: Boolean) {
    if (isSelected) holder.lnlSuperCategory?.setBackgroundResource(R.drawable.border_item_red_light)
    else holder.lnlSuperCategory?.setBackgroundResource(R.drawable.border_item_gray_dark)
}

class SuperCategoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val lnlSuperCategory: LinearLayout? = itemView.findViewById(R.id.lnl_super_category)
    val imvAvatar: ImageView? = itemView.findViewById(R.id.imv_avatar)
    val tvName: TextView? = itemView.findViewById(R.id.tv_name)
}