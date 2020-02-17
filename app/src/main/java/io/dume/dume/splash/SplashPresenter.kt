package io.dume.dume.splash

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import io.dume.dume.auth.AuthGlobalContract.AccountTypeFoundListener
import io.dume.dume.util.StateManager

class SplashPresenter(var view: SplashContract.View, var model: SplashContract.Model) : SplashContract.Presenter {
    override fun init(myActivity: Activity) {
        val window = myActivity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun enqueue(context: Context) {
        //  val isFirstTimeUser: Boolean = StateManager.getInstance(context).sharedPreferences().all[StateManager.FIRST_TIME_USER] as? Boolean ?: true

//        if (true) {
//            view.gotoForwardFlowActivity()
//        } else {
            val user = FirebaseAuth.getInstance().currentUser
            if (model.isUserLoggedIn()) {
                model.onAccountTypeFound(model.getUser()!!, object : AccountTypeFoundListener {
                    override fun onStart() {
                        Log.w(TAG, "onStart: ")
                    }

                    override fun onTeacherFound() {
                        model.detachListener()
                        view.gotoTeacherActivity()
                        Log.w(TAG, "onTeacherFound: ")
                    }

                    override fun onStudentFound() {
                        model.detachListener()
                        view.gotoStudentActivity()
                        Log.w(TAG, "onStudentFound: ")
                    }

                    override fun onForeignObligation() {
                        model.detachListener()
                        view.gotoForeignObligation()
                        Log.w(TAG, "onForeignObligation: ")
                    }

                    override fun onFail(exception: String) {
                        view.foundErr(exception)
                    }
                })
            } else {
                view.gotoLoginActivity()
                Log.w(TAG, "enqueue: login")
            }
        //}
    }

    companion object {
        private const val TAG = "SplashPresenter"
    }

}