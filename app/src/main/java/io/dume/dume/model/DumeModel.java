package io.dume.dume.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
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

import io.dume.dume.Google;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.util.DumeUtils;

public class DumeModel implements TeacherModel {

    private static final String TAG = "DumeModel";
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private final Context context;
    private CollectionReference skillCollection;
    private final GeoFirestore geoFirestore;

    public DumeModel(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        skillCollection = firebaseFirestore.collection("users").document("mentors").collection("skills");
        geoFirestore = new GeoFirestore(skillCollection);
    }

    public void switchAcount(String to, TeacherContract.Model.Listener<Void> listener) {
        firebaseFirestore.document("mini_users/" + firebaseAuth.getUid()).update("account_major", to).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Google.getInstance().setAccountMajor(to);
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
        dataMap.put("creation", FieldValue.serverTimestamp());
        dataMap.put("jizz", skill.getJizz());
        dataMap.put("rating", skill.getRating());
        dataMap.put("totalRating", skill.getTotalRating());
        GeoPoint location = (GeoPoint) TeacherDataStore.getInstance().getDocumentSnapshot().get("location");
        dataMap.put("location", location);
        dataMap.put("query_string", skill.getQuery_string());
        dataMap.put("mentor_uid", firebaseAuth.getCurrentUser().getUid());
        dataMap.put("enrolled", skill.getEnrolled());
        dataMap.put("sp_info", TeacherDataStore.getInstance().getDocumentSnapshot());
        dataMap.put("likes", skill.getLikes());
        dataMap.put("dislikes", skill.getDislikes());
        dataMap.put("package_name", skill.getPackage_name());
        dataMap.put("query_list", skill.getQuery_list());
        dataMap.put("query_list_name", skill.getQuery_list_name());


        final DocumentReference document = skillCollection.document();
        String id = document.getId();

        document.set(dataMap).addOnSuccessListener((Activity) context, aVoid -> geoFirestore.setLocation(id, location, e -> {
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
        query.addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                ArrayList<Skill> skillList = new ArrayList<>();
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : documents) {
                    Skill skill = document.toObject(Skill.class);
                    if (skill != null) {
                        skill.setId(document.getId());
                    }
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

    @Override
    public void switchAccountStatus(boolean status, TeacherContract.Model.Listener<Void> listener) {
        firebaseFirestore.document("users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getUid()).update("account_active", status).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
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
    public void deleteSkill(String id, TeacherContract.Model.Listener<Void> listener) {
        firebaseFirestore.document("users/mentors/skills/" + id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void loadReview(String id, String oldid, TeacherContract.Model.Listener<List<ReviewHighlightData>> listener) {
        Query query = firebaseFirestore.document("users/mentors/skills/" + id).collection("reviews").orderBy("time", Query.Direction.DESCENDING);

        if (oldid == null) {
            //do nothing
        } else query = query.startAfter(oldid);


        query.addSnapshotListener((Activity) context, (queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                List<ReviewHighlightData> list = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++) {
                    ReviewHighlightData reviewHighlightData = documents.get(i).toObject(ReviewHighlightData.class);
                    reviewHighlightData.setDoc_id(documents.get(i).getId());
                    list.add(reviewHighlightData);
                }
                if (list.size() > 0) {
                    listener.onSuccess(list);
                } else listener.onError("No review");


            } else {
                listener.onError("Empty Response");
            }

        });
    }

    @Override
    public void swithSkillStatus(String id, boolean status, TeacherContract.Model.Listener<Void> listener) {
        firebaseFirestore.document("users/mentors/skills/" + id).update("status", status).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
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
