package io.dume.dume.teacher.skill;

import android.util.Log;
import android.view.View;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.teacher.homepage.TeacherContract;

import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.util.DumeUtils;

public class SkillPresenter implements SkillContract.Presenter {
    private SkillContract.Model model;
    private SkillContract.View view;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ListenerRegistration listenerRegistration;


    private static final String TAG = "SkillPresenter";

    private io.dume.dume.model.TeacherModel teacherModel;
    private Map<String, Object> documentSnapshot;
    private int percentage;

    public SkillPresenter(SkillContract.Model model, SkillContract.View view) {
        this.model = model;
        this.view = view;
        teacherModel = new DumeModel();
    }

    @Override
    public void enqueue() {
        view.init();
        teacherModel.getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
            @Override
            public void onSuccess(ArrayList<Skill> list) {
                view.loadSkillRV(list);
                Log.e(TAG, "onSuccess: " + list.size());
            }

            @Override
            public void onError(String msg) {
                view.flush(msg);
            }
        });

        firestore = FirebaseFirestore.getInstance();
      /*  FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);*/
        Log.w(TAG, "AuthModel: " + firestore.hashCode());
        mAuth = FirebaseAuth.getInstance();
    }


    protected boolean isProfileOK() {
        documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();


        if (documentSnapshot != null) {
            String beh = (String) documentSnapshot.get("pro_com_%");
            percentage = Integer.parseInt(beh);
            if (percentage >= 95) {
                return true;
            }

        }
        view.flush("Profile should be at least 95% completed");
        String snackString = "Profile only " + percentage + "% complete";
        view.showSnack(snackString, "GO TO PROFILE");
        return false;

    }

    @Override
    public void onViewInteracted(View element) {
        switch (element.getId()) {
            case R.id.fab_regular:
                if (isProfileOK()) {
                    view.goToCrudActivity(DumeUtils.TEACHER);
                }


                break;
            case R.id.fab_gang:
                if (isProfileOK()) {
                    view.goToCrudActivity(DumeUtils.BOOTCAMP);
                }
                break;
            case R.id.fab_instant:
                if (isProfileOK()) {
                    final Map<String, Boolean> achievements = (Map<String, Boolean>) documentSnapshot.get("achievements");
                    Boolean premier = achievements.get("premier");
                    if (premier) {
                        view.goToCrudActivity(DumeUtils.TEACHER);
                    } else
                        view.flush("You can't create instant dume without unlocking Premier Badge.");
                }

                break;
            case R.id.view_musk:
                view.performMultiFabClick();
                break;
            case R.id.multiple_actions:
                break;
            /*case R.id.fabAdd:
                DocumentReference mini_users = firestore.collection("mini_users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                listenerRegistration = mini_users.addSnapshotListener((documentSnapshot, e) -> {
                    if (documentSnapshot != null) {
                        detachListener();
                        Object o = documentSnapshot.get("account_major");
                        String account_major = "";
                        if (o != null) {
                            account_major = o.toString();
                        }
                        assert account_major != null;
                        if (account_major.equals(DumeUtils.TEACHER)) {
                            view.goToCrudActivity(DumeUtils.TEACHER);
                        } else if (account_major.equals(DumeUtils.BOOTCAMP)) {
                            view.goToCrudActivity(DumeUtils.BOOTCAMP);
                        }
                    }
                });
                break;*/
        }
    }

    public void detachListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
