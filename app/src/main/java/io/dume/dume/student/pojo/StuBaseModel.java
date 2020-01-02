package io.dume.dume.student.pojo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import androidx.annotation.NonNull;
import io.dume.dume.interFace.usefulListeners;

public abstract class StuBaseModel {
    private static final String TAG = "StuBaseModel";
    protected Context context;
    protected Activity activity;
    private final FirebaseAuth mAuth;
    protected final FirebaseFirestore fireStore;
    private DocumentReference mini_users;
    protected DocumentReference userStudentProInfo;
    protected final CollectionReference skillRef;

    public StuBaseModel(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        skillRef = fireStore.collection("users/mentors/skills");

        //////hard coded string for testing/////
        mini_users = fireStore.collection("mini_users").document("fDTDMUod3UPN36nbBepXHVB9k1r1");
        userStudentProInfo = fireStore.collection("/users/students/stu_pro_info").document("fDTDMUod3UPN36nbBepXHVB9k1r1");
    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

    public static boolean setStuProfile(Activity activity, DocumentReference userStudentProInfo, Map<String, Object> stuProfileInfo) {
        return userStudentProInfo.set(stuProfileInfo).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Log.e(TAG, "onComplete: " + "written to student database");
                }
            }
        }).isSuccessful();
    }

    protected boolean updateStuProfile(Map<String, Object> stuProfileInfo, usefulListeners.uploadToDBListerer updateListener) {
        return userStudentProInfo.update(stuProfileInfo).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Log.e(TAG, "onComplete: " + "written to student database");
                    updateListener.onSuccessDB("Profile updated.");
                }
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateListener.onFailDB(e.getMessage());
            }
        }).addOnCanceledListener(activity, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                updateListener.onFailDB("on cancel called ");
            }
        }).isSuccessful();
    }
}
