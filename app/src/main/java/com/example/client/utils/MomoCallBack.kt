package com.example.client.utils

interface MomoCallBack {
    fun onSuccess(data: String, userPhone: String)
    fun onError()
}