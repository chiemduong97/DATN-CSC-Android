package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.RatingService
import com.example.client.models.rating.RatingIdResponse
import com.example.client.models.rating.RatingRequest
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class RatingUseCase {
    private val ratingService by lazy { ApiClient.newInstance().create(RatingService::class.java) }
    companion object {
        fun newInstance() = RatingUseCase()
    }
    fun postRating(request: RatingRequest) = ratingService.postRating(request)

    fun getRatings(page: Int, limit: Int) = ratingService.getRatings(page, limit)

}