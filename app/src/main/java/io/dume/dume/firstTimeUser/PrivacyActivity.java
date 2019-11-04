package io.dume.dume.firstTimeUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import io.dume.dume.R;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class PrivacyActivity extends CustomStuAppCompatActivity {

    private DataStore local;
    private WebView webViewPrivacey;
    private LinearLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        setActivityContext(this, 3638);
        webViewPrivacey = findViewById(R.id.termsWV);
        local = DataStore.getInstance();
        init();
    }

    private void init() {
         setDarkStatusBarIcon();
        if (local.getAccountManjor().equals(DataStore.TEACHER)) {
            webViewPrivacey.loadUrl("file:///android_asset/pages/teacher_privacy.html");
        } else {
            webViewPrivacey.loadUrl("file:///android_asset/pages/student_privacy.html");
        }
        ExtendedFloatingActionButton testOne = findViewById(R.id.continueBtn);
        testOne.extend();
        configureAppbar(this, "Teacher Guide", true);
    }


    public void continueBtnCLicked(View view) {
        startActivity(new Intent(this, AuthActivity.class));
    }
}
