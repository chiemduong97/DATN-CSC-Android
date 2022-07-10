package com.example.client.screens.promotion.present

import com.example.client.base.IBasePresenter
import com.example.client.models.promotion.PromotionModel

interface IPromotionPresent: IBasePresenter {
    fun getPromotions()
    fun usePromotion(promotion: PromotionModel)
    fun removePromotion()
}