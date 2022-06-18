package com.example.client.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.client.R
import kotlinx.android.synthetic.main.fragment_base_collection_layout.*


abstract class BaseCollectionFragment<P : IBaseCollectionPresenter> : BaseFragmentMVP<P>(), IBaseCollectionView, SwipeRefreshLayout.OnRefreshListener {

    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var recyclerView: RecyclerView? = null
    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView?.adapter = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(getLayout(), null)
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh)
        recyclerView = rootView.findViewById(R.id.recycler_view)
        swipeRefreshLayout?.isEnabled = true
        swipeRefreshLayout?.setOnRefreshListener(this)

        recyclerView?.run {
            setHasFixedSize(true)
            scrollTo(0, 0)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val linearLayoutManager: LinearLayoutManager = layoutManager as LinearLayoutManager
                    val totalItemCount: Int = linearLayoutManager.itemCount
                    val lastVisibleItemPosition: Int = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if (shouldLoadMore() && lastVisibleItemPosition == totalItemCount - 1) {
                        mPresenter?.loadMore()
                    }
                }
            })
        }
        return rootView
    }

    override fun onRefresh() {
        mPresenter?.onRefresh()
    }

    protected open fun shouldLoadMore(): Boolean {
        return true
    }

    override fun showLoading() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideLoading() {
        if (swipeRefreshLayout?.isRefreshing == true) swipeRefreshLayout?.isRefreshing = false
    }

    override fun addLoadMore() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun removeLoadMore() {
        progress_bar.visibility = View.GONE
    }

    protected open fun getLayout(): Int {
        return R.layout.fragment_base_collection_layout
    }
}