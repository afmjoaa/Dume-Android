package io.dume.dume.student.freeCashBack;

public interface FreeCashBackContact {

    interface View {

        void findView();

        void initFreeCashBack();

        void configFreeCashBack();

        void onAnimationImage();
    }

    interface Presenter {

        void freeCashBackEnqueue();

        void onFreeCashBackViewIntracted(android.view.View view);

    }

    interface Model {

        void freeCashBackHawwa();
    }
}
