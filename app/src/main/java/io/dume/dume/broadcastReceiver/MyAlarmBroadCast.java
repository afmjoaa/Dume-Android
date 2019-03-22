package io.dume.dume.broadcastReceiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.List;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.homePage.StudentActivity;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.util.DumeUtils;

public class MyAlarmBroadCast extends BroadcastReceiver {

    public static final String CHANNEL_1_ID = "Informative";
    public static final String ACTION_ALARM_RECEIVER = "ACTION_ALARM_RECEIVER";
    public static final String NOTIFICATION_DISMISSED = "FIRED_BY_ANDROID_NOTIFICATION_STYSTEM";
    private MediaPlayer mediaPlayer = null;
    private NotificationManager notificationManager = null;
    private int notification_id = 234;
    private static final String TAG = "MyAlarmBroadCast";

    public MyAlarmBroadCast() {
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.w(TAG, "onReceive: " + intent.getAction());

        String action = intent.getAction() == null ? "" : intent.getAction();
        if (action.equals(NOTIFICATION_DISMISSED)) {
            NotificationManager mahNoti = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mahNoti.cancel(notification_id);
            if (Google.getInstance().getmMediaPlayer().size() > 0) {
                List<MediaPlayer> mediaPlayers = Google.getInstance().getmMediaPlayer();
                for (MediaPlayer mediaPlayer : mediaPlayers) {
                    mediaPlayer.stop();
                }
            }
            return;
        }
        mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();
        Google.getInstance().setmMediaPlayer(mediaPlayer);

        notification_id = 234;
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Informative",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Informative, Messenger, Promo, action");
            channel1.enableLights(true);
            channel1.setLightColor(Color.CYAN);
            channel1.enableVibration(true);
            channel1.setVibrationPattern(new long[]{200, 500, 200, 500});
            channel1.setShowBadge(true);
            notificationManager.createNotificationChannel(channel1);
        }

        Intent resultIntent = new Intent(context, RecordsCurrentActivity.class).setAction(DumeUtils.STUDENT);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomePageActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        //final Intent[] intents = stackBuilder.getIntents();

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        //setting the pending intent for the add action btn
        Intent actionIntent = new Intent(context, MyAlarmBroadCast.class);
        actionIntent.putExtra("fromWhere", "identify"); //If you wan to send data also
        actionIntent.setAction(NOTIFICATION_DISMISSED);
        PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, 1000, actionIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_launcher)
                .setContentTitle("Reminder. Your students may be wating.")
                .setContentText("Hurry up. its time to guide your student")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setDeleteIntent(actionPendingIntent)
                .setLights(Color.CYAN, 2000, 2000)
                .setVibrate(new long[]{200, 500, 200, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .addAction(R.drawable.ic_disable_notification, "Calm Down", actionPendingIntent)
                .setTicker("Reminder")
                .setColor(context.getResources().getColor(R.color.noti_color));

        builder.setContentIntent(resultPendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(notification_id, builder.build());
        }
    }
}
