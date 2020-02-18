package io.dume.dume.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import io.dume.dume.poko.MiniUser

interface AuthGlobalContract {

    interface CodeVerificationCallBack {
        fun onStart()
        fun onSuccess()
        fun onFail(error: String?)
    }
    interface CodeResponse {
        fun onStart()
        fun onFail(error: String?)
        fun onSuccess(id: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?)
        fun onAutoSuccess(authResult: AuthResult?)
    }

    interface Model {
        fun getUser(): FirebaseUser?
        fun getData(): DataStore?
    }

    interface View {
        fun gotoTeacherActivity()
        fun gotoStudentActivity()
        fun gotoForeignObligation()
    }

    interface AccountTypeFoundListener {
        fun onStart()
        fun onTeacherFound()
        fun onStudentFound()
        fun onForeignObligation()
        fun onFail(exception: String)
    }

    interface OnExistingUserCallback {
        fun onStart()
        fun onUserFound(miniUser: MiniUser? = null)
        fun onNewUserFound()
        fun onError(err: String?)
    }

}