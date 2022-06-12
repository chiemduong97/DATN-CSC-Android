package com.example.client.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.client.R;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.noti.activity.NotificationActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import io.reactivex.Observable;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // handle a notification payload.
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        sendNotification(remoteMessage.getData());
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Preferences.newInstance().setDeviceToken(token);
        // TODO: Implement this method to send token to your app server.
    }

    private void sendNotification(Map data) {

        ProfileModel user = Preferences.newInstance().getProfile();
        PendingIntent pendingIntent = null;
        if(user == null){
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.example.client");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        else {
            Intent intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        String action = (String) data.get("action");
        String description = (String) data.get("description");

        String channelId = getString(R.string.app_name);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            notificationBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_noti))
                    .setContentTitle(action)
                    .setContentText(description)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    public Observable<Integer> resetToken(){
        return Observable.just(1).doOnNext(o -> FirebaseMessaging.getInstance().getToken());
    }

    public void deleteToken(){
        FirebaseMessaging.getInstance().deleteToken();
    }
}

