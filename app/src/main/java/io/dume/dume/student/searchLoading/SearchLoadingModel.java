package io.dume.dume.student.searchLoading;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryDataEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.student.recordsPage.RecordsPageActivity;
import io.dume.dume.student.recordsPage.RecordsPageModel;
import io.dume.dume.student.recordsPending.RecordsPendingActivity;
import io.dume.dume.student.searchResult.SearchResultActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

public class SearchLoadingModel extends StuBaseModel implements SearchLoadingContract.Model {
    ArrayList<DocumentSnapshot> instructorList = null;
    private List<DocumentSnapshot> recordDataRejected;
    private List<String> rejectedRecordIds;
    private Number salary;
    private String rejectedBy = null;

    SearchLoadingModel(Context context) {
        super(context);
        instructorList = new ArrayList<>();
    }

    public boolean isMatch(ArrayList<String> subject, String queryStringFromDb) {
        for (int i = 0; i < subject.size(); i++) {
            if (!queryStringFromDb.contains(subject.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void search(double lat, double lon, double radius, String queryString, TeacherContract.Model.Listener<List<DocumentSnapshot>> listener) {
        instructorList = new ArrayList<>();
        GeoFirestore geoFirestore = new GeoFirestore(skillRef);
        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(lat, lon), radius);
        geoQuery.removeAllListeners();
        //testing the removal here
        if (Google.getInstance().getRecords() != null) {
            recordDataRejected = DumeUtils.filterList(Google.getInstance().getRecords(), "Rejected");
            rejectedRecordIds = new ArrayList<>();
            for (int i = 0; i < recordDataRejected.size(); i++) {
                rejectedRecordIds.add(recordDataRejected.get(i).getString("skill_uid"));
            }
            geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                @Override
                public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint location) {
                    String queryStringFromDb = (String) documentSnapshot.get("query_string");
                    Boolean status = documentSnapshot.getBoolean("status");
                    Map<String, Object> sp_info = (Map<String, Object>) documentSnapshot.get("sp_info");
                    Map<String, Object> self_rating = (Map<String, Object>) sp_info.get("self_rating");
                    Map<String, Object> unread_records = (Map<String, Object>) sp_info.get("unread_records");
                    Integer a_ratio_value = ((Integer.parseInt(unread_records.get("accepted_count").toString())
                            + Integer.parseInt(unread_records.get("completed_count").toString())
                            + Integer.parseInt(unread_records.get("current_count").toString())
                            + Integer.parseInt(unread_records.get("pending_count").toString()) + 1) /
                            (Integer.parseInt(unread_records.get("accepted_count").toString())
                                    + Integer.parseInt(unread_records.get("completed_count").toString())
                                    + Integer.parseInt(unread_records.get("current_count").toString())
                                    + Integer.parseInt(unread_records.get("pending_count").toString())
                                    + Integer.parseInt(unread_records.get("rejected_count").toString()) + 1)) * 100;
                    Float starRating = Float.parseFloat(self_rating.get("star_rating").toString());
                    Integer expertise_value = (Integer.parseInt(self_rating.get("l_expertise").toString()) /
                            Integer.parseInt(self_rating.get("l_expertise").toString()) + Integer.parseInt(self_rating.get("dl_expertise").toString())) * 100;
                    Float beha_value = (Float.parseFloat(self_rating.get("l_behaviour").toString()) /
                            Float.parseFloat(self_rating.get("l_behaviour").toString()) + Float.parseFloat(self_rating.get("dl_behaviour").toString())) * 100;


                    Map<String, Object> queryMap = SearchDataStore.getInstance().getQueryMap();
                    ArrayList<String> subjectList = (ArrayList<String>) queryMap.get("subject_list");
                    String commonQuery = (String) queryMap.get("common_query");

                    //validating valid mentors
                    if (queryStringFromDb != null && status!= null && status && a_ratio_value >= 40 && starRating>=2.2 && expertise_value>=40 && beha_value>= 40 ) {
                            if (queryStringFromDb.equals(queryString)) {
                                if (!SearchDataStore.getInstance().getUserUid().equals(documentSnapshot.getString("mentor_uid"))) {
                                    String skillId = documentSnapshot.getId();
                                    if (rejectedRecordIds.contains(skillId)) {
                                        for (int i = 0; i < recordDataRejected.size(); i++) {
                                            salary = null;
                                            if (recordDataRejected.get(i).getString("skill_uid").equals(skillId)) {
                                                //get the salary here
                                                // and if bigger then add otherwise not
                                                rejectedBy = recordDataRejected.get(i).getString("rejected_by");
                                                if (rejectedBy.equals(DumeUtils.TEACHER)) {
                                                    salary = (Number) recordDataRejected.get(i).get("salary");
                                                    break;
                                                }
                                            }
                                        }
                                        Number documentSalary = (Number) documentSnapshot.get("salary");

                                        if (documentSalary != null && SearchLoadingModel.this.salary != null && documentSalary.intValue() > SearchLoadingModel.this.salary.intValue()) {
                                            instructorList.add(documentSnapshot);
                                        }
                                    } else {
                                        //testing other thing like obligation etc here
                                        //TODO
                                        instructorList.add(documentSnapshot);
                                    }
                                }
                            } else if (queryStringFromDb.startsWith(commonQuery) && isMatch(subjectList, queryStringFromDb)) {

                                if (!SearchDataStore.getInstance().getUserUid().equals(documentSnapshot.getString("mentor_uid"))) {
                                    String skillId = documentSnapshot.getId();
                                    if (rejectedRecordIds.contains(skillId)) {
                                        for (int i = 0; i < recordDataRejected.size(); i++) {
                                            salary = null;
                                            if (recordDataRejected.get(i).getString("skill_uid").equals(skillId)) {
                                                //get the salary here
                                                // and if bigger then add otherwise not
                                                rejectedBy = recordDataRejected.get(i).getString("rejected_by");
                                                if (rejectedBy.equals(DumeUtils.TEACHER)) {
                                                    salary = (Number) recordDataRejected.get(i).get("salary");
                                                    break;
                                                }
                                            }
                                        }
                                        Number documentSalary = (Number) documentSnapshot.get("salary");

                                        if (documentSalary != null && SearchLoadingModel.this.salary != null && documentSalary.intValue() > SearchLoadingModel.this.salary.intValue()) {
                                            instructorList.add(documentSnapshot);
                                        }
                                    } else {
                                        //testing other thing like obligation etc here
                                        //TODO
                                        instructorList.add(documentSnapshot);
                                    }
                                }

                            }
                    }
                }

                @Override
                public void onDocumentExited(DocumentSnapshot documentSnapshot) {
                    // ...
                }

                @Override
                public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint location) {
                    // ...
                }

                @Override
                public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint location) {
                    // ...
                }

                @Override
                public void onGeoQueryReady() {

                    listener.onSuccess(instructorList);

                }

                @Override
                public void onGeoQueryError(Exception exception) {
                    // ...
                    listener.onError(exception.getLocalizedMessage());
                }
            });
        } else {
            RecordsPageModel recordsPageModel = new RecordsPageModel(context);
            recordsPageModel.getRecords(new TeacherContract.Model.Listener<List<Record>>() {
                @Override
                public void onSuccess(List<Record> list) {
                    recordDataRejected = DumeUtils.filterList(Google.getInstance().getRecords(), "Rejected");
                    rejectedRecordIds = new ArrayList<>();
                    for (int i = 0; i < recordDataRejected.size(); i++) {
                        rejectedRecordIds.add(recordDataRejected.get(i).getString("skill_uid"));
                    }
                    geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                        @Override
                        public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint location) {
                            String queryStringFromDb = (String) documentSnapshot.get("query_string");
                            if (queryStringFromDb != null) {
                                if (queryStringFromDb.equals(queryString)) {
                                    if (!SearchDataStore.getInstance().getUserUid().equals(documentSnapshot.getString("mentor_uid"))) {
                                        String skillId = documentSnapshot.getId();
                                        if (rejectedRecordIds.contains(skillId)) {
                                            for (int i = 0; i < recordDataRejected.size(); i++) {
                                                salary = null;
                                                if (recordDataRejected.get(i).getString("skill_uid").equals(skillId)) {
                                                    //get the salary here
                                                    // and if bigger then add otherwise not
                                                    rejectedBy = recordDataRejected.get(i).getString("rejected_by");
                                                    if (rejectedBy.equals(DumeUtils.TEACHER)) {
                                                        salary = (Number) recordDataRejected.get(i).get("salary");
                                                        break;
                                                    }
                                                }
                                            }
                                            Number documentSalary = (Number) documentSnapshot.get("salary");

                                            if (documentSalary != null && SearchLoadingModel.this.salary != null && documentSalary.intValue() > SearchLoadingModel.this.salary.intValue()) {
                                                instructorList.add(documentSnapshot);
                                            }
                                        } else {
                                            //testing other thing like obligation etc here
                                            //TODO
                                            instructorList.add(documentSnapshot);
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onDocumentExited(DocumentSnapshot documentSnapshot) {
                            // ...
                        }

                        @Override
                        public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint location) {
                            // ...
                        }

                        @Override
                        public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint location) {
                            // ...
                        }

                        @Override
                        public void onGeoQueryReady() {

                            listener.onSuccess(instructorList);

                        }

                        @Override
                        public void onGeoQueryError(Exception exception) {
                            // ...
                            listener.onError(exception.getLocalizedMessage());
                        }
                    });
                }

                @Override
                public void onError(String msg) {
                    listener.onError(msg);
                }
            });
        }


    }

    @Override
    public void searchLoadingHawwa() {

    }


    @Override
    public void updateRecentSearch(String identify, Map<String, Object> mainMap, TeacherContract.Model.Listener<Void> listener) {
        switch (identify) {
            case "rs_1":
                userStudentProInfo.update("recent_search.rs_1", mainMap, "next_rs_write", "2").addOnSuccessListener(new OnSuccessListener<Void>() {
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
                break;
            case "rs_2":
                userStudentProInfo.update("recent_search.rs_2", mainMap, "next_rs_write", "3").addOnSuccessListener(new OnSuccessListener<Void>() {
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
                break;
            case "rs_3":
                userStudentProInfo.update("recent_search.rs_3", mainMap, "next_rs_write", "1").addOnSuccessListener(new OnSuccessListener<Void>() {
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
                break;
            default:
                listener.onError("else running");
                break;
        }
    }

}
