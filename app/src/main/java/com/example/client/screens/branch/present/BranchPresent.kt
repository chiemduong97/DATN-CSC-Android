package com.example.client.screens.branch.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.branch.BranchModel
import com.example.client.models.branch.toBranches
import com.example.client.models.event.Event
import com.example.client.screens.branch.IBranchView
import com.example.client.usecase.BranchUseCase
import org.greenrobot.eventbus.EventBus

class BranchPresent(mView: IBranchView): BaseCollectionPresenter<IBranchView>(mView), IBranchPresent {
    private val branchUseCase = BranchUseCase.newInstance()
    override fun binData() {
        getBranches()
    }

    override fun getBranches() {
        mView?.showLoading()
        subscribe(branchUseCase.getBranches(), {
            mView?.run {
                hideLoading()
                if (it.isError || it.data.isEmpty()) {
                    showEmptyData()
                    return@subscribe
                }
                Preferences.getInstance().branch?.let { branch ->
                    showData(it.data.toBranches(), branch.id)
                } ?: kotlin.run {
                    showData(it.data.toBranches(), -1)
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
        Preferences.getInstance().branch = branch
        EventBus.getDefault().post(Event(Constants.EventKey.CHANGE_BRANCH))
        mView?.onBackPress()
    }

}