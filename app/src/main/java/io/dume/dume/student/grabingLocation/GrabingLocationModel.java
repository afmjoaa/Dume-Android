package io.dume.dume.student.grabingLocation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.teacher.homepage.TeacherContract;

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

    @Override
    public void updateFavoritePlaces(String identify, Map<String, Object> savedPlacesAdaData, TeacherContract.Model.Listener<Void> listener) {
        switch (identify) {
            case "Home":
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
            case "Work":
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
    }

    @Override
    public void updatePermanentAddress(GeoPoint point, TeacherContract.Model.Listener listener) {
        userStudentProInfo.update("current_address", point).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }

    @Override
    public void updateRecentPlaces(String identify, Map<String, Object> savedPlacesAdaData, TeacherContract.Model.Listener<Void> listener) {
        switch (identify) {
            case "rp_1":
                userStudentProInfo.update("recent_places.rp_1", savedPlacesAdaData, "next_rp_write", "2").addOnSuccessListener(new OnSuccessListener<Void>() {
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
            case "rp_2":
                userStudentProInfo.update("recent_places.rp_2", savedPlacesAdaData,"next_rp_write", "3").addOnSuccessListener(new OnSuccessListener<Void>() {
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
            case "rp_3":
                userStudentProInfo.update("recent_places.rp_3", savedPlacesAdaData, "next_rp_write", "1").addOnSuccessListener(new OnSuccessListener<Void>() {
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
    }

}
