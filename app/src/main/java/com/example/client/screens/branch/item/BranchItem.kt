package com.example.client.screens.branch.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.branch.BranchModel

class BranchItem(
        private val context: Context,
        private val items: List<BranchModel>,
        private val selected: Int,
        private val onClick: (item: BranchModel) -> Unit,
) : RecyclerView.Adapter<BranchItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchItemViewHolder {
        return BranchItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_branch, null))
    }

    override fun onBindViewHolder(viewHolder: BranchItemViewHolder, position: Int) {
        viewHolder.apply {
            val item: BranchModel = items[position]
            tvName?.text = item.name
            tvAddress?.text = item.address
            tvDistance?.text = context.getString(R.string.branch_distance, item.distance)
            itemView.setOnClickListener {
                checkBox?.run {
                    if (!isChecked) {
                        onClick.invoke(item)
                        isChecked = true
                    }
                }
            }
            checkBox?.isChecked = selected == item.id
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class BranchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvName: TextView? = itemView.findViewById(R.id.tv_name)
    var tvAddress: TextView? = itemView.findViewById(R.id.tv_address)
    var checkBox: CheckBox? = itemView.findViewById(R.id.check_box)
    var tvDistance: TextView? = itemView.findViewById(R.id.tv_distance)
}