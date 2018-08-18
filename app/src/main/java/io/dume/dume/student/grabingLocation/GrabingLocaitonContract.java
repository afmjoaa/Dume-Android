package io.dume.dume.student.grabingLocation;

public interface GrabingLocaitonContract {
    interface View {

        void configGrabingLocationPage();

        void initGrabingLocationPage();

        void findView();

    }

    interface Presenter {

        void grabingLocationPageEnqueue();

        void onGrabingLocationViewIntracted(android.view.View view);

    }

    interface Model {

        void grabingLocationPagehawwa();
    }
}
