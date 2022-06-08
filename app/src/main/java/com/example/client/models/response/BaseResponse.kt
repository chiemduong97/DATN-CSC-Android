package com.example.client.models.response

import com.example.client.base.BaseModel

data class BaseResponse<T>(
        var data: T,
        var code: Int,
        var isError: Boolean,
        var message: String
) : BaseModel()
