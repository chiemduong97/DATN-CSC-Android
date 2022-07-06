package com.example.client.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.models.cart.CartProductModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.util.*

class AddToCartDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var product: ProductModel? = null
    private var category: CategoryModel? = null
    private var listener: OptionAddToCartListener? = null
    private var quantity = 1
    private var imvBack: ImageView? = null
    private var btnMinus: ImageButton? = null
    private var btnPlus: ImageButton? = null
    private var tvAddToCart: TextView? = null
    private var tvQuantity: TextView? = null
    private var imvProductAvatar: ImageView? = null
    private var tvName: TextView? = null
    private var tvPrice: TextView? = null
    private var tvDescription: TextView? = null
    private var rltProduct: RelativeLayout? = null

    companion object {
        fun newInstance(category: CategoryModel,product: ProductModel): AddToCartDialog {
            return AddToCartDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(Constants.CATEGORY_MODEL, category)
                    putSerializable(Constants.PRODUCT_MODEL, product)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            getSerializable(Constants.PRODUCT_MODEL)?.let {
                product = it as ProductModel
            }
            getSerializable(Constants.CATEGORY_MODEL)?.let {
                category = it as CategoryModel
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_add_to_cart, null)
        initView(rootView)
        return rootView
    }


    private fun initView(view: View) {
        dialog?.run {
            setOnShowListener {
                findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
            }
            window?.apply {
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        imvBack = view.findViewById(R.id.imv_back)
        btnMinus = view.findViewById(R.id.btn_minus)
        btnPlus = view.findViewById(R.id.btn_plus)
        tvAddToCart = view.findViewById(R.id.tv_add_to_cart)
        tvQuantity = view.findViewById(R.id.tv_quantity)
        imvProductAvatar = view.findViewById(R.id.imv_product_avatar)
        tvName = view.findViewById(R.id.tv_name)
        tvPrice = view.findViewById(R.id.tv_price)
        tvDescription = view.findViewById(R.id.tv_description)
        rltProduct = view.findViewById(R.id.rlt_product)

        product?.run {
            imvProductAvatar?.run {
                Glide.with(requireContext()).asBitmap().placeholder(R.drawable.subject_default).load(avatar).into(this)
            }

            tvName?.text = name
            tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(price)
            tvDescription?.text = description
        }

        tvQuantity?.text = quantity.toString()
        btnMinus?.isEnabled = quantity > 1

        imvBack?.setOnClickListener(this)
        btnMinus?.setOnClickListener(this)
        btnPlus?.setOnClickListener(this)
        tvAddToCart?.setOnClickListener(this)
        rltProduct?.setOnClickListener(this)
    }

    private fun updateQuantity() {
        btnMinus?.isEnabled = quantity > 1
        tvQuantity?.text = quantity.toString()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_back -> {
                dismiss()
            }
            R.id.btn_minus -> {
                product?.let {
                    quantity--
                    updateQuantity()
                }
            }
            R.id.btn_plus -> {
                product?.let {
                    quantity++
                    updateQuantity()
                }
            }
            R.id.tv_add_to_cart -> {
                product?.let {
                    listener?.addToCart(CartProductModel(it, quantity).apply {
                        product.addToCart += quantity
                    })
                }
                dismiss()
            }
            R.id.rlt_product -> {
                product?.let {
                    category?.run { listener?.showProductDetail(this, it) }
                }
                dismiss()
            }
        }
    }

    fun setListener(callback: OptionAddToCartListener): AddToCartDialog {
        this.listener = callback
        return this
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager ?: return
        show(fragmentManager, tag)
    }
}