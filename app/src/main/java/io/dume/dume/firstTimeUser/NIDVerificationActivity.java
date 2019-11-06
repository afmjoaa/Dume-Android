package io.dume.dume.firstTimeUser;

import android.os.Bundle;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class NIDVerificationActivity extends CustomStuAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_nidverification);
        super.onCreate(savedInstanceState);
        setDarkStatusBarIcon();
        configureAppbar(this, "NID Verification", true);
    }
}
