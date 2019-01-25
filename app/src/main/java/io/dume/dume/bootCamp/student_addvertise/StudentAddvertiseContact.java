package io.dume.dume.bootCamp.student_addvertise;

public interface StudentAddvertiseContact {
    interface View {

        void findView();

        void initStudentAddvertise();

        void configStudentAddvertise();

        void onAnimationImage();
    }

    interface Presenter {

        void studentAddvertiseEnqueue();

        void onStudentAddvertiseViewIntracted(android.view.View view);

    }

    interface Model {

        void studentAddvertiseHawwa();
    }
}
