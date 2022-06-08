package com.example.client.screens.home.present

import com.example.client.api.ApiClient
import com.example.client.api.service.BannerService
import com.example.client.app.Preferences
import com.example.client.base.BasePresenter
import com.example.client.models.banner.BannerModel
import com.example.client.models.category.toCategories
import com.example.client.usecase.CategoryUseCase
import com.example.client.screens.home.fragment.IHomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomePresent(mView: IHomeView) : BasePresenter<IHomeView>(mView), IHomePresent {
    private val categoryUseCase = CategoryUseCase.newInstance()

    override fun getCategoriesParent() {
        add(categoryUseCase.getParents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.isError) {
                        mView?.showCategories(arrayListOf())
                        return@subscribe
                    }
                    mView?.showCategories(it.data.toCategories())
                }, {
                    mView?.showCategories(arrayListOf())
                })
        )
    }

    override fun getListBannerFromService() {
        val service = ApiClient.getInstance().create(BannerService::class.java)
        service.all.enqueue(object : Callback<List<BannerModel?>?> {
            override fun onResponse(call: Call<List<BannerModel?>?>, response: Response<List<BannerModel?>?>) {
                mView?.showBanners(response.body())
            }

            override fun onFailure(call: Call<List<BannerModel?>?>, t: Throwable) {
                mView?.showBanners(ArrayList())
            }
        })
    }

    override fun getProductsHighLightFromService() {}
    override fun getProductNewFromService() {}
    override fun getBranchFromRes() {
        if (Preferences.getInstance().branch == null) {
            mView!!.toBranchScreen()
            return
        }
        mView?.showBranchInfo(Preferences.getInstance().branch)
    }

    override fun getUserFromRes() {
        mView?.showUserInfo(Preferences.getInstance().profile)
    }
}