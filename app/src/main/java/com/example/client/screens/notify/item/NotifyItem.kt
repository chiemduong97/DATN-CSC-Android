package com.example.client.screens.notify.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.models.notify.NotifyModel
import com.example.client.models.notify.getAction

class NotifyItem(
        private val context: Context,
        private val items: List<NotifyModel>,
        private val onClickItem: (orderCode: String) -> Unit
) : RecyclerView.Adapter<NotifyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotifyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notify, null))

    override fun onBindViewHolder(viewHolder: NotifyViewHolder, position: Int) {
        viewHolder.apply {
            val item = items[position]
            tvCreatedAt?.text = item.created_at
            tvAction?.text = getAction(item)
            tvDescription?.text = item.description
            if (item.action != Constants.NotifyAction.NOTIFY && item.action != Constants.NotifyAction.RECHARGE_SUCCESS) {
                rllNotify?.setOnClickListener {
                    item.order_code?.let { orderCode -> onClickItem.invoke(orderCode) }
                }
            }
        }
    }

    override fun getItemCount() = items.size
}

class NotifyViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvCreatedAt: TextView? = itemView.findViewById(R.id.tv_created_at)
    val tvAction: TextView? = itemView.findViewById(R.id.tv_action)
    val tvDescription: TextView? = itemView.findViewById(R.id.tv_description)
    val rllNotify: RelativeLayout? = itemView.findViewById(R.id.rll_notify)
}