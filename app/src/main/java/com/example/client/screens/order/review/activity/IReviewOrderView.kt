package com.example.client.screens.order.review.activity

import com.example.client.app.Constants
import com.example.client.base.IBaseView
import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.profile.ProfileModel

interface IReviewOrderView: IBaseView {
    fun showUser(profile: ProfileModel)
    fun showBranch(branch: BranchModel)
    fun showCartProduct(cart: CartModel)
    fun showErrorMessage(errMessage: Int)
    fun toOrderDetailScreen(orderCode: String)
    fun updateTotalPrice(cart: CartModel)
    fun updatePaymentMethod(paymentMethod: Constants.PaymentMethod, amount: Double)
}