package io.dume.dume.student.grabingLocation;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import io.dume.dume.student.pojo.StuBaseModel;

public class GrabingLocationModel extends StuBaseModel implements GrabingLocaitonContract.Model {

    public GrabingLocationModel(Activity activity, Context context) {
        super(activity, context);
    }

    @Override
    public void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener) {
        userStudentProInfo.addSnapshotListener(activity, updateViewListener);
    }

    @Override
    public void grabingLocationPagehawwa() {

    }
}
