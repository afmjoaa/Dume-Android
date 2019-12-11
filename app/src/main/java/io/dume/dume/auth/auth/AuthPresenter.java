package io.dume.dume.auth.auth;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthProvider;

import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.DataStore;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.hideKeyboard;

public class AuthPresenter implements AuthContract.Presenter {

    private Activity activity;
    private Context context;
    AuthContract.View view;
    AuthContract.Model model;
    private Bundle mBundle;
    private final DataStore dataStore;
    private static final String TAG = "AuthPresenter";

    public AuthPresenter(Context context, AuthContract.View view, @Nullable AuthContract.Model model) {
        this.context = context;
        this.activity = (Activity) context;
        this.view = view;
        this.model = model;
        mBundle = new Bundle();
        dataStore = DataStore.getInstance();
    }

    @Override
    public void enqueue() {
        view.findView();
        view.init();
    }

    @Override
    public void onPhoneTextChange(String text) {
        if (text.length() == 11) {
            view.enableVerifyButton();
        } else view.disableVerifyButton();


    }


    @Override
    public void onPhoneValidation(String phoneNumber) {
        view.showProgress();
        if (phoneNumber.isEmpty()) {
            view.onValidationFailed("Should not be empty");
            view.hideProgress();
            return;
        } else if (!DumeUtils.isInteger(phoneNumber)) {
            view.onValidationFailed("Only Digits Allowed (0-9)");
            view.hideProgress();
            return;
        } else if (phoneNumber.length() != 11) {
            view.onValidationFailed("Should be 11 Digits");
            view.hideProgress();
            return;
        }
        view.disableVerifyButton();
        view.sending();
        DumeUtils.hideKeyboard((Activity) context);
        model.isExistingUser(phoneNumber, new AuthGlobalContract.OnExistingUserCallback() {
            @Override
            public void onStart() {
                view.showProgress();
                Log.w(TAG, "onStart: Showing dialog");
            }

            @Override
            public void onUserFound() {
                Log.w(TAG, "onUserFound: hiding dialog");
                //view.hideProgress();
                model.sendCode("+88" + phoneNumber, new AuthContract.Model.Callback() {
                    @Override
                    public void onStart() {
                        view.showProgress();
                        view.showToast("Sending Code..");
                        Log.w(TAG, "onStart: showing dialog");
                    }

                    @Override
                    public void onFail(String error) {
                        Log.w(TAG, "onFail: hiding dialog");
                        view.hideProgress();
                        view.onValidationFailed(error);
                        view.showToast(error);
                        view.enableVerifyButton();
                        view.resetSending();

                    }

                    @Override
                    public void onSuccess(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Log.w(TAG, "onSuccess: hiding dialog");
                        hideKeyboard(activity);
                        view.hideProgress();
                        dataStore.setVerificationId(s);
                        DataStore.resendingToken = forceResendingToken;
                        dataStore.setPhoneNumber(phoneNumber);
                        view.goToVerificationActivity();
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
                                view.showProgress();
                            }

                            @Override
                            public void onTeacherFound() {
                                Log.w(TAG, "onTeacherFound: hiding dialog");
                                view.hideProgress();
                                view.gotoTeacherActivity();
                            }

                            @Override
                            public void onStudentFound() {
                                Log.w(TAG, "onStudentFound: hiding dialog");
                                view.hideProgress();
                                view.gotoStudentActivity();
                            }

                            @Override
                            public void onBootcamp() {
                                Log.w(TAG, "onBootcampFound: hiding dialog");
                                view.hideProgress();
                                view.gotoTeacherActivity();
                            }

                            @Override
                            public void onForeignObligation() {
                                Log.w(TAG, "onForeignObligation: hiding dialog");
                                view.hideProgress();
                                view.gotoForeignObligation();
                            }

                            @Override
                            public void onFail(String exeption) {
                                Log.w(TAG, "onFail: hiding dialog");
                                view.hideProgress();
                                view.showToast(exeption);
                                view.enableVerifyButton();
                                view.resetSending();

                            }
                        });

                    }
                });
            }

            @Override
            public void onNewUserFound() {
                hideKeyboard(activity);
                view.hideProgress();
                Log.w(TAG, "onNewUserFound: ");
                dataStore.setPhoneNumber(phoneNumber);
                view.goToRegesterActivity();
            }

            @Override
            public void onError(String err) {
                Log.w(TAG, "onGpsError: hiding dialog");
                view.hideProgress();
                view.showToast(err);
                view.enableVerifyButton();
                view.resetSending();
            }
        });
    }


    @Override
    public void setBundle() {
       /* if (dataStore!=null) {
            view.restoreData(dataStore);
        }*/
    }


}
