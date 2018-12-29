package io.dume.dume.student.pojo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Map;

import io.dume.dume.auth.DataStore;


public class StuBaseModel {
    private static final String TAG = "StuBaseModel";
    protected Context context;
    protected Activity activity;
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore firestore;
    private StuDataStore stuDataStore = null;
    private final DocumentReference mini_users;
    private final DocumentReference userStudentProInfo;


    public StuBaseModel(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        Log.w(TAG, "StuBaseModel: " + firestore.hashCode());
        //initializing
        mini_users = firestore.collection("mini_users").document(getUser().getUid());
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

    public static boolean setStuProfile(Activity activity, DocumentReference userStudentProInfo, Map<String, Object> stuProfileInfo){
        return  userStudentProInfo.set(stuProfileInfo).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.e(TAG, "onComplete: " + "written to student database" );
                }
            }
        }).isSuccessful();
    }

    public boolean updateStuProfile(Map<String, Object> stuProfileInfo){
        return  userStudentProInfo.update(stuProfileInfo).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Log.e(TAG, "onComplete: " + "written to student database" );
                }
            }
        }).isSuccessful();
    }


}
//   Map<String, Object> newMap = new HashMap<>();
