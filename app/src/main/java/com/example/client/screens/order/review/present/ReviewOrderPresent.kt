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
import com.example.client.models.order.*
import com.example.client.screens.order.review.activity.IReviewOrderView
import com.example.client.usecase.OrderUseCase

class ReviewOrderPresent(mView: IReviewOrderView) : BasePresenterMVP<IReviewOrderView>(mView), IReviewOrderPresent {
    private val orderUseCase by lazy { OrderUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    override fun binData(isReOrder: Boolean, order: OrderModel?) {

        if (isReOrder && order != null) {
            binDataForReOrder(order.order_details)
        } else {
            mView?.run {
                if (preferences.paymentMethod == null) preferences.paymentMethod = Constants.PaymentMethod.COD
                updatePaymentMethod(preferences.paymentMethod, preferences.profile.wallet)
                preferences.cart = preferences.cart.apply {
                    this.order_lat = preferences.orderLocation.lat
                    this.order_lng = preferences.orderLocation.lng
                    this.order_address = preferences.orderLocation.address
                    this.branch_lat = preferences.branch.lat
                    this.branch_lng = preferences.branch.lng
                    this.branch_address = preferences.branch.address
                }
                showBranch(preferences.branch)
                showUser(preferences.profile)
                showOrderLocation(preferences.orderLocation)
                getCartFromRes()
                updatePromotion(preferences.cart)
            }
        }
    }

    private fun binDataForReOrder(orderDetails: List<OrderDetailModel>) {
        mView?.apply {
            preferences.cart = preferences.cart.apply {
                cartProducts = orderDetails.toCartProducts() as ArrayList<CartProductModel>
            }
            if (preferences.paymentMethod == null) preferences.paymentMethod = Constants.PaymentMethod.COD
            updatePaymentMethod(preferences.paymentMethod, preferences.profile.wallet)
            preferences.cart = preferences.cart.apply {
                this.order_lat = preferences.orderLocation.lat
                this.order_lng = preferences.orderLocation.lng
                this.order_address = preferences.orderLocation.address
                this.branch_lat = preferences.branch.lat
                this.branch_lng = preferences.branch.lng
                this.branch_address = preferences.branch.address
            }
            showBranch(preferences.branch)
            showUser(preferences.profile)
            showOrderLocation(preferences.orderLocation)
            getCartFromRes()
            updatePromotion(preferences.cart)
            RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_CART))
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
        } else saveCart(cartProduct)
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

    override fun createOrderWithMomo(customerNumber: String, appData: String) {
        mView?.showLoading()
        subscribe(orderUseCase.createOrder(generationOrderRequest(preferences.cart).apply {
            this.customerNumber = customerNumber
            this.appData = appData
            this.amount = preferences.cart.getTotalPrice()
        }), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                createOrderSuccess(it.data.order_code)
                preferences.deleteCart()
                RxBus.newInstance().onNext(Event(Constants.EventKey.DELETE_CART))
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

    override fun createOrderWithWallet() {

        if (preferences.profile.wallet < preferences.cart.getTotalPrice()) {
            mView?.showErrorMessage(getErrorMessage(1017))
            return
        }

        mView?.showLoading()
        subscribe(orderUseCase.createOrder(generationOrderRequest(preferences.cart).apply { this.amount = preferences.cart.getTotalPrice() }), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                createOrderSuccess(it.data.order_code)
                preferences.profile = preferences.profile.apply { wallet -= preferences.cart.getTotalPrice() }
                preferences.deleteCart()
                RxBus.newInstance().onNext(Event(Constants.EventKey.DELETE_CART))
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


    override fun createOrder() {
        mView?.showLoading()
        subscribe(orderUseCase.createOrder(generationOrderRequest(preferences.cart)), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                createOrderSuccess(it.data.order_code)
                preferences.deleteCart()
                RxBus.newInstance().onNext(Event(Constants.EventKey.DELETE_CART))
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

    override fun removePromotion() {
        preferences.cart = preferences.cart.apply {
            promotion_id = null
            promotion_code = null
            promotion_value = null
        }
        mView?.updatePromotion(preferences.cart)
    }

    override fun requestMomo() {
        mView?.requestMomo(preferences.cart)
    }

    private fun saveCart(cartProduct: CartProductModel) {
        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_CART))
        RxBus.newInstance().onNext(ValueEvent(Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT, cartProduct.product.checkAddToCart(preferences.cart)))
        mView?.run {
            updateTotalPrice(preferences.cart)
            updatePromotion(preferences.cart)
        }
    }

    private fun generationOrderRequest(cart: CartModel) = OrderRequest(
            preferences.profile.id,
            preferences.branch.id,
            cart.promotion_id,
            cart.order_address,
            cart.cartProducts.toOrderDetails(),
            cart.getShippingFeeExpect(),
            preferences.profile.phone,
            cart.getDistance(),
            preferences.paymentMethod
    )

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.CHANGE_BRANCH -> mView?.run {
                    showBranch(preferences.branch)
                    showCartProduct(preferences.cart)
                }
                Constants.EventKey.UPDATE_LOCATION -> mView?.run {
                    showOrderLocation(preferences.orderLocation)
                    showCartProduct(preferences.cart)
                }
                Constants.EventKey.CHANGE_PAYMENT_METHOD -> mView?.updatePaymentMethod(preferences.paymentMethod, preferences.profile.wallet)
                Constants.EventKey.UPDATE_PROMOTION -> mView?.updatePromotion(preferences.cart)
            }
        })
    }

}