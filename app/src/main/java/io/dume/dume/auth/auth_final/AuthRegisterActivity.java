package io.dume.dume.auth.auth_final;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import io.dume.dume.R;
import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.DataStore;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.foreignObligation.PayActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.student.pojo.StuBaseModel.setStuProfile;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class AuthRegisterActivity extends BaseAppCompatActivity {
    private DataStore dataStore = null;
    private FirebaseFirestore fireStore;
    private Activity activity;
    private static final String TAG = "AuthRegisterActivity";
    private Context context;
    private HorizontalLoadView loadView;
    private TextInputEditText nidNo;
    private TextInputEditText nidName;
    private TextInputEditText nidBirthDate;

    private void configMentorProfile(DataStore dataStore) {
        Map<String, Object> mentorFeild = new HashMap<>();
        GeoPoint location = null;
        mentorFeild.put("first_name", dataStore.getFirstName());
        mentorFeild.put("last_name", dataStore.getLastName());
        mentorFeild.put("email", dataStore.getEmail());
        mentorFeild.put("phone_number", dataStore.getPhoneNumber());
        mentorFeild.put("obligation", false);
        mentorFeild.put("avatar", dataStore.getPhotoUri());
        mentorFeild.put("location", location);
        mentorFeild.put("penalty", 0);

        mentorFeild.put("gender", "");
        mentorFeild.put("religion", "");
        mentorFeild.put("birth_date", "");
        mentorFeild.put("marital", "");
        mentorFeild.put("current_status_icon", "");

        mentorFeild.put("referred", false);
        mentorFeild.put("referer_id", "");
        mentorFeild.put("user_ref_link", "");

        mentorFeild.put("account_active", true);
        mentorFeild.put("pro_com_%", "40");

        mentorFeild.put("unread_msg", "0");
        mentorFeild.put("unread_noti", "0");
        Map<String, Object> unreadRecords = new HashMap<>();
        unreadRecords.put("pending_count", "0");
        unreadRecords.put("accepted_count", "0");
        unreadRecords.put("current_count", "0");
        unreadRecords.put("completed_count", "0");
        unreadRecords.put("rejected_count", "0");
        mentorFeild.put("unread_records", unreadRecords);

        mentorFeild.put("daily_i", "0");
        mentorFeild.put("daily_r", "0");
        mentorFeild.put("p_daily_i", "0");
        mentorFeild.put("p_daily_r", "0");

        Map<String, Object> selfRating = new HashMap<>();
        selfRating.put("star_rating", "5.00");
        selfRating.put("star_count", "1");
        selfRating.put("l_communication", "1");
        selfRating.put("dl_communication", "0");
        selfRating.put("l_behaviour", "1");
        selfRating.put("dl_behaviour", "0");
        selfRating.put("l_expertise", "1");
        selfRating.put("dl_expertise", "0");
        selfRating.put("l_experience", "1");
        selfRating.put("dl_experience", "0");
        selfRating.put("response_time", "0");
        selfRating.put("student_guided", "0");
        mentorFeild.put("self_rating", selfRating);

        /* Payment Section */
        Map<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("obligation_amount", "0");
        paymentMap.put("obligation_currency", "BDT");
        paymentMap.put("total_paid", "0");
        paymentMap.put("penalty_paid", "0");
        mentorFeild.put("payments", paymentMap);


        List<String> appliedPromoList = new ArrayList<>();
        mentorFeild.put("applied_promo", appliedPromoList);
        ArrayList<String> availablePromoList = new ArrayList<>();
        mentorFeild.put("available_promo", availablePromoList);

        List<String> rating_array = new ArrayList<>();
        mentorFeild.put("rating_array", rating_array);

        Map<String, Object> achievements = new HashMap<>();
        achievements.put("joined", true);
        achievements.put("inaugural", false);
        achievements.put("leading", false);
        achievements.put("premier", false);
        mentorFeild.put("achievements", achievements);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            fireStore.document("users/mentors/mentor_profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).set(mentorFeild).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.w(TAG, "onSuccess: Teacher Dummy Profile Created");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onSuccess: Teacher Dummy Profile Creation Failed");
                }
            });
        }
    }

    private void nextActivity() {
        new AuthModel(activity, context).onAccountTypeFound(FirebaseAuth.getInstance().getCurrentUser(), new AuthGlobalContract.AccountTypeFoundListener() {
            @Override
            public void onStart() {
                showDialog();
                //"Authenticating..."
            }

            @Override
            public void onTeacherFound() {
                hideDialog();
                gotoTeacherActivity();
            }

            @Override
            public void onStudentFound() {
                hideDialog();
                gotoStudentActivity();
            }

            @Override
            public void onBootcamp() {
                //view.hideProgress();
                Log.w(TAG, "onBootcamp: " + "Hurrrrreh Its Bootcamp");
            }

            @Override
            public void onForeignObligation() {
                hideDialog();
                gotoForeignObligation();
            }

            @Override
            public void onFail(String exeption) {
                hideDialog();
                toast(exeption);
            }
        });
    }


    private Map<String, Object> generateStuProInfo(DataStore dataStore) {
        Map<String, Object> stuProInfo = new HashMap<>();
        stuProInfo.put("first_name", dataStore.getFirstName());
        stuProInfo.put("last_name", dataStore.getLastName());
        stuProInfo.put("phone_number", dataStore.getPhoneNumber());
        stuProInfo.put("avatar", dataStore.getPhotoUri());
        stuProInfo.put("email", dataStore.getEmail());
        stuProInfo.put("gender", "");
        stuProInfo.put("current_status_icon", "");
        stuProInfo.put("previous_result", "");
        stuProInfo.put("pro_com_%", "60");
        stuProInfo.put("unread_msg", "0");
        stuProInfo.put("unread_noti", "0");
        stuProInfo.put("next_rp_write", "1");
        stuProInfo.put("next_rs_write", "1");
        stuProInfo.put("penalty", 0);

        //GeoPoint current_address = new GeoPoint(84.9, -180);
        stuProInfo.put("current_address", null);

        //String[] hawwa = {"joaa", "Enam", "sumon"};Arrays.asList(hawwa);
        List<String> appliedPromoList = new ArrayList<String>();
        stuProInfo.put("applied_promo", appliedPromoList);
        List<String> availablePromoList = new ArrayList<String>();
        stuProInfo.put("available_promo", availablePromoList);
        List<String> rating_array = new ArrayList<>();
        stuProInfo.put("rating_array", rating_array);

        Map<String, Object> selfRating = new HashMap<>();
        selfRating.put("star_rating", "5.00");
        selfRating.put("star_count", "1");
        selfRating.put("l_communication", "1");
        selfRating.put("dl_communication", "0");
        selfRating.put("l_behaviour", "1");
        selfRating.put("dl_behaviour", "0");
        stuProInfo.put("self_rating", selfRating);

        Map<String, Object> unreadRecords = new HashMap<>();
        unreadRecords.put("pending_count", "0");
        unreadRecords.put("accepted_count", "0");
        unreadRecords.put("current_count", "0");
        unreadRecords.put("completed_count", "0");
        unreadRecords.put("rejected_count", "0");
        stuProInfo.put("unread_records", unreadRecords);

        Map<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("obligation_amount", "0");
        paymentMap.put("obligation_currency", "BDT");
        paymentMap.put("total_paid", "0");
        paymentMap.put("penalty_paid", "0");
        stuProInfo.put("payments", paymentMap);

        Map<String, Map<String, Object>> favorites = new HashMap<>();
        stuProInfo.put("favourite_places", favorites);
        Map<String, Map<String, Object>> savedPlaces = new HashMap<>();
        stuProInfo.put("saved_places", savedPlaces);
        Map<String, Map<String, Object>> recentlyUsedPlaces = new HashMap<>();
        stuProInfo.put("recent_places", recentlyUsedPlaces);
        Map<String, Map<String, Object>> recentlySearched = new HashMap<>();
        stuProInfo.put("recent_search", recentlySearched);

        stuProInfo.put("referred", false);
        stuProInfo.put("referer_id", "");
        stuProInfo.put("user_ref_link", "");
        return stuProInfo;
    }

    private void saveUserToDb(DataStore dataStore) {
        if (dataStore != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            Map<String, Object> user = new HashMap<>();
            user.put("first_name", dataStore.getFirstName());
            user.put("last_name", dataStore.getLastName());
            user.put("account_major", dataStore.getAccountManjor());
            user.put("phone_number", dataStore.getPhoneNumber());
            user.put("obligation", false);

            if (dataStore.getEmail() != null) {
                user.put("email", dataStore.getEmail());
            }
            user.put("imei", DumeUtils.getImei(this));
            showDialog();
            user.put("foreign_obligation", dataStore.isObligation());
            user.put("obligated_user", dataStore.getObligatedUser());

            String uid = FirebaseAuth.getInstance().getUid();
            if (uid == null) {
                return;
            }
            DocumentReference userStudentProInfo = fireStore.collection("/users/students/stu_pro_info").document(uid);
            fireStore.collection("mini_users").document(FirebaseAuth.getInstance().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    configMentorProfile(dataStore);
                    setStuProfile((Activity) context, userStudentProInfo, generateStuProInfo(dataStore));
                    nextActivity();
                    Log.w(TAG, "onComplete: User Added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toast("Network error !!");
                    Log.w(TAG, "onFailure: User Not Added  " + e.getLocalizedMessage());
                }
            });
        } else {
            toast("Unknown error 101!!");
            Log.w(TAG, "saveUserToDb: " + "DataStore null or isExiting not logged in");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_final);
        setActivityContext(this, fromFlag);
        setDarkStatusBarIcon();
        init();
        dataStore = DataStore.getInstance();
        restoreData();
        activity = this;
    }

    private void restoreData() {
        //get bundle data and show them
        Long NIDNo = getIntent().getExtras().getLong("NIDNo");
        String NIDName = getIntent().getExtras().getString("NIDName");
        String NIDBirthDate = getIntent().getExtras().getString("NIDBirthDate");

        nidNo.setText(NIDNo.toString() == null ? "" : NIDNo.toString());
        nidName.setText(NIDName == null ? "" : NIDName);
        nidBirthDate.setText(NIDBirthDate == null ? "" : NIDBirthDate.replace("Date of Birth ", ""));
    }

    private void init() {
        context = this;
        loadView = findViewById(R.id.loadView);
        nidNo = findViewById(R.id.nidNo);
        nidName = findViewById(R.id.name);
        nidBirthDate = findViewById(R.id.birthDate);

        fireStore = FirebaseFirestore.getInstance();
        ArrayList<String> emailAddress = getEmailAddress();
        if (emailAddress.size() != 0) {
           /* email.setThreshold(1);
            email.setAdapter(new ArrayAdapter<String>(this, R.layout.item_layout_suggestion, R.id.suggetionTextView, emailAddress));*/
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
           onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onViewClick(View view) {
        switch (view.getId()) {
            /*case R.id.floating_button:
                if (firstname.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || phoneNumber.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Hey Man !!! Fill up all the data...", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!privacyCB.isChecked()){
                    Toast.makeText(this, "Please check the confirmation box...", Toast.LENGTH_SHORT).show();
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

                                        new AuthModel(activity, getApplicationContext()).onAccountTypeFound(authResultTask.getResult().isExiting(), new AuthGlobalContract.AccountTypeFoundListener() {
                                            @Override
                                            public void onStart() {
                                                showDialog();
                                            }

                                            @Override
                                            public void onTeacherFound() {
                                                hideKeyboard(AuthRegisterActivity.this);
                                                hideDialog();
                                                startActivity(new Intent(AuthRegisterActivity.this, TeacherActivtiy.class));
                                                finish();
                                            }

                                            @Override
                                            public void onStudentFound() {
                                                hideKeyboard(AuthRegisterActivity.this);
                                                hideDialog();
                                                startActivity(new Intent(AuthRegisterActivity.this, HomePageActivity.class));
                                                finish();
                                            }

                                            @Override
                                            public void onBootcamp() {
                                                hideKeyboard(AuthRegisterActivity.this);
                                                hideDialog();
                                                startActivity(new Intent(AuthRegisterActivity.this, TeacherActivtiy.class));
                                                finish();
                                            }

                                            @Override
                                            public void onForeignObligation() {
                                                hideKeyboard(AuthRegisterActivity.this);
                                                hideDialog();
                                                startActivity(new Intent(AuthRegisterActivity.this, PayActivity.class));
                                                finish();
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
                                hideKeyboard(AuthRegisterActivity.this);
                                hideDialog();
                                Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                                dataStore.setFirstName(firstname.getText().toString());
                                dataStore.setLastName(lastName.getText().toString());
                                dataStore.setEmail(email.getText().toString());
                                dataStore.setPhoneNumber(phoneNumber.getText().toString());
                                DataStore.resendingToken = forceResendingToken;
                                dataStore.setVerificationId(verificationId);
                                DataStore.STATION = 1;
                                startActivity(intent);
                                finish();
                            }
                        });


                    }

                    @Override
                    public void onNewUserFound() {
                        DataStore.STATION = 2;
                        Log.w(TAG, "onNewUserFound: ");
                        showDialog();
                        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + phoneStr, 60, TimeUnit.SECONDS, activity, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                Log.w(TAG, "onVerificationCompleted: OnNewUser");
                                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(authResultTask -> {
                                    if (authResultTask.isSuccessful()) {
                                        dataStore.setFirstName(firstname.getText().toString());
                                        dataStore.setLastName(lastName.getText().toString());
                                        dataStore.setEmail(email.getText().toString());
                                        dataStore.setPhoneNumber(phoneNumber.getText().toString());
                                        saveUserToDb(dataStore);

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
                                hideKeyboard(AuthRegisterActivity.this);
                                hideDialog();
                                Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                                dataStore.setFirstName(firstname.getText().toString());
                                dataStore.setLastName(lastName.getText().toString());
                                dataStore.setEmail(email.getText().toString());
                                dataStore.setPhoneNumber(phoneNumber.getText().toString());
                                DataStore.resendingToken = forceResendingToken;
                                dataStore.setVerificationId(verificationId);
                                DataStore.setSTATION(2);
                                intent.putExtra("datastore", dataStore);
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
            case R.id.hiperlink_terms_text:
                startActivity(new Intent(AuthRegisterActivity.this, PrivacyPolicyActivity.class).setAction("fromHelp"));
                break;*/
        }
    }

    public void gotoTeacherActivity() {
        startActivity(new Intent(this, TeacherActivtiy.class));
        finish();
    }


    public void gotoStudentActivity() {
        startActivity(new Intent(this, HomePageActivity.class));
        finish();
    }


    public void gotoForeignObligation() {
        startActivity(new Intent(this, PayActivity.class));
        finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }


    public void registerBtnClicked(View view) {
    }
}
