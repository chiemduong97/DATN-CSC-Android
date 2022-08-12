package com.example.client.screens.rating.post

import android.graphics.Bitmap
import com.example.client.base.IBaseView
import com.example.client.models.rating.ImageModel

interface IPostRatingView : IBaseView {
    fun showImages(images: List<ImageModel>)
    fun showWarningLimit(currentSize: Int, images: List<Bitmap>)
    fun showErrorMessage(errMessage: Int)
    fun postRatingSuccess(rating_id: Int)
}