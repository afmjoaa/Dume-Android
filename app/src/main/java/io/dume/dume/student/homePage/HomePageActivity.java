package io.dume.dume.student.homePage;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.dume.dume.R;
import io.dume.dume.components.customView.HorizontalLoadView;
import io.dume.dume.components.customView.HorizontalLoadViewTwo;
import io.dume.dume.commonActivity.model.DumeModel;
import io.dume.dume.components.services.LocationServiceHandler;
import io.dume.dume.components.services.MyLocationService;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerAdapter;
import io.dume.dume.student.homePage.adapter.RecentSearchAdapter;
import io.dume.dume.student.homePage.adapter.RecentSearchData;
import io.dume.dume.student.pojo.BaseMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.profilePage.ProfilePageActivity;
import io.dume.dume.student.searchLoading.SearchLoadingActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.NetworkUtil;

import static io.dume.dume.util.DumeUtils.getEndOFNest;
import static io.dume.dume.util.DumeUtils.hideKeyboard;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmapSquare;

public class HomePageActivity extends BaseMapActivity implements HomePageContract.View,
        NavigationView.OnNavigationItemSelectedListener, MyGpsLocationChangeListener {

    private static final String TAG = "HomePageActivity";
    private static final int RC_RECENT_SEARCH = 8989;
    HomePageContract.Presenter mPresenter;
    private Menu menu;
    private MenuItem home, records, payments, messages, notifications, heat_map, free_cashback, settings, forum, help, selectAccount, infoItem, studentProfile, mentorProfile, bootCampProfile;
    private Button switchAcountBtn;
    private NavigationView navigationView;
    private Drawable leftDrawable;
    private Drawable filterDrawable;
    private Drawable filterDrawableOne;
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
    private BottomSheetDialog filterBottomSheetDialog;
    private View filterRootView;
    private HomePageRecyclerAdapter hPageBSRcyclerAdapter;
    private Snackbar enamSnackbar;
    private LinearLayout mentorAddLayout;
    private HomePageModel mModel;
    private HorizontalLoadView loadView;
    public LinearLayout hackHeight;
    private TextView promotionTV;
    private TextView promotionExpireDate;
    private Integer discount = 0;
    private carbon.widget.LinearLayout headsUpPromoContainer;
    private TextView dumeInfo;
    private LinearLayout dumeInfoContainer;
    private Button learnMoreBtnOne;
    private Button startCouching;
    private Button startTakingCouching;
    private TextView referLearnMore;
    //private Button referMentorBtn;
    private TextView how_invite_works;
    private Button freeCashBack;
    private Button startMentoringBtn;
    private SharedPreferences sharedPreferences;
    private carbon.widget.ImageView searchFilterBtn;
    private carbon.widget.ImageView searchFilterBtnOne;
    private List<String> selectedUnis;
    private boolean[] checkedUnis;
    private List<String> selectedDegrees;
    private boolean[] checkedItems;
    private SharedPreferences prefs;
    private Dialog dialog;


    @Override
    protected void onDestroy() {
        super.onDestroy();
//       stopService(locationServiceIntent);
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
        sharedPreferences = context.getSharedPreferences(UNREAD_MESSAGE, MODE_PRIVATE);
        updateChatBadge(sharedPreferences.getInt("unread", 0));
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
        mPresenter = new HomePagePresenter(this,this, mModel);
        mPresenter.homePageEnqueue();
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
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

        prefs = getSharedPreferences("filter", MODE_PRIVATE);

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
        hackHeight = findViewById(R.id.hack_height);
        enamSnackbar = Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG);
        promotionTV = findViewById(R.id.promotion_text);
        promotionExpireDate = findViewById(R.id.promotion_validity_text);
        headsUpPromoContainer = findViewById(R.id.percent_off_block);
        dumeInfo = findViewById(R.id.dume_info);
        dumeInfoContainer = findViewById(R.id.dume_info_container);

        learnMoreBtnOne = findViewById(R.id.learn_more_btn_one);
        startCouching = findViewById(R.id.start_couching);
        startTakingCouching = findViewById(R.id.start_taking_couching);
        referLearnMore = findViewById(R.id.refer_learn_more_tv);
        //referMentorBtn = findViewById(R.id.refer_mentor_btn);
        how_invite_works = findViewById(R.id.how_invite_works);
        freeCashBack = findViewById(R.id.free_cashback_Btn);
        startMentoringBtn = findViewById(R.id.start_mentoring_btn);

        //finding the filter btn image
        searchFilterBtn = findViewById(R.id.search_filter_image_view);
        searchFilterBtnOne = findViewById(R.id.nogps_search_filter_image);
        filterDrawable = searchFilterBtn.getDrawable();
        filterDrawableOne = searchFilterBtnOne.getDrawable();
        bottomSheetBtnCallback();
    }

    public void bottomSheetBtnCallback() {


        learnMoreBtnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("how_to_use");
                startActivity(intent);
            }
        });
        startCouching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("whats_new");
                startActivity(intent);
            }
        });
        startTakingCouching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("whats_new");
                startActivity(intent);
            }
        });

        referLearnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("faq");
                startActivity(intent);
            }
        });
        how_invite_works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("faq");
                startActivity(intent);
            }
        });
        freeCashBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteAFriendCalled();
            }
        });
        startMentoringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchProfileDialog(DumeUtils.TEACHER);
            }
        });

    }

    private void inviteAFriendCalled() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TITLE, "Dume");
            String strShareMessage = "Check out Dume, It's simple just share your skill and earn money.Get it for free from\n\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            startActivity(Intent.createChooser(i, "Share via"));
        } catch (Exception e) {
            Log.e(TAG, "inviteAFriendCalled: " + e.toString());
        }
    }


    @Override
    public void init() {
        /*if (Google.getInstance().getTotalStudent()>100&& Google.getInstance().getTotalMentor()>100) {
            dumeInfoContainer.setVisibility(View.VISIBLE);
            //dumeInfo.setText(Google.getInstance().getTotalStudent() +" students & "+Google.getInstance().getTotalMentor() +" mentors on dume network");
            dumeInfo.setText("Dume has "+Google.getInstance().getTotalStudent() +" student & "+Google.getInstance().getTotalMentor() +" mentor accros");
        }*/
        dumeInfoContainer.setVisibility(View.GONE);
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



    public void configHomePage() {
        //hiding the segment group
        dialog = new Dialog(context);
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
        snackBar.addCallback(new Snackbar.Callback() {
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

        //initializing the filter dialogue here
        filterBottomSheetDialog = new BottomSheetDialog(this);
        filterRootView = this.getLayoutInflater().inflate(R.layout.filter_bottom_sheet_dialogue, null);
        filterBottomSheetDialog.setContentView(filterRootView);
        //onclick listener
        profileDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START, true);
                gotoProfilePage();
            }
        });
    }

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
                if (temp != null) {
                    if (temp.equals(SearchDataStore.REGULAR_DUME)) {
                        temp = "Monthly Tutor";
                    } else if (temp.equals(SearchDataStore.INSTANT_DUME)) {
                        temp = "Weekly Tutor";
                    } else {
                        temp = "Coaching";
                    }
                }
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
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

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


        Drawable alRecordDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.tuition_icon, null);
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
                    hideKeyboard(HomePageActivity.this);
                    hackHeight.setVisibility(View.GONE);

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

    public void gotoProfilePage() {
        startActivity(new Intent(this, ProfilePageActivity.class));
    }


    @Override
    public void gotoGrabingLocationPage() {
        Boolean chooseLocationRadio1 = prefs.getBoolean("chooseLocationRadio", true);
        if (chooseLocationRadio1) {
            startActivity(new Intent(this, GrabingLocationActivity.class).setAction("HomePage"));
        } else {
            GeoPoint current_address = documentSnapshot.getGeoPoint("current_address");
            if (current_address != null) {
                if (Objects.requireNonNull(current_address).getLatitude() != 84.9 && current_address.getLongitude() != 180) {
                    searchDataStore.setAnchorPoint(new LatLng(current_address.getLatitude(), current_address.getLongitude()));
                }
                startActivity(new Intent(this, CrudSkillActivity.class).setAction(DumeUtils.STUDENT));
            } else {
                startActivity(new Intent(this, GrabingLocationActivity.class).setAction("HomePage"));
            }
        }
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
    @Override
    public void flush(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public String getAvatarString() {
        return documentSnapshot.getString("avatar");
    }

    public Map<String, Object> getSelfRating() {
        return (Map<String, Object>) documentSnapshot.get("self_rating");
    }

    public String generateMsgName(String first, String last) {
        return "@" + first + last;
    }

    public String getUserName() {
        return userNameTextView.getText().toString();
    }

    public void setUserName(String first, String last) {
        userNameTextView.setText(String.format("%s %s", first, last));
    }

    public void setAvatar(String avatarString) {
        if (avatarString != null && !avatarString.equals("")) {
            Glide.with(this).load(avatarString).apply(new RequestOptions().override(100, 100).placeholder(R.drawable.demo_alias_dp)).into(userDP);
        }
    }

    public void setAvatarForMenu(String avatar) {
        if (avatar != null && !avatar.equals("")) {
            try{
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
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setRating(Map<String, Object> selfRating) {
        if (selfRating != null) {
            String ret = (String) selfRating.get("star_rating");
            ret += "  ★";
            userRatingTextView.setText(ret);
        }
    }

    public void setMsgName(String msgName) {
        userAddressingTextView.setText(msgName);
        //referMentorBtn.setText(msgName.toLowerCase());
        freeCashBack.setText(msgName.toLowerCase());
    }


    public void setProfileComPercent(String num) {
        if (Integer.parseInt(num) < 100) {
            updateProfileBadge('!');
        } else {
            updateProfileBadge('%');
        }
    }

    public static String UNREAD_MESSAGE = "unread_message";

    public void setUnreadMsg(String unreadMsg) {
        updateChatBadge(Integer.parseInt(unreadMsg));
        sharedPreferences = context.getSharedPreferences(UNREAD_MESSAGE, MODE_PRIVATE);
        updateChatBadge(sharedPreferences.getInt("unread", 0));
    }

    public void setUnreadNoti(String unreadNoti) {
        updateNotificationsBadge(Integer.parseInt(unreadNoti));
    }

    public void setUnreadRecords(Map<String, Object> unreadRecords) {
        updateRecordsBadge(Integer.parseInt((String) unreadRecords.get("pending_count")),
                Integer.parseInt((String) unreadRecords.get("accepted_count")),
                Integer.parseInt((String) unreadRecords.get("current_count")));
    }

    public void showSnackBar(String completePercent) {
        mySnackbar = Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) mySnackbar.getView();
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
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
        parentParams.height = (int) (36 * (getResources().getDisplayMetrics().density));

        layout.setLayoutParams(parentParams);
        layout.addView(snackView, 0);
        int status = NetworkUtil.getConnectivityStatusString(context);
        if (snackBar != null && !snackBar.isShown() && status != NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
            mySnackbar.show();
        }
    }

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
                                //go to mentor activity
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


    public void showPercentSnak(String message, String actionName) {
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) enamSnackbar.getView();
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
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
        parentParams.height = (int) (36 * (getResources().getDisplayMetrics().density));
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
    public void searchFilterClicked() {
        if (searchFilterBtn.getVisibility() == View.VISIBLE){
            if (filterDrawable instanceof Animatable) {
                ((Animatable) filterDrawable).start();
            }
        }
        if(searchFilterBtnOne.getVisibility() == View.VISIBLE){
            if (filterDrawableOne instanceof Animatable) {
                ((Animatable) filterDrawableOne).start();
            }
        }

        TextView mainText = filterBottomSheetDialog.findViewById(R.id.main_text);
        TextView subText = filterBottomSheetDialog.findViewById(R.id.sub_text);
        Button proceedBtn = filterBottomSheetDialog.findViewById(R.id.cancel_yes_btn);
        EditText universityNames = filterBottomSheetDialog.findViewById(R.id.input_uni);
        EditText degreeNames = filterBottomSheetDialog.findViewById(R.id.input_degree);
        CheckBox uniCheckBox = filterBottomSheetDialog.findViewById(R.id.uni_checkbox);
        CheckBox degreeCheckBox = filterBottomSheetDialog.findViewById(R.id.degree_checkbox);
        RadioButton permanentRadio = filterBottomSheetDialog.findViewById(R.id.permanent_radio);
        RadioButton chooseLocationRadio = filterBottomSheetDialog.findViewById(R.id.choose_radio);
        RadioButton permanentLocationRadio = filterBottomSheetDialog.findViewById(R.id.permanent_radio);

        //initialize this part form the shared Preference
        String[] uniItems = getResources().getStringArray(R.array.University);
        String[] listItems = getResources().getStringArray(R.array.Degrees);

        SharedPreferences.Editor editor = getSharedPreferences("filter", MODE_PRIVATE).edit();

        if (prefs != null) {
            Boolean uniCheckBox1 = prefs.getBoolean("uniCheckBox", false);
            uniCheckBox.setChecked(uniCheckBox1);

            Boolean degreeCheckBox1 = prefs.getBoolean("degreeCheckBox", false);
            degreeCheckBox.setChecked(degreeCheckBox1);

            Gson gson = new Gson();
            String json = prefs.getString("selectedUnis", "");
            if (!json.equals("")) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                selectedUnis = gson.fromJson(json, type);
                //setting the textview
                StringBuilder item = new StringBuilder();
                for (int i = 0; i < selectedUnis.size(); i++) {
                    item.append(selectedUnis.get(i));
                    if (i != selectedUnis.size() - 1) {
                        item.append(", ");
                    }
                }
                universityNames.setText(item);
            } else {
                selectedUnis = new ArrayList<>();
            }

            //retrieving the array
            int size = prefs.getInt("checkedUnis" + "_size", 0);
            checkedUnis = new boolean[uniItems.length];
            for (int i = 0; i < size; i++)
                checkedUnis[i] = prefs.getBoolean("checkedUnis" + "_" + i, false);

            String json1 = prefs.getString("selectedDegrees", "");
            if (!json1.equals("")) {
                Type type1 = new TypeToken<List<String>>() {
                }.getType();
                selectedDegrees = gson.fromJson(json1, type1);
                StringBuilder item = new StringBuilder();
                for (int i = 0; i < selectedDegrees.size(); i++) {
                    item.append(selectedDegrees.get(i));
                    if (i != selectedDegrees.size() - 1) {
                        item.append(", ");
                    }
                }
                degreeNames.setText(item);
            } else {
                selectedDegrees = new ArrayList<>();
            }

            //retrieving the array
            int size1 = prefs.getInt("checkedItems" + "_size", 0);
            checkedItems = new boolean[listItems.length];
            for (int i = 0; i < size1; i++)
                checkedItems[i] = prefs.getBoolean("checkedItems" + "_" + i, false);

            Boolean chooseLocationRadio1 = prefs.getBoolean("chooseLocationRadio", true);
            if (chooseLocationRadio1) {
                chooseLocationRadio.setChecked(true);
                permanentRadio.setChecked(false);
            } else {
                chooseLocationRadio.setChecked(false);
                permanentRadio.setChecked(true);
            }
        }

        if (mainText != null && subText != null && proceedBtn != null &&
                universityNames != null && degreeNames != null && uniCheckBox != null && degreeCheckBox != null) {

            universityNames.setOnClickListener(new View.OnClickListener() {

                private AlertDialog mUniDialog;

                @Override
                public void onClick(View view) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomePageActivity.this, R.style.RadioDialogTheme);
                    mBuilder.setTitle("Select filter universities");
                    mBuilder.setMultiChoiceItems(uniItems, checkedUnis, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                            if (isChecked) {
                                if (selectedUnis.size() == 3) {
                                    Toast.makeText(HomePageActivity.this, "Maximum 3 filter allowed", Toast.LENGTH_SHORT).show();
                                    ((AlertDialog) mUniDialog).getListView().setItemChecked(position, false);
                                    checkedUnis[position] = false;
                                } else {
                                    if (!selectedUnis.contains(uniItems[position])) {
                                        selectedUnis.add(uniItems[position]);
                                    }
                                    checkedUnis[position] = true;
                                }
                            } else {
                                if (selectedUnis.contains(uniItems[position])) {
                                    selectedUnis.remove(uniItems[position]);
                                }
                                checkedUnis[position] = false;
                            }
                        }
                    });

                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            if (selectedUnis.size() > 0) {
                                StringBuilder item = new StringBuilder();
                                for (int i = 0; i < selectedUnis.size(); i++) {
                                    item.append(selectedUnis.get(i));
                                    if (i != selectedUnis.size() - 1) {
                                        item.append(", ");
                                    }
                                }
                                universityNames.setText(item);
                                uniCheckBox.setChecked(true);
                            } else {
                                universityNames.setText("");
                                uniCheckBox.setChecked(false);
                            }

                            editor.putBoolean("uniCheckBox", uniCheckBox.isChecked());

                            editor.putInt("checkedUnis" + "_size", checkedUnis.length);
                            for (int i = 0; i < checkedUnis.length; i++)
                                editor.putBoolean("checkedUnis" + "_" + i, checkedUnis[i]);

                            Gson gson = new Gson();
                            String json = gson.toJson(selectedUnis);
                            editor.putString("selectedUnis", json);
                            editor.apply();
                        }
                    });

                    mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            for (int i = 0; i < checkedUnis.length; i++) {
                                checkedUnis[i] = false;
                                selectedUnis.clear();
                                universityNames.setText("");
                                uniCheckBox.setChecked(false);
                            }
                            editor.putBoolean("uniCheckBox", uniCheckBox.isChecked());

                            editor.putInt("checkedUnis" + "_size", checkedUnis.length);
                            for (int i = 0; i < checkedUnis.length; i++)
                                editor.putBoolean("checkedUnis" + "_" + i, checkedUnis[i]);

                            Gson gson = new Gson();
                            String json = gson.toJson(selectedUnis);
                            editor.putString("selectedUnis", json);
                            editor.apply();
                        }
                    });

                    mUniDialog = mBuilder.create();
                    mUniDialog.show();
                }
            });

            uniCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //is chkIos checked?
                    editor.putBoolean("uniCheckBox", ((CheckBox) v).isChecked());
                    editor.apply();
                    if (((CheckBox) v).isChecked()) {
                        universityNames.performClick();
                    }
                }
            });

            degreeNames.setOnClickListener(new View.OnClickListener() {

                private AlertDialog mDegreeDialog;

                @Override
                public void onClick(View view) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(HomePageActivity.this, R.style.RadioDialogTheme);
                    mBuilder.setTitle("Select filter degrees");
                    mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                            if (isChecked) {
                                if (selectedDegrees.size() == 3) {
                                    Toast.makeText(HomePageActivity.this, "Maximum 3 filter allowed", Toast.LENGTH_SHORT).show();
                                    ((AlertDialog) mDegreeDialog).getListView().setItemChecked(position, false);
                                    checkedItems[position] = false;
                                } else {
                                    if (!selectedDegrees.contains(listItems[position])) {
                                        selectedDegrees.add(listItems[position]);
                                    }
                                    checkedItems[position] = true;
                                }
                            } else {
                                if (selectedDegrees.contains(listItems[position])) {
                                    selectedDegrees.remove(listItems[position]);
                                }
                                checkedItems[position] = false;
                            }
                        }
                    });

                    mBuilder.setCancelable(false);
                    mBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            if (selectedDegrees.size() > 0) {
                                StringBuilder item = new StringBuilder();
                                for (int i = 0; i < selectedDegrees.size(); i++) {
                                    item.append(selectedDegrees.get(i));
                                    if (i != selectedDegrees.size() - 1) {
                                        item.append(", ");
                                    }
                                }
                                degreeNames.setText(item);
                                degreeCheckBox.setChecked(true);
                            } else {
                                degreeNames.setText("");
                                degreeCheckBox.setChecked(false);
                            }

                            editor.putBoolean("degreeCheckBox", degreeCheckBox.isChecked());

                            editor.putInt("checkedItems" + "_size", checkedItems.length);
                            for (int i = 0; i < checkedItems.length; i++)
                                editor.putBoolean("checkedItems" + "_" + i, checkedItems[i]);

                            Gson gson1 = new Gson();
                            String json1 = gson1.toJson(selectedDegrees);
                            editor.putString("selectedDegrees", json1);
                            editor.apply();
                        }
                    });

                    mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            for (int i = 0; i < checkedItems.length; i++) {
                                checkedItems[i] = false;
                                selectedDegrees.clear();
                                degreeNames.setText("");
                                degreeCheckBox.setChecked(false);
                                //mItemSelected.setText("");
                            }
                            editor.putBoolean("degreeCheckBox", degreeCheckBox.isChecked());

                            editor.putInt("checkedItems" + "_size", checkedItems.length);
                            for (int i = 0; i < checkedItems.length; i++)
                                editor.putBoolean("checkedItems" + "_" + i, checkedItems[i]);

                            Gson gson1 = new Gson();
                            String json1 = gson1.toJson(selectedDegrees);
                            editor.putString("selectedDegrees", json1);
                            editor.apply();
                        }
                    });

                    mDegreeDialog = mBuilder.create();
                    mDegreeDialog.show();

                }
            });

            degreeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //is chkIos checked?
                    editor.putBoolean("degreeCheckBox", ((CheckBox) view).isChecked());
                    editor.apply();
                    if (((CheckBox) view).isChecked()) {
                        degreeNames.performClick();
                    }
                }
            });

            chooseLocationRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putBoolean("chooseLocationRadio", chooseLocationRadio.isChecked());
                    editor.apply();
                }
            });

            permanentLocationRadio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putBoolean("chooseLocationRadio", chooseLocationRadio.isChecked());
                    editor.apply();
                }
            });

            proceedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editor.putBoolean("uniCheckBox", uniCheckBox.isChecked());
                    editor.putBoolean("degreeCheckBox", degreeCheckBox.isChecked());

                    editor.putInt("checkedUnis" + "_size", checkedUnis.length);
                    for (int i = 0; i < checkedUnis.length; i++)
                        editor.putBoolean("checkedUnis" + "_" + i, checkedUnis[i]);

                    editor.putInt("checkedItems" + "_size", checkedItems.length);
                    for (int i = 0; i < checkedItems.length; i++)
                        editor.putBoolean("checkedItems" + "_" + i, checkedItems[i]);

                    Gson gson = new Gson();
                    String json = gson.toJson(selectedUnis);
                    editor.putString("selectedUnis", json);

                    Gson gson1 = new Gson();
                    String json1 = gson1.toJson(selectedDegrees);
                    editor.putString("selectedDegrees", json1);

                    editor.putBoolean("chooseLocationRadio", chooseLocationRadio.isChecked());
                    editor.apply();

                    filterBottomSheetDialog.dismiss();
                    gotoGrabingLocationPage();
                }
            });
        }
        //filterBottomSheetDialog.setCanceledOnTouchOutside(false);
        //filterBottomSheetDialog.setCancelable(false);
        filterBottomSheetDialog.show();
    }
}
