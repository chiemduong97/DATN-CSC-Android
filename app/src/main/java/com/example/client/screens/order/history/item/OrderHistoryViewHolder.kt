package com.example.client.screens.order.history.item

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R

class OrderHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imvBranchAvatar: ImageView? = itemView.findViewById(R.id.imv_branch_avatar)
    val tvOrderCode: TextView? = itemView.findViewById(R.id.tv_order_code)
    val tvBranchAddress: TextView? = itemView.findViewById(R.id.tv_branch_address)
    val tvOrderTotalPrice: TextView? = itemView.findViewById(R.id.tv_order_total_price)
    val tvOrderStatus: TextView? = itemView.findViewById(R.id.tv_order_status)
}