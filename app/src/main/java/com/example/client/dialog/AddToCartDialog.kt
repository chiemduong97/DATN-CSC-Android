package com.example.client.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.models.cart.CartProductModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.detail.ProductDetailFragment
import com.example.client.utils.ActivityUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.NumberFormat
import java.util.*

class AddToCartDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var product: ProductModel? = null
    private var listener: OptionAddToCartListener? = null
    private var quantity = 1
    private var imvBack: ImageView? = null
    private var lnlProduct: LinearLayout? = null
    private var btnMinus: ImageButton? = null
    private var btnPlus: ImageButton? = null
    private var tvAddToCart: TextView? = null
    private var tvQuantity: TextView? = null
    private var imvProductAvatar: ImageView? = null
    private var tvProductName: TextView? = null
    private var tvProductPrice: TextView? = null
    companion object {
        fun newInstance(product: ProductModel): AddToCartDialog {
            return AddToCartDialog().apply {
                arguments = Bundle().apply {
                    putSerializable(Constants.PRODUCT_MODEL, product)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            getSerializable(Constants.PRODUCT_MODEL)?.let {
                it as ProductModel
                product = it
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View =  inflater.inflate(R.layout.dialog_add_to_cart, null)
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
        lnlProduct = view.findViewById(R.id.lnl_product)
        btnMinus = view.findViewById(R.id.btn_minus)
        btnPlus = view.findViewById(R.id.btn_plus)
        tvAddToCart = view.findViewById(R.id.tv_add_to_cart)
        tvQuantity = view.findViewById(R.id.tv_quantity)
        imvProductAvatar = view.findViewById(R.id.imv_product_avatar)
        tvProductName = view.findViewById(R.id.tv_product_name)
        tvProductPrice = view.findViewById(R.id.tv_product_price)

        context?.let {
            imvProductAvatar?.run {
                Glide.with(it).asBitmap().placeholder(R.drawable.subject_default).load(product?.avatar).into(this)
            }
        }
        tvProductName?.text = product?.name
        tvProductPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(product?.price)

        tvQuantity?.text = quantity.toString()
        btnMinus?.isEnabled = quantity > 1

        imvBack?.setOnClickListener(this)
        lnlProduct?.setOnClickListener(this)
        btnMinus?.setOnClickListener(this)
        btnPlus?.setOnClickListener(this)
        tvAddToCart?.setOnClickListener(this)
    }

    private fun updateQuantity() {
        btnMinus?.isEnabled = quantity > 1
        tvQuantity?.text  = quantity.toString()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imv_back -> {
                dismiss()
            }
            R.id.lnl_product -> {
                product?.let {
                    listener?.onClickProduct(it)
                }
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
            else -> {

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