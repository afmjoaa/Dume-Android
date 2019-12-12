package io.dume.dume.firstTimeUser

import android.app.Activity
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthProvider
import io.dume.dume.auth.AuthGlobalContract
import io.dume.dume.auth.AuthModel
import io.dume.dume.auth.auth.AuthContract
import io.dume.dume.auth.code_verification.PhoneVerificationContract

class ForwardFlowViewModel : ViewModel() {

    /* Live Reference */
    val role = MutableLiveData<Role>()
    val firstTimeUser = MutableLiveData<Boolean>()
    val studentCurrentPosition = MutableLiveData<ForwardFlowStatStudent>()
    val teacherCurrentPosition = MutableLiveData<ForwardFlowStatTeacher>()
    val isExiting = MutableLiveData<UserState>()
    val resendToken = MutableLiveData<PhoneAuthProvider.ForceResendingToken>()
    val verificationId = MutableLiveData<String>()
    val codeSent = MutableLiveData<Boolean>(false)
    val autoVerified = MutableLiveData<Boolean>(false)
    val load = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>(null)
    var phoneNumber = MutableLiveData<String>()
    lateinit var activity: Activity
    var menu = MutableLiveData<MenuItem>()
    var scan = MutableLiveData<NID>()


    /* General Reference */
    lateinit var repository: AuthModel

    fun updateRole(roleOne: Role) = role.postValue(roleOne)
    fun updateFirstTimeUser(status: Boolean) = firstTimeUser.postValue(status)
    fun updateStudentCurrentPosition(forwardFlowStatStudent: ForwardFlowStatStudent) = studentCurrentPosition.postValue(forwardFlowStatStudent)
    fun updateTeacherCurrentPosition(forwardFlowStatTeacher: ForwardFlowStatTeacher) = teacherCurrentPosition.postValue(forwardFlowStatTeacher)


    fun inject(activity: Activity) {
        this.activity = activity
        repository = AuthModel(activity, activity)
    }

    /**
     *  <p> listener to observe data from repository @link Model#sendCode </p>
     *  */
    var repositoryListener = object : AuthContract.Model.Callback {
        override fun onStart() = run { load.value = true }
        override fun onFail(error: String?) = run { load.value = false; this@ForwardFlowViewModel.error.value = error }
        override fun onSuccess(id: String?, forceResendingToken: PhoneAuthProvider.ForceResendingToken?) = run {
            codeSent.value = true
            resendToken.value = forceResendingToken
            verificationId.value = id
            load.value = false
        }

        override fun onAutoSuccess(authResult: AuthResult?) = run { autoVerified.value = true; load.value = false }

    }

    /**
     *  called when user enter pin code and pressed verify button
     *  */
    fun login(phoneNumber: String) {
        this.phoneNumber.value = phoneNumber
        repository.sendCode("+88" + phoneNumber, repositoryListener)
    }


    /**
     *  called when user presses resend code button
     *  */
    fun resendCode(phoneNumber: String = this.phoneNumber.value!!) {
        repository.resendCode(phoneNumber, resendToken.value, repositoryListener)
    }

    /**
     * called from @matchCode:onSuccess function
     *  */
    private fun isExisting(phoneNumber: String = this.phoneNumber.value!!) {
        repository.isExistingUser(phoneNumber, object : AuthGlobalContract.OnExistingUserCallback {
            override fun onStart() = run { load.value = true }
            override fun onUserFound() = run { isExiting.value = UserState.USERFOUND; load.value = false }
            override fun onNewUserFound() = run { isExiting.value = UserState.NEWUSERFOUND; load.value = false }
            override fun onError(err: String?) = run { error.value = err; load.value = false }
        })
    }

    /**
     *  called when user enters pin code and presses verify button
     *  */
    fun matchCode(code: String, verificationId: String = this.verificationId.value!!) {
        repository.verifyCode(code, verificationId, object : PhoneVerificationContract.Model.CodeVerificationCallBack {
            override fun onStart() = run { load.value = true }
            override fun onSuccess() = run { load.value = false; isExisting() }
            override fun onFail(error: String?) = run { load.value = false; this@ForwardFlowViewModel.error.value = error }

        })
    }

    /** isLoggedIn returns true or false based on user loged status*/
    fun isLoggedIn(): Boolean = repository.isUserLoggedIn


}