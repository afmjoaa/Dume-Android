package io.dume.dume.splash

import android.content.Context
import io.dume.dume.firstTimeUser.AuthGlobalContract

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