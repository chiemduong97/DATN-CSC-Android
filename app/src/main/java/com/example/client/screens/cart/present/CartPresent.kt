package com.example.client.screens.cart.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.cart.CartModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.event.Event
import com.example.client.screens.cart.activity.ICartView
import org.greenrobot.eventbus.EventBus

class CartPresent(var view: ICartView?): ICartPresent {
    private var cart = Preferences.getInstance().cart ?: CartModel(arrayListOf())
    override fun generationCart() {
        val profile = Preferences.getInstance().profile
        val branch = Preferences.getInstance().branch
        cart.apply {
            order_latitude = profile.latitude
            order_longitude = profile.longitude
            order_address = profile.address
            branch_latitude = branch.latitude
            branch_longitude = branch.longitude
            branch_address = branch.address
        }
        saveCart()

    }

    override fun getBranchFromRes() {
        view?.showBranchInfo(Preferences.getInstance().branch)
    }

    override fun getUserFromRes() {
        view?.showUserInfo(Preferences.getInstance().profile)
    }

    override fun getCartFromRes() {
        cart = Preferences.getInstance().cart ?: CartModel(arrayListOf())
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
        Preferences.getInstance().cart = cart
        EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
        view?.updateTotalPrice(cart)
    }

}