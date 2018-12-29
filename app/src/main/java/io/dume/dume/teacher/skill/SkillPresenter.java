package io.dume.dume.teacher.skill;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.util.DumeUtils;

public class SkillPresenter implements SkillContract.Presenter {
    private SkillContract.Model model;
    private SkillContract.View view;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ListenerRegistration listenerRegistration;


    private static final String TAG = "SkillPresenter";


    public SkillPresenter(SkillContract.Model model, SkillContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void enqueue() {
        view.init();
        view.loadSkillRV(null);
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        Log.w(TAG, "AuthModel: " + firestore.hashCode());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewInteracted(View element) {
        switch (element.getId()) {
            case R.id.fabAdd:
                DocumentReference mini_users = firestore.collection("mini_users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                listenerRegistration = mini_users.addSnapshotListener((documentSnapshot, e) -> {
                    if (documentSnapshot != null) {
                        //Log.w(TAG, "onAccountTypeFound: " + documentSnapshot.toString());
                        //Log.e(TAG, "Fucked Here : " + documentSnapshot.toString());
                        detachListener();
                        Object o = documentSnapshot.get("account_major");
                        String account_major = "";
                        if (o != null) {
                            account_major = o.toString();
                        }
                        assert account_major != null;
                        if (account_major.equals(DumeUtils.TEACHER)) {
                            view.goToCrudActivity(DumeUtils.TEACHER);
                        } else if(account_major.equals(DumeUtils.BOOTCAMP)){
                            view.goToCrudActivity(DumeUtils.BOOTCAMP);
                        }
                    }
                });
                break;
        }
    }

    public void detachListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
