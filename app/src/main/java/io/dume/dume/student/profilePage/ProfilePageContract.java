package io.dume.dume.student.profilePage;

public interface ProfilePageContract {
    interface View {

        void configProfilePage();

        void initProfilePage();

        void findView();

    }

    interface Presenter {

        void profilePageEnqueue();

        void onProfileViewIntracted(android.view.View view);

    }

    interface Model {

        void profilePagehawwa();
    }
}
