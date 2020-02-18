package io.dume.dume.splash

import android.content.Context
import io.dume.dume.auth.AuthGlobalContract.AccountTypeFoundListener
import io.dume.dume.auth.AuthModel
import io.dume.dume.util.StateManager

class SplashPresenter(var view: SplashContract.View, var model: AuthModel) : SplashContract.Presenter {

    override fun enqueue(context: Context) {
        val isFirstTimeUser: Boolean = StateManager.getInstance(context).sharedPreferences().all[StateManager.FIRST_TIME_USER] as? Boolean
                ?: true

        if (isFirstTimeUser) {
            view.gotoForwardFlowActivity()
        } else {
            if (model.isUserLoggedIn()) {
                model.onAccountTypeFound(model.getUser()!!, object : AccountTypeFoundListener {
                    override fun onStart() = run { }
                    override fun onTeacherFound() = view.gotoTeacherActivity()
                    override fun onStudentFound() = view.gotoStudentActivity()
                    override fun onForeignObligation() = view.gotoForeignObligation()
                    override fun onFail(exception: String) = run { view.foundErr(exception) }
                })
            } else view.gotoLoginActivity()
        }
    }

    companion object {
        private const val TAG = "SplashPresenter"
    }

}