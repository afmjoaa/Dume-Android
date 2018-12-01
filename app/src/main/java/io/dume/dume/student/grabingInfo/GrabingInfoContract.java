package io.dume.dume.student.grabingInfo;

public interface GrabingInfoContract {
    interface View {

        void configGrabingInfoPage();

        void initGrabingInfoPage();

        void findView();

        void viewMuskClicked();

        void fabClicked(android.view.View view);

        void selectFromContactClicked();
    }

    interface Presenter {

        void grabingInfoPageEnqueue();

        void onGrabingInfoViewIntracted(android.view.View view);

    }

    interface Model {

        void grabingInfoPagehawwa();
    }
}
