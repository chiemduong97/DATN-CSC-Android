package com.example.client.models.profile

class ProfileModel(
    var id: Int,
    var email: String,
    var avatar: String?,
    var fullname: String?,
    var birthday: String?,
    var phone: String,
    var firstorder: Boolean,
    var wallet: Double,
    var latitude: Double,
    var longitude: Double,
    var address: String
)