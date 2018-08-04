package io.dume.dume.student.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import io.dume.dume.R;
import io.dume.dume.auth.auth.AuthActivity;

public class StudentActivity extends AppCompatActivity implements StudentContract.View {
    StudentContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        presenter = new StudentPresenter(this, this, new StudentModel());
        presenter.enqueue();
    }

    @Override
    public void onSignOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthActivity.class));
        }
    }

    @Override
    public void goToMapView() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void goToStudentHOmePage() {
        startActivity(new Intent(this, HomePageActivity.class));

    }

    @Override
    public void configView() {

    }

    public void onStudentViewClicked(View view) {
        presenter.onViewIntracted(view);
    }

}
