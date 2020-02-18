package io.dume.dume.auth

import android.content.Intent
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import io.dume.dume.auth.AuthGlobalContract.OnExistingUserCallback

interface AuthContract {
    interface Model : AuthGlobalContract.Model {
        fun sendCode(phoneNumber: String, listener: Callback)
        fun getIntent(): Intent

        interface Callback {
            fun onStart()
            fun onFail(error: String?)
            fun onSuccess(id: String?, forceResendingToken: ForceResendingToken?)
            fun onAutoSuccess(authResult: AuthResult?)
        }

        fun isExistingUser(phoneNumber: String, listener: OnExistingUserCallback): Boolean
    }
}