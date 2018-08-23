package io.dume.dume.student.homepage;


public interface StudentContract {
    interface View {
        void onSignOut();

        void goToMapView();

        void goToStudentHOmePage();

        void configView();

    }

    interface Presenter {
        void enqueue();

        void onViewIntracted(android.view.View view);
    }

    interface Model {
        void theHawwa();
    }
}
