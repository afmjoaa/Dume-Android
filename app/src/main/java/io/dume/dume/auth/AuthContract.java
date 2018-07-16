package io.dume.dume.auth;

import android.content.Intent;
import android.view.MenuItem;

import com.google.firebase.auth.PhoneAuthProvider;

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

        void goToVerificationActivity(DataStore dataStore);

        void goToRegesterActivity(DataStore dataStore);

        void showProgress(String titile, String message);

        void hideProgress();

        void showToast(String toast);

        void restoreData(DataStore dataStore);


    }

    interface Model {
        void sendMessage(String phoneNumber, Callback listener);

        Intent getIntent();

        interface Callback {

            void onFail(String error);

            void onSuccess(String id, PhoneAuthProvider.ForceResendingToken forceResendingToken);


        }

        boolean isExistingUser(String phoneNumber);


    }

    interface Presenter {
        void enqueue();

        void onPhoneTextChange(String text);

        void onBottomNavChange(MenuItem menuItem);

        void onPhoneValidation(String phoneNumber);

        void onAppBarStateChange(AppbarStateChangeListener.State state);


        void setBundle();


    }


}
