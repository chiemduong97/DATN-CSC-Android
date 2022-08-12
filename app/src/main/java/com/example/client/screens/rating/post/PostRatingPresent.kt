package com.example.client.screens.rating.post

import android.graphics.Bitmap
import android.net.Uri
import com.example.client.app.Constants
import com.example.client.app.Firebase
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.models.rating.ImageModel
import com.example.client.models.rating.RatingRequest
import com.example.client.models.rating.RatingType
import com.example.client.usecase.RatingUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class PostRatingPresent(mView: IPostRatingView): BasePresenterMVP<IPostRatingView>(mView), IPostRatingPresent {
    private val ratingUseCase by lazy { RatingUseCase.newInstance() }
    companion object {
        private var images: MutableList<ImageModel> = ArrayList()
    }
    override fun addImages(bitmaps: List<Bitmap>) {
        if (images.size + bitmaps.size > 4) {
            mView?.showWarningLimit(images.size, bitmaps)
            return
        }
        bitmaps.forEach {
            images.add(ImageModel(bitmap = it))
        }
        mView?.showImages(images)
    }

    override fun removeImage(image: ImageModel) {
        images.remove(image)
        mView?.showImages(images)
    }

    override fun bindData() {
        mView?.showImages(images)
    }

    private fun postRating(rating: RatingType, content: String, orderCode: String) {
        mView?.showLoading()
        subscribe(ratingUseCase.postRating(createRequest(rating, content, orderCode)), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                images.clear()
                RxBus.newInstance().onNext(Event(Constants.EventKey.RATING_SUCCESS))
                postRatingSuccess(it.data.rating_id)
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun uploadImages(rating: RatingType, content: String, orderCode: String) {
        if (images.isEmpty() || uploadImageSuccess()) postRating(rating, content, orderCode)
        else {
            images.forEach {
                uploadImage(it, rating, content, orderCode)
            }
        }


    }

    private fun uploadImage(image: ImageModel, rating: RatingType, content: String, orderCode: String) {
        mView?.showLoading()
        val ref = Firebase.newInstance().ref()
        val byteArray = ByteArrayOutputStream()
        image.bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
        val data = byteArray.toByteArray()
        val uploadTask = ref.putBytes(data)
        uploadTask.addOnFailureListener {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        }.addOnSuccessListener {
            uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
                if (!task.isSuccessful) {
                    mView?.run {
                        hideLoading()
                        showErrorMessage(getErrorMessage(1001))
                    }
                    throw task.exception!!
                }
                ref.downloadUrl
            }.addOnCompleteListener { task: Task<Uri> ->
                if (task.isSuccessful) {
                    val url = task.result
                    image.path = url.toString()
                    if (uploadImageSuccess()) {
                        postRating(rating, content, orderCode)
                    }
                } else {
                    mView?.run {
                        hideLoading()
                        showErrorMessage(getErrorMessage(1001))
                    }
                }
            }
        }
    }

    private fun uploadImageSuccess() = images.size == images.filter { it.path.isNullOrEmpty().not() }.size

    private fun createRequest(rating: RatingType, content: String, orderCode: String) = RatingRequest(
            user_id = Preferences.newInstance().profile.id,
            rating = rating,
            content = content,
            images = images.map { it.path ?: "" },
            order_code = orderCode
    )


}