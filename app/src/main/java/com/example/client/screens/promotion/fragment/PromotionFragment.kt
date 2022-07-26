package com.example.client.screens.promotion.fragment

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.promotion.PromotionModel
import com.example.client.screens.promotion.activity.IPromotionView
import com.example.client.screens.promotion.item.PromotionItem
import com.example.client.screens.promotion.navigate.NavigatorPromotion
import com.example.client.screens.promotion.present.IPromotionPresent
import com.example.client.screens.promotion.present.PromotionPresent
import com.jakewharton.rxbinding2.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_promotion.*
import java.util.concurrent.TimeUnit

class PromotionFragment: BaseCollectionFragment<IPromotionPresent>(), IPromotionView, SearchView.OnQueryTextListener{

    companion object {
        @JvmStatic
        fun newInstance() = PromotionFragment()
    }

    override val presenter: IPromotionPresent
        get() = PromotionPresent(this)

    override fun getLayout() = R.layout.fragment_promotion

    override fun shouldLoadMore() = false

    override fun bindData() {
        presenter.getPromotions()
        search_view.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        val searchTextId = search_view.context.resources.getIdentifier("android:id/search_src_text", null, null)
        TextViewCompat.setTextAppearance(search_view.findViewById(searchTextId), R.style.search_text)
    }

    override fun bindEvent() {
        imv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        swipe_refresh.setOnRefreshListener {
            presenter.getPromotions()
        }
        search_view.setOnQueryTextListener(this)
        val searchCloseButtonId = search_view.context.resources.getIdentifier("android:id/search_close_btn", null, null)
        search_view.findViewById<ImageView>(searchCloseButtonId).setOnClickListener {
            search_view.setQuery("", false)
            onRefresh()
        }

    }

    override fun showData(items: List<PromotionModel>, promotion_id: Int) {
        imv_empty.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val item = PromotionItem(requireContext(), items, promotion_id, {
            NavigatorPromotion.showPromotionDetailScreen(Bundle().apply {
                putSerializable(Constants.BundleKey.PROMOTION_MODEL, it)
            })
        }, {
            presenter.usePromotion(it)
        }, {
            presenter.removePromotion()
        })
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showEmptyData() {
        imv_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        presenter.getPromotion(query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.trim { it <= ' ' }.length >= 2) {
            add(RxSearchView.queryTextChanges(search_view)
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .filter { str: CharSequence -> !TextUtils.isEmpty(str.toString().trim { it <= ' ' }) && str.toString().trim { it <= ' ' }.isNotEmpty() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        presenter.getPromotion(it.toString())
                    }, {
                        it.printStackTrace()
                    })
            )
        }
        return true
    }
}