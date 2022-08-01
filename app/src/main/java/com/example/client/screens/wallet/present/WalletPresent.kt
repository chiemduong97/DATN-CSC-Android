package com.example.client.screens.wallet.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.loading.LoadingMode
import com.example.client.models.transaction.toTransactions
import com.example.client.screens.wallet.fragment.IWalletView
import com.example.client.usecase.ProfileUseCase
import com.example.client.usecase.TransactionUseCase

class WalletPresent(mView: IWalletView) : BaseCollectionPresenter<IWalletView>(mView), IWalletPresent {

    private val preferences by lazy { Preferences.newInstance() }
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val transactionUseCase by lazy { TransactionUseCase.newInstance() }

    companion object {
        var currentType = Constants.Transaction.RECHARGE
    }

    override fun getProfile() {
        mView?.showLoading()
        subscribe(profileUseCase.getUserByEmail(preferences.profile.email), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                preferences.profile = it.data.toProfileModel()
                showWallet(preferences.profile)
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
                showWallet(preferences.profile)
            }
        })
    }

    override fun bindData(type: Constants.Transaction) {
        currentType = type
        getTransactions(currentType, page, LoadingMode.LOAD)
    }

    private fun getTransactions(type: Constants.Transaction, page: Int, loadingMode: LoadingMode) {
        if (loadingMode == LoadingMode.LOAD) mView?.showLoading()
        subscribe(transactionUseCase.getTransactions(
            preferences.profile.id,
            type.name,
            page,
            limit
        ), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showEmptyData()
                    onLoadMoreComplete()
                    return@subscribe
                }
                when (loadingMode) {
                    LoadingMode.LOAD -> {
                        if (it.data.isEmpty()) showEmptyData()
                        else {
                            showData(it.data.toTransactions())
                            loadMore = it.load_more
                        }
                    }
                    LoadingMode.LOAD_MORE -> {
                        showMoreData(it.data.toTransactions())
                        loadMore = it.load_more
                        onLoadMoreComplete()
                    }
                }
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                if (loadingMode == LoadingMode.LOAD) {
                    showEmptyData()
                }
            }
            onLoadMoreComplete()
        })
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.RECHARGE_SUCCESS -> getProfile()
                Constants.EventKey.UPDATE_STATUS_ORDER -> mView?.refreshOrderInfo()
            }
        })
    }

    override fun invokeLoadMore(page: Int) {
        super.invokeLoadMore(page)
        getTransactions(currentType, page, LoadingMode.LOAD_MORE)
    }

    override fun onRefresh() {
        super.onRefresh()
        getProfile()
        bindData(currentType)
    }

}