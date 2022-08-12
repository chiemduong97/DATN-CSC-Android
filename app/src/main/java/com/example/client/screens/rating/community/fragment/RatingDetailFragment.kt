package com.example.client.screens.rating.community.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseFragmentMVP
import com.example.client.base.BasePresenterMVP
import com.example.client.base.IBasePresenter
import com.example.client.models.rating.RatingModel
import com.example.client.models.rating.RatingType
import com.example.client.screens.rating.community.widget.ImageRadiusView
import kotlinx.android.synthetic.main.fragment_rating_detail.*

class RatingDetailFragment : BaseFragmentMVP<IBasePresenter>() {

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) = RatingDetailFragment().apply {
            arguments = args
        }
    }

    private val rating by lazy { arguments?.getSerializable(Constants.BundleKey.RATING_MODEL) as? RatingModel }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_rating_detail, null)
    }

    override fun bindComponent() {
        rating?.run {
            Glide.with(requireContext()).asBitmap().placeholder(R.drawable.avatar_default).load(user.avatar).into(imv_avatar)
            tv_full_name.text = user.fullname
            tv_created_at.text = created_at
            if (rating == RatingType.RATING_GOOD) {
                imv_rating.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rating_good))
                tv_rating.text = requireContext().getString(R.string.rating_good)
                tv_rating.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
            } else {
                imv_rating.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_rating_bad))
                tv_rating.text = requireContext().getString(R.string.rating_bad)
                tv_rating.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
            }

            if (content.isEmpty()) fl_content.visibility = View.GONE
            if (images.isEmpty()) scroll_view.visibility = View.GONE
            tv_content.text = content
            toImageModels().forEach {
                val imageRadius = ImageRadiusView(requireContext()).apply { bind(it) }
                lnl_images.addView(imageRadius)
            }
        }
    }


    override fun bindEvent() {
        imv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override val presenter: IBasePresenter
        get() = BasePresenterMVP(this)
}