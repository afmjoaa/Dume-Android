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
import io.dume.dume.student.pojo.BaseAppCompatActivity;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class PrivacyActivity extends BaseAppCompatActivity {

    private DataStore local;
    private WebView webViewPrivacey;
    private LinearLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        setActivityContext(this, 2002);
        webViewPrivacey = findViewById(R.id.termsWV);
        local = DataStore.getInstance();
        init();
    }

    private void init() {
        if (local.getAccountManjor().equals(DataStore.TEACHER)) {
            webViewPrivacey.loadUrl("file:///android_asset/pages/teacher_privacy.html");
            configureAppbar(this, "Teacher Guide", true);
        } else {
            webViewPrivacey.loadUrl("file:///android_asset/pages/student_privacy.html");
            configureAppbar(this, "User Guide", true);
        }
        ExtendedFloatingActionButton testOne = findViewById(R.id.continueBtn);
        testOne.extend();

    }


    public void continueBtnCLicked(View view) {
        startActivity(new Intent(this, AuthActivity.class));
    }
}
