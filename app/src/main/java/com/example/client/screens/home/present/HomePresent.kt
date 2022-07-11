package com.example.client.screens.home.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.category.MAX_ITEM_CATEGORY
import com.example.client.models.category.toCategories
import com.example.client.screens.home.fragment.IHomeView
import com.example.client.usecase.CategoryUseCase

class HomePresent(mView: IHomeView) : BasePresenterMVP<IHomeView>(mView), IHomePresent {
    private val categoryUseCase by lazy { CategoryUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    override fun binData() {
        mView?.run {
            showProfile(preferences.profile)
            showOrderLocation(preferences.orderLocation)
            preferences.branch?.let {
                showBranch(it)
            }
        }
    }

    override fun getCategories() {
        mView?.showLoading()
        subscribe(categoryUseCase.getSuperCategories(), {
            mView?.run {
                hideLoading()
                when {
                    it.is_error -> {
                        hideCategories()
                        showErrorMessage(getErrorMessage(it.code))
                    }
                    it.data.isNullOrEmpty() -> {
                        hideCategories()
                        showErrorMessage(getErrorMessage(1001))
                    }
                    else -> {
                        showCategories(it.data.toCategories().subList(0, MAX_ITEM_CATEGORY))
                    }
                }
            }

        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                hideCategories()
            }
        })
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.CHANGE_BRANCH -> mView?.showBranch(preferences.branch)
                Constants.EventKey.UPDATE_PROFILE_AVATAR, Constants.EventKey.UPDATE_PROFILE_INFO -> mView?.showProfile(preferences.profile)
                Constants.EventKey.UPDATE_LOCATION -> mView?.showOrderLocation(preferences.orderLocation)
                Constants.EventKey.UPDATE_LOCATION_WHEN_RUN_APP -> {
                    mView?.showOrderLocation(preferences.orderLocation)
                    preferences.branch?.run {
                        mView?.showBranch(this)
                    }
                }
            }
        })
    }
}