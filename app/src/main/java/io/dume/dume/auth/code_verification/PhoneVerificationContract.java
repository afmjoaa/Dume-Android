package io.dume.dume.auth.code_verification;

public interface PhoneVerificationContract {

    interface View {
        void init();

        void initActionBar();

        void findView();

        void getUpComingData();

    }

    interface Model {

    }

    interface Presenter {
        void enqueue();

    }
}
