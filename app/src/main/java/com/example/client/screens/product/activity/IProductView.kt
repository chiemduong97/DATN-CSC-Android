package com.example.client.screens.product.activity

import com.example.client.base.IBaseCollectionView
import com.example.client.models.product.ProductModel

interface IProductView: IBaseCollectionView {
    fun showData(items: List<ProductModel>)
    fun showMoreData(items: List<ProductModel>)
    fun showEmptyData()
    fun showErrorMessage(errMessage: Int)
    fun navigateToProductDetailScreen(productModel: ProductModel)
    fun updateData(productModel: ProductModel)
}