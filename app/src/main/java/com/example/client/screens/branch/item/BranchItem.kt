package com.example.client.screens.branch.item

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.branch.BranchModel

class BranchItem (private var context: Context?,
                  private var items: List<BranchModel>,
                  private var selected: Int?,
                  private var onClick: (item:BranchModel) -> Unit
): RecyclerView.Adapter<BranchItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BranchItemViewHolder {
        return BranchItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_branch, null))
    }

    override fun onBindViewHolder(viewHolder: BranchItemViewHolder, position: Int) {
        viewHolder.apply {
            val item: BranchModel = items[position]
            name?.text = item.name
            address?.text = item.address
            itemView.setOnClickListener {
                checkBox.run {
                    if (!isChecked) {
                        onClick.invoke(item)
                        isChecked = true
                    }
                }
            }
            selected?.let {
                checkBox.isChecked = it == item.id
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}