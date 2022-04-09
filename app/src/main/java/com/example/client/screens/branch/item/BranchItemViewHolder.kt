package com.example.client.screens.branch.item

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R

class BranchItemViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    var name: TextView? = itemView.findViewById(R.id.tv_name)
    var address: TextView? = itemView.findViewById(R.id.tv_address)
    var checkBox: CheckBox = itemView.findViewById(R.id.check_box)
}