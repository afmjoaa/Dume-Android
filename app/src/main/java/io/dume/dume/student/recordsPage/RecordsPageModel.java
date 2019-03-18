package io.dume.dume.student.recordsPage;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.dume.dume.Google;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.util.DumeUtils;

public class RecordsPageModel implements RecordsPageContract.Model {

    private final Context context;
    private FirebaseFirestore firestore;
    private CollectionReference records;
    private final FirebaseAuth mAuth;
    private String key;

    public RecordsPageModel(Context context) {
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void recordsPageHawwa() {

    }

    @Override
    public void getRecords(TeacherContract.Model.Listener<List<Record>> listener) {

        Google google = Google.getInstance();
        String pre = null;
        key = "s_show_status";
        if (google.getAccountMajor() != null) {
            if (google.getAccountMajor().equals(DumeUtils.TEACHER)) {
                pre = "sp_";
                key = "t_show_status";
            } else if (google.getAccountMajor().equals(DumeUtils.STUDENT)) {
                pre = "sh_";
                key = "s_show_status";
            }
        } else return;

        firestore.collection("records").whereEqualTo(pre + "uid", google.getAccountPrefix() + FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener((Activity) context, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    listener.onError(e.getMessage());
                } else {
                    if (queryDocumentSnapshots != null) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        if (documents.size() > 0) {
                            //Google.getInstance().setRecords(documents);
                            List<Record> recordList = new ArrayList<>();
                            List<DocumentSnapshot> alteredDocuments = new ArrayList<>();
                            for (int i = 0; i < documents.size(); i++) {
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
                                Map<String, Object> data = documents.get(i).getData();
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
                                salaryInDemand = String.valueOf((Number) data.get("salary"));
                                sGender = (String) forMap.get("request_gender");
                                mGender = (String) spMap.get("gender");
                                subjectExchange = DumeUtils.getLast((Map<String, Object>) data.get("jizz"));
                                Date creation = (Date) data.get("creation");
                                date = creation.toString();
                                status = (String) data.get("record_status");
                                Record record = new Record(mentorName, studentName, salaryInDemand, subjectExchange, creation, mentorDpUrl, studentDpUrl, studentRating, mentorRating, status, Record.DELIVERED, sGender, mGender);
                                record.setRecordSnap(documents.get(i));
                                if(documents.get(i).getBoolean(key)){
                                    alteredDocuments.add(documents.get(i));
                                    recordList.add(record);
                                }
                            }
                            google.setRecords(alteredDocuments);
                            google.setRecordList(recordList);
                            listener.onSuccess(recordList);
                        } else listener.onError("No record found.");

                    } else listener.onError("No record found.");

                }


            }
        });
    }

    @Override
    public void changeRecordStatus(String recordId, String status, String rejectedBy, TeacherContract.Model.Listener<Void> listener) {
        if (!status.equals("Rejected")) {
            firestore.document("records/" + recordId).update("record_status", status).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
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
        } else {
            firestore.document("records/" + recordId).update("record_status", status, "rejected_by", rejectedBy).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
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

    @Override
    public void changeRecordValues(String recordId, String key, Boolean value, TeacherContract.Model.Listener<Void> listener) {
        firestore.document("records/" + recordId).update(key, value).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
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
    public void setPenalty(String acountMajor, Integer amount, TeacherContract.Model.Listener<Void> listener) {
        String path = "/users/students/stu_pro_info";
        Number previousAmount = 0;
        Integer currentAmount = amount;
        switch (acountMajor) {
            case DumeUtils.STUDENT:
                path = "/users/students/stu_pro_info";
                previousAmount = (Number) SearchDataStore.getInstance().getDocumentSnapshot().get("penalty");
                break;
            case DumeUtils.TEACHER:
                path = "/users/mentors/mentor_profile";
                previousAmount = (Number) TeacherDataStore.getInstance().getDocumentSnapshot().get("penalty");
                break;
            case DumeUtils.BOOTCAMP:
                path = "/users/bootcamps/camp_profile";
                break;
        }
        if (previousAmount != null) {
            currentAmount = previousAmount.intValue() + currentAmount;
        } else {
            currentAmount = 0 + currentAmount;
        }
        firestore.collection(path).document(mAuth.getCurrentUser().getUid()).update("penalty", currentAmount).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess(aVoid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onError("Network err !!");
                Log.e("Tag", e.getLocalizedMessage());
            }
        });
    }
}
