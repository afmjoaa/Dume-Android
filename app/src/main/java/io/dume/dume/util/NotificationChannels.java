package io.dume.dume.util;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;


public class NotificationChannels extends Application {
    public static final String CHANNEL_1_ID = "Informative";
    public static final String CHANNEL_2_ID = "Notice";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Informative",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Informative, Messenger, Promo, action");
            channel1.enableLights(true);
            channel1.setLightColor(Color.CYAN);
            channel1.enableVibration(true);
            channel1.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel1.setShowBadge(true);

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Notice",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel2.setDescription("Notice Notifications");
            channel1.enableLights(true);
            channel1.setLightColor(Color.WHITE);
            channel1.enableVibration(true);
            channel1.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel1.setShowBadge(false);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}