package io.dume.dume.teacher.homepage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.hanks.htextview.scale.ScaleTextView;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import info.hoang8f.android.segmented.SegmentedGroup;
import io.dume.dume.R;
import io.dume.dume.common.aboutUs.AboutUsActivity;
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.common.privacyPolicy.PrivacyPolicyActivity;
import io.dume.dume.student.freeCashBack.FreeCashBackActivity;
import io.dume.dume.student.heatMap.HeatMapActivity;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.homePage.adapter.HomePageRatingAdapter;
import io.dume.dume.student.homePage.adapter.HomePageRatingData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerAdapter;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.recordsPage.RecordsPageActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentPayment.StudentPaymentActivity;
import io.dume.dume.teacher.homepage.fragments.AcademicFragment;
import io.dume.dume.teacher.homepage.fragments.InboxFragment;
import io.dume.dume.teacher.homepage.fragments.PayFragment;
import io.dume.dume.teacher.homepage.fragments.PerformanceFragment;
import io.dume.dume.teacher.homepage.fragments.SkillFragment;
import io.dume.dume.teacher.homepage.fragments.StatisticsFragment;
import io.dume.dume.teacher.mentor_settings.AccountSettings;
import io.dume.dume.teacher.pojo.TabModel;
import io.dume.dume.teacher.skill.SkillActivity;
import io.dume.dume.util.DumeUtils;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

import static io.dume.dume.util.DumeUtils.animateImage;


