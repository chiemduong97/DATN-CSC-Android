package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.PromotionService
import com.example.client.models.promotion.PromotionResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class PromotionUseCase {
    private val promotionService by lazy { ApiClient.newInstance().create(PromotionService::class.java) }
    companion object {
        fun newInstance() = PromotionUseCase()
    }
    fun getPromotions(): Observable<BaseResponse<List<PromotionResponse>>> = promotionService.getPromotions()

    fun getPromotion(code: String): Observable<BaseResponse<PromotionResponse>> = promotionService.getPromotion(code)


}