package io.dume.dume.auth.code_verification;

public interface PhoneVerificationContract {

    interface View {
        void init();

        void initActionBar();

        void findView();

        void getUpComingData();

        void onVerificationFailed(String msg);

        void gotoTeacherActivity();

        void gotoStudentActivity();

        void showProgress();

        void hideProgress();

        void updateTimer(long millis);

        void onTimerCompleted();


    }

    interface Model {

    }

    interface Presenter {
        void enqueue();

        void onPinConfirm(String pin);

    }
}
