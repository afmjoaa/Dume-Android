package io.dume.dume.auth.code_verification;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;


import java.util.HashMap;
import java.util.Map;

import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth.AuthContract;

public class PhoneVerficationPresenter implements PhoneVerificationContract.Presenter {
    PhoneVerificationContract.View view;
    PhoneVerificationContract.Model model;
    private CountDownTimer countDownTimer;
    private static final String TAG = "PhoneVerficationPresent";
  //  private final FirebaseFirestore fireStore;

    public PhoneVerficationPresenter(PhoneVerificationContract.View view, PhoneVerificationContract.Model authModel) {
        this.view = view;
        this.model = authModel;
      //  fireStore = FirebaseFirestore.getInstance();
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
        model.verifyCode(pin, new PhoneVerificationContract.Model.CodeVerificationCallBack() {
            @Override
            public void onStart() {
                view.showProgress("Authenticating...");
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
                view.showProgress("Resending Code");
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
                view.showProgress("Authenticating...");
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
            public void onFail(String exeption) {
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
            user.put("avatar", dataStore.getPhotoUri());
            user.put("email", dataStore.getEmail());
/*
            fireStore.collection("users").document(model.getUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
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
*/

        } else {
            Log.w(TAG, "saveUserToDb: " + "Datastore null or user not logged in");
        }

    }

}
