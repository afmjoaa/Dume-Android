package io.dume.dume.student.homePage;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.GatewayInfo;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.afterSplashTrp.AfterSplashActivity;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.common.chatActivity.ChatActivity;
import io.dume.dume.student.grabingInfo.GrabingInfoActivity;
import io.dume.dume.student.grabingPackage.GrabingPackageActivity;
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity;
import io.dume.dume.student.recordsCompleted.RecordsCompletedActivity;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.student.recordsPending.RecordsPendingActivity;
import io.dume.dume.student.recordsRejected.RecordsRejectedActivity;
import io.dume.dume.student.searchLoading.SearchLoadingActivity;
import io.dume.dume.student.searchResult.SearchResultActivity;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.util.DumeUtils;

public class StudentActivity extends AppCompatActivity implements StudentContract.View {
    private static final String TAG = "Hola";
    StudentContract.Presenter presenter;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private FirebaseFirestore db;

    public void test()
    {


    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        presenter = new StudentPresenter(this, this, new StudentModel());
        presenter.enqueue();
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    @Override
    public void onSignOut() {
        final CollectionReference collection = db.collection("/users/mentors/testing_profile");
        //final Query query = collection.whereEqualTo("name", "foo");

        GeoPoint geoPoint = new GeoPoint(2, 2.2);
        Map<String, Object> map = new HashMap<>();
        map.put("lt", 2.2);
        map.put("ln", 5.1);
        final Query query = collection.whereEqualTo("location", geoPoint);
        query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<DocumentSnapshot> documents;
            if (queryDocumentSnapshots != null) {
                documents = queryDocumentSnapshots.getDocuments();
                // final GeoPoint location = (GeoPoint) documents.get(0).get("location");
                for (int i = 0; i < documents.size(); i++) {

                    Log.e(TAG, "onEvent: " + documents.get(i).get("name"));
                }
                Log.e(TAG, "onEvent: Called");

            } else {
                Log.e(TAG, "onEvent: Empty");
            }

        });


    }

    @Override
    public void goToMapView() {
        startActivity(new Intent(this, SearchResultActivity.class));
    }

    @Override
    public void goToStudentHOmePage() {
        startActivity(new Intent(this, HomePageActivity.class));

    }

    @Override
    public void configView() {

    }

    @Override
    public void goTORecordsActivity() {
        startActivity(new Intent(this, GrabingInfoActivity.class).setAction(DumeUtils.STUDENT));
    }

    @Override
    public void goTORecordsActivity2() {
        startActivity(new Intent(this, SearchLoadingActivity.class));
        //startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    public void goTORecordsActivity3() {
        startActivity(new Intent(this, CrudSkillActivity.class).setAction(DumeUtils.BOOTCAMP));

    }

    public void onStudentViewClicked(View view) {
        presenter.onViewIntracted(view);
    }

}
//<!--android:name=".splash.SplashActivity"-->

