package io.dume.dume.student.grabingPackage;

public interface GrabingPackageContract {
    interface View {

        void configGrabingPackagePage();

        void initGrabingPackagePage();

        void findView();

    }

    interface Presenter {

        void grabingPackagePageEnqueue();

        void onGrabingPackageViewIntracted(android.view.View view);

    }

    interface Model {

        void grabingPackagePagehawwa();
    }
}
