package io.dume.dume.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
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

import io.dume.dume.R;
import io.dume.dume.auth.code_verification.PhoneVerificationActivity;
import io.dume.dume.auth.register.RegisterActivity;
import io.dume.dume.splash.FeaturedSliderAdapter;


public class AuthActivity extends AppCompatActivity implements AuthContract.View, BottomNavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener, TextWatcher {
    SliderLayout sliderLayout;
    AuthContract.Presenter presenter;
    private String[] studentTextArray;
    private BottomNavigationView bottomNavigationView;
    private EditText phoneEditText;
    private String[] teacherTextArray;
    private String[] bootcampTextArray;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBar;
    private ActionBar actionBar;
    private NestedScrollView scrollView;
    private TextView countryCode;
    private FloatingActionButton floatingButoon;
    private TextView numberCounter;
    private static final String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        presenter = new AuthPresenter(this, null);
        presenter.enqueue();
    }


    @Override
    public void init() {
        studentTextArray = getResources().getStringArray(R.array.student_text_array);
        teacherTextArray = getResources().getStringArray(R.array.teacher_text_array);
        bootcampTextArray = getResources().getStringArray(R.array.bootcamp_text_array);
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
    }

    @Override
    public void onStudentSelected() {

        sliderLayout.removeAllSliders();
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(studentTextArray[0]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(studentTextArray[1]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(studentTextArray[2]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(studentTextArray[3]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(studentTextArray[4]));
    }

    @Override
    public void onTeacherSelected() {

        sliderLayout.removeAllSliders();
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(teacherTextArray[0]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(teacherTextArray[1]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(teacherTextArray[2]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(teacherTextArray[3]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(teacherTextArray[4]));

    }

    @Override
    public void onBootcampSelected() {

        sliderLayout.removeAllSliders();
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(bootcampTextArray[0]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(bootcampTextArray[1]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(bootcampTextArray[2]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(bootcampTextArray[3]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(bootcampTextArray[4]));


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
        presenter.isExistingUser(number);
    }


    @Override
    public void onValidationFailed(String err) {
        phoneEditText.setError(err);
    }

    @Override
    public void goToVerificationActivity() {
        Intent intent = new Intent(this, PhoneVerificationActivity.class);
        startActivity(intent);
    }

    @Override
    public void goToRegesterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
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

}


