package io.dume.dume.teacher.mentor_settings;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.teacher.pojo.User;

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
    public ArrayList<User> getUser(DataListener listener) {
        firestore.collection("mini_user").document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot user = task.getResult();
                    if (user.exists()) {
                        Map<String, Object> data = user.getData();
                        Log.w(TAG, "onComplete: " + data);
                    }

                } else listener.onFailure("Request was not successful");
            }
        });
        return null;
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
