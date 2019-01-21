package io.dume.dume.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.dume.dume.auth.auth.AuthContract;
import io.dume.dume.auth.code_verification.PhoneVerificationContract;
import io.dume.dume.splash.SplashContract;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

public class AuthModel implements AuthContract.Model, SplashContract.Model, PhoneVerificationContract.Model {
    private static final String TAG = "AuthModel";
    Activity activity;
    Context context;
    private final FirebaseAuth mAuth;
    private final Intent mIntent;
    private DataStore datastore = null;
    private final FirebaseFirestore firestore;
    private ListenerRegistration listenerRegistration;
    private ListenerRegistration listenerRegistration1;
    private ListenerRegistration listenerRegistration2;


    public AuthModel(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mIntent = this.activity.getIntent();
        if (mIntent.getSerializableExtra("datastore") != null) {
            datastore = (DataStore) mIntent.getSerializableExtra("datastore");
        } else {
            datastore = DataStore.getInstance();
        }
        firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);
        Log.w(TAG, "AuthModel: " + firestore.hashCode());
    }


    @Override
    public void sendMessage(String phoneNumber, Callback listener) {
        listener.onStart();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, activity, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onAutoSuccess(task.getResult());
                    } else if (task.isCanceled()) {
                        listener.onFail("Authentication Canceled");
                    } else if (task.isComplete()) {
                        Log.w(TAG, "onComplete: task completed");
                    }

                }).addOnFailureListener(e -> {
                    if (e instanceof FirebaseNoSignedInUserException) {
                        listener.onFail("Already signed in");
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        listener.onFail("You tried too times");
                    } else if (e instanceof FirebaseAuthInvalidUserException) {
                        listener.onFail("Invalid User");
                    }

                    listener.onFail(e.getLocalizedMessage() + e.getClass().getName());
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                listener.onFail(e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                listener.onSuccess(s, forceResendingToken);
                super.onCodeSent(s, forceResendingToken);
            }
        });
    }

    @Override
    public Intent getIntent() {
        return mIntent != null ? mIntent : new Intent();
    }

    @Override
    public boolean isExistingUser(@Nullable String phoneNumber, AuthGlobalContract.OnExistingUserCallback listener) {
        Log.w(TAG, "isExistingUser: ");
        listener.onStart();
        // final boolean[] isexists = {false};
        listenerRegistration1 = firestore.collection("mini_users").whereEqualTo("phone_number", phoneNumber).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documents = null;
                detachListener();
                if (queryDocumentSnapshots != null) {
                    documents = queryDocumentSnapshots.getDocuments();

                    if (documents.size() >= 1) {
                        datastore.setDocumentSnapshot(documents.get(0).getData());
                        listener.onUserFound();

                    } else {
                        //   listener.onNewUserFound();
                        checkImei(new TeacherContract.Model.Listener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot list) {
                                if (list.getDocuments().size() > 0) {
                                    /*context.startActivity(new Intent(context, PayActivity.class));*/
                                    boolean obligation = false;
                                    Map<String, Map<String, Object>> obligatedUser = new HashMap<>();
                                    for (int i = 0; i < list.getDocuments().size(); i++) {
                                        obligation = (boolean) list.getDocuments().get(i).getData().get("obligation");
                                        if (obligation) {
                                            obligatedUser.put(list.getDocuments().get(i).getId(), list.getDocuments().get(i).getData());
                                        }
                                    }
                                    if (obligatedUser.size() > 0) {
                                        DataStore.getInstance().setObligation(true);
                                        DataStore.getInstance().setObligatedUser(obligatedUser);
                                    } else DataStore.getInstance().setObligation(false);
                                    listener.onNewUserFound();

                                } else {
                                    listener.onNewUserFound();
                                }
                            }

                            @Override
                            public void onError(String msg) {

                            }
                        });

                    }
                } else {
                    listener.onError("Internal Error. No queryDocumentSpanshot Returned By Google");
                }
                if (e != null) {
                    listener.onError(e.getLocalizedMessage());
                }

            }
        });

        return false;
    }

    public void checkImei(TeacherContract.Model.Listener<QuerySnapshot> imeiFound) {

        listenerRegistration2 = firestore.collection("mini_users").whereArrayContains("imei", DumeUtils.getImei(context).get(0)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    imeiFound.onSuccess(queryDocumentSnapshots);
                    Log.e(TAG, "onEvent: " + queryDocumentSnapshots.getDocuments().toString());

                } else {
                    Log.e(TAG, "onEvent: Balda KIchui Painai");
                    imeiFound.onError(e == null ? "Baler Erro" : e.getLocalizedMessage());
                }
                listenerRegistration2.remove();
            }
        });
    }

    @Override
    public void verifyCode(String code, PhoneVerificationContract.Model.CodeVerificationCallBack listener) {
        if (listener != null && datastore != null) {
            listener.onStart();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(datastore.getVerificationId(), code);
            mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                    if (DataStore.STATION == 1) {
                    } else if (DataStore.STATION == 2) {

                    }


                } else {
                    if (task.getException() != null) {
                        listener.onFail(task.getException().getLocalizedMessage());
                    }
                }
            }).addOnFailureListener(e -> {
                listener.onFail(e.getLocalizedMessage());
            });
        }
    }

    @Override
    public void onResendCode(Callback listener) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+88" + datastore.getPhoneNumber(), 60, TimeUnit.SECONDS, activity, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onAutoSuccess(task.getResult());
                    } else if (task.isCanceled()) {
                        listener.onFail("Authentication Canceled");
                    } else if (task.isComplete()) {
                        Log.w(TAG, "onComplete: task completed");
                    }

                }).addOnFailureListener(e -> {
                    listener.onFail(e.getLocalizedMessage());
                });
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                listener.onFail(e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                listener.onSuccess(s, forceResendingToken);
                super.onCodeSent(s, forceResendingToken);
            }
        }, DataStore.resendingToken);
    }

    @Override
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    @Override
    public FirebaseUser getUser() {
        return mAuth.getCurrentUser();
    }

    @Override
    public DataStore getData() {
        return datastore;
    }

    @Override
    public void onAccountTypeFound(FirebaseUser user, AuthGlobalContract.AccountTypeFoundListener listener) {
        listener.onStart();

        DocumentReference mini_users = firestore.collection("mini_users").document(user.getUid());
        listenerRegistration = mini_users.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {

                Log.w(TAG, "onAccountTypeFound: " + documentSnapshot.toString());
                Log.e(TAG, "Fucked Here : " + documentSnapshot.toString());

                boolean foreignObligation = (boolean) documentSnapshot.getData().get("foreign_obligation");
                detachListener();
                Object o = documentSnapshot.get("account_major");
                if (datastore != null && datastore.isBottomNavAccountMajor()) {
                    if (datastore.getAccountManjor() != null) {
                        datastore.setBottomNavAccountMajor(false);
                        Map<String, Object> newMap = new HashMap<>();
                        newMap.put("account_major", datastore.getAccountManjor());
                        final Task<Void> mini_users1 = mini_users.update(newMap).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: Enam " + e.getLocalizedMessage());
                            }
                        }).addOnCompleteListener(task -> {

                            Log.e(TAG, "addOnCompleteListener: Enam ");
                            String account_major = "";
                            account_major = datastore.getAccountManjor();
                            assert account_major != null;
                            if (!foreignObligation) {
                                if (account_major.equals("student")) {
                                    listener.onStudentFound();
                                } else {
                                    listener.onTeacherFound();
                                }
                            } else {
                                listener.onForeignObligation();
                            }
                        });

                    } else {
                        Log.w(TAG, "onAccountTypeFound: UnKnown Error");
                    }

                } else {
                    String account_major = "";
                    assert o != null;
                    account_major = o.toString();
                    assert account_major != null;
                    if (!foreignObligation) {
                        if (account_major.equals("student")) {
                            listener.onStudentFound();
                        } else {
                            listener.onTeacherFound();
                        }
                    } else {
                        listener.onForeignObligation();
                    }

                }

            } else {
                listener.onFail("Does not found any user");
                Log.w(TAG, "onAccountTypeFound: document is not null");
            }
        });

    }

    @Override
    public void detachListener() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        if (listenerRegistration1 != null) {
            listenerRegistration1.remove();
        }

    }


}
