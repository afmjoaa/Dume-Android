package io.dume.dume.auth

import com.google.firebase.auth.FirebaseUser
import io.dume.dume.poko.MiniUser

interface AuthGlobalContract {
    interface Model {
        fun isUserLoggedIn(): Boolean
        fun getUser(): FirebaseUser?
        fun getData(): DataStore?
        fun onAccountTypeFound(user: FirebaseUser, listener: AccountTypeFoundListener)
        fun detachListener()
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