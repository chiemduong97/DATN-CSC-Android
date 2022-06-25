package com.example.client.screens.order.detail.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.order.OrderDetailModel
import java.text.NumberFormat
import java.util.*

class OrderDetailItem(var context: Context, var items: List<OrderDetailModel>) : RecyclerView.Adapter<OrderDetailItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailItemViewHolder {
        return OrderDetailItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_detail, null))
    }

    override fun onBindViewHolder(viewHolder: OrderDetailItemViewHolder, position: Int) {
        viewHolder.apply {
            val item = items[position]
            tvQuantity?.text = context.getString(R.string.text_cart_product_quantity, item.quantity)
            tvProductName?.text = item.name
            tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.price)
            if (position == items.lastIndex) viewLine?.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class OrderDetailItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvQuantity: TextView? = itemView.findViewById(R.id.tv_quantity)
    val tvProductName: TextView? = itemView.findViewById(R.id.tv_product_name)
    val tvPrice: TextView? = itemView.findViewById(R.id.tv_price)
    val viewLine: View? = itemView.findViewById(R.id.view_line)
}