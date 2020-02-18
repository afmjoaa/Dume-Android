package io.dume.dume.auth

interface PhoneVerificationContract {

    interface Model : AuthGlobalContract.Model {
        fun verifyCode(code: String, listener: CodeVerificationCallBack?)
        fun onResendCode(listener: AuthContract.Model.Callback)
        interface CodeVerificationCallBack {
            fun onStart()
            fun onSuccess()
            fun onFail(error: String?)
        }
    }
}