package com.example.client.screens.home.present

import com.example.client.base.IBasePresenter

interface IHomePresent: IBasePresenter {
    fun getCategoriesParent()
    fun getListBannerFromService()
    fun getProductsHighLightFromService()
    fun getProductNewFromService()
    fun getBranchFromRes()
    fun getUserFromRes()
}