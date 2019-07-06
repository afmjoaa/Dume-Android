package io.dume.dume.student.recordsPending;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;
import java.util.Objects;

import io.dume.dume.inter_face.usefulListeners;
import io.dume.dume.student.recordsPage.RecordsPageModel;
import io.dume.dume.teacher.homepage.TeacherContract;

public class RecordsPendingModel extends RecordsPageModel implements RecordsPendingContract.Model {

    private Context context;
    private Activity activity;

    public RecordsPendingModel(Context context) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;

    }



    @Override
    public void recordsPendingHawwa() {

    }

    @Override
    public void uploadPhotoId(Uri uri, usefulListeners.uploadToSTGListererMin progressListener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference imgRef = FirebaseStorage.getInstance().getReference(Objects.requireNonNull("photo_id" + uid));
        UploadTask uploadTask = imgRef.putFile(uri);

        uploadTask.continueWithTask(task -> imgRef.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    progressListener.onSuccessSTG(task.getResult().toString());//this is the url returned
                }).addOnFailureListener(exception -> {
            progressListener.onFailSTG(exception.toString());
        }).addOnCanceledListener(() -> {
            progressListener.onFailSTG("Upload is canceled");
        });
    }

    public void updatePhotoIdToMentorProfile(Map<String, Object> updateData, usefulListeners.uploadToDBListerer updateListener) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference mentorProfile = FirebaseFirestore.getInstance().collection("/users/mentors/mentor_profile").document(uid);
        mentorProfile.update(updateData).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    updateListener.onSuccessDB("Profile updated.");
                }
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                updateListener.onFailDB(e.getMessage());
            }
        }).addOnCanceledListener(activity, new OnCanceledListener() {
            @Override
            public void onCanceled() {
                updateListener.onFailDB("on cancel called ");
            }
        });
    }

}
