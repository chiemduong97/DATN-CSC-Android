package com.example.client.screens.order.activity

import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.profile.ProfileModel

interface IReviewOrderView {
    fun showUserInfo(profile: ProfileModel)
    fun showBranchInfo(branch: BranchModel)
    fun showCartProduct(cart: CartModel)
    fun showLoading()
    fun hideLoading()
    fun showErrorMessage(errMessage: Int)
    fun toOrderDetailScreen(ordercode: String)
}