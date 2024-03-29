package io.dume.dume.components.services;

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
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Keep;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import io.dume.dume.R;
import io.dume.dume.commonActivity.chatActivity.ChatActivity;
import io.dume.dume.commonActivity.inboxActivity.InboxActivity;
import io.dume.dume.splash.SplashActivity;
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
    private int TAB_NUMBER = 0;
    private static final String TAG = "MyNotification";
    private Intent resultIntent;

    @SuppressLint("CommitPrefEdits")
    public MyNotification() {
        super();
        firestore = FirebaseFirestore.getInstance();
    }

    private void sendNotification(String messageTitle, String messageBody, String type) {

        //Log.w(TAG, "sendNotification: " + intentClass.toString());

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
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        //update here
        switch (type) {
            case "message":
                resultIntent = new Intent(getApplicationContext(), InboxActivity.class);
                TAB_NUMBER = 0;
                break;
            case "payment":
                resultIntent = new Intent(getApplicationContext(), StudentPaymentActivity.class);
                TAB_NUMBER = 1;
                break;
            case "pending":
                resultIntent = new Intent(getApplicationContext(), RecordsPageActivity.class);
                resultIntent.putExtra("seletedTab", 0);
                TAB_NUMBER = 2;
                break;
            case "accepted":
                resultIntent = new Intent(getApplicationContext(), RecordsPageActivity.class);
                resultIntent.putExtra("seletedTab", 1);
                TAB_NUMBER = 3;
                break;
            case "current":
                resultIntent = new Intent(getApplicationContext(), RecordsPageActivity.class);
                resultIntent.putExtra("seletedTab", 2);
                TAB_NUMBER = 4;
                break;
            case "completed":
                resultIntent = new Intent(getApplicationContext(), RecordsPageActivity.class);
                resultIntent.putExtra("seletedTab", 3);
                TAB_NUMBER = 5;
                break;
            case "rejected":
                resultIntent = new Intent(getApplicationContext(), RecordsPageActivity.class);
                resultIntent.putExtra("seletedTab", 4);
                TAB_NUMBER = 6;
                break;
            default:
                resultIntent = null;
                TAB_NUMBER = 7;
                break;
        }

        stackBuilder.addNextIntent(new Intent(getApplicationContext(), SplashActivity.class));
        if(resultIntent!= null){
            stackBuilder.addNextIntent(resultIntent);
        }
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(TAB_NUMBER, PendingIntent.FLAG_UPDATE_CURRENT);
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


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid ="";
        if(currentUser!= null ){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        HashMap<String, Object> saveData = new HashMap<>();
        saveData.put("token", s);
        //testing code here
        if(!uid.equals("")){
            FirebaseFirestore.getInstance().collection("token").document(uid).set(saveData).addOnSuccessListener(aVoid -> {
                //Log.e("foo", "onSuccess token: " );
            }).addOnFailureListener(e -> {
                //Log.e("foo", "onFailure:token " + e.getLocalizedMessage());
            });
        }
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