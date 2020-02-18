package io.dume.dume.firstTimeUser

import android.app.Activity
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import io.dume.dume.R
import io.dume.dume.auth.AuthGlobalContract
import io.dume.dume.auth.AuthModel
import io.dume.dume.auth.AuthContract
import io.dume.dume.auth.PhoneVerificationContract
import io.dume.dume.poko.MiniUser
import io.dume.dume.poko.sub_pojo.Failure
import io.dume.dume.poko.sub_pojo.Success
import io.dume.dume.teacher.homepage.TeacherContract
import io.dume.dume.util.Event
import java.util.*

class ForwardFlowViewModel : ViewModel() {

    /* Live Reference */
    val role = MutableLiveData<Role>()
    val firstTimeUser = MutableLiveData<Boolean>()
    val studentCurrentPosition = MutableLiveData<ForwardFlowStatStudent>()
    val teacherCurrentPosition = MutableLiveData<ForwardFlowStatTeacher>()
    val isExiting = MutableLiveData<Event<UserResponse>>()
    val resendToken = MutableLiveData<PhoneAuthProvider.ForceResendingToken>()
    val verificationId = MutableLiveData<String>()
    val avatar = MutableLiveData<Uri>()
    val codeSent = MutableLiveData<Event<Boolean?>>(Event(false))
    val autoVerified = MutableLiveData<Event<Boolean?>>(Event(false))
    val load = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>(null)
    var phoneNumber = MutableLiveData<String>()
    var scan = MutableLiveData<Event<NID?>>(Event(null))
    var success = MutableLiveData<Event<Success<String>?>>(Event(null))
    var failure = MutableLiveData<Failure<String>>(null)

    /* Lateinit Reference */
    lateinit var repository: AuthModel
    lateinit var activity: Activity

    /* General Reference */
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
            codeSent.value = Event(true)
            resendToken.value = forceResendingToken
            verificationId.value = id
            load.value = false
        }

        override fun onAutoSuccess(authResult: AuthResult?) = run { autoVerified.value = Event(true); load.value = false }

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
     * called from [@matchCode:onSuccess] function
     *  */
    private fun isExisting(phoneNumber: String = this.phoneNumber.value!!) {
        repository.isExistingUser(phoneNumber, object : AuthGlobalContract.OnExistingUserCallback {
            override fun onStart() = run { load.value = true }
            override fun onUserFound(miniUser : MiniUser?) = run {
                isExiting.value = Event(UserResponse(UserState.USERFOUND, miniUser))
                load.value = false
            }
            override fun onNewUserFound() = run { isExiting.value = Event(UserResponse(UserState.NEWUSERFOUND,null)); load.value = false }
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

    /**
     * isLoggedIn returns true or false based on user loged status
     *  */
    fun isLoggedIn(): Boolean = repository.isUserLoggedIn()

    fun getUserUID(): String {
        return Objects.requireNonNull<FirebaseUser>(FirebaseAuth.getInstance().currentUser).getUid()
    }


    fun register(miniUser: MiniUser) {
        if (avatar.value != null) {
            repository.uploadPhoto(avatar.value!!, object : TeacherContract.Model.Listener<Uri> {
                override fun onSuccess(list: Uri?) {
                    miniUser.avatar = list.toString()
                    addUserToDatabase(miniUser)
                }
                override fun onError(msg: String?) = run { failure.postValue(Failure(msg)) }
            })
        } else {
            addUserToDatabase(miniUser)
        }
    }

    fun privacyContinue(){
        success.postValue(Event(Success("")))
    }

    private fun addUserToDatabase(miniUser: MiniUser) {
        repository.addUser(miniUser, object : TeacherContract.Model.Listener<Void> {
            override fun onSuccess(list: Void?) = run { success.postValue(Event(Success(""))) }
            override fun onError(msg: String?) = run { failure.postValue(Failure(msg)) }
        })
    }


}