package com.example.client.screens.branch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.models.branch.BranchModel
import com.example.client.screens.branch.item.BranchItem
import com.example.client.screens.branch.present.BranchPresent
import kotlinx.android.synthetic.main.activity_branch.imv_back
import kotlinx.android.synthetic.main.activity_branch.imv_empty
import kotlinx.android.synthetic.main.activity_branch.recyclerView
import kotlinx.android.synthetic.main.activity_branch.rll_loading
import kotlinx.android.synthetic.main.activity_branch.swipe_refresh

class BranchActivity : AppCompatActivity(), IBranchView, View.OnClickListener{

    private var present: BranchPresent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch)
        present = BranchPresent(this)
        present?.loadDataFromRes()
        swipe_refresh.setOnRefreshListener {
            present?.loadDataFromRes()
            swipe_refresh.isRefreshing = false
        }
        imv_back.setOnClickListener(this)
    }

    override fun showData(items: List<BranchModel>, selected: Int) {
        recyclerView.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val manager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        val item = BranchItem(this, items, selected) {
            present?.saveBranch(it)
        }
        recyclerView.layoutManager = manager
        recyclerView.adapter = item
    }

    override fun showEmptyData() {
        recyclerView.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun showErrorMessage(errMessage: Int) {

    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
                }
            }
        }
    }
}