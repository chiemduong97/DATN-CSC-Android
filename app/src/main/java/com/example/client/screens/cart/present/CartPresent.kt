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
    private var cart = preferences.cart
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
        cart.cartProducts.let {
            cart.cartProducts = it.filter { cartProductModel -> cartProductModel.quantity > 0 } as ArrayList<CartProductModel>
        }
        mView?.showCartProduct(cart)
    }

    override fun minus(cartProduct: CartProductModel) {
        cart.cartProducts.let { list ->
            list.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity--
                    saveCart(cartProduct)
                    if (it.quantity <= 0) {
                        getCartFromRes()
                    }
                }
            }
        }
    }

    override fun plus(cartProduct: CartProductModel) {
        cart.cartProducts.let { list ->
            list.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity++
                    saveCart(cartProduct)
                }
            }
        }
    }

    private fun saveCart(cartProduct: CartProductModel) {
        preferences.cart = cart
        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_CART))
        RxBus.newInstance().onNext(ValueEvent(Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT, cartProduct.product.checkAddToCart(cart)))
        mView?.updateTotalPrice(cart)
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