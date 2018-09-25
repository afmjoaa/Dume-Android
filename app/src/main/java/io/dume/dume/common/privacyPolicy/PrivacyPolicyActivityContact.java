package io.dume.dume.common.privacyPolicy;

public interface PrivacyPolicyActivityContact {

    interface View {

        void findView();

        void initPrivacyPolicy();

        void configPrivacyPolicy();

    }

    interface Presenter {

        void privacyPolicyEnqueue();

        void onPrivacyPolicyViewIntracted(android.view.View view);

    }

    interface Model {

        void privacyPolicyHawwa();
    }
}
