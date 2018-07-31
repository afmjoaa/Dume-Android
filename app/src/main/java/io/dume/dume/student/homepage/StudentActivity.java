package io.dume.dume.student.homepage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.auth.FirebaseAuth;


import io.dume.dume.R;
import io.dume.dume.auth.auth.AuthActivity;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
    }


    public void onStudentViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signOutBtn:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.testingActivityBtn:
                startActivity(new Intent(this, StuHomepageActivity.class));
                break;

            case R.id.testingMapBtn:
                startActivity(new Intent(this, MapsActivity.class));
                break;
        }

    }

}
