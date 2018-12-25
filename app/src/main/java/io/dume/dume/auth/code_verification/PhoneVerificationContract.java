package io.dume.dume.auth.code_verification;

import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.auth.AuthContract;

public interface PhoneVerificationContract {

    interface View extends AuthGlobalContract.View {
        void init();

        void initActionBar();

        void findView();

        void getUpComingData();

        void onVerificationFailed(String msg);

        void showProgress();

        void hideProgress();

        void updateTimer(long millis);

        void onTimerCompleted();

        void showToast(String toast);

        void onTimerStarted();


    }

    interface Model extends AuthGlobalContract.Model {
        void verifyCode(String code, CodeVerificationCallBack listener);

        void onResendCode(AuthContract.Model.Callback listener);

        interface CodeVerificationCallBack {
            void onStart();

            void onSuccess();

            void onFail(String error);


        }

    }

    interface Presenter {
        void enqueue();

        void onPinConfirm(String pin);

        void onResendCode();

        void startTimer();

    }
}
