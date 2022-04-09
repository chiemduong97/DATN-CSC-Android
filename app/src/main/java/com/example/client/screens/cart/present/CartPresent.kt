package com.example.client.screens.cart.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.cart.CartModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.event.Event
import com.example.client.screens.cart.activity.ICartView
import org.greenrobot.eventbus.EventBus

class CartPresent(var view: ICartView?): ICartPresent {
    private var cart = Preferences.getInstance().carModel ?: CartModel(arrayListOf())

    override fun getBranchFromRes() {
        view?.showBranchInfo(Preferences.getInstance().branchModel)
    }

    override fun getUserFromRes() {
        view?.showUserInfo(Preferences.getInstance().profile)
    }

    override fun getCartFromRes() {
        cart = Preferences.getInstance().carModel ?: CartModel(arrayListOf())
        cart.listProduct?.let {
            cart.listProduct = it.filter { cartProductModel -> cartProductModel.quantity != 0 } as ArrayList<CartProductModel>
        }
        view?.showCartProduct(cart)
    }

    override fun minus(cartProduct: CartProductModel) {
        cart.listProduct?.let { list ->
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
        cart.listProduct?.let { list ->
            list.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity++
                    saveCart()
                }
            }
        }
    }

    private fun saveCart() {
        Preferences.getInstance().setCartModel(cart)
        EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
    }

}