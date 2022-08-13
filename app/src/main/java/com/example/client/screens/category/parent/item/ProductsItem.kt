package com.example.client.screens.category.parent.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.example.client.models.product.toProducts
import com.example.client.screens.product.item.ProductHorizontalItem
import com.example.client.usecase.ProductUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProductsItem(
        private val context: Context,
        private val items: List<CategoryModel>,
        private val onClickMore: (category: CategoryModel) -> Unit,
        private val onClickProduct: (product: ProductModel) -> Unit,
        private val onAddToCart: (product: ProductModel) -> Unit,
) : RecyclerView.Adapter<ProductsItemViewHolder>() {
    private val compositeDisposable = CompositeDisposable()
    private val productUseCase by lazy { ProductUseCase.newInstance() }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsItemViewHolder {
        return ProductsItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_products, null))
    }

    override fun onBindViewHolder(viewHolder: ProductsItemViewHolder, position: Int) {
        viewHolder.apply {
            val category = items[position]
            tvName?.text = category.name
            tvMore?.setOnClickListener {
                onClickMore.invoke(category)
            }
            rllLoading?.visibility = View.VISIBLE
            compositeDisposable.add(
                    productUseCase.getProducts(category.id, 1, 10)
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
                                                context, response.data.toProducts() + ProductModel(),
                                                category,
                                                { prod ->
                                                    onClickProduct.invoke(prod)
                                                },
                                                { prod ->
                                                    onAddToCart.invoke(prod)
                                                },
                                                {
                                                    onClickMore.invoke(category)
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

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewDetachedFromWindow(holder: ProductsItemViewHolder) {
        super.onViewDetachedFromWindow(holder)
        compositeDisposable.clear()
    }

}

class ProductsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName: TextView? = itemView.findViewById(R.id.tv_name)
    val recyclerView: RecyclerView? = itemView.findViewById(R.id.recycler_view)
    val tvMore: TextView? = itemView.findViewById(R.id.tv_more)
    val imvEmpty: ImageView? = itemView.findViewById(R.id.imv_empty)
    val rllLoading: RelativeLayout? = itemView.findViewById(R.id.rll_loading)
}