package io.dume.dume.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.Keep;
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
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.splash.SplashActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.recordsPage.RecordsPageActivity;
import io.dume.dume.student.studentPayment.StudentPaymentActivity;
import io.dume.dume.util.DumeUtils;

@Keep
public class MyNotification extends FirebaseMessagingService {

    private static String token;
    private final FirebaseFirestore firestore;
    private String type = "";
    public static String UNREAD_MESSAGE = "unread_message";
    private SharedPreferences prefs;
    private int TAB_NUMBER;
    private static final String TAG = "MyNotification";

    @SuppressLint("CommitPrefEdits")
    public MyNotification() {
        super();
        firestore = FirebaseFirestore.getInstance();

    }

    private void sendNotification(String messageTitle, String messageBody, String type) {

        Class intentClass = SplashActivity.class;
        Bundle bundle = new Bundle();
        if (type.equals("message")) {
            intentClass = InboxActivity.class;
        } else if (type.equals("payment")) {
            intentClass = StudentPaymentActivity.class;
        } else if (type.equals("pending")) {
            intentClass = RecordsPageActivity.class;
            TAB_NUMBER = 0;
        } else if (type.equals("accepted")) {
            intentClass = RecordsPageActivity.class;
            TAB_NUMBER = 1;
        } else if (type.equals("current")) {
            intentClass = RecordsPageActivity.class;
            TAB_NUMBER = 2;
        } else if (type.equals("completed")) {
            intentClass = RecordsPageActivity.class;
            TAB_NUMBER = 3;
        } else if (type.equals("rejected")) {
            intentClass = RecordsPageActivity.class;
            TAB_NUMBER = 4;
        }
        Log.w(TAG, "sendNotification: " + intentClass.toString());


        int NOTIFICATION_ID = 234;
        prefs = getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE);
        String ringToneString = prefs.getString("notifications_ringtone", null);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "Urgent notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{200, 500, 200, 500});
            mChannel.setShowBadge(true);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.CYAN);
            mChannel.shouldShowLights();
            mChannel.shouldVibrate();
            mChannel.canShowBadge();
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            if (ringToneString != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(this, Uri.parse(ringToneString));
                if (ringtone != null) {
                    mChannel.setSound(Uri.parse(ringToneString), new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
                } else {
                    mChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build());
                }
            } else {
                mChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
            }
            /*if (type.equals("message") || type.equals("payment")) {
                mChannel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
            } else {
            }*/
            mChannel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_01")
                .setSmallIcon(R.drawable.ic_notification_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setLights(Color.CYAN, 1000, 2000)
                .setVibrate(new long[]{200, 500, 200, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setColor(getResources().getColor(R.color.noti_color))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true);
        if (ringToneString != null) {
            Ringtone ringtone = RingtoneManager.getRingtone(this, Uri.parse(ringToneString));
            if (ringtone != null) {
                builder.setSound(Uri.parse(ringToneString));
            }
        }
        //setting priority
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent resultIntent = new Intent(this, intentClass);
        resultIntent.putExtra("tab_number", TAB_NUMBER);
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

            if ("message".equals(type)) {
                if (!ChatActivity.isActivityLive) {
                    sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), type);
                    SharedPreferences sharedPreferences = getSharedPreferences(UNREAD_MESSAGE, MODE_PRIVATE);
                    int unread = sharedPreferences.getInt("unread", 0);
                    unread++;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("unread", unread);
                    editor.apply();
                }
            } else {
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), type);
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

/*// do your work here
                prefs = getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE);
                String ringToneString = prefs.getString("notifications_ringtone", null);
                if(ringToneString!= null){
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            this, Uri.parse(ringToneString));
                    if(ringtone!= null){
                        //TODO play
                        try {
                            ringtone.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }else{
                    //TODO play default
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/