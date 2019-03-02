package io.dume.dume.student.homePage;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.bootCamp.bootCampHomePage.BootCampHomePageActivity;
import io.dume.dume.common.aboutUs.AboutUsActivity;
import io.dume.dume.common.chatActivity.DemoModel;
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.common.privacyPolicy.PrivacyPolicyActivity;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.customView.HorizontalLoadViewTwo;
import io.dume.dume.model.DumeModel;
import io.dume.dume.obligation.foreignObli.PayActivity;
import io.dume.dume.service.LocationServiceHandler;
import io.dume.dume.service.MyLocationService;
import io.dume.dume.student.freeCashBack.FreeCashBackActivity;
import io.dume.dume.student.grabingInfo.GrabingInfoActivity;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.heatMap.HeatMapActivity;
import io.dume.dume.student.homePage.adapter.HomePageRatingAdapter;
import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerAdapter;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.homePage.adapter.RecentSearchAdapter;
import io.dume.dume.student.homePage.adapter.RecentSearchData;
import io.dume.dume.student.mentorAddvertise.MentorAddvertiseActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.profilePage.ProfilePageActivity;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.student.recordsPage.RecordsPageActivity;
import io.dume.dume.student.searchLoading.SearchLoadingActivity;
import io.dume.dume.student.searchResult.SearchResultActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentPayment.StudentPaymentActivity;
import io.dume.dume.student.studentSettings.StudentSettingsActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.NetworkUtil;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static io.dume.dume.util.DumeUtils.animateImage;
import static io.dume.dume.util.DumeUtils.getEndOFNest;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmapSquare;

