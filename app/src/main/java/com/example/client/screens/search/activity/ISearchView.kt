package com.example.client.screens.search.activity

import com.example.client.base.IBaseCollectionView
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel

interface ISearchView: IBaseCollectionView {
    fun showData(items: List<ProductModel>)
    fun showMoreData(items: List<ProductModel>)
    fun showEmptyData()
    fun updateData(productModel: ProductModel)
    fun toProductDetailScreen(cate: CategoryModel, prod: ProductModel)
    fun showErrorMessage(errMessage: Int)
}