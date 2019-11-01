package io.dume.dume.student.heatMap;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dume.dume.student.pojo.DataSet;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

public class HeatMapModel implements HeatMapContract.Model {

    private final FirebaseFirestore db;

    HeatMapModel() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void heatMaphawwa() {

    }


    public void getStuLocData(TeacherContract.Model.Listener<DataSet> listener) {
        db.collection("app/dume_utils/student_location").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> result) {
                if(result.isSuccessful()){
                    QuerySnapshot mainResult = result.getResult();
                    if (mainResult != null) {
                        List<DocumentSnapshot> documents = mainResult.getDocuments();
                        if (documents.size() > 0) {
                            DataSet dataSet = new DataSet();
                            ArrayList<LatLng> list = new ArrayList<>();
                            for (int i = 0; i < documents.size(); i++) {
                                Map<String, Object> data = documents.get(i).getData();
                                if (data != null) {
                                    for (Map.Entry<String, Object> item : data.entrySet()) {
                                        Map<String, Object> loc = (Map<String, Object>) item.getValue();
                                        Number latitude = (Number) loc.get("_latitude");
                                        Number longtitude = (Number) loc.get("_longitude");
                                        if (!(latitude.doubleValue() == 84.9) || !(longtitude.doubleValue() == -180)) {
                                            list.add(new LatLng(latitude.doubleValue(), longtitude.doubleValue()));
                                        }
                                    }
                                }
                            }

                            if (list.size() > 0) {
                                dataSet.setmDataset(list);
                                dataSet.setmUrl(DumeUtils.STUDENT);
                                listener.onSuccess(dataSet);
                            } else {
                                listener.onError("Students location data not available...");
                            }
                        }

                    }
                }else {
                    listener.onError("Network error !!");
                }
            }
        });



    }

    public void getMentorLocData(TeacherContract.Model.Listener<DataSet> listener) {

        db.collection("/app/dume_utils/mentor_location").get(Source.DEFAULT).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> result) {
                if(result.isSuccessful()){
                    QuerySnapshot mainResult = result.getResult();
                    if (mainResult != null) {
                        List<DocumentSnapshot> documents = mainResult.getDocuments();
                        if (documents.size() > 0) {

                            DataSet dataSet = new DataSet();
                            ArrayList<LatLng> list = new ArrayList<>();
                            for (int i = 0; i < documents.size(); i++) {
                                Map<String, Object> data = documents.get(i).getData();
                                if (data != null) {
                                    for (Map.Entry<String, Object> item : data.entrySet()) {
                                        Map<String, Object> loc = (Map<String, Object>) item.getValue();
                                        Number latitude = (Number) loc.get("_latitude");
                                        Number longtitude = (Number) loc.get("_longitude");
                                        if (!(latitude.doubleValue() == 84.9) || !(longtitude.doubleValue() == -180)) {
                                            list.add(new LatLng(latitude.doubleValue(), longtitude.doubleValue()));
                                        }
                                    }
                                }
                            }

                            if (list.size() > 0) {
                                dataSet.setmDataset(list);
                                dataSet.setmUrl(DumeUtils.TEACHER);
                                listener.onSuccess(dataSet);
                            } else {
                                listener.onError("N/A Users Location");
                            }
                        }
                    }
                }else{
                    listener.onError("Network error !!");
                }
            }
        });
    }
}
