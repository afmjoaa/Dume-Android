package io.dume.dume.auth;

import com.google.firebase.auth.FirebaseUser;

public interface AuthGlobalContract {

    interface Model {
        boolean isUserLoggedIn();

        FirebaseUser getUser();

        DataStore getData();

        void onAccountTypeFound(FirebaseUser user, AccountTypeFoundListener listener);

        void detachListener();

    }

    interface View {

        void gotoTeacherActivity();

        void gotoStudentActivity();
    }

    interface AccountTypeFoundListener {
        void onStart();

        void onTeacherFound();

        void onStudentFound();
        void onBootcamp();

        void onFail(String exeption);
    }

    interface OnExistingUserCallback {
        void onStart();

        void onUserFound();

        void onNewUserFound();

        void onError(String err);
    }
}

