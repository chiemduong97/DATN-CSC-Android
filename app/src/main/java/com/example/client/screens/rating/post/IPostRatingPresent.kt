package com.example.client.screens.rating.post

import android.graphics.Bitmap
import com.example.client.base.IBasePresenter
import com.example.client.models.rating.ImageModel
import com.example.client.models.rating.RatingType

interface IPostRatingPresent : IBasePresenter {
    fun addImages(bitmaps: List<Bitmap>)
    fun removeImage(image: ImageModel)
    fun bindData()
    fun uploadImages(rating: RatingType, content: String, orderCode: String)
}