package com.example.client.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.client.R
import com.example.client.screens.noti.activity.NotificationActivity
import com.example.client.usecase.ProfileUseCase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MyFirebaseService : FirebaseMessagingService() {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    private val firebaseMessaging by lazy { FirebaseMessaging.getInstance() }
    private val compositeDisposable = CompositeDisposable()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)
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
            val intent = user?.let {
                packageManager.getLaunchIntentForPackage("com.example.client")
            } ?: kotlin.run {
                Intent(this, NotificationActivity::class.java)
            }.apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val channelId = getString(R.string.app_name)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_noti))
                    .setContentTitle(data["action"] as String)
                    .setContentText(data["description"] as String)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
            val channel = NotificationChannel(
                    channelId,
                    "CSC channel",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(Random().nextInt(), notificationBuilder.build())
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