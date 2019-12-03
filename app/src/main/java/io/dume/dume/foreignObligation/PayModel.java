package io.dume.dume.foreignObligation;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import io.dume.dume.teacher.homepage.TeacherContract;

public class PayModel implements PayContact.Model {
    private static final String TAG = "PayModel";
    private static PayModel instance = null;
    private final Context context;
    private final Activity activity;
    private final FirebaseFirestore firestore;
    private final FirebaseAuth mAuth;


    public PayModel(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
    }

    public static PayModel getModelInstance() {
        return instance;
    }

    @Override
    public void setInstance(PayModel mModel) {
        PayModel.instance = mModel;
    }

    @Override
    public void payActivityHawwa() {

    }

    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener) {
        firestore.collection("mini_users").document(getUser().getUid()).addSnapshotListener(activity, updateViewListener);
    }

    @Override
    public Task<DocumentSnapshot> getResultSnapShot(TeacherContract.Model.Listener<DocumentSnapshot> listener) {
        return   firestore.collection("mini_users").document(getUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        listener.onSuccess(document);
                    } else {
                        listener.onError("Unknown Error !!");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                    listener.onError("Network Error !!");
                }
            }
        });
    }
}
