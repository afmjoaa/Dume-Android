package io.dume.dume.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.common.chatActivity.ChatActivity;
import io.dume.dume.student.homePage.HomePageActivity;

public class MyNotification extends FirebaseMessagingService {

    private static String token;
    private final FirebaseFirestore firestore;
    private String type = "";

    public MyNotification() {
        super();
        firestore = FirebaseFirestore.getInstance();
    }

    private void sendNotification(String messageTitle, String messageBody) {
        int NOTIFICATION_ID = 234;
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.CYAN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{200, 500, 200, 500});
            mChannel.setShowBadge(true);
            mChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI,new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_01")
                .setSmallIcon(R.drawable.ic_notification_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setLights(Color.CYAN, 1000, 2000)
                .setVibrate(new long[]{200, 500, 200, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setColor(getResources().getColor(R.color.noti_color));

        Intent resultIntent = new Intent(this, HomePageActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomePageActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null) {
            Map<String, String> data = remoteMessage.getData();
            if (data.get("type") != null) {
                type = data.get("type");
            }

            if (type.equals("message")) {
                if (!ChatActivity.isActivityLive) {
                    sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                }
            } else{
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                // do your work here

            }

        }
        Log.w("foo", "messageReceived: " + remoteMessage.getData().toString());
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("dume", MODE_PRIVATE).edit().putString("fcm_token", s).apply();
    }

    public static String getToken(Context context) {
        token = context.getSharedPreferences("dume", MODE_PRIVATE).getString("fcm_token", "undefined").equals("undefined") ? FirebaseInstanceId.getInstance().getToken()
                == null ? "undefined" : FirebaseInstanceId.getInstance().getToken() : context.getSharedPreferences("dume", MODE_PRIVATE).getString("fcm_token", "undefined");
        return token;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
}