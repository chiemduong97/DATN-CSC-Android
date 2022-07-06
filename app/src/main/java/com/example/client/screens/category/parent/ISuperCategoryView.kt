package com.example.client.screens.category.parent

import com.example.client.base.IBaseView
import com.example.client.models.category.CategoryModel

interface ISuperCategoryView: IBaseView {
    fun showErrorMessage(errMessage: Int)
    fun showSuperCategories(items: List<CategoryModel>)
    fun showProducts(items: List<CategoryModel>)
    fun showCart(quantity: Int)
    fun hideCart()
}