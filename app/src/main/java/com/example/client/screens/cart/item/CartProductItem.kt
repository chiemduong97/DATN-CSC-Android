package com.example.client.screens.cart.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.cart.CartProductModel
import java.text.NumberFormat
import java.util.*

class CartProductItem(var context: Context, var items: List<CartProductModel>, var onPlus:(item:CartProductModel) -> Unit, var onMinus:(item:CartProductModel) -> Unit): RecyclerView.Adapter<CartProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_product, null))
    }

    override fun onBindViewHolder(viewHolder: CartProductViewHolder, position: Int) {
        viewHolder.apply {
            val item = items[position]
            if (position == items.lastIndex) viewLine?.visibility = View.GONE
            tvProductName?.text = item.product.name
            tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getPrice())
            tvQuantity?.text = item.quantity.toString()
            btnPlus?.setOnClickListener {
                item.quantity++
                tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getPrice())
                tvQuantity?.text = item.quantity.toString()
                onPlus.invoke(item)
            }
            btnMinus?.setOnClickListener {
                item.quantity--
                tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getPrice())
                tvQuantity?.text = item.quantity.toString()
                onMinus.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class CartProductViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var tvProductName: TextView? = itemView.findViewById(R.id.tv_name)
    var tvPrice: TextView? = itemView.findViewById(R.id.tv_price)
    var btnMinus: ImageButton? = itemView.findViewById(R.id.btn_minus)
    var btnPlus: ImageButton? = itemView.findViewById(R.id.btn_plus)
    var tvQuantity: TextView? =  itemView.findViewById(R.id.tv_quantity)
    var viewLine: View? = itemView.findViewById(R.id.view_line)
}