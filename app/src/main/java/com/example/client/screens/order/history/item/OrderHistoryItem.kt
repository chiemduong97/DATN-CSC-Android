package com.example.client.screens.order.history.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.models.order.OrderModel
import java.text.NumberFormat
import java.util.*

class OrderHistoryItem(
        private val context: Context,
        private val orders: List<OrderModel>,
        private val onClickItem: (orderCode: String) -> Unit) : RecyclerView.Adapter<OrderHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        return OrderHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_history, null))
    }

    override fun onBindViewHolder(viewHolder: OrderHistoryViewHolder, position: Int) {
        viewHolder.apply {
            val item = orders[position]
            tvOrderCode?.text = context.getString(R.string.text_order_code, item.order_code)
            tvCreatedAt?.text = item.created_at
            tvAddress?.text = context.getString(R.string.delivery_address, item.address)
            tvTotal?.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.getTotalPrice())
            when {
                item.isWaiting() -> {
                    tvStatus?.text = context.getString(R.string.order_status_0)
                    tvStatus?.setTextColor(ContextCompat.getColor(context, R.color.orange))
                }
                item.isConfirm() -> {
                    tvStatus?.text = context.getString(R.string.order_status_1)
                    tvStatus?.setTextColor(ContextCompat.getColor(context, R.color.blue))
                }
                item.isDelivery() -> {
                    tvStatus?.text = context.getString(R.string.order_status_2)
                    tvStatus?.setTextColor(ContextCompat.getColor(context, R.color.red_light))
                }
                item.isComplete() -> {
                    tvStatus?.text = context.getString(R.string.order_status_3)
                    tvStatus?.setTextColor(ContextCompat.getColor(context,R.color.green))
                }
                item.isDestroy() -> {
                    tvStatus?.text = context.getString(R.string.order_status_4)
                    tvStatus?.setTextColor(ContextCompat.getColor(context, R.color.gray_dark))
                }
            }

            when (item.payment_method) {
                Constants.PaymentMethod.COD -> {
                    imvPaymentMethod?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_payment_cod))
                    tvPaymentMethod?.text = context.getString(R.string.payment_method_cod)
                }
                Constants.PaymentMethod.MOMO -> {
                    imvPaymentMethod?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_payment_momo))
                    tvPaymentMethod?.text = context.getString(R.string.payment_method_momo)
                }
                Constants.PaymentMethod.WALLET -> {
                    imvPaymentMethod?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_payment_wallet))
                    tvPaymentMethod?.text = context.getString(R.string.payment_method_wallet)
                }
            }

            itemView.setOnClickListener {
                onClickItem.invoke(item.order_code)
            }
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

}

class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvOrderCode: TextView? = itemView.findViewById(R.id.tv_order_code)
    val tvAddress: TextView? = itemView.findViewById(R.id.tv_address)
    val tvTotal: TextView? = itemView.findViewById(R.id.tv_total)
    val tvStatus: TextView? = itemView.findViewById(R.id.tv_status)
    val tvCreatedAt: TextView? = itemView.findViewById(R.id.tv_created_at)
    val imvPaymentMethod: ImageView? = itemView.findViewById(R.id.imv_payment_method)
    val tvPaymentMethod: TextView? = itemView.findViewById(R.id.tv_payment_method)
}