package com.example.client.screens.promotion.activity

import com.example.client.base.IBaseView
import com.example.client.models.promotion.PromotionModel

interface IPromotionView: IBaseView {
    fun showData(items: List<PromotionModel>, promotion_id: Int)
    fun showEmptyData()
}