package io.dume.dume.auth.code_verification;

import android.os.CountDownTimer;

import java.util.logging.Handler;

public class PhoneVerficationPresenter implements PhoneVerificationContract.Presenter {
    PhoneVerificationContract.View view;
    PhoneVerificationContract.Model model;

    public PhoneVerficationPresenter(PhoneVerificationContract.View view, PhoneVerificationContract.Model model) {
        this.view = view;
        this.model = model;
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
        view.showProgress();
        new android.os.Handler().postDelayed(() -> {
            view.hideProgress();
            view.gotoTeacherActivity();
        }, 2000);

    }
}
