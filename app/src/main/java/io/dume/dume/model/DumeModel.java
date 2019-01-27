package io.dume.dume.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.pojo.Skill;

public class DumeModel implements TeacherModel {

    private static final String TAG = "DumeModel";
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private CollectionReference skillCollection;
    private final GeoFirestore geoFirestore;

    public DumeModel() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        skillCollection = firebaseFirestore.collection("users").document("mentors").collection("skills");
        geoFirestore = new GeoFirestore(skillCollection);
    }

    public void switchAcount(String to, TeacherContract.Model.Listener<Void> listener) {
        firebaseFirestore.document("mini_users/" + firebaseAuth.getUid()).update("account_major", to).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void saveSkill(Skill skill, TeacherContract.Model.Listener<Void> listener) {
        HashMap<String, Object> dataMap = new HashMap<>();
        dataMap.put("status", skill.isStatus());
        dataMap.put("salary", skill.getSalary());
        dataMap.put("creation", skill.getCreation());
        dataMap.put("jizz", skill.getJizz());
        dataMap.put("rating", skill.getRating());
        dataMap.put("totalRating", skill.getTotalRating());
        GeoPoint location = (GeoPoint) TeacherDataStore.getInstance().getDocumentSnapshot().get("location");
        dataMap.put("location", location);
        dataMap.put("query_string", skill.getQuery_string());
        dataMap.put("mentor_uid", firebaseAuth.getCurrentUser().getUid());
        dataMap.put("enrolled", skill.getEnrolled());
        dataMap.put("sp_info", TeacherDataStore.getInstance().getDocumentSnapshot());


        final DocumentReference document = skillCollection.document();
        String id = document.getId();

        document.set(dataMap).addOnSuccessListener(aVoid -> geoFirestore.setLocation(id, location, e -> {
            if (e == null) {
                listener.onSuccess(aVoid);
            }
        })).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void getSkill(TeacherContract.Model.Listener<ArrayList<Skill>> listener) {
        CollectionReference skillCollection = firebaseFirestore.collection("users").document("mentors").collection("skills");
        Query query = skillCollection.whereEqualTo("mentor_uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Skill> skillList = new ArrayList<>();
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
