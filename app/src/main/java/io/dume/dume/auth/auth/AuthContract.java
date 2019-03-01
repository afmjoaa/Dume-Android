package io.dume.dume.auth.auth;

import android.content.Intent;
import android.view.MenuItem;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthProvider;

import io.dume.dume.auth.AuthGlobalContract;
import io.dume.dume.auth.DataStore;

public interface AuthContract {
    interface View extends AuthGlobalContract.View {
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

        void showProgress();

        void hideProgress();

        void showToast(String toast);

        void restoreData();


    }

    interface Model extends AuthGlobalContract.Model {

        void sendMessage(String phoneNumber, Callback listener);

        Intent getIntent();

        interface Callback {
            void onStart();

            void onFail(String error);

            void onSuccess(String id, PhoneAuthProvider.ForceResendingToken forceResendingToken);

            void onAutoSuccess(AuthResult authResult);


        }

        boolean isExistingUser(String phoneNumber, AuthGlobalContract.OnExistingUserCallback listener );


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
