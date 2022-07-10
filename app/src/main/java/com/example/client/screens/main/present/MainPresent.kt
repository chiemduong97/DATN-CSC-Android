package com.example.client.screens.main.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.cart.CartProductModel
import com.example.client.models.order.OrderModel
import com.example.client.screens.main.activity.IMainView
import com.example.client.usecase.OrderUseCase
import com.example.client.usecase.ProfileUseCase
import java.util.*

class MainPresent(mView: IMainView) : BasePresenterMVP<IMainView>(mView), IMainPresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val orderUseCase by lazy { OrderUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    companion object {
        private var orderModel: OrderModel? = null
    }

    override fun setUserActive(email: String) {
        mView?.showLoading()
        subscribe(profileUseCase.getUserByEmail(email), {
            mView?.run {
                if (it.is_error) {
                    hideLoading()
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                preferences.profile = it.data.toProfileModel()
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun bindData() {
        getCart()
        getCountOrder()
        getOrder()
    }

    private fun getCart() {
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

    private fun getCountOrder() {
        mView?.showLoading()
        subscribe(orderUseCase.getCountOrder(preferences.profile.id), {
            mView?.run {
                hideLoading()
                when {
                    it.is_error -> {
                        hideOrderCount()
                        showErrorMessage(getErrorMessage(it.code))
                    }
                    it.data.count != 0 -> showOrderCount(it.data.count)
                    else -> hideOrderCount()
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

    private fun getOrder() {
        mView?.showLoading()
        subscribe(orderUseCase.getOrders(preferences.profile.id, 1, 1), {
            mView?.run {
                hideLoading()
                when {
                    it.is_error -> {
                        hideOrder()
                        showErrorMessage(getErrorMessage(it.code))
                    }
                    it.data.isNullOrEmpty() -> hideOrder()
                    else -> {
                        orderModel = it.data[0].toOrderModel()
                        showOrder(it.data[0].toOrderModel())
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

    override fun navigateToOrderDetail() {
        orderModel?.let { mView?.navigateToOrderDetail(it.order_code) }
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.UPDATE_CART -> {
                    getCart()
                }
                Constants.EventKey.UPDATE_STATUS_ORDER -> {
                    getOrder()
                    getCountOrder()
                }
                Constants.EventKey.UPDATE_LOCATION_WHEN_RUN_APP -> if (preferences.branch == null) mView?.toBranchScreen()
            }
        })
    }

}