package com.example.client.screens.promotion.present

import com.example.client.base.IBaseCollectionPresenter
import com.example.client.base.IBasePresenter
import com.example.client.models.promotion.PromotionModel

interface IPromotionPresent: IBaseCollectionPresenter {
    fun getPromotion(code: String)
    fun getPromotions()
    fun usePromotion(promotion: PromotionModel)
    fun removePromotion()
}