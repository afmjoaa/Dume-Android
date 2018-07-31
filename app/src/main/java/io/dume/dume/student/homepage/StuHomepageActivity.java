package io.dume.dume.student.homepage;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.util.BadgeDrawable;
import io.dume.dume.util.DumeUtils;

public class StuHomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button switchAcountBtn;
    NavigationView navigationView;
    Menu menu;
    MenuItem home, records, payments, messages, notifications, free_cashback, settings, forum, help,
            selectAccount, infoItem, studentProfile, mentorProfile, bootCampProfile;
    Drawable leftDrawable;
    Drawable less;
    Drawable more;
    SwipeRefreshLayout swipeRefreshLayout;
    private int mNotificationsCount = 0;
    private char mProfileChar = '!';
    private int mChatCount = 0;
    private int mRecPendingCount = 0, mRecAcceptedCount = 0, mRecCurrentCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_activity_homepage);
        BadgeDrawable badgeDrawable = new BadgeDrawable(this, 3.0f, 3.0f);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        View decor = getWindow().getDecorView();
        switchAcountBtn = findViewById(R.id.switch_account_btn);
        swipeRefreshLayout = findViewById(R.id.s_R_Layout);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_green_light,
                android.R.color.holo_red_light, android.R.color.holo_blue_light);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 5000);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setAlpha(0.80f);
        Drawable d = fab.getDrawable();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d instanceof Animatable) {
                    ((Animatable) d).start();
                }
                Snackbar snackbar = Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_INDEFINITE);
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

                TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
                textView.setVisibility(View.INVISIBLE);

                LayoutInflater inflater = LayoutInflater.from(StuHomepageActivity.this);

                View snackView = inflater.inflate(R.layout.custom_snackbar_layout, null);
                /*ImageView imageView = (ImageView) snackView.findViewById(R.id.image);
                imageView.setImageBitmap(image);*/

                TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
                textViewStart.setText("Can't reach the Dume network.");
                textViewStart.setTextColor(Color.WHITE);

                layout.setPadding(0, 0, 0, 0);
                layout.setBackgroundColor(ContextCompat.getColor(StuHomepageActivity.this, R.color.snackbar_red));
                CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
                parentParams.height = (int) (28 * (getResources().getDisplayMetrics().density));
                layout.setLayoutParams(parentParams);


                layout.addView(snackView, 0);
                snackbar.show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_menu);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }*/
        // Toolbar :: Transparent
        // toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.getBackground().setAlpha(0);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Status bar :: Transparent
        settingStatusBarTransparent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        menu = navigationView.getMenu();
        init();
        home.setChecked(true);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.stu_homepage, menu);
//        menu.findItem(R.id.al_messages).setVisible(false);

        MenuItem alProfile = menu.findItem(R.id.al_display_pic);
        LayerDrawable alProfileIcon = (LayerDrawable) alProfile.getIcon();
        // Update LayerDrawable's BadgeDrawable
        DumeUtils.setBadgeChar(this, alProfileIcon, 0xfff56161,Color.BLACK,'!', 3.0f, 3.0f);


        MenuItem alNoti = menu.findItem(R.id.al_notifications);
        LayerDrawable alNotiIcon = (LayerDrawable) alNoti.getIcon();
        // Update LayerDrawable's BadgeDrawable
        DumeUtils.setBadgeCount(this, alNotiIcon, R.id.ic_badge, 0xfface0ac , Color.BLACK, mNotificationsCount, 3.0f, 3.0f);


        MenuItem alChat = menu.findItem(R.id.al_messages);
        LayerDrawable alChatIcon = (LayerDrawable) alChat.getIcon();
        // Update LayerDrawable's BadgeDrawable
        DumeUtils.setBadgeCount(this, alChatIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mChatCount, 3.0f, 3.0f);


        MenuItem alRecords = menu.findItem(R.id.al_records);
        LayerDrawable alRecordsIcon = (LayerDrawable) alRecords.getIcon();
        // Update LayerDrawable's BadgeDrawable
        DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeTwo, 0xfff56161, Color.BLACK, mRecCurrentCount, 3.0f, 3.0f);
        DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badgeOne, 0xfff4f094, Color.BLACK, mRecAcceptedCount, 8.0f, -3.4f);
        DumeUtils.setBadgeCount(this, alRecordsIcon, R.id.ic_badge, 0xfface0ac, Color.BLACK, mRecPendingCount,  5.0f, -11.0f);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Drawable drawableGeneral = item.getIcon();
        if (drawableGeneral instanceof Animatable) {
            ((Animatable) drawableGeneral).start();
        }


        if (id == R.id.al_display_pic) {
            updateProfileBadge(mProfileChar);
        } else if (id == R.id.al_notifications) {
            updateNotificationsBadge(++mNotificationsCount);
        } else if (id == R.id.al_messages) {
            updateChatBadge(++mChatCount);
        } else if( id == R.id.al_records){
            updateRecordsBadge(++mRecPendingCount, ++mRecAcceptedCount, ++mRecCurrentCount);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.student) {

        } else if (id == R.id.mentor) {

        } else if (id == R.id.boot_camp) {

        }

/*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onNavigationHeaderClick(View view) {
        switch (view.getId()) {
            case R.id.switch_account_btn:

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
                break;
            case R.id.user_dp:
                break;
        }

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

    public void init() {
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

    }

    private void settingStatusBarTransparent() {

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /*
   Updates the count of notifications in the ActionBar.
    */
    private void updateProfileBadge(char character) {
        mProfileChar = character;
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;
        invalidateOptionsMenu();
    }

    private void updateChatBadge(int count) {
        mChatCount = count;
        invalidateOptionsMenu();
    }

    private void updateRecordsBadge(int penCount, int acptCount, int curCount) {
        mRecPendingCount = penCount;
        mRecAcceptedCount = acptCount;
        mRecCurrentCount = curCount;
        invalidateOptionsMenu();
    }
}


