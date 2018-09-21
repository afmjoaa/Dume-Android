package io.dume.dume.student.studentHelp;

public interface StudentHelpContract {
    interface View {

        void configStudentHelp();

        void initStudentHelp();

        void findView();

    }

    interface Presenter {

        void studentHelpEnqueue();

        void onStudentHelpIntracted(android.view.View view);

    }

    interface Model {

        void studentHelpHawwa();
    }
}
