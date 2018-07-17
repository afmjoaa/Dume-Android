package io.dume.dume.auth.code_verification;

import android.os.CountDownTimer;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthProvider;

import io.dume.dume.auth.auth.AuthContract;
import io.dume.dume.auth.DataStore;

public class PhoneVerficationPresenter implements PhoneVerificationContract.Presenter {
    PhoneVerificationContract.View view;
    PhoneVerificationContract.Model model;
    private CountDownTimer countDownTimer;

    public PhoneVerficationPresenter(PhoneVerificationContract.View view, PhoneVerificationContract.Model authModel) {
        this.view = view;
        this.model = authModel;
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
                view.showProgress("Authenticating");
            }

            @Override
            public void onSuccess() {
                view.hideProgress();
                view.gotoTeacherActivity();
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

            }
        });
    }

    @Override
    public void startTimer() {
        view.onTimerStarted();
        countDownTimer.start();
    }
}
