package com.example.client.screens.rating.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.models.rating.ImageModel

class ImageItem(
    private val context: Context,
    private val items: List<ImageModel>,
    private val onAdd: () -> Unit,
    private val onRemove: (image: ImageModel) -> Unit,
    private val maxItem: Int,
) : RecyclerView.Adapter<ImageViewHolder>() {
    companion object {
        private const val TYPE_ADD = 1
        private const val TYPE_EMPTY = 2
        private const val TYPE_IMAGE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return when (viewType) {
            TYPE_ADD -> ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_picker, null))
            TYPE_EMPTY -> ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_empty, null))
            TYPE_IMAGE -> ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, null))
            else -> ImageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image_empty, null))
        }
    }

    override fun onBindViewHolder(viewHolder: ImageViewHolder, position: Int) {
        viewHolder.apply {
            ivImage?.let {
                val item = items[position - if (maxItem == 4) 0 else 1]
                item.bitmap?.let {
                    ivImage.setImageBitmap(it)
                } ?: kotlin.run {
                    item.path?.let {
                        Glide.with(context).asBitmap().placeholder(R.drawable.ic_category_default).load(it).into(ivImage)
                    }
                }
            }


            ivDelete?.setOnClickListener {
                val item = items[position - if (maxItem == 4) 0 else 1]
                onRemove.invoke(item)
            }
            flAddImage?.setOnClickListener {
                onAdd.invoke()
            }
        }
    }

    override fun getItemCount() = maxItem

    override fun getItemViewType(position: Int): Int {
        val isMax = if (maxItem == 4) 0 else 1
        return when {
            items.size < maxItem && position == 0 -> TYPE_ADD
            items.size == maxItem || (items.size < maxItem && position in (0 + isMax)..(items.size - 1 + isMax)) -> TYPE_IMAGE
            items.size < maxItem && position in (items.size + isMax)..maxItem -> TYPE_EMPTY
            else -> -1
        }
    }
}

class ImageViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivImage: ImageView? = itemView.findViewById(R.id.iv_image)
    val flAddImage: FrameLayout? = itemView.findViewById(R.id.fl_add_image)
    val ivDelete: ImageView? = itemView.findViewById(R.id.iv_delete)
}