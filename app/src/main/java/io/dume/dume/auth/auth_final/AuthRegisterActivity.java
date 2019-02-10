package io.dume.dume.auth.auth_final;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.dume.dume.R;
import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.auth.auth.AuthContract;
import io.dume.dume.auth.code_verification.PhoneVerificationActivity;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.obligation.foreignObli.PayActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.student.pojo.StuBaseModel.setStuProfile;
import static io.dume.dume.util.DumeUtils.configureAppbar;
import static io.dume.dume.util.DumeUtils.hideKeyboard;

public class AuthRegisterActivity extends CustomStuAppCompatActivity {
    EditText firstname, lastName, phoneNumber;
    AutoCompleteTextView email;
    private DataStore datastore = null;
    private FirebaseFirestore fireStore;
    private Activity activity;
    private static final String TAG = "AuthRegisterActivity";
    private Context context;
    private HorizontalLoadView loadView;
    private FirebaseUser model;

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

        mentorFeild.put("gender", "");
        mentorFeild.put("religion", "");
        mentorFeild.put("birth_date", "");
        mentorFeild.put("marital", "");
        mentorFeild.put("current_status", "");

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
        selfRating.put("response_time", "90");
        selfRating.put("student_guided", "5");
        mentorFeild.put("self_rating", selfRating);



        /* Payment Section */
        Map<String, Object> paymentMap = new HashMap<>();
        paymentMap.put("obligation_amount", "0");
        paymentMap.put("obligation_currency", "BDT");
        paymentMap.put("total_paid", "0");
        mentorFeild.put("payments", paymentMap);


        List<String> appliedPromoList = new ArrayList<>();
        mentorFeild.put("applied_promo", appliedPromoList);
        ArrayList<String> availablePromoList = new ArrayList<>();
        mentorFeild.put("available_promo", availablePromoList);

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

