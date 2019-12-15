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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import androidx.annotation.NonNull;
import io.dume.dume.interFace.usefulListeners;

public abstract class StuBaseModel{
    private static final String TAG = "StuBaseModel";
    protected Context context;
    protected Activity activity;
    protected final FirebaseAuth mAuth;
    protected final FirebaseFirestore firestore;
    protected final DocumentReference mini_users;
    protected final DocumentReference userStudentProInfo;
    public final CollectionReference skillRef;

    public StuBaseModel(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        mini_users = firestore.collection("mini_users").document(getUser().getUid());
        skillRef = firestore.collection("users/mentors/skills");
        userStudentProInfo = firestore.collection("/users/students/stu_pro_info").document(getUser().getUid());
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

    public boolean updateUserInfo(Map<String, Object> userInfo) {
        return mini_users.update(userInfo).isSuccessful();
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

    public boolean updateStuProfile(Map<String, Object> stuProfileInfo, usefulListeners.uploadToDBListerer updateListener) {
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

    public DocumentSnapshot getStuProfile() {
        return userStudentProInfo.get().getResult();
    }

}
//Map<String, Object> newMap = new HashMap<>();
//only set get and update functions will be there
//and addSnapshotListener or query functions will be in the specific model class
