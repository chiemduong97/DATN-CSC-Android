package com.example.client.screens.order.detail.item

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.order.OrderDetailModel
import java.text.NumberFormat
import java.util.*

class OrderDetailItem(var context: Context, var order_details: List<OrderDetailModel>) : RecyclerView.Adapter<OrderDetailItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailItemViewHolder {
        return OrderDetailItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_detail, null))
    }

    override fun onBindViewHolder(viewHolder: OrderDetailItemViewHolder, position: Int) {
        viewHolder.apply {
            val item = order_details[position]
            tvQuantity?.text = context.getString(R.string.text_product_quantity).replace("%s", item.quantity.toString())
            tvProductName?.text = item.name
            tvPrice?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.amount)
        }
    }

    override fun getItemCount(): Int {
        return order_details.size
    }
}