package com.example.client.screens.promotion.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BaseCollectionPresenter
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.models.promotion.PromotionModel
import com.example.client.models.promotion.toPromotions
import com.example.client.screens.promotion.activity.IPromotionView
import com.example.client.usecase.PromotionUseCase

class PromotionPresent(mView: IPromotionView): BaseCollectionPresenter<IPromotionView>(mView), IPromotionPresent {
    private val promotionUseCase by lazy { PromotionUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    override fun getPromotion(code: String) {
        mView?.showLoading()
        subscribe(promotionUseCase.getPromotion(code), {
            mView?.run {
                hideLoading()
                if (!it.is_error) {
                    showData(arrayListOf(it.data.toPromotionModel()), preferences.cart.promotion_id ?: -1)
                }
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showEmptyData()
            }
        })
    }

    override fun getPromotions() {
        mView?.showLoading()
        subscribe(promotionUseCase.getPromotions(), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showEmptyData()
                    return@subscribe
                }
                if (it.data.isEmpty()) showEmptyData()
                else {
                    showData(it.data.toPromotions(), preferences.cart.promotion_id ?: -1)
                }
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showEmptyData()
            }
        })
    }

    override fun usePromotion(promotion: PromotionModel) {
        preferences.cart = preferences.cart.apply {
            promotion_id = promotion.id
            promotion_code = promotion.code
            promotion_value = promotion.value
        }
        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_PROMOTION))
        mView?.onBackPress()
    }

    override fun removePromotion() {
        preferences.cart = preferences.cart.apply {
            promotion_id = null
            promotion_code = null
            promotion_value = null
        }
        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_PROMOTION))
    }

    override fun onRefresh() {
        getPromotions()
    }
}