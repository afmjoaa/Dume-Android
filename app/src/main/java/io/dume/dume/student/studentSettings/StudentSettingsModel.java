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

    public void updateFavoritePlaces(String identify, SavedPlacesAdaData savedPlacesAdaData, TeacherContract.Model.Listener<Void> listener) {
        switch (identify) {
            case "home":
                userStudentProInfo.update("favourite_places.home", savedPlacesAdaData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess(aVoid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getLocalizedMessage());
                    }
                });
                break;
            case "work":
                userStudentProInfo.update("favourite_places.work", savedPlacesAdaData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess(aVoid);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getLocalizedMessage());
                    }
                });
                break;
            default:
                listener.onError("else running");
                break;
        }
        /* userStudentProInfo.update("favourite_places", FieldValue.arrayUnion(savedPlacesAdaData)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        });*/
    }


}
