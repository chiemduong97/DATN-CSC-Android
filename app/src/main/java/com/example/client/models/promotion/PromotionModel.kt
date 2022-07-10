package com.example.client.models.promotion

import com.example.client.base.BaseModel

data class PromotionModel(
        val id: Int,
        val avatar: String,
        val description: String,
        val code: String,
        val value: Double,
        val created_at: String,
        val start: String,
        val end: String,
) : BaseModel()

data class PromotionResponse(
        val id: Int?,
        val avatar: String?,
        val description: String?,
        val code: String?,
        val value: Double?,
        val created_at: String?,
        val start: String?,
        val end: String?,
) : BaseModel() {
    fun toPromotionModel() = PromotionModel(
            id = id ?: -1,
            avatar = avatar.orEmpty(),
            description = description.orEmpty(),
            code = code.orEmpty(),
            value = value ?: 0.0,
            created_at = created_at.orEmpty(),
            start = start.orEmpty(),
            end = end.orEmpty()
    )
}

fun List<PromotionResponse>.toPromotions() = map { it.toPromotionModel() }