public class HomePageActivity extends CusStuAppComMapActivity implements HomePageContract.View,
        NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, MyGpsLocationChangeListener {

    private static final String TAG = "HomePageActivity";
    private static final int RC_RECENT_SEARCH = 8989;
    HomePageContract.Presenter mPresenter;
    private Menu menu;
    private MenuItem home, records, payments, messages, notifications, heat_map, free_cashback, settings, forum, help, selectAccount, infoItem, studentProfile, mentorProfile, bootCampProfile;
    private Button switchAcountBtn;
    private NavigationView navigationView;
    private Drawable leftDrawable;
    private Drawable less;
    private Drawable more;
    private NestedScrollView nestedScrollViewContent;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private int mNotificationsCount = 0;
    private char mProfileChar = '%';
    private int mChatCount = 0;
    private int mRecPendingCount = 0, mRecAcceptedCount = 0, mRecCurrentCount = 0;
    private View llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean firstTime;
    private FrameLayout viewMusk;
    private carbon.widget.RelativeLayout searchMentorBtn;
    private AppBarLayout defaultAppBerLayout;
    private AppBarLayout secondaryAppBarLayout;
    private LinearLayout profileDataLayout;
    private GoogleMap mMap;
    private View map;
    private LinearLayout primaryNavContainer;
    private Toolbar secondaryToolbar;
    private CollapsingToolbarLayout secondaryCollapsableToolbar;
    private static final int fromFlag = 1;
    private carbon.widget.LinearLayout secondaryNavContainer;
    private SupportMapFragment mapFragment;
    private Intent locationServiceIntent;

    private MyLocationService mLocationService;
    boolean mLocationServiceIsBound;
    MyServiceConnection mConn;
    private LocationServiceHandler locationServiceHandler;
    private CoordinatorLayout coordinatorLayout;
    private ImageView startMentoringImageView;
    private ImageView freeCashbackImageView;
    private ImageView referMentorImageView;
    private RecyclerView hPageBSRecycler;
    private String[] feedbackStrings;
    private SegmentedGroup radioSegmentGroup;
    private ActionBar supportActionBarMain;
    private ActionBar supportActionBarSecond;
    private MenuItem alProfile;
    private MenuItem alNoti;
    private MenuItem alChat;
    private MenuItem alRecords;
    private LayerDrawable alProfileIcon;
    private LayerDrawable alNotiIcon;
    private LayerDrawable alChatIcon;
    private LayerDrawable alRecordsIcon;
    private int optionMenu = R.menu.stu_homepage;
    private DocumentSnapshot documentSnapshot;
    private TextView userNameTextView;
    private TextView userAddressingTextView;
    private TextView userRatingTextView;
    private carbon.widget.ImageView userDP;
    private TextView doMoreTextView;
    private TextView doMoreDetailTextView;
    private TextView promotionValidityTextView;
    private TextView promotionTextView;
    private Snackbar mySnackbar;
    private CoordinatorLayout coordiHackFab;
    private HorizontalLoadViewTwo loadViewOne;
    private NestedScrollView bottomSheetNSV;
    private RecyclerView recentSearchRV;
    private RecentSearchAdapter recentSearchAdapter;
    private static Map<String, Map<String, Object>> recently_searched;
    private BottomSheetDialog mCancelBottomSheetDialog;
    private View cancelsheetRootView;
    private HomePageRecyclerAdapter hPageBSRcyclerAdapter;
    private Snackbar enamSnackbar;
    private LinearLayout mentorAddLayout;
    private HomePageModel mModel;
    private HorizontalLoadView loadView;


    @Override
    protected void onDestroy() {
        super.onDestroy();
//       stopService(locationServiceIntent);
    }

    @Override
    public void loadPromoData(HomePageRecyclerData promoData) {
        Log.w(TAG, "loadPromoData: ");
        hPageBSRcyclerAdapter.addPromoToList(promoData);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConn = new MyServiceConnection();
        getApplicationContext().bindService(locationServiceIntent, mConn, Context.BIND_AUTO_CREATE);
        if (DumeUtils.isGpsEnabled(this)) {
            MAPCONTAINER.setVisibility(View.VISIBLE);
            primaryNavContainer.setVisibility(View.VISIBLE);
            secondaryNavContainer.setVisibility(View.GONE);
        } else {
            MAPCONTAINER.setVisibility(View.INVISIBLE);
            primaryNavContainer.setVisibility(View.GONE);
            secondaryNavContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (searchDataStore.getProfileChanged()) {
            mPresenter.getDataFromDB();
            searchDataStore.setProfileChanged(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationServiceIsBound) {
            getApplicationContext().unbindService(mConn);
            mLocationServiceIsBound = false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_activity_homepage);
        setActivityContextMap(this, fromFlag);
        //findLoadView();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        mModel = new HomePageModel(this, this);
        mPresenter = new HomePagePresenter(this, mModel);
        mPresenter.homePageEnqueue();
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        setIsNight();
        isNightConfig();

        locationServiceIntent = new Intent(this, MyLocationService.class);
        searchDataStore.setSelectedMentor(null);
//        locationServiceIntent.putExtra("FROM", "HomePageActivity" );
//        startService(locationServiceIntent);
        if (hPageBSRcyclerAdapter == null) {
            hPageBSRcyclerAdapter = new HomePageRecyclerAdapter(this, new ArrayList<>());
            hPageBSRcyclerAdapter.setWindow(getWindow());
            hPageBSRecycler.setAdapter(hPageBSRcyclerAdapter);
            hPageBSRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        enamSnackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        });
    }


    class MyServiceConnection implements ServiceConnection {
        private final String TAG = "BOOMBOOMTESTEPS";

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected()");
            MyLocationService.LocalBinder binder = (MyLocationService.LocalBinder) service;
            HomePageActivity.this.mLocationService = (MyLocationService) binder.getService();

            if (mLocationService != null) {
                mLocationService.setLocationServiceHandler(locationServiceHandler);
            }
            mLocationServiceIsBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected()");
            mLocationService = null;
            mLocationServiceIsBound = false;
        }
    }


    @Override
    public void findView() {
        loadView = findViewById(R.id.loadView);
        switchAcountBtn = findViewById(R.id.switch_account_btn);
        nestedScrollViewContent = findViewById(R.id.s_R_Layout);
        fab = findViewById(R.id.fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        drawer = findViewById(R.id.drawer_layout);
        home = menu.findItem(R.id.home_id);
        records = menu.findItem(R.id.records);
        payments = menu.findItem(R.id.payments);
        messages = menu.findItem(R.id.messages);
        notifications = menu.findItem(R.id.notifications);
        free_cashback = menu.findItem(R.id.free_cashback);
        heat_map = menu.findItem(R.id.heat_map);
        settings = menu.findItem(R.id.settings);
        forum = menu.findItem(R.id.forum);
        help = menu.findItem(R.id.help);
        selectAccount = menu.findItem(R.id.select_account);
        infoItem = menu.findItem(R.id.information_item);
        leftDrawable = switchAcountBtn.getCompoundDrawables()[0];
        less = this.getResources().getDrawable(R.drawable.less_icon);
        more = this.getResources().getDrawable(R.drawable.more_icon);
        studentProfile = menu.findItem(R.id.student);
        mentorProfile = menu.findItem(R.id.mentor);
        bootCampProfile = menu.findItem(R.id.boot_camp);
        llBottomSheet = findViewById(R.id.homeBottomSheet);
        viewMusk = findViewById(R.id.view_musk);
        searchMentorBtn = findViewById(R.id.search_mentor_btn);
        defaultAppBerLayout = findViewById(R.id.my_appbarLayout);
        secondaryToolbar = findViewById(R.id.Secondary_toolbar);
        secondaryAppBarLayout = findViewById(R.id.secondary_Appbar);
        secondaryCollapsableToolbar = findViewById(R.id.secondary_collapsing_toolbar);
        profileDataLayout = findViewById(R.id.profile_data);
        mentorAddLayout = findViewById(R.id.mentor_add_layout);
        map = findViewById(R.id.map);
        primaryNavContainer = findViewById(R.id.primary_navigation_container);
        secondaryNavContainer = findViewById(R.id.secondary_noGps_navigation_container);
        coordinatorLayout = findViewById(R.id.parent_coor_layout);
        coordiHackFab = findViewById(R.id.coordi_hack_fab);
        startMentoringImageView = findViewById(R.id.start_mentoring_imageView);
        referMentorImageView = findViewById(R.id.refer_mentor_imageView);
        freeCashbackImageView = findViewById(R.id.free_cashback_imageView);
        hPageBSRecycler = findViewById(R.id.homePage_bottomSheet_recycler);
        feedbackStrings = getResources().getStringArray(R.array.review_hint_text_dependent);
        radioSegmentGroup = findViewById(R.id.segmentGroup);
        userNameTextView = findViewById(R.id.user_name);
        userAddressingTextView = findViewById(R.id.user_addressing);
        userRatingTextView = findViewById(R.id.user_rating);
        userDP = findViewById(R.id.user_dp);
        doMoreTextView = findViewById(R.id.do_more);
        doMoreDetailTextView = findViewById(R.id.make_money_mentoring);
        promotionTextView = findViewById(R.id.promotion_text);
        promotionValidityTextView = findViewById(R.id.promotion_validity_text);
        loadViewOne = findViewById(R.id.loadViewTwo);
        bottomSheetNSV = findViewById(R.id.bottom_sheet_scroll_view);
        recentSearchRV = findViewById(R.id.recent_search_recycler);
        enamSnackbar = Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG);

    }

    @Override
    public void init() {
        menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        //initializing actionbar/toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                llBottomSheet.getLayoutParams().height = (llBottomSheet.getHeight() - secondaryAppBarLayout.getHeight() - (int) (2 * (getResources().getDisplayMetrics().density)));
                llBottomSheet.requestLayout();
                bottomSheetBehavior.onLayoutChild(coordinatorLayout, llBottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
                ViewTreeObserver obs = llBottomSheet.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });

        //secondary appbar font fix
        secondaryCollapsableToolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableToolbar.setExpandedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableToolbar.setTitle("Messages");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_white_24dp);
        secondaryToolbar.setOverflowIcon(drawable);
    }

    @Override
    public void makingCallbackInterfaces() {
        locationServiceHandler = new LocationServiceHandler() {
            @Override
            public void onGpsProviderDisabled() {
                //Toast.makeText(HomePageActivity.this, "Gps Disabled by callback", Toast.LENGTH_SHORT).show();
                MAPCONTAINER.setVisibility(View.INVISIBLE);
                primaryNavContainer.setVisibility(View.GONE);
                secondaryNavContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGpsProviderEnabled() {
                //Toast.makeText(HomePageActivity.this, "Gps enabled by callback", Toast.LENGTH_SHORT).show();
                MAPCONTAINER.setVisibility(View.VISIBLE);
                primaryNavContainer.setVisibility(View.VISIBLE);
                secondaryNavContainer.setVisibility(View.GONE);
                onCenterCurrentLocation();
            }

            @Override
            public void onNetworkDisabled() {

            }

            @Override
            public void onNetworkEnabled() {

            }

            @Override
            public void onlocationChangedByGps(Location location) {

            }

            @Override
            public void onlocationChangedByNetwork(Location location) {

            }

            @Override
            public void onLocationServiceHandlerError(Exception e) {

            }
        };
    }


    @Override
    public void configHomePage() {
        //hiding the segment group
        studentProfile.setVisible(false);
        radioSegmentGroup.setVisibility(View.GONE);
        fab.setAlpha(0.90f);
        drawer.setScrimColor(getResources().getColor(R.color.black_overlay));

        // Toolbar :: Transparent
        defaultAppBerLayout.bringToFront();
        defaultAppBerLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // Status bar :: dark
        setDarkStatusBarIcon();
        //setting toggle behavior for navigation drawer
        navigationTogglerConfig();
        //bottom sheet config
        bottomSheetCallbackConfig();
        //setting my snackbar callback
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                //network resumed make functionality available
            }

            @Override
            public void onShown(Snackbar snackbar) {
                //network unavailable make functionality unavailable
            }
        });


        if (DumeUtils.isGpsEnabled(this)) {
            MAPCONTAINER.setVisibility(View.VISIBLE);
            primaryNavContainer.setVisibility(View.VISIBLE);
            secondaryNavContainer.setVisibility(View.GONE);
        } else {
            MAPCONTAINER.setVisibility(View.INVISIBLE);
            primaryNavContainer.setVisibility(View.GONE);
            secondaryNavContainer.setVisibility(View.VISIBLE);
        }
        //initializing the bottomSheet dialogue
        mCancelBottomSheetDialog = new BottomSheetDialog(this);
        cancelsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mCancelBottomSheetDialog.setContentView(cancelsheetRootView);
        //onclick listener
        profileDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START, true);
                gotoProfilePage();
            }
        });
        mentorAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START, true);
                gotoMentorAddvertise();
            }
        });
    }

    @Override
    public void initRecentSearchRecycler(DocumentSnapshot documentSnapshot) {
        List<RecentSearchData> recentSearchData = new ArrayList<>();
        String preIdentifyOne = documentSnapshot.getString("next_rs_write");
        recently_searched = (Map<String, Map<String, Object>>) documentSnapshot.get("recent_search");
        if (recently_searched != null && recently_searched.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : recently_searched.entrySet()) {
                RecentSearchData recentSearchDataCurrent = new RecentSearchData();
                String primaryText = "";
                String secondaryText = "";
                String temp = "";
                List<String> queryListName = (List<String>) entry.getValue().get("query_list_name");
                List<String> queryList = (List<String>) entry.getValue().get("query_list");
                recentSearchDataCurrent.setCategoryName(queryList.get(0));
                for (int i = 0; i < queryListName.size(); i++) {
                    if (getEndOFNest().contains(queryListName.get(i))) {
                        primaryText = primaryText + queryListName.get(i);
                        break;
                    }
                }
                Map<String, Object> jizz = (Map<String, Object>) entry.getValue().get("jizz");
                temp = (String) jizz.get(primaryText);
                primaryText = primaryText + ": " + temp + " / ";
                temp = (String) entry.getValue().get("package_name");
                primaryText = primaryText + temp;
                recentSearchDataCurrent.setPrimaryText(primaryText);
                temp = (String) jizz.get("Gender");
                secondaryText = secondaryText + temp;
                temp = (String) jizz.get("Salary");
                secondaryText = secondaryText + " / " + temp;
                recentSearchDataCurrent.setSecondaryText(secondaryText);
                recentSearchDataCurrent.setIdentify(entry.getKey());
                recentSearchData.add(recentSearchDataCurrent);
            }
        }
        recentSearchAdapter = new RecentSearchAdapter(this, recentSearchData, preIdentifyOne) {
            @Override
            public void OnItemClicked(View v, int position, String identify) {
                Map<String, Object> clickedSearchData = recently_searched.get(identify);
                GeoPoint anchorPoint = (GeoPoint) clickedSearchData.get("anchor_point");
                searchDataStore.setAnchorPoint(new LatLng(anchorPoint.getLatitude(), anchorPoint.getLongitude()));
                searchDataStore.setPackageName((String) clickedSearchData.get("package_name"));
                searchDataStore.setJizz((Map<String, Object>) clickedSearchData.get("jizz"));
                searchDataStore.setQueryList((List<String>) clickedSearchData.get("query_list"));
                searchDataStore.setQueryListName((List<String>) clickedSearchData.get("query_list_name"));
                searchDataStore.setForWhom((Map<String, Object>) clickedSearchData.get("for_whom"));
                searchDataStore.setPreferredDays((Map<String, Object>) clickedSearchData.get("preferred_days"));
                searchDataStore.setStartDate((Map<String, Object>) clickedSearchData.get("start_date"));
                searchDataStore.setStartTime((Map<String, Object>) clickedSearchData.get("start_time"));
                Intent intent = new Intent(HomePageActivity.this, SearchLoadingActivity.class);
                intent.setAction("from_HPA");
                startActivity(intent);
            }
        };
        recentSearchRV.setAdapter(recentSearchAdapter);
        recentSearchRV.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSwitchAccount() {
        if (home.isVisible()) {
            switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, less, null);
            subMenu();
        } else {
            switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, more, null);
            mainMenu();
        }

        if (leftDrawable instanceof Animatable) {
            ((Animatable) leftDrawable).start();
        }
    }

    @Override
    public void onCenterCurrentLocation() {
        Drawable d = fab.getDrawable();
        if (d instanceof Animatable) {
            ((Animatable) d).start();
        }
        Log.d(TAG, "onClick: clicked gps icon");
        getDeviceLocation(mMap);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        mPresenter.onMenuItemInteracted(item);
        drawer.closeDrawer(GravityCompat.START, true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(optionMenu, menu);
        if (optionMenu == R.menu.stu_homepage) {
            alProfile = menu.findItem(R.id.al_display_pic);
            alProfileIcon = (LayerDrawable) alProfile.getIcon();
            DumeUtils.setBadgeChar(this, alProfileIcon, 0xfff56161, Color.BLACK, mProfileChar, 3.0f, 3.0f);

            alNoti = menu.findItem(R.id.al_notifications);
            alNotiIcon = (LayerDrawable) alNoti.getIcon();
            DumeUtils.setBadgeCount(this, alNotiIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mNotificationsCount, 3.0f, 3.0f);

            alChat = menu.findItem(R.id.al_messages);
            alChatIcon = (LayerDrawable) alChat.getIcon();
            DumeUtils.setBadgeCount(this, alChatIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mChatCount, 3.0f, 3.0f);

            alRecords = menu.findItem(R.id.al_records);
            alRecordsIcon = (LayerDrawable) alRecords.getIcon();
            if (mRecPendingCount != 0 && mRecAcceptedCount == 0 && mRecCurrentCount == 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            } else if (mRecPendingCount == 0 && mRecAcceptedCount != 0 && mRecCurrentCount == 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            } else if (mRecPendingCount == 0 && mRecAcceptedCount == 0 && mRecCurrentCount != 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 12.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 3.0f, 3.0f);
            } else if (mRecPendingCount != 0 && mRecAcceptedCount != 0 && mRecCurrentCount == 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            } else if (mRecPendingCount != 0 && mRecAcceptedCount == 0 && mRecCurrentCount != 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 12.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 8.0f, -3.4f);
            } else if (mRecPendingCount == 0 && mRecAcceptedCount != 0 && mRecCurrentCount != 0) {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 12.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 8.0f, -3.4f);
            } else {
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecPendingCount, 3.0f, 3.0f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
                DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecCurrentCount, 12.0f, 3.0f);
            }
            if (searchDataStore.getAvatarString() != null) {
                setAvatarForMenu(searchDataStore.getAvatarString());
            } else {
                viewMusk.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (searchDataStore.getAvatarString() != null) {
                            setAvatarForMenu(searchDataStore.getAvatarString());
                        }
                    }
                }, 1000L);
            }
        } else if (optionMenu == R.menu.menu_only_help) {

        }
        return super.onCreateOptionsMenu(menu);
    }

    private void hideToolbarMenu() {
        alProfile.setVisible(false);
        alNoti.setVisible(false);
        alChat.setVisible(false);
        alRecords.setVisible(false);
    }

    private void showToolbarMenu() {
        alProfile.setVisible(true);
        alNoti.setVisible(true);
        alChat.setVisible(true);
        alRecords.setVisible(true);
    }

    private void setMenuIconColor(int tint) {
        Drawable alProfileDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.alias_profile_icon, null);
        alProfileDrawable = DrawableCompat.wrap(Objects.requireNonNull(alProfileDrawable));
        DrawableCompat.setTint(alProfileDrawable, tint);
        alProfileIcon.setDrawableByLayerId(R.id.ic_al_profile_pic, alProfileDrawable);


        Drawable alRecordDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.record_icon, null);
        alRecordDrawable = DrawableCompat.wrap(Objects.requireNonNull(alRecordDrawable));
        DrawableCompat.setTint(alRecordDrawable, tint);
        alRecordsIcon.setDrawableByLayerId(R.id.ic_al_record, alRecordDrawable);

        Drawable alNotiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.noti_icon, null);
        alNotiDrawable = DrawableCompat.wrap(Objects.requireNonNull(alNotiDrawable));
        DrawableCompat.setTint(alNotiDrawable, tint);
        alNotiIcon.setDrawableByLayerId(R.id.ic_al_noti, alNotiDrawable);

        Drawable alChatDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.chat_icon, null);
        alChatDrawable = DrawableCompat.wrap(Objects.requireNonNull(alChatDrawable));
        DrawableCompat.setTint(alChatDrawable, tint);
        alChatIcon.setDrawableByLayerId(R.id.ic_al_chat, alChatDrawable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.onMenuItemInteracted(item);
        Drawable drawableGeneral = item.getIcon();
        if (drawableGeneral instanceof Animatable) {
            ((Animatable) drawableGeneral).start();
        }
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            } else {
                drawer.openDrawer(GravityCompat.START, true);
                //super.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void bottomSheetCallbackConfig() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        firstTime = true;

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    fab.animate().translationYBy((float) (60.0f * (getResources().getDisplayMetrics().density))).setDuration(60).start();
                    COMPASSBTN.animate().translationYBy((float) (54.0f * (getResources().getDisplayMetrics().density))).setDuration(60).start();
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    setDarkStatusBarIcon();
                    nestedScrollViewContent.setVisibility(View.VISIBLE);
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppBarLayout.setVisibility(View.GONE);
                    defaultAppBerLayout.setVisibility(View.VISIBLE);
                    llBottomSheet.animate().scaleY(1).setDuration(0).start();
                    //hack
                    setSupportActionBar(toolbar);
                    supportActionBarMain = getSupportActionBar();
                    if (supportActionBarMain != null) {
                        supportActionBarMain.setDisplayHomeAsUpEnabled(true);
                        supportActionBarMain.setDisplayShowHomeEnabled(true);
                        optionMenu = R.menu.stu_homepage;
                        invalidateOptionsMenu();
                    }
                    bottomSheetNSV.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.smoothScrollTo(0, 0);
                            //bottomSheetNSV.scrollTo(0, 0);
                            //bottomSheetNSV.fling(-10000);
                        }
                    });
                    bottomSheetNSV.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.scrollTo(0, 0);
                        }
                    }, 200L);

                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    nestedScrollViewContent.setVisibility(View.GONE);
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    defaultAppBerLayout.setVisibility(View.GONE);
                    //hack
                    setSupportActionBar(secondaryToolbar);
                    supportActionBarSecond = getSupportActionBar();
                    if (supportActionBarSecond != null) {
                        supportActionBarSecond.setDisplayHomeAsUpEnabled(true);
                        supportActionBarSecond.setDisplayShowHomeEnabled(true);
                        optionMenu = R.menu.menu_only_help;
                        invalidateOptionsMenu();
                    }
                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    defaultAppBerLayout.setVisibility(View.GONE);
