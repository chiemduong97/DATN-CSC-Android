package com.example.client.screens.rating.community.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.models.rating.RatingModel
import com.example.client.models.rating.RatingType
import com.example.client.screens.rating.community.widget.ImageRadiusView

class RatingItem(
    private val context: Context,
    private val items: List<RatingModel>,
    private val onShowDetail: (rating: RatingModel) -> Unit
) : RecyclerView.Adapter<RatingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RatingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rating, null))

    override fun onBindViewHolder(viewHolder: RatingViewHolder, position: Int) {
        viewHolder.apply {
            val item = items[position]
            imvAvatar?.let {
                Glide.with(context).asBitmap().placeholder(R.drawable.avatar_default).load(item.user.avatar).into(it)
            }
            tvFullName?.text = item.user.fullname
            tvCreatedAt?.text = item.created_at

            if (item.rating == RatingType.RATING_GOOD) {
                imvRating?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_good))
                tvRating?.text = context.getString(R.string.rating_good)
                tvRating?.setTextColor(ContextCompat.getColor(context, R.color.orange))
            } else {
                imvRating?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_bad))
                tvRating?.text = context.getString(R.string.rating_bad)
                tvRating?.setTextColor(ContextCompat.getColor(context, R.color.purple))
            }

            if (item.content.isEmpty()) flContent?.visibility = View.GONE
            if (item.images.isEmpty()) scrollView?.visibility = View.GONE
            tvContent?.text = item.content
            lnlImages?.removeAllViews()
            item.toImageModels().forEach {
                val imageRadius = ImageRadiusView(context).apply { bind(it) }
                lnlImages?.addView(imageRadius)
            }
            tvContent?.setOnClickListener {
                onShowDetail.invoke(item)
            }
        }
    }

    override fun getItemCount() = items.size
}

class RatingViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imvAvatar: ImageView? = itemView.findViewById(R.id.imv_avatar)
    val tvFullName: TextView? = itemView.findViewById(R.id.tv_full_name)
    val tvCreatedAt: TextView? = itemView.findViewById(R.id.tv_created_at)
    val imvRating: ImageView? = itemView.findViewById(R.id.imv_rating)
    val tvRating: TextView? = itemView.findViewById(R.id.tv_rating)
    val tvContent: TextView? = itemView.findViewById(R.id.tv_content)
    val lnlImages: LinearLayout? = itemView.findViewById(R.id.lnl_images)
    val flContent: FrameLayout? = itemView.findViewById(R.id.fl_content)
    val scrollView: FrameLayout? = itemView.findViewById(R.id.scroll_view)
}