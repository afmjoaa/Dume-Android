package io.dume.dume.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.dume.dume.afterSplashTrp.AfterSplashActivity;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.student.homePage.StudentActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    SplashContract.Presenter presenter;
    private static final String TAG = "SplashActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SplashPresenter(this, new AuthModel(this, this));
        presenter.enqueue();
        presenter.init(this);

    }

    @Override
    public void gotoLoginActivity() {
        startActivity(new Intent(this, AfterSplashActivity.class));
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
