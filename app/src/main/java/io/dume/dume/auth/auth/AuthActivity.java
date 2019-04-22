package io.dume.dume.auth.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;

import io.dume.dume.R;
import io.dume.dume.auth.AuthModel;
import io.dume.dume.auth.DataStore;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;
import io.dume.dume.auth.code_verification.PhoneVerificationActivity;
import io.dume.dume.auth.social_init.SocialInitActivity;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.obligation.foreignObli.PayActivity;
import io.dume.dume.splash.FeaturedSliderAdapter;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.util.DumeUtils;


public class AuthActivity extends CustomStuAppCompatActivity implements AuthContract.View, BottomNavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener, TextWatcher {
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
    private Context context;
    private Typeface cairoRegular;
    private HorizontalLoadView loadView;
    private Intent fromIntent;
    private String fromIntentAction;
    private DataStore dataStore;
    private boolean isAccountDefined = false;
    private int accountDefinedFlag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        setActivityContext(this, 1605);
        presenter = new AuthPresenter(this, this, new AuthModel(this, this));
        presenter.enqueue();
        dataStore = DataStore.getInstance();
        socialConnect.setOnClickListener(view -> startActivity(new Intent(AuthActivity.this, SocialInitActivity.class)));
        presenter.setBundle();
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (tMgr != null) {
                String mPhoneNumber = tMgr.getLine1Number();
                if (mPhoneNumber != null || !mPhoneNumber.equals("")) {
                    phoneEditText.setText(mPhoneNumber);
                }
            }
        }
        fromIntent = getIntent();
        fromIntentAction = fromIntent.getAction();
        String phone_number = fromIntent.getStringExtra("phone_number");
        if (phone_number != null) {
            phoneEditText.setText(phone_number);
        }
        //testing null error solution
        DataStore.setSTATION(1);
        //setting my snackbar callback
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                floatingButoon.setClickable(true);
                floatingButoon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            }

            @Override
            public void onShown(Snackbar snackbar) {
                floatingButoon.setClickable(false);
                floatingButoon.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
            }
        });
    }

    @Override
    public boolean isAccountDefined() {
        return isAccountDefined;
    }

    @Override
    public void init() {
        context = this;
        promoTextArray = getResources().getStringArray(R.array.promo_text_array);
        changingTextArray = getResources().getStringArray(R.array.changing_text_array);
        sliderLayout.setCustomIndicator(findViewById(R.id.page_indicator));
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        phoneEditText.setOnEditorActionListener(this);
        appBar.addOnOffsetChangedListener(new AppbarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                presenter.onAppBarStateChange(state);
            }
        });
        phoneEditText.setOnClickListener(view -> appBar.setExpanded(false, true));
        floatingButoon.setOnClickListener(view -> presenter.onPhoneValidation(phoneEditText.getText().toString()));
        phoneEditText.addTextChangedListener(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        cairoRegular = Typeface.createFromAsset(getAssets(), "fonts/Cairo_Regular.ttf");

        //changingTextView.setTypeface(custom_font);
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

        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem item = bottomNavigationView.getMenu().getItem(i);
            Menu menu = bottomNavigationView.getMenu();
            if (item.getMenuInfo() != null) {
                Log.w(TAG, "init: " + item.getMenuInfo());
            } else Log.w(TAG, "init: null");
            SpannableString spannableString = new SpannableString(item.getTitle());
            spannableString.setSpan(cairoRegular, 0, item.getTitle().length(), 0);
            item.setTitle(spannableString);
        }
        collapsingToolbarLayout.setExpandedTitleTypeface(cairoRegular);
        collapsingToolbarLayout.setCollapsedTitleTypeface(cairoRegular);
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
        loadView = findViewById(R.id.loadView);
        bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.drawable.color_state_list_pre));
        bottomNavigationView.setItemTextColor(getResources().getColorStateList(R.drawable.color_state_list_pre));

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
        collapsingToolbarLayout.setTitle("");
        setDarkStatusBarIcon();
    }

    @Override
    public void onAppBarLayoutCollapsed() {
        toolbar.setVisibility(View.VISIBLE);
        floatingButoon.show();
        collapsingToolbarLayout.setTitle("");
        setLightStatusBarIcon();
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
    public void goToVerificationActivity() {
        DataStore.setSTATION(1);
        Intent intent = new Intent(this, PhoneVerificationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToRegesterActivity() {
        Intent intent = new Intent(this, AuthRegisterActivity.class);
        //J
        String accountMajor = null;
        switch (bottomNavigationView.getSelectedItemId()) {
            case R.id.student_nav:
                accountMajor = DumeUtils.STUDENT;
                break;
            case R.id.teacher_nav:
                accountMajor = DumeUtils.TEACHER;
                break;
            default:
                accountMajor = DumeUtils.BOOTCAMP;
                break;
        }
        dataStore.setAccountManjor(accountMajor);
        dataStore.setBottomNavAccountMajor(true);

        startActivity(intent);
        finish();
    }

    @Override
    public void showProgress() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    @Override
    public void hideProgress() {
        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void restoreData() {

        phoneEditText.setText(dataStore.getPhoneNumber());
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        isAccountDefined = true;
        bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.drawable.color_state_list));
        bottomNavigationView.setItemTextColor(getResources().getColorStateList(R.drawable.color_state_list));

        switch (item.getItemId()) {
            case R.id.bootcamp_nav:
                showToast("Boot Camp Service is coming soon...");
                bottomNavigationView.setSelectedItemId(R.id.teacher_nav);
                return false;
            default:
                presenter.onBottomNavChange(item);
                break;
        }
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
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void gotoTeacherActivity() {
        startActivity(new Intent(this, TeacherActivtiy.class));
        finish();
    }

    @Override
    public void gotoStudentActivity() {
        startActivity(new Intent(this, HomePageActivity.class));
        finish();
    }

    @Override
    public void gotoForeignObligation() {
        startActivity(new Intent(this, PayActivity.class));
        finish();
    }
}


