package io.dume.dume.auth.auth_final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import io.dume.dume.R;
import io.dume.dume.auth.AuthActivity;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.code_verification.PhoneVerificationActivity;
import io.dume.dume.util.DumeUtils;

public class AuthRegisterActivity extends AppCompatActivity {
    EditText firstname, lastName, email, phoneNumber;
    private AlertDialog.Builder dialogBuilder;
    private DataStore datastore = null;
    private android.app.AlertDialog sendingCodeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_final);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        if (getIntent().getSerializableExtra("datastore") != null) {
            datastore = (DataStore) getIntent().getSerializableExtra("datastore");
            restoreData(datastore);
        }

    }

    private void restoreData(DataStore dataStore) {
        firstname.setText(dataStore.getFirstName() == null ? "" : dataStore.getFirstName());
        lastName.setText(dataStore.getLastName() == null ? "" : dataStore.getLastName());
        email.setText(dataStore.getEmail() == null ? "" : dataStore.getEmail());
        phoneNumber.setText(dataStore.getPhoneNumber() == null ? "" : dataStore.getPhoneNumber());
    }

    private void init() {
        firstname = findViewById(R.id.input_firstname);
        lastName = findViewById(R.id.input_lastname);
        email = findViewById(R.id.input_email);
        phoneNumber = findViewById(R.id.phoneNumberEditText);
        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("We sending a code to your mobile number");
        dialogBuilder.setTitle("Sending....");
        sendingCodeDialog = new SpotsDialog.Builder().setContext(this).setMessage("Sending Code").build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auth_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutMenu) {
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
        } else {
            this.startActivity(new Intent(this, AuthActivity.class).putExtra("datastore", datastore).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.floating_button:


                if (firstname.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || phoneNumber.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Hey Man !!! Fill up all the data.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phoneStr = this.phoneNumber.getText().toString();
                if (phoneStr.isEmpty()) {
                    phoneNumber.setError("Should not be empty");
                    return;
                } else if (!DumeUtils.isInteger(phoneStr)) {
                    phoneNumber.setError("Only Digits Allowed (0-9)");
                    return;
                } else if (phoneStr.length() != 11) {
                    phoneNumber.setError("Should be 11 Digits");
                    return;
                }
                sendingCodeDialog.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + phoneStr, 60, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        sendingCodeDialog.dismiss();
                        Toast.makeText(AuthRegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        sendingCodeDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                        datastore.setFirstName(firstname.getText().toString());
                        datastore.setLastName(lastName.getText().toString());
                        datastore.setEmail(email.getText().toString());
                        datastore.setPhoneNumber(phoneNumber.getText().toString());
                        DataStore.resendingToken = forceResendingToken;
                        datastore.setVerificationId(verificationId);
                        datastore.setSTATION(2);
                        intent.putExtra("datastore", datastore);
                        startActivity(intent);
                        finish();

                    }
                });

                /**/

                break;
            case R.id.hiperlink_privacy_text:
                dialogBuilder.setTitle("Privacy Policy");
                dialogBuilder.setMessage(R.string.lorem_para);
                dialogBuilder.create().show();
                break;
            case R.id.hiperlink_terms_text:
                dialogBuilder.setTitle("Terms and Conditions");
                dialogBuilder.setMessage(R.string.lorem_para);
                dialogBuilder.create().show();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
