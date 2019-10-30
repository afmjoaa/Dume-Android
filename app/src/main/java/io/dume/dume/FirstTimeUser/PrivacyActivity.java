package io.dume.dume.FirstTimeUser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import io.dume.dume.R;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthActivity;

public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener {

    private DataStore local;
    private WebView webViewPrivacey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        webViewPrivacey = findViewById(R.id.termsWV);
        local = DataStore.getInstance();
        init();
    }


    private void init() {
        if (local.getAccountManjor() == DataStore.TEACHER) {
            webViewPrivacey.loadUrl("file:///android_asset/pages/teacher_privacy.html");
        } else {
            webViewPrivacey.loadUrl("file:///android_asset/pages/student_privacy.html");
        }
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, AuthActivity.class));
    }
}
