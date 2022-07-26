package com.example.client.api.service

import com.example.client.models.promotion.PromotionResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface PromotionService {
    @GET("api/promotion/promotion_getAll.php")
    fun getPromotions(): Observable<BaseResponse<List<PromotionResponse>>>

    @GET("api/promotion/promotion_getByCode.php")
    fun getPromotion(@Query("query") code: String): Observable<BaseResponse<PromotionResponse>>

}