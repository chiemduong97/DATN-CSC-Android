package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.UserService
import com.example.client.app.Constants
import com.example.client.app.MyFirebaseService
import com.example.client.app.Preferences
import com.example.client.models.profile.DataProfileResponse
import com.example.client.models.profile.ProfileModel
import com.example.client.models.profile.ProfileRequest
import com.example.client.models.profile.ProfileResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class ProfileUseCase {
    private val userService by lazy { ApiClient.newInstance().create(UserService::class.java) }
    private val firebaseService by lazy { MyFirebaseService() }

    companion object {
        fun newInstance() = ProfileUseCase()
    }


    fun resetFirebaseToken(): Observable<Int> {
        return firebaseService.resetToken()
    }

    fun checkEmail(email: String): Observable<BaseResponse<DataProfileResponse>> {
        return userService.checkEmail(email)
    }

    fun login(email: String, password: String): Observable<BaseResponse<DataProfileResponse>> {
        return userService.login(email, password)
    }

    fun register(fullname: String, phone: String, email: String, password: String): Observable<BaseResponse<DataProfileResponse>> {
        return userService.register(fullname, phone, email, password)
    }

    fun resetPass(email: String, password: String): Observable<BaseResponse<DataProfileResponse>> {
        return userService.resetPassword(email, password)
    }

    fun sendEmail(email: String, phone: String, requestType: Constants.RequestType): Observable<BaseResponse<DataProfileResponse>> {
        return userService.sendEmail(email, phone, requestType)
    }

    fun verification(email: String, code: String): Observable<BaseResponse<DataProfileResponse>> {
        return userService.verification(email, code)
    }

    fun getUserByEmail(email: String): Observable<BaseResponse<ProfileResponse>> {
        return userService.getUserByEmail(email)
    }

    fun updateInfo(profileRequest: ProfileRequest): Observable<BaseResponse<DataProfileResponse>> {
        return userService.updateInfo(profileRequest)
    }

    fun updatePass(profileRequest: ProfileRequest): Observable<BaseResponse<DataProfileResponse>> {
        return userService.updatePass(profileRequest)
    }

    fun updateAvatar(profileRequest: ProfileRequest): Observable<BaseResponse<DataProfileResponse>> {
        return userService.updateAvatar(profileRequest)
    }

    fun updateLocation(email: String, lat: Double, lng: Double, address: String): Observable<BaseResponse<DataProfileResponse>> {
        return userService.updateLocation(email, lat, lng, address)
    }

    fun updateDeviceToken(email: String, device_token: String): Observable<BaseResponse<DataProfileResponse>> {
        return userService.updateDeviceToken(email, device_token)
    }

}