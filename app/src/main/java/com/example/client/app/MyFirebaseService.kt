package com.example.client.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Typeface
import android.media.RingtoneManager
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.client.R
import com.example.client.models.notify.NotifyModel
import com.example.client.models.notify.getAction
import com.example.client.screens.notify.NotifyActivity
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.usecase.ProfileUseCase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MyFirebaseService : FirebaseMessagingService() {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    private val firebaseMessaging by lazy { FirebaseMessaging.getInstance() }
    private val compositeDisposable = CompositeDisposable()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from + ": " + Gson().toJson(remoteMessage.data))
        sendNotification(remoteMessage.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String) {
        preferences.deviceToken = token
        compositeDisposable.add(profileUseCase.updateDeviceToken(mEmail ?: return, device_token = token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.is_error) {
                        firebaseMessaging.token
                    }
                }, {
                    it.printStackTrace()
                    firebaseMessaging.token
                }))
    }

    private fun sendNotification(data: Map<*, *>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val user = Preferences.newInstance().profile
            val notify = NotifyModel(
                action = data["action"] as String,
                description = data["description"] as String,
                order_code = data["order"] as String?
            )

            val intent = user?.let {
                when(notify.action) {
                    Constants.NotifyAction.NOTIFY -> Intent(this, NotifyActivity::class.java)
                    Constants.NotifyAction.RECHARGE_SUCCESS -> packageManager.getLaunchIntentForPackage("com.example.client")
                    else -> Intent(this, OrderDetailActivity::class.java).apply {
                        putExtra(Constants.BundleKey.ORDER_CODE, notify.order_code)
                    }
                }
            } ?: kotlin.run {
                packageManager.getLaunchIntentForPackage("com.example.client")
            }

            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId = getString(R.string.app_name)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val title = SpannableString(getAction(notify)).apply {
                setSpan(StyleSpan(Typeface.BOLD), 0, getAction(notify).length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo_square)
                    .setContentTitle(title)
                    .setContentText(notify.description)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setFullScreenIntent(pendingIntent, true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(notify.description))
            val channel = NotificationChannel(
                    channelId,
                    "CSC channel",
                    NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }

    fun resetToken(email: String) {
        mEmail = email
        firebaseMessaging.token
    }

    fun deleteToken() {
        firebaseMessaging.deleteToken()
    }

    companion object {
        private const val TAG = "MyFirebaseService"
        private var mEmail: String? = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}