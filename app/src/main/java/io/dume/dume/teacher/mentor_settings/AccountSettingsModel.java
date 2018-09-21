package io.dume.dume.teacher.mentor_settings;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.inter_face.UserQueryListener;
import io.dume.dume.teacher.pojo.Education;
import io.dume.dume.teacher.pojo.GlobalListener;

public class AccountSettingsModel implements AccountSettingsContract.MentorModel {
    private static final String TAG = "AccountSettingsModel";
    private FirebaseFirestore firestore;

    public AccountSettingsModel() {
        firestore = FirebaseFirestore.getInstance();
    }


    @Override
    public int getData() {

        return 0;
    }

    @Override
    public void queryUserData(UserQueryListener listener) {
        listener.onStart();
        firestore.collection("mini_users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.w(TAG, "onComplete: ");
                if (task.isSuccessful()) {
                    DocumentSnapshot user = task.getResult();
                    if (user.exists()) {
                        Map<String, Object> data = user.getData();
                        Log.w(TAG, "onComplete: " + data);
                        listener.onSuccess(data);
                    }

                } else listener.onFailure("Request was not successful");
            }
        });

    }

    @Override
    public void queryAcademicData(GlobalListener.AcademicQuery listener) {
        listener.onStart();
        firestore.collection("users/mentors/mentor_profile").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).collection("academic_qualification").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    List<DocumentSnapshot> documents = result.getDocuments();
                    ArrayList<Education> arrayList = new ArrayList<>();

                    for (DocumentSnapshot item : documents) {
                        arrayList.add(new Education(Integer.parseInt(Objects.requireNonNull(item.get("from_year")).toString()), Integer.parseInt(Objects.requireNonNull(item.get("to_year")).toString()), Objects.requireNonNull(item.get("institution")).toString(), Objects.requireNonNull(item.get("degree")).toString(), Objects.requireNonNull(item.get("description")).toString()));
                        Log.e(TAG, "onComplete: " + Objects.requireNonNull(item.getData()).toString());
                    }
                    listener.onSuccess(arrayList);
                }
            }
        }).addOnFailureListener(e -> {
            listener.onFailure(e.getLocalizedMessage());
        });
    }


    @Override
    public void getDataArray(DataListener listener) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        arrayList.add("Doe");
        arrayList.add("Edward");
        arrayList.add("Alex");
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        new android.os.Handler().postDelayed(() -> {
            listener.onSuccess(arrayList);
        }, 1000);

    }


}
