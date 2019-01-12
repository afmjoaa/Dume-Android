package io.dume.dume.student.studentSettings;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;

import java.util.List;
import java.util.Map;

import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.teacher.homepage.TeacherContract;

public class StudentSettingsModel extends StuBaseModel implements StudentSettingsContract.Model {
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

    public void updateFavoritePlaces(Map<String, Object> savedPlacesAdaData, TeacherContract.Model.Listener<Void> listener) {
        userStudentProInfo.update("favourite_places", FieldValue.arrayUnion(savedPlacesAdaData)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.w(TAG, "onSuccess: " + "Ok");
                listener.onSuccess(aVoid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getLocalizedMessage());
            }
        });

        userStudentProInfo.update("fp.home", savedPlacesAdaData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


}
