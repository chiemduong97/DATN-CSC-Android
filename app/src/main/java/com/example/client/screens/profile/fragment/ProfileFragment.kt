package com.example.client.screens.profile.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseFragmentMVP
import com.example.client.base.IBasePresenter
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.event.Event
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.login.activity.LoginEmailActivity
import com.example.client.screens.message.activity.MessageActivity
import com.example.client.screens.order.history.activity.OrderHistoryActivity
import com.example.client.screens.profile.manager_info.ManagerInfoActivity
import com.example.client.screens.profile.navigate.INavigateProfile
import com.example.client.screens.profile.present.IProfilePresent
import com.example.client.screens.profile.present.ProfilePresent
import kotlinx.android.synthetic.main.fragment_profile.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class ProfileFragment : BaseFragmentMVP<IProfilePresent>(), IProfileView, View.OnClickListener {

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_profile, null)
    }

    override fun bindEvent() {
        lnl_logout.setOnClickListener(this)
    }

    override fun bindComponent() {
        presenter.bindData()
    }

    override fun bindEventBus(event: Event) {
        when (event.key) {
            Constants.EventKey.UPDATE_PROFILE_AVATAR, Constants.EventKey.UPDATE_PROFILE_INFO -> presenter.bindData()
        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_update_info -> startActivity(Intent(context, ManagerInfoActivity::class.java))
            R.id.lnl_logout -> {
                PrimaryDialog({
                              presenter.onLogout()
                }, {})
                        .setDescription(getString(R.string.dialog_question_logout))
                        .hideBtnCancel()
                        .show(childFragmentManager)
            }
            R.id.lnl_order_history -> startActivity(Intent(context, OrderHistoryActivity::class.java))
            R.id.lnl_contact -> presenter.navigateToContact()
        }
    }

    override fun logout() {
        activity?.let {
            startActivity(LoginEmailActivity.newInstance(it))
        }
    }

    override fun showUserInfo(profile: ProfileModel) {
        tv_full_name.text = profile.fullname
        tv_email.text = profile.email
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(profile.avatar)
                .into(imv_avatar)
    }

    override fun showErrorMessage(errMessage: Int) {
    }

    override fun toContactScreen(id: Int) {
        val i = Intent(context, MessageActivity::class.java)
        i.putExtra("user", id)
        startActivity(i)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onBackPress() {
    }

    override val presenter: IProfilePresent
        get() = ProfilePresent(this)

}