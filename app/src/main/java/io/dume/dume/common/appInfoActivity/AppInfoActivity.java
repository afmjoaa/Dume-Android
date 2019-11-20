package io.dume.dume.common.appInfoActivity;

import android.os.Bundle;

import io.dume.dume.R;
import io.dume.dume.student.pojo.BaseAppCompatActivity;

public class AppInfoActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        setActivityContext(this, fromFlag);
        settingStatusBarTransparent();
        makeFullScreen();
    }
}
