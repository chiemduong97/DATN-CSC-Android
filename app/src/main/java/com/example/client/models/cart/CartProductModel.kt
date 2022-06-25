package com.example.client.models.cart

import com.example.client.models.order.OrderDetailModel
import com.example.client.models.product.ProductModel

data class CartProductModel(var product: ProductModel, var quantity: Int) {
    fun getPrice() : Double{
        return product.price * quantity
    }
}
fun List<CartProductModel>.toOrderDetails(): List<OrderDetailModel> {
    val listOrderDetailParam = arrayListOf<OrderDetailModel>()
    forEach {
        listOrderDetailParam.add(OrderDetailModel(it.quantity, it.product.id, it.product.price, it.product.name))
    }
    return listOrderDetailParam
}