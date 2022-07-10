package com.example.client.screens.profile.manager_info.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.base.BaseFragmentMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.profile.manager_info.activity.IManagerProfileView
import com.example.client.screens.profile.manager_info.present.IManagerProfilePresent
import com.example.client.screens.profile.manager_info.present.ManagerProfilePresent
import com.example.client.screens.profile.navigate.NavigatorProfile
import kotlinx.android.synthetic.main.fragment_manager_profile.*

class ManagerProfileFragment : BaseFragmentMVP<IManagerProfilePresent>(), IManagerProfileView, View.OnClickListener {

    companion object {
        @JvmStatic
        fun newInstance() = ManagerProfileFragment()
    }

    override val presenter: IManagerProfilePresent
        get() = ManagerProfilePresent(this)

    private var intentActivityResultLauncher: ActivityResultLauncher<Intent>? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manager_profile, null)
    }

    override fun bindData() {
        presenter.bindData()
    }

    override fun bindEvent() {
        imv_change_avatar.setOnClickListener(this)
        tv_change_info.setOnClickListener(this)
        lnl_change_password.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        intentActivityResultLauncher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                try {
                    data?.let {
                        it.extras?.run {
                            val bitmap = this["data"] as Bitmap
                            view_icon.setImageBitmap(bitmap)
                            presenter.updateAvatar(bitmap)
                        } ?: kotlin.run {
                            val uri = it.data ?: return@let
                            val bitmap = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                                ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, uri))
                            } else {
                                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                            }
                            view_icon.setImageBitmap(bitmap)
                            presenter.updateAvatar(bitmap)
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    showToastMessage(getString(R.string.err_code_1001))
                }
            }
        }
    }

    override fun showProfile(profileModel: ProfileModel) {
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(profileModel.avatar)
                .into(view_icon)
        tv_full_name.text = profileModel.fullname
        tv_email.text = profileModel.email
        tv_birthday.text = if (profileModel.birthday.isEmpty()) getString(R.string.manager_profile_empty_info) else profileModel.birthday
        tv_phone.text = if (profileModel.phone.isEmpty()) getString(R.string.manager_profile_empty_info) else profileModel.phone
    }

    override fun updateInfoSuccess() {}

    override fun updatePassSuccess() {}

    override fun updateAvatarSuccess() {
        PrimaryDialog({ }, { })
                .setDescription(getString(R.string.manager_profile_update_avatar_success))
                .show(childFragmentManager)
    }

    override fun showErrorMessage(errMessage: Int) {
        showDialogErrorMessage(getString(errMessage))
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {
        NavigatorProfile.popFragment()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_change_avatar -> {
                val take = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val pick = Intent(Intent.ACTION_GET_CONTENT)
                pick.type = "image/*"
                val intent = Intent.createChooser(pick, getString(R.string.manager_profile_text_pick))
                intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(take))
                intentActivityResultLauncher?.launch(intent)
            }
            R.id.tv_change_info -> NavigatorProfile.navigateToUpdateInfoScreen()
            R.id.lnl_change_password -> NavigatorProfile.navigateToUpdatePasswordScreen()
            R.id.imv_back -> NavigatorProfile.popFragment()
        }
    }

}