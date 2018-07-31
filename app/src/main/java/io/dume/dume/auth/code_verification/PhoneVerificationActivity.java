package io.dume.dume.auth.code_verification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import dmax.dialog.SpotsDialog;
import io.dume.dume.R;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;
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

    private TextView timerTextView;
    private Button resendButton;
    private DataStore dataStore;
    private SpotsDialog.Builder spotsBuilder;
    private AlertDialog alertDialog;
    private AlertDialog dialog = null;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        presenter = new PhoneVerficationPresenter(this, new AuthModel(this, this));
        presenter.enqueue();

    }

    @Override
    public void init() {
        context = this;
        pinEditText.setOnClickListener(view -> appbar.setExpanded(false));
        fab.setOnClickListener(view -> presenter.onPinConfirm(pinEditText.getText().toString()));
        resendButton.setOnClickListener(view -> presenter.onResendCode());
        spotsBuilder = new SpotsDialog.Builder().setContext(this);
        dataStore = (DataStore) this.getIntent().getSerializableExtra("datastore");
        detailsTextView.setText("Enter the 6 digit verification code sent to you at +88 " + dataStore.getPhoneNumber());
        detailsTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Cairo-ExtraLight.ttf"));
    }

    @Override
    public void initActionBar() {
        toolbar.setTitleTextAppearance(this, R.style.Widget_BottomNavigationView);
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
    public void showProgress(String title) {
        if (!((Activity) context).isFinishing()) {
            dialog = spotsBuilder.setMessage(title).build();
            dialog.show();
        }

    }

    @Override
    public void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void updateTimer(long millis) {
        timerTextView.setText(String.format("Resend Code in %d Seconds", millis / 1000));
    }

    @Override
    public void onTimerCompleted() {
        timerTextView.setVisibility(View.GONE);
        resendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimerStarted() {
        timerTextView.setVisibility(View.VISIBLE);
        resendButton.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        goBack();
        return super.onOptionsItemSelected(item);
    }

    private void goBack() {
        Class intentClass;
        if (DataStore.STATION == 1) {
            intentClass = AuthActivity.class;
        } else {
            intentClass = AuthRegisterActivity.class;
        }
        this.startActivity(new Intent(this, intentClass).putExtra("datastore", dataStore).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        this.finish();
    }

    @Override
    public void onBackPressed() {

    }

    public void onPhoneEdit(View view) {
        goBack();
    }
}
