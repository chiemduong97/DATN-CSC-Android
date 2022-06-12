package com.example.client.screens.order.present

import com.example.client.R
import com.example.client.api.ApiClient
import com.example.client.api.service.OrderService
import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.cart.CartModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.event.Event
import com.example.client.models.message.MessageModel
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderParam
import com.example.client.screens.order.activity.IReviewOrderView
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewOrderPresent(var view: IReviewOrderView?) : IReviewOrderPresent {
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
    override fun getUserFromRes() {
        view?.showUserInfo(Preferences.newInstance().profile)
    }

    override fun getBranchFromRes() {
        view?.showBranchInfo(Preferences.newInstance().branch)
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

    override fun createOrder() {
        view?.showLoading()
        val service = ApiClient.newInstance().create(OrderService::class.java)
        service.createOrder(generationOrderParam(cart)).enqueue(object : Callback<MessageModel> {
            override fun onResponse(call: Call<MessageModel>, response: Response<MessageModel>) {
                response.body()?.let {
                    when {
                        it.isStatus -> {
                            view?.toOrderDetailScreen(it.ordercode)
                            Preferences.newInstance().deleteCart()
                            EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
                            EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_STATUS_ORDER))
                        }
                        else -> {
                            view?.showErrorMessage(getErrorMessage(it.code))
                        }
                    }
                    view?.hideLoading()
                } ?: kotlin.run {
                    view?.run {
                        showErrorMessage(getErrorMessage(1001))
                        hideLoading()
                    }
                }
            }

            override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                view?.run {
                    showErrorMessage(getErrorMessage(1001))
                    hideLoading()
                }
            }
        } )

    }

    private fun saveCart() {
        Preferences.newInstance().cart = cart
        EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
        getCartFromRes()
    }

    private fun generationOrderParam(cart: CartModel): OrderParam {

        return OrderParam(
                Preferences.newInstance().profile.id,
                Preferences.newInstance().branch.id,
                null,
                cart.order_latitude,
                cart.order_longitude,
                cart.order_address,
                generationListOrderDetail(cart.listProduct),
                cart.branch_latitude,
                cart.branch_longitude,
                cart.branch_address,
                cart.getShippingFeeExpect(),
                Preferences.newInstance().profile.phone
        )

    }

    private fun generationListOrderDetail(list: ArrayList<CartProductModel>): ArrayList<OrderDetailModel> {
        val listOrderDetailParam = arrayListOf<OrderDetailModel>()
        list.forEach {
            listOrderDetailParam.add(OrderDetailModel(it.quantity, it.product.id, 0.0, it.product.name))
        }
        return listOrderDetailParam
    }

    private fun getErrorMessage(errCode: Int): Int {
        var errMessage = -1
        when (errCode) {
            Constants.ErrorCode.ERROR_1001 -> errMessage = R.string.err_code_1001
            Constants.ErrorCode.ERROR_1002 -> errMessage = R.string.err_code_1002
            Constants.ErrorCode.ERROR_1003 -> errMessage = R.string.err_code_1003
            Constants.ErrorCode.ERROR_1004 -> errMessage = R.string.err_code_1004
            Constants.ErrorCode.ERROR_1005 -> errMessage = R.string.err_code_1005
            Constants.ErrorCode.ERROR_1006 -> errMessage = R.string.err_code_1006
            Constants.ErrorCode.ERROR_1007 -> errMessage = R.string.err_code_1007
            Constants.ErrorCode.ERROR_1008 -> errMessage = R.string.err_code_1008
            Constants.ErrorCode.ERROR_1009 -> errMessage = R.string.err_code_1009
            Constants.ErrorCode.ERROR_1010 -> errMessage = R.string.err_code_1010
            Constants.ErrorCode.ERROR_1011 -> errMessage = R.string.err_code_1011
            Constants.ErrorCode.ERROR_1012 -> errMessage = R.string.err_code_1012
            Constants.ErrorCode.ERROR_1013 -> errMessage = R.string.err_code_1013
            Constants.ErrorCode.ERROR_1014 -> errMessage = R.string.err_code_1014

        }
        return errMessage
    }

}