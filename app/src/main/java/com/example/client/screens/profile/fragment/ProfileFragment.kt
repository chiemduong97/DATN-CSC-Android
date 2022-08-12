package com.example.client.screens.profile.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.base.BaseFragmentMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.login.activity.LoginEmailActivity
import com.example.client.screens.message.activity.MessageActivity
import com.example.client.screens.order.history.activity.OrderHistoryActivity
import com.example.client.screens.profile.manager_info.activity.ManagerProfileActivity
import com.example.client.screens.profile.present.IProfilePresent
import com.example.client.screens.profile.present.ProfilePresent
import com.example.client.screens.rating.community.CommunityActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragmentMVP<IProfilePresent>(), IProfileView, View.OnClickListener {

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_profile, null)
    }

    override fun bindEvent() {
        tv_update_info.setOnClickListener(this)
        lnl_logout.setOnClickListener(this)
        lnl_order_history.setOnClickListener(this)
        lnl_community.setOnClickListener(this)
        lnl_contact.setOnClickListener(this)
        swipe_refresh.setOnRefreshListener { hideLoading() }
    }

    override fun bindComponent() {
        presenter.bindData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_update_info -> startActivity(ManagerProfileActivity.newInstance(requireActivity()))
            R.id.lnl_logout -> {
                PrimaryDialog({
                    presenter.onLogout()
                }, {})
                        .setDescription(getString(R.string.dialog_question_logout))
                        .show(childFragmentManager)
            }
            R.id.lnl_order_history -> startActivity(OrderHistoryActivity.newInstance(requireActivity()))
            R.id.lnl_contact -> presenter.navigateToContact()
            R.id.lnl_community -> startActivity(CommunityActivity.newInstance(requireActivity()))
        }
    }

    override fun logout() {
        requireActivity().finish()
        startActivity(LoginEmailActivity.newInstance(requireActivity()))
    }

    override fun showUserInfo(profile: ProfileModel) {
        tv_full_name.text = profile.fullname
        tv_email.text = profile.email
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(profile.avatar)
                .into(view_icon)
    }

    override fun showErrorMessage(errMessage: Int) {
    }

    override fun toContactScreen(id: Int) {
        val i = Intent(context, MessageActivity::class.java)
        i.putExtra("user", id)
        startActivity(i)
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh.run {
            if (isRefreshing) isRefreshing = false
        }
    }

    override fun onBackPress() {
    }

    override val presenter: IProfilePresent
        get() = ProfilePresent(this)

}