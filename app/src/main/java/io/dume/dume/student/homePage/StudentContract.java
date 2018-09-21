package io.dume.dume.student.homePage;


public interface StudentContract {
    interface View {
        void onSignOut();

        void goToMapView();

        void goToStudentHOmePage();

        void configView();

        void goTORecordsActivity();

        void goTORecordsActivity2();

        void goTORecordsActivity3();
    }

    interface Presenter {
        void enqueue();

        void onViewIntracted(android.view.View view);
    }

    interface Model {
        void theHawwa();
    }
}