public class TeacherActivtiy extends CusStuAppComMapActivity implements TeacherContract.View,
        NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, MyGpsLocationChangeListener,
        RadioGroup.OnCheckedChangeListener {
    private TeacherContract.Presenter presenter;
    private TextView textView;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private Button signOutButton;
    private static final String TAG = "TeacherActivtiy";
    private NavigationView navigationView;
    ToolTipsManager mToolTipsManager;
    private Context context;
    private int mNotificationsCount = 0;
    private char mProfileChar = '!';
    private int mChatCount = 0;
    private int mRecPendingCount = 0, mRecAcceptedCount = 0, mRecCurrentCount = 0;
    private ActionBar supportActionBarMain;
    private ActionBar supportActionBarSecond;

    private Menu menu;
    private MenuItem home, records, payments, messages, notifications, heat_map, free_cashback, settings, forum, help, selectAccount, infoItem, studentProfile, mentorProfile, bootCampProfile;
    private Drawable less;
    private Drawable more;
    private Drawable leftDrawable;
    private Button switchAcountBtn;
    private MenuItem skills;
    private LinearLayout bottomSheet;
    private View headerView;
    private ListView listView;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean firstTime;
    private Toolbar secondaryToolbar;
    private AppBarLayout secondaryAppBarLayout;
    private CollapsingToolbarLayout secondaryCollapsableToolbar;
    private FrameLayout viewMusk;
    private AppBarLayout mainAppbar;
    private CoordinatorLayout coordinatorLayout;

    private int spacing;
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    @BindView(R.id.tablayout)
    VerticalTabLayout tabLayout;
    @BindView(R.id.mViewPager)
    VerticalViewPager viewPager;
    @BindView(R.id.fragmentTitle)
    ScaleTextView fragmentTitle;
    @BindView(R.id.tipsTV)
    ScaleTextView scaleTextView;
    private CoordinatorLayout mainInterface;
    private ImageView referMentorImageView;
    private ImageView enhanceSkillImageView;
    private ImageView freeCashBackImageView;
    private SegmentedGroup radioSegmentGroup;
    private RadioButton buttonActive;
    private RadioButton buttonInActive;
    private TextView userName;
    private RecyclerView hPageBSRecycler;
    private int optionMenu = R.menu.stu_homepage;
    private String[] feedbackStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setActivityContextMap(this, 1604);
        presenter = new TeacherPresenter(this, new TeacherModel());
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);
    }

    @Override
    public void init() {
        context = this;
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        navigationView = findViewById(R.id.navigationView);
        mToolTipsManager = new ToolTipsManager();
        menu = navigationView.getMenu();

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
        infoItem = menu.findItem(R.id.information_item);
        selectAccount = menu.findItem(R.id.select_account);
        skills = menu.findItem(R.id.skills);

        studentProfile = menu.findItem(R.id.student);
        mentorProfile = menu.findItem(R.id.mentor);
        bootCampProfile = menu.findItem(R.id.boot_camp);
        less = this.getResources().getDrawable(R.drawable.less_icon);
        more = this.getResources().getDrawable(R.drawable.more_icon);
        switchAcountBtn = findViewById(R.id.switch_account_btn);
        leftDrawable = switchAcountBtn.getCompoundDrawables()[0];
        bottomSheet = findViewById(R.id.amBottomSheet);
        coordinatorLayout = findViewById(R.id.parent_coor_layout);
        listView = findViewById(R.id.messagesRV);
        secondaryToolbar = findViewById(R.id.Secondary_toolbar);
        secondaryAppBarLayout = findViewById(R.id.secondary_Appbar);
        secondaryCollapsableToolbar = findViewById(R.id.secondary_collapsing_toolbar);
        viewMusk = findViewById(R.id.view_musk);
        mainAppbar = findViewById(R.id.my_appbarLayout);
        mainInterface = findViewById(R.id.main_interface);
        referMentorImageView = findViewById(R.id.refer_mentor_imageView);
        enhanceSkillImageView = findViewById(R.id.enhance_skill_imageview);
        freeCashBackImageView = findViewById(R.id.free_cashback_imageView);
        radioSegmentGroup = findViewById(R.id.segmentGroup);
        buttonActive = findViewById(R.id.buttonActive);
        buttonInActive = findViewById(R.id.buttonInActive);
        userName = findViewById(R.id.user_name);
        hPageBSRecycler = findViewById(R.id.homePage_bottomSheet_recycler);
        feedbackStrings = getResources().getStringArray(R.array.review_hint_text_dependent);
    }

    @Override
    public void configView() {
        // Toolbar :: Transparent
        mainAppbar.bringToFront();
        mainAppbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        //secondary appbar font fix
        secondaryCollapsableToolbar.setCollapsedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableToolbar.setExpandedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsableToolbar.setTitle("Messages");
        //config the bottom sheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetCallbackConfig();
        //
        initAdvance();
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.drawer_menu);
        navigationView.setNavigationItemSelectedListener(this);
        //listener for the active inactive radio btn
        radioSegmentGroup.setOnCheckedChangeListener(this);
        //initializing the recycler
        List<HomePageRecyclerData> promoData = new ArrayList<>();
        HomePageRecyclerAdapter hPageBSRcyclerAdapter = new HomePageRecyclerAdapter(this, promoData);
        hPageBSRecycler.setAdapter(hPageBSRcyclerAdapter);
        hPageBSRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void referMentorImageViewClicked() {
        animateImage(referMentorImageView);
    }

    @Override
    public void freeCashBackImageViewClicked() {
        animateImage(freeCashBackImageView);
    }

    @Override
    public void enhanceVIewImageClicked() {
        animateImage(enhanceSkillImageView);
    }


    public void bottomSheetCallbackConfig() {
        ViewTreeObserver vto = bottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                bottomSheet.getLayoutParams().height = (bottomSheet.getHeight() - secondaryAppBarLayout.getHeight() + (int) (0 * (getResources().getDisplayMetrics().density)));
                bottomSheet.requestLayout();
                bottomSheetBehavior.onLayoutChild(coordinatorLayout, bottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
                ViewTreeObserver obs = bottomSheet.getViewTreeObserver();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        firstTime = true;
        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    setDarkStatusBarIcon();
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppBarLayout.setVisibility(View.GONE);
                    mainAppbar.setVisibility(View.VISIBLE);
                    bottomSheet.animate().scaleY(1).setDuration(0).start();
                    //hack
                    setSupportActionBar(toolbar);
                    supportActionBarMain = getSupportActionBar();
                    if (supportActionBarMain != null) {
                        supportActionBarMain.setDisplayHomeAsUpEnabled(true);
                        supportActionBarMain.setDisplayShowHomeEnabled(true);
                        optionMenu = R.menu.stu_homepage;
                        invalidateOptionsMenu();
                    }
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    mainAppbar.setVisibility(View.GONE);
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
                    mainAppbar.setVisibility(View.GONE);
//                    secondaryAppBarLayout.setExpanded(false);

                } /*else if (BottomSheetBehavior.STATE_SETTLING == newState) {
                    mainAppbar.setVisibility(View.VISIBLE);
                }*/

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    bottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).setDuration(0).start();
                    viewMusk.animate().alpha(2 * slideOffset).setDuration(0).start();
                    mainInterface.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    secondaryAppBarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
                }
            }
        });

    }

    @Override
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initAdvance() {
        drawerLayout.setScrimColor(getResources().getColor(R.color.black_overlay));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                setDarkStatusBarIcon();
                switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, more, null);
                mainMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setLightStatusBarIcon();
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        ButterKnife.bind(this);
        ArrayList<TabModel> tabModelArrayList = new ArrayList<>();
        tabModelArrayList.add(new TabModel(R.drawable.performance, R.drawable.performance_selected, 0, "Performance"));
        tabModelArrayList.add(new TabModel(R.drawable.inbox, R.drawable.inbox_selected, 0, "Inbox"));
        tabModelArrayList.add(new TabModel(R.drawable.pay, R.drawable.pay_selceted, 0, "Pay"));
        tabModelArrayList.add(new TabModel(R.drawable.ic_statistics, R.drawable.ic_statistics_selected, 3, "Statistics"));
        tabModelArrayList.add(new TabModel(R.drawable.skills, R.drawable.skills_selected, 0, "Manage Skills"));
        tabModelArrayList.add(new TabModel(R.drawable.academics_icon, R.drawable.academics_icon_selected, 0, "Academic"));
        tabLayout.setTabAdapter(new TabAdapter() {

            @Override
            public int getCount() {
                return tabModelArrayList.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                if (tabModelArrayList.get(position).getBadge() > 0) {
                    return new ITabView.TabBadge.Builder().setBadgeNumber(2).setBadgeText(tabModelArrayList.get(position).getBadge() + "").build();
                }
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                int normalIcon = tabModelArrayList.get(position).getNormalIcon();
                int selected = tabModelArrayList.get(position).getSelectedIcon();
                return new ITabView.TabIcon.Builder().setIcon(selected, normalIcon).build();
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                return null;
            }

            @Override
            public int getBackground(int position) {
                return 0;
            }
        });
        tabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                viewPager.setCurrentItem(position);
                fragmentTitle.animateText(tabModelArrayList.get(position).getTabName());
                scaleTextView.animateText(tabModelArrayList.get(position).getTabName());
            }

            @Override
            public void onTabReselected(TabView tab, int position) {
                tips("Every person is a new door to a different world.");
            }
        });
        viewPager.setAdapter(new pager(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.setTabSelected(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        fragmentTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Cairo_Regular.ttf"));
        fragmentTitle.animateText(tabModelArrayList.get(tabLayout.getSelectedTabPosition()).getTabName());


    }

    public void tips(CharSequence sequence) {
        scaleTextView.animateText(sequence);
        scaleTextView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Cairo_Regular.ttf"));
        scaleTextView.setAnimationListener(hTextView -> {

        });
        scaleTextView.setSelected(true);
    }

    public void toggle(android.view.View view) {
        presenter.onButtonClicked();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(optionMenu, menu);
        if(optionMenu == R.menu.stu_homepage){
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
        }else if (optionMenu == R.menu.menu_only_help){

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.al_display_pic:
                //mView.updateProfileBadge(mProfileChar);
                break;
            case R.id.al_records:
                updateRecordsBadge(++mRecPendingCount, ++mRecAcceptedCount, ++mRecCurrentCount);
                startActivity(new Intent(this, RecordsPageActivity.class));
                break;
            case R.id.al_messages:
                updateChatBadge(++mChatCount);
                startActivity(new Intent(this, InboxActivity.class));
                break;
            case R.id.al_notifications:
                updateNotificationsBadge(++mNotificationsCount);
                Intent notificationTabIntent = new Intent(this, InboxActivity.class);
                notificationTabIntent.putExtra("notiTab", 1);
                startActivity(notificationTabIntent);
                break;
            case android.R.id.home:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START, true);
                    //super.onBackPressed();
                }
                break;

        }
        Drawable drawableGeneral = item.getIcon();
        if (drawableGeneral instanceof Animatable) {
            ((Animatable) drawableGeneral).start();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(this, AccountSettings.class));
                break;
            case R.id.skills:
                startActivity(new Intent(this, SkillActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                FirebaseAuth.getInstance().signOut();
                break;
            case R.id.help:
                startActivity(new Intent(this, StudentHelpActivity.class));
                break;
            case R.id.payments:
                startActivity(new Intent(this, StudentPaymentActivity.class));
                break;
            case R.id.forum:
                Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.messages:
                startActivity(new Intent(this, InboxActivity.class));
                break;
            case R.id.notifications:
                Intent notificationTabIntent = new Intent(this, InboxActivity.class);
                notificationTabIntent.putExtra("notiTab", 1);
                startActivity(notificationTabIntent);
                break;
            case R.id.free_cashback:
                startActivity(new Intent(this, FreeCashBackActivity.class));
                break;
            case R.id.privacy_policy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                break;
            case R.id.records:
                startActivity(new Intent(this, RecordsPageActivity.class));
                break;
            case R.id.heat_map:
                startActivity(new Intent(this, HeatMapActivity.class));
                break;
            case R.id.student:
                startActivity(new Intent(this, HomePageActivity.class));
                break;
            case R.id.mentor:
                break;
            case R.id.boot_camp:
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START,true);
        return true;
    }

    public void onHomePageViewClicked(View view) {
        presenter.onViewInteracted(view);
    }

    public void onNavigationHeaderClick(View view) {
        presenter.onViewInteracted(view);
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
        skills.setVisible(false);

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
        skills.setVisible(true);
        infoItem.setVisible(true);
        selectAccount.setVisible(false);
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
    public void onMapReady(GoogleMap googleMap) {
        setDarkStatusBarIcon();
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_default_no_landmarks);
        googleMap.setMapStyle(style);
        mMap = googleMap;
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();
        mMap.setPadding((int) (20 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (72 * (getResources().getDisplayMetrics().density)));

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

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.buttonActive:
                Toast.makeText(this, "You are active on Dume network now", Toast.LENGTH_SHORT).show();
                userName.setText("Demo User(Active)");
                buttonActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_active_active, 0, 0, 0);
                buttonInActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_inactive_inactive, 0, 0, 0);
                break;
            case R.id.buttonInActive:
                Toast.makeText(this, "You are inactive on Dume network now", Toast.LENGTH_SHORT).show();
                userName.setText("Demo User(Inactive)");
                buttonActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_active_inactive, 0, 0, 0);
                buttonInActive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.state_inactive_active, 0, 0, 0);
                break;
            default:
                // Nothing to do
        }
    }

    /*
      Updates the count of notifications in the ActionBar finishes here.
    */

    class pager extends FragmentPagerAdapter {

        public pager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            if (i == 0) {
                fragment = new PerformanceFragment();
            } else if (i == 1) {
                fragment = new InboxFragment();
            } else if (i == 2) {
                fragment = new PayFragment();
            } else if (i == 3) {
                fragment = new StatisticsFragment();
            } else if (i == 4) {
                fragment = new SkillFragment();
            } else if (i == 5) {
                fragment = new AcademicFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    //rating dialog not used right now
    @Override
    public void testingCustomDialogue() {
        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_rating_dialogue);
        dialog.setCanceledOnTouchOutside(false);

        //all find view here
        MaterialRatingBar mDecimalRatingBars = dialog.findViewById(R.id.rated_mentor_rating_bar);
        RecyclerView itemRatingRecycleView = dialog.findViewById(R.id.rating_item_recycler);
        carbon.widget.ImageView ratedMentorDP = dialog.findViewById(R.id.rated_mentor_dp);
        TextView ratingPrimaryText = dialog.findViewById(R.id.rating_primary_text);
        TextView ratingSecondaryText = dialog.findViewById(R.id.rating_secondary_text);
        TextInputLayout feedbackTextViewLayout = dialog.findViewById(R.id.input_layout_firstname);
        AutoCompleteTextView feedbackTextView = dialog.findViewById(R.id.feedback_textview);
        Button dismissBtn = (Button) dialog.findViewById(R.id.skip_btn);
        Button nextSubmitBtn = dialog.findViewById(R.id.next_btn);
        RelativeLayout dialogHostingLayout = dialog.findViewById(R.id.dialog_hosting_layout);
        Button SubmitBtn = dialog.findViewById(R.id.submit_btn);


        //testing the recycle view here
        HomePageRatingAdapter itemRatingRecycleAdapter = new HomePageRatingAdapter(this, getFinalRatingData());
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
                        String userName = "Azgor";
                        feedbackTextView.setHint("Share how " + userName + " can improve");
                    } else if (rating > 100 && rating <= 200) {
                        feedbackTextView.setHint(feedbackStrings[1]);
                    } else if (rating > 200 && rating <= 300) {
                        String userName = "Azgor";
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
        dialog.show();


        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
                    nextSubmitBtn.setVisibility(View.GONE);
                    SubmitBtn.setVisibility(View.VISIBLE);

                    mDecimalRatingBars.setVisibility(View.GONE);
                    ratedMentorDP.setVisibility(View.GONE);
                    ratingPrimaryText.setVisibility(View.GONE);

                    ratingSecondaryText.setVisibility(View.VISIBLE);
                    itemRatingRecycleView.setVisibility(View.VISIBLE);
                    feedbackTextViewLayout.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(TeacherActivtiy.this, "please rate your experience", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public List<HomePageRatingData> getFinalRatingData() {
        List<HomePageRatingData> data = new ArrayList<>();
        String[] primaryText = getResources().getStringArray(R.array.rating_demo_data);
        for (String aPrimaryText : primaryText) {
            HomePageRatingData current = new HomePageRatingData();
            current.ratingAboutName = aPrimaryText;
            data.add(current);
        }
        return data;
    }
}
