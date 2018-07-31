package io.dume.dume.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.dume.dume.auth.AuthModel;
import io.dume.dume.student.homepage.StudentActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.mentor_settings.AccountSettings;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    SplashContract.Presenter presenter;
    private static final String TAG = "SplashActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this, new AuthModel(this, this));
        presenter.enqueue();
    }

    @Override
    public void gotoLoginActivity() {
      /*  startActivity(new Intent(this, AuthActivity.class));
        finish();*/
        startActivity(new Intent(this, AccountSettings.class));
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
