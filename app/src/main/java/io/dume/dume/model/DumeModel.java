package io.dume.dume.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Skill;

public class DumeModel implements TeacherModel {

    private static final String TAG = "DumeModel";
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;

    public DumeModel() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void saveSkill(Skill skill, TeacherContract.Model.Listener<Void> listener) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", skill.isStatus());
        dataMap.put("salary", skill.getSalary());
        dataMap.put("creation", skill.getCreation());
        dataMap.put("jizz", skill.getJizz());
        dataMap.put("rating", skill.getRating());
        dataMap.put("totalRating", skill.getTotalRating());
        dataMap.put("query_string", skill.getQuery_string());
        dataMap.put("mentor_uid", firebaseAuth.getCurrentUser().getUid());
        dataMap.put("enrolled", skill.getEnrolled());
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put(firebaseAuth.getCurrentUser().getUid(), "Baler Teacher");
        stringObjectHashMap.put(firebaseAuth.getCurrentUser().getUid() + "K", "Wow Vala Sir");
        dataMap.put("feedback", stringObjectHashMap);
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

    @Override
    public void getSkill(TeacherContract.Model.Listener<ArrayList<Skill>> listener) {
        CollectionReference skillCollection = firebaseFirestore.collection("users").document("mentors").collection("skills");
        skillCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            private ArrayList<Skill> skillList = new ArrayList<>();

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Skill skill = document.toObject(Skill.class);
                    skillList.add(skill);
                    Log.w(TAG, "onEvent: " + document.toString());
                }
                listener.onSuccess(skillList);
                if (e != null) {
                    listener.onError(e.getLocalizedMessage());
                }
            }
        });
    }


}
