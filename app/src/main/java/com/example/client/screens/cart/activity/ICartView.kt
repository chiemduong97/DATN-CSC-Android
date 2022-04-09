package com.example.client.screens.cart.activity

import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.profile.ProfileModel

interface ICartView {
    fun showBranchInfo(branch: BranchModel)
    fun showUserInfo(profile: ProfileModel)
    fun showCartProduct(cart : CartModel)
}