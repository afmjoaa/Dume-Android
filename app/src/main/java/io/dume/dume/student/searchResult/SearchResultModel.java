package io.dume.dume.student.searchResult;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

public class SearchResultModel extends StuBaseModel implements SearchResultContract.Model {

    private static final String TAG = "SearchResultModel";
    public SearchResultModel(Context context) {
        super(context);
    }

    @Override
    public void searchResultHawwa() {
    }

    @Override
    public void riseNewRecords(Map<String, Object> data, TeacherContract.Model.Listener<DocumentReference> listener) {

        firestore.collection("records").add(data).addOnSuccessListener(documentReference -> {
            listener.onSuccess(documentReference);
        }).addOnFailureListener(e -> {
            listener.onError(e.getLocalizedMessage());
        });


    }

    @Override
    public void riseNewPushNoti(TeacherContract.Model.Listener<Void> listener) {
        String CHANNEL_1_ID = "Informative";
        int notification_id = 5432;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
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
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HomePageActivity.class);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_launcher)
                .setContentTitle("Dume request cancelled")
                .setContentText("Dear " + SearchDataStore.getInstance().getUserName() + " we noticed that you cancelled your request. Please check here to request again")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setLights(Color.CYAN, 2000, 2000)
                .setVibrate(new long[]{200, 500, 200, 500})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setTicker("Reminder")
                .setColor(context.getResources().getColor(R.color.noti_color));

        builder.setContentIntent(resultPendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(notification_id, builder.build());
            listener.onSuccess(null);
        }else {
            listener.onError("Error!!");
        }
    }


    @Override
    public void updateMentorDailys(List<String> imprssionUid, String requestUid, TeacherContract.Model.Listener<WriteBatch> listener) {
        // Get a new write batch
        WriteBatch batch = firestore.batch();
        for (int i = 0; i < imprssionUid.size(); i++) {
            //read first
            DocumentReference mentorDocRef = firestore.collection("/users/mentors/mentor_profile").document(imprssionUid.get(i));
            int finalI = i;
            mentorDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        assert document != null;
                        if (document.exists()) {
                            //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Integer dailyImpression = Integer.parseInt(document.getString("daily_i"));
                            Integer dailyRequest = Integer.parseInt(document.getString("daily_r"));
                            dailyImpression = dailyImpression+1;
                            dailyRequest = dailyRequest + 1;
                            if(requestUid!= null && requestUid == imprssionUid.get(finalI)){
                                batch.update(mentorDocRef, "daily_i", dailyImpression.toString(), "daily_r", dailyRequest.toString());
                            }else{
                                batch.update(mentorDocRef, "daily_i", dailyImpression.toString());
                            }
                            if(finalI == imprssionUid.size()-1){
                                listener.onSuccess(batch);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            if(finalI == imprssionUid.size()-1){
                                listener.onError("Unknown err !!");
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        listener.onError("Network err !!");
                    }
                }
            });
        }
    }
}
