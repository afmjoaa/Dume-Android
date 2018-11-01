package io.dume.dume.teacher.homepage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import io.dume.dume.teacher.adapters.FeedBackAdapter;
import io.dume.dume.teacher.adapters.InboxAdapter;
import io.dume.dume.teacher.mentor_settings.AccountSettings;
import io.dume.dume.teacher.pojo.Feedback;
import io.dume.dume.teacher.pojo.Inbox;
import io.dume.dume.teacher.skill.SkillActivity;

public class TeacherActivtiy extends AppCompatActivity implements TeacherContract.View, NavigationView.OnNavigationItemSelectedListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new TeacherPresenter(this, new TeacherModel());
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
                mToolTipsManager.dismissAll();
                mToolTipsManager.show(new ToolTip(new ToolTip.Builder(context, view, rvWrapper, "I am tooltip", ToolTip.POSITION_BELOW)));
            }
        });

    }

    @Override
    public void showInboxRV(ArrayList<Inbox> list) {
        inboxRv.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
        inboxRv.setAdapter(new InboxAdapter(list));
    }

    private void initAdvance() {
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
}
