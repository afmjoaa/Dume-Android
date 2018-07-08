package io.dume.dume.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v7.app.AppCompatActivity;

import io.dume.dume.auth.AuthenticationActivity;
import io.dume.dume.student.homepage.StudentActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    SplashContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this, new AuthModel());
        presenter.enqueue();

    }

    @Override
    public void gotoLoginActivity() {
        startActivity(new Intent(this, AuthenticationActivity.class));
        finish();
    }

    @Override
    public void gotoTeacherActivity() {
        startActivity(new Intent(this, TeacherActivtiy.class));
        finish();
    }

    @Override
    public void gotoStudentActivity() {
        startActivity(new Intent(this, StudentActivity.class));
        finish();
    }
}
