package io.dume.dume.teacher.mentor_settings.academic;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AcademicModel implements AcademicContract.Model {
    private static AcademicModel academicModel = null;
    private FirebaseFirestore firestore = null;
    private FirebaseAuth mAuth = null;
    private ModelCallback listener;
    private Map<String, Object> hashMap;

    private AcademicModel() {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        hashMap = new HashMap<>();
    }

    public static AcademicModel getInstance() {
        if (academicModel == null) {
            academicModel = new AcademicModel();
        }
        return academicModel;
    }

    @Override
    public void syncWithDatabase(@NonNull String institution, String degree, String from, String to, String description, String result, String resultType) {
        if (listener != null) {
            listener.onStart();
            hashMap.clear();
            hashMap.put("institution", institution);
            hashMap.put("degree", degree);
            hashMap.put("from_year", from);
            hashMap.put("to_year", to);
            hashMap.put("description", description);
            hashMap.put("result_type", resultType);
            hashMap.put("result", result);
            firestore.collection("users").document("mentors").collection("mentor_profile").document(Objects.requireNonNull(mAuth.getUid())).update("academic." + degree, hashMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                }
            }).addOnFailureListener(e -> listener.onFail(e.toString()));
        } else throw new Error("Listener Not Set at AcademicModel");
    }


    @Override
    public void attachCallback(ModelCallback listener) {
        this.listener = listener;
    }

    @Override
    public void detachFirebaseListener() {

    }

    @Override
    public void removeFromDatabase(String itemUid, ModelCallback listener) {
        listener.onStart();
        firestore.collection("users").document("mentors").collection("mentor_profile").
                document(Objects.requireNonNull(mAuth.getUid())).update("academic" + itemUid, FieldValue.delete()).addOnSuccessListener(aVoid -> listener.onSuccess()).addOnFailureListener(e -> listener.onFail(e.toString()));
    }
}
