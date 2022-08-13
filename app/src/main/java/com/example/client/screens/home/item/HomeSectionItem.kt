package com.example.client.screens.home.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.category.HomeSectionModel
import com.example.client.models.product.ProductModel
import com.example.client.models.product.applyHomeSection
import com.example.client.models.product.toProducts
import com.example.client.screens.category.parent.item.ProductsItemViewHolder
import com.example.client.screens.product.item.ProductHorizontalItem
import com.example.client.usecase.ProductUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeSectionItem(
        private val context: Context,
        private val items: List<HomeSectionModel>,
        private val onClickMore: (homeSection: HomeSectionModel) -> Unit,
        private val onClickProduct: (product: ProductModel) -> Unit,
        private val onAddToCart: (product: ProductModel) -> Unit,
) : RecyclerView.Adapter<ProductsItemViewHolder>() {
    private val compositeDisposable = CompositeDisposable()
    private val productUseCase by lazy { ProductUseCase.newInstance() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ProductsItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_products, null))

    override fun onBindViewHolder(viewHolder: ProductsItemViewHolder, position: Int) {
        viewHolder.apply {
            val homeSection = items[position]
            tvName?.text = homeSection.title
            tvMore?.setOnClickListener {
                onClickMore.invoke(homeSection)
            }
            rllLoading?.visibility = View.VISIBLE
            compositeDisposable.add(
                productUseCase.getProductsByUrl(homeSection.url, 1, 10)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        rllLoading?.visibility = View.GONE
                        if (response.is_error || response.data.isNullOrEmpty()) {
                            imvEmpty?.visibility = View.VISIBLE
                            recyclerView?.visibility = View.GONE
                        } else {
                            imvEmpty?.visibility = View.GONE
                            recyclerView?.visibility = View.VISIBLE
                            val manager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            recyclerView?.let {
                                it.layoutManager = manager
                                val item = ProductHorizontalItem(
                                    context, response.data.applyHomeSection(homeSection) + ProductModel(),
                                    homeSection,
                                    { prod ->
                                        onClickProduct.invoke(prod)
                                    },
                                    { prod ->
                                        onAddToCart.invoke(prod)
                                    },
                                    { cate ->
                                        onClickMore.invoke(cate as HomeSectionModel)
                                    }
                                )
                                it.adapter = item
                            }
                        }
                    }, {
                        it.printStackTrace()
                        rllLoading?.visibility = View.GONE
                        imvEmpty?.visibility = View.VISIBLE
                        recyclerView?.visibility = View.GONE
                    })
            )
        }
    }

    override fun getItemCount() = items.size

    override fun onViewDetachedFromWindow(holder: ProductsItemViewHolder) {
        super.onViewDetachedFromWindow(holder)
        compositeDisposable.clear()
    }
}