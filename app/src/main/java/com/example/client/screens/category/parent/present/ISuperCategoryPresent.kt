package com.example.client.screens.category.parent.present

import com.example.client.base.IBasePresenter
import com.example.client.models.cart.CartProductModel
import com.example.client.models.category.CategoryModel

interface ISuperCategoryPresent: IBasePresenter {
    fun getSuperCategories()
    fun getCategories(category_id: Int)
    fun onClickSuperCategory(category: CategoryModel)
    fun addToCart(cartProduct: CartProductModel)
    fun getCartFromRes()
}