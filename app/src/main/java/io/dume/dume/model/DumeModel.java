package io.dume.dume.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Skill;

public class DumeModel implements TeacherModel {

    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth instance;

    public DumeModel() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        instance = FirebaseAuth.getInstance();
    }


    @Override
    public void saveSkill(Skill skill, TeacherContract.Model.Listener<Void> listener) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", skill.isStatus());
        dataMap.put("salary", skill.getSalary());
        dataMap.put("creation", skill.getCreationDate());
        dataMap.put("jizz", skill.getMap());
        dataMap.put("rating",skill.getRatingValue());
        dataMap.put("totalRating",skill.getTotalRating());
        dataMap.put("query_string", skill.getQueryString());
        CollectionReference skillCollection = firebaseFirestore.collection("users").document("mentors").collection("skills");

        skillCollection.document().set(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}
