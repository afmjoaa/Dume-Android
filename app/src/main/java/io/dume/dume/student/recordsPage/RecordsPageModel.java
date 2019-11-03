package io.dume.dume.student.recordsPage;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private static final String TAG = "RecordsPageModel";

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
                                Date modi_creation = null;
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
                                modi_creation = (Date) data.get("status_modi_date");

                                date = creation.toString();
                                status = (String) data.get("record_status");
                                Record record = new Record(mentorName, studentName, salaryInDemand, subjectExchange, creation, mentorDpUrl, studentDpUrl, studentRating, mentorRating, status, Record.DELIVERED, sGender, mGender);
                                record.setRecordSnap(documents.get(i));
                                record.setModiDate(modi_creation);
                                if (documents.get(i).getBoolean(key)) {
                                    alteredDocuments.add(documents.get(i));
                                    recordList.add(record);
                                }
                            }
                            google.setRecords(alteredDocuments);
                            google.setRecordList(recordList);
                            listener.onSuccess(recordList);
                        } else {
                            google.setRecords(new ArrayList<>());
                            google.setRecordList(new ArrayList<>());
                            listener.onSuccess(new ArrayList<>());
                        }

                    } else listener.onError("No record found.");
                }
            }
        });
    }

    @Override
    public void changeRecordStatus(DocumentSnapshot record, String status, String rejectedBy, TeacherContract.Model.Listener<Void> listener) {
        String studentUid = record.getString("user_uid");
        String mentorUid = record.getString("mentor_uid");
        if (status.equals("Accepted")) {
            firestore.document("records/" + record.getId()).update("record_status", status, "status_modi_date", FieldValue.serverTimestamp()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    WriteBatch batch = firestore.batch();
                    if (Google.getInstance().getAccountMajor().equals(DumeUtils.TEACHER)) {
                        if (status.equals("Accepted")) {
                            Date requestTime = (Date) record.get("creation");

                            long currentResponseTime = new Date().getTime() - requestTime.getTime();
                            Map<String, Object> documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
                            Map<String, Object> selfRating = (Map<String, Object>) documentSnapshot.get("self_rating");
                            Map<String, Object> unreadRecords = (Map<String, Object>) documentSnapshot.get("unread_records");
                            String response = (String) selfRating.get("response_time");
                            String accepted = (String) unreadRecords.get("accepted_count");
                            String rejected = (String) unreadRecords.get("rejected_count");
                            String current = (String) unreadRecords.get("current_count");
                            String completed = (String) unreadRecords.get("completed_count");
                            String pending = (String) unreadRecords.get("pending_count");
                            int responseTime = Integer.parseInt(response == null ? "0" : response);

                            int pendingCount = Integer.parseInt(pending == null ? "0" : pending);
                            int acceptedCount = Integer.parseInt(accepted == null ? "0" : accepted);
                            int currentCount = Integer.parseInt(current == null ? "0" : current);
                            int completedCount = Integer.parseInt(completed == null ? "0" : completed);
                            int rejectedCount = Integer.parseInt(rejected == null ? "0" : rejected);
                            acceptedCount = acceptedCount + 1;
                            pendingCount = pendingCount - 1;
                            if (pendingCount < 0) {
                                pendingCount = 0;
                            }
                            int foo = (int) (currentResponseTime / (1000 * 60 * 60));
                            int finalResponseTime = (responseTime + foo) / (acceptedCount + rejectedCount + completedCount + currentCount);
                            DocumentReference document = firestore.document("users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getUid());
                            batch.update(document, "self_rating.response_time", String.valueOf(finalResponseTime), "unread_records.pending_count", pendingCount + "", "unread_records.accepted_count", acceptedCount + "");

                            //testing here
                            DocumentReference studentDocRef = firestore.collection("/users/students/stu_pro_info").document(studentUid);//todo
                            studentDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot studentDocument = task.getResult();
                                        assert studentDocument != null;
                                        if (studentDocument.exists()) {
                                            Map<String, Object> stuUnreadRecords = (Map<String, Object>) studentDocument.get("unread_records");
                                            String accepted = (String) stuUnreadRecords.get("accepted_count");
                                            String rejected = (String) stuUnreadRecords.get("rejected_count");
                                            String pending = (String) stuUnreadRecords.get("pending_count");
                                            int stuPendingCount = Integer.parseInt(pending == null ? "0" : pending);
                                            int stuAcceptedCount = Integer.parseInt(accepted == null ? "0" : accepted);
                                            int stuRejectedCount = Integer.parseInt(rejected == null ? "0" : rejected);
                                            stuAcceptedCount = stuAcceptedCount + 1;
                                            stuPendingCount = stuPendingCount - 1;
                                            if (stuPendingCount < 0) {
                                                stuPendingCount = 0;
                                            }
                                            batch.update(studentDocRef, "unread_records.pending_count", stuPendingCount + "", "unread_records.accepted_count", stuAcceptedCount + "");
                                            batch.commit();
                                            listener.onSuccess(aVoid);
                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        listener.onError("Network err !!");
                                    }
                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));
        } else {//this is for the rejected by teacher one
            firestore.document("records/" + record.getId()).update("record_status", status, "rejected_by", rejectedBy, "status_modi_date", FieldValue.serverTimestamp()).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {

                private int responseTime;

                @Override
                public void onSuccess(Void aVoid) {
                    WriteBatch batch = firestore.batch();
                    Date requestTime = (Date) record.get("creation");
                    long currentResponseTime = new Date().getTime() - requestTime.getTime();
                    Map<String, Object> documentSnapshot = new HashMap<>();
                    if (rejectedBy.equals(DumeUtils.TEACHER)) {
                        documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
                        Map<String, Object> selfRating = (Map<String, Object>) documentSnapshot.get("self_rating");
                        String response = (String) selfRating.get("response_time");
                        responseTime = Integer.parseInt(response == null ? "0" : response);
                    } else {
                        documentSnapshot = SearchDataStore.getInstance().getDocumentSnapshot();
                    }
                    Map<String, Object> unreadRecords = (Map<String, Object>) documentSnapshot.get("unread_records");
                    String accepted = (String) unreadRecords.get("accepted_count");
                    String rejected = (String) unreadRecords.get("rejected_count");
                    String current = (String) unreadRecords.get("current_count");
                    String completed = (String) unreadRecords.get("completed_count");
                    String pending = (String) unreadRecords.get("pending_count");
                    int pendingCount = Integer.parseInt(pending == null ? "0" : pending);
                    int acceptedCount = Integer.parseInt(accepted == null ? "0" : accepted);
                    int currentCount = Integer.parseInt(current == null ? "0" : current);
                    int completedCount = Integer.parseInt(completed == null ? "0" : completed);
                    int rejectedCount = Integer.parseInt(rejected == null ? "0" : rejected);
                    rejectedCount = rejectedCount + 1;
                    pendingCount = pendingCount - 1;
                    if (pendingCount < 0) {
                        pendingCount = 0;
                    }


                    if (rejectedBy.equals(DumeUtils.TEACHER)) {
                        int foo = (int) (currentResponseTime / (1000 * 60 * 60));
                        int finalResponseTime = (responseTime + foo) / (acceptedCount + rejectedCount + completedCount + currentCount);
                        DocumentReference document = firestore.document("users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getUid());
                        batch.update(document, "self_rating.response_time", String.valueOf(finalResponseTime), "unread_records.pending_count", pendingCount + "", "unread_records.rejected_count", rejectedCount + "");
                        //testing here
                        DocumentReference studentDocRef = firestore.collection("/users/students/stu_pro_info").document(studentUid);//todo
                        studentDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot studentDocument = task.getResult();
                                    assert studentDocument != null;
                                    if (studentDocument.exists()) {
                                        Map<String, Object> stuUnreadRecords = (Map<String, Object>) studentDocument.get("unread_records");
                                        String accepted = (String) stuUnreadRecords.get("accepted_count");
                                        String rejected = (String) stuUnreadRecords.get("rejected_count");
                                        String pending = (String) stuUnreadRecords.get("pending_count");
                                        int stuPendingCount = Integer.parseInt(pending == null ? "0" : pending);
                                        int stuAcceptedCount = Integer.parseInt(accepted == null ? "0" : accepted);
                                        int stuRejectedCount = Integer.parseInt(rejected == null ? "0" : rejected);
                                        stuRejectedCount = stuRejectedCount + 1;
                                        stuPendingCount = stuPendingCount - 1;
                                        if (stuPendingCount < 0) {
                                            stuPendingCount = 0;
                                        }
                                        batch.update(studentDocRef, "unread_records.pending_count", stuPendingCount + "", "unread_records.rejected_count", stuRejectedCount + "");
                                        batch.commit();
                                        listener.onSuccess(aVoid);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    listener.onError("Network err !!");
                                }
                            }
                        });
                    } else {//rejected by student...
                        DocumentReference studentDocument = firestore.document("/users/students/stu_pro_info/" + FirebaseAuth.getInstance().getUid());
                        batch.update(studentDocument, "unread_records.pending_count", pendingCount + "", "unread_records.rejected_count", rejectedCount + "");
                        //testing here
                        DocumentReference mentorDocRef = firestore.collection("users/mentors/mentor_profile/").document(mentorUid);//todo
                        mentorDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot studentDocument = task.getResult();
                                    assert studentDocument != null;
                                    if (studentDocument.exists()) {
                                        Map<String, Object> stuUnreadRecords = (Map<String, Object>) studentDocument.get("unread_records");
                                        String accepted = (String) stuUnreadRecords.get("accepted_count");
                                        String rejected = (String) stuUnreadRecords.get("rejected_count");
                                        String pending = (String) stuUnreadRecords.get("pending_count");
                                        int menPendingCount = Integer.parseInt(pending == null ? "0" : pending);
                                        int menAcceptedCount = Integer.parseInt(accepted == null ? "0" : accepted);
                                        int menRejectedCount = Integer.parseInt(rejected == null ? "0" : rejected);
                                        menRejectedCount = menRejectedCount + 1;
                                        menPendingCount = menPendingCount - 1;
                                        if (menPendingCount < 0) {
                                            menPendingCount = 0;
                                        }
                                        batch.update(mentorDocRef, "unread_records.pending_count", menPendingCount + "", "unread_records.rejected_count", menRejectedCount + "");
                                        batch.commit();
                                        listener.onSuccess(aVoid);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    listener.onError("Network err !!");
                                }
                            }
                        });
                    }
                    //listener.onSuccess(aVoid);
                }
            }).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));
        }
    }

    @Override
    public void changeRecordValues(String recordId, String key, boolean value, TeacherContract.Model.Listener<Void> listener) {
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
    public void setPenalty(String acountMajor, Integer amount, boolean ratingPenalty, String ratingVal, TeacherContract.Model.Listener<Void> listener) {
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
        if (ratingPenalty) {
            firestore.collection(path).document(mAuth.getCurrentUser().getUid()).update("penalty", currentAmount, "self_rating.star_rating", ratingVal).addOnSuccessListener((Activity) context, new OnSuccessListener<Void>() {
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
        } else {
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
}
