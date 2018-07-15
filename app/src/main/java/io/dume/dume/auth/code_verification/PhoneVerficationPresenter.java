package io.dume.dume.auth.code_verification;

import android.os.CountDownTimer;

import java.util.logging.Handler;

import io.dume.dume.auth.AuthContract;
import io.dume.dume.auth.AuthModel;

public class PhoneVerficationPresenter implements PhoneVerificationContract.Presenter {
    PhoneVerificationContract.View view;
    PhoneVerificationContract.Model model;

    public PhoneVerficationPresenter(PhoneVerificationContract.View view, PhoneVerificationContract.Model authModel) {
        this.view = view;
        this.model = authModel;
    }

    @Override
    public void enqueue() {
        view.findView();
        view.initActionBar();
        view.init();
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                view.updateTimer(l);
            }

            @Override
            public void onFinish() {
                view.onTimerCompleted();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onPinConfirm(String pin) {
        model.verifyCode(pin, new PhoneVerificationContract.Model.CodeVerificationCallBack() {
            @Override
            public void onStart() {
                view.showProgress();
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
}
