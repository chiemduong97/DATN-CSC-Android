package com.example.client.screens.rating.post

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.rating.ImageModel
import com.example.client.models.rating.RatingType
import com.example.client.screens.rating.item.ImageItem
import kotlinx.android.synthetic.main.activity_post_rating.*


class PostRatingActivity : BaseActivityMVP<IPostRatingPresent>(), IPostRatingView {

    companion object {
        fun newInstance(from: Activity, orderCode: String) = Intent(from, PostRatingActivity::class.java).apply {
            putExtra(Constants.BundleKey.ORDER_CODE, orderCode)
        }
    }
    override val presenter: IPostRatingPresent
        get() = PostRatingPresent(this)

    private var intentActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    private val orderCode by lazy { intent.getStringExtra(Constants.BundleKey.ORDER_CODE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_rating)
    }

    override fun bindData() {
        presenter.bindData()
    }

    override fun bindComponent() {
        tv_title.text = getString(R.string.rating_title, orderCode)
        tv_rating_good.isSelected = true
        tv_rating_bad.isSelected = false
    }

    override fun bindEvent() {
        tv_rating_good.setOnClickListener {
            tv_rating_good.isSelected = true
            tv_rating_bad.isSelected = false
        }

        tv_rating_bad.setOnClickListener {
            tv_rating_good.isSelected = false
            tv_rating_bad.isSelected = true
        }

        tv_save_rating.setOnClickListener {
            orderCode?.let { ordeCode -> presenter.uploadImages(if(tv_rating_good.isSelected) RatingType.RATING_GOOD else RatingType.RATING_BAD, et_content.text.toString(), ordeCode) }
        }

        imv_back.setOnClickListener {
            onBackPressed()
        }

        intentActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                try {
                    data?.let {
                        it.extras?.run {
                            val bitmap = this["data"] as Bitmap
                            presenter.addImages(arrayListOf(bitmap))
                        } ?: kotlin.run {
                            it.data?.let { uri ->
                                val bitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                                } else {
                                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                }
                                presenter.addImages(arrayListOf(bitmap))
                            } ?: kotlin.run {
                                val bitmaps = arrayListOf<Bitmap>()
                                it.clipData?.let { clipData ->
                                    for (i in 0 until clipData.itemCount) {
                                        val uri = clipData.getItemAt(i).uri
                                        val bitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                                        } else {
                                            MediaStore.Images.Media.getBitmap(contentResolver, uri)
                                        }
                                        bitmaps.add(bitmap)
                                    }
                                    presenter.addImages(bitmaps)
                                }

                            }


                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    showToastMessage(getString(R.string.err_code_1001))
                }
            }
        }
    }

    override fun showImages(images: List<ImageModel>) {
        recycler_view.removeAllViews()
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val item = ImageItem(this, images, {
            val take = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val pick = Intent(Intent.ACTION_GET_CONTENT).apply { putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) }
            pick.type = "image/*"
            val intent = Intent.createChooser(pick, getString(R.string.manager_profile_text_pick))
            intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(take))
            intentActivityResultLauncher?.launch(intent)
        }, {
            showWarningRemove(it)
        }, if (images.size == 4) 4 else 5)
        recycler_view.adapter = item
        recycler_view.layoutManager = manager
    }

    private fun showWarningRemove(image: ImageModel) {
        PrimaryDialog({ presenter.removeImage(image) }, {})
            .setDescription(getString(R.string.rating_dialog_sure_remove))
            .show(supportFragmentManager)
    }

    override fun showWarningLimit(currentSize: Int, images: List<Bitmap>) {
        PrimaryDialog({ presenter.addImages(images.take(4 - currentSize)) }, {})
            .setDescription(getString(R.string.rating_dailog_show_limit))
            .show(supportFragmentManager)
    }

    override fun showErrorMessage(errMessage: Int) {
        showDialogErrorMessage(getString(errMessage))
    }

    override fun postRatingSuccess(rating_id: Int) {
        PrimaryDialog({ finish() }, {})
                .showBtnCancel(false)
                .setDescription(getString(R.string.dialog_rating_success))
                .show(supportFragmentManager)
    }


    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {
        onBackPressed()
    }
}