package io.dume.dume.student.grabingPackage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;
import carbon.widget.Button;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.searchLoading.SearchLoadingActivity;
import io.dume.dume.util.DatePickerFragment;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.TimePickerFragment;
import io.dume.dume.util.VisibleToggleClickListener;

import static com.facebook.FacebookSdk.getApplicationContext;
import static io.dume.dume.util.DumeUtils.firstThree;
import static io.dume.dume.util.DumeUtils.firstTwo;
import static io.dume.dume.util.DumeUtils.getScreenSize;

public class GrabingPackageActivity extends CusStuAppComMapActivity implements GrabingPackageContract.View,
        MyGpsLocationChangeListener, OnMapReadyCallback {

    private static final String TAG = "GrabingPackageActivity";
    private GrabingPackageContract.Presenter mPresenter;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final int fromFlag = 4;
    private ViewPagerBottomSheetBehavior bottomSheetBehavior;
    private HorizontalLoadView loadView;
    private carbon.widget.LinearLayout llBottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout myAppBarLayout;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private int[] navIcons = {
            R.drawable.ic_seven_days,
            R.drawable.ic_preffered_day,
            R.drawable.ic_time
    };
    private android.widget.Button packageSearchBtn;
    private RelativeLayout dumeGangContainer;
    private RelativeLayout regularDumeContainer;
    private RelativeLayout instantDumeContainer;
    private carbon.widget.ImageView dumeGangImage;
    private carbon.widget.ImageView regularDumeImage;
    private carbon.widget.ImageView instantDumeImage;
    private TextView instantDumePriceText;
    private TextView regularDumePriceText;
    private TextView dumeGangPriceText;
    private TextView specificPromoText;
    private String[] specificPromoTextArr;
    private RelativeLayout bottomsheetContainerRelalay;
    private Boolean dragFirst = true;
    private RelativeLayout moreInfoLayOut;
    private TextView individualPromoTitle;
    private TextView salaryText;
    private TextView salaryTextValue;
    private TextView capacityText;
    private TextView capacityTextValue;
    private String[] primaryPromoTextArr;
    private TextView hintIdThree;
    private TextView hintIdTwo;
    private TextView hintIdOne;
    private carbon.widget.ImageView dumeGangPercentageOffImage;
    private carbon.widget.ImageView instantDumePercentageOffImage;
    private carbon.widget.ImageView regularDumePercentageOffImage;
    private LayerDrawable dumeGangBadgeOffLayDraw;
    private LayerDrawable regularDumeBadgeOffLayDraw;
    private LayerDrawable instantDumeBadgeOffLayDraw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu4_activity_grabing_package);
        setActivityContextMap(this, fromFlag);
        findLoadView();
        mPresenter = new GrabingPackagePresenter(this, new GrabingPackageModel());
        mPresenter.grabingPackagePageEnqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        BottomSheetUtils.setupViewPager(mViewPager);
        //making the custom tab here
        int[] wh = DumeUtils.getScreenSize(this);
        int tabMinWidth = ((wh[0] / 3)-(int) (24 * (getResources().getDisplayMetrics().density)));
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                (tabMinWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
            String navLabels[] = getResources().getStringArray(R.array.GrabingPackageTabtext);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
            tab_label.setText(navLabels[i]);
            tab_icon.setImageResource(navIcons[i]);
            tab_label.setLayoutParams(textParam);

            // finally publish this custom view to navigation tab
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
        // finishes here ................

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    @Override
    public void findView() {
        loadView = findViewById(R.id.loadView);
        llBottomSheet = findViewById(R.id.packageBottomSheet);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.my_main_container);
        myAppBarLayout = findViewById(R.id.my_appbarLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        packageSearchBtn = findViewById(R.id.package_search_btn);
        dumeGangContainer = findViewById(R.id.dume_gang_container);
        regularDumeContainer = findViewById(R.id.regular_dume_container);
        instantDumeContainer = findViewById(R.id.instant_dume_container);
        dumeGangImage = findViewById(R.id.dume_gang_image);
        regularDumeImage = findViewById(R.id.regular_dume_image);
        instantDumeImage = findViewById(R.id.instant_dume_image);
        dumeGangPriceText = findViewById(R.id.dume_gang_price_text);
        regularDumePriceText = findViewById(R.id.regular_dume_price_text);
        instantDumePriceText = findViewById(R.id.instant_dume_price_text);
        specificPromoText = findViewById(R.id.specific_promo_text);
        specificPromoTextArr = getResources().getStringArray(R.array.specificPromoText);
        primaryPromoTextArr = getResources().getStringArray(R.array.primaryPromoText);
        bottomsheetContainerRelalay = findViewById(R.id.bottom_sheet_container);
        moreInfoLayOut = findViewById(R.id.more_info_layout);
        individualPromoTitle = findViewById(R.id.individual_promo_title);
        salaryText = findViewById(R.id.salary_text);
        salaryTextValue = findViewById(R.id.salary_text_value);
        capacityText = findViewById(R.id.capacity_text);
        capacityTextValue = findViewById(R.id.capacity_text_value);
        hintIdOne = findViewById(R.id.hint_id_1);
        hintIdTwo = findViewById(R.id.hint_id_2);
        hintIdThree = findViewById(R.id.hint_id_3);

        dumeGangPercentageOffImage = findViewById(R.id.dume_gang_percent_off_image);
        regularDumePercentageOffImage = findViewById(R.id.regular_dume_percent_off_image);
        instantDumePercentageOffImage = findViewById(R.id.instant_dume_percent_off_image);

    }

    @Override
    public void initGrabingPackagePage() {
        //setting the support action bar
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);
        //status bar and action bar transparent
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        myAppBarLayout.bringToFront();
        myAppBarLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //initializing the product block thing
        specificPromoText.setText(specificPromoTextArr[0]);
        //bottom sheet height fix
        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                int[] widthHeight = getScreenSize(GrabingPackageActivity.this);
                int height = widthHeight[1] - 164;
                if (height < 1021) {
                    llBottomSheet.getLayoutParams().height = (height);
                    llBottomSheet.requestLayout();
                    bottomSheetBehavior.onLayoutChild(coordinatorLayout, llBottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
                }
                //Log.e(TAG, "testing_height" + (llBottomSheet.getHeight() - myAppBarLayout.getHeight() - (int) (2 * (getResources().getDisplayMetrics().density))) +"fuck"+ widthHeight[1] );
                ViewTreeObserver obs = llBottomSheet.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });

        //setting the animation for the container
        dumeGangContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dumeGangSelected();

            }
        });

        //setting the animation for the container
        regularDumeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regularDumeSelected();

            }
        });

        //setting the animation for the container
        instantDumeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instantDumeSelected();

            }
        });
    }

    @Override
    public void configGrabingPackagePage() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                    ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
                    Drawable drawableIcon = tab_icon.getDrawable();

                    if (drawableIcon instanceof Animatable) {
                        ((Animatable) drawableIcon).start();
                    }
                }
                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
                    Drawable drawableIcon = tab_icon.getDrawable();

                    if (drawableIcon instanceof Animatable) {
                        ((Animatable) drawableIcon).start();
                    }
                }
                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    default:
                        break;
                }
            }
        });

        bottomSheetBehavior = ViewPagerBottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new ViewPagerBottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {

                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    dragFirst = true;
                    moreInfoLayOut.setVisibility(View.GONE);
                    individualPromoTitle.setVisibility(View.GONE);
                    specificPromoText.setVisibility(View.VISIBLE);
                    dumeGangContainer.setVisibility(View.VISIBLE);
                    regularDumeContainer.setVisibility(View.VISIBLE);
                    instantDumeContainer.setVisibility(View.VISIBLE);
                    dumeGangPriceText.setVisibility(View.VISIBLE);
                    regularDumePriceText.setVisibility(View.VISIBLE);
                    instantDumePriceText.setVisibility(View.VISIBLE);
                    if (specificPromoText.getText() == specificPromoTextArr[0]) {
                        dumeGangContainer.animate()
                                .translationYBy((float) (-3.0f * (getResources().getDisplayMetrics().density)))
                                .setDuration(60)
                                .start();
                    } else if (specificPromoText.getText() == specificPromoTextArr[1]) {
                        regularDumeContainer.animate()
                                .translationYBy((float) (-3.0f * (getResources().getDisplayMetrics().density)))
                                .setDuration(60)
                                .start();
                    } else {
                        instantDumeContainer.animate()
                                .translationYBy((float) (-3.0f * (getResources().getDisplayMetrics().density)))
                                .setDuration(60)
                                .start();
                    }
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    if (dragFirst) {
                        if (specificPromoText.getText() == specificPromoTextArr[0]) {
                            dumeGangContainer.animate()
                                    .translationYBy((float) (3.0f * (getResources().getDisplayMetrics().density)))
                                    .setDuration(60)
                                    .start();
                        } else if (specificPromoText.getText() == specificPromoTextArr[1]) {
                            regularDumeContainer.animate()
                                    .translationYBy((float) (3.0f * (getResources().getDisplayMetrics().density)))
                                    .setDuration(60)
                                    .start();
                        } else {
                            instantDumeContainer.animate()
                                    .translationYBy((float) (3.0f * (getResources().getDisplayMetrics().density)))
                                    .setDuration(60)
                                    .start();
                        }
                    }
                    moreInfoLayOut.setVisibility(View.VISIBLE);
                    individualPromoTitle.setVisibility(View.VISIBLE);
                    specificPromoText.setVisibility(View.INVISIBLE);


                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    if (dragFirst) {
                        dragFirst = false;
                        if (specificPromoText.getText() == specificPromoTextArr[0]) {
                            dumeGangSelected();
                            dumeGangContainer.animate()
                                    .translationYBy((float) (3.0f * (getResources().getDisplayMetrics().density)))
                                    .setDuration(60)
                                    .start();
                        } else if (specificPromoText.getText() == specificPromoTextArr[1]) {
                            regularDumeSelected();
                            regularDumeContainer.animate()
                                    .translationYBy((float) (3.0f * (getResources().getDisplayMetrics().density)))
                                    .setDuration(60)
                                    .start();
                        } else {
                            instantDumeSelected();
                            instantDumeContainer.animate()
                                    .translationYBy((float) (3.0f * (getResources().getDisplayMetrics().density)))
                                    .setDuration(60)
                                    .start();
                        }
                    }
                    moreInfoLayOut.setVisibility(View.VISIBLE);
                    individualPromoTitle.setVisibility(View.VISIBLE);
                    specificPromoText.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (specificPromoText.getText() == specificPromoTextArr[0]) {
                    dumeGangImage.animate()
                            .scaleX((float) (1 + (0.20 * slideOffset)))
                            .scaleY((float) (1 + (0.20 * slideOffset)))
                            .setDuration(0).start();
                    dumeGangPercentageOffImage.animate()
                            .scaleX((float) (1 + (0.18 * slideOffset)))
                            .scaleY((float) (1 + (0.18 * slideOffset)))
                            .translationY((float) (2.3 * (getResources().getDisplayMetrics().density)))
                            .translationX((float) (24 * (getResources().getDisplayMetrics().density) + (8 * slideOffset * (getResources().getDisplayMetrics().density))))
                            .setDuration(0).start();
                } else if (specificPromoText.getText() == specificPromoTextArr[1]) {
                    regularDumeImage.animate()
                            .scaleX((float) (1 + (0.20 * slideOffset)))
                            .scaleY((float) (1 + (0.20 * slideOffset)))
                            .setDuration(0).start();
                    regularDumePercentageOffImage.animate()
                            .scaleX((float) (1 + (0.18 * slideOffset)))
                            .scaleY((float) (1 + (0.18 * slideOffset)))
                            .translationY((float) (2.3 * (getResources().getDisplayMetrics().density)))
                            .translationX((float) (24 * (getResources().getDisplayMetrics().density) + (8 * slideOffset * (getResources().getDisplayMetrics().density))))
                            .setDuration(0).start();
                } else {
                    instantDumeImage.animate()
                            .scaleX((float) (1 + (0.20 * slideOffset)))
                            .scaleY((float) (1 + (0.20 * slideOffset)))
                            .setDuration(0).start();
                    instantDumePercentageOffImage.animate()
                            .scaleX((float) (1 + (0.18 * slideOffset)))
                            .scaleY((float) (1 + (0.18 * slideOffset)))
                            .translationY((float) (2.3 * (getResources().getDisplayMetrics().density)))
                            .translationX((float) (24 * (getResources().getDisplayMetrics().density) + (8 * slideOffset * (getResources().getDisplayMetrics().density))))
                            .setDuration(0).start();
                }
                //packageSearchBtn.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).enqueue();
            }
        });

        dumeGangBadgeOffLayDraw = (LayerDrawable) dumeGangPercentageOffImage.getDrawable();
        DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, "-20%", 3);

        regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
        DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-30%", 3);

        instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();
        DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-40%", 3);
    }

    @Override
    public void executeSearchActivity() {
        startActivity(new Intent(this, SearchLoadingActivity.class));
    }

    @Override
    public void dumeGangSelected() {
        if (specificPromoText.getText() == specificPromoTextArr[0]
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .addTransition(new Slide(Gravity.END))
                    .setInterpolator(new LinearOutSlowInInterpolator());
            TransitionManager.beginDelayedTransition(bottomsheetContainerRelalay, set);
            if (regularDumeContainer.getVisibility() == View.VISIBLE) {
                regularDumeContainer.setVisibility(View.GONE);
                instantDumeContainer.setVisibility(View.GONE);
            }
            moreInfoLayOut.setVisibility(View.VISIBLE);
            individualPromoTitle.setVisibility(View.VISIBLE);
            specificPromoText.setVisibility(View.INVISIBLE);
            dumeGangPriceText.setVisibility(View.GONE);

        } else if (specificPromoText.getText() == specificPromoTextArr[0]
                && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            specificPromoText.setText(specificPromoTextArr[0]);
            individualPromoTitle.setText(primaryPromoTextArr[0]);
            dumeGangPriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            regularDumePriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            instantDumePriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            dumeGangImage.setImageResource(R.drawable.dume_gang_image);
            regularDumeImage.setImageResource(R.drawable.dume_regular_grayscale_image);
            instantDumeImage.setImageResource(R.drawable.dume_instant_grayscale_image);

            dumeGangImage.getLayoutParams().height = (int) (64 * (getResources().getDisplayMetrics().density));
            dumeGangImage.getLayoutParams().width = (int) (64 * (getResources().getDisplayMetrics().density));
            regularDumeImage.getLayoutParams().height = (int) (60 * (getResources().getDisplayMetrics().density));
            regularDumeImage.getLayoutParams().width = (int) (60 * (getResources().getDisplayMetrics().density));
            instantDumeImage.getLayoutParams().height = (int) (60 * (getResources().getDisplayMetrics().density));
            instantDumeImage.getLayoutParams().width = (int) (60 * (getResources().getDisplayMetrics().density));

            dumeGangImage.setElevation((int) (8 * (getResources().getDisplayMetrics().density)));
            regularDumeImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            instantDumeImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));

            dumeGangPercentageOffImage.setElevation((int) (8 * (getResources().getDisplayMetrics().density)));
            regularDumePercentageOffImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            instantDumePercentageOffImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));

            dumeGangPercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list);
            regularDumePercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list_inactive);
            instantDumePercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list_inactive);
            dumeGangBadgeOffLayDraw = (LayerDrawable) dumeGangPercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, "-20%", 3);

            regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-30%", 3);

            instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-40%", 3);

            packageSearchBtn.setText("Search Dume Gang");

        }
    }

    @Override
    public void regularDumeSelected() {
        if (specificPromoText.getText() == specificPromoTextArr[1]
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .setInterpolator(new LinearOutSlowInInterpolator());
            TransitionManager.beginDelayedTransition(bottomsheetContainerRelalay, set);
            if (instantDumeContainer.getVisibility() == View.VISIBLE) {
                instantDumeContainer.setVisibility(View.GONE);
                dumeGangContainer.setVisibility(View.GONE);
            }
            moreInfoLayOut.setVisibility(View.VISIBLE);
            individualPromoTitle.setVisibility(View.VISIBLE);
            specificPromoText.setVisibility(View.INVISIBLE);
            regularDumePriceText.setVisibility(View.GONE);


        } else if (specificPromoText.getText() == specificPromoTextArr[1]
                && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            specificPromoText.setText(specificPromoTextArr[1]);
            individualPromoTitle.setText(primaryPromoTextArr[1]);
            dumeGangImage.setImageResource(R.drawable.dume_gang_grayscale_image);
            regularDumeImage.setImageResource(R.drawable.dume_regular_image);
            instantDumeImage.setImageResource(R.drawable.dume_instant_grayscale_image);

            dumeGangPriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            regularDumePriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            instantDumePriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            dumeGangImage.getLayoutParams().height = (int) (60 * (getResources().getDisplayMetrics().density));
            dumeGangImage.getLayoutParams().width = (int) (60 * (getResources().getDisplayMetrics().density));
            regularDumeImage.getLayoutParams().height = (int) (64 * (getResources().getDisplayMetrics().density));
            regularDumeImage.getLayoutParams().width = (int) (64 * (getResources().getDisplayMetrics().density));
            instantDumeImage.getLayoutParams().height = (int) (60 * (getResources().getDisplayMetrics().density));
            instantDumeImage.getLayoutParams().width = (int) (60 * (getResources().getDisplayMetrics().density));

            dumeGangImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            regularDumeImage.setElevation((int) (8 * (getResources().getDisplayMetrics().density)));
            instantDumeImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));

            dumeGangPercentageOffImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            regularDumePercentageOffImage.setElevation((int) (8 * (getResources().getDisplayMetrics().density)));
            instantDumePercentageOffImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            dumeGangPercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list_inactive);
            regularDumePercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list);
            instantDumePercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list_inactive);
            dumeGangBadgeOffLayDraw = (LayerDrawable) dumeGangPercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-20%", 3);

            regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, "-30%", 3);

            instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-40%", 3);
            packageSearchBtn.setText("Search Regular Dume");
        }
    }

    @Override
    public void instantDumeSelected() {
        if (specificPromoText.getText() == specificPromoTextArr[2]
                && bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            TransitionSet set = new TransitionSet()
                    .addTransition(new Fade())
                    .addTransition(new Slide(Gravity.START))
                    .setInterpolator(new LinearOutSlowInInterpolator());
            TransitionManager.beginDelayedTransition(bottomsheetContainerRelalay, set);
            if (regularDumeContainer.getVisibility() == View.VISIBLE) {
                regularDumeContainer.setVisibility(View.GONE);
                dumeGangContainer.setVisibility(View.GONE);
            }
            moreInfoLayOut.setVisibility(View.VISIBLE);
            individualPromoTitle.setVisibility(View.VISIBLE);
            specificPromoText.setVisibility(View.INVISIBLE);
            instantDumePriceText.setVisibility(View.GONE);

        } else if (specificPromoText.getText() == specificPromoTextArr[2]
                && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            specificPromoText.setText(specificPromoTextArr[2]);
            individualPromoTitle.setText(primaryPromoTextArr[2]);
            dumeGangImage.setImageResource(R.drawable.dume_gang_grayscale_image);
            regularDumeImage.setImageResource(R.drawable.dume_regular_grayscale_image);
            instantDumeImage.setImageResource(R.drawable.dume_instant_image);

            dumeGangPriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            regularDumePriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            instantDumePriceText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            dumeGangImage.getLayoutParams().height = (int) (60 * (getResources().getDisplayMetrics().density));
            dumeGangImage.getLayoutParams().width = (int) (60 * (getResources().getDisplayMetrics().density));
            regularDumeImage.getLayoutParams().height = (int) (60 * (getResources().getDisplayMetrics().density));
            regularDumeImage.getLayoutParams().width = (int) (60 * (getResources().getDisplayMetrics().density));
            instantDumeImage.getLayoutParams().height = (int) (64 * (getResources().getDisplayMetrics().density));
            instantDumeImage.getLayoutParams().width = (int) (64 * (getResources().getDisplayMetrics().density));

            dumeGangImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            regularDumeImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            instantDumeImage.setElevation((int) (8 * (getResources().getDisplayMetrics().density)));

            dumeGangPercentageOffImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            regularDumePercentageOffImage.setElevation((int) (0 * (getResources().getDisplayMetrics().density)));
            instantDumePercentageOffImage.setElevation((int) (8 * (getResources().getDisplayMetrics().density)));

            dumeGangPercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list_inactive);
            regularDumePercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list_inactive);
            instantDumePercentageOffImage.setImageResource(R.drawable.ic_percentage_off_badge_layer_list);
            dumeGangBadgeOffLayDraw = (LayerDrawable) dumeGangPercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-20%", 3);

            regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-30%", 3);

            instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();
            DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, "-40%", 3);
            packageSearchBtn.setText("Search Instant Dume");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_default_no_landmarks);
        googleMap.setMapStyle(style);

        mMap = googleMap;
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (400 * (getResources().getDisplayMetrics().density)));

    }

    public void onGrabingPackageViewCLicked(View view) {
        mPresenter.onGrabingPackageViewIntracted(view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                super.onBackPressed();
            }
        }else if (id == R.id.action_help){
            //add function here
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private View rootView;
        private View mainRootView;
        private TextView textView;
        private WeekdaysDataSource weekdaysDataSource3;
        private GrabingPackageActivity myMainActivity;
        private CoordinatorLayout coordinatorLayout;
        private String myTime;
        private TimePickerFragment thisTimePicker;
        private android.widget.Button timePickerBtn;
        private android.widget.Button datePickerBtn;
        private DatePickerFragment thisDatePicker;
        private IndicatorSeekBar numberOfDaysSeekbar;
        private String currentDateStr;
        private String finalWeekOfDays;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myMainActivity = (GrabingPackageActivity) getActivity();
            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (fragmentPosition) {
                case 1:
                    rootView = inflater.inflate(R.layout.stu4_fragment_seven_days, container, false);
                    numberOfDaysSeekbar = rootView.findViewById(R.id.complete_seekbar);

                    numberOfDaysSeekbar.setOnSeekChangeListener(new OnSeekChangeListener() {
                        @Override
                        public void onSeeking(SeekParams seekParams) {
                            myMainActivity.hintIdOne.setText(String.format("%s days", Integer.toString(seekParams.progress)));
                        }

                        @Override
                        public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.stu4_fragment_preferred_days, container, false);
                    WeekdaysDataSource.Callback callback3 = new WeekdaysDataSource.Callback() {
                        @Override
                        public void onWeekdaysItemClicked(int i, WeekdaysDataItem weekdaysDataItem) {

                        }

                        @Override
                        public void onWeekdaysSelected(int i, ArrayList<WeekdaysDataItem> items) {
                            String selectedDays = getSelectedDaysFromWeekdaysData(items);
                            if (!TextUtils.isEmpty(selectedDays))
                                showSnackbarShort(selectedDays);
                            List<String> myWeekOfDays = Arrays.asList(selectedDays.split(","));
                            for (int fuck = 0; fuck < myWeekOfDays.size(); fuck++) {
                                if (fuck == 0) {
                                    finalWeekOfDays = firstTwo(myWeekOfDays.get(fuck));

                                } else {
                                    finalWeekOfDays = finalWeekOfDays + "," + firstThree(myWeekOfDays.get(fuck));
                                }
                            }
                            myMainActivity.hintIdTwo.setText(finalWeekOfDays);

                        }
                    };
                    weekdaysDataSource3 = new WeekdaysDataSource(myMainActivity, R.id.weekdays_sample_3, rootView)
                            .setFirstDayOfWeek(Calendar.SUNDAY)
                            .setSelectedDays(Calendar.MONDAY, Calendar.WEDNESDAY)
                            .setTextColorSelected(Color.WHITE)
                            .setFillWidth(true)
                            .setTextColorUnselectedRes(R.color.textColorPrimary)
                            .setUnselectedColor(Color.TRANSPARENT)
                            .setWeekdayItemLayoutRes(R.layout.custom_weekdays_image_view)
                            .setNumberOfLetters(1)
                            .start(callback3);


                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.stu4_fragment_time, container, false);
                    datePickerBtn = rootView.findViewById(R.id.date_picker_btn);
                    timePickerBtn = rootView.findViewById(R.id.time_picker_btn);

                    //testting the time picker here
                    thisTimePicker = new TimePickerFragment();
                    thisTimePicker.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (DateFormat.is24HourFormat(getActivity())) {
                                myTime = "" + hourOfDay + ":" + minute;
                            } else {
                                String AM_PM;
                                int myHourOfDay = hourOfDay;
                                if (hourOfDay < 12) {
                                    AM_PM = "AM";
                                } else {
                                    AM_PM = "PM";
                                }
                                if (hourOfDay >= 13) {
                                    myHourOfDay = hourOfDay - 12;
                                } else if (hourOfDay == 0) {
                                    myHourOfDay = 12;
                                }
                                myTime = "" + myHourOfDay + ":" + minute + " " + AM_PM;
                            }
                            timePickerBtn.setText(myTime);
                            myMainActivity.hintIdThree.setText(String.format("%s, %s", currentDateStr, myTime));

                        }
                    });

                    timePickerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            thisTimePicker.show(getChildFragmentManager(), "package_time_picker");
                        }
                    });

                    //testing the date picker here
                    thisDatePicker = new DatePickerFragment();
                    thisDatePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month);
                            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            currentDateStr = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(c.getTime());
                            datePickerBtn.setText(currentDateStr);
                            myMainActivity.hintIdThree.setText(String.format("%s, %s", currentDateStr, myTime));

                        }
                    });

                    datePickerBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            thisDatePicker.show(getChildFragmentManager(), "Package_date_picker");
                        }
                    });

                    break;
            }
            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {

            }
        }

        private String getSelectedDaysFromWeekdaysData(ArrayList<WeekdaysDataItem> items) {
            StringBuilder stringBuilder = new StringBuilder();
            boolean selected = false;
            for (WeekdaysDataItem dataItem : items) {
                if (dataItem.isSelected()) {
                    selected = true;
                    stringBuilder.append(dataItem.getLabel());
                    stringBuilder.append(", ");
                }
            }
            if (selected) {
                String result = stringBuilder.toString();
                return result.substring(0, result.lastIndexOf(","));
            } else return "No days selected";
        }

        public void showSnackbarShort(String message) {
            mainRootView = ((Activity) myMainActivity).getWindow().getDecorView().findViewById(android.R.id.content);
            coordinatorLayout = (CoordinatorLayout) mainRootView.findViewById(R.id.parent_coor_layout);
            if (coordinatorLayout != null) {
                showSnackbar(coordinatorLayout, message, null, Snackbar.LENGTH_SHORT, null);
            }
        }

        public Snackbar showSnackbar(@NonNull ViewGroup viewGroup, String message, String action, int length, View.OnClickListener onActionListener) {
            Snackbar snackbar = Snackbar.make(viewGroup, message, length);
            if (!TextUtils.isEmpty(action) && onActionListener != null)
                snackbar.setAction(action, onActionListener);
            snackbar.show();
            return snackbar;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DataHolderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
