package com.example.client.models.response

import com.example.client.base.BaseModel

data class BaseResponse<T>(
        var data: T,
        var code: Int,
        var is_error: Boolean,
        var message: String,
        var load_more: Boolean,
) : BaseModel()
