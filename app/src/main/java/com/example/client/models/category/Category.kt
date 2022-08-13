package com.example.client.models.category

import com.example.client.base.BaseModel


open class CategoryModel(
        val id: Int = -1,
        var name: String = "",
        val avatar: String = "",
        val category: CategoryModel? = null,
        var selected: Boolean = false,
) : BaseModel()

data class CategoryResponse(
        val id: Int?,
        val name: String?,
        val avatar: String?,
        val category: CategoryModel?
) : BaseModel() {
    fun toCategoryModel() = CategoryModel(
            id = id ?: -1,
            name = name.orEmpty(),
            avatar = avatar.orEmpty(),
            category = category,
            selected = false
    )
}


fun List<CategoryResponse>.toCategories() = map { it.toCategoryModel() }

const val MAX_ITEM_CATEGORY = 12

