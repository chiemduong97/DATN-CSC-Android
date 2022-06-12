package com.example.client.screens.cart.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.cart.CartModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.event.Event
import com.example.client.screens.cart.activity.ICartView
import org.greenrobot.eventbus.EventBus

class CartPresent(var view: ICartView?): ICartPresent {
    private var cart = Preferences.newInstance().cart ?: CartModel(arrayListOf())
    override fun generationCart() {
        val profile = Preferences.newInstance().profile
        val branch = Preferences.newInstance().branch
        cart.apply {
            order_latitude = profile.lat
            order_longitude = profile.lng
            order_address = profile.address
            branch_latitude = branch.lat
            branch_longitude = branch.lng
            branch_address = branch.address
        }
        saveCart()

    }

    override fun getBranchFromRes() {
        view?.showBranchInfo(Preferences.newInstance().branch)
    }

    override fun getUserFromRes() {
        view?.showUserInfo(Preferences.newInstance().profile)
    }

    override fun getCartFromRes() {
        cart = Preferences.newInstance().cart ?: CartModel(arrayListOf())
        cart.listProduct.let {
            cart.listProduct = it.filter { cartProductModel -> cartProductModel.quantity != 0 } as ArrayList<CartProductModel>
        }
        view?.showCartProduct(cart)
    }

    override fun minus(cartProduct: CartProductModel) {
        cart.listProduct.let { list ->
            list.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity--
                    saveCart()
                    if (it.quantity == 0) {
                        getCartFromRes()
                    }
                }
            }
        }
    }

    override fun plus(cartProduct: CartProductModel) {
        cart.listProduct.let { list ->
            list.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity++
                    saveCart()
                }
            }
        }
    }

    private fun saveCart() {
        Preferences.newInstance().cart = cart
        EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
        view?.updateTotalPrice(cart)
    }

}