package io.dume.dume.auth.code_verification;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthContract;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.student.pojo.StuBaseModel.setStuProfile;

public class PhoneVerficationPresenter implements PhoneVerificationContract.Presenter {

    PhoneVerificationContract.View view;
    PhoneVerificationContract.Model model;
    private CountDownTimer countDownTimer;
    private static final String TAG = "PhoneVerficationPresent";
    private final FirebaseFirestore fireStore;
    private Context context;
    private final ArrayList<String> imeiList;

    public PhoneVerficationPresenter(Context context, PhoneVerificationContract.View view, PhoneVerificationContract.Model authModel) {
        this.context = context;
        this.view = view;
        this.model = authModel;
        fireStore = FirebaseFirestore.getInstance();
        imeiList = DumeUtils.getImei(context);

    }

    @Override
    public void enqueue() {
        view.findView();
        view.initActionBar();
        view.init();
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                view.updateTimer(l);
            }

            @Override
            public void onFinish() {
                view.onTimerCompleted();
            }
        };
        startTimer();

    }

    @Override
    public void onPinConfirm(String pin) {
        if (pin.isEmpty() || !DumeUtils.isInteger(pin)) {
            view.showToast("Enter a valid pin");
            return;
        }
        model.verifyCode(pin, new PhoneVerificationContract.Model.CodeVerificationCallBack() {
            @Override
            public void onStart() {
                view.showProgress();
                //"Authenticating..."
            }

            @Override
            public void onSuccess() {
                view.hideProgress();
                if (DataStore.STATION == 2) {
                    saveUserToDb(model.getData());
                } else {
                    nextActivity();
                }
            }

            @Override
            public void onFail(String error) {
                view.hideProgress();
                view.onVerificationFailed(error);
            }
        });
    }

    @Override
    public void onResendCode() {

        model.onResendCode(new AuthContract.Model.Callback() {
            @Override
            public void onStart() {
                view.showProgress();
                Log.w(TAG, "onStart: ");
                //"Resending Code"
            }

            @Override
            public void onFail(String error) {
                view.hideProgress();
                view.showToast(error);
            }

            @Override
            public void onSuccess(String id, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                view.hideProgress();
                view.showToast("Code sent. Please check your mobile phone");
                DataStore.resendingToken = forceResendingToken;
                startTimer();
            }

            @Override
            public void onAutoSuccess(AuthResult authResult) {
                view.hideProgress();
                if (DataStore.STATION == 2) {
                    saveUserToDb(model.getData());
                } else {
                    nextActivity();
                }

            }
        });
    }

    private void nextActivity() {
        model.onAccountTypeFound(FirebaseAuth.getInstance().getCurrentUser(), new AuthGlobalContract.AccountTypeFoundListener() {
            @Override
            public void onStart() {
                view.showProgress();
                //"Authenticating..."
            }

            @Override
            public void onTeacherFound() {
                view.hideProgress();
                view.gotoTeacherActivity();
            }

            @Override
            public void onStudentFound() {
                view.hideProgress();
                view.gotoStudentActivity();
            }

            @Override
            public void onBootcamp() {

            }

            @Override
            public void onFail(String exeption) {
                view.hideProgress();
                view.showToast(exeption);
            }
        });
    }

    @Override
    public void startTimer() {
        view.onTimerStarted();
        countDownTimer.start();
    }


    private void saveUserToDb(DataStore dataStore) {
        if (dataStore != null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            Map<String, Object> user = new HashMap<>();
            user.put("first_name", dataStore.getFirstName());
            user.put("last_name", dataStore.getLastName());
            user.put("account_major", dataStore.getAccountManjor());
            user.put("phone_number", dataStore.getPhoneNumber());
            if (dataStore.getEmail() != null) {
                user.put("email", dataStore.getEmail());
            }
            user.put("imei", imeiList);
            view.showProgress();

            DocumentReference userStudentProInfo = fireStore.collection("/users/students/stu_pro_info").document(model.getUser().getUid());
            fireStore.collection("mini_users").document(model.getUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    configMentorProfile(dataStore);
                    configBootCampProfile(dataStore);
                    setStuProfile((Activity) context, userStudentProInfo, generateStuProInfo(dataStore));
                    view.hideProgress();
                    nextActivity();
                    Log.w(TAG, "onComplete: User Added");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "onFailure: User Not Added  " + e.getLocalizedMessage());
                }
            });
        } else {
            Log.w(TAG, "saveUserToDb: " + "Datastore null or user not logged in");
        }
    }

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
        mentorFeild.put("pro_com_%", "60");

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
        mentorFeild.put("self_rating", selfRating);

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

        Map<String,Map<String, Object>> favorites =new HashMap<>();
        stuProInfo.put("favourite_places", favorites);
        Map<String,Map<String, Object>> savedPlaces = new HashMap<>();
        stuProInfo.put("saved_places", savedPlaces);
        Map<String,Map<String, Object>> recentlyUsedPlaces =new HashMap<>();
        stuProInfo.put("recent_places", recentlyUsedPlaces);

        stuProInfo.put("referred", false);
        stuProInfo.put("referer_id", "");
        stuProInfo.put("user_ref_link", "");
        return stuProInfo;
    }
}
