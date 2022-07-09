package com.example.client.screens.order.review.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.cart.CartModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.cart.toOrderDetails
import com.example.client.models.event.Event
import com.example.client.models.event.ValueEvent
import com.example.client.models.order.OrderRequest
import com.example.client.screens.order.review.activity.IReviewOrderView
import com.example.client.usecase.OrderUseCase

class ReviewOrderPresent(mView: IReviewOrderView) : BasePresenterMVP<IReviewOrderView>(mView), IReviewOrderPresent {
    private val orderUseCase by lazy { OrderUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    private var cart = preferences.cart
    override fun binData() {
        mView?.run {
            if (preferences.paymentMethod == null) preferences.paymentMethod = Constants.PaymentMethod.COD
            updatePaymentMethod(preferences.paymentMethod, preferences.profile.wallet)
            preferences.cart = preferences.cart.apply {
                this.order_lat = preferences.profile.lat
                this.order_lng = preferences.profile.lng
                this.order_address = preferences.profile.address
                this.branch_lat = preferences.branch.lat
                this.branch_lng = preferences.branch.lng
                this.branch_address = preferences.branch.address
            }
            showBranch(preferences.branch)
            showUser(preferences.profile)
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

    override fun createOrder() {
        mView?.showLoading()
        subscribe(orderUseCase.createOrder(generationOrderRequest(cart)), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                toOrderDetailScreen(it.data.order_code)
                preferences.deleteCart()
                RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_CART))
                RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_STATUS_ORDER))
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    private fun saveCart(cartProduct: CartProductModel) {
        preferences.cart = cart
        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_CART))
        RxBus.newInstance().onNext(ValueEvent(Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT, cartProduct.product.checkAddToCart(cart)))
        mView?.updateTotalPrice(cart)
    }

    private fun generationOrderRequest(cart: CartModel) = OrderRequest(
            preferences.profile.id,
            preferences.branch.id,
            null,
            cart.order_lat,
            cart.order_lng,
            cart.order_address,
            cart.cartProducts.toOrderDetails(),
            cart.branch_lat,
            cart.branch_lng,
            cart.branch_address,
            cart.getShippingFeeExpect(),
            preferences.profile.phone,
            preferences.paymentMethod
    )

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.CHANGE_BRANCH -> mView?.run {
                    showBranch(preferences.branch)
                    updateTotalPrice(preferences.cart)
                }
                Constants.EventKey.UPDATE_LOCATION -> mView?.run {
                    showUser(preferences.profile)
                    updateTotalPrice(preferences.cart)
                }
                Constants.EventKey.CHANGE_PAYMENT_METHOD -> mView?.updatePaymentMethod(preferences.paymentMethod, preferences.profile.wallet)
            }
        })
    }

}