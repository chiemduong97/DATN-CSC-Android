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
import com.example.client.models.order.OrderDetailParam
import com.example.client.models.order.OrderParam
import com.example.client.screens.order.activity.IReviewOrderView
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReviewOrderPresent(var view: IReviewOrderView?) : IReviewOrderPresent {
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
    override fun getUserFromRes() {
        view?.showUserInfo(Preferences.getInstance().profile)
    }

    override fun getBranchFromRes() {
        view?.showBranchInfo(Preferences.getInstance().branch)
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

    override fun createOrder() {
        view?.showLoading()
        val service = ApiClient.getInstance().create(OrderService::class.java)
        service.createOrder(generationOrderParam(cart)).enqueue(object : Callback<MessageModel> {
            override fun onResponse(call: Call<MessageModel>, response: Response<MessageModel>) {
                response.body()?.let {
                    when {
                        it.isStatus -> {
                            view?.toOrderDetailScreen(it.ordercode)
                            Preferences.getInstance().removeCart()
                            EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
                        }
                        else -> {
                            view?.showErrorMessage(it.code)
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
        Preferences.getInstance().cart = cart
        EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
        getCartFromRes()
    }

    private fun generationOrderParam(cart: CartModel): OrderParam {

        return OrderParam(
                Preferences.getInstance().profile.id,
                Preferences.getInstance().branch.id,
                null,
                cart.order_latitude,
                cart.order_longitude,
                cart.order_address,
                generationListOrderDetailParam(cart.listProduct),
                cart.branch_latitude,
                cart.branch_longitude,
                cart.branch_address,
                cart.getShippingFeeExpect()
        )

    }

    private fun generationListOrderDetailParam(list: ArrayList<CartProductModel>): ArrayList<OrderDetailParam> {
        val listOrderDetailParam = arrayListOf<OrderDetailParam>()
        list.forEach {
            listOrderDetailParam.add(OrderDetailParam(it.quantity, it.product.id))
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
        }
        return errMessage
    }

}