package io.dume.dume.auth.code_verification;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.dume.dume.R;
import io.dume.dume.student.homepage.StudentActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;

public class PhoneVerificationActivity extends AppCompatActivity implements PhoneVerificationContract.View {
    private PhoneVerificationContract.Presenter presenter;
    private Toolbar toolbar;
    private TextView detailsTextView;
    private static final String TAG = "PhoneVerificationActivi";
    private EditText pinEditText;
    private AppBarLayout appbar;
    private FloatingActionButton fab;
    private ProgressDialog progressDialog;
    private TextView timerTextView;
    private Button resendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        presenter = new PhoneVerficationPresenter(this, new PhoneVerificationModel());
        presenter.enqueue();
    }

    @Override
    public void init() {
        pinEditText.setOnClickListener(view -> appbar.setExpanded(false));
        fab.setOnClickListener(view -> presenter.onPinConfirm(pinEditText.getText().toString()));
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("");
    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        Log.w(TAG, "initActionBar: ");
    }

    @Override
    public void findView() {
        toolbar = findViewById(R.id.verifyToolbar);
        detailsTextView = findViewById(R.id.detailsTxt);
        pinEditText = findViewById(R.id.pinEditTxt);
        appbar = findViewById(R.id.verifyAppBar);
        fab = findViewById(R.id.verifyFab);
        timerTextView = findViewById(R.id.timerTxt);
        resendButton = findViewById(R.id.resendButton);
    }

    @Override
    public void getUpComingData() {

    }


    @Override
    public void onVerificationFailed(String msg) {
        pinEditText.setError(msg);
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

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void updateTimer(long millis) {
        timerTextView.setText("Resend Code in " + millis / 1000 + " Seconds");
    }

    @Override
    public void onTimerCompleted() {
        timerTextView.setVisibility(View.GONE);
        resendButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
    }
}
