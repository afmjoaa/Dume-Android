package io.dume.dume.auth.code_verification;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import io.dume.dume.R;

public class PhoneVerificationActivity extends AppCompatActivity implements PhoneVerificationContract.View {
    PhoneVerificationContract.Presenter presenter;
    Toolbar toolbar;
    private static final String TAG = "PhoneVerificationActivi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        presenter = new PhoneVerficationPresenter(this, new PhoneVerificationModel());
        presenter.enqueue();
    }

    @Override
    public void init() {

    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.setHomeAsUpIndicator(R.drawable.back_arraw_icon);
        }
        Log.w(TAG, "initActionBar: ");
    }

    @Override
    public void findView() {
        toolbar = findViewById(R.id.verifyToolbar);
    }

    @Override
    public void getUpComingData() {

    }
}
