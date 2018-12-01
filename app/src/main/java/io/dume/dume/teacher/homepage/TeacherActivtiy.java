package io.dume.dume.teacher.homepage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.teacher.adapters.FeedBackAdapter;
import io.dume.dume.teacher.adapters.InboxAdapter;
import io.dume.dume.teacher.mentor_settings.AccountSettings;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;
import io.dume.dume.teacher.skill.SkillActivity;


public class TeacherActivtiy extends CustomStuAppCompatActivity implements TeacherContract.View, NavigationView.OnNavigationItemSelectedListener {
    private TeacherContract.Presenter presenter;
    private TextView textView;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private Button signOutButton;
    private static final String TAG = "TeacherActivtiy";
    private NavigationView navigationView;
    ToolTipsManager mToolTipsManager;
    @BindView(R.id.feedbackRV)
    public RecyclerView feedBackRV;
    private Context context;
    @BindView(R.id.teacherActivitySV)
    public FrameLayout rvWrapper;
    @BindView(R.id.inboxRV)
    RecyclerView inboxRv;
    @BindView(R.id.teacherChartStatistics)
    LineChart lineChart;
    private Menu menu;
    private MenuItem home, records, payments, messages, notifications, heat_map, free_cashback, settings, forum, help, selectAccount, infoItem, studentProfile, mentorProfile, bootCampProfile;
    private Drawable less;
    private Drawable more;
    private Drawable leftDrawable;
    private Button switchAcountBtn;
    private MenuItem skills;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setActivityContext(this, 0461);
        presenter = new TeacherPresenter(this, new TeacherModel());
        settingStatusBarTransparent();
        setDarkStatusBarIcon();
    }


    @Override
    public void showChart(LineData data) {
        lineChart.setData(data);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        YAxis axisLeft = lineChart.getAxisLeft();
        axisLeft.setDrawGridLines(false);
        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);
        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        lineChart.setTouchEnabled(false);
        lineChart.enableScroll();
        lineChart.invalidate();

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
    }

    @Override
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFeedbackRV(ArrayList<Feedback> list) {
        feedBackRV.setLayoutManager(new GridLayoutManager(this, 3));
        feedBackRV.setAdapter(new FeedBackAdapter(list) {
            @Override
            public void onItemClick(int position, View view) {

            }
        });

    }

    @Override
    public void showInboxRV(ArrayList<Inbox> list) {
        inboxRv.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        inboxRv.setAdapter(new InboxAdapter(list));
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


        navigationView.setNavigationItemSelectedListener(this);
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

}
