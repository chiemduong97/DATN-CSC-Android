package com.example.client.models.product

import com.example.client.models.category.CategoryModel

class HomeSectionModel(
        val title: String,
        val url: String
): CategoryModel() {
    companion object {
        val HOME_SECTION = arrayListOf(
            HomeSectionModel("Sản phẩm mới", "api/product/product_getNew.php"),
            HomeSectionModel("Sản phẩm nổi bậc", "api/product/product_getHighLight.php"),
            HomeSectionModel("Vừa đặt gần đây", "api/product/product_getRecent.php")
        ).map { it.apply { name = title } }
    }

}