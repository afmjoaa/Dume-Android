package io.dume.dume.auth.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import io.dume.dume.R;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;
import io.dume.dume.auth.code_verification.PhoneVerificationActivity;
import io.dume.dume.foreignObligation.PayActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class AuthActivity extends BaseAppCompatActivity implements AuthContract.View, TextView.OnEditorActionListener, TextWatcher {
    AuthContract.Presenter presenter;
    private TextInputEditText phoneEditText;
    private ExtendedFloatingActionButton floatingButoon;
    private TextView numberCounter;
    private static final String TAG = "AuthActivity";
    private DataStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        setActivityContext(this, 1605);
        presenter = new AuthPresenter(this, this, new AuthModel(this, this));
        presenter.enqueue();
        findLoadView();
        dataStore = DataStore.getInstance();
        presenter.setBundle();

        //testing null error solution
        DataStore.setSTATION(1);
        //setting my snackbar callback
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                floatingButoon.setClickable(true);
                floatingButoon.setEnabled(true);
            }

            @Override
            public void onShown(Snackbar snackbar) {
                floatingButoon.setClickable(false);
                floatingButoon.setEnabled(false);
            }
        });
    }


    @Override
    public void init() {
        setDarkStatusBarIcon();
        configureAppbar(this, "Enter Phone Number", true);
        phoneEditText.setOnEditorActionListener(this);
        floatingButoon.setOnClickListener(view -> presenter.onPhoneValidation(phoneEditText.getText().toString()));
        phoneEditText.addTextChangedListener(this);
    }


    @Override
    public void findView() {
        phoneEditText = findViewById(R.id.phoneNumberEditText);
        floatingButoon = findViewById(R.id.verify_phone);
        numberCounter = findViewById(R.id.phoneCount);
    }


    @Override
    public void showCount(String s) {
        if (numberCounter.getVisibility() == View.INVISIBLE) {
            numberCounter.setVisibility(View.VISIBLE);
        }
        numberCounter.setText(s);
    }

    @Override
    public void hideCount() {
        numberCounter.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onValidationSuccess(String number) {
        Toast.makeText(this, "Validation Success", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onValidationFailed(String err) {
        phoneEditText.setError(err);
    }

    @Override
    public void goToVerificationActivity() {
        DataStore.setSTATION(1);
        Intent intent = new Intent(this, PhoneVerificationActivity.class);
        startActivity(intent);

    }

    @Override
    public void goToRegesterActivity() {
        Intent intent = new Intent(this, AuthRegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }


    @Override
    public void enableVerifyButton() {
        floatingButoon.setEnabled(true);


    }

    @Override
    public void disableVerifyButton() {
        floatingButoon.setEnabled(false);
    }

    @Override
    public void sending() {
        floatingButoon.setText("Code Sending...");
    }

    @Override
    public void resetSending() {

    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            presenter.onPhoneValidation(phoneEditText.getText().toString());
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        presenter.onPhoneTextChange(String.valueOf(charSequence));
    }

    @Override
    public void afterTextChanged(Editable editable) {
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
    public void gotoForeignObligation() {
        startActivity(new Intent(this, PayActivity.class));
        finish();
    }

}


