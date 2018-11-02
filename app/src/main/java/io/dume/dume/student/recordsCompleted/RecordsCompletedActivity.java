package io.dume.dume.student.recordsCompleted;

import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import java.util.ArrayList;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.util.DumeUtils;

public class RecordsCompletedActivity extends CustomStuAppCompatActivity implements RecordsCompletedContract.View {

    private RecordsCompletedContract.Presenter mPresenter;
    private static final int fromFlag = 24;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu11_activity_records_completed);
        setActivityContext(this, fromFlag);
        mPresenter = new RecordsCompletedPresenter(this, new RecordsCompletedModel());
        mPresenter.recordsCompletedEnqueue();
        DumeUtils.configureAppbar(this, "Completed Records");


    }

    @Override
    public void findView() {
        pager = (ViewPager) findViewById(R.id.completed_page_container);

    }

    @Override
    public void initRecordsCompleted() {

    }

    @Override
    public void configRecordsCompleted() {
        myPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_help:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //testing code goes here
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecordsCompletedActivity myThisActivity;
        private ChartProgressBar mChart;
        private ImageView ratingPerformance;
        private ImageView ratingExperience;



        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myThisActivity = (RecordsCompletedActivity) getActivity();
            View rootView = inflater.inflate(R.layout.stu11_viewpager_layout_completed, container, false);
            mChart = (ChartProgressBar) rootView.findViewById(R.id.myChartProgressBar);
            ratingPerformance = rootView.findViewById(R.id.main_rating_performance);
            ratingExperience = rootView.findViewById(R.id.main_rating_experience);

            //setting text over drawable
            LayerDrawable performanceLayDraw = (LayerDrawable) ratingPerformance.getDrawable();
            DumeUtils.setTextOverDrawable(myThisActivity, performanceLayDraw, R.id.ic_badge, Color.BLACK, "4.89");
            //ratingPerformance.setImageDrawable(performanceLayDraw);

            LayerDrawable experienceLayDraw = (LayerDrawable) ratingExperience.getDrawable();
            DumeUtils.setTextOverDrawable(myThisActivity, experienceLayDraw, R.id.ic_badge, Color.BLACK, "00");
            //ratingExperience.setImageDrawable(experienceLayDraw);

            ArrayList<BarData> dataList = new ArrayList<>();

            BarData data = new BarData("Comm.", 3.4f, "3.4€");
            dataList.add(data);

            data = new BarData("Behaviour", 7.3f, "7.3€");
            dataList.add(data);

            data = new BarData("Phy", 10f, "8€");
            dataList.add(data);

            data = new BarData("Chem", 6.2f, "6.2€");
            dataList.add(data);

            data = new BarData("Math", 3.3f, "3.3€");
            dataList.add(data);

            mChart.setDataList(dataList);
            mChart.build();

            return rootView;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
