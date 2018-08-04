package io.dume.dume.student.homepage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.auth.auth.AppbarStateChangeListener;
import io.dume.dume.util.DumeUtils;

public class HomePageActivity extends AppCompatActivity implements HomePageContract.View,
        NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    HomePageContract.Presenter mPresenter;
    private Menu menu;
    private MenuItem home, records, payments, messages, notifications, free_cashback, settings, forum, help, selectAccount, infoItem, studentProfile, mentorProfile, bootCampProfile;
    private Button switchAcountBtn;
    private NavigationView navigationView;
    private Drawable leftDrawable;
    private Drawable less;
    private Drawable more;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private FloatingActionButton bottomSheetFab;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private int mNotificationsCount = 0;
    private char mProfileChar = '!';
    private int mChatCount = 0;
    private int mRecPendingCount = 0, mRecAcceptedCount = 0, mRecCurrentCount = 0;
    private View decor;
    private View llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean firstTime;
    private FrameLayout viewMusk;
    private Button searchMentorBtn;
    private AppBarLayout defaultAppBerLayout;
    private AppBarLayout secondaryAppBarLayout;
    private RelativeLayout relativeLayoutHack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_activity_homepage);
        mPresenter = new HomePagePresenter(this, this, new HomePageModel());
        mPresenter.homePageEnqueue();
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
        Snackbar snackbar = Snackbar.make(findViewById(R.id.parent_coor_layout), "Replace with your own action", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(HomePageActivity.this);

        View snackView = inflater.inflate(R.layout.custom_snackbar_layout, null);
                /*ImageView imageView = (ImageView) snackView.findViewById(R.id.image);
                imageView.setImageBitmap(image);*/

        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText("Can't reach the Dume network.");
        textViewStart.setTextColor(Color.WHITE);

        layout.setPadding(0, 0, 0, 0);
        layout.setBackgroundColor(ContextCompat.getColor(HomePageActivity.this, R.color.snackbar_red));
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (30 * (getResources().getDisplayMetrics().density));

        //Id for your bottomNavBar or TabLayout
        parentParams.setAnchorId(R.id.bottom_sheet);
        parentParams.anchorGravity = Gravity.TOP;
        parentParams.gravity = Gravity.TOP;

        layout.setLayoutParams(parentParams);

        layout.addView(snackView, 0);
        snackbar.show();
    }


    @Override
    public void configHomePage() {
        fab.setAlpha(0.80f);
        bottomSheetFab.setAlpha(0.80f);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_green_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);
        // Toolbar :: Transparent
        // toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.getBackground().setAlpha(0);
        // Status bar :: Transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //setting toggle behavior for navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                switchAcountBtn.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, more, null);
                mainMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decor.setSystemUiVisibility(0);
                }
                viewMusk.setVisibility(View.GONE);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_menu);
        home.setChecked(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        firstTime = true;

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    fab.animate().translationYBy((float) (60.0f * (getResources().getDisplayMetrics().density))).setDuration(60).start();
                    bottomSheetFab.setVisibility(View.VISIBLE);
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    }
                    secondaryAppBarLayout.setExpanded(true);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppBarLayout.setVisibility(View.GONE);
                    defaultAppBerLayout.setVisibility(View.VISIBLE);
                    relativeLayoutHack.setVisibility(View.GONE);
                    llBottomSheet.animate().scaleY(1).setDuration(0).start();
                    relativeLayoutHack.animate().alpha(0).setDuration(0).start();
                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        decor.setSystemUiVisibility(0);
                    }
                    secondaryAppBarLayout.setExpanded(true,true);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    relativeLayoutHack.setVisibility(View.VISIBLE);
                    defaultAppBerLayout.setVisibility(View.GONE);
                }
                else if(BottomSheetBehavior.STATE_DRAGGING ==newState){
                    secondaryAppBarLayout.setExpanded(true);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppBarLayout.setVisibility(View.VISIBLE);
                    relativeLayoutHack.setVisibility(View.VISIBLE);
                    defaultAppBerLayout.setVisibility(View.GONE);
                }else if(BottomSheetBehavior.STATE_SETTLING == newState){
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                if (slideOffset > 0.0f && slideOffset < 1.0f) {
                    fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                    llBottomSheet.animate().scaleX(1 + (slideOffset * 0.05f)).setDuration(0).start();
                    viewMusk.animate().alpha(2 * slideOffset).setDuration(0).start();

                    searchMentorBtn.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset)
                            .setDuration(0).start();
                    defaultAppBerLayout.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();

                    secondaryAppBarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
                }
            }
        });
    }

    @Override
    public void onShowBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fab.animate().translationYBy((float) (-60.0f * (getResources().getDisplayMetrics().density))).setDuration(100).start();

            if (bottomSheetFab.getVisibility() == View.VISIBLE) {
                bottomSheetFab.setVisibility(View.GONE);
            }
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            if (firstTime) {
                fab.animate().translationYBy((float) (-60.0f * (getResources().getDisplayMetrics().density))).setDuration(100).start();
                firstTime = false;
            }
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.setSystemUiVisibility(0);
            }
            viewMusk.setVisibility(View.VISIBLE);
            secondaryAppBarLayout.setVisibility(View.VISIBLE);
            defaultAppBerLayout.setVisibility(View.GONE);

            if (bottomSheetFab.getVisibility() == View.VISIBLE) {
                bottomSheetFab.setVisibility(View.GONE);
            }

        } /*else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            viewMusk.setVisibility(View.GONE);
            secondaryAppBarLayout.setVisibility(View.GONE);
            defaultAppBerLayout.setVisibility(View.VISIBLE);
            llBottomSheet.animate().scaleY(1).setDuration(0).start();
        }*/
    }


    @Override
    public void init() {
        decor = getWindow().getDecorView();
        menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        //initializing actionbar/toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        secondaryAppBarLayout.addOnOffsetChangedListener(new AppbarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state.toString().equals("EXPANDED") &&
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    llBottomSheet.animate().scaleY(1).setDuration(80).start();
                    relativeLayoutHack.animate().alpha(0).setDuration(0).start();

                } else if (state.toString().equals("COLLAPSED") &&
                        bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    llBottomSheet.animate().scaleY(1 + (1 * 0.05f)).setDuration(80).start();
                    relativeLayoutHack.animate().alpha(1).setDuration(0).start();

//TODO should solve the whiteborad task
                }
            }
        });

    }

    @Override
    public void findView() {

        switchAcountBtn = findViewById(R.id.switch_account_btn);
        swipeRefreshLayout = findViewById(R.id.s_R_Layout);
        fab = findViewById(R.id.fab);
        bottomSheetFab = findViewById(R.id.bottom_sheet_fab);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();

        drawer = findViewById(R.id.drawer_layout);
        home = menu.findItem(R.id.home_id);
        records = menu.findItem(R.id.records);
        payments = menu.findItem(R.id.payments);
        messages = menu.findItem(R.id.messages);
        notifications = menu.findItem(R.id.notifications);
        free_cashback = menu.findItem(R.id.free_cashback);
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

        llBottomSheet = findViewById(R.id.bottom_sheet);
        viewMusk = findViewById(R.id.view_musk);
        searchMentorBtn = findViewById(R.id.search_mentor_btn);
        defaultAppBerLayout = findViewById(R.id.my_appbarLayout);
        secondaryAppBarLayout = findViewById(R.id.secondary_Appbar);
        secondaryAppBarLayout.getHeight();
        relativeLayoutHack = findViewById(R.id.relativeLayout_hack);
    }

    public void subMenu() {
        home.setVisible(false);
        records.setVisible(false);
        payments.setVisible(false);
        messages.setVisible(false);
        notifications.setVisible(false);
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
        free_cashback.setVisible(true);
        forum.setVisible(true);
        help.setVisible(true);
        settings.setVisible(true);
        infoItem.setVisible(true);
        selectAccount.setVisible(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        mPresenter.onMenuItemInteracted(item);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }

    @Override
    public void onBottomSheetClicked() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            if (firstTime) {
                fab.animate().translationYBy((float) (-60.0f * (getResources().getDisplayMetrics().density))).setDuration(100).start();
                firstTime = false;
            }
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decor.setSystemUiVisibility(0);
            }
            viewMusk.setVisibility(View.VISIBLE);
            secondaryAppBarLayout.setVisibility(View.VISIBLE);
            defaultAppBerLayout.setVisibility(View.GONE);

        }
    }

    /*
   Updates the count of notifications in the ActionBar.
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
}
