package io.dume.dume.splash

import android.app.Activity
import android.content.Context
import io.dume.dume.auth.AuthGlobalContract
import io.dume.dume.teacher.homepage.TeacherContract

interface SplashContract {
    interface View : AuthGlobalContract.View {
        fun foundUpdates()
        fun foundErr(msg: String)
        fun gotoLoginActivity()
        fun gotoForwardFlowActivity()
    }

    interface Model : AuthGlobalContract.Model {
        fun hasUpdate(listener: TeacherContract.Model.Listener<Boolean>)
    }

    interface Presenter {
        fun init(myActivity: Activity)
        fun enqueue(context: Context)
    }
}