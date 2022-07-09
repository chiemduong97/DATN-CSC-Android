package com.example.client.screens.search.present

import com.example.client.base.IBaseCollectionPresenter
import com.example.client.models.product.ProductModel

interface ISearchPresent: IBaseCollectionPresenter {
    fun searchProducts(query: String)
    fun onClickItem(product: ProductModel)
}