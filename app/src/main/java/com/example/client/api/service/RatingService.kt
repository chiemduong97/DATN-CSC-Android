package com.example.client.api.service

import com.example.client.models.rating.RatingIdResponse
import com.example.client.models.rating.RatingRequest
import com.example.client.models.rating.RatingResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*


interface RatingService {
    @POST("api/rating/rating_post.php")
    fun postRating(@Body request: RatingRequest): Observable<BaseResponse<RatingIdResponse>>

    @GET("api/rating/rating_getAll.php")
    fun getRatings(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Observable<BaseResponse<List<RatingResponse>>>

}