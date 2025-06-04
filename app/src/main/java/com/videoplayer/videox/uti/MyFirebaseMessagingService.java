package com.videoplayer.videox.uti;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.videoplayer.videox.R;
import com.videoplayer.videox.activity.HomPageActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    private void sendRegistrationToServer(String token) {
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            scheduleJob();
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            remoteMessage.getNotification().getBody();
            if (remoteMessage.getNotification().getBody() != null) {
                sendNotification(remoteMessage);
            }
        }
    }

    @Override // com.google.firebase.messaging.FirebaseMessagingService
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void scheduleJob() {
        WorkManager.getInstance(this).beginWith(new OneTimeWorkRequest.Builder(MyWor.class).build()).enqueue();
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, (Class<?>) HomPageActivity.class);
        intent.addFlags(67108864);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 67108864);
        String string = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder contentIntent = new NotificationCompat.Builder(this, string).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(remoteMessage.getData().get("TITLE")).setContentText(remoteMessage.getData().get("BODY")).setAutoCancel(true).setSound(RingtoneManager.getDefaultUri(2)).setContentIntent(activity);
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            notificationManager.createNotificationChannel(new NotificationChannel(string, "Channel human readable title", 3));
        }
        notificationManager.notify(0, contentIntent.build());
    }
}
