package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.NotifyService

class NotifyUseCase {
    private val notifyService by lazy { ApiClient.newInstance().create(NotifyService::class.java) }
    companion object {
        fun newInstance() = NotifyUseCase()
    }
    fun getNotifies(userId: Int, page: Int, limit: Int) = notifyService.getByUser(userId, page, limit)

}