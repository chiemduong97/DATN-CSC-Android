package com.example.client.screens.category.present

import com.example.client.base.BasePresenterMVP
import com.example.client.models.category.MAX_ITEM_CATEGORY
import com.example.client.models.category.toCategories
import com.example.client.screens.category.activity.ICategoryView
import com.example.client.usecase.CategoryUseCase

class CategoryPresent(mView: ICategoryView) : BasePresenterMVP<ICategoryView>(mView), ICategoryPresent {
    private val categoryUseCase by lazy { CategoryUseCase.newInstance() }
    override fun getCategories() {
        mView?.showLoading()
        subscribe(categoryUseCase.getSuperCategories(), {
            mView?.run {
                hideLoading()
                when {
                    it.is_error -> {
                        showEmptyData()
                        showErrorMessage(getErrorMessage(it.code))
                    }
                    it.data.isNullOrEmpty() -> {
                        showEmptyData()
                        showErrorMessage(getErrorMessage(1001))
                    }
                    else -> {
                        showCategories(it.data.toCategories())
                    }
                }
            }

        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showEmptyData()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }
}