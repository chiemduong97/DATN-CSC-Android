package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.UserService
import com.example.client.models.profile.DataResponse
import com.example.client.models.profile.ProfileResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class ProfileUseCase {
    private val userService by lazy { ApiClient.getInstance().create(UserService::class.java) }
    companion object {
        fun newInstance() = ProfileUseCase()
    }
    fun checkEmail(email: String): Observable<BaseResponse<DataResponse>> {
        return userService.checkEmail(email)
    }
    fun login(email: String, password: String): Observable<BaseResponse<DataResponse>> {
        return userService.login(email, password)
    }

    fun getUserByEmail(email: String): Observable<BaseResponse<ProfileResponse>> {
        return userService.getUserByEmail(email)
    }

    fun updateDeviceToken(email: String, device_token: String): Observable<BaseResponse<DataResponse>> {
        return userService.updateDeviceToken(email, device_token)
    }

}