//                    secondaryAppBarLayout.setExpanded(false);

                } else if (BottomSheetBehavior.STATE_SETTLING == newState) {
                    defaultAppBerLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    if (COMPASSBTN != null) {
                        COMPASSBTN.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    }
                    llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
                    viewMusk.animate().alpha(2 * slideOffset).setDuration(0).start();


                    if (primaryNavContainer.getVisibility() == View.VISIBLE) {
                        primaryNavContainer.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    } else if (secondaryNavContainer.getVisibility() == View.VISIBLE) {
                        secondaryNavContainer.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    }
                    defaultAppBerLayout.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    secondaryAppBarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
                }
            }
        });
    }

    @Override
    public void gotoProfilePage() {
        startActivity(new Intent(this, ProfilePageActivity.class));
    }

    @Override
    public void gotoGrabingInfoPage() {
        startActivity(new Intent(this, GrabingInfoActivity.class));
    }

    @Override
    public void gotoGrabingLocationPage() {
        SearchDataStore instance = SearchDataStore.getInstance();
        instance.setPackageName("Fucker");
        startActivity(new Intent(this, GrabingLocationActivity.class).setAction("HomePage"));
    }

    @Override
    public void gotoHeatMapActivity() {
        startActivity(new Intent(this, HeatMapActivity.class));
    }

    @Override
    public void gotoRecordsPage() {
        startActivity(new Intent(this, RecordsPageActivity.class).setAction(DumeUtils.STUDENT));
    }

    @Override
    public void gotoSettingActivity() {
        Intent settingIntent = new Intent(this, StudentSettingsActivity.class);
        Map<String, Map<String, Object>> saved_places = (Map<String, Map<String, Object>>) documentSnapshot.get("saved_places");
        settingIntent.putExtra("avatar", getAvatarString());
        settingIntent.putExtra("userName", getUserName());
        settingIntent.putExtra("phone", documentSnapshot.getString("phone_number"));
        settingIntent.putExtra("email", documentSnapshot.getString("email"));
        if (saved_places != null) {
            settingIntent.putExtra("sp_count", saved_places.size());
        }
        startActivity(settingIntent);
    }

    @Override
    public void gotoHelpActivity() {
        startActivity(new Intent(this, StudentHelpActivity.class));
    }

    @Override
    public void gotoPaymentActivity() {
        startActivity(new Intent(this, StudentPaymentActivity.class));
    }

    @Override
    public void gotoInboxActivity() {
        startActivity(new Intent(this, InboxActivity.class));
    }

    @Override
    public void gotoFreeCashBackActivity() {
        startActivity(new Intent(this, FreeCashBackActivity.class));
    }

    @Override
    public void gotoAboutUsActivity() {
        startActivity(new Intent(this, AboutUsActivity.class));
    }

    @Override
    public void gotoPrivacyPolicyActivity() {
        startActivity(new Intent(this, PrivacyPolicyActivity.class));
    }

    @Override
    public void gotoMentorAddvertise() {
        startActivity(new Intent(this, MentorAddvertiseActivity.class));
    }

    @Override
    public void gotoBootCampHomePage() {
        startActivity(new Intent(this, BootCampHomePageActivity.class));
        finish();
    }

    @Override
    public void gotoNotificationTab() {
        Intent notificationTabIntent = new Intent(this, InboxActivity.class);
        notificationTabIntent.putExtra("notiTab", 1);
        startActivity(notificationTabIntent);
    }

    @Override
    public void referMentorImageViewClicked() {
        animateImage(referMentorImageView);
    }

    @Override
    public void freeCashBackImageViewClicked() {
        animateImage(freeCashbackImageView);
    }

    @Override
    public void startMentoringImageViewClicked() {
        animateImage(startMentoringImageView);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        /*if (ISNIGHT) {
            setLightStatusBarIcon();
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_night_no_landmarks);
            googleMap.setMapStyle(style);
        } else {}*/
        setDarkStatusBarIcon();
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_default_no_landmarks);
        googleMap.setMapStyle(style);

        mMap = googleMap;
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();
        mMap.setPadding((int) (10 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (72 * (getResources().getDisplayMetrics().density)));
        getDeviceLocation(mMap);
    }

    public void navigationTogglerConfig() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //if (ISNIGHT) setLightStatusBarIcon();
                //else
                setDarkStatusBarIcon();
                switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, more, null);
                mainMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setLightStatusBarIcon();
                viewMusk.setVisibility(View.INVISIBLE);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        home.setChecked(true);
    }

    public void isNightConfig() {
        /*if (ISNIGHT) {
            Drawable navDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.drawer_menu, null);
            navDrawable = DrawableCompat.wrap(Objects.requireNonNull(navDrawable));
            DrawableCompat.setTint(navDrawable, Color.WHITE);
            Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(navDrawable);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.drawer_menu);
        }*/
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.drawer_menu);
    }

    public void subMenu() {
        home.setVisible(false);
        records.setVisible(false);
        payments.setVisible(false);
        messages.setVisible(false);
        notifications.setVisible(false);
        heat_map.setVisible(false);
        free_cashback.setVisible(false);
        forum.setVisible(false);
        help.setVisible(false);
        settings.setVisible(false);
        infoItem.setVisible(false);
        selectAccount.setVisible(true);
    }

    public void mainMenu() {
        home.setVisible(true);
        records.setVisible(true);
        payments.setVisible(true);
        messages.setVisible(true);
        notifications.setVisible(true);
        heat_map.setVisible(true);
        free_cashback.setVisible(true);
        forum.setVisible(true);
        help.setVisible(true);
        settings.setVisible(true);
        infoItem.setVisible(true);
        selectAccount.setVisible(false);
    }

    public void onNavigationHeaderClick(View view) {
        mPresenter.onViewIntracted(view);
    }

    public void onFabClicked(View view) {
        mPresenter.onViewIntracted(view);
    }

    public void onHomePageViewClicked(View view) {
        mPresenter.onViewIntracted(view);
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

    }

    /*
      Updates the count of notifications in the ActionBar starts here.
    */
    public void updateProfileBadge(char character) {
        mProfileChar = character;
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    public void updateNotificationsBadge(int count) {
        mNotificationsCount = count;
        invalidateOptionsMenu();
    }

    public void updateChatBadge(int count) {
        mChatCount = count;
        invalidateOptionsMenu();
    }

    public void updateRecordsBadge(int penCount, int acptCount, int curCount) {
        mRecPendingCount = penCount;
        mRecAcceptedCount = acptCount;
        mRecCurrentCount = curCount;
        invalidateOptionsMenu();
    }

    /*
      Updates the count of notifications in the ActionBar finishes here.
    */

    //testing the customDialogue
    @Override
    public void testingCustomDialogue(HomePageRatingData myData, Record record) {
        // custom dialog
        String keyToChange = "s_rate_status";
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_rating_dialogue);
        dialog.setCanceledOnTouchOutside(false);
        DocumentSnapshot snapshot = record.getRecordSnap();


        //all find view here
        MaterialRatingBar mDecimalRatingBars = dialog.findViewById(R.id.rated_mentor_rating_bar);
        RecyclerView itemRatingRecycleView = dialog.findViewById(R.id.rating_item_recycler);
        carbon.widget.ImageView ratedMentorDP = dialog.findViewById(R.id.rated_mentor_dp);
        TextView ratingPrimaryText = dialog.findViewById(R.id.rating_primary_text);
        TextView ratingSecondaryText = dialog.findViewById(R.id.rating_secondary_text);
        TextInputLayout feedbackTextViewLayout = dialog.findViewById(R.id.input_layout_firstname);
        EditText feedbackTextView = dialog.findViewById(R.id.feedback_textview);
        Button dismissBtn = (Button) dialog.findViewById(R.id.skip_btn);
        Button dismissBtnOne = (Button) dialog.findViewById(R.id.skip_btn_two);
        Button nextSubmitBtn = dialog.findViewById(R.id.next_btn);
        RelativeLayout dialogHostingLayout = dialog.findViewById(R.id.dialog_hosting_layout);
        Button SubmitBtn = dialog.findViewById(R.id.submit_btn);
        RelativeLayout firstLayout = dialog.findViewById(R.id.first_layout);
        RelativeLayout secondLayout = dialog.findViewById(R.id.second_layout);

        ratingPrimaryText.setText("How was your learning with " + myData.getName());
        Glide.with(getApplicationContext()).load(myData.getAvatar()).into(ratedMentorDP);

        //testing the recycle view here
        HomePageRatingAdapter itemRatingRecycleAdapter = new HomePageRatingAdapter(this, myData);
        itemRatingRecycleView.setAdapter(itemRatingRecycleAdapter);
        itemRatingRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mDecimalRatingBars.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                nextSubmitBtn.performClick();
            }
        });

        feedbackTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    int rating = mDecimalRatingBars.getProgress();
                    if (rating <= 100) {
                        String userName = myData.getName();
                        feedbackTextView.setHint("Share how " + userName + " can improve");
                    } else if (rating > 100 && rating <= 200) {
                        feedbackTextView.setHint(feedbackStrings[1]);
                    } else if (rating > 200 && rating <= 300) {
                        String userName = myData.getName();
                        feedbackTextView.setHint("Say something about " + userName);
                    } else if (rating > 300 && rating <= 400) {
                        feedbackTextView.setHint(feedbackStrings[3]);
                    } else if (rating > 400 && rating <= 500) {
                        feedbackTextView.setHint(feedbackStrings[4]);
                    }
                } else {
                    feedbackTextView.setHint(feedbackStrings[4]);
                }
            }
        });

        nextSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionSet set = new TransitionSet()
                        .addTransition(new Fade())
                        .addTransition(new Slide(Gravity.START))
                        .setInterpolator(new FastOutLinearInInterpolator());
                TransitionManager.beginDelayedTransition(dialogHostingLayout, set);
                if (nextSubmitBtn.getText().equals("Next") && mDecimalRatingBars.getProgress() != 0) {
                    firstLayout.setVisibility(View.GONE);
                    secondLayout.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(HomePageActivity.this, "please rate your experience", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SubmitBtn.setOnClickListener(view -> {
            if (feedbackTextView.getText() != null && feedbackTextView.getText().toString().equals("")) {
                feedbackTextView.setError("Please write your feedack.");
            } else if (itemRatingRecycleAdapter.getInputRating() == null) {
                flush("Make sure you hit the like or dislike thumb");
            } else {
                SubmitBtn.setEnabled(false);
                showProgress();
                mModel.submitRating(snapshot.getId(), snapshot.getString("skill_uid"), new DemoModel(context).opponentUid((List<String>) snapshot.get("participants")),
                        Google.getInstance().getAccountMajor(), itemRatingRecycleAdapter.getInputRating(), mDecimalRatingBars.getRating(), feedbackTextView.getText().toString(), new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void list) {
                                hideProgress();
                                SubmitBtn.setEnabled(true);
                                flush("from inside submitteed");
                            }

                            @Override
                            public void onError(String msg) {
                                flush(msg);
                                hideProgress();
                                SubmitBtn.setEnabled(true);
                            }
                        });
                dialog.dismiss();
            }
        });

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.changeRecordStatus(snapshot.getId(), keyToChange, Record.BOTTOM_SHEET);
                dialog.dismiss();
            }
        });
        dismissBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mModel.changeRecordStatus(snapshot.getId(), keyToChange, Record.BOTTOM_SHEET);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void gotoMentorProfile() {
        startActivity(new Intent(this, TeacherActivtiy.class));
        finish();
    }

    @Override
    public void gotoStudentProfile() {
        startActivity(new Intent(this, PayActivity.class));
        finish();
    }


    @Override
    public void flush(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void setDocumentSnapshot(DocumentSnapshot documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
        searchDataStore.setDocumentSnapshot(documentSnapshot.getData());
        searchDataStore.setUserName(getUserName());
        searchDataStore.setUserNumber(documentSnapshot.getString("phone_number"));
        searchDataStore.setUserMail(documentSnapshot.getString("email"));
        searchDataStore.setUserUid(documentSnapshot.getId());
        searchDataStore.setAvatarString(getAvatarString());
        searchDataStore.setGender(documentSnapshot.getString("gender"));
    }

    @Override
    public String getAvatarString() {
        return documentSnapshot.getString("avatar");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getSelfRating() {
        return (Map<String, Object>) documentSnapshot.get("self_rating");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getUnreadRecords() {
        return (Map<String, Object>) documentSnapshot.get("unread_records");
    }

    @Override
    public String unreadMsg() {
        return documentSnapshot.getString("unread_msg");
    }

    @Override
    public String unreadNoti() {
        return documentSnapshot.getString("unread_noti");
    }

    @Override
    public String getProfileComPercent() {
        return documentSnapshot.getString("pro_com_%");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<String> getAppliedPromo() {
        return (ArrayList<String>) documentSnapshot.get("applied_promo");
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<String> getAvailablePromo() {
        return (ArrayList<String>) documentSnapshot.get("applied_promo");
    }

    @Override
    public String generateMsgName(String first, String last) {
        return "@" + first + last;
    }

    @Override
    public String getUserName() {
        return userNameTextView.getText().toString();
    }

    @Override
    public void setUserName(String first, String last) {
        userNameTextView.setText(String.format("%s %s", first, last));
    }

    @Override
    public void setAvatar(String avatarString) {
        if (avatarString != null && !avatarString.equals("")) {
            Glide.with(this).load(avatarString).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.demo_alias_dp)).into(userDP);
        }
    }

    @Override
    public void setAvatarForMenu(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            Glide.with(this).asBitmap().load(avatar)
                    .apply(new RequestOptions().override((int) (20 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            final Bitmap roundedCornerBitmap = getRoundedCornerBitmapSquare(resource, (int) (10 * (getResources().getDisplayMetrics().density)), (int) (20 * (getResources().getDisplayMetrics().density)));
                            Drawable drawable = new BitmapDrawable(getResources(), roundedCornerBitmap);
                            if (drawable != null) {
                                alProfileIcon.setDrawableByLayerId(R.id.ic_al_profile_pic, drawable);
                                alProfile.setIcon(alProfileIcon);
                            }
                        }
                    });
        }
    }

    @Override
    public void setRating(Map<String, Object> selfRating) {
        userRatingTextView.setText(selfRating.get("star_rating") + "  ★");
    }

    @Override
    public void setMsgName(String msgName) {
        userAddressingTextView.setText(msgName);
    }

    //TODO
    @Override
    public void setAvailablePromo(ArrayList<String> availablePromo) {

    }

    //TODO
    @Override
    public void setAppliedPromo(ArrayList<String> appliedPromo) {

    }


    @Override
    public void setProfileComPercent(String num) {
        if (Integer.parseInt(num) < 100) {
            updateProfileBadge('!');
        } else {
            updateProfileBadge('%');
        }
    }

    @Override
    public void setUnreadMsg(String unreadMsg) {
        updateChatBadge(Integer.parseInt(unreadMsg));
    }

    @Override
    public void setUnreadNoti(String unreadNoti) {
        updateNotificationsBadge(Integer.parseInt(unreadNoti));
    }

    @Override
    public void setUnreadRecords(Map<String, Object> unreadRecords) {
        updateRecordsBadge(Integer.parseInt((String) unreadRecords.get("pending_count")),
                Integer.parseInt((String) unreadRecords.get("accepted_count")),
                Integer.parseInt((String) unreadRecords.get("current_count")));
    }

    @Override
    public void showSnackBar(String completePercent) {
        mySnackbar = Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) mySnackbar.getView();
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.custom_snackbar_layout_one, null);

        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText("Profile only " + completePercent + "% complete");


        layout.setPadding(0, 0, 0, 0);
        if (Integer.parseInt(completePercent) < 90) {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_yellow));
            textViewStart.setTextColor(Color.BLACK);
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_green));
            textViewStart.setTextColor(Color.WHITE);
        }
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (30 * (getResources().getDisplayMetrics().density));

        layout.setLayoutParams(parentParams);
        layout.addView(snackView, 0);
        int status = NetworkUtil.getConnectivityStatusString(context);
        if (snackbar != null && !snackbar.isShown() && status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            mySnackbar.show();
        }
    }

    /*for animation of the rating bar
     * mDecimalRatingBars.startAnimation(new RatingAnimation(mDecimalRatingBars));
     * */


    @Override
    public void switchProfileDialog(String identify) {
        TextView mainText = mCancelBottomSheetDialog.findViewById(R.id.main_text);
        TextView subText = mCancelBottomSheetDialog.findViewById(R.id.sub_text);
        Button cancelYesBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_yes_btn);
        Button cancelNoBtn = mCancelBottomSheetDialog.findViewById(R.id.cancel_no_btn);
        if (mainText != null && subText != null && cancelYesBtn != null && cancelNoBtn != null) {
            mainText.setText("Switch Profile ?");
            cancelYesBtn.setText("Yes, Switch");
            cancelNoBtn.setText("No");
            if (identify.equals(DumeUtils.TEACHER)) {
                subText.setText("Switch from student to mentor profile ...");
            } else {
                subText.setText("Switch from student to boot camp profile ...");
            }

            cancelNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCancelBottomSheetDialog.dismiss();
                }
            });

            cancelYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCancelBottomSheetDialog.dismiss();
                    showProgress();
                    if (identify.equals(DumeUtils.TEACHER)) {
                        new DumeModel(context).switchAcount(DumeUtils.TEACHER, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoMentorProfile();
                            }

                            @Override
                            public void onError(String msg) {
                                hideProgress();
                                flush("Network error 101 !!");
                            }
                        });
                    } else {
                        new DumeModel(context).switchAcount(DumeUtils.BOOTCAMP, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                gotoBootCampHomePage();
                            }

                            @Override
                            public void onError(String msg) {
                                hideProgress();
                                flush("Network error 101 !!");
                            }
                        });
                    }
                }
            });
        }
        mCancelBottomSheetDialog.show();
    }

    @Override
    public void showPercentSnak(String message, String actionName) {
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) enamSnackbar.getView();
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = LayoutInflater.from(this);
        View snackView = inflater.inflate(R.layout.teachers_snakbar_layout, null);
        // layout.setBackgroundColor(R.color.red);
        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText(message);
        TextView actionTV = snackView.findViewById(R.id.actionTV);
        actionTV.setTextColor(getResources().getColor(R.color.snack_action));
        actionTV.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ProfilePageActivity.class));
        });
        actionTV.setText(actionName);
        layout.setPadding(0, 0, 0, 0);
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (30 * (getResources().getDisplayMetrics().density));
       /* parentParams.setAnchorId(R.id.Secondary_toolbar);
        parentParams.anchorGravity = Gravity.BOTTOM;*/
        layout.setLayoutParams(parentParams);
        layout.addView(snackView, 0);
        int status = NetworkUtil.getConnectivityStatusString(this);
        enamSnackbar.show();

    }

    @Override
    public void showProgress() {
        if (loadViewOne.getVisibility() == View.INVISIBLE || loadViewOne.getVisibility() == View.GONE) {
            loadViewOne.setVisibility(View.VISIBLE);
        }
        if (!loadViewOne.isRunningAnimation()) {
            loadViewOne.startLoading();
        }
    }

    @Override
    public void hideProgress() {
        if (loadViewOne.isRunningAnimation()) {
            loadViewOne.stopLoading();
        }
        if (loadViewOne.getVisibility() == View.VISIBLE) {
            loadViewOne.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showProgressTwo() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadViewOne.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    @Override
    public void hideProgressTwo() {
        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void gotoTestingActivity() {
        startActivity(new Intent(this, SearchResultActivity.class));
    }

    @Override
    public void gotoBootCampActivity() {
        flush("Boot Camp Service is coming soon...");
    }

    @Override
    public boolean checkNull() {
        return switchAcountBtn != null;
    }

    @Override
    public void showSingleBottomSheetRating(HomePageRatingData currentRatingDataList) {
        List<HomePageRecyclerData> promoData = new ArrayList<>();
        if (hPageBSRcyclerAdapter == null) {
            hPageBSRcyclerAdapter = new HomePageRecyclerAdapter(this, promoData);
            hPageBSRecycler.setAdapter(hPageBSRcyclerAdapter);
            hPageBSRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
        hPageBSRcyclerAdapter.addNewData(currentRatingDataList);

    }
}
