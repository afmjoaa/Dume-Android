package io.dume.dume.teacher.mentor_settings.basicinfo;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

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
    private final Activity activity;
    private FirebaseFirestore database;
    private FirebaseAuth auth;
    private EditContract.onDataUpdate listener;
    private static EditModel instance = null;
    private static final String TAG = "Enam";
    private HashMap<String, Object> map;
    private final FirebaseStorage storage;
    private ListenerRegistration listenerRegistration;

    private EditModel(Context context) {
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        map = new HashMap<>();
        Log.w(TAG, "EditModel: " + database.hashCode());
        storage = FirebaseStorage.getInstance();

        this.activity = (Activity) context;
    }

    public static EditModel getModelInstance(Context context) {
        if (instance == null) {
            instance = new EditModel(context);
        }
        return instance;
    }

    @Override
    public void synWithDataBase(String first, String last, String avatarUrl, String email, String gender, String phone, String religion, String marital, String birth_date, GeoPoint geoPoint, String currentStatus) {
        if (this.listener != null) {
            map.put("avatar", avatarUrl);
            map.put("first_name", first);
            map.put("last_name", last);
            map.put("email", email);
            map.put("gender", gender);
            map.put("religion", religion);
            map.put("birth_date", birth_date);
            map.put("marital", marital);
            map.put("location", geoPoint);
            map.put("current_status", currentStatus);
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
    public void getDocumentSnapShot(TeacherContract.Model.Listener<DocumentSnapshot> listener) {
        listenerRegistration = database.document("users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getUid()).addSnapshotListener(activity, (documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                listener.onSuccess(documentSnapshot);
            } else {
                listener.onError("Got Nothing From Dume Database");

            }

        });
    }

}
