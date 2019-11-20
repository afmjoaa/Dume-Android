package io.dume.dume.student.grabingPackage;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.maps.android.ui.IconGenerator;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;
import com.touchboarder.weekdaysbuttons.WeekdaysDataSource;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetBehavior;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadViewTwo;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.BaseMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.searchLoading.SearchLoadingActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.util.DatePickerFragment;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.TimePickerFragment;

import static io.dume.dume.util.DumeUtils.firstThree;
import static io.dume.dume.util.DumeUtils.firstTwo;
import static io.dume.dume.util.DumeUtils.getScreenSize;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmap;

public class GrabingPackageActivity extends BaseMapActivity implements GrabingPackageContract.View,
        MyGpsLocationChangeListener, OnMapReadyCallback {

    private static final String TAG = "GrabingPackageActivity";
    private GrabingPackageContract.Presenter mPresenter;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public ViewPager mViewPager;
    private static final int fromFlag = 4;
    private ViewPagerBottomSheetBehavior bottomSheetBehavior;
    private carbon.widget.LinearLayout llBottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout myAppBarLayout;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    public int checkPackage = -1;
    public Map<String, Object> preferredDays = null;
    public Map<String, Object> startTime = null;
    public Map<String, Object> startDate = null;
    public boolean executeClicked = false;
    public boolean executeClickedTwo = false;

    private int[] navIcons = {
            R.drawable.ic_seven_days,
            R.drawable.ic_preffered_day,
            R.drawable.ic_time
    };
    public android.widget.Button packageSearchBtn;
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
    private HorizontalLoadViewTwo loadView;
    private TextView salaryDetailText;
    private FrameLayout alwaysViewMusk;
    private String defaultUrl;
    private View mCustomMarkerView;
    private carbon.widget.ImageView mMarkerImageView;
    private IconGenerator iconFactory;
    private int SEARCH_REQUEST_CODE = 0101;
    private String gangDiscount = null;
    private String regularDiscount = null;
    private String instantDiscount = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu4_activity_grabing_package);
        setActivityContextMap(this, fromFlag);
        mPresenter = new GrabingPackagePresenter(this, new GrabingPackageModel());
        mPresenter.grabingPackagePageEnqueue();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        BottomSheetUtils.setupViewPager(mViewPager);
        //making the custom tab here
        int[] wh = DumeUtils.getScreenSize(this);
        int tabMinWidth = ((wh[0] / 3) - (int) (24 * (getResources().getDisplayMetrics().density)));
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
        //selecting regular dume as default
        regularDumeSelected();
    }

    @Override
    public void findView() {
        loadView = findViewById(R.id.loadViewTwo);
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
        salaryDetailText = findViewById(R.id.individual_promo_body);
        hintIdOne = findViewById(R.id.hint_id_1);
        hintIdTwo = findViewById(R.id.hint_id_2);
        hintIdThree = findViewById(R.id.hint_id_3);
        alwaysViewMusk = findViewById(R.id.always_view_musk);
        mCustomMarkerView = ((LayoutInflater) Objects.requireNonNull(getSystemService(LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_view, null);
        mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);
        iconFactory = new IconGenerator(this);

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

        //initializing the search data store
        List<Integer> mySelectedDaysInt = new ArrayList<>();
        String mySelectedDays = "Monday, Wednesday, Friday";
        mySelectedDaysInt.add(2);
        mySelectedDaysInt.add(4);
        mySelectedDaysInt.add(6);
        preferredDays = searchDataStore.genSetRetPreferredDays(mySelectedDays, mySelectedDaysInt);
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

        //gathering the touch event here
        alwaysViewMusk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        dumeGangBadgeOffLayDraw = (LayerDrawable) dumeGangPercentageOffImage.getDrawable();
        regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
        instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();

        Map<String, Object> documentSnapshot = searchDataStore.getDocumentSnapshot();
        ArrayList<String> applied_promo = (ArrayList<String>) documentSnapshot.get("applied_promo");
        if (applied_promo.size() > 0) {
            for (String applied : applied_promo) {
                Log.w(TAG, "appliedPromo: " + applied);
                Map<String, Object> promo_item = (Map<String, Object>) documentSnapshot.get(applied);
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(promo_item);
                HomePageRecyclerData homePageRecyclerData = gson.fromJson(jsonElement, HomePageRecyclerData.class);
                if (homePageRecyclerData != null) {
                    if (homePageRecyclerData.getPackageName().equals(SearchDataStore.DUME_GANG)) {
                        dumeGangPercentageOffImage.setVisibility(View.VISIBLE);
                        Integer max_dicount_percentage = homePageRecyclerData.getMax_dicount_percentage();
                        gangDiscount = "-" + max_dicount_percentage.toString() + "%";
                        DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, gangDiscount, 3);
                    } else if (homePageRecyclerData.getPackageName().equals(SearchDataStore.REGULAR_DUME)) {
                        regularDumePercentageOffImage.setVisibility(View.VISIBLE);
                        Integer max_dicount_percentage = homePageRecyclerData.getMax_dicount_percentage();
                        regularDiscount = "-" + max_dicount_percentage.toString() + "%";
                        DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, regularDiscount, 3);
                    } else {
                        instantDumePercentageOffImage.setVisibility(View.VISIBLE);
                        Integer max_dicount_percentage = homePageRecyclerData.getMax_dicount_percentage();
                        instantDiscount = "-" + max_dicount_percentage.toString() + "%";
                        DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, instantDiscount, 3);
                    }
                }
            }
        }
    }

    @Override
    public void executeSearchActivity() {
        Calendar instance = Calendar.getInstance();
        /*if (preferredDays != null && startDate != null && startTime != null) {
            //handle at last
        } else if (preferredDays == null) {
            flush("Internal error !!");
        } else */
        if (startDate == null && startTime == null) {
            //TODO testing auto time date
            int hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
            int minute = instance.get(Calendar.MINUTE);
            String myTime;
            if (DateFormat.is24HourFormat(this)) {
                if (minute < 10) {
                    myTime = "" + hourOfDay + ":0" + minute;
                } else {
                    myTime = "" + hourOfDay + ":" + minute;
                }
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
                if (minute < 10) {
                    myTime = "" + myHourOfDay + ":0" + minute + " " + AM_PM;
                } else {
                    myTime = "" + myHourOfDay + ":" + minute + " " + AM_PM;
                }
            }
            //timePickerEt.setText(myTime);
            hintIdThree.setText(String.format("%s", myTime));
            startTime = searchDataStore.genSetRetStartTime(hourOfDay, minute, myTime);

            //now date
            //datePickerEt.setText(currentDateStr);
            instance.add(Calendar.DATE, 1);
            String currentDateStr = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(instance.getTime());
            hintIdTwo.setText(String.format("%s", currentDateStr));
            startDate = searchDataStore.genSetRetStartDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), currentDateStr);
        } else if (startDate == null) {
            /*executeClicked = true;
            flush("please select start date...");
            mViewPager.setCurrentItem(1, true);*/

            //now date
            //datePickerEt.setText(currentDateStr);
            instance.add(Calendar.DATE, 1);
            String currentDateStr = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(instance.getTime());
            hintIdTwo.setText(String.format("%s", currentDateStr));
            startDate = searchDataStore.genSetRetStartDate(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), currentDateStr);


        } else {
            /*executeClickedTwo = true;
            flush("please select start time...");
            mViewPager.setCurrentItem(2, true);*/

            //time here
            int hourOfDay = instance.get(Calendar.HOUR_OF_DAY);
            int minute = instance.get(Calendar.MINUTE);
            String myTime;
            if (DateFormat.is24HourFormat(this)) {
                if (minute < 10) {
                    myTime = "" + hourOfDay + ":0" + minute;
                } else {
                    myTime = "" + hourOfDay + ":" + minute;
                }
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
                if (minute < 10) {
                    myTime = "" + myHourOfDay + ":0" + minute + " " + AM_PM;
                } else {
                    myTime = "" + myHourOfDay + ":" + minute + " " + AM_PM;
                }
            }
            //timePickerEt.setText(myTime);
            hintIdThree.setText(String.format("%s", myTime));
            startTime = searchDataStore.genSetRetStartTime(hourOfDay, minute, myTime);
        }
        switch (checkPackage) {
            case 0:
                searchDataStore.setPackageName(SearchDataStore.DUME_GANG);
                break;
            case 1:
                searchDataStore.setPackageName(SearchDataStore.REGULAR_DUME);
                break;
            case 2:
                searchDataStore.setPackageName(SearchDataStore.INSTANT_DUME);
                break;
        }
        Intent intent = new Intent(this, SearchLoadingActivity.class);
        intent.setAction("from_GPA");
        startActivity(intent);
        finish();
    }

    @Override
    public void dumeGangSelected() {
        checkPackage = 0;
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

            //testing
            capacityTextValue.setText("10 Person");
            salaryTextValue.setText("BDT 1k-3k");
            salaryDetailText.setText(getResources().getString(R.string.gang_salary_detail));
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
            regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
            instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();

            if (gangDiscount != null) {
                dumeGangPercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, gangDiscount, 3);
            } else {
                dumeGangPercentageOffImage.setVisibility(View.INVISIBLE);
                //DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, "-0%", 3);
            }
            if (regularDiscount != null) {
                regularDumePercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, regularDiscount, 3);
            } else {
                regularDumePercentageOffImage.setVisibility(View.INVISIBLE);
                //DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-0%", 3);
            }
            if (instantDiscount != null) {
                instantDumePercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, instantDiscount, 3);
            } else {
                instantDumePercentageOffImage.setVisibility(View.INVISIBLE);
                //DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, "-0%", 3);
            }
            packageSearchBtn.setText("Search Coaching Service");
        }
    }

    @Override
    public void flush(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void regularDumeSelected() {
        checkPackage = 1;
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

            capacityTextValue.setText("1-2 Person");
            salaryTextValue.setText("BDT 3k-12k");
            salaryDetailText.setText(getResources().getString(R.string.regular_salary_detail));
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
            regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
            instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();

            if (gangDiscount != null) {
                dumeGangPercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, 0xff575757, gangDiscount, 3);
            } else {
                dumeGangPercentageOffImage.setVisibility(View.INVISIBLE);
            }
            if (regularDiscount != null) {
                regularDumePercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, regularDiscount, 3);
            } else {
                regularDumePercentageOffImage.setVisibility(View.INVISIBLE);
            }
            if (instantDiscount != null) {
                instantDumePercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, instantDiscount, 3);
            } else {
                instantDumePercentageOffImage.setVisibility(View.INVISIBLE);
            }
            packageSearchBtn.setText("Search Monthly Tutor");
        }
    }

    @Override
    public void instantDumeSelected() {
        checkPackage = 2;
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

            capacityTextValue.setText("1 Person");
            salaryTextValue.setText("BDT 5k-15k");
            salaryDetailText.setText(getResources().getString(R.string.instant_salary_detail));
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
            regularDumeBadgeOffLayDraw = (LayerDrawable) regularDumePercentageOffImage.getDrawable();
            instantDumeBadgeOffLayDraw = (LayerDrawable) instantDumePercentageOffImage.getDrawable();

            if (gangDiscount != null) {
                dumeGangPercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, dumeGangBadgeOffLayDraw, R.id.ic_badge, 0xff575757, gangDiscount, 3);
            } else {
                dumeGangPercentageOffImage.setVisibility(View.INVISIBLE);
            }
            if (regularDiscount != null) {
                regularDumePercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, regularDumeBadgeOffLayDraw, R.id.ic_badge, 0xff575757, regularDiscount, 3);
            } else {
                regularDumePercentageOffImage.setVisibility(View.INVISIBLE);
            }
            if (instantDiscount != null) {
                instantDumePercentageOffImage.setVisibility(View.VISIBLE);
                DumeUtils.setTextOverDrawable(this, instantDumeBadgeOffLayDraw, R.id.ic_badge, Color.WHITE, instantDiscount, 3);
            } else {
                instantDumePercentageOffImage.setVisibility(View.INVISIBLE);
            }
            packageSearchBtn.setText("Search Weekly Tutor");
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
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (400 * (getResources().getDisplayMetrics().density)));
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.setMyLocationEnabled(false);
        onMapReadyGeneralConfig();
        addCustomMarkerFromURL(searchDataStore.getAvatarString(), searchDataStore.getAnchorPoint());
        alwaysViewMusk.postDelayed(new Runnable() {
            @Override
            public void run() {
                //give anchor point here
                moveCamera(searchDataStore.getAnchorPoint(), DEFAULT_ZOOM, "Device Location", mMap);
            }
        }, 0L);
    }

    private void addCustomMarkerFromURL(String url, LatLng lattitudeLongitude) {
        if (mMap == null) {
            return;
        }
        if (url != null && !url.equals("")) {
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(url)
                    .apply(new RequestOptions().override((int) (28 * (getResources().getDisplayMetrics().density)), (int) (28 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            mMap.addMarker(new MarkerOptions().position(lattitudeLongitude)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, resource))));
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lattitudeLongitude, 15f));
                        }
                    });
        } else {
            if (searchDataStore.getGender().equals("Male") || searchDataStore.getGender().equals("")) {
                defaultUrl = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/avatar.png?alt=media&token=801c75b7-59fe-4a13-9191-186ef50de707";
            } else {
                defaultUrl = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/avatar_female.png?alt=media&token=7202ea91-4f0d-4bd6-838e-8b73d0db13eb";
            }
            Glide.with(getApplicationContext())
                    .asBitmap()
                    .load(defaultUrl)
                    .apply(new RequestOptions().override((int) (28 * (getResources().getDisplayMetrics().density)), (int) (28 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            mMap.addMarker(new MarkerOptions().position(lattitudeLongitude)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, resource))));
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lattitudeLongitude, 15f));
                        }
                    });
        }
    }

    //testing custom marker code here
    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        mMarkerImageView.setImageBitmap(getRoundedCornerBitmap(bitmap, (int) (28 * (getResources().getDisplayMetrics().density))));
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;

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
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(context, StudentHelpActivity.class);
            intent.setAction("how_to_use");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showProgress() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    public void hideProgress() {
        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
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
        private DatePickerFragment thisDatePicker;
        private String currentDateStr;
        private String finalWeekOfDays;
        private EditText timePickerEt;
        private carbon.widget.ImageView emptyTPF;
        private EditText datePickerEt;
        private carbon.widget.ImageView emptyDPF;

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
                    rootView = inflater.inflate(R.layout.stu4_fragment_preferred_days, container, false);
                    weekdaysDataSource3 = new WeekdaysDataSource(myMainActivity, R.id.weekdays_sample_3, rootView)
                            .setFirstDayOfWeek(Calendar.SUNDAY)
                            .setSelectedDays(Calendar.MONDAY, Calendar.WEDNESDAY, Calendar.FRIDAY)
                            .setTextColorSelected(Color.WHITE)
                            .setFillWidth(true)
                            .setTextColorUnselectedRes(R.color.textColorPrimary)
                            .setUnselectedColor(Color.TRANSPARENT)
                            .setWeekdayItemLayoutRes(R.layout.custom_weekdays_image_view)
                            .setNumberOfLetters(1)
                            .start(new WeekdaysDataSource.Callback() {
                                @Override
                                public void onWeekdaysItemClicked(int i, WeekdaysDataItem weekdaysDataItem) {
                                }

                                @Override
                                public void onWeekdaysSelected(int i, ArrayList<WeekdaysDataItem> items) {
                                    String selectedDays = getSelectedDaysFromWeekdaysData(items);
                                    List<String> myWeekOfDays = Arrays.asList(selectedDays.split(","));
                                    List<Integer> selectedDaysInt = new ArrayList<>();
                                    for (int fuck = 0; fuck < myWeekOfDays.size(); fuck++) {
                                        if (fuck == 0) {
                                            finalWeekOfDays = firstTwo(myWeekOfDays.get(fuck));

                                        } else {
                                            finalWeekOfDays = finalWeekOfDays + "," + firstThree(myWeekOfDays.get(fuck));
                                        }
                                    }
                                    myMainActivity.hintIdOne.setText(finalWeekOfDays);
                                    //depends on which
                                    if (myMainActivity.checkPackage == 0 || myMainActivity.checkPackage == 1) {
                                        if (weekdaysDataSource3.isAllDaysSelected()) {
                                            weekdaysDataSource3.selectAll(false);
                                            weekdaysDataSource3.setSelectedDays(Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY);
                                            myMainActivity.flush("Max 6 days/week allowed for Regular Dume");
                                        }
                                    }
                                    for (WeekdaysDataItem dataItem : items) {
                                        if (dataItem.isSelected()) {
                                            selectedDaysInt.add(dataItem.getCalendarDayId());
                                        }
                                    }
                                    myMainActivity.preferredDays = myMainActivity.searchDataStore.genSetRetPreferredDays(selectedDays, selectedDaysInt);
                                   /* if (!TextUtils.isEmpty(selectedDays)) {
                                        showSnackbarShort(selectedDays);
                                    }*/
                                }
                            });

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.stu4_fragment_date_picker, container, false);
                    RelativeLayout hostingRelativeDP = rootView.findViewById(R.id.date_picker_relative);
                    datePickerEt = rootView.findViewById(R.id.date_picker_Et);
                    emptyDPF = rootView.findViewById(R.id.empty_startDate_found);
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
                            datePickerEt.setText(currentDateStr);
                            myMainActivity.hintIdTwo.setText(String.format("%s", currentDateStr));
                            myMainActivity.startDate = myMainActivity.searchDataStore.genSetRetStartDate(year, month, dayOfMonth, currentDateStr);
                            //myMainActivity.mViewPager.setCurrentItem(myMainActivity.mViewPager.getCurrentItem() + 1, true);
                        }
                    });
                    datePickerEt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            thisDatePicker.show(getChildFragmentManager(), "Package_date_picker");
                        }
                    });
                    datePickerEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!s.equals("")) {
                                emptyDPF.setVisibility(View.GONE);
                            } else {
                                emptyDPF.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString().equals("")) {
                                emptyDPF.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.stu4_fragment_time, container, false);
                    RelativeLayout hostingRelativeTP = rootView.findViewById(R.id.time_picker_relative);
                    timePickerEt = rootView.findViewById(R.id.time_picker_Et);
                    emptyTPF = rootView.findViewById(R.id.empty_startTime_found);

                    //testting the time picker here
                    thisTimePicker = new TimePickerFragment();
                    thisTimePicker.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (DateFormat.is24HourFormat(getActivity())) {
                                if (minute < 10) {
                                    myTime = "" + hourOfDay + ":0" + minute;
                                } else {
                                    myTime = "" + hourOfDay + ":" + minute;
                                }
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
                                if (minute < 10) {
                                    myTime = "" + myHourOfDay + ":0" + minute + " " + AM_PM;
                                } else {
                                    myTime = "" + myHourOfDay + ":" + minute + " " + AM_PM;
                                }
                            }
                            timePickerEt.setText(myTime);
                            myMainActivity.hintIdThree.setText(String.format("%s", myTime));
                            myMainActivity.startTime = myMainActivity.searchDataStore.genSetRetStartTime(hourOfDay, minute, myTime);
                        }
                    });

                    timePickerEt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            thisTimePicker.show(getChildFragmentManager(), "package_time_picker");
                        }
                    });
                    timePickerEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (!s.equals("")) {
                                emptyTPF.setVisibility(View.GONE);
                            } else {
                                emptyTPF.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString().equals("")) {
                                emptyTPF.setVisibility(View.VISIBLE);
                            }
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
                if (getArguments() != null) {
                    myMainActivity = (GrabingPackageActivity) getActivity();
                    int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
                    switch (fragmentPosition) {
                        case 1:
                            break;
                        case 2:
                            if (myMainActivity.executeClicked) {
                                if (datePickerEt.getText().toString().equals("")) {
                                    emptyDPF.setVisibility(View.VISIBLE);
                                }
                                myMainActivity.executeClicked = false;
                            }
                            break;
                        case 3:
                            try{
                                if (myMainActivity.executeClickedTwo) {
                                    if (timePickerEt.getText().toString().equals("")) {
                                        emptyTPF.setVisibility(View.VISIBLE);
                                    }
                                    myMainActivity.executeClickedTwo = false;
                                }
                            }catch (Exception err){
                                Log.e(TAG, err.getLocalizedMessage());
                            }
                            break;
                    }
                }
            } else {
                //not visible here
            }
        }

        public static String getSelectedDaysFromWeekdaysData(ArrayList<WeekdaysDataItem> items) {
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
            } else return "No-days selected";
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
