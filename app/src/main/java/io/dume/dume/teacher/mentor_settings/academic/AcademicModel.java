package io.dume.dume.teacher.mentor_settings.academic;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
    public void syncWithDatabase(@NonNull String level, String institution, String degree, String from, String to, String result) {
        if (listener != null) {
            listener.onStart();
            hashMap = new HashMap<>();
            hashMap.put("level", level);
            hashMap.put("institution", institution);
            hashMap.put("degree", degree);
            hashMap.put("from_year", from);
            hashMap.put("to_year", to);
            hashMap.put("result", result);
            if (FirebaseAuth.getInstance().getUid() != null) {
                String path = "academic." + degree.replace(".", "");
                firestore.collection("/users/mentors/mentor_profile").document(FirebaseAuth.getInstance().getUid()).update(path, hashMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    }
                }).addOnFailureListener(e -> listener.onFail(e.toString()));
            }
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
                document(Objects.requireNonNull(mAuth.getUid())).update("academic." + itemUid.replace(".", ""), FieldValue.delete()).addOnSuccessListener(aVoid -> listener.onSuccess()).addOnFailureListener(e -> listener.onFail(e.toString()));
    }
}
