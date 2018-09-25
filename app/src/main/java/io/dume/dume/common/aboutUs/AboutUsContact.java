package io.dume.dume.common.aboutUs;

public interface AboutUsContact {

    interface View {

        void findView();

        void initAboutUs();

        void configAboutUs();

    }

    interface Presenter {

        void aboutUsEnqueue();

        void onAboutUsViewIntracted(android.view.View view);

    }

    interface Model {

        void aboutUsHawwa();
    }
}
