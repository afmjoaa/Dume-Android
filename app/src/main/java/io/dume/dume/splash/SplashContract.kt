package io.dume.dume.splash

import android.content.Context
import io.dume.dume.auth.AuthGlobalContract

interface SplashContract {
    interface View : AuthGlobalContract.View {
        fun foundErr(msg: String)
        fun gotoLoginActivity()
        fun gotoForwardFlowActivity()
    }

    interface Presenter {
        fun enqueue(context: Context)
    }
}