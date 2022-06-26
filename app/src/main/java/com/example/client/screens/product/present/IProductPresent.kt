package com.example.client.screens.product.present

import com.example.client.base.IBaseCollectionPresenter
import com.example.client.models.cart.CartModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.loading.LoadingMode
import com.example.client.models.product.ProductModel

interface IProductPresent: IBaseCollectionPresenter {
    fun binData(categoryModel: CategoryModel)
    fun getProducts(category_id: Int, page: Int, loadingMode: LoadingMode)
    fun onClickItem(productModel: ProductModel)
    fun getCartFromRes(): CartModel
}