package com.example.client.screens.promotion.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.models.promotion.PromotionModel
import java.text.NumberFormat
import java.util.*

class PromotionItem(
        private val context: Context,
        private val items: List<PromotionModel>,
        private val promotionId: Int,
        private val onUse: (promotion: PromotionModel) -> Unit,
        private val onRemove: () -> Unit,
) : RecyclerView.Adapter<PromotionItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            PromotionItemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_promotion, null))

    override fun onBindViewHolder(viewHolder: PromotionItemViewHolder, position: Int) {
        viewHolder.apply {
            val item = items[position]
            if (item.value < 1) {
                tvValue?.text = HtmlCompat.fromHtml(context.getString(
                        R.string.promotion_value,
                        "${(item.value * 100).toInt()}%"),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                tvValue?.text = HtmlCompat.fromHtml(context.getString(
                        R.string.promotion_value,
                        NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(item.value)),
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            }


            tvCode?.text = item.code
            tvTime?.text = context.getString(R.string.promotion_end, item.end)
            tvUse?.setOnClickListener {
                onUse.invoke(item)
            }
            if (promotionId == item.id) {
                viewChecked?.visibility = View.VISIBLE
                tvUse?.visibility = View.GONE
                viewRemove?.visibility = View.VISIBLE
                ctlPromotion?.background = ContextCompat.getDrawable(context, R.drawable.border_item_green_10)
            } else {
                viewChecked?.visibility = View.GONE
                tvUse?.visibility = View.VISIBLE
                viewRemove?.visibility = View.GONE
                ctlPromotion?.background = ContextCompat.getDrawable(context, R.drawable.border_item_gray_10)
            }
            viewRemove?.setOnClickListener {
                onRemove.invoke()
                viewChecked?.visibility = View.GONE
                tvUse?.visibility = View.VISIBLE
                viewRemove?.visibility = View.GONE
                ctlPromotion?.background = ContextCompat.getDrawable(context, R.drawable.border_item_gray_10)
            }
        }
    }

    override fun getItemCount() = items.size
}

class PromotionItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var ctlPromotion: ConstraintLayout? = itemView.findViewById(R.id.ctl_promotion)
    var tvValue: TextView? = itemView.findViewById(R.id.tv_value)
    var tvCode: TextView? = itemView.findViewById(R.id.tv_code)
    var tvTime: TextView? = itemView.findViewById(R.id.tv_time)
    var tvUse: TextView? = itemView.findViewById(R.id.tv_use)
    var viewChecked: View? = itemView.findViewById(R.id.view_checked)
    var viewRemove: View? = itemView.findViewById(R.id.view_remove)
}