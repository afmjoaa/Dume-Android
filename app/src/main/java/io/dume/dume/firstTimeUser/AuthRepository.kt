package io.dume.dume.firstTimeUser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.google.firebase.storage.FirebaseStorage
import io.dume.dume.components.library.myGeoFIreStore.GeoFirestore
import io.dume.dume.poko.MiniUser
import io.dume.dume.poko.Student
import io.dume.dume.poko.Teacher
import io.dume.dume.teacher.homepage.TeacherContract
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class AuthRepository(internal var activity: Activity, internal var context: Context) {
    private val mAuth: FirebaseAuth
    private val mIntent: Intent?
    private val firestore: FirebaseFirestore
    private var miniUserRef: CollectionReference
    private var studentUserRef: CollectionReference
    private var mentorUserRef: CollectionReference
    private var geoFirestore: GeoFirestore
    private var storage: FirebaseStorage

    init {
        mAuth = FirebaseAuth.getInstance()
        mIntent = this.activity.intent
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        miniUserRef = firestore.collection("mini_users")
        studentUserRef = firestore.collection("users/students/stu_pro_info")
        mentorUserRef = firestore.collection("users/mentors/mentor_profile")
        geoFirestore = GeoFirestore(mentorUserRef)
    }

    fun sendCode(phoneNumber: String, listener: AuthGlobalContract.CodeResponse) {
        listener.onStart()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, activity, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listener.onAutoSuccess(task.result)
                    } else if (task.isCanceled) {
                        listener.onFail("Authentication Canceled")
                    } else if (task.isComplete) {
                        Log.w(TAG, "onComplete: task completed")
                    }

                }.addOnFailureListener { e ->
                    if (e is FirebaseNoSignedInUserException) {
                        listener.onFail("Already signed in")
                    } else if (e is FirebaseTooManyRequestsException) {
                        listener.onFail("You tried too times")
                    } else if (e is FirebaseAuthInvalidUserException) {
                        listener.onFail("Invalid User")
                    }

                    listener.onFail(e.localizedMessage!! + e.javaClass.name)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                listener.onFail(e.localizedMessage)
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                listener.onSuccess(s, forceResendingToken)

                super.onCodeSent(s, forceResendingToken)
            }
        })
    }

    fun getIntent(): Intent {
        return mIntent ?: Intent()
    }

    fun isExistingUser(phoneNumber: String, listener: AuthGlobalContract.OnExistingUserCallback): Boolean {
        listener.onStart()
        firestore.collection("mini_users").whereEqualTo("phone_number", phoneNumber).get().apply {
            addOnSuccessListener {
                if (!it.documents.isEmpty()) listener.onUserFound(miniUser = it.documents[0].toObject(MiniUser::class.java))
                else listener.onNewUserFound()
            }
            addOnFailureListener { listener.onError(it.localizedMessage) }
        }
        return false
    }


    fun verifyCode(code: String, verificationId: String, listener: AuthGlobalContract.CodeVerificationCallBack) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener.onSuccess()

            } else {
                if (task.exception != null) {
                    listener.onFail(task.exception!!.localizedMessage)
                }
            }
        }.addOnFailureListener { e -> listener.onFail(e.localizedMessage) }

    }


    fun resendCode(phoneNumber: String, resendToken: ForceResendingToken?, listener: AuthGlobalContract.CodeResponse) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + phoneNumber, 60, TimeUnit.SECONDS, activity, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listener.onAutoSuccess(task.result)
                    } else if (task.isCanceled) {
                        listener.onFail("Authentication Canceled")
                    } else if (task.isComplete) {
                        Log.w(TAG, "onComplete: task completed")
                    }

                }.addOnFailureListener { e -> listener.onFail(e.localizedMessage) }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                listener.onFail(e.localizedMessage)
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                listener.onSuccess(s, forceResendingToken)
                super.onCodeSent(s, forceResendingToken)
            }
        }, resendToken)
    }


    fun isUserLoggedIn(): Boolean {
        val uid = mAuth.uid
        val foo = uid ?: "Null"
        Log.w(TAG, "isUserLoggedIn: $foo")
        return uid != null
    }

    fun getUser(): FirebaseUser? {
        return mAuth.currentUser
    }


    fun onAccountTypeFound(user: FirebaseUser, listener: AuthGlobalContract.AccountTypeFoundListener) {
        listener.onStart()
        val miniUserRef = firestore.collection("mini_users").document(user.uid)
        miniUserRef.get().apply {
            addOnSuccessListener {
                if (it != null) {
                    it.toObject(MiniUser::class.java)?.apply {
                        if (accoount_major == Role.TEACHER.flow) listener.onTeacherFound()
                        else listener.onStudentFound()
                    }
                } else listener.onFail("No user found. please reopen the app.")
            }
            addOnFailureListener { listener.onFail(it.message.toString()) }
        }
    }


    /**
     *  this functions is calling from AuthViewModel to create a new user.it performs upto three operation at once using batch write.
     *  @returns [Unit]
     *  @param user
     */

    fun addUser(user: MiniUser, listener: TeacherContract.Model.Listener<Void>) {

        val writeBatch = firestore.batch()
        val miniDocumentReference = miniUserRef.document(FirebaseAuth.getInstance().uid!!)
        val mentorDocumentReference = mentorUserRef.document(FirebaseAuth.getInstance().uid!!)
        val studentDocumentReference = studentUserRef.document(FirebaseAuth.getInstance().uid!!)

        writeBatch.set(miniDocumentReference, user)
        writeBatch.set(mentorDocumentReference, prepareStudent(miniUser = user))
        writeBatch.set(studentDocumentReference, prepareStudent(miniUser = user))
        writeBatch.commit().addOnCompleteListener { listener.onSuccess(null);geoFirestore.setLocation(FirebaseAuth.getInstance().uid, user.parmanent_location!!) }.addOnFailureListener { listener.onError(it.localizedMessage) }
    }

    /**
     *  this functions is calling from AuthViewModel to create a new [Student] Student from @param miniUser.it converts miniUser to User
     *  @returns [Student]
     *  @param miniUser
     */

    fun prepareStudent(miniUser: MiniUser): Student {
        return Student(name = miniUser.name,
                first_name = miniUser.first_name,
                last_name = miniUser.last_name,
                phone_number = miniUser.phone_number,
                avatar = miniUser.avatar,
                location = GeoPoint(miniUser.parmanent_location?.latitude!!, miniUser.parmanent_location?.longitude!!),
                email = miniUser.mail,
                birth_date = miniUser.birth_date,
                id = FirebaseAuth.getInstance().uid
        )
    }

    /**
     *  this functions is calling from AuthViewModel to create a new [Teacher] from @param miniUser.it converts miniUser to User
     *  @returns [Teacher]
     *  @param miniUser
     */

    fun prepareTeacher(miniUser: MiniUser): Teacher {
        return Teacher(name = miniUser.name,
                first_name = miniUser.first_name,
                last_name = miniUser.last_name,
                phone_number = miniUser.phone_number,
                avatar = miniUser.avatar,
                nid = miniUser.nid,
                location = GeoPoint(miniUser.parmanent_location?.latitude!!, miniUser.parmanent_location?.longitude!!),
                email = miniUser.mail,
                birth_date = miniUser.birth_date,
                id = FirebaseAuth.getInstance().uid
        )
    }

    /**
     *  upload image to firebase
     * */

    fun uploadPhoto(uri: Uri, listener: TeacherContract.Model.Listener<Uri>) {
        storage.reference.child(uri.lastPathSegment ?: Random(30).nextInt().toString()).putFile(uri)
                .apply {
                    addOnSuccessListener {
                        Log.e("debug", "url : ${it.task.result}")
                        it.storage.downloadUrl.apply {
                            addOnSuccessListener {
                                listener.onSuccess(it)
                            }
                            addOnFailureListener {
                                listener.onError(it.message)
                            }
                        }
                    }
                    addOnFailureListener {
                        Log.e("debug", "uri : ${it.message}")
                        listener.onError(it.message)
                    }
                }
    }

    fun isEducatedSync(uid: String, listener: TeacherContract.Model.Listener<Void>) {

        miniUserRef.document(uid).update("isEducated", true).addOnSuccessListener {
            listener.onSuccess(null)
        }.addOnFailureListener { listener.onError(it.localizedMessage) }
    }


    companion object {
        private val TAG = "AuthModel"
    }


}
