package com.example.client.screens.product.detail.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.cart.CartProductModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.event.Event
import com.example.client.models.event.ValueEvent
import com.example.client.models.category.HomeSectionModel
import com.example.client.models.product.toProducts
import com.example.client.screens.product.detail.IProductDetailView
import com.example.client.usecase.ProductUseCase

class ProductDetailPresent(mView: IProductDetailView) : BasePresenterMVP<IProductDetailView>(mView), IProductDetailPresent {
    private val productUseCase by lazy { ProductUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }

    override fun loadDataByCategory(categoryModel: CategoryModel) {
        if (categoryModel is HomeSectionModel) getProductsByUrl(categoryModel.url)
        else getProductsByCategory(categoryModel.id)
    }

    private fun getProductsByUrl(url: String) {
        mView?.showLoading()
        subscribe(productUseCase.getProductsByUrl(url, 1, 10), {
            mView?.run {
                hideLoading()
                if (it.is_error || it.data.isEmpty()) {
                    showEmptyData()
                    return@subscribe
                }
                showListProduct(it.data.toProducts())
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showEmptyData()
            }
        })
    }

    private fun getProductsByCategory(category_id: Int) {
        mView?.showLoading()
        subscribe(productUseCase.getProducts(category_id, 1, 10), {
            mView?.run {
                hideLoading()
                if (it.is_error || it.data.isEmpty()) {
                    showEmptyData()
                    return@subscribe
                }
                showListProduct(it.data.toProducts())
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showEmptyData()
            }
        })
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
        RxBus.newInstance().onNext(ValueEvent(Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT, cartProduct.product))
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
        add(RxBus.newInstance().subscribe{
            when (it.key) {
                Constants.EventKey.UPDATE_CART -> getCartFromRes()
            }
        })
    }

}