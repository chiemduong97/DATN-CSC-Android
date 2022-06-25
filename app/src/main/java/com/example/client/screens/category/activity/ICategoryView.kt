package com.example.client.screens.category.activity

import com.example.client.base.IBaseView
import com.example.client.models.category.CategoryModel

interface ICategoryView: IBaseView {
    fun showCategories(items: List<CategoryModel>)
    fun showEmptyData()
    fun showErrorMessage(errorMessage: Int)
}