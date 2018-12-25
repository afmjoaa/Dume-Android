package io.dume.dume.auth.auth_final;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.dume.dume.R;
import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.auth.code_verification.PhoneVerificationActivity;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.homePage.StudentActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class AuthRegisterActivity extends AppCompatActivity {
    EditText firstname, lastName, phoneNumber;
    AutoCompleteTextView email;
    private DataStore datastore = null;
    private FirebaseFirestore firestore;
    private Activity activity;
    private static final String TAG = "AuthRegisterActivity";
    private Context context;
    private HorizontalLoadView loadView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_final);
        init();
        if (getIntent().getSerializableExtra("datastore") != null) {
            datastore = (DataStore) getIntent().getSerializableExtra("datastore");
            restoreData(datastore);
        }
        activity = this;
    }

    private void restoreData(DataStore dataStore) {
        firstname.setText(dataStore.getFirstName() == null ? "" : dataStore.getFirstName());
        lastName.setText(dataStore.getLastName() == null ? "" : dataStore.getLastName());
        email.setText(dataStore.getEmail() == null ? "" : dataStore.getEmail());
        phoneNumber.setText(dataStore.getPhoneNumber() == null ? "" : dataStore.getPhoneNumber());
    }

    private void init() {
        context = this;
        firstname = findViewById(R.id.input_firstname);
        lastName = findViewById(R.id.input_lastname);
        email = findViewById(R.id.input_email);
        phoneNumber = findViewById(R.id.phoneNumberEditText);
        loadView = findViewById(R.id.loadView);
        firestore = FirebaseFirestore.getInstance();
        ArrayList<String> emailAddress = getEmailAddress();
        if (emailAddress.size() != 0) {
            email.setThreshold(1);
            email.setAdapter(new ArrayAdapter<String>(this, R.layout.item_layout_suggestion, R.id.suggetionTextView, emailAddress));
        }
        for (String s : emailAddress) {
            Log.w(TAG, "init: " + s);
        }
        configureAppbar(this, "Confirm your info", true);
    }

    private ArrayList<String> getEmailAddress() {
        ArrayList<String> emailArray = new ArrayList<>();
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (DumeUtils.isValidEmailAddress(account.name)) {
                emailArray.add(account.name);
            }
        }
        return emailArray;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_only_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) {
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
                } else if (!DumeUtils.isValidEmailAddress(email.getText().toString())) {
                    email.setError("Please provide a valid email address");
                    return;
                }

                new AuthModel(this, this).isExistingUser(phoneStr, new AuthGlobalContract.OnExistingUserCallback() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onUserFound() {
                        DataStore.STATION = 1;
                        //hideDialog();
                        showDialog();
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + phoneStr, 60, TimeUnit.SECONDS, activity, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                Log.w(TAG, "onVerificationCompleted: OnExistingUser");
                                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(authResultTask -> {
                                    if (authResultTask.isSuccessful()) {
                                        hideDialog();
                                        new AuthModel(activity, getApplicationContext()).onAccountTypeFound(authResultTask.getResult().getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                                            @Override
                                            public void onStart() {
                                                showDialog();
                                            }

                                            @Override
                                            public void onTeacherFound() {
                                                hideDialog();
                                                startActivity(new Intent(AuthRegisterActivity.this, TeacherActivtiy.class));
                                                finish();
                                            }

                                            @Override
                                            public void onStudentFound() {
                                                hideDialog();
                                                startActivity(new Intent(AuthRegisterActivity.this, StudentActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onBootcamp() {

                                            }

                                            @Override
                                            public void onFail(String exeption) {
                                                hideDialog();
                                                toast(exeption);
                                            }
                                        });

                                    } else if (authResultTask.getException() != null) {
                                        hideDialog();
                                        toast(authResultTask.getException().getLocalizedMessage());
                                    }

                                });
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                hideDialog();
                                Toast.makeText(AuthRegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                hideDialog();
                                Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                                datastore.setFirstName(firstname.getText().toString());
                                datastore.setLastName(lastName.getText().toString());
                                datastore.setEmail(email.getText().toString());
                                datastore.setPhoneNumber(phoneNumber.getText().toString());
                                DataStore.resendingToken = forceResendingToken;
                                datastore.setVerificationId(verificationId);
                                intent.putExtra("datastore", datastore);
                                startActivity(intent);
                                finish();

                            }
                        });


                    }

                    @Override
                    public void onNewUserFound() {
                        Log.w(TAG, "onNewUserFound: " );
                        showDialog();
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + phoneStr, 60, TimeUnit.SECONDS, activity, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                Log.w(TAG, "onVerificationCompleted: OnNewUser");
                                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(authResultTask -> {
                                    if (authResultTask.isSuccessful()) {
                                        datastore.setFirstName(firstname.getText().toString());
                                        datastore.setLastName(lastName.getText().toString());
                                        datastore.setEmail(email.getText().toString());
                                        datastore.setPhoneNumber(phoneNumber.getText().toString());
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("first_name", datastore.getFirstName());
                                        user.put("last_name", datastore.getLastName());
                                        user.put("account_major", datastore.getAccountManjor());
                                        user.put("phone_number", datastore.getPhoneNumber());
                                        user.put("avatar", datastore.getPhotoUri());
                                        user.put("email", datastore.getEmail());
                                        user.put("gender", "");
                                        user.put("marital", "");
                                        user.put("religion", "");
                                        user.put("obligation", false);
                                        user.put("referred", false);
                                        user.put("referer_id", "");
                                        user.put("user_ref_link", "");
                                        user.put("account_active", true);
                                        user.put("imei", new ArrayList<>(Arrays.asList("Device 1", "Device 2", "Device 3")));


                                        firestore.collection("users").document(Objects.requireNonNull(authResultTask.getResult()).getUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                new AuthModel(AuthRegisterActivity.this, getApplicationContext()).onAccountTypeFound(authResultTask.getResult().getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                                                    @Override
                                                    public void onStart() {

                                                    }

                                                    @Override
                                                    public void onTeacherFound() {
                                                        hideDialog();
                                                        startActivity(new Intent(AuthRegisterActivity.this, TeacherActivtiy.class));
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onStudentFound() {
                                                        hideDialog();
                                                        startActivity(new Intent(AuthRegisterActivity.this, StudentActivity.class));
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onBootcamp() {

                                                    }

                                                    @Override
                                                    public void onFail(String exeption) {
                                                        hideDialog();
                                                        toast(exeption);
                                                    }
                                                });
                                            }


                                        }).addOnFailureListener((OnFailureListener) e -> {
                                            hideDialog();
                                            toast(e.getLocalizedMessage());
                                        });

                                    } else if (authResultTask.getException() != null) {
                                        hideDialog();
                                        toast(authResultTask.getException().getLocalizedMessage());
                                    }

                                });
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                hideDialog();
                                toast(e.getLocalizedMessage());
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationId, forceResendingToken);
                                hideDialog();
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

                    }

                    @Override
                    public void onError(String err) {
                        hideDialog();
                        toast(err);
                    }
                });
                break;
            case R.id.hiperlink_privacy_text:

                break;
            case R.id.hiperlink_terms_text:

                break;
        }
    }


    public void showDialog() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    public void hideDialog() {

        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
    }

    /*void showDialog(String msg) {
        if (!((Activity) context).isFinishing()) {
            spotBuilder.setMessage(msg);
            dialog = spotBuilder.build();
            dialog.show();
            Log.w(TAG, "showDialog: ");
        }
    }

    void hideDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
            Log.w(TAG, "hideDialog: ");
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }


}
