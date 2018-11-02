package io.dume.dume.common.contactActivity;

public interface ContactActivityContact {

    interface View {

        void findView();

        void initContactActivity();

        void configContactActivity();

    }

    interface Presenter {

        void contactActivityEnqueue();

        void onContactActivityViewIntracted(android.view.View view);

    }

    interface Model {

        void contactActivityHawwa();
    }
}
