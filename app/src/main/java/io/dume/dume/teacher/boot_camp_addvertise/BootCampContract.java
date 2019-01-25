package io.dume.dume.teacher.boot_camp_addvertise;

public interface BootCampContract {
    interface View {

        void findView();

        void initBootCampAdd();

        void configBootCampAdd();

        void onAnimationImage();

    }

    interface Presenter {

        void bootCampAddEnqueue();

        void onBootCampViewIntracted(android.view.View view);

    }

    interface Model {

        void bootCampHawwa();
    }
}
