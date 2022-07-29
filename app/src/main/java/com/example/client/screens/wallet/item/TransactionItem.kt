package com.example.client.screens.wallet.item

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        private val onShowOrderDetail: (orderCode: String) -> Unit,
) : RecyclerView.Adapter<TransactionItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        TransactionItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_transaction, null))

    override fun onBindViewHolder(viewHolder: TransactionItemViewHolder, position: Int) {
        viewHolder.apply {
            val item = items[position]
            item.transid_momo ?: kotlin.run {
                imvPaymentMethod?.visibility = View.GONE
                tvPaymentModel?.visibility = View.GONE
            }

            item.order_code?.let {
                tvOrderCode?.text = context.getString(R.string.wallet_order_code, it)
            } ?: kotlin.run {
                tvOrderCode?.visibility = View.GONE
            }
            tvCreatedAt?.text = item.created_at
            val formatVND = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

            when (item.type) {
                Constants.TransactionType.RECHARGE -> {
                    tvLabel?.text = context.getString(R.string.wallet_recharge)
                    tvAmount?.text = context.getString(R.string.wallet_amount_plus, formatVND.format(item.amount))
                    tvAmount?.setTextColor(Color.GREEN)
                }
                Constants.TransactionType.PAID -> {
                    tvLabel?.text = context.getString(R.string.wallet_payment_order)
                    tvAmount?.text = context.getString(R.string.wallet_amount_minus, formatVND.format(item.amount))
                    tvAmount?.setTextColor(Color.RED)
                }
                Constants.TransactionType.REFUND -> {
                    tvLabel?.text = context.getString(R.string.wallet_payment_refund)
                    tvAmount?.text = context.getString(R.string.wallet_amount_plus, formatVND.format(item.amount))
                    tvAmount?.setTextColor(Color.GREEN)
                }
            }

            itemView.setOnClickListener {
                onShowOrderDetail.invoke(item.order_code ?: return@setOnClickListener)
            }

        }
    }

    override fun getItemCount() = items.size
}

class TransactionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvLabel: TextView? = itemView.findViewById(R.id.tv_label)
    var imvPaymentMethod: ImageView? = itemView.findViewById(R.id.imv_payment_method)
    var tvPaymentModel: TextView? = itemView.findViewById(R.id.tv_payment_method)
    var tvOrderCode: TextView? = itemView.findViewById(R.id.tv_order_code)
    var tvCreatedAt: TextView? = itemView.findViewById(R.id.tv_created_at)
    var tvAmount: TextView? = itemView.findViewById(R.id.tv_amount)
}