package io.dume.dume.student.searchLoading;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.GeoQueryDataEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.pojo.StuBaseModel;
import io.dume.dume.teacher.homepage.TeacherContract;

public class SearchLoadingModel extends StuBaseModel implements SearchLoadingContract.Model {
    ArrayList<DocumentSnapshot> instructorList = null;

    public SearchLoadingModel(Context context) {
        super(context);
        instructorList = new ArrayList<>();
    }

    @Override
    public void search(double lat, double lon, double radius, String queryString, TeacherContract.Model.Listener<List<DocumentSnapshot>> listener) {
        //CollectionReference geoFirestoreRef = FirebaseFirestore.getInstance().collection("my-collection");


        GeoFirestore geoFirestore = new GeoFirestore(skillRef);
        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(lat, lon), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint location) {
                String queryStringFromDb = (String) documentSnapshot.get("query_string");
                if (queryStringFromDb != null) {
                    if (queryStringFromDb.equals(queryString)) {
                        instructorList.add(documentSnapshot);
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
                if (instructorList.size() > 0) {
                    listener.onSuccess(instructorList);
                } else {
                    listener.onError("Not Found Any Data");
                }
            }

            @Override
            public void onGeoQueryError(Exception exception) {
                // ...
            }
        });
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
