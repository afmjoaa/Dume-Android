package io.dume.dume.auth.code_verification;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.dume.dume.R;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import me.philio.pinentry.PinEntryView;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class PhoneVerificationActivity extends AppCompatActivity implements PhoneVerificationContract.View {
    private PhoneVerificationContract.Presenter presenter;
    private TextView detailsTextView;
    private static final String TAG = "PhoneVerificationActivi";
    private PinEntryView pinEditText;
    private AppBarLayout appbar;
    private FloatingActionButton fab;
    private TextView timerTextView;
    private Button resendButton;
    private DataStore dataStore;
    private AlertDialog dialog = null;
    private Context context;
    private HorizontalLoadView loadView;
    private TextView editPhn;
    public static boolean isAlive;
    private BroadcastReceiver smsAutoVerfication;

    @Override
    protected void onStart() {
        super.onStart();
        isAlive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);
        presenter = new PhoneVerficationPresenter(this, this, new AuthModel(this, this));
        presenter.enqueue();
        smsAutoVerfication = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();

                String sms_body = null;
                if (extras != null) {
                    sms_body = extras.getString("sms_body");

                    if (sms_body != null && sms_body.contains("code")) {
                        String[] split = sms_body.split(" ");
                        String smsCode = split[0];
                        char[] chars = smsCode.toCharArray();
                        pinEditText.setText(smsCode);
                        Log.w(TAG, "onReceive: Sms Code " + smsCode);
                        fab.performClick();
                    }

                }

            }
        };
        registerReceiver(smsAutoVerfication, new IntentFilter("got_sms"));


    }

    @Override
    public void init() {
        context = this;
        pinEditText.setOnClickListener(view -> appbar.setExpanded(false));
        fab.setOnClickListener(view -> {
            presenter.onPinConfirm(pinEditText.getText().toString());
        });
        resendButton.setOnClickListener(view -> {
            showProgress();
            resendButton.setText("Sending Code....");
            resendButton.setEnabled(false);
            presenter.onResendCode();
        });
        dataStore = (DataStore) this.getIntent().getSerializableExtra("datastore");
        detailsTextView.setText("Enter the 6 digit verification code sent to you at +88 " + dataStore.getPhoneNumber());
        detailsTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Cairo-ExtraLight.ttf"));
    }

    @Override
    public void initActionBar() {
        configureAppbar(this, "Verify your number", true);
    }

    @Override
    public void findView() {
        detailsTextView = findViewById(R.id.detailsTxt);
        pinEditText = findViewById(R.id.pinEditTxt);
        appbar = findViewById(R.id.app_bar);
        fab = findViewById(R.id.verifyFab);
        timerTextView = findViewById(R.id.timerTxt);
        resendButton = findViewById(R.id.resendButton);
        loadView = findViewById(R.id.loadView);
        editPhn = findViewById(R.id.editPhn);
    }

    @Override
    public void getUpComingData() {

    }


    @Override
    public void onVerificationFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void gotoTeacherActivity() {
        startActivity(new Intent(this, TeacherActivtiy.class));
        finish();
    }

    @Override
    public void gotoStudentActivity() {
        startActivity(new Intent(this, HomePageActivity.class));
        finish();
    }

    @Override
    public void showProgress() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    @Override
    public void hideProgress() {
        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateTimer(long millis) {
        timerTextView.setText(String.format("Resend Code in %d Seconds", millis / 1000));
    }

    @Override
    public void onTimerCompleted() {
        resendButton.setText("Resend Code");
        resendButton.setEnabled(true);
        timerTextView.setVisibility(View.GONE);
        resendButton.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10 * (int) getResources().getDisplayMetrics().density, 0, 0, 0);
        editPhn.setLayoutParams(layoutParams);
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimerStarted() {
        hideProgress();
        timerTextView.setVisibility(View.VISIBLE);
        resendButton.setVisibility(View.GONE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        editPhn.setLayoutParams(layoutParams);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
        isAlive = false;
        if (smsAutoVerfication != null) {
            unregisterReceiver(smsAutoVerfication);
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
