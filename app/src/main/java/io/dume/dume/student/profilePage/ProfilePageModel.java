package io.dume.dume.student.profilePage;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.inter_face.usefulListeners;
import io.dume.dume.student.pojo.StuBaseModel;

public class ProfilePageModel extends StuBaseModel implements ProfilePageContract.Model {
    private static final String TAG = "ProfilePageModel";
    private static ProfilePageModel instance = null;
    private Context context;
    private Activity activity;
    private HashMap<String, Object> map;
    private final FirebaseStorage storage;


    public ProfilePageModel(Activity activity, Context context) {
        super(activity, context);
        this.context = context;
        this.activity = activity;
        map = new HashMap<>();
        storage = FirebaseStorage.getInstance();
    }

    public static ProfilePageModel getModelInstance() {
        return instance;
    }

    @Override
    public void setInstance(ProfilePageModel mModel) {
        ProfilePageModel.instance = mModel;
    }

    @Override
    public Boolean synWithDataBase(String fn, String ln, String mail, GeoPoint ca, String cs, String pr,String gender, Number progress, String avatar) {
        Map<String, Object> map = new HashMap<>();
        map.put("first_name", fn);
        map.put("last_name", ln);
        map.put("email", mail);
        map.put("current_address", ca);
        map.put("current_status", cs);
        map.put("previous_result", pr);
        map.put("gender", gender);
        map.put("pro_com_%", progress);
        map.put("avatar", avatar);
        return updateStuProfile(map);
    }

    @Override
    public void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener) {
        userStudentProInfo.addSnapshotListener(activity, updateViewListener);
    }

    @Override
    public void uploadImage(Uri uri, usefulListeners.uploadListenerMin progressListener) {
        StorageReference imgRef = storage.getReference(Objects.requireNonNull(getUser().getUid()));
        UploadTask uploadTask = imgRef.putFile(uri);

        uploadTask.continueWithTask(task -> imgRef.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    progressListener.onSuccess(task.getResult().toString());//this is the url returned
                }).addOnFailureListener(exception -> {
            progressListener.onFail(exception.toString());
        }).addOnCanceledListener(() -> {
            progressListener.onFail("Upload is canceled");
        });
    }


}
