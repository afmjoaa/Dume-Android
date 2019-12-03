package io.dume.dume.student.profilePage;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.interFace.usefulListeners;
import io.dume.dume.student.pojo.StuBaseModel;

public class ProfilePageModel extends StuBaseModel implements ProfilePageContract.Model {
    private static final String TAG = "ProfilePageModel";
    private static ProfilePageModel instance = null;
    private Context context;
    private Activity activity;
    private HashMap<String, Object> map;
    private FirebaseStorage storage;


    public ProfilePageModel(Context context) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
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
    public Boolean synWithDataBase(String fn, String ln, String mail, GeoPoint ca, String cs, String pr, String gender, String progress, String avatar,
                                   usefulListeners.uploadToDBListerer progressListener) {
        Map<String, Object> map = new HashMap<>();
        map.put("first_name", fn);
        map.put("last_name", ln);
        map.put("email", mail);
        map.put("current_address", ca);
        map.put("current_status_icon", cs);
        map.put("previous_result", pr);
        map.put("gender", gender);
        map.put("pro_com_%", progress);
        map.put("avatar", avatar);
        return updateStuProfile(map, progressListener);
    }

    @Override
    public void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener) {
        userStudentProInfo.addSnapshotListener(activity, updateViewListener);
    }

    @Override
    public void uploadImage(Uri uri, usefulListeners.uploadToSTGListererMin progressListener) {
        StorageReference imgRef = storage.getReference(Objects.requireNonNull("stu_" + getUser().getUid()));
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



}
