package com.example.client.screens.cart.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.cart.CartProductModel
import com.example.client.models.event.Event
import com.example.client.models.event.ValueEvent
import com.example.client.screens.cart.activity.ICartView

class CartPresent(mView: ICartView): BasePresenterMVP<ICartView>(mView), ICartPresent {
    private val preferences by lazy { Preferences.newInstance() }
    override fun bindData() {
        mView?.run {
            preferences.cart = preferences.cart.apply {
                this.order_lat = preferences.orderLocation.lat
                this.order_lng = preferences.orderLocation.lng
                this.order_address = preferences.orderLocation.address
                this.branch_lat = preferences.branch.lat
                this.branch_lng = preferences.branch.lng
                this.branch_address = preferences.branch.address
            }
            showUserInfo(preferences.profile)
            showBranchInfo(preferences.branch)
            getCartFromRes()
        }
    }

    private fun getCartFromRes() {
        preferences.cart = preferences.cart.apply {
            cartProducts = cartProducts.filter { cartProductModel -> cartProductModel.quantity > 0 } as ArrayList<CartProductModel>
        }
        mView?.showCartProduct(preferences.cart)
    }

    override fun minus(cartProduct: CartProductModel) {
        var updateUI = false
        preferences.cart = preferences.cart.apply {
            cartProducts.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity--
                    updateUI = it.quantity == 0
                }
            }
        }
        if (updateUI) {
            getCartFromRes()
            RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_CART))
            RxBus.newInstance().onNext(ValueEvent(Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT, cartProduct.product.apply { addToCart = 0 }))
        } else {
            saveCart(cartProduct)
        }
    }


    override fun plus(cartProduct: CartProductModel) {
        preferences.cart = preferences.cart.apply {
            cartProducts.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity++
                }
            }
        }
        saveCart(cartProduct)
    }

    private fun saveCart(cartProduct: CartProductModel) {
        RxBus.newInstance().onNext(ValueEvent(Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT, cartProduct.product.checkAddToCart(preferences.cart)))
        mView?.updateTotalPrice(preferences.cart)
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.CHANGE_BRANCH -> mView?.showBranchInfo(preferences.branch)
            }
        })
    }

}