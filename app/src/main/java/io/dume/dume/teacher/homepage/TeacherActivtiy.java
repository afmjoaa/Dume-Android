package io.dume.dume.teacher.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.hanks.htextview.scale.ScaleTextView;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.teacher.homepage.fragments.AcademicFragment;
import io.dume.dume.teacher.homepage.fragments.InboxFragment;
import io.dume.dume.teacher.homepage.fragments.PayFragment;
import io.dume.dume.teacher.homepage.fragments.PerformanceFragment;
import io.dume.dume.teacher.homepage.fragments.SkillFragment;
import io.dume.dume.teacher.homepage.fragments.StatisticsFragment;
import io.dume.dume.teacher.mentor_settings.AccountSettings;
import io.dume.dume.teacher.pojo.TabModel;
import io.dume.dume.teacher.skill.SkillActivity;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;


public class TeacherActivtiy extends CusStuAppComMapActivity implements TeacherContract.View, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, MyGpsLocationChangeListener {
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
    public void onSheetChanges(boolean halfCrossed) {


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
        actionBar.setHomeAsUpIndicator(R.drawable.drawer_menu);
        navigationView = findViewById(R.id.navigationView);
        mToolTipsManager = new ToolTipsManager();
        initAdvance();
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
        mainAppbar = findViewById(R.id.mainAppBar);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetCallbackConfig();

    }


    public void bottomSheetCallbackConfig() {
        ViewTreeObserver vto = bottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                bottomSheet.getLayoutParams().height = (bottomSheet.getHeight() - secondaryAppBarLayout.getHeight() - (int) (2 * (getResources().getDisplayMetrics().density)));
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
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppBarLayout.setVisibility(View.GONE);
                    mainAppbar.setVisibility(View.VISIBLE);
                    bottomSheet.animate().scaleY(1).setDuration(0).start();

                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    mainAppbar.setVisibility(View.GONE);


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
                    //mainAppbar.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
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
        tabModelArrayList.add(new TabModel(R.drawable.inbox, R.drawable.inbox, 0, "Inbox"));
        tabModelArrayList.add(new TabModel(R.drawable.pay, R.drawable.pay, 0, "Pay"));
        tabModelArrayList.add(new TabModel(R.drawable.ic_cross_check, R.drawable.ic_cross_check, 3, "Statistics"));
        tabModelArrayList.add(new TabModel(R.drawable.skills, R.drawable.skills, 0, "Manage Skills"));
        tabModelArrayList.add(new TabModel(R.drawable.education, R.drawable.education, 0, "Academic"));
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
            scaleTextView.setSelected(true);
        });
    }

    public void toggle(android.view.View view) {
        presenter.onButtonClicked();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //  getMenuInflater().inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
        }
        return false;
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
}
