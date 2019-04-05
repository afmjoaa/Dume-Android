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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.dume.dume.R;
import io.dume.dume.inter_face.usefulListeners;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.teacher.homepage.TeacherContract;

public class SearchResultModel extends StuBaseModel implements SearchResultContract.Model {

    private static final String TAG = "SearchResultModel";
    private int flag;

    public SearchResultModel(Context context) {
        super(context);
    }

    @Override
    public void searchResultHawwa() {
    }

    @Override
    public void riseNewRecords(Map<String, Object> data, boolean penaltyChanged, TeacherContract.Model.Listener<DocumentReference> listener) {

        if(penaltyChanged){
            Map<String, Object> map = new HashMap<>();
            Number penaltyValue = 0;
            map.put("penalty", penaltyValue);
            updateStuProfile(map, new usefulListeners.uploadToDBListerer() {
                @Override
                public void onSuccessDB(Object obj) {
                    firestore.collection("records").add(data).addOnSuccessListener((Activity) context, documentReference -> {
                        listener.onSuccess(documentReference);
                    }).addOnFailureListener(e -> {
                        listener.onError(e.getLocalizedMessage());
                    });
                }

                @Override
                public void onFailDB(Object obj) {
                    listener.onError(obj.toString());
                }
            });
        }else {
            firestore.collection("records").add(data).addOnSuccessListener((Activity) context, documentReference -> {
                listener.onSuccess(documentReference);
            }).addOnFailureListener(e -> {
                listener.onError(e.getLocalizedMessage());
            });
        }
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

        Intent intent = new Intent(context,HomePageActivity.class);
        Random generator = new Random();
        PendingIntent resultPendingIntent=PendingIntent.getActivity(context, generator.nextInt(), intent,PendingIntent.FLAG_UPDATE_CURRENT);
        //TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        //stackBuilder.addParentStack(HomePageActivity.class);
        //PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_launcher)
                .setContentTitle("Dume request cancelled")
                .setContentText("Dear " + SearchDataStore.getInstance().getUserName() + " we noticed that you cancelled your request. Please check here to request again")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setLights(Color.CYAN, 1200, 2000)
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
    public void updateMentorDailys(List<String> imprssionUid, String requestUid, TeacherContract.Model.Listener<Void> listener) {
        // Get a new write batch
        WriteBatch batch = firestore.batch();
        flag = 0;
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
                            Map<String, Object> unreadRecords = (Map<String, Object>) document.get("unread_records");
                            Integer dailyImpression = Integer.parseInt(document.getString("daily_i"));
                            Integer dailyRequest = Integer.parseInt(document.getString("daily_r"));
                            Integer pendingCount = Integer.parseInt(unreadRecords.get("pending_count").toString());
                            pendingCount = pendingCount+1;
                            dailyImpression = dailyImpression+1;
                            dailyRequest = dailyRequest + 1;
                            if(requestUid!= null && requestUid == imprssionUid.get(finalI)){
                                batch.update(mentorDocRef, "daily_i", dailyImpression.toString(), "daily_r", dailyRequest.toString(), "unread_records.pending_count",pendingCount.toString());

                                DocumentReference studentProfile =firestore.collection("/users/students/stu_pro_info").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Map<String, Object> myDocumentSnap = SearchDataStore.getInstance().getDocumentSnapshot();
                                Map<String, Object> myUnreadRecords = (Map<String, Object>) myDocumentSnap.get("unread_records");
                                Integer myPendingCount = Integer.parseInt(myUnreadRecords.get("pending_count").toString());
                                myPendingCount = myPendingCount+1;
                                batch.update(studentProfile,  "unread_records.pending_count",myPendingCount.toString());
                            }else{
                                batch.update(mentorDocRef, "daily_i", dailyImpression.toString());
                            }
                            flag++;
                            if (flag == imprssionUid.size()) {
                                batch.commit();
                                listener.onSuccess(null);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                            flag++;
                            if (flag == imprssionUid.size()) {
                                listener.onError("Unknown err !!");
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        flag++;
                        listener.onError("Network err !!");
                    }
                }
            });
        }
    }
}
