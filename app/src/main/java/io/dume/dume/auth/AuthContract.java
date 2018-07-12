package io.dume.dume.auth;

import android.support.annotation.Nullable;
import android.view.MenuItem;

public interface AuthContract {
    interface View {
        void init();

        void initActionBar();

        void findView();

        void onStudentSelected();

        void onTeacherSelected();

        void onBootcampSelected();

        void onAppBarLayoutExpanded();

        void onAppBarLayoutCollapsed();

        void showCount(String s);

        void hideCount();

        void onValidationSuccess(String number);

        void onValidationFailed(String err);

        void goToVerificationActivity();

        void goToRegesterActivity();


    }

    interface Model {

    }

    interface Presenter {
        void enqueue();

        void onPhoneTextChange(String text);

        void onBottomNavChange(MenuItem menuItem);

        void onPhoneValidation(String phoneNumber);

        void onAppBarStateChange(AppbarStateChangeListener.State state);

        void isExistingUser(String phoneNumber);


    }
}
