package io.dume.dume.student.profilePage;

public interface ProfilePageContract {
    interface View {

        void configProfilePage();

        void initProfilePage();

        void findView();

        void onGenderClicked();

        void onCurrentAddressClicked();

        void onPreviousResultClicked();
    }

    interface Presenter {

        void profilePageEnqueue();

        void onProfileViewIntracted(android.view.View view);

    }

    interface Model {

        void profilePagehawwa();
    }
}
