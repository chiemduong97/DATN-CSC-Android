package com.example.client.screens.wallet.item

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.models.transaction.TransactionModel
import java.text.NumberFormat
import java.util.*

class TransactionItem(
        private val context: Context,
        private val items: List<TransactionModel>,
        private val method: Constants.Transaction,
        private val onShowOrderDetail: (orderCode: String) -> Unit,
) : RecyclerView.Adapter<TransactionItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        return TransactionItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_transaction, null))
    }

    override fun onBindViewHolder(viewHolder: TransactionItemViewHolder, position: Int) {
        viewHolder.apply {
            val item = items[position]
            tvCreatedAt?.text = item.createAt
            tvOrderCode?.text = context.getString(R.string.wallet_order_code, item.ordercode)
            val formatVND = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
            if (method == Constants.Transaction.RECHARGE) {
                tvLabel?.text = context.getString(R.string.wallet_recharge_label)
                tvAmount?.text = context.getString(R.string.wallet_amount_plus, formatVND.format(item.amount))
                tvAmount?.setTextColor(Color.GREEN)
            } else {
                if (item.status == 1) {
                    tvLabel?.text = context.getString(R.string.wallet_payment_order)
                    tvAmount?.text = context.getString(R.string.wallet_amount_minus, formatVND.format(item.amount))
                    tvAmount?.setTextColor(Color.RED)
                } else {
                    tvLabel?.text = context.getString(R.string.wallet_payment_refund)
                    tvAmount?.text = context.getString(R.string.wallet_amount_plus, formatVND.format(item.amount))
                    tvAmount?.setTextColor(Color.GREEN)
                }
                itemView.setOnClickListener {
//                    onShowOrderDetail.invoke(item.ordercode)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class TransactionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvLabel: TextView? = itemView.findViewById(R.id.tv_label)
    var tvOrderCode: TextView? = itemView.findViewById(R.id.tv_order_code)
    var tvCreatedAt: TextView? = itemView.findViewById(R.id.tv_created_at)
    var tvAmount: TextView? = itemView.findViewById(R.id.tv_amount)
    var tvStatus: TextView? = itemView.findViewById(R.id.tv_status)
    var viewLine: View? = itemView.findViewById(R.id.view_line)
}