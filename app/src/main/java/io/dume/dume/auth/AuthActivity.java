package io.dume.dume.auth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;

import dmax.dialog.SpotsDialog;
import io.dume.dume.R;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;
import io.dume.dume.auth.code_verification.PhoneVerificationActivity;
import io.dume.dume.auth.social_init.SocialInitActivity;
import io.dume.dume.splash.FeaturedSliderAdapter;


public class AuthActivity extends AppCompatActivity implements AuthContract.View, BottomNavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener, TextWatcher {
    SliderLayout sliderLayout;
    AuthContract.Presenter presenter;
    private String[] promoTextArray;
    private BottomNavigationView bottomNavigationView;
    private EditText phoneEditText;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBar;
    private ActionBar actionBar;
    private NestedScrollView scrollView;
    private TextView countryCode;
    private FloatingActionButton floatingButoon;
    private TextView numberCounter;
    private static final String TAG = "AuthActivity";
    private String[] changingTextArray;
    private TextView changingTextView;

    private TextView socialConnect;

    private ProgressDialog progressDialog;
    private SpotsDialog.Builder spotsBuilder;
    private AlertDialog spotDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        presenter = new AuthPresenter(this, new AuthModel(this, this));
        presenter.enqueue();
        socialConnect.setOnClickListener(view -> startActivity(new Intent(AuthActivity.this, SocialInitActivity.class)));
        presenter.setBundle();

    }


    @Override
    public void init() {
        promoTextArray = getResources().getStringArray(R.array.promo_text_array);
        changingTextArray = getResources().getStringArray(R.array.changing_text_array);
        sliderLayout.setCustomIndicator(findViewById(R.id.page_indicator));
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.student_nav);
        phoneEditText.setOnEditorActionListener(this);
        appBar.addOnOffsetChangedListener(new AppbarStateChangeListener() {
            @Override
            void onStateChanged(AppBarLayout appBarLayout, State state) {
                presenter.onAppBarStateChange(state);
            }
        });
        phoneEditText.setOnClickListener(view -> appBar.setExpanded(false, true));
        floatingButoon.setOnClickListener(view -> presenter.onPhoneValidation(phoneEditText.getText().toString()));
        phoneEditText.addTextChangedListener(this);
        progressDialog = new ProgressDialog(this);



        spotsBuilder = new SpotsDialog.Builder().setContext(this);
        spotsBuilder.setCancelable(false);
        spotsBuilder.setMessage("Loading");
        spotDialog = spotsBuilder.build();
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

        changingTextView.setTypeface(custom_font);
        //initializing sliderLayout
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(promoTextArray[0]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(promoTextArray[1]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(promoTextArray[2]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(promoTextArray[3]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(promoTextArray[4]));
        sliderLayout.startAutoCycle(1000, 5000, true);

    }

    @Override
    public void initActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void findView() {
        sliderLayout = findViewById(R.id.slidingLayout);
        bottomNavigationView = findViewById(R.id.bottomNav);
        phoneEditText = findViewById(R.id.phoneNumberEditText);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        appBar = findViewById(R.id.appbarLayout);
        toolbar = findViewById(R.id.authToolbar);
        scrollView = findViewById(R.id.scroll_view);
        countryCode = findViewById(R.id.countryCode);
        floatingButoon = findViewById(R.id.floating_button);
        numberCounter = findViewById(R.id.phoneCount);
        changingTextView = findViewById(R.id.changingText);
        socialConnect = findViewById(R.id.socialConnect);
    }

    @Override
    public void onStudentSelected() {
        changingTextView.setText(changingTextArray[0]);

    }

    @Override
    public void onTeacherSelected() {
        changingTextView.setText(changingTextArray[1]);

    }

    @Override
    public void onBootcampSelected() {
        changingTextView.setText(changingTextArray[2]);

    }

    @Override
    public void onAppBarLayoutExpanded() {
        toolbar.setVisibility(View.INVISIBLE);
        floatingButoon.hide();
    }

    @Override
    public void onAppBarLayoutCollapsed() {
        toolbar.setVisibility(View.VISIBLE);
        floatingButoon.show();
    }

    @Override
    public void showCount(String s) {
        if (numberCounter.getVisibility() == View.INVISIBLE) {
            numberCounter.setVisibility(View.VISIBLE);
        }
        numberCounter.setText(s);
    }

    @Override
    public void hideCount() {
        numberCounter.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onValidationSuccess(String number) {
        Toast.makeText(this, "Validation Success", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onValidationFailed(String err) {
        phoneEditText.setError(err);
    }

    @Override
    public void goToVerificationActivity(DataStore dataStore) {
        Intent intent = new Intent(this, PhoneVerificationActivity.class);
        intent.putExtra("datastore", dataStore);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToRegesterActivity(DataStore dataStore) {
        Intent intent = new Intent(this, AuthRegisterActivity.class);
        dataStore.setAccountManjor(bottomNavigationView.getSelectedItemId() == R.id.student_nav ? "student" : "teacher");
        intent.putExtra("datastore", dataStore);
        startActivity(intent);
        finish();

    }

    @Override
    public void showProgress(String titile, String message) {
        spotDialog.show();
    }

    @Override
    public void hideProgress() {
        spotDialog.dismiss();
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void restoreData(DataStore dataStore) {
        phoneEditText.setText(dataStore.getPhoneNumber());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        presenter.onBottomNavChange(item);
        return true;
    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            presenter.onPhoneValidation(phoneEditText.getText().toString());
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (appBar != null) {
            appBar.setExpanded(true);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332 && appBar != null) {
            appBar.setExpanded(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.w(TAG, "beforeTextChanged: " + charSequence);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        presenter.onPhoneTextChange(String.valueOf(charSequence));
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.w(TAG, "afterTextChanged: " + editable.toString());
    }

    @Override
    protected void onPause() {
        spotDialog.dismiss();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        spotDialog.dismiss();
        super.onDestroy();
    }
}


