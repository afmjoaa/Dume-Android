package io.dume.dume.student.grabingInfo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import io.dume.dume.inter_face.usefulListeners;
import io.dume.dume.student.pojo.StuBaseModel;

public class GrabingInfoModel extends StuBaseModel implements GrabingInfoContract.Model {
    private final FirebaseStorage storage;
    private static final String TAG = "GrabingInfoModel";

    public GrabingInfoModel(Context context) {
        super(context);
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public void grabingInfoPagehawwa() {

    }

    @Override
    public void uploadImage(InputStream uri, usefulListeners.uploadToSTGListererMin progressListener, String ref) {
        StorageReference imgRef = storage.getReference(Objects.requireNonNull(ref + getUser().getUid()));
        UploadTask uploadTask = imgRef.putStream(uri);
        uploadTask.continueWithTask((Task<UploadTask.TaskSnapshot> task) -> {
            return imgRef.getDownloadUrl();
        })
                .addOnCompleteListener(task -> {
                    progressListener.onSuccessSTG(task.getResult().toString());//this is the url returned
                }).addOnFailureListener(exception -> {
            progressListener.onFailSTG(exception.toString());
        }).addOnCanceledListener(() -> {
            progressListener.onFailSTG("Upload is canceled");
        });
    }
}
