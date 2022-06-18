package com.example.client.screens.profile.manager_info.present

import android.graphics.Bitmap
import android.net.Uri
import com.example.client.app.Constants
import com.example.client.app.Firebase
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.models.profile.ProfileRequest
import com.example.client.screens.profile.manager_info.activity.IManagerProfileView
import com.example.client.usecase.ProfileUseCase
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import org.greenrobot.eventbus.EventBus
import java.io.ByteArrayOutputStream

class ManagerProfilePresent(mView: IManagerProfileView) : BasePresenterMVP<IManagerProfileView>(mView), IManagerProfilePresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }

    override fun bindData() {
        mView?.showProfile(profileUseCase.getProfile())
    }

    override fun updateProfile(full_name: String, birthday: String, phone: String) {
        mView?.showLoading()
        subscribe(profileUseCase.updateInfo(ProfileRequest(email = profileUseCase.getProfile().email, fullname = full_name, birthday = birthday, phone = phone)), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                updateInfoSuccess()
                profileUseCase.setProfile(profileUseCase.getProfile().apply {
                    this.fullname = full_name
                    this.birthday = birthday
                    this.phone = phone
                })
                EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_PROFILE_INFO))
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun updatePassword(oldPass: String, newPass: String) {
        mView?.showLoading()
        subscribe(profileUseCase.updatePass(ProfileRequest(email = profileUseCase.getProfile().email, old_password = oldPass, new_password = newPass)), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                updatePassSuccess()
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun updateAvatar(avatar: Bitmap) {
        mView?.showLoading()
        Firebase.createInstance()
        val ref = Firebase.getInstance().ref()
        val byteArray = ByteArrayOutputStream()
        avatar.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
        val data = byteArray.toByteArray()
        val uploadTask = ref.putBytes(data)
        uploadTask.addOnFailureListener {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        }.addOnSuccessListener {
            uploadTask.continueWithTask { task: Task<UploadTask.TaskSnapshot?> ->
                if (!task.isSuccessful) {
                    mView?.run {
                        hideLoading()
                        showErrorMessage(getErrorMessage(1001))
                    }
                    throw task.exception!!
                }
                ref.downloadUrl
            }.addOnCompleteListener { task: Task<Uri> ->
                if (task.isSuccessful) {
                    val uri = task.result
                    subscribe(profileUseCase.updateAvatar(ProfileRequest(email = profileUseCase.getProfile().email, avatar = uri.toString())), {
                        mView?.run {
                            hideLoading()
                            if (it.is_error) {
                                showErrorMessage(getErrorMessage(it.code))
                                return@subscribe
                            }
                            updatePassSuccess()
                            profileUseCase.setProfile(profileUseCase.getProfile().apply {
                                this.avatar = uri.toString()
                            })
                            EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_PROFILE_AVATAR))
                        }
                    }, {
                        it.printStackTrace()
                        mView?.run {
                            hideLoading()
                            showErrorMessage(getErrorMessage(1001))
                        }
                    })
                } else {
                    mView?.run {
                        hideLoading()
                        showErrorMessage(getErrorMessage(1001))
                    }
                }
            }
        }
    }
}