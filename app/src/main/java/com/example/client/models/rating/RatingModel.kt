package com.example.client.models.rating

import com.example.client.base.BaseModel
import com.example.client.models.order.OrderModel
import com.example.client.models.profile.ProfileModel
import com.google.gson.annotations.SerializedName

data class RatingModel(
        val id: Int = -1,
        val rating: RatingType = RatingType.RATING_GOOD,
        val content: String = "",
        val created_at: String = "",
        val images: List<String> = emptyList(),
        val user: ProfileModel = ProfileModel(),
        val order: OrderModel = OrderModel(),
): BaseModel() {
    fun toImageModels() = images.map { ImageModel(path = it) }
}

data class RatingResponse(
    val id: Int?,
    val rating: RatingType?,
    val content: String?,
    val created_at: String?,
    val images: List<String>?,
    val user: ProfileModel?,
    val order: OrderModel?
) : BaseModel() {
    fun toRatingModel() = RatingModel(
        id = id ?: -1,
        rating = rating ?: RatingType.RATING_BAD,
        content = content.orEmpty(),
        created_at = created_at.orEmpty(),
        images = images.orEmpty(),
        user = user ?: ProfileModel(),
        order = order ?: OrderModel()
    )
}

data class RatingRequest(
    val user_id: Int,
    val rating: RatingType,
    val content: String,
    val images: List<String>,
    val order_code: String
)

fun List<RatingResponse>.toRatings() = map { it.toRatingModel() }

data class RatingIdResponse(val rating_id: Int)

enum class RatingType {
    @SerializedName("good")
    RATING_GOOD,
    @SerializedName("bad")
    RATING_BAD
}