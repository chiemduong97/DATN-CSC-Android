package com.example.client.models.cart

import com.example.client.models.branch.BranchModel
import com.example.client.models.profile.ProfileModel

class CartModel(
        var listProduct: ArrayList<CartProductModel>?
){
    public fun getAmount():Double {
        var amount = 0.0;
        listProduct?.let {
            if (it.isNotEmpty()) {
                it.forEach { cartProduct ->
                    amount += cartProduct.product.price.times(cartProduct.quantity)
                }
            }
        }
        return amount
    }
}