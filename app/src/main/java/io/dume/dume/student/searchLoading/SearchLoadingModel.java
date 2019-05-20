package io.dume.dume.student.searchLoading;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

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
    ArrayList<String> mentorList = null;
    private List<DocumentSnapshot> recordDataRejected;
    private List<String> rejectedRecordIds;
    private Number salary;
    private String rejectedBy = null;
    private SearchDataStore searchDataStore;
    private Context context;
    private Integer offeredSalary;

    SearchLoadingModel(Context context) {
        super(context);
        this.context = context;
        instructorList = new ArrayList<>();
        mentorList = new ArrayList<>();
        searchDataStore = SearchDataStore.getInstance();
    }

    @Override
    public void search(double lat, double lon, double radius, String queryString, TeacherContract.Model.Listener<List<DocumentSnapshot>> listener) {
        instructorList = new ArrayList<>();
        GeoFirestore geoFirestore = new GeoFirestore(skillRef);
        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(lat, lon), radius);
        geoQuery.removeAllListeners();

        String salaryInOffer = (String) searchDataStore.getJizz().get("Salary");
        if (salaryInOffer != null) {
            String[] splited = salaryInOffer.split("\\s+");
            offeredSalary = Integer.parseInt(splited[2].substring(0, splited[2].length() - 1));
            offeredSalary = offeredSalary * 1000;
            //adding 2k threshold value
            offeredSalary = offeredSalary + 2000;
        }
        //testing the removal of rejected and pending here
        if (Google.getInstance().getRecords() != null) {
            recordDataRejected = DumeUtils.filterList(Google.getInstance().getRecords(), "Rejected");
            rejectedRecordIds = new ArrayList<>();
            for (int i = 0; i < recordDataRejected.size(); i++) {
                rejectedRecordIds.add(recordDataRejected.get(i).getString("skill_uid"));
            }
            geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {

                @Override
                public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint location) {
                    //checking gender preference validity
                    String gendePreference = (String) searchDataStore.getJizz().get("Gender");
                    Map<String, Object> sp_info = (Map<String, Object>) documentSnapshot.get("sp_info");
                    if (gendePreference != null) {
                        String mentorGender = (String) sp_info.get("gender");
                        if (gendePreference.equals("Any")) {
                            validateSkill(documentSnapshot, queryString);
                        } else if (sp_info != null && mentorGender != null) {
                            if (mentorGender.equals(gendePreference)) {
                                validateSkill(documentSnapshot, queryString);
                            }
                        } else {
                            validateSkill(documentSnapshot, queryString);
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
                            //checking gender preference validity
                            String gendePreference = (String) searchDataStore.getJizz().get("Gender");
                            Map<String, Object> sp_info = (Map<String, Object>) documentSnapshot.get("sp_info");
                            if (gendePreference != null) {
                                String mentorGender = (String) sp_info.get("gender");
                                if (gendePreference.equals("Any")) {
                                    validateSkill(documentSnapshot, queryString);
                                } else if (sp_info != null && mentorGender != null) {
                                    if (mentorGender.equals(gendePreference)) {
                                        validateSkill(documentSnapshot, queryString);
                                    }
                                } else {
                                    validateSkill(documentSnapshot, queryString);
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

    private void validateSkill(DocumentSnapshot documentSnapshot, String queryString) {
        String mentor_uid = (String) documentSnapshot.get("mentor_uid");

        String queryStringFromDb = (String) documentSnapshot.get("query_string");
        String dbPackageName = (String) documentSnapshot.get("package_name");
        List<String> dbQueryList = (List<String>) documentSnapshot.get("query_list");

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

        Log.e("bar", "Document Entered" + queryStringFromDb);

        //validating valid mentors
        if (queryStringFromDb != null && status != null && status && a_ratio_value >= 40 && starRating >= 2.2 && expertise_value >= 40 && beha_value >= 40) {
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
                            //TODO

                            if (!mentorList.contains(mentor_uid)) {
                                instructorList.add(documentSnapshot);
                                mentorList.add(mentor_uid);
                            }
                        }
                    } else {
                        //adding if not found in previous records
                        if (offeredSalary != null) {
                            Number mentorAskedSalary = (Number) documentSnapshot.get("salary");
                            //checking salary validity
                            if (mentorAskedSalary != null && offeredSalary >= mentorAskedSalary.intValue()) {
                                if (!mentorList.contains(mentor_uid)) {
                                    instructorList.add(documentSnapshot);
                                    mentorList.add(mentor_uid);
                                }
                            }
                        } else {
                            if (!mentorList.contains(mentor_uid)) {
                                instructorList.add(documentSnapshot);
                                mentorList.add(mentor_uid);
                            }
                        }
                    }
                }
            } else {//checking multi subject skill for adding
                searchDataStore = SearchDataStore.getInstance();
                if (searchDataStore.getPackageName().equals(dbPackageName) && DumeUtils.isCommonMatched(searchDataStore.getQueryList(), dbQueryList, dbPackageName.equals(SearchDataStore.DUME_GANG) ? 4 : 3) && DumeUtils.isAMinimalMatch(searchDataStore.getQueryList(), dbQueryList, dbPackageName.equals(SearchDataStore.DUME_GANG) ? 4 : 3)) {

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
                                if (!mentorList.contains(mentor_uid)) {
                                    instructorList.add(documentSnapshot);
                                    mentorList.add(mentor_uid);
                                }
                            }
                        } else {
                            Number mentorAskedSalary = (Number) documentSnapshot.get("salary");
                            //adding if not found in previous records
                            //TODO calculated salary
                            //getting the subject array string
                            int threshold = dbPackageName.equals(SearchDataStore.DUME_GANG) ? 4 : 3;
                            List<String> localQueryList = searchDataStore.getQueryList();

                            String localSB = localQueryList.get(localQueryList.size() - threshold);
                            String dbSB = dbQueryList.get(dbQueryList.size() - threshold);

                            String localtrim = localSB.replaceAll("\\s", "");
                            String dbtrim = dbSB.replaceAll("\\s", "");

                            String[] localsplit = localtrim.split(",");
                            String[] dbsplit = dbtrim.split(",");

                            Number calculatedSalary = mentorAskedSalary.floatValue()*0.25 +((mentorAskedSalary.floatValue() - (mentorAskedSalary.floatValue()*0.25))/
                                    (dbsplit.length))*localsplit.length;

                            if (offeredSalary != null) {
                                //checking salary validity
                                if (calculatedSalary != null && offeredSalary >= calculatedSalary.intValue()) {
                                    if (!mentorList.contains(mentor_uid)) {
                                        instructorList.add(documentSnapshot);
                                        mentorList.add(mentor_uid);
                                    }
                                }
                            } else {
                                if (!mentorList.contains(mentor_uid)) {
                                    instructorList.add(documentSnapshot);
                                    mentorList.add(mentor_uid);
                                }
                            }
                        }
                    }
                }
            }
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

/*
 geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
                        @Override
                        public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint location) {
                            String gendePreference = (String) SearchDataStore.getInstance().getJizz().get("Gender");
                            Map<String, Object> sp_info = (Map<String, Object>) documentSnapshot.get("sp_info");
                            if (gendePreference != null) {
                                String mentorGender = (String) sp_info.get("gender");
                                if (gendePreference.equals("Any")) {
                                    validateSkillOnRecord(documentSnapshot, queryString);
                                } else if (sp_info != null && mentorGender != null) {
                                    if (mentorGender.equals(gendePreference)) {
                                        validateSkillOnRecord(documentSnapshot, queryString);
                                    }
                                } else {
                                    validateSkillOnRecord(documentSnapshot, queryString);
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
*/

/*

    private void validateSkillOnRecord(DocumentSnapshot documentSnapshot, String queryString) {
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
*/