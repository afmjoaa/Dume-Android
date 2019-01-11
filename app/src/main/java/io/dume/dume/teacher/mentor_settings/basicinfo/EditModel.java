package io.dume.dume.teacher.mentor_settings.basicinfo;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import javax.annotation.Nullable;

import io.dume.dume.teacher.homepage.TeacherContract;

public class EditModel implements EditContract.Model {
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private EditContract.onDataUpdate listener;
    private static EditModel instance = null;
    private static final String TAG = "Enam";
    private HashMap<String, Object> map;
    private final FirebaseStorage storage;
    private ListenerRegistration listenerRegistration;

    private EditModel() {
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        map = new HashMap<>();
        Log.w(TAG, "EditModel: " + database.hashCode());
        storage = FirebaseStorage.getInstance();
    }

    public static EditModel getModelInstance() {
        if (instance == null) {
            instance = new EditModel();
        }
        return instance;
    }


    @Override
    public void synWithDataBase(String first, String last, String avatarUrl, String email, String gender, String phone, String religion, String marital,String birth_date) {
        if (this.listener != null) {
            map.put("avatar", avatarUrl);
            map.put("first_name", first);
            map.put("last_name", last);
            map.put("email", email);
            map.put("gender", gender);
            map.put("religion", religion);
            map.put("birth_date",birth_date);
            map.put("marital", marital);
            /*users/mentors/mentor_profile/*/
            database.collection("users/mentors/mentor_profile").document(Objects.requireNonNull(auth.getUid())).update(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onUpdateSuccess();
                } else if (task.isCanceled()) {
                    listener.onFail("Update Cancelled");
                }
            }).addOnFailureListener(e ->
                    listener.onFail(e.toString())
            );

        } else {
            Log.e(TAG, "synWithDataBase: Set Listener First");
        }
    }

    @Override
    public void setListener(EditContract.onDataUpdate listener) {
        this.listener = listener;
    }

    @Override
    public void uploadImage(Uri uri, EditContract.DownloadListener progressListener) {
        StorageReference imgRef = storage.getReference(Objects.requireNonNull(auth.getUid()));
        UploadTask uploadTask = imgRef.putFile(uri);


        uploadTask.continueWithTask(task -> imgRef.getDownloadUrl())
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        Log.w(TAG, "uploadImage: " + task.getResult());
                        progressListener.onSuccess(task.getResult().toString());
                    }
                })
                .addOnFailureListener(e ->
                        progressListener.onFail(e.toString()))
                .addOnCanceledListener(() ->
                        progressListener.onFail("Upload Cancelled")
                );

    }

    @Override
    public void getLocation(TeacherContract.Model.Listener<GeoPoint> listener) {
        listenerRegistration = database.document("users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                GeoPoint location = (GeoPoint) documentSnapshot.get("location");

                if (location != null) {
                    listener.onSuccess(location);
                } else {
                    listener.onError("");

                }Log.w(TAG, "onEvent: " + location.toString());
                listenerRegistration.remove();
            }
        });
    }


    @Override
    public void updateLocaiton(GeoPoint point, TeacherContract.Model.Listener listener) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("location", point);
        database.document("users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getLocalizedMessage());
            }
        });
    }


}
