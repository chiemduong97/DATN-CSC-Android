package com.example.client.screens.category.parent.present

import com.example.client.base.BasePresenterMVP
import com.example.client.models.category.CategoryModel
import com.example.client.models.category.toCategories
import com.example.client.screens.category.parent.ISuperCategoryView
import com.example.client.usecase.CategoryUseCase

class SuperCategoryPresent(mView: ISuperCategoryView) : BasePresenterMVP<ISuperCategoryView>(mView), ISuperCategoryPresent {

    private val categoryUseCase by lazy { CategoryUseCase.newInstance() }

    override fun bindData() {
        getSuperCategories()
    }

    private fun getSuperCategories() {
        mView?.showLoading()
        subscribe(categoryUseCase.getSuperCategories(), {
            mView?.run {
                hideLoading()
                when {
                    it.is_error -> {
                        showErrorMessage(getErrorMessage(it.code))
                    }
                    it.data.isNullOrEmpty() -> {
                        showErrorMessage(getErrorMessage(1001))
                    }
                    else -> {
                        showSuperCategories(it.data.toCategories())
                        showProducts(it.data.toCategories())
                    }
                }
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun onClickItem(categoryModel: CategoryModel) {
    }
}