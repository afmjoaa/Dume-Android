package io.dume.dume.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.dume.dume.auth.code_verification.PhoneVerificationContract;
import io.dume.dume.splash.SplashContract;

public class AuthModel implements AuthContract.Model, SplashContract.Model, PhoneVerificationContract.Model {
    private static final String TAG = "AuthModel";
    Activity activity;
    Context context;
    private final FirebaseAuth mAuth;
    private final Intent mIntent;


    public AuthModel(Activity activity, Context context) {
        this.context = context;
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        mIntent = this.activity.getIntent();
    }

    @Override
    public void sendMessage(String phoneNumber, Callback listener) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, activity, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                listener.onFail(e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                listener.onSuccess(s);
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
        if (listener != null && mIntent != null) {
            listener.onStart();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mIntent.getBundleExtra("bundle").getString("verification_id", ""), code);
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
    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() == null ? false : true;
    }

    @Override
    public void onAccountTypeFound(SplashContract.AuthCallbackListener listener) {
        listener.onTeacherFound();
    }

}
