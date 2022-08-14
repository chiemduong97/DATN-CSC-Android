package com.example.client.api.service

import com.example.client.models.notify.NotifyResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NotifyService {
    @GET("api/notify/getAll.php")
    fun getByUser(
            @Query("user_id") user_id: Int,
            @Query("page") page: Int,
            @Query("limit") limit: Int,
    ): Observable<BaseResponse<List<NotifyResponse>>>
}