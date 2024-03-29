package com.example.client.screens.category.parent.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.cart.CartProductModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.category.toCategories
import com.example.client.models.event.Event
import com.example.client.screens.category.parent.ISuperCategoryView
import com.example.client.usecase.CategoryUseCase

class SuperCategoryPresent(mView: ISuperCategoryView) : BasePresenterMVP<ISuperCategoryView>(mView), ISuperCategoryPresent {

    private val categoryUseCase by lazy { CategoryUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }

    override fun getSuperCategories() {
        mView?.showLoading()
        subscribe(categoryUseCase.getSuperCategories(), {
            mView?.run {
                hideLoading()
                when {
                    it.is_error -> {
                        showErrorMessage(getErrorMessage(it.code))
                    }
                    it.data.isEmpty() -> {
                        showErrorMessage(getErrorMessage(1001))
                    }
                    else -> {
                        showSuperCategories(it.data.toCategories())
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

    override fun getCategories(category_id: Int) {
        mView?.showLoading()
        subscribe(categoryUseCase.getCategories(category_id), {
            mView?.run {
                hideLoading()
                when {
                    it.is_error -> {
                        showErrorMessage(getErrorMessage(it.code))
                    }
                    it.data.isEmpty() -> {
                        showEmptyData()
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
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun onClickSuperCategory(category: CategoryModel) {
        getCategories(category.id)
    }

    override fun addToCart(cartProduct: CartProductModel) {
        preferences.cart = preferences.cart.apply {
            cartProducts = cartProducts.apply here@{
                map {
                    if (it.product.id == cartProduct.product.id) {
                        it.quantity += cartProduct.quantity
                        if (it.quantity > it.product.quantity) it.quantity = it.product.quantity
                        return@here
                    }
                }
                if (cartProduct.quantity > cartProduct.product.quantity) cartProduct.quantity = cartProduct.product.quantity
                add(cartProduct)
            }
        }
        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_CART))
    }

    override fun getCartFromRes() {
        preferences.cart = preferences.cart.apply {
            cartProducts = cartProducts.filter { cartProductModel -> cartProductModel.quantity > 0 } as ArrayList<CartProductModel>
        }
        preferences.cart.cartProducts.let {
            if (it.isNotEmpty()) {
                mView?.showCart(it.size)
            } else {
                mView?.hideCart()
            }
        }
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.UPDATE_CART -> {
                    getCartFromRes()
                }
            }
        })
    }
}