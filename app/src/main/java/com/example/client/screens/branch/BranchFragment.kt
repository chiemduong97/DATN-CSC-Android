package com.example.client.screens.branch

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.base.BaseCollectionFragment
import com.example.client.base.IBaseCollectionPresenter
import com.example.client.models.branch.BranchModel
import com.example.client.screens.branch.item.BranchItem
import com.example.client.screens.branch.present.BranchPresent
import com.example.client.screens.branch.present.IBranchPresent
import kotlinx.android.synthetic.main.fragment_branch.*

class BranchFragment: BaseCollectionFragment<IBranchPresent>(), IBranchView, View.OnClickListener {
    private val manager by lazy { LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) }

    companion object {
        @JvmStatic
        fun newInstance() = BranchFragment()
    }
    override val presenter: BranchPresent
        get() = BranchPresent(this)

    override fun onRefresh() {
        presenter.binData()
    }

    override fun bindData() {
        presenter.binData()
    }

    override fun bindEvents() {
        imv_back.setOnClickListener(this)
    }

    override fun showData(items: List<BranchModel>, selected: Int) {
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val item = BranchItem(context, items, selected) {
            presenter.saveBranch(it)
        }
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showEmptyData() {
        recycler_view.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showErrorMessage(errMessage: Int) {
        showToastMessage(getString(errMessage))
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.imv_back) {
            activity?.onBackPressed()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_branch
    }

    override fun onBackPress() {
        activity?.onBackPressed()
    }

    override fun shouldLoadMore(): Boolean {
        return false
    }

}