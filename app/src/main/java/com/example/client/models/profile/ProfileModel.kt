package com.example.client.models.profile

import com.example.client.base.BaseModel

data class ProfileModel(
        val id: Int,
        val email: String,
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
        val id: Int?,
        val email: String?,
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

data class DataProfileResponse(var access_token: String)

data class ProfileRequest(
        var email: String? = null,
        var avatar: String? = null,
        var fullname: String? = null,
        var birthday: String? = null,
        var phone: String? = null,
        var new_password: String? = null,
        var old_password: String? = null,
)