package com.example.client.screens.order.history.present

import com.example.client.api.ApiClient
import com.example.client.api.service.OrderService
import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.event.ValueEvent
import com.example.client.models.loading.LoadingMode
import com.example.client.models.order.OrderModel
import com.example.client.models.order.toOrders
import com.example.client.models.product.ProductModel
import com.example.client.models.product.checkCart
import com.example.client.models.product.toProducts
import com.example.client.screens.order.history.activity.IOrderHistoryView
import com.example.client.screens.product.present.ProductPresent
import com.example.client.usecase.OrderUseCase

class OrderHistoryPresent(mView: IOrderHistoryView) : BaseCollectionPresenter<IOrderHistoryView>(mView), IOrderHistoryPresent {
    private val orderUseCase by lazy { OrderUseCase.newInstance() }

    override fun bindData() {
        getOrders(page, LoadingMode.LOAD)
    }

    override fun getOrders(page: Int, loadingMode: LoadingMode) {
        mView?.showLoading()
        subscribe(orderUseCase.getOrders(Preferences.newInstance().profile.id, page, limit), {
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
                            showData(it.data.toOrders())
                            loadMore = it.load_more
                        }
                    }
                    LoadingMode.LOAD_MORE -> {
                        showMoreData(it.data.toOrders())
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

    override fun onClickItem(orderCode: String) {
        mView?.goToOrderDetailScreen(orderCode)
    }

    override fun invokeLoadMore(page: Int) {
        super.invokeLoadMore(page)
        getOrders(page, LoadingMode.LOAD_MORE)
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.UPDATE_ORDER -> {
                    val orderModel = (it as ValueEvent<*>).value
                    mView?.updateData(orderModel as OrderModel)
                }
            }
        })
    }

    override fun onRefresh() {
        super.onRefresh()
        bindData()
    }

}