package io.dume.dume.student.mentorAddvertise;

public interface MentorAddvertiseContact {

    interface View {

        void findView();

        void initMentorAddvertise();

        void configMentorAddvertise();

        void onAnimationImage();
    }

    interface Presenter {

        void mentorAddvertiseEnqueue();

        void onMentorAddvertiseViewIntracted(android.view.View view);

    }

    interface Model {

        void mentorAddvertiseHawwa();
    }
}