    private void configBootCampProfile(DataStore dataStore) {
        //admin, member, viewer
        Map<String, Object> bootCampField = new HashMap<>();
        GeoPoint location = null;
        bootCampField.put("associated_uid", "");
        bootCampField.put("association_set", false);
        bootCampField.put("user_role", "Admin");

        List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
        Map<String, Object> creator = new HashMap<>();
        creator.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        creator.put("role", "Admin");
        memberList.add(creator);
        bootCampField.put("member_list", memberList);

        bootCampField.put("bootcamp_name", "");
        bootCampField.put("email", dataStore.getEmail());
        bootCampField.put("phone_number", dataStore.getPhoneNumber());
        bootCampField.put("obligation", false);
        bootCampField.put("avatar", "");
        bootCampField.put("location", location);

        bootCampField.put("account_active", true);
        bootCampField.put("pro_com_%", "60");

        bootCampField.put("unread_msg", "0");
        bootCampField.put("unread_noti", "0");
        Map<String, Object> unreadRecords = new HashMap<>();
        unreadRecords.put("pending_count", "0");
        unreadRecords.put("accepted_count", "0");
        unreadRecords.put("current_count", "0");
        unreadRecords.put("completed_count", "0");
        unreadRecords.put("rejected_count", "0");
        bootCampField.put("unread_records", unreadRecords);

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
        selfRating.put("response_time", "90");
        selfRating.put("student_guided", "5");
        bootCampField.put("self_rating", selfRating);

        List<String> appliedPromoList = new ArrayList<>();
        bootCampField.put("applied_promo", appliedPromoList);
        List<String> availablePromoList = new ArrayList<>();
        bootCampField.put("available_promo", availablePromoList);

        Map<String, Object> achievements = new HashMap<>();
        achievements.put("joined", true);
        achievements.put("inaugural", false);
        achievements.put("leading", false);
        achievements.put("premier", false);
        bootCampField.put("achievements", achievements);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
            fireStore.document("users/bootcamps/camp_profile/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).set(bootCampField).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    private Map<String, Object> generateStuProInfo(DataStore dataStore) {
        Map<String, Object> stuProInfo = new HashMap<>();
        stuProInfo.put("first_name", dataStore.getFirstName());
        stuProInfo.put("last_name", dataStore.getLastName());
        stuProInfo.put("phone_number", dataStore.getPhoneNumber());
        stuProInfo.put("avatar", dataStore.getPhotoUri());
        stuProInfo.put("email", dataStore.getEmail());
        stuProInfo.put("gender", "");
        stuProInfo.put("current_status", "");
        stuProInfo.put("previous_result", "");
        stuProInfo.put("pro_com_%", "60");
        stuProInfo.put("unread_msg", "0");
        stuProInfo.put("unread_noti", "0");
        stuProInfo.put("next_rp_write", "1");
        stuProInfo.put("next_rs_write", "1");

        GeoPoint current_address = new GeoPoint(84.9, -180);
        stuProInfo.put("current_address", current_address);

        //String[] hawwa = {"joaa", "Enam", "sumon"};Arrays.asList(hawwa);
        List<String> appliedPromoList = new ArrayList<String>();
        stuProInfo.put("applied_promo", appliedPromoList);
        List<String> availablePromoList = new ArrayList<String>();
        stuProInfo.put("available_promo", availablePromoList);

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

            DocumentReference userStudentProInfo = fireStore.collection("/users/students/stu_pro_info").document(model.getUid());
            fireStore.collection("mini_users").document(model.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    configMentorProfile(dataStore);
                    configBootCampProfile(dataStore);
                    setStuProfile((Activity) context, userStudentProInfo, generateStuProInfo(dataStore));
                    //view.hideProgress();
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
            Log.w(TAG, "saveUserToDb: " + "DataStore null or user not logged in");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_final);
        setActivityContext(this, fromFlag);
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
        model = FirebaseAuth.getInstance().getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();
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

                /*new AuthModel(this, this).isExistingUser(phoneStr, new AuthGlobalContract.OnExistingUserCallback() {
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
                                datastore.setFirstName(firstname.getText().toString());
                                datastore.setLastName(lastName.getText().toString());
                                datastore.setEmail(email.getText().toString());
                                datastore.setPhoneNumber(phoneNumber.getText().toString());
                                DataStore.resendingToken = forceResendingToken;
                                datastore.setVerificationId(verificationId);
                                DataStore.STATION = 1;
                                intent.putExtra("datastore", datastore);
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
                                        datastore.setFirstName(firstname.getText().toString());
                                        datastore.setLastName(lastName.getText().toString());
                                        datastore.setEmail(email.getText().toString());
                                        datastore.setPhoneNumber(phoneNumber.getText().toString());
                                        saveUserToDb(datastore);

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
                                datastore.setFirstName(firstname.getText().toString());
                                datastore.setLastName(lastName.getText().toString());
                                datastore.setEmail(email.getText().toString());
                                datastore.setPhoneNumber(phoneNumber.getText().toString());
                                DataStore.resendingToken = forceResendingToken;
                                datastore.setVerificationId(verificationId);
                                DataStore.setSTATION(2);
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
                });*/
                //testing my code here
                AuthModel model = new AuthModel(this, this);
                model.isExistingUser(phoneStr, new AuthGlobalContract.OnExistingUserCallback() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onUserFound() {
                        Log.w(TAG, "onUserFound: hiding dialog");
                        //view.hideProgress();
                        model.sendMessage("+88" + phoneStr, new AuthContract.Model.Callback() {
                            @Override
                            public void onStart() {
                                showDialog();
                                toast("Sending Code..");
                                Log.w(TAG, "onStart: showing dialog");
                            }

                            @Override
                            public void onFail(String error) {
                                Log.w(TAG, "onFail: hiding dialog");
                                hideDialog();
                                phoneNumber.setError(error);
                                //view.onValidationFailed(error);
                                toast(error);
                            }

                            @Override
                            public void onSuccess(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Log.w(TAG, "onSuccess: hiding dialog");
                                hideKeyboard(activity);
                                hideDialog();
                                datastore.setVerificationId(s);
                                DataStore.resendingToken = forceResendingToken;
                                datastore.setPhoneNumber(phoneStr);
                                DataStore.setSTATION(1);
                                Intent intent = new Intent(AuthRegisterActivity.this, PhoneVerificationActivity.class);
                                intent.putExtra("datastore", datastore);
                                startActivity(intent);
                                finish();
                                //view.goToVerificationActivity(dataStore);
                            }

                            @Override
                            public void onAutoSuccess(AuthResult authResult) {
                                Log.w(TAG, "onAutoSuccess: hiding dialog");
                                hideKeyboard(activity);
                                //view.hideProgress();
                                model.onAccountTypeFound(authResult.getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                                    @Override
                                    public void onStart() {
                                        Log.w(TAG, "onStart: auto success-showing dialog");
                                        showDialog();
                                    }

                                    @Override
                                    public void onTeacherFound() {
                                        Log.w(TAG, "onTeacherFound: hiding dialog");
                                        hideDialog();
                                        gotoTeacherActivity();
                                    }

                                    @Override
                                    public void onStudentFound() {
                                        Log.w(TAG, "onStudentFound: hiding dialog");
                                        hideDialog();
                                        gotoStudentActivity();
                                    }

                                    @Override
                                    public void onBootcamp() {
                                        Log.w(TAG, "onBootcampFound: hiding dialog");
                                        hideDialog();
                                        gotoTeacherActivity();
                                    }

                                    @Override
                                    public void onForeignObligation() {
                                        Log.w(TAG, "onForeignObligation: hiding dialog");
                                        hideDialog();
                                        gotoForeignObligation();
                                    }

                                    @Override
                                    public void onFail(String exeption) {
                                        Log.w(TAG, "onFail: hiding dialog");
                                        hideDialog();
                                        toast(exeption);
                                        //view.showToast(exeption);
                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void onNewUserFound() {
                        DataStore.STATION = 2;
                        Log.w(TAG, "onNewUserFound: ");
                        showDialog();
                        //testing code
                        model.sendMessage("+88" + phoneStr, new AuthContract.Model.Callback() {
                            @Override
                            public void onStart() {
                                showDialog();
                                toast("Sending Code..");
                                Log.w(TAG, "onStart: showing dialog");
                            }

                            @Override
                            public void onFail(String error) {
                                Log.w(TAG, "onFail: hiding dialog");
                                showDialog();
                                toast(error);

                            }

                            @Override
                            public void onSuccess(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Log.w(TAG, "onSuccess: hiding dialog");
                                hideKeyboard(AuthRegisterActivity.this);
                                hideDialog();
                                Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                                datastore.setFirstName(firstname.getText().toString());
                                datastore.setLastName(lastName.getText().toString());
                                datastore.setEmail(email.getText().toString());
                                datastore.setPhoneNumber(phoneNumber.getText().toString());
                                DataStore.resendingToken = forceResendingToken;
                                datastore.setVerificationId(s);
                                DataStore.setSTATION(2);
                                intent.putExtra("datastore", datastore);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onAutoSuccess(AuthResult authResult) {
                                Log.w(TAG, "onAutoSuccess: hiding dialog");
                                hideKeyboard(activity);
                                //view.hideProgress();
                                model.onAccountTypeFound(authResult.getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                                    @Override
                                    public void onStart() {
                                        Log.w(TAG, "onStart: auto success-showing dialog");
                                        showDialog();
                                    }

                                    @Override
                                    public void onTeacherFound() {
                                        Log.w(TAG, "onTeacherFound: hiding dialog");
                                        hideDialog();
                                        gotoTeacherActivity();
                                    }

                                    @Override
                                    public void onStudentFound() {
                                        Log.w(TAG, "onStudentFound: hiding dialog");
                                        hideDialog();
                                        gotoStudentActivity();
                                    }

                                    @Override
                                    public void onBootcamp() {
                                        Log.w(TAG, "onBootcampFound: hiding dialog");
                                        hideDialog();
                                        gotoTeacherActivity();
                                    }

                                    @Override
                                    public void onForeignObligation() {
                                        Log.w(TAG, "onForeignObligation: hiding dialog");
                                        hideDialog();
                                        gotoForeignObligation();
                                    }

                                    @Override
                                    public void onFail(String exeption) {
                                        Log.w(TAG, "onFail: hiding dialog");
                                        showDialog();
                                        toast(exeption);
                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void onError(String err) {
                        hideDialog();
                        toast(err);
                    }
                });


               /* AuthModel model = new AuthModel(this, this);
                model.sendMessage("+88" + phoneStr, new AuthContract.Model.Callback() {
                    @Override
                    public void onStart() {
                        showDialog();
                        toast("Sending Code..");
                        Log.w(TAG, "onStart: showing dialog");
                    }

                    @Override
                    public void onFail(String error) {
                        Log.w(TAG, "onFail: hiding dialog");
                        showDialog();
                        toast(error);

                    }

                    @Override
                    public void onSuccess(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Log.w(TAG, "onSuccess: hiding dialog");
                        hideKeyboard(AuthRegisterActivity.this);
                        hideDialog();
                        Intent intent = new Intent(getApplicationContext(), PhoneVerificationActivity.class);
                        datastore.setFirstName(firstname.getText().toString());
                        datastore.setLastName(lastName.getText().toString());
                        datastore.setEmail(email.getText().toString());
                        datastore.setPhoneNumber(phoneNumber.getText().toString());
                        DataStore.resendingToken = forceResendingToken;
                        datastore.setVerificationId(s);
                        DataStore.setSTATION(2);
                        intent.putExtra("datastore", datastore);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAutoSuccess(AuthResult authResult) {
                        Log.w(TAG, "onAutoSuccess: hiding dialog");
                        hideKeyboard(activity);
                        //view.hideProgress();
                        model.onAccountTypeFound(authResult.getUser(), new AuthGlobalContract.AccountTypeFoundListener() {
                            @Override
                            public void onStart() {
                                Log.w(TAG, "onStart: auto success-showing dialog");
                                showDialog();
                            }

                            @Override
                            public void onTeacherFound() {
                                Log.w(TAG, "onTeacherFound: hiding dialog");
                                hideDialog();
                                gotoTeacherActivity();
                            }

                            @Override
                            public void onStudentFound() {
                                Log.w(TAG, "onStudentFound: hiding dialog");
                                hideDialog();
                                gotoStudentActivity();
                            }

                            @Override
                            public void onBootcamp() {
                                Log.w(TAG, "onBootcampFound: hiding dialog");
                                hideDialog();
                                gotoTeacherActivity();
                            }

                            @Override
                            public void onForeignObligation() {
                                Log.w(TAG, "onForeignObligation: hiding dialog");
                                hideDialog();
                                gotoForeignObligation();
                            }

                            @Override
                            public void onFail(String exeption) {
                                Log.w(TAG, "onFail: hiding dialog");
                                showDialog();
                                toast(exeption);
                            }
                        });
                    }
                });*/
                break;
            case R.id.hiperlink_privacy_text:

                break;
            case R.id.hiperlink_terms_text:

                break;
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


}
