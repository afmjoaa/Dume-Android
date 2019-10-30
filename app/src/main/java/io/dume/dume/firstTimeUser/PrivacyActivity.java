package io.dume.dume.firstTimeUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import io.dume.dume.R;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

public class PrivacyActivity extends CustomStuAppCompatActivity implements View.OnClickListener {

    private DataStore local;
    private WebView webViewPrivacey;
    private LinearLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        setActivityContext(this, 3636);
        webViewPrivacey = findViewById(R.id.termsWV);
        local = DataStore.getInstance();
        init();
    }


    private void init() {
        //  settingStatusBarTransparent();
        //  setDarkStatusBarIcon();
        if (local.getAccountManjor() == DataStore.TEACHER) {
            webViewPrivacey.loadUrl("file:///android_asset/pages/teacher_privacy.html");
        } else {
            webViewPrivacey.loadUrl("file:///android_asset/pages/student_privacy.html");
        }
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backBtn) {
            super.onBackPressed();
        } else {
            startActivity(new Intent(this, AuthActivity.class));

        }

    }


}
