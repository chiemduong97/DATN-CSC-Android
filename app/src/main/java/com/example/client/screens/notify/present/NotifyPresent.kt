package com.example.client.screens.notify.present

import com.example.client.app.Preferences
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.loading.LoadingMode
import com.example.client.models.notify.toNotifies
import com.example.client.screens.notify.INotifyView
import com.example.client.usecase.NotifyUseCase

class NotifyPresent(mView: INotifyView) : BaseCollectionPresenter<INotifyView>(mView), INotifyPresent {
    private val notifyUseCase by lazy { NotifyUseCase.newInstance() }

    override fun bindData() {
        getNotifies(page, LoadingMode.LOAD)
    }

    override fun getNotifies(page: Int, loadingMode: LoadingMode) {
        mView?.showLoading()
        subscribe(notifyUseCase.getNotifies(Preferences.newInstance().profile.id, page, limit), {
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
                            showData(it.data.toNotifies())
                            loadMore = it.load_more
                        }
                    }
                    LoadingMode.LOAD_MORE -> {
                        showMoreData(it.data.toNotifies())
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
                    showErrorMessage(getErrorMessage(1001))
                }
            }
        })
    }

    override fun invokeLoadMore(page: Int) {
        super.invokeLoadMore(page)
        getNotifies(page, LoadingMode.LOAD_MORE)
    }

    override fun onRefresh() {
        super.onRefresh()
        bindData()
    }
}