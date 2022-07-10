package com.example.client.screens.branch.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.branch.BranchModel
import com.example.client.models.branch.toBranches
import com.example.client.models.event.Event
import com.example.client.screens.branch.IBranchView
import com.example.client.usecase.BranchUseCase
import com.google.android.gms.maps.model.LatLng

class BranchPresent(mView: IBranchView) : BaseCollectionPresenter<IBranchView>(mView), IBranchPresent {
    private val branchUseCase by lazy { BranchUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    override fun binData() {
        getBranches()
    }

    override fun getBranches() {
        mView?.showLoading()
        subscribe(branchUseCase.getBranches(), {
            mView?.run {
                hideLoading()
                if (it.is_error || it.data.isEmpty()) {
                    showEmptyData()
                    return@subscribe
                }
                preferences.branch?.let { branch ->
                    showData(it.data.toBranches(LatLng(preferences.profile.lat, preferences.profile.lng)).sortedByDescending { it.distance }.reversed(), branch.id)
                } ?: kotlin.run {
                    showData(it.data.toBranches(LatLng(preferences.profile.lat, preferences.profile.lng)).sortedByDescending { it.distance }.reversed(), -1)
                }
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun saveBranch(branch: BranchModel) {
        preferences.branch = branch
        preferences.cart = preferences.cart.apply {
            branch_lat = branch.lat
            branch_lng = branch.lng
            branch_address = branch.address
        }
        RxBus.newInstance().onNext(Event(Constants.EventKey.CHANGE_BRANCH))
        mView?.onBackPress()
    }

}