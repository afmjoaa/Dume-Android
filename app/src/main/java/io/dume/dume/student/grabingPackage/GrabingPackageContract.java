package io.dume.dume.student.grabingPackage;

public interface GrabingPackageContract {
    interface View {

        void configGrabingPackagePage();

        void initGrabingPackagePage();

        void findView();

        void executeSearchActivity();

        void instantDumeSelected();

        void regularDumeSelected();

        void dumeGangSelected();
    }

    interface Presenter {

        void grabingPackagePageEnqueue();

        void onGrabingPackageViewIntracted(android.view.View view);

    }

    interface Model {

        void grabingPackagePagehawwa();
    }
}
