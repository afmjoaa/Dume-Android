package io.dume.dume.auth;

import com.google.firebase.auth.FirebaseUser;

public interface AuthGlobalModel {
    boolean isUserLoggedIn();

    FirebaseUser getUser();

    void onAccountTypeFound(FirebaseUser user, AccountTypeFoundListener listener);

    interface AccountTypeFoundListener {
        void onStart();

        void onTeacherFound();

        void onStudentFound();

        void onFail(String exeption);
    }
}

