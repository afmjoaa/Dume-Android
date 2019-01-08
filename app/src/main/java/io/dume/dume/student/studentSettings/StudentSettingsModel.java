package io.dume.dume.student.studentSettings;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import io.dume.dume.student.pojo.StuBaseModel;

public class StudentSettingsModel extends StuBaseModel implements StudentSettingsContract.Model{
    private static final String TAG = "StudentSettingsModel";

    public StudentSettingsModel(Activity activity, Context context) {
        super(activity, context);
    }

    @Override
    public void studentSettingshawwa() {

    }

    @Override
    public void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener) {
        userStudentProInfo.addSnapshotListener(activity, updateViewListener);
    }
}
