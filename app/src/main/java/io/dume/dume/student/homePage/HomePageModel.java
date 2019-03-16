package io.dume.dume.student.homePage;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.dume.dume.Google;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.grabingLocation.MenualRecyclerData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.util.DumeUtils;

public class HomePageModel extends StuBaseModel implements HomePageContract.Model {
    private Context context;
    private Activity activity;
    private static final String TAG = "HomePageModel";
    private ListenerRegistration registration;
    private String name;

    public HomePageModel(Activity activity, Context context) {
        super(context);
        this.context = context;
        this.activity = activity;
    }


    @Override
    public void applyPromo(HomePageRecyclerData promoData, String promo_code, String accountType, TeacherContract.Model.Listener<String> listener) {
        String path;
        if (accountType == DumeUtils.TEACHER) {
            path = "/users/mentors/mentor_profile";
        } else {
            path = "/users/students/stu_pro_info";

        }


        Map<String, Object> promoMap = new HashMap<>();

        promoMap.put("title", promoData.getTitle());
        promoMap.put("description", promoData.getDescription());
        promoMap.put("sub_description", promoData.getSub_description());
        promoMap.put("expirity", promoData.getExpirity());
        promoMap.put("start_date", promoData.getStart_date());
        promoMap.put("max_dicount_percentage", promoData.getMax_dicount_percentage());
        promoMap.put("max_discount_credit", promoData.getMax_discount_credit());
        promoMap.put("max_tution_count", promoData.getMax_tution_count());
        promoMap.put("promo_image", promoData.getPromo_image());
        promoMap.put("packageName", promoData.getPackageName());
        promoMap.put("promo_code", promoData.getPromo_code());
        promoMap.put("promo_for", promoData.getPromo_for());
        promoMap.put("expired", promoData.isExpired());
        promoMap.put("criteria", promoData.getCriteria());


        firestore.collection(path).document(FirebaseAuth.getInstance().getUid())
                .update("applied_promo", FieldValue.arrayUnion(promo_code),
                        "available_promo", FieldValue.arrayRemove(promo_code),
                        promo_code, promoMap).addOnSuccessListener(aVoid -> listener.onSuccess("Promo Applied.")).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));

    }

    @Override
    public void updatePromo(HomePageRecyclerData promoData, TeacherContract.Model.Listener<String> listener) {
        String accountType = Google.getInstance().getAccountMajor();
        String path;
        if (accountType == DumeUtils.TEACHER) {
            path = "/users/mentors/mentor_profile";
        } else {
            path = "/users/students/stu_pro_info";

        }

        if (promoData.getMax_tution_count() > 1) {
            Integer max_tution_count = promoData.getMax_tution_count();
            max_tution_count -= 1;
            firestore.collection(path).document(FirebaseAuth.getInstance().getUid()).update(promoData.getPromo_code() + ".max_tution_count", max_tution_count).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listener.onSuccess("Promo Updated");
                }
            });
        } else {
            firestore.collection(path).document(FirebaseAuth.getInstance().getUid())
                    .update("applied_promo", FieldValue.arrayRemove(promoData.getPromo_code()),
                            promoData.getPromo_code(), FieldValue.delete()).addOnSuccessListener(aVoid -> listener.onSuccess("Promo Used")).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));

        }
    }


    @Override
    public void hawwa() {

    }

    @Override
    public void getPromo(String promoCode, TeacherContract.Model.Listener<HomePageRecyclerData> listener) {
        firestore.collection("promo").whereEqualTo("promo_code", promoCode).addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots == null) {
                    listener.onError("Promo Not Exists");
                } else {
                    List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                    if (documents.size() > 0) {
                        HomePageRecyclerData homePageRecyclerData = documents.get(0).toObject(HomePageRecyclerData.class);
                        if (!homePageRecyclerData.isExpired()) {
                            listener.onSuccess(homePageRecyclerData);
                        }
                    } else listener.onError("Promo Not Exists");

                }

            }
        });
    }


    @Override
    public void submitRating(String record_id, String skill_id, String opponent_uid, String myAccountType, Map<String, Boolean> inputRating, Float inputStar, String feedbackString, TeacherContract.Model.Listener<Void> listener) {

        String keyToChange;
        String path;
        name = "";

        if (myAccountType.equals(DumeUtils.TEACHER)) {
            keyToChange = "t_rate_status";
            path = "/users/students/stu_pro_info";
            name = TeacherDataStore.getInstance().gettUserName();

        } else {
            keyToChange = "s_rate_status";
            path = "/users/mentors/mentor_profile";
            name = SearchDataStore.getInstance().getUserName();

        }
        changeRecordStatus(record_id, keyToChange, Record.DONE);

        firestore.collection(path).document(opponent_uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot == null) {
                    return;
                }
                if (myAccountType.equals(DumeUtils.TEACHER) || myAccountType.equals(DumeUtils.BOOTCAMP)) {
                    Map<String, Object> self_rating = (Map<String, Object>) documentSnapshot.get("self_rating");
                    float star_rating = Float.parseFloat((String) self_rating.get("star_rating"));
                    Integer star_count = Integer.parseInt((String) self_rating.get("star_count"));
                    Integer dl_behaviour = Integer.parseInt((String) self_rating.get("dl_behaviour"));
                    Integer l_behaviour = Integer.parseInt((String) self_rating.get("l_behaviour"));
                    Integer dl_communication = Integer.parseInt((String) self_rating.get("dl_communication"));
                    Integer l_communication = Integer.parseInt((String) self_rating.get("l_communication"));

                    star_rating = (star_rating * star_count) + inputStar / (++star_count);

                    for (Map.Entry<String, Boolean> entry : inputRating.entrySet()) {
                        switch (entry.getKey()) {
                            case "Communication":
                                if (entry.getValue()) {
                                    l_communication = l_communication + 1;
                                } else {
                                    dl_communication = dl_communication + 1;
                                }
                                break;
                            case "Behaviour":
                                if (entry.getValue()) {
                                    l_behaviour = l_behaviour + 1;
                                } else {
                                    dl_behaviour = dl_behaviour + 1;
                                }
                                break;
                        }
                    }

                    Map<String, Object> selfRating = new HashMap<>();
                    selfRating.put("star_rating", star_rating);
                    selfRating.put("star_count", star_count);
                    selfRating.put("l_communication", l_communication);
                    selfRating.put("dl_communication", dl_communication);
                    selfRating.put("l_behaviour", l_behaviour);
                    selfRating.put("dl_behaviour", dl_behaviour);
                    documentSnapshot.getReference().update("self_rating", self_rating);
                    listener.onSuccess(null);
                } else {//this is for my account type student
                    Map<String, Object> self_rating = (Map<String, Object>) documentSnapshot.get("self_rating");
                    float star_rating = Float.parseFloat((String) self_rating.get("star_rating"));
                    Integer star_count = Integer.parseInt((String) self_rating.get("star_count"));
                    Integer dl_behaviour = Integer.parseInt((String) self_rating.get("dl_behaviour"));
                    Integer l_behaviour = Integer.parseInt((String) self_rating.get("l_behaviour"));
                    Integer dl_communication = Integer.parseInt((String) self_rating.get("dl_communication"));
                    Integer l_communication = Integer.parseInt((String) self_rating.get("l_communication"));

                    Integer dl_experience = Integer.parseInt((String) self_rating.get("dl_experience"));
                    Integer l_experience = Integer.parseInt((String) self_rating.get("l_experience"));
                    Integer dl_expertise = Integer.parseInt((String) self_rating.get("dl_expertise"));
                    Integer l_expertise = Integer.parseInt((String) self_rating.get("l_expertise"));
                    Integer student_guided = Integer.parseInt((String) self_rating.get("student_guided"));
                    student_guided++;

                    star_rating = (star_rating * star_count) + inputStar / (++star_count);
                    for (Map.Entry<String, Boolean> entry : inputRating.entrySet()) {
                        switch (entry.getKey()) {
                            case "Expertise":
                                if (entry.getValue()) {
                                    l_expertise = l_expertise + 1;
                                } else {
                                    dl_expertise = dl_expertise + 1;

                                }
                                break;
                            case "Experience":
                                if (entry.getValue()) {
                                    l_experience = l_experience + 1;
                                } else {
                                    dl_experience = dl_experience + 1;

                                }

                                break;
                            case "Communication":
                                if (entry.getValue()) {
                                    l_communication = l_communication + 1;
                                } else {
                                    dl_communication = dl_communication + 1;

                                }

                                break;
                            case "Behaviour":
                                if (entry.getValue()) {
                                    l_behaviour = l_behaviour + 1;
                                } else {
                                    dl_behaviour = dl_behaviour + 1;

                                }
                                break;
                        }
                    }

                    Map<String, Object> selfRating = new HashMap<>();
                    selfRating.put("star_rating", star_rating);
                    selfRating.put("star_count", star_count);
                    selfRating.put("l_communication", l_communication);
                    selfRating.put("dl_communication", dl_communication);
                    selfRating.put("l_behaviour", l_behaviour);
                    selfRating.put("dl_behaviour", dl_behaviour);
                    selfRating.put("l_expertise", l_expertise);
                    selfRating.put("dl_expertise", dl_expertise);
                    selfRating.put("l_experience", l_experience);
                    selfRating.put("dl_experience", dl_experience);
                    selfRating.put("student_guided", student_guided);
                    documentSnapshot.getReference().update("self_rating", self_rating);

                    registration = firestore.collection("/users/mentors/skills/").document(skill_id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (myAccountType == DumeUtils.STUDENT) {
                                Map<String, Number> dblikes = (Map<String, Number>) documentSnapshot.get("likes");
                                Map<String, Number> likes = dblikes;
                                Map<String, Number> dbdislikes = (Map<String, Number>) documentSnapshot.get("dislikes");
                                Map<String, Number> dislikes = dbdislikes;
                                for (Map.Entry<String, Number> entry : dblikes.entrySet()) {
                                    Boolean aBoolean = inputRating.get(entry.getKey());
                                    if (aBoolean) {
                                        Number value = entry.getValue();
                                        value = 1 + value.longValue();
                                        likes.put(entry.getKey(), value);
                                    }
                                }
                                for (Map.Entry<String, Number> entry : dbdislikes.entrySet()) {
                                    final Boolean aBoolean = inputRating.get(entry.getKey());
                                    if (!aBoolean) {
                                        Number value = entry.getValue();
                                        value = 1 + value.longValue();
                                        dislikes.put(entry.getKey(), value);
                                    }
                                }
                                listener.onSuccess(null);

                                registration.remove();
                                firestore.collection("/users/mentors/skills/").document(skill_id).update("sp_info.self_rating", self_rating, "likes", likes, "dislikes", dislikes);
                            }
                        }
                    });

                    //setting the skill feedback here
                    Map<String, Object> reviewMap = new HashMap<>();
                    reviewMap.put("body", feedbackString);
                    //TODO
                    reviewMap.put("dislikes", 0);
                    reviewMap.put("likes", 0);
                    reviewMap.put("name", name);
                    reviewMap.put("r_avatar", "avatar");
                    reviewMap.put("reviewer_rating", inputStar.toString());
                    reviewMap.put("time", FieldValue.serverTimestamp());
                    firestore.collection("/users/mentors/skills/").document(skill_id).collection("reviews").add(reviewMap);
                }
            }
        });


    }

    @Override
    public void getSingleRecords(String recordId, TeacherContract.Model.Listener<Record> listener) {
        firestore.collection("records").document(recordId).addSnapshotListener((Activity) context, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    listener.onError(e.getMessage());
                } else {
                    if (documentSnapshot != null) {
                        String mentorName;
                        String studentName;
                        String salaryInDemand;
                        String subjectExchange;
                        String date;
                        String mentorDpUrl;
                        String studentDpUrl, sGender, mGender;
                        float studentRating;
                        float mentorRating;
                        String status;
                        int deliveryStatus;
                        Map<String, Object> data = documentSnapshot.getData();
                        Map<String, Object> spMap = (Map<String, Object>) data.get("sp_info");
                        mentorName = spMap.get("first_name") + " " + spMap.get("last_name");
                        mentorDpUrl = (String) spMap.get("avatar");
                        Map<String, Object> bal = (Map<String, Object>) spMap.get("self_rating");
                        mentorRating = Float.parseFloat((String) bal.get("star_rating"));
                        Map<String, Object> forMap = (Map<String, Object>) data.get("for_whom");
                        Map<String, Object> shMap = (Map<String, Object>) forMap.get("request_sr");
                        studentRating = Float.parseFloat((String) shMap.get("star_rating"));
                        studentName = (String) forMap.get("stu_name");
                        studentDpUrl = (String) forMap.get("request_avatar");
                        salaryInDemand = String.valueOf((Double) data.get("salary"));
                        sGender = (String) forMap.get("request_gender");
                        mGender = (String) spMap.get("gender");
                        subjectExchange = DumeUtils.getLast((Map<String, Object>) data.get("jizz"));
                        Date creation = (Date) data.get("creation");
                        date = creation.toString();
                        status = (String) data.get("record_status");
                        Record record = new Record(mentorName, studentName, salaryInDemand, subjectExchange, creation, mentorDpUrl, studentDpUrl, studentRating, mentorRating, status, Record.DELIVERED, sGender, mGender);
                        record.setT_rate_status(documentSnapshot.getString("t_rate_status"));
                        record.setS_rate_status(documentSnapshot.getString("s_rate_status"));
                        record.setRecordSnap(documentSnapshot);
                        listener.onSuccess(record);

                    } else listener.onError("No record found.");
                }
            }
        });
    }

    @Override
    public void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener) {
        userStudentProInfo.addSnapshotListener(activity, updateViewListener);
    }

    @Override
    public void removeCompletedRating(String identify, TeacherContract.Model.Listener<Void> listener) {
        String accountMajor = Google.getInstance().getAccountMajor();
        String path;
        if (accountMajor.equals(DumeUtils.TEACHER)) {
            path = "/users/mentors/mentor_profile";
        } else {
            path = "/users/students/stu_pro_info";
        }

        firestore.collection(path).document(getUser().getUid()).update("rating_array", FieldValue.arrayRemove(identify)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.w(TAG, "onSuccess: " + "Ok");
                listener.onSuccess(aVoid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError(e.getLocalizedMessage());
            }
        });
    }

    public void changeRecordStatus(String record_id, String keyToChange, String status) {
        firestore.collection("records").document(record_id).update(keyToChange, status);
    }

}
