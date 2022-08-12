package com.example.client.screens.rating.community.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.models.rating.ImageModel
import kotlinx.android.synthetic.main.item_image.view.*

class ImageRadiusView : FrameLayout {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_image_radius, this, false)
        addView(view)
    }

    fun bind(image: ImageModel) {
        image.bitmap?.let {
            iv_image.setImageBitmap(it)
        } ?: kotlin.run {
            image.path?.let {
                Glide.with(context).asBitmap().placeholder(R.drawable.ic_category_default).load(it).into(iv_image)
            }
        }
    }
}