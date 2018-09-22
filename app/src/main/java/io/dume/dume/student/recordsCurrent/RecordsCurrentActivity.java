package io.dume.dume.student.recordsCurrent;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;
import io.dume.dume.student.common.QualificationAdapter;
import io.dume.dume.student.common.QualificationData;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.TimePickerFragment;

public class RecordsCurrentActivity extends CustomStuAppCompatActivity implements RecordsCurrentContract.View,
        MyGpsLocationChangeListener {

    private RecordsCurrentContract.Presenter mPresenter;
    private static final int fromFlag = 23;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu13_activity_records_current);
        setActivityContext(this, fromFlag);
        mPresenter = new RecordsCurrentPresenter(this, new RecordsCurrentModel());
        mPresenter.recordsCurrentEnqueue();
        DumeUtils.configureAppbar(this, "Current Records");

    }

    @Override
    public void findView() {
        pager = (ViewPager) findViewById(R.id.current_page_container);

    }

    @Override
    public void initRecordsCurrent() {

    }

    @Override
    public void configRecordsCurrent() {
        myPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

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
        private RecordsCurrentActivity myThisActivity;
        private RecyclerView qualificationRecyView;
        private QualificationAdapter qualificaitonRecyAda;
        private RecyclerView reviewRecyView;
        private ChartProgressBar mChart;
        private ImageView ratingPerformance;
        private ImageView ratingExperience;
        boolean scrollFirstTime = true;
        private LinearLayout locationMapHost;
        private View locationMapHostContent;
        private EditText notificationEditText;
        private TimePickerFragment timePicker;


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
            myThisActivity = (RecordsCurrentActivity) getActivity();
            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.stu13_viewpager_layout_current, container, false);
            qualificationRecyView = rootView.findViewById(R.id.recycler_view_qualifications);
            reviewRecyView = rootView.findViewById(R.id.recycler_view_reviews);
            mChart = (ChartProgressBar) rootView.findViewById(R.id.myChartProgressBar);
            ratingPerformance = rootView.findViewById(R.id.main_rating_performance);
            ratingExperience = rootView.findViewById(R.id.main_rating_experience);
            locationMapHost = rootView.findViewById(R.id.location_layout_vertical);
            notificationEditText = rootView.findViewById(R.id.notification_edittext);


            timePicker = new TimePickerFragment();
            timePicker.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Toast.makeText(myThisActivity, "fuck fuck", Toast.LENGTH_SHORT).show();
                }
            });

            notificationEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePicker.show(getChildFragmentManager(), "time picker");
                }
            });


            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
            if (mapFragment == null) {
                mapFragment = new SupportMapFragment();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.location_layout_vertical, mapFragment, "mapFragment");
                ft.commit();
                fm.executePendingTransactions();
            }



            //setting text over drawable
            LayerDrawable performanceLayDraw = (LayerDrawable) ratingPerformance.getDrawable();
            DumeUtils.setTextOverDrawable(myThisActivity, performanceLayDraw, R.id.ic_badge, Color.BLACK, "4.89");

            LayerDrawable experienceLayDraw = (LayerDrawable) ratingExperience.getDrawable();
            DumeUtils.setTextOverDrawable(myThisActivity, experienceLayDraw, R.id.ic_badge, Color.BLACK, "00");

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



            //setting the qualification recycler view
            qualificaitonRecyAda = new QualificationAdapter(myThisActivity, getQualificationData());
            qualificationRecyView.setAdapter(qualificaitonRecyAda);
            qualificationRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));


            //setting the review recycler view
            List<ReviewHighlightData> reviewData = new ArrayList<>();
            ReviewAdapter reviewRecyAda = new ReviewAdapter(myThisActivity, reviewData);
            reviewRecyView.setAdapter(reviewRecyAda);
            reviewRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));
            reviewRecyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && scrollFirstTime) {
                        Toast.makeText(myThisActivity, "Last", Toast.LENGTH_SHORT).show();
                        scrollFirstTime = false;
                    }
                }
            });



            return rootView;
        }

        public List<QualificationData> getQualificationData() {
            List<QualificationData> data = new ArrayList<>();
            String[] institutionTextArr = getResources().getStringArray(R.array.QualificationInstitutionText);
            String[] examTextArr = getResources().getStringArray(R.array.QualificationExamText);
            String[] sessionTextArr = getResources().getStringArray(R.array.QualificationSessionText);
            String[] resultTextArr = getResources().getStringArray(R.array.QualificationResultText);


            for (int i = 0; i < institutionTextArr.length
                    && i < examTextArr.length
                    && i < sessionTextArr.length
                    && i < resultTextArr.length; i++) {
                QualificationData current = new QualificationData();
                current.institution = institutionTextArr[i];
                current.exam = examTextArr[i];
                current.session = sessionTextArr[i];
                current.result = resultTextArr[i];
                data.add(current);
            }
            return data;
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
