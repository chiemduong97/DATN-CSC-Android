package com.example.client.screens.promotion.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.promotion.PromotionModel
import com.example.client.screens.promotion.activity.IPromotionView
import com.example.client.screens.promotion.present.IPromotionPresent
import com.example.client.screens.promotion.present.PromotionPresent
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.fragment_promotion_detail.*
import java.text.NumberFormat
import java.util.*

class PromotionDetailFragment : BaseCollectionFragment<IPromotionPresent>(), IPromotionView {
    private var scrollTop = true

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) = PromotionDetailFragment().apply {
            arguments = args
        }
    }

    private val promotionModel by lazy { arguments?.getSerializable(Constants.BundleKey.PROMOTION_MODEL) as PromotionModel }

    override val presenter: IPromotionPresent
        get() = PromotionPresent(this)

    override fun getLayout() = R.layout.fragment_promotion_detail

    override fun shouldLoadMore() = false

    override fun bindData() {
        promotionModel.run {
            tv_code.text = code
            tv_value.text = HtmlCompat.fromHtml(getString(R.string.promotion_value,
                    if (value < 1) "${(value * 100).toInt()}%"
                    else NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(value)),
                    HtmlCompat.FROM_HTML_MODE_LEGACY)
            tv_time.text = start.plus(" - ").plus(end)
            tv_description.text = description
        }
    }

    override fun bindEvent() {
        tv_use.setOnClickListener {
            presenter.usePromotion(promotionModel)
        }
        imv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        appbar_layout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            when (verticalOffset) {
                -collapsing_toolbar.height + toolbar.height -> {
                    tv_title.visibility = View.VISIBLE
                    imv_back.setColorFilter(Color.BLACK)
                    imv_share.setColorFilter(Color.BLACK)
                    scrollTop = false
                }
                else -> {
                    if (!scrollTop) {
                        tv_title.visibility = View.INVISIBLE
                        imv_back.setColorFilter(Color.WHITE)
                        imv_share.setColorFilter(Color.WHITE)
                        scrollTop = true
                    }
                }
            }
        })
    }

    override fun showData(items: List<PromotionModel>, promotion_id: Int) {}

    override fun showEmptyData() {}

    override fun onBackPress() {
        requireActivity().finish()
    }
}