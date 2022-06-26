package com.example.client.models.category

import com.example.client.base.BaseModel

data class CategoryResponse(var id:Int?, var name: String?, var avatar: String?) : BaseModel() {
    fun toCategoryModel() = CategoryModel(
            id = id ?: -1,
            name = name ?: "Tên thể loại",
            avatar = avatar ?: "https://ps.w.org/gazchaps-woocommerce-auto-category-product-thumbnails/assets/icon-256x256.png?rev=1848416"
    )
}

data class CategoryModel(var id:Int, var name: String, var avatar: String) : BaseModel()

fun List<CategoryResponse>.toCategories(): List<CategoryModel> {
    val categories = arrayListOf<CategoryModel>()
    forEach {
        categories.add(it.toCategoryModel())
    }
    return categories
}

const val MAX_ITEM_CATEGORY = 12
