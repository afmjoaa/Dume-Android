package io.dume.dume.common.appInfoActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

public class AppInfoActivity extends CustomStuAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        setActivityContext(this, fromFlag);
        makeFullScreen();
    }
}