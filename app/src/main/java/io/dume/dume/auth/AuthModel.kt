package io.dume.dume.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.firestore.*
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.google.firebase.storage.FirebaseStorage
import io.dume.dume.BuildConfig
import io.dume.dume.library.myGeoFIreStore.GeoFirestore
import io.dume.dume.poko.MiniUser
import io.dume.dume.poko.User
import io.dume.dume.splash.SplashActivity
import io.dume.dume.splash.SplashContract
import io.dume.dume.teacher.homepage.TeacherContract
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.Google
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class AuthModel(internal var activity: Activity, internal var context: Context) : AuthContract.Model, SplashContract.Model, PhoneVerificationContract.Model {
    private val mAuth: FirebaseAuth
    private val mIntent: Intent?
    private var datastore: DataStore? = null
    private val firestore: FirebaseFirestore
    private val listenerRegistration: ListenerRegistration? = null
    private var listenerRegistration1: ListenerRegistration? = null
    private var listenerRegistration2: ListenerRegistration? = null
    private var obligation: Boolean? = null
    private var foreignObligation: Boolean? = null
    private var miniUserRef: CollectionReference
    private var studentUserRef: CollectionReference
    private var mentorUserRef: CollectionReference
    private var geoFirestore: GeoFirestore
    private var storage: FirebaseStorage

    init {
        mAuth = FirebaseAuth.getInstance()
        mIntent = this.activity.intent
        datastore = DataStore.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        miniUserRef = firestore.collection("mini_users")
        studentUserRef = firestore.collection("users/students/stu_pro_info")
        mentorUserRef = firestore.collection("users/mentors/mentor_profile")
        geoFirestore = GeoFirestore(mentorUserRef)

        Log.w(TAG, "AuthModel: " + firestore.hashCode())
    }

    override fun sendCode(phoneNumber: String, listener: AuthContract.Model.Callback) {
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

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                listener.onSuccess(s, forceResendingToken)

                super.onCodeSent(s, forceResendingToken)
            }
        })
    }

    override fun getIntent(): Intent {
        return mIntent ?: Intent()
    }

    override fun isExistingUser(phoneNumber: String, listener: AuthGlobalContract.OnExistingUserCallback): Boolean {
        Log.w(TAG, "isExistingUser: ")
        listener.onStart()
        listenerRegistration1 = firestore.collection("mini_users").whereEqualTo("phone_number", phoneNumber).addSnapshotListener { queryDocumentSnapshots, e ->
            var documents: List<DocumentSnapshot>? = null
            detachListener()
            if (queryDocumentSnapshots != null) {
                documents = queryDocumentSnapshots.documents

                if (documents.isNotEmpty()) {
                    val miniUser = documents[0].toObject(MiniUser::class.java)
                    datastore!!.documentSnapshot = documents[0].data
                    val obligation = documents[0].data!!["obligation"] as Boolean
                    Google.getInstance().isObligation = obligation
                    listener.onUserFound(miniUser)

                } else {
                    //   listener.onNewUserFound();
                    checkImei(object : TeacherContract.Model.Listener<QuerySnapshot> {
                        override fun onSuccess(list: QuerySnapshot) = if (list.documents.size > 0) {
                            /*context.startActivity(new Intent(context, PayActivity.class));*/
                            var obligation = false
                            val obligatedUser = HashMap<String, Map<String, Any>>()
                            for (i in 0 until list.documents.size) {
                                obligation = list.documents[i].data!!["obligation"] as Boolean
                                if (obligation) {
                                    obligatedUser[list.documents[i].id] = list.documents[i].data as Map<String, Any>
                                }
                            }
                            if (obligatedUser.size > 0) {
                                DataStore.getInstance().isObligation = true
                                DataStore.getInstance().obligatedUser = obligatedUser
                            } else
                                DataStore.getInstance().isObligation = false
                            listener.onNewUserFound()

                        } else {
                            listener.onNewUserFound()
                        }

                        override fun onError(msg: String) {
                            listener.onError(msg)
                        }
                    })
                }
            } else {
                listener.onError("Internal Error. No queryDocumentSpanshot Returned By Google")
            }
            if (e != null) {
                listener.onError(e.localizedMessage)
            }
        }

        return false
    }

    fun checkImei(imeiFound: TeacherContract.Model.Listener<QuerySnapshot>) {
        var firstIMEI: String? = null
        try {
            firstIMEI = DumeUtils.getImei(context)[0]
        } catch (error: Exception) {
            Log.w(TAG, "checkImei: " + error.localizedMessage!!)
        } finally {
            if (firstIMEI == null) {
                firstIMEI = "111111111111111"
            }
        }
        listenerRegistration2 = firestore.collection("mini_users").whereArrayContains("imei", firstIMEI).addSnapshotListener { queryDocumentSnapshots, e ->
            if (queryDocumentSnapshots != null) {
                imeiFound.onSuccess(queryDocumentSnapshots)
                Log.e(TAG, "onEvent: " + queryDocumentSnapshots.documents.toString())

            } else {
                Log.e(TAG, "onEvent: Balda KIchui Painai")
                imeiFound.onError(if (e == null) "Baler Erro" else e.localizedMessage)
            }
            listenerRegistration2!!.remove()
        }
    }

    override fun verifyCode(code: String, listener: PhoneVerificationContract.Model.CodeVerificationCallBack?) {
        if (listener != null && datastore != null) {
            listener.onStart()
            val credential = PhoneAuthProvider.getCredential(datastore!!.verificationId, code)
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
    }


    fun verifyCode(code: String, verificationId: String, listener: PhoneVerificationContract.Model.CodeVerificationCallBack) {

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

    override fun onResendCode(listener: AuthContract.Model.Callback) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + datastore!!.phoneNumber, 60, TimeUnit.SECONDS, activity, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                listener.onSuccess(s, forceResendingToken)
                super.onCodeSent(s, forceResendingToken)
            }
        }, DataStore.resendingToken)
    }

    fun resendCode(phoneNumber: String, resendToken: ForceResendingToken?, listener: AuthContract.Model.Callback) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + datastore!!.phoneNumber, 60, TimeUnit.SECONDS, activity, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
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

            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                listener.onSuccess(s, forceResendingToken)
                super.onCodeSent(s, forceResendingToken)
            }
        }, resendToken)
    }


    override fun isUserLoggedIn(): Boolean {
        val uid = mAuth.uid
        val foo = uid ?: "Null"
        Log.w(TAG, "isUserLoggedIn: $foo")
        return uid != null
    }

    override fun getUser(): FirebaseUser? {
        return mAuth.currentUser
    }

    override fun getData(): DataStore? {
        return datastore
    }

    override fun onAccountTypeFound(user: FirebaseUser, listener: AuthGlobalContract.AccountTypeFoundListener) {
        listener.onStart()
        val mini_users = firestore.collection("mini_users").document(user.uid)
        mini_users.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Document found in the offline cache
                val documentSnapshot = task.result
                if (documentSnapshot != null) {
                    foreignObligation = documentSnapshot.getBoolean("foreign_obligation")
                    obligation = documentSnapshot.getBoolean("obligation")
                    //TODO solving err
                    if (foreignObligation == null) {
                        foreignObligation = false
                    }
                    if (obligation == null) {
                        obligation = false
                    }
                    Google.getInstance().isObligation = obligation!!
                    detachListener()
                    val o = documentSnapshot.get("account_major")
                    if (datastore != null && datastore!!.isBottomNavAccountMajor) {
                        if (datastore!!.accountManjor != null) {
                            datastore!!.isBottomNavAccountMajor = false
                            val newMap = HashMap<String, Any>()
                            newMap["account_major"] = datastore!!.accountManjor
                            val mini_users1 = mini_users.update(newMap).addOnFailureListener { e -> Log.e(TAG, "onFailure: Enam " + e.localizedMessage!!) }.addOnCompleteListener { taskOne ->

                                Log.e(TAG, "addOnCompleteListener: Enam ")
                                var account_major: String? = ""
                                account_major = datastore!!.accountManjor
                                assert(account_major != null)
                                if (!foreignObligation!!) {
                                    if (account_major == "student") {
                                        Google.getInstance().accountMajor = DumeUtils.STUDENT
                                        listener.onStudentFound()
                                    } else {
                                        Google.getInstance().accountMajor = DumeUtils.TEACHER
                                        listener.onTeacherFound()

                                    }
                                } else {
                                    listener.onForeignObligation()
                                }
                            }

                        } else {
                            Log.w(TAG, "onAccountTypeFound: UnKnown Error")
                        }

                    } else {
                        var account_major = ""
                        if (o != null) {
                            account_major = o.toString()
                        } else {
                            account_major = DumeUtils.STUDENT
                        }

                        if (!foreignObligation!!) {
                            if (account_major == "student") {
                                Google.getInstance().accountMajor = DumeUtils.STUDENT

                                listener.onStudentFound()
                            } else {
                                Google.getInstance().accountMajor = DumeUtils.TEACHER
                                listener.onTeacherFound()
                            }
                        } else {
                            listener.onForeignObligation()
                        }
                    }

                } else {
                    listener.onFail("Does not found any isExiting")
                    Log.w(TAG, "onAccountTypeFound: document is not null")
                }
            } else {
                if (task.exception != null)
                    listener.onFail(task.exception!!.localizedMessage)
            }
        }
    }

    override fun detachListener() {
        listenerRegistration?.remove()
        if (listenerRegistration1 != null) {
            listenerRegistration1!!.remove()
        }

    }


    override fun hasUpdate(listener: TeacherContract.Model.Listener<Boolean>) {
        firestore.collection("app").document("dume_utils").get().addOnSuccessListener { documentSnapshot ->
            Log.w(TAG, "hasUpdate: ")
            val currentVersion = documentSnapshot.get("version_code") as Number?
            val updateVersionName = documentSnapshot.get("version_name") as String?
            val updateDescription = documentSnapshot.get("version_description") as String?
            val totalStudent = documentSnapshot.get("total_students") as Number?
            val totalMentors = documentSnapshot.get("total_mentors") as Number?
            Google.getInstance().totalStudent = totalStudent?.toInt() ?: 0
            Google.getInstance().totalMentor = totalMentors?.toInt() ?: 0
            if (currentVersion != null) {
                if (currentVersion.toInt() > BuildConfig.VERSION_CODE) {
                   // SplashActivity.updateVersionName = updateVersionName!!
                    //SplashActivity.updateDescription = updateDescription!!
                    listener.onSuccess(true)
                } else {
                    listener.onSuccess(false)
                }
            } else
                listener.onSuccess(false)

        }.addOnFailureListener { e -> listener.onError(e.localizedMessage) }
    }

    /**
     *  this functions is calling from AuthViewModel to create a new user.it performs upto three operation at once using batch write.
     *  @returns Unit
     *  @param user
     */

    fun addUser(user: MiniUser, listener: TeacherContract.Model.Listener<Void>) {

        val writeBatch = firestore.batch()
        val miniDocumentReference = miniUserRef.document(FirebaseAuth.getInstance().uid!!)
        val mentorDocumentReference = mentorUserRef.document(FirebaseAuth.getInstance().uid!!)
        val studentDocumentReference = studentUserRef.document(FirebaseAuth.getInstance().uid!!)

        writeBatch.set(miniDocumentReference, user)
        writeBatch.set(mentorDocumentReference, prepareUser(miniUser = user))
        writeBatch.set(studentDocumentReference, prepareUser(miniUser = user))
        writeBatch.commit().addOnCompleteListener { listener.onSuccess(null);geoFirestore.setLocation(FirebaseAuth.getInstance().uid, user.parmanent_location!!) }.addOnFailureListener { listener.onError(it.localizedMessage) }
    }

    /**
     *  this functions is calling from AuthViewModel to create a new user from @param miniUser.it converts miniUser to User
     *  @returns Unit
     *  @param miniUser
     */

    fun prepareUser(miniUser: MiniUser): User {
        return User(name = miniUser.name,
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


    companion object {
        private val TAG = "AuthModel"
    }


}
