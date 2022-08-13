package com.example.client.screens.order.review.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.client.R
import com.example.client.models.rating.RatingType
import kotlinx.android.synthetic.main.widget_rating_order.view.*

class RatingOrderView: FrameLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    init {
        val view: View = LayoutInflater.from(context).inflate(R.layout.widget_rating_order, this, false)
        addView(view)
    }

    fun bind(isRated: Boolean, rating: RatingType?, onRating: () -> Unit, showRatingDetail: () -> Unit) {
        if (isRated) {
            tv_rating.visibility = View.GONE
            lnl_rating.visibility = View.VISIBLE
            tv_view_rating.setOnClickListener {
                showRatingDetail.invoke()
            }
            if (rating == RatingType.RATING_GOOD) {
                imv_rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_good))
                tv_rating_type.text = context.getString(R.string.rating_good)
            } else {
                imv_rating.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_rating_bad))
                tv_rating_type.text = context.getString(R.string.rating_bad)
            }
        } else {
            tv_rating.visibility = View.VISIBLE
            lnl_rating.visibility = View.GONE
            tv_rating.setOnClickListener {
                onRating.invoke()
            }
        }
    }
}