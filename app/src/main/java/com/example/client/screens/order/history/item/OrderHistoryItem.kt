package com.example.client.screens.order.history.item

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.order.OrderModel
import java.text.NumberFormat
import java.util.*

class OrderHistoryItem (var context: Context, var orders: List<OrderModel>, var onClick: (ordercode: String) -> Unit): RecyclerView.Adapter<OrderHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        return OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history, null))
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(viewHolder: OrderHistoryViewHolder, position: Int) {
        viewHolder.apply {
            val item = orders[position]
            tvOrderCode?.text = context.getString(R.string.text_order_code).replace("%s", item.ordercode)
            tvBranchAddress?.text = item.branch_address
            tvOrderTotalPrice?.text = context.getString(R.string.text_order_total_price).replace("%s", NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getTotalPrice()))
            when {
                item.isWaiting() -> {
                    tvOrderStatus?.text = context.getString(R.string.order_status_0)
                    tvOrderStatus?.setTextColor(context.getColor(R.color.orange))
                }
                item.isConfirm() -> {
                    tvOrderStatus?.text = context.getString(R.string.order_status_1)
                    tvOrderStatus?.setTextColor(context.getColor(R.color.blue))
                }
                item.isDelivery() -> {
                    tvOrderStatus?.text = context.getString(R.string.order_status_2)
                    tvOrderStatus?.setTextColor(context.getColor(R.color.red_light))
                }
                item.isComplete() -> {
                    tvOrderStatus?.text = context.getString(R.string.order_status_3)
                    tvOrderStatus?.setTextColor(context.getColor(R.color.green))
                }
                item.isDestroy() -> {
                    tvOrderStatus?.text = context.getString(R.string.order_status_4)
                    tvOrderStatus?.setTextColor(context.getColor(R.color.gray_dark))
                }
            }
            itemView.setOnClickListener {
                onClick.invoke(item.ordercode)
            }
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

}