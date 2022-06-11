package com.example.client.screens.product.detail.present

import com.example.client.R
import com.example.client.api.ApiClient
import com.example.client.api.service.ProductService
import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.cart.CartModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.event.Event
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.detail.IProductDetailView
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailPresent(private var view: IProductDetailView?) : IProductDetailPresent {
    private var cart = Preferences.getInstance().cart ?: CartModel(arrayListOf())
    override fun loadDataByCategory(category_id: Int) {
//        view?.showLoading()
//        val service = ApiClient.getInstance().create(ProductService::class.java)
//        val branch = Preferences.getInstance().branch
//        service.getByCategory(category_id, branch.id).enqueue(object : Callback<List<ProductModel>> {
//            override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>?>) {
//                response.body()?.let {
//                    when {
//                        it.isEmpty() -> {
//                            view?.showListEmpty()
//                        }
//                        else -> {
//                            view?.showListProduct(it)
//                        }
//                    }
//                    view?.hideLoading()
//                } ?: kotlin.run {
//                    view?.run {
//                        showListEmpty()
//                        hideLoading()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
//                view?.run {
//                    showListEmpty()
//                    hideLoading()
//                }
//            }
//        })
    }

    override fun addToCart(cartProduct: CartProductModel) {
        cart.listProduct.let { list ->
            list.map {
                if (it.product.id == cartProduct.product.id) {
                    it.quantity += cartProduct.quantity
                    saveCart()
                    return
                }
            }
            list.add(cartProduct)
            saveCart()
        }
    }

    private fun saveCart() {
        Preferences.getInstance().cart = cart
        EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_CART))
    }

    override fun getCartFromRes() {
        cart = Preferences.getInstance().cart ?: CartModel(arrayListOf())
        cart.listProduct.let {
            cart.listProduct = it.filter { cartProductModel -> cartProductModel.quantity != 0 } as ArrayList<CartProductModel>
        }
        cart.let {
            it.listProduct.let { products ->
                if(products.isNotEmpty()) {
                    view?.showCart(products.size)
                } else {
                    view?.hideCart()
                }
            }
        }
    }

    override fun plus(product: ProductModel) {
        TODO("Not yet implemented")
    }

    override fun minus(product: ProductModel) {
        TODO("Not yet implemented")
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