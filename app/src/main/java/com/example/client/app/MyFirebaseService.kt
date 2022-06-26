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
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.Observable
import java.util.*

class MyFirebaseService : FirebaseMessagingService() {
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
        Preferences.newInstance().deviceToken = token
    }

    private fun sendNotification(data: Map<*, *>) {
        val user = Preferences.newInstance().profile
        var pendingIntent: PendingIntent? = null
        val intent: Intent?
        intent = if (user == null) {
            packageManager.getLaunchIntentForPackage("com.example.client")
        } else {
            Intent(this, NotificationActivity::class.java)
        }
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val action = data["action"] as String
        val description = data["description"] as String
        val channelId = getString(R.string.app_name)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var notificationBuilder: NotificationCompat.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_noti))
                    .setContentTitle(action)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationBuilder?.let {
            notificationManager.notify(Random().nextInt(), it.build())
        }
    }

    fun resetToken(): Observable<Int> {
        return Observable.just(1).doOnNext { FirebaseMessaging.getInstance().token }
    }

    fun deleteToken() {
        FirebaseMessaging.getInstance().deleteToken()
    }

    companion object {
        private const val TAG = "MyFirebaseService"
    }
}