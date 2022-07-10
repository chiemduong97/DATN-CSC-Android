package com.example.client.screens.promotion.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.models.promotion.PromotionModel
import com.example.client.screens.promotion.item.PromotionItem
import com.example.client.screens.promotion.present.IPromotionPresent
import com.example.client.screens.promotion.present.PromotionPresent
import kotlinx.android.synthetic.main.activity_promotion.*

class PromotionActivity : BaseActivityMVP<IPromotionPresent>(), IPromotionView {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity) = Intent(from, PromotionActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotion)
    }

    override fun bindData() {
        presenter.getPromotions()
    }

    override fun bindEvent() {
        imv_back.setOnClickListener {
            finish()
        }
        swipe_refresh.setOnRefreshListener {
            presenter.getPromotions()
        }
    }

    override val presenter: IPromotionPresent
        get() = PromotionPresent(this)

    override fun showData(items: List<PromotionModel>, promotion_id: Int) {
        imv_empty.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val item = PromotionItem(this, items, promotion_id, {
            presenter.usePromotion(it)
        }, {
            presenter.removePromotion()
        })
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showEmptyData() {
        imv_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh.run {
            if (isRefreshing) isRefreshing = false
        }
    }

    override fun onBackPress() {
        onBackPressed()
    }
}