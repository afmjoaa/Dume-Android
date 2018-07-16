package io.dume.dume.auth.social_init;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;

public class SocialInitActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    public final static int RC_SIGN_IN = 2;
    private Bundle mBundle;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_init);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        init();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Todo catch the data in bundle and change the activity with passing bundle
                    Toast.makeText(SocialInitActivity.this, "successfully log in ", Toast.LENGTH_SHORT).show();

                    String str = user.getDisplayName();
                    String[] splited = str.split(" ");
                    mBundle.putString("first_name", splited[0]);
                    mBundle.putString("last_name", splited[1]);
                    mBundle.putString("email", user.getEmail());
                    mBundle.putString("phone_number", user.getPhoneNumber());
                    mBundle.putString("photo_url", Objects.requireNonNull(user.getPhotoUrl()).toString());

                    signOut();
                    passToNextActivity();
                } else {
                    // Todo check for facebook login or else

                }
            }
        };

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Tag", "Google sign in failed due to canceling", e);
            }
        }

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Tag", "signInWithCredential:success");
                            //   updateUI(user);
                        } else {
                            Log.w("Tag", "signInWithCredential:failure", task.getException());
                            //   updateUI(null);
                        }
                        // ...
                    }
                });
    }

//    facebook success error methods
private void handleFacebookAccessToken(AccessToken token) {
//    Log.d(TAG, "handleFacebookAccessToken:" + token);

    AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("fbtag", "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
//                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("fbtag", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SocialInitActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
//                        updateUI(null);
                    }

                }
            });
}


    //################################## My part start here###########################
    private void init() {
        mBundle = new Bundle();
        // google initialization part
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Facebook initialization part
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.d("login canceled", "Login");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(SocialInitActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void onSocialViewClicked(View view) {
        switch (view.getId()) {
            case R.id.facebook_btn:
                signInWithFacebook();
                break;
            case R.id.google_btn:
                signInWithGoogle();
                break;
        }

    }

    public void passToNextActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthRegisterActivity.class);
        intent.putExtra("bundle", mBundle);
        startActivity(intent);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

}