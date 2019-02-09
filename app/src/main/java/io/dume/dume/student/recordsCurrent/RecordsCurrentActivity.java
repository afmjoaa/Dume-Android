package io.dume.dume.student.recordsCurrent;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
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
import android.text.format.DateFormat;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import io.dume.dume.R;
import io.dume.dume.student.common.QualificationAdapter;
import io.dume.dume.student.common.QualificationData;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.recordsCurrent.calenderDecorator.EventDecorator;
import io.dume.dume.student.recordsCurrent.calenderDecorator.HighlightWeekendsDecorator;
import io.dume.dume.student.recordsCurrent.calenderDecorator.MySelectorDecorator;
import io.dume.dume.student.recordsCurrent.calenderDecorator.OneDayDecorator;
import io.dume.dume.teacher.pojo.Academic;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.TimePickerFragment;

public class RecordsCurrentActivity extends CustomStuAppCompatActivity implements RecordsCurrentContract.View,
        MyGpsLocationChangeListener {

    private RecordsCurrentContract.Presenter mPresenter;
    private static final int fromFlag = 23;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;
    private BottomSheetDialog mBottomSheetContactDialog;
    private View contactSheetView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu13_activity_records_current);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new RecordsCurrentPresenter(this, new RecordsCurrentModel());
        mPresenter.recordsCurrentEnqueue();
        DumeUtils.configureAppbar(this, "Current Records");

    }

    @Override
    public void findView() {
        pager = (ViewPager) findViewById(R.id.current_page_container);

    }

    @Override
    public void contactBtnClicked() {
        mBottomSheetContactDialog = new BottomSheetDialog(this);
        contactSheetView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_call_msg, null);
        mBottomSheetContactDialog.setContentView(contactSheetView);
        mBottomSheetContactDialog.show();
    }

    @Override
    public void initRecordsCurrent() {

    }

    @Override
    public void configRecordsCurrent() {
        myPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);
    }

    public void onRecordsCurrentViewClicked(View view) {
        mPresenter.onRecordsCurrentIntracted(view);
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
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        private EditText reminderEditText;
        private TimePickerFragment timePickerReminder;
        private ImageView calenderCurDateImageView;
        private MaterialCalendarView myCalenderView;
        private OneDayDecorator oneDayDecorator;


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
            //testing the layer drawable for  the calender current date
            calenderCurDateImageView = rootView.findViewById(R.id.current_date_imageview);
            locationMapHost = rootView.findViewById(R.id.location_layout_vertical);
            notificationEditText = rootView.findViewById(R.id.notification_edittext);
            reminderEditText = rootView.findViewById(R.id.reminder_edittext);

            //fucking the code for calender decorator
            oneDayDecorator = new OneDayDecorator();
            myCalenderView = rootView.findViewById(R.id.calendarView);
            myCalenderView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay date, boolean selected) {
                    //If you change a decorate, you need to invalidate decorators
                    oneDayDecorator.setDate(date.getDate());
                    materialCalendarView.invalidateDecorators();
                }
            });
            //setting the minimum and maximum date and day value
            final LocalDate instance = LocalDate.now();
            myCalenderView.setSelectedDate(instance);
            final LocalDate min = LocalDate.of(instance.getYear(), Month.JANUARY, 1);
            final LocalDate max = LocalDate.of(instance.getYear(), Month.DECEMBER, 31);
            myCalenderView.state().edit().setMinimumDate(min).setMaximumDate(max).commit();
            //adding the decorators
            myCalenderView.addDecorators(
                    new MySelectorDecorator(myThisActivity),
                    new HighlightWeekendsDecorator(),
                    oneDayDecorator
            );
            new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

            timePicker = new TimePickerFragment();
            timePicker.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String myTime;
                    if (DateFormat.is24HourFormat(getActivity())) {
                        myTime = "" + hourOfDay + ":" + minute;
                    } else {
                        String AM_PM;
                        int myHourOfDay = hourOfDay;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        if (hourOfDay >= 13) {
                            myHourOfDay = hourOfDay - 12;
                        } else if (hourOfDay == 0) {
                            myHourOfDay = 12;
                        }
                        myTime = "" + myHourOfDay + ":" + minute + " " + AM_PM;
                        notificationEditText.setText(myTime);
                    }
                }
            });
            notificationEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePicker.show(getChildFragmentManager(), "time_notification");
                }
            });


            //testting the reminder here
            timePickerReminder = new TimePickerFragment();
            timePickerReminder.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String myTime;
                    if (DateFormat.is24HourFormat(getActivity())) {
                        myTime = "" + hourOfDay + ":" + minute;
                    } else {
                        String AM_PM;
                        int myHourOfDay = hourOfDay;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        if (hourOfDay >= 13) {
                            myHourOfDay = hourOfDay - 12;
                        } else if (hourOfDay == 0) {
                            myHourOfDay = 12;
                        }
                        myTime = "" + myHourOfDay + ":" + minute + " " + AM_PM;
                        reminderEditText.setText(myTime);
                    }
                }
            });
            reminderEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerReminder.show(getChildFragmentManager(), "time_reminder");
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

            LayerDrawable calenderCurDateLayDraw = (LayerDrawable) calenderCurDateImageView.getDrawable();
            DumeUtils.setTextOverDrawable(myThisActivity, calenderCurDateLayDraw, R.id.ic_badge, Color.WHITE, "20", 2);


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
            List<Academic> academicList = new ArrayList<>();
            qualificaitonRecyAda = new QualificationAdapter(myThisActivity,academicList);
            qualificationRecyView.setAdapter(qualificaitonRecyAda);
            qualificationRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));


            //setting the review recycler view
            List<ReviewHighlightData> reviewData = new ArrayList<>();
            ReviewAdapter reviewRecyAda = new ReviewAdapter(myThisActivity, reviewData);
            reviewRecyView.setAdapter(reviewRecyAda);
            reviewRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));
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

        /**
         * Simulate an API call to show how to add decorators
         */
        private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

            @Override
            protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LocalDate temp = LocalDate.now().minusMonths(2);
                final ArrayList<CalendarDay> dates = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    final CalendarDay day = CalendarDay.from(temp);
                    dates.add(day);
                    temp = temp.plusDays(5);
                }
                return dates;
            }

            @Override
            protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
                super.onPostExecute(calendarDays);
                if (myThisActivity.isFinishing()) {
                    return;
                }
                myCalenderView.addDecorator(new EventDecorator(0xFF009688, calendarDays, myThisActivity));
            }
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
