package io.dume.dume.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.WriteBatch;

import io.dume.dume.library.myGeoFIreStore.GeoFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.dume.dume.util.Google;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.homePage.HomePageModel;
import io.dume.dume.student.studentPayment.adapterAndData.PaymentHistory;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.pojo.Skill;

import static android.content.Context.MODE_PRIVATE;

public class DumeModel extends HomePageModel implements TeacherModel {

    private static final String TAG = "DumeModel";
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private final Context context;
    private CollectionReference skillCollection;
    private final GeoFirestore geoFirestore;
    private WriteBatch batch;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private static final String MY_PREFS_NAME = "welcome";
    private Boolean modifiedStatus;

    public DumeModel(Context context) {
        super((Activity) context, context);
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        skillCollection = firebaseFirestore.collection("users").document("mentors").collection("skills");
        geoFirestore = new GeoFirestore(skillCollection);
        batch = firebaseFirestore.batch();
        editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);



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
        dataMap.put("common_query_str", skill.getCommonQueryString());


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
                //listener.onSuccess(aVoid);
                //TODO
                //get all skills
                //Change all skills status
                ArrayList<Skill> skillArrayList = TeacherDataStore.getInstance().getSkillArrayList();
                if (skillArrayList != null) {
                    if (skillArrayList.size() <= 0) {
                        TeacherActivtiy activtiy = (TeacherActivtiy) context;
                        activtiy.hideProgress();
                        activtiy.hideProgressTwo();
                    }
                    changeAllSkillStatus(skillArrayList, status, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            listener.onSuccess(aVoid);
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(context, "Network err !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    getSkill(new TeacherContract.Model.Listener<ArrayList<Skill>>() {
                        @Override
                        public void onSuccess(ArrayList<Skill> list) {
                            if (list != null && list.size() <= 0) {
                                TeacherActivtiy activtiy = (TeacherActivtiy) context;
                                activtiy.hideProgress();
                                activtiy.hideProgressTwo();
                            }
                            Log.e(TAG, "onSuccess: " + list.size());
                            TeacherDataStore.getInstance().setSkillArrayList(list);
                            changeAllSkillStatus(list, status, new TeacherContract.Model.Listener<Void>() {
                                @Override
                                public void onSuccess(Void list) {
                                    listener.onSuccess(aVoid);
                                }

                                @Override
                                public void onError(String msg) {
                                    Toast.makeText(context, "Network err !!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getLocalizedMessage());
            }
        });
    }

    @Override
    public void changeAllSkillStatus(ArrayList<Skill> skillArrayList, boolean status, TeacherContract.Model.Listener<Void> listener) {
        WriteBatch batch = firebaseFirestore.batch();

        for (int i = 0; i < skillArrayList.size(); i++) {
            DocumentReference skillRef = firebaseFirestore.collection("users/mentors/skills/").document(skillArrayList.get(i).getId());
            if (status) {
                boolean retrivedStatus = prefs.getBoolean(skillArrayList.get(i).getId(), true);
                batch.update(skillRef, "status", retrivedStatus);
            } else {//status == false
                editor.putBoolean(skillArrayList.get(i).getId(), skillArrayList.get(i).isStatus());
                editor.apply();
                batch.update(skillRef, "status", false);
            }
            if (i == (skillArrayList.size() - 1)) {
                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onSuccess(null);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError(e.getLocalizedMessage());
                    }
                });
            }
        }
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
    public void updateSkill(String id, Map<String, Object> updateData,  TeacherContract.Model.Listener<Void> listener) {
        firebaseFirestore.document("users/mentors/skills/" + id).update(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        Query query = firebaseFirestore.document("users/mentors/skills/" + id).collection("reviews").orderBy("time", Query.Direction.DESCENDING).limit(120);

        if (oldid == null) {
            //do nothing
        } else query = query.startAfter(oldid);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
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
                } else {
                    listener.onError("Network Err !!");
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        /*query.addSnapshotListener((queryDocumentSnapshots, e) -> {
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
        });*/
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


    public void modifySeenStatusNotification(String doc_id, TeacherContract.Model.Listener<Boolean> listener) {
        fireStore.collection("push_notifications").document(doc_id).update("seen", true).addOnSuccessListener((Activity) context, aVoid -> listener.onSuccess(true)).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));
    }

    public void deleteNotification(String doc_id, TeacherContract.Model.Listener<Boolean> listener) {
        fireStore.collection("push_notifications").document(doc_id).delete().addOnSuccessListener((Activity) context, aVoid -> listener.onSuccess(true)).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));
    }

    public void reportIssue(String usermail, String issue, TeacherContract.Model.Listener<Void> listener) {

        HashMap<String, Object> data = new HashMap<>();
        data.put("body", issue);
        data.put("email", usermail);
        fireStore.collection("contact").add(data).addOnSuccessListener((Activity) context, aVoid -> listener.onSuccess(null)).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));


    }

    public void getPaymentHistory(String uid, TeacherContract.Model.Listener<List<PaymentHistory>> listener) {
        firebaseFirestore.collection("payments").whereEqualTo("uid", uid).get(Source.DEFAULT).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<PaymentHistory> histories = new ArrayList<>();
                if (task.getException() != null) {
                    listener.onError(task.getException().getMessage());
                    return;
                }
                if (task.isComplete()) {
                    if (task.getResult() != null) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for (int i = 0; i < documents.size(); i++) {
                            PaymentHistory paymentHistory = documents.get(i).toObject(PaymentHistory.class);
                            histories.add(paymentHistory);
                        }
                        listener.onSuccess(histories);
                    }


                } else {
                    listener.onError(task.getException().getMessage());
                }

            }
        });
    }


}
