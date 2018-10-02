package io.dume.dume.student.homePage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.jaeger.library.StatusBarUtil;

import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.auth.auth.AppbarStateChangeListener;
import io.dume.dume.common.aboutUs.AboutUsActivity;
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.common.privacyPolicy.PrivacyPolicyActivity;
import io.dume.dume.service.LocationServiceHandler;
import io.dume.dume.service.MyLocationService;
import io.dume.dume.student.freeCashBack.FreeCashBackActivity;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.grabingInfo.GrabingInfoActivity;
import io.dume.dume.student.heatMap.HeatMapActivity;
import io.dume.dume.student.mentorAddvertise.MentorAddvertiseActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.profilePage.ProfilePageActivity;
import io.dume.dume.student.recordsPage.RecordsPageActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentPayment.StudentPaymentActivity;
import io.dume.dume.student.studentSettings.StudentSettingsActivity;
import io.dume.dume.util.DumeUtils;

public class HomePageActivity extends CusStuAppComMapActivity implements HomePageContract.View,
        NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, HomePageContract.ParentCallback,
        MyGpsLocationChangeListener {

    private static final String TAG = "HomePageActivity";
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
    private char mProfileChar = '!';
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
    private HomePageContract.ParentCallback parentCallback;
    private carbon.widget.LinearLayout secondaryNavContainer;
    private SupportMapFragment mapFragment;
    private Intent locationServiceIntent;

    private MyLocationService mLocationService;
    boolean mLocationServiceIsBound;
    MyServiceConnection mConn;
    private LocationServiceHandler locationServiceHandler;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(locationServiceIntent);
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
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
        mPresenter = new HomePagePresenter(this, new HomePageModel());
        mPresenter.homePageEnqueue();
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        setIsNight();
        isNightConfig();

        locationServiceIntent = new Intent(this, MyLocationService.class);
//        locationServiceIntent.putExtra("FROM", "HomePageActivity" );
//        startService(locationServiceIntent);
    }


    class MyServiceConnection implements ServiceConnection {
        private final String TAG = "BOOMBOOMTESTEPS";

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected()");
            MyLocationService.LocalBinder binder = (MyLocationService.LocalBinder) service;
            HomePageActivity.this.mLocationService = (MyLocationService) binder.getService();
           /* if (mLocationService == null) {
                Log.e(TAG, "onServiceConnected: you haven't fucked that");
            } else {
                Log.e(TAG, "onServiceConnected: urecaaaaaaaaaaaaaaaaaaaa");
            }*/
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
        map = findViewById(R.id.map);
        primaryNavContainer = findViewById(R.id.primary_navigation_container);
        secondaryNavContainer = findViewById(R.id.secondary_noGps_navigation_container);
        coordinatorLayout = findViewById(R.id.parent_coor_layout);
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
        //setting up parent callback listener
        setParentCallback(this);

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
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stu_homepage, menu);
        //testing visibility  -----   menu.findItem(R.id.al_messages).setVisible(false);

        MenuItem alProfile = menu.findItem(R.id.al_display_pic);
        LayerDrawable alProfileIcon = (LayerDrawable) alProfile.getIcon();
        DumeUtils.setBadgeChar(this, alProfileIcon, 0xfff56161, Color.BLACK, mProfileChar, 3.0f, 3.0f);

        MenuItem alNoti = menu.findItem(R.id.al_notifications);
        LayerDrawable alNotiIcon = (LayerDrawable) alNoti.getIcon();
        DumeUtils.setBadgeCount(this, alNotiIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mNotificationsCount, 3.0f, 3.0f);

        MenuItem alChat = menu.findItem(R.id.al_messages);
        LayerDrawable alChatIcon = (LayerDrawable) alChat.getIcon();
        DumeUtils.setBadgeCount(this, alChatIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mChatCount, 3.0f, 3.0f);

        MenuItem alRecords = menu.findItem(R.id.al_records);
        LayerDrawable alRecordsIcon = (LayerDrawable) alRecords.getIcon();
        DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecCurrentCount, 3.0f, 3.0f);
        DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
        DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecPendingCount, 12.0f, 3.0f);

        if (ISNIGHT && false) {
            Drawable alProfileDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.alias_profile_icon, null);
            alProfileDrawable = DrawableCompat.wrap(Objects.requireNonNull(alProfileDrawable));
            DrawableCompat.setTint(alProfileDrawable, Color.WHITE);
            alProfileIcon.setDrawableByLayerId(R.id.ic_al_profile_pic, alProfileDrawable);

            Drawable alRecordDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.record_icon, null);
            alRecordDrawable = DrawableCompat.wrap(Objects.requireNonNull(alRecordDrawable));
            DrawableCompat.setTint(alRecordDrawable, Color.WHITE);
            alRecordsIcon.setDrawableByLayerId(R.id.ic_al_record, alRecordDrawable);

            Drawable alNotiDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.noti_icon, null);
            alNotiDrawable = DrawableCompat.wrap(Objects.requireNonNull(alNotiDrawable));
            DrawableCompat.setTint(alNotiDrawable, Color.WHITE);
            alNotiIcon.setDrawableByLayerId(R.id.ic_al_noti, alNotiDrawable);

            Drawable alChatDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.chat_icon, null);
            alChatDrawable = DrawableCompat.wrap(Objects.requireNonNull(alChatDrawable));
            DrawableCompat.setTint(alChatDrawable, Color.WHITE);
            alChatIcon.setDrawableByLayerId(R.id.ic_al_chat, alChatDrawable);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        mPresenter.onMenuItemInteracted(item);
        Drawable drawableGeneral = item.getIcon();
        if (drawableGeneral instanceof Animatable) {
            ((Animatable) drawableGeneral).start();
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
                    if (ISNIGHT) setLightStatusBarIcon();
                    else setDarkStatusBarIcon();
                    nestedScrollViewContent.setVisibility(View.VISIBLE);
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppBarLayout.setVisibility(View.GONE);
                    defaultAppBerLayout.setVisibility(View.VISIBLE);
                    llBottomSheet.animate().scaleY(1).setDuration(0).start();

                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    nestedScrollViewContent.setVisibility(View.GONE);
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    defaultAppBerLayout.setVisibility(View.GONE);


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
                    COMPASSBTN.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
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
        startActivity(new Intent(this, GrabingLocationActivity.class));

    }

    @Override
    public void gotoHeatMapActivity() {
        startActivity(new Intent(this, HeatMapActivity.class));
    }

    @Override
    public void gotoRecordsPage() {
        startActivity(new Intent(this, RecordsPageActivity.class));
    }

    @Override
    public void gotoSettingActivity() {
        startActivity(new Intent(this, StudentSettingsActivity.class));
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
    public void gotoNotificationTab() {
        Intent notificationTabIntent = new Intent(this, InboxActivity.class);
        notificationTabIntent.putExtra("notiTab", 1);
        startActivity(notificationTabIntent);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        if (ISNIGHT) {
            setLightStatusBarIcon();
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_night_no_landmarks);
            googleMap.setMapStyle(style);
        } else {
            setDarkStatusBarIcon();
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style_default_no_landmarks);
            googleMap.setMapStyle(style);
        }
        mMap = googleMap;
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();
    }

    public void navigationTogglerConfig() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (ISNIGHT) setLightStatusBarIcon();
                else setDarkStatusBarIcon();
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
        if (ISNIGHT) {
            Drawable navDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.drawer_menu, null);
            navDrawable = DrawableCompat.wrap(Objects.requireNonNull(navDrawable));
            DrawableCompat.setTint(navDrawable, Color.WHITE);
            Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(navDrawable);


        } else {
            Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.drawer_menu);
        }
    }

    /*public void secondaryAppbarChangerListener() {
        secondaryAppBarLayout.addOnOffsetChangedListener(new AppbarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.toString().equals("EXPANDED") &&
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (state.toString().equals("COLLAPSED") &&
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                }
            }
        });
    }*/

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
    public void onNetworkPause() {
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN && !firstTime) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    @Override
    public void onNetworkResume() {
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


}
