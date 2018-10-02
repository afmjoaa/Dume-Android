package io.dume.dume.student.studentSettings;

public interface StudentSettingsContract {
    interface View {

        void configStudentSettings();

        void initStudentSettings();

        void findView();

        void gotoProfilePage();
    }

    interface Presenter {

        void studentSettingsEnqueue();

        void onStudentSettingsIntracted(android.view.View view);

    }

    interface Model {

        void studentSettingshawwa();
    }
}
