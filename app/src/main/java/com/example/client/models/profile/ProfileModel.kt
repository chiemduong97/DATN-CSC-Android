package com.example.client.models.profile

import com.example.client.base.BaseModel

data class ProfileModel(
        var id: Int,
        var email: String,
        var avatar: String,
        var fullname: String,
        var birthday: String,
        var phone: String,
        var csc_point: Double,
        var first_order: Boolean,
        var wallet: Double,
        var lat: Double,
        var lng: Double,
        var address: String,
)

data class ProfileResponse(
        var id: Int?,
        var email: String?,
        var avatar: String?,
        var fullname: String?,
        var birthday: String?,
        var phone: String?,
        var first_order: Boolean?,
        var wallet: Double?,
        var csc_point: Double?,
        var lat: Double?,
        var lng: Double?,
        var address: String?,
) : BaseModel() {
    fun toProfileModel() = ProfileModel(
            id = id ?: -1,
            email = email.orEmpty(),
            avatar = avatar.orEmpty(),
            fullname = fullname.orEmpty(),
            birthday = birthday.orEmpty(),
            phone = phone.orEmpty(),
            first_order = first_order ?: false,
            wallet = wallet ?: 0.0,
            csc_point = csc_point ?: 0.0,
            lat = lat ?: 0.0,
            lng = lng ?: 0.0,
            address = address.orEmpty()
    )
}

data class DataResponse(var access_token: String)