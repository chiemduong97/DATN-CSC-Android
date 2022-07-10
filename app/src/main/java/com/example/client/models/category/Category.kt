package com.example.client.models.category

import com.example.client.base.BaseModel


data class CategoryModel(
        val id: Int,
        val name: String,
        val avatar: String,
        var selected: Boolean,
) : BaseModel()

data class CategoryResponse(
        val id: Int?,
        val name: String?,
        val avatar: String?,
) : BaseModel() {
    fun toCategoryModel() = CategoryModel(
            id = id ?: -1,
            name = name.orEmpty(),
            avatar = avatar.orEmpty(),
            selected = false
    )
}


fun List<CategoryResponse>.toCategories() = map { it.toCategoryModel() }

const val MAX_ITEM_CATEGORY = 12

