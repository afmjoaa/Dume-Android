package io.dume.dume.student.grabingInfo;

public interface GrabingInfoContract {
    interface View {

        void configGrabingInfoPage();

        void initGrabingInfoPage();

        void findView();

    }

    interface Presenter {

        void grabingInfoPageEnqueue();

        void onGrabingInfoViewIntracted(android.view.View view);

    }

    interface Model {

        void grabingInfoPagehawwa();
    }
}