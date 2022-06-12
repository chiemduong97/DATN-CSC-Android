package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.UserService
import com.example.client.app.Constants
import com.example.client.app.MyFirebaseService
import com.example.client.app.Preferences
import com.example.client.models.profile.DataResponse
import com.example.client.models.profile.ProfileModel
import com.example.client.models.profile.ProfileResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class ProfileUseCase {
    private val userService by lazy { ApiClient.newInstance().create(UserService::class.java) }
    private val preferences by lazy { Preferences.newInstance() }
    private val firebaseService by lazy { MyFirebaseService() }

    companion object {
        fun newInstance() = ProfileUseCase()
    }


    fun resetFirebaseToken(): Observable<Int> {
        return firebaseService.resetToken()
    }

    fun checkEmail(email: String): Observable<BaseResponse<DataResponse>> {
        return userService.checkEmail(email)
    }

    fun login(email: String, password: String): Observable<BaseResponse<DataResponse>> {
        return userService.login(email, password)
    }

    fun register(fullname: String, phone: String, email: String, password: String): Observable<BaseResponse<DataResponse>> {
        return userService.register(fullname, phone, email, password)
    }

    fun resetPass(email: String, password: String): Observable<BaseResponse<DataResponse>> {
        return userService.resetPassword(email, password)
    }

    fun sendEmail(email: String, phone: String, requestType: Constants.RequestType): Observable<BaseResponse<DataResponse>> {
        return userService.sendEmail(email, phone, requestType)
    }

    fun verification(email: String, code: String): Observable<BaseResponse<DataResponse>> {
        return userService.verification(email, code)
    }

    fun getUserByEmail(email: String): Observable<BaseResponse<ProfileResponse>> {
        return userService.getUserByEmail(email)
    }

    fun updateDeviceToken(email: String, device_token: String): Observable<BaseResponse<DataResponse>> {
        return userService.updateDeviceToken(email, device_token)
    }

    fun getProfile(): ProfileModel {
        return preferences.profile
    }

    fun setAccessToken(accessToken: String) {
        preferences.accessToken = accessToken
    }

    fun setProfile(profileModel: ProfileModel) {
        preferences.profile = profileModel
    }

    fun setDeviceToken(deviceToken: String) {
        preferences.deviceToken = deviceToken
    }

    fun getDeviceToken(): String {
        return preferences.deviceToken
    }

    fun removeProfile() {
        preferences.deleteDeviceToken()
        preferences.deleteProfile()
        preferences.deleteAccessToken()
        preferences.deleteCart()
    }




}