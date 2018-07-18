package io.dume.dume.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.concurrent.TimeUnit;

import io.dume.dume.auth.auth.AuthContract;
import io.dume.dume.auth.code_verification.PhoneVerificationContract;
import io.dume.dume.splash.SplashContract;

public class AuthModel implements AuthContract.Model, SplashContract.Model, PhoneVerificationContract.Model {
    private static final String TAG = "AuthModel";
    Activity activity;
    Context context;
    private final FirebaseAuth mAuth;
    private final Intent mIntent;
    private DataStore datastore = null;


    public AuthModel(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mIntent = this.activity.getIntent();
        if (mIntent.getSerializableExtra("datastore") != null) {
            datastore = (DataStore) mIntent.getSerializableExtra("datastore");
        }

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
                        listener.onFail("FirebaseNoSignedInUserException");
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        listener.onFail("FirebaseTooManyRequestsException");
                    } else if (e instanceof FirebaseAuthInvalidUserException) {

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
    public boolean isExistingUser(String phoneNumber) {
        return false;
    }

    @Override
    public void verifyCode(String code, PhoneVerificationContract.Model.CodeVerificationCallBack listener) {
        if (listener != null && datastore != null) {
            listener.onStart();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(datastore.getVerificationId(), code);
            mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onSuccess();
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
        listener.onTeacherFound();
    }


}
