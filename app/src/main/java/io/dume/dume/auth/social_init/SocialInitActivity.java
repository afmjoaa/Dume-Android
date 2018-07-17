package io.dume.dume.auth.social_init;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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

import dmax.dialog.SpotsDialog;
import io.dume.dume.R;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;

public class SocialInitActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    public final static int RC_SIGN_IN = 2;
    private DataStore dataStore;
    private SpotsDialog.Builder spotsBuilder;
    private AlertDialog spotDialog;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_init);
        spotsBuilder = new SpotsDialog.Builder().setContext(this);
        spotsBuilder.setCancelable(false);
        spotsBuilder.setMessage("Loading");
        spotDialog = spotsBuilder.build();

        init();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(SocialInitActivity.this, "success", Toast.LENGTH_SHORT).show();
                    String str = user.getDisplayName();
                    String[] splited = str.split(" ");

                    dataStore.setEmail(user.getEmail());
                    dataStore.setFirstName(splited[0]);
                    dataStore.setLastName(splited[1]);
                    dataStore.setPhoneNumber(user.getPhoneNumber());
                    dataStore.setPhotoUri(Objects.requireNonNull(user.getPhotoUrl()).toString());

                    signOut();
                    passToNextActivity();
                } else {
                    // doing nothing here no
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
                //  Log.w("Tag", "Google sign in failed due to canceling", e);
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
//                            Log.d("Tag", "signInWithCredential:success");
                        } else {
//                            Log.w("Tag", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SocialInitActivity.this, "failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    //    facebook success error methods
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Log.d("fbtag", "signInWithCredential:success");
                        } else {
//                            Log.w("fbtag", "signInWithCredential:failure", task.getException());
                            Toast.makeText(SocialInitActivity.this, "failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    //################################## My part start here###########################
    private void init() {
        dataStore = DataStore.getInstance();
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
//                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
//                        Log.d("login canceled", "Login");
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
                spotDialog.show();
                signInWithFacebook();
                break;
            case R.id.google_btn:
                spotDialog.show();
                signInWithGoogle();
                break;
        }

    }

    public void passToNextActivity() {
        Intent intent = new Intent(getApplicationContext(), AuthRegisterActivity.class);
        intent.putExtra("datastore", dataStore);
        startActivity(intent);
        spotDialog.dismiss();
        finish();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInWithFacebook() {
        // add more request to the arraylist
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
    }

    @Override
    protected void onDestroy() {
        spotDialog.dismiss();
        super.onDestroy();
    }

}