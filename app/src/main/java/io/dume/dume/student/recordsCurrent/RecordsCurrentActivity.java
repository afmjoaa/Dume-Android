package io.dume.dume.student.recordsCurrent;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.temporal.TemporalAdjusters;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.broadcastReceiver.MyAlarmBroadCast;
import io.dume.dume.common.inboxActivity.InboxActivity;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.QualificationAdapter;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity;
import io.dume.dume.student.recordsCurrent.calenderDecorator.EventDecorator;
import io.dume.dume.student.recordsCurrent.calenderDecorator.HighlightWeekendsDecorator;
import io.dume.dume.student.recordsCurrent.calenderDecorator.MySelectorDecorator;
import io.dume.dume.student.recordsCurrent.calenderDecorator.OneDayDecorator;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Academic;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.TimePickerFragment;
import io.dume.dume.util.VisibleToggleClickListener;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static io.dume.dume.util.DumeUtils.getAddress;
import static io.dume.dume.util.DumeUtils.getLast;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmap;

public class RecordsCurrentActivity extends CustomStuAppCompatActivity implements RecordsCurrentContract.View,
        MyGpsLocationChangeListener {

    private RecordsCurrentContract.Presenter mPresenter;
    private static final int fromFlag = 23;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;
    private BottomSheetDialog mBottomSheetContactDialog;
    private View contactSheetView;
    private String retriveAction = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu13_activity_records_current);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new RecordsCurrentPresenter(this, new RecordsCurrentModel());
        mPresenter.recordsCurrentEnqueue();
        DumeUtils.configureAppbar(this, "Current Records");
        if (getIntent().getAction() != null) {
            retriveAction = getIntent().getAction();
        }
    }

    @Override
    public void findView() {
        pager = (ViewPager) findViewById(R.id.current_page_container);

    }

    @Override
    public void flush(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
        Intent retrivedIntent = getIntent();
        int pageToOpen = retrivedIntent.getIntExtra(DumeUtils.RECORDTAB, -1);
        String recordId = retrivedIntent.getStringExtra("recordId");

        if (pageToOpen != -1 && pageToOpen < Objects.requireNonNull(pager.getAdapter()).getCount()) {
            // Open the right pager
            pager.setCurrentItem(pageToOpen, true);
        }else if(recordId != null && !recordId.equals("")){
            List<DocumentSnapshot> currentRecords = DumeUtils.filterList(Google.getInstance().getRecords(), "Current");
            for (int i = 0; i < currentRecords.size(); i++) {
                DocumentSnapshot record = currentRecords.get(i);
                if (recordId.equals(record.getId())) {
                    pager.setCurrentItem(i, true);
                    break;
                }
            }
        }
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
                Intent intent = new Intent(context, StudentHelpActivity.class);
                intent.setAction("how_to_use");
                startActivity(intent);
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
        private static List<DocumentSnapshot> recordList;
        private RecordsCurrentActivity myThisActivity;
        private RecyclerView qualificationRecyView;
        private QualificationAdapter qualificaitonRecyAda;
        private RecyclerView reviewRecyView;
        private ChartProgressBar mChart;
        private ImageView ratingPerformance;
        private ImageView ratingExperience;
        private EditText reminderEditText;
        private TimePickerFragment timePickerReminder;
        private ImageView calenderCurDateImageView;
        private MaterialCalendarView myCalenderView;
        private OneDayDecorator oneDayDecorator;

        private DocumentSnapshot selectedMentor;
        private Button salaryBtn;
        private carbon.widget.ImageView joinedBadge;
        private carbon.widget.ImageView inauguralBadge;
        private carbon.widget.ImageView leadingBadge;
        private carbon.widget.ImageView premierBadge;
        private TextView currentStatusTV;
        private TextView currentlyMentoringTV;
        private TextView maritalStatusTV;
        private TextView religionTV;
        private TextView genderTV;
        private TextView performanceCount;
        private TextView experienceCount;
        private TextView aRatioCount;
        private TextView expertiseCount;
        private String[] splitMainSsss;
        private Button loadMoreReviewBtn;
        private LinearLayout noDataBlockReview;
        private ReviewHighlightData lastReviewData;
        private CircleProgressbar circleProgressbarARatio;
        private CircleProgressbar circleProgressbarExpertise;
        private SearchDataStore searchDataStore;
        private Context context;
        private carbon.widget.ImageView studentDisplayPic;
        private carbon.widget.ImageView mentorDisplayPic;
        private TextView studentNameTV;
        private TextView mentorNameTV;
        private MaterialRatingBar studentRatingBar;
        private MaterialRatingBar mentorRatingBar;
        private TextView subjectInDemand;
        private TextView salaryInDemandTV;
        private RelativeLayout relativeHostLayout;
        private String defaultUrl;
        private float mDensity;
        private Button moreInfoBtn;
        private Button reviewInfoBtn;
        private LinearLayout reviewHidable;
        private LinearLayout reviewHost;
        private LinearLayout moreInfoHidable;
        private LinearLayout moreInfoHost;
        private LinearLayout achievementHidable;
        private LinearLayout achievementHost;
        private Button achievementInfoBtn;
        private LinearLayout ratingHostVertical;
        private Button showAdditionalRatingBtn;
        private LinearLayout onlyRatingContainer;
        private LinearLayout agreementHostLayout;
        private Button agreementInfoBtn;
        private LinearLayout agreementHideable;
        private static final String TAG = "PlaceholderFragment";
        private ReviewAdapter reviewRecyAda;
        private static DocumentSnapshot record;
        private Button locationShowBtn;
        private LinearLayout locationHideable;
        private LinearLayout locationHostLayout;
        private TextView startingDateValue;
        private TextView finishingDateValue;
        private Long startingYear;
        private Long startingMonth;
        private Long startingDay;
        private Map<String, Object> preferred_days;
        private carbon.widget.ImageView mMarkerImageView;
        private View mCustomMarkerView;
        private Button getDirectionBtn;
        private Button contactBtn;
        private Button cancelBtn;
        private View divider;
        private BottomSheetDialog mBottomSheetContactDialog;
        private View contactSheetView;
        private TextView contactMainText;
        private TextView contactSubText;
        private Button callBtn;
        private Button offlineMsgBtn;
        private Button onlineMsgBtn;
        private BottomSheetDialog mBottomSheetReject;
        private View sheetViewReject;
        private TextView rejectMainText;
        private TextView rejectSubText;
        private Button rejectYesBtn;
        private Button rejectNoBtn;
        private Integer myTimePickerHourOfDay = null;
        private Integer myTimePickerMinite = null;
        private SwitchCompat reminderSwitch;
        private Calendar alarmCalender;
        private SharedPreferences localDb;
        private String recordTimeSuggestedTime;
        private AlarmManager alarmManager;
        private Button stuMoreInfoBtn;
        private LinearLayout stuMoreInfoHidable;
        private LinearLayout stuMoreInfoHost;
        private TextView stuComTV;
        private TextView stuBehaTV;
        private TextView stuGenderTV;
        private TextView stuPreviousResultTV;
        private TextView stuCurrentStatusTV;
        private TextView addressTV;
        private int fragmentPosition;
        private android.widget.ImageView saleImageView;
        private Integer max_dicount_percentage = null;
        private Integer max_discount_credit = null;
        private int validDiscount;
        private String salaryFormatted;


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, List<DocumentSnapshot> recordList) {
            PlaceholderFragment.recordList = recordList;
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            searchDataStore = SearchDataStore.getInstance();
            mDensity = context.getResources().getDisplayMetrics().density;
            localDb = context.getSharedPreferences("DUME_REMINDER", MODE_PRIVATE);
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myThisActivity = (RecordsCurrentActivity) getActivity();
            if (getArguments() != null) {
                fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            }
            record = recordList.get(fragmentPosition);
            View rootView = inflater.inflate(R.layout.stu13_viewpager_layout_current, container, false);
            //testing the layer drawable for  the calender current date
            mCustomMarkerView = ((LayoutInflater) Objects.requireNonNull(context.getSystemService(LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_view, null);
            mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);


            calenderCurDateImageView = rootView.findViewById(R.id.current_date_imageview);
            reminderEditText = rootView.findViewById(R.id.reminder_edittext);

            locationShowBtn = rootView.findViewById(R.id.show_location_btn);
            locationHideable = rootView.findViewById(R.id.location_layout_vertical);
            locationHostLayout = rootView.findViewById(R.id.location_host_linearlayout);

            qualificationRecyView = rootView.findViewById(R.id.recycler_view_qualifications);
            reviewRecyView = rootView.findViewById(R.id.recycler_view_reviews);
            mChart = (ChartProgressBar) rootView.findViewById(R.id.myChartProgressBar);
            ratingPerformance = rootView.findViewById(R.id.main_rating_performance);
            ratingExperience = rootView.findViewById(R.id.main_rating_experience);

            moreInfoBtn = rootView.findViewById(R.id.show_more_info_btn);
            moreInfoHost = rootView.findViewById(R.id.more_info_host_linearlayout);
            moreInfoHidable = rootView.findViewById(R.id.more_info_layout_vertical);

            reviewHost = rootView.findViewById(R.id.review_host_linearlayout);
            reviewHidable = rootView.findViewById(R.id.review_layout_vertical);
            reviewInfoBtn = rootView.findViewById(R.id.review_info_btn);

            achievementInfoBtn = rootView.findViewById(R.id.show_achievement_btn);
            achievementHost = rootView.findViewById(R.id.achievement_host_linearlayout);
            achievementHidable = rootView.findViewById(R.id.achievement_layout_vertical);

            agreementHostLayout = rootView.findViewById(R.id.agreement_term_host_linearlayout);
            agreementInfoBtn = rootView.findViewById(R.id.show_agreement_terms_btn);
            agreementHideable = rootView.findViewById(R.id.agreement_term_layout_vertical);

            ratingHostVertical = rootView.findViewById(R.id.rating_host_linearlayout);
            showAdditionalRatingBtn = rootView.findViewById(R.id.show_additional_rating_btn);
            onlyRatingContainer = rootView.findViewById(R.id.rating_layout_vertical);

            //testing anything can happen
            salaryBtn = rootView.findViewById(R.id.show_salary_btn);
            joinedBadge = rootView.findViewById(R.id.achievement_joined_image);
            inauguralBadge = rootView.findViewById(R.id.achievement_inaugural_image);
            leadingBadge = rootView.findViewById(R.id.achievement_leading_image);
            premierBadge = rootView.findViewById(R.id.achievement_premier_image);

            currentStatusTV = rootView.findViewById(R.id.textview_current_status);
            currentlyMentoringTV = rootView.findViewById(R.id.textview_currently_mentoring);
            maritalStatusTV = rootView.findViewById(R.id.textview_marital_status);
            religionTV = rootView.findViewById(R.id.textview_religion);
            genderTV = rootView.findViewById(R.id.textview_gender);

            performanceCount = rootView.findViewById(R.id.txtStatus);
            experienceCount = rootView.findViewById(R.id.txtStatus_experience);
            circleProgressbarARatio = (CircleProgressbar) rootView.findViewById(R.id.rating_main_accept_ratio);
            aRatioCount = rootView.findViewById(R.id.txtStatus_accept_ratio);
            circleProgressbarExpertise = (CircleProgressbar) rootView.findViewById(R.id.rating_main_professionalism);
            expertiseCount = rootView.findViewById(R.id.txtStatus_professionalism);

            loadMoreReviewBtn = rootView.findViewById(R.id.load_more_review_btn);
            noDataBlockReview = rootView.findViewById(R.id.no_data_block);

            relativeHostLayout = rootView.findViewById(R.id.recordsHostLayout);
            studentDisplayPic = rootView.findViewById(R.id.student_display_pic);
            mentorDisplayPic = rootView.findViewById(R.id.mentor_display_pic);
            studentNameTV = rootView.findViewById(R.id.student_name);
            mentorNameTV = rootView.findViewById(R.id.mentor_name);
            studentRatingBar = rootView.findViewById(R.id.student_rating_bar);
            mentorRatingBar = rootView.findViewById(R.id.mentor_rating_bar);
            subjectInDemand = rootView.findViewById(R.id.subject_in_demand);
            salaryInDemandTV = rootView.findViewById(R.id.salary_in_demand);

            startingDateValue = rootView.findViewById(R.id.starting_date_value);
            finishingDateValue = rootView.findViewById(R.id.finishing_date_value);
            getDirectionBtn = rootView.findViewById(R.id.get_direction_btn);

            contactBtn = rootView.findViewById(R.id.current_contact_btn);
            cancelBtn = rootView.findViewById(R.id.current_cancel_btn);
            divider = rootView.findViewById(R.id.divider3);
            reminderSwitch = rootView.findViewById(R.id.reminder_switch);
            saleImageView = rootView.findViewById(R.id.sale_image);

            addressTV = rootView.findViewById(R.id.address_textView);
            stuMoreInfoBtn = rootView.findViewById(R.id.stu_show_more_info_btn);
            stuMoreInfoHost = rootView.findViewById(R.id.stu_more_info_host_linearlayout);
            stuMoreInfoHidable = rootView.findViewById(R.id.stu_more_info_layout_vertical);
            stuComTV = rootView.findViewById(R.id.stu_textview_communication);
            stuBehaTV = rootView.findViewById(R.id.stu_textview_behaviour);
            stuGenderTV = rootView.findViewById(R.id.stu_textview_gender);
            stuPreviousResultTV = rootView.findViewById(R.id.stu_previous_result);
            stuCurrentStatusTV = rootView.findViewById(R.id.stu_textview_current_status);

            //setting the qualification recycler view
            List<Academic> academicList = new ArrayList<>();
            qualificaitonRecyAda = new QualificationAdapter(myThisActivity, academicList);
            qualificationRecyView.setAdapter(qualificaitonRecyAda);
            qualificationRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));

            //setting the review recycler view
            List<ReviewHighlightData> reviewData = new ArrayList<>();
            ReviewAdapter reviewRecyAda = new ReviewAdapter(myThisActivity, reviewData);
            reviewRecyView.setAdapter(reviewRecyAda);
            reviewRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));
            onMentorSelect(record);
            configFragmentBtnClick();
            toggleStatus();

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


            //testting the reminder here
            timePickerReminder = new TimePickerFragment();
            timePickerReminder.setTimePickerListener(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    myTimePickerHourOfDay = hourOfDay;
                    myTimePickerMinite = minute;
                    if (reminderSwitch.isChecked()) {

                        if (isAlarmExists(record.getId().hashCode())) {
                            stopAlarm(record.getId().hashCode());
                        }
                        setReminder(record.getId(), true, myTimePickerHourOfDay, myTimePickerMinite);
                        setAlarm(myTimePickerHourOfDay, myTimePickerMinite, record.getId().hashCode());

                    }
                    String myTime;
                    if (DateFormat.is24HourFormat(getActivity())) {
                        if (minute < 10) {
                            myTime = "" + hourOfDay + ":0" + minute;
                        } else {
                            myTime = "" + hourOfDay + ":" + minute;
                        }
                    } else {
                        String AM_PM;
                        if (hourOfDay < 12) {
                            AM_PM = "AM";
                        } else {
                            AM_PM = "PM";
                        }
                        if (hourOfDay >= 13) {
                            myTimePickerHourOfDay = hourOfDay - 12;
                        } else if (hourOfDay == 0) {
                            myTimePickerHourOfDay = 12;
                        }
                        if (minute < 10) {
                            myTime = "" + myTimePickerHourOfDay + ":0" + minute + " " + AM_PM;
                        } else {
                            myTime = "" + myTimePickerHourOfDay + ":" + minute + " " + AM_PM;
                        }
                    }
                    reminderEditText.setText(myTime);
                    //setAlarm(calendar.getTimeInMillis());

                }
            });
            reminderEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerReminder.show(getChildFragmentManager(), "time_reminder");
                }
            });


            BackUpReminder reminderOn = isReminderOn(record.getId());
            if (reminderOn.isSet) {
                String myTime;
                myTimePickerHourOfDay = reminderOn.hour;
                myTimePickerMinite = reminderOn.minute;
                if (DateFormat.is24HourFormat(getActivity())) {
                    if (reminderOn.minute < 10) {
                        myTime = "" + reminderOn.hour + ":0" + reminderOn.minute;
                    } else {
                        myTime = "" + reminderOn.hour + ":" + reminderOn.minute;
                    }
                } else {
                    String AM_PM;
                    if (reminderOn.hour < 12) {
                        AM_PM = "AM";
                    } else {
                        AM_PM = "PM";
                    }
                    if (reminderOn.hour >= 13) {
                        myTimePickerHourOfDay = reminderOn.hour - 12;
                    } else if (reminderOn.hour == 0) {
                        myTimePickerHourOfDay = 12;
                    }
                    if (reminderOn.minute < 10) {
                        myTime = "" + myTimePickerHourOfDay + ":0" + reminderOn.minute + " " + AM_PM;
                    } else {
                        myTime = "" + myTimePickerHourOfDay + ":" + reminderOn.minute + " " + AM_PM;
                    }
                }
                reminderEditText.setText(myTime);
            } else {
                reminderEditText.setText(recordTimeSuggestedTime);
                reminderSwitch.setChecked(false);
            }


            reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (isAlarmExists(record.getId().hashCode())) {
                            stopAlarm(record.getId().hashCode());
                        }
                        setReminder(record.getId(), isChecked, myTimePickerHourOfDay, myTimePickerMinite);
                        setAlarm(myTimePickerHourOfDay, myTimePickerMinite, record.getId().hashCode());
                    } else {
                        setReminder(record.getId(), isChecked, myTimePickerHourOfDay, myTimePickerMinite);

                    }
                }
            });


            return rootView;
        }

        public void stopAlarm(int requestCode) {

            for (int i = 0; i < getSelectedDates().size(); i++) {
                Intent intent = new Intent(getActivity(), MyAlarmBroadCast.class);//the same as up
                intent.setAction(MyAlarmBroadCast.ACTION_ALARM_RECEIVER);//the same as up
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode + i, intent, PendingIntent.FLAG_CANCEL_CURRENT);//the same as up
                alarmManager.cancel(pendingIntent);//important
                pendingIntent.cancel();//important
            }

        }

        public boolean isAlarmExists(int requestCode) {
            Intent intent = new Intent(getActivity(), MyAlarmBroadCast.class);//the same as up
            intent.setAction(MyAlarmBroadCast.ACTION_ALARM_RECEIVER);//the same as up
            boolean isWorking = (PendingIntent.getBroadcast(getActivity(), requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
            Log.d(TAG, "alarm is " + (isWorking ? "" : "not") + " working...");
            return isWorking;
        }


        public BackUpReminder isReminderOn(String id) {
            BackUpReminder backUpReminder = new BackUpReminder();
            backUpReminder.isSet = localDb.getBoolean(id + "_isSet", false);
            backUpReminder.hour = localDb.getInt(id + "_hour", 0);
            backUpReminder.minute = localDb.getInt(id + "_minute", 0);
            return backUpReminder;
        }

        public void setReminder(String id, boolean status, int hour, int minute) {
            SharedPreferences.Editor edit = localDb.edit();
            edit.putBoolean(id + "_isSet", status);
            edit.putInt(id + "_hour", hour);
            edit.putInt(id + "_minute", minute);
            edit.apply();
            edit.commit();

        }

        class BackUpReminder {
            public boolean isSet;
            public int hour;
            public int minute;
        }

        private void setAlarm(int hourOfDay, int minute, int requestCode) {
            Log.w(TAG, "setAlarm: " + requestCode);

            //creating a new intent specifying the broadcast receiver
            Intent i = new Intent(context, MyAlarmBroadCast.class);

            //creating a pending intent using the intent
            alarmCalender = Calendar.getInstance();
            ArrayList<CalendarDay> selectedDates = getSelectedDates();
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            for (int count = 0; count < selectedDates.size(); count++) {
                Calendar instance = Calendar.getInstance();
                PendingIntent pi = PendingIntent.getBroadcast(context, requestCode + count, i, 0);
                CalendarDay calendarDay = selectedDates.get(count);
                Log.w(TAG, "setAlarm: " + calendarDay.getDay());
                alarmCalender.set(calendarDay.getYear(),
                        calendarDay.getMonth() - 1,
                        calendarDay.getDay(),
                        hourOfDay, minute, 0);
                if (alarmManager != null) {
                    if (alarmCalender.getTimeInMillis() >= instance.getTimeInMillis()) {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), pi);
                    }
                }

                Log.w(TAG, "setAlarm: Millis " + alarmCalender.getTimeInMillis());
                Date date = new Date(alarmCalender.getTimeInMillis());
                Log.w(TAG, "setAlarm:" + date.toString());
            }


            /*alarmCalender.set(alarmCalender.get(Calendar.YEAR),
                    alarmCalender.get(Calendar.MONTH),
                    alarmCalender.get(Calendar.DAY_OF_MONTH),
                    hourOfDay, minute, 0);
            if (alarmManager != null) {
                PendingIntent pi = PendingIntent.getBroadcast(myThisActivity, 12, i, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis(), pi);
                PendingIntent pi1 = PendingIntent.getBroadcast(myThisActivity, 13, i, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmCalender.getTimeInMillis() + 30000, pi1);
            }*/
            Toast.makeText(myThisActivity, "Alarm is set", Toast.LENGTH_SHORT).show();

            //setting the repeating alarm that will be fired every day
            //am.setRepeating(AlarmManager.RTC_WAKEUP, time, , pi);
        }

        public void toggleStatus() {
            //confirm bottom sheet
            Map<String, Object> documentData = record.getData();
            Map<String, Object> spMap = (Map<String, Object>) documentData.get("sp_info");
            Map<String, Object> forMap = (Map<String, Object>) documentData.get("for_whom");
            String mentorName = spMap.get("first_name") + " " + spMap.get("last_name");
            String studentName = (String) forMap.get("stu_name");
            String studentPhoneNum = (String) forMap.get("stu_phone_number");
            String mentorPhoneNum = (String) spMap.get("phone_number");

            //cancel bottom sheet
            mBottomSheetReject = new BottomSheetDialog(context);
            sheetViewReject = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
            mBottomSheetReject.setContentView(sheetViewReject);
            rejectMainText = mBottomSheetReject.findViewById(R.id.main_text);
            rejectSubText = mBottomSheetReject.findViewById(R.id.sub_text);
            rejectYesBtn = mBottomSheetReject.findViewById(R.id.cancel_yes_btn);
            rejectNoBtn = mBottomSheetReject.findViewById(R.id.cancel_no_btn);
            if (rejectMainText != null && rejectSubText != null && rejectYesBtn != null && rejectNoBtn != null) {
                rejectMainText.setText("Reject Request");
                rejectSubText.setText("By Rejecting you are making sure you are not willing to mentor " + studentName);
                rejectYesBtn.setText("Yes, Reject");
                rejectNoBtn.setText("No");
                rejectYesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                rejectNoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetReject.dismiss();
                    }
                });
            }
            mBottomSheetContactDialog = new BottomSheetDialog(context);
            contactSheetView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_call_msg, null);
            mBottomSheetContactDialog.setContentView(contactSheetView);
            contactMainText = mBottomSheetContactDialog.findViewById(R.id.main_text);
            contactSubText = mBottomSheetContactDialog.findViewById(R.id.sub_text);
            callBtn = mBottomSheetContactDialog.findViewById(R.id.call_btn);
            offlineMsgBtn = mBottomSheetContactDialog.findViewById(R.id.offline_msg_btn);
            onlineMsgBtn = mBottomSheetContactDialog.findViewById(R.id.online_msg_btn);
            if (contactMainText != null && contactSubText != null && callBtn != null && offlineMsgBtn != null && onlineMsgBtn != null) {
                callBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetContactDialog.dismiss();
                        Uri u = Uri.parse("tel:" + studentPhoneNum);
                        switch (myThisActivity.retriveAction) {
                            case DumeUtils.STUDENT:
                                u = Uri.parse("tel:" + mentorPhoneNum);
                                break;
                            case DumeUtils.TEACHER:
                                u = Uri.parse("tel:" + studentPhoneNum);
                                break;
                            case DumeUtils.BOOTCAMP:
                                u = Uri.parse("tel:" + studentPhoneNum);
                                break;
                        }
                        Intent i = new Intent(Intent.ACTION_DIAL, u);
                        context.startActivity(i);
                    }
                });

                offlineMsgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetContactDialog.dismiss();
                        Uri uri = Uri.parse("smsto:" + studentPhoneNum);
                        switch (myThisActivity.retriveAction) {
                            case DumeUtils.STUDENT:
                                uri = Uri.parse("smsto:" + mentorPhoneNum);
                                break;
                            case DumeUtils.TEACHER:
                                uri = Uri.parse("smsto:" + studentPhoneNum);
                                break;
                            case DumeUtils.BOOTCAMP:
                                uri = Uri.parse("smsto:" + studentPhoneNum);
                                break;
                        }
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "");
                        startActivity(intent);
                    }
                });

                onlineMsgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent inboxActivity = new Intent(context, InboxActivity.class);
                        inboxActivity.setAction("from_record");
                        startActivity(inboxActivity);
                    }
                });
            }

            switch (myThisActivity.retriveAction) {
                case DumeUtils.STUDENT:
                    cancelBtn.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    contactMainText.setText("Contact with " + mentorName);
                    stuMoreInfoHost.setVisibility(View.GONE);
                    locationShowBtn.setText("Mentor Location");
                    break;
                case DumeUtils.TEACHER:
                    cancelBtn.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    contactMainText.setText("Contact with " + studentName);
                    stuMoreInfoHost.setVisibility(View.VISIBLE);
                    showAdditionalRatingBtn.setText("Your Rating");
                    achievementInfoBtn.setText("Your Achievements");
                    moreInfoBtn.setText("Your Info");
                    locationShowBtn.setText("Student Location");
                    break;
                case DumeUtils.BOOTCAMP:
                    cancelBtn.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    break;
                default:
                    cancelBtn.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    break;
            }

            contactBtn.setOnClickListener(view -> {
                mBottomSheetContactDialog.show();
            });
            cancelBtn.setOnClickListener(view -> {
                mBottomSheetReject.show();
            });

        }

        public void onMentorSelect(DocumentSnapshot selectedMentor) {
            this.selectedMentor = selectedMentor;
            Map<String, Object> sp_info = (Map<String, Object>) selectedMentor.get("sp_info");

            //TODO fuck holder data
            String mentorName;
            String studentName;
            String salaryInDemand;
            String subjectExchange;
            String mentorDpUrl;
            String studentDpUrl, sGender, mGender;
            float studentRating;
            float mentorRating;
            String status;
            int deliveryStatus;
            Map<String, Object> documentData = selectedMentor.getData();
            Map<String, Object> spMap = (Map<String, Object>) documentData.get("sp_info");
            mentorName = spMap.get("first_name") + " " + spMap.get("last_name");
            mentorDpUrl = (String) spMap.get("avatar");
            Map<String, Object> bal = (Map<String, Object>) spMap.get("self_rating");
            mentorRating = Float.parseFloat((String) bal.get("star_rating"));
            Map<String, Object> forMap = (Map<String, Object>) documentData.get("for_whom");
            Map<String, Object> shMap = (Map<String, Object>) forMap.get("request_sr");
            studentRating = Float.parseFloat((String) shMap.get("star_rating"));
            studentName = (String) forMap.get("stu_name");
            studentDpUrl = (String) forMap.get("request_avatar");
            salaryInDemand = String.valueOf((Number) documentData.get("salary"));
            sGender = (String) forMap.get("request_gender");
            mGender = (String) spMap.get("gender");
            subjectExchange = DumeUtils.getLast((Map<String, Object>) documentData.get("jizz"));
            Date creation = (Date) documentData.get("creation");
            status = (String) documentData.get("record_status");
            Record record = new Record(mentorName, studentName, salaryInDemand, subjectExchange, creation, mentorDpUrl, studentDpUrl, studentRating, mentorRating, status, Record.DELIVERED, sGender, mGender);

            mentorNameTV.setText(record.getMentorName());
            studentNameTV.setText(record.getStudentName());
            salaryInDemand = record.getSalaryInDemand();
            salaryInDemand = NumberFormat.getCurrencyInstance(Locale.US).format(Double.parseDouble(salaryInDemand));
            salaryInDemandTV.setText(salaryInDemand.substring(1, salaryInDemand.length() - 3) + " BDT");
            subjectInDemand.setText(record.getSubjectExchange());

            if (record.getStudentDpUrl() != null && !record.getStudentDpUrl().equals("")) {
                Glide.with(context).load(record.getStudentDpUrl()).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(studentDisplayPic)
                ;
            } else {
                if (record.getsGender() != null || record.getsGender().equals("Male") || record.getsGender().equals("")) {
                    defaultUrl = SearchDataStore.DEFAULTMALEAVATER;
                } else {
                    defaultUrl = SearchDataStore.DEFAULTFEMALEAVATER;
                }
                Glide.with(context).load(defaultUrl).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(studentDisplayPic)
                ;
            }

            Glide.with(context).load(record.getMentorDpUrl()).into(mentorDisplayPic);
            mentorRatingBar.setRating(record.getMentorRating());
            studentRatingBar.setRating(record.getStudentRating());

            //fuck the location data here
            GeoPoint student_location = selectedMentor.getGeoPoint("anchor_point");
            LatLng stu_location_lat_lng = new LatLng(student_location.getLatitude(), student_location.getLongitude());

            GeoPoint mentor_location = selectedMentor.getGeoPoint("location");
            LatLng mentor_location_lat_lng = new LatLng(mentor_location.getLatitude(), mentor_location.getLongitude());

            FragmentManager fm = getChildFragmentManager();
            SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");
            if (mapFragment == null) {
                mapFragment = new SupportMapFragment();
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                                myThisActivity, R.raw.loading_map_style_one);
                        googleMap.setMapStyle(style);
                        //GoogleMapOptions googleMapOptions = new GoogleMapOptions().liteMode(true);
                        //googleMap.setMapType(googleMapOptions.getMapType());
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        if (myThisActivity.retriveAction != null) {
                            switch (myThisActivity.retriveAction) {
                                case DumeUtils.STUDENT:
                                    addressTV.setText(addressTV.getText() + getAddress(context, mentor_location_lat_lng.latitude, mentor_location_lat_lng.longitude));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mentor_location_lat_lng, 15.25f));
                                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                        @Override
                                        public void onMapLoaded() {
                                            final float thisZoom = googleMap.getCameraPosition().zoom;
                                            googleMap.setMaxZoomPreference((thisZoom + 2f));
                                            googleMap.setMinZoomPreference((thisZoom - 2f));
                                            addCustomMarkerFromURL(googleMap, record.getMentorDpUrl(), mentor_location_lat_lng, record.getmGender());
                                        }
                                    });
                                    break;
                                case DumeUtils.TEACHER:
                                case DumeUtils.BOOTCAMP:
                                    addressTV.setText(addressTV.getText() + getAddress(context, stu_location_lat_lng.latitude, stu_location_lat_lng.longitude));
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stu_location_lat_lng, 15.25f));
                                    googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                        @Override
                                        public void onMapLoaded() {
                                            final float thisZoom = googleMap.getCameraPosition().zoom;
                                            googleMap.setMaxZoomPreference((thisZoom + 2f));
                                            googleMap.setMinZoomPreference((thisZoom - 2f));
                                            addCustomMarkerFromURL(googleMap, record.getStudentDpUrl(), stu_location_lat_lng, record.getsGender());
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.location_layout_inside, mapFragment, "mapFragment");
                ft.commit();
                fm.executePendingTransactions();
            }

            getDirectionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri;
                    if (myThisActivity.retriveAction != null) {
                        switch (myThisActivity.retriveAction) {
                            case DumeUtils.STUDENT:
                                uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", mentor_location_lat_lng.latitude, mentor_location_lat_lng.longitude, record.getMentorName() + " address");
                                break;
                            case DumeUtils.TEACHER:
                            case DumeUtils.BOOTCAMP:
                                uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", stu_location_lat_lng.latitude, stu_location_lat_lng.longitude, record.getStudentName() + " address");
                                break;
                            default:
                                uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", mentor_location_lat_lng.latitude, mentor_location_lat_lng.longitude, record.getMentorName() + " address");
                                break;
                        }
                    } else {
                        uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", mentor_location_lat_lng.latitude, mentor_location_lat_lng.longitude, record.getMentorName() + " address");
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException ex) {
                        try {
                            Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(unrestrictedIntent);
                        } catch (ActivityNotFoundException innerEx) {
                            Toast.makeText(context, "Please install any maps application", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


            //fill up all info of the mentor
            NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
            Number salary = (Number) selectedMentor.get("salary");

            Map<String, Object> promo = (Map<String, Object>) selectedMentor.get("promo");
            if(promo!= null){
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(promo);
                HomePageRecyclerData homePageRecyclerData = gson.fromJson(jsonElement, HomePageRecyclerData.class);
                if (homePageRecyclerData != null) {
                    max_dicount_percentage = homePageRecyclerData.getMax_dicount_percentage();
                    max_discount_credit = homePageRecyclerData.getMax_discount_credit();

                }
            }
            if (max_dicount_percentage != null && max_discount_credit != null) {
                Number calculatedCreditOff = salary.intValue() * max_dicount_percentage * 0.01;
                validDiscount = Math.min(max_discount_credit, calculatedCreditOff.intValue());
                String perviousSalaryFormatted = currencyInstance.format(salary.intValue());
                salaryFormatted = currencyInstance.format(salary.intValue() - validDiscount);
                SpannableString text = new SpannableString("Salary : " + perviousSalaryFormatted.substring(1, perviousSalaryFormatted.length() - 3) + " BDT " + salaryFormatted.substring(1, salaryFormatted.length() - 3) + " BDT");
                text.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.textColorPrimary)), 9, 9 + (perviousSalaryFormatted.length()), 0);
                text.setSpan(new StrikethroughSpan(), 9, 9 + (perviousSalaryFormatted.length()), 0);
                salaryBtn.setText(text);
                saleImageView.setVisibility(View.VISIBLE);

            } else {
                saleImageView.setVisibility(View.GONE);
                salaryFormatted = currencyInstance.format(salary);
                salaryBtn.setText("Salary : " + salaryFormatted.substring(1, salaryFormatted.length() - 3) + " BDT");
            }

            loadQualificationData(sp_info);
            //setting the achievements badge
            Map<String, Object> achievements = (Map<String, Object>) sp_info.get("achievements");
            if ((boolean) achievements.get("joined")) {
                joinedBadge.setImageResource(R.drawable.ic_badge_joined);
            }
            if ((boolean) achievements.get("inaugural")) {
                inauguralBadge.setImageResource(R.drawable.ic_badge_inaugural);
            }
            if ((boolean) achievements.get("leading")) {
                leadingBadge.setImageResource(R.drawable.ic_badge_leading);
            }
            if ((boolean) achievements.get("premier")) {
                premierBadge.setImageResource(R.drawable.ic_badge_premier);
            }

            //fixing more info now
            String Temp = (String) sp_info.get("current_status");
            currentStatusTV.setText(currentStatusTV.getText() + Temp);
            Map<String, Object> self_rating = (Map<String, Object>) sp_info.get("self_rating");
            Temp = (String) self_rating.get("student_guided");
            currentlyMentoringTV.setText(currentlyMentoringTV.getText() + Temp);
            Temp = (String) sp_info.get("marital");
            maritalStatusTV.setText(maritalStatusTV.getText() + Temp);
            Temp = (String) sp_info.get("gender");
            genderTV.setText(genderTV.getText() + Temp);
            Temp = (String) sp_info.get("religion");
            religionTV.setText(religionTV.getText() + Temp);

            //fixing the agreement terms now
            //Timestamp timestamp = (Timestamp) selectedMentor.getTimestamp("creation");
            Map<String, Object> start_date = (Map<String, Object>) selectedMentor.get("start_date");
            Map<String, Object> start_time = (Map<String, Object>) selectedMentor.get("start_time");
            preferred_days = (Map<String, Object>) selectedMentor.get("preferred_days");
            startingYear = (Long) start_date.get("year");
            startingMonth = (Long) start_date.get("month");
            startingDay = (Long) start_date.get("day_of_month");
            Temp = (String) start_date.get("date_string");
            startingDateValue.setText(Temp);

            recordTimeSuggestedTime = (String) start_time.get("time_string");
            Long dbHour = (Long) start_time.get("hour_of_day");
            myTimePickerHourOfDay = dbHour.intValue();
            Long dbMinute = (Long) start_time.get("minute");
            myTimePickerMinite = dbMinute.intValue();


            Calendar calendar = Calendar.getInstance();
            Integer mMonth = calendar.get(Calendar.MONTH);
            Integer mDay = calendar.get(Calendar.DAY_OF_MONTH);

            LayerDrawable calenderCurDateLayDraw = (LayerDrawable) calenderCurDateImageView.getDrawable();
            DumeUtils.setTextOverDrawable(myThisActivity, calenderCurDateLayDraw, R.id.ic_badge, Color.WHITE, mDay.toString(), 2);

            /*long millSeconds = creation.getTime();
            long finishingMillis = 1000 * 60 * 60 * 24 * 30;
            finishingMillis = millSeconds+ finishingMillis ;
            calendar.setTimeInMillis(finishingMillis);*/
            calendar.setTime(creation);
            calendar.add(Calendar.MONTH, 1);
            String currentDateStr = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM).format(calendar.getTime());
            finishingDateValue.setText(currentDateStr);

            //fixing the rating now
            performanceCount.setText(self_rating.get("star_count").toString());
            LayerDrawable performanceLayDraw = (LayerDrawable) ratingPerformance.getDrawable();
            DumeUtils.setTextOverDrawable(context, performanceLayDraw, R.id.ic_badge, Color.BLACK, self_rating.get("star_rating").toString());
            ratingPerformance.setImageDrawable(performanceLayDraw);

            Map<String, Object> unread_records = (Map<String, Object>) sp_info.get("unread_records");
            Integer a_ratio_value = ((Integer.parseInt(unread_records.get("accepted_count").toString())
                    + Integer.parseInt(unread_records.get("completed_count").toString())
                    + Integer.parseInt(unread_records.get("current_count").toString())
                    + Integer.parseInt(unread_records.get("pending_count").toString()) + 1) /
                    (Integer.parseInt(unread_records.get("accepted_count").toString())
                            + Integer.parseInt(unread_records.get("completed_count").toString())
                            + Integer.parseInt(unread_records.get("current_count").toString())
                            + Integer.parseInt(unread_records.get("pending_count").toString())
                            + Integer.parseInt(unread_records.get("rejected_count").toString()) + 1)) * 100;
            aRatioCount.setText(a_ratio_value + " %");
            circleProgressbarARatio.setProgressWithAnimation(a_ratio_value, 600);

            Integer expertise_value = (Integer.parseInt(self_rating.get("l_expertise").toString()) /
                    Integer.parseInt(self_rating.get("l_expertise").toString()) + Integer.parseInt(self_rating.get("dl_expertise").toString())) * 100;
            expertiseCount.setText(expertise_value + " %");
            circleProgressbarExpertise.setProgressWithAnimation(expertise_value, 600);

            experienceCount.setText(self_rating.get("student_guided").toString());
            Integer experience_value = (Integer.parseInt(self_rating.get("l_experience").toString()) /
                    Integer.parseInt(self_rating.get("l_experience").toString()) + Integer.parseInt(self_rating.get("dl_experience").toString())) * 100;
            LayerDrawable experienceLayDraw = (LayerDrawable) ratingExperience.getDrawable();
            DumeUtils.setTextOverDrawable(context, experienceLayDraw, R.id.ic_badge, Color.BLACK, experience_value.toString());
            ratingExperience.setImageDrawable(experienceLayDraw);

            //now the other rating
            ArrayList<BarData> dataList = new ArrayList<>();

            Float comm_value = (Float.parseFloat(self_rating.get("l_communication").toString()) /
                    Float.parseFloat(self_rating.get("l_communication").toString()) + Float.parseFloat(self_rating.get("dl_communication").toString())) * 10;
            Float comm_text = comm_value * 10;
            BarData data = new BarData("Comm.", comm_value, comm_text.toString().substring(0, comm_text.toString().length() - 2) + " %");
            dataList.add(data);

            Float beha_value = (Float.parseFloat(self_rating.get("l_behaviour").toString()) /
                    Float.parseFloat(self_rating.get("l_behaviour").toString()) + Float.parseFloat(self_rating.get("dl_behaviour").toString())) * 10;
            Float baha_text = beha_value * 10;
            data = new BarData("Behaviour", beha_value, baha_text.toString().substring(0, baha_text.toString().length() - 2) + " %");
            dataList.add(data);

            Map<String, Object> jizz = (Map<String, Object>) selectedMentor.get("jizz");
            if (getLast(jizz) != null) {
                String mainSsss = (String) getLast(jizz);
                splitMainSsss = mainSsss.split("\\s*(=>|,)\\s*");
            }
            Map<String, Object> likes = (Map<String, Object>) selectedMentor.get("likes");
            Map<String, Object> dislikes = (Map<String, Object>) selectedMentor.get("dislikes");
            for (String splited : splitMainSsss) {
                Float loop_value = (Float.parseFloat(likes.get(splited).toString()) /
                        Float.parseFloat(likes.get(splited).toString()) + Float.parseFloat(dislikes.get(splited).toString())) * 10;
                Float loop_text = (loop_value * 10);
                data = new BarData(splited, loop_value, loop_text.toString().substring(0, loop_text.toString().length() - 2) + " %");
                dataList.add(data);
            }
            mChart.setDataList(dataList);
            mChart.build();

            //now fixing the review data
            new DumeModel(getContext()).loadReview(selectedMentor.getId(), null, new TeacherContract.Model.Listener<List<ReviewHighlightData>>() {
                @Override
                public void onSuccess(List<ReviewHighlightData> list) {
                    lastReviewData = list.get(list.size() - 1);
                    reviewRecyAda.update(list);
                    //reviewRecyAda = new ReviewAdapter(context, list, true);
                    if (list.size() >= 10) {
                        loadMoreReviewBtn.setEnabled(true);
                        loadMoreReviewBtn.setVisibility(View.VISIBLE);
                    } else {
                        loadMoreReviewBtn.setEnabled(false);
                        loadMoreReviewBtn.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(String msg) {
                    loadMoreReviewBtn.setVisibility(View.GONE);
                    noDataBlockReview.setVisibility(View.VISIBLE);
                    if (msg.equals("No review")) {
                        return;
                    }
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });

            loadMoreReviewBtn.setOnClickListener(view -> {
                view.setEnabled(false);
                new DumeModel(context).loadReview(selectedMentor.getId(), lastReviewData.getDoc_id(), new TeacherContract.Model.Listener<List<ReviewHighlightData>>() {
                    @Override
                    public void onSuccess(List<ReviewHighlightData> list) {
                        lastReviewData = list.get(list.size() - 1);
                        reviewRecyAda.addMore(list);
                        if (list.size() >= 10) {
                            loadMoreReviewBtn.setEnabled(true);
                            loadMoreReviewBtn.setVisibility(View.VISIBLE);
                        } else {
                            loadMoreReviewBtn.setEnabled(false);
                            loadMoreReviewBtn.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        view.setEnabled(true);
                        if (msg.equals("No review")) {
                            loadMoreReviewBtn.setVisibility(View.GONE);
                            return;
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
            });

            Log.e(TAG, "onMentorSelect: " + "method is running ");

            //setting the student info here
            Float stu_comm_value = ((Float.parseFloat((String) shMap.get("l_communication"))) /
                    (Float.parseFloat((String) shMap.get("l_communication")) + Float.parseFloat((String) shMap.get("dl_communication")))) * 100;
            stuComTV.setText(String.format("%s%s %%", stuComTV.getText(), stu_comm_value.toString().substring(0, stu_comm_value.toString().length() - 2)));

            Float stu_beha_value = ((Float.parseFloat((String) shMap.get("l_behaviour"))) /
                    (Float.parseFloat((String) shMap.get("l_behaviour")) + Float.parseFloat((String) shMap.get("dl_behaviour")))) * 100;
            stuBehaTV.setText(String.format("%s%s %%", stuBehaTV.getText(), stu_beha_value.toString().substring(0, stu_beha_value.toString().length() - 2)));

            String stuTemp = (String) forMap.get("request_gender");
            stuGenderTV.setText(String.format("%s%s", stuGenderTV.getText(), stuTemp));
            stuTemp = (String) forMap.get("request_cs");
            stuCurrentStatusTV.setText(String.format("%s%s", stuCurrentStatusTV.getText(), stuTemp));
            stuTemp = (String) forMap.get("request_pr");
            stuPreviousResultTV.setText(String.format("%s%s", stuPreviousResultTV.getText(), stuTemp));
        }

        public void loadQualificationData(Map<String, Object> sp_info) {
            if (sp_info != null) {
                List<Academic> academicList = new ArrayList<>();
                Map<String, Map<String, Object>> academicMap = (Map<String, Map<String, Object>>) sp_info.get("academic");
                if (academicMap != null && academicMap.size() > 0) {
                    for (Map.Entry<String, Map<String, Object>> entry : academicMap.entrySet()) {
                        String level = (String) entry.getValue().get("level");
                        String institution = (String) entry.getValue().get("institution");
                        String degree = (String) entry.getValue().get("degree");
                        String from_year = (String) entry.getValue().get("from_year");
                        String to_year = (String) entry.getValue().get("to_year");
                        String result = (String) entry.getValue().get("result");
                        Academic academic = new Academic(level, institution, degree, from_year, to_year, result);
                        academicList.add(academic);
                    }
                }
                qualificaitonRecyAda.update(academicList);
            }
        }

        public void configFragmentBtnClick() {
            //setting the animation for the more info btn
            stuMoreInfoBtn.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (visible) {
                                        stuMoreInfoHidable.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(stuMoreInfoHost, set);
                    //onlyRatingContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    stuMoreInfoBtn.setEnabled(false);
                    if (visible) {
                        stuMoreInfoHidable.setVisibility(View.INVISIBLE);
                    } else {
                        stuMoreInfoHidable.setVisibility(View.VISIBLE);
                    }
                    Drawable[] compoundDrawables = stuMoreInfoBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    stuMoreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                } else {
                                    stuMoreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                }
                                stuMoreInfoBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            stuMoreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            stuMoreInfoBtn.setEnabled(true);
                        } else {
                            stuMoreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            stuMoreInfoBtn.setEnabled(true);
                        }
                    }
                }

            });


            //setting the animation for the btn
            showAdditionalRatingBtn.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new FastOutLinearInInterpolator() : new LinearOutSlowInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (visible) {
                                        onlyRatingContainer.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(ratingHostVertical, set);
                    showAdditionalRatingBtn.setEnabled(false);
                    if (visible) {
                        onlyRatingContainer.setVisibility(View.INVISIBLE);
                        //showAdditionalRatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                    } else {
                        onlyRatingContainer.setVisibility(View.VISIBLE);
                        //showAdditionalRatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                    }
                    Drawable[] compoundDrawables = showAdditionalRatingBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    showAdditionalRatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                } else {
                                    showAdditionalRatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                }
                                showAdditionalRatingBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            showAdditionalRatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            showAdditionalRatingBtn.setEnabled(true);
                        } else {
                            showAdditionalRatingBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            showAdditionalRatingBtn.setEnabled(true);
                        }
                    }
                }
            });

            //setting the animation for the more info btn
            moreInfoBtn.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (visible) {
                                        moreInfoHidable.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(moreInfoHost, set);
                    //onlyRatingContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    moreInfoBtn.setEnabled(false);
                    if (visible) {
                        moreInfoHidable.setVisibility(View.INVISIBLE);
                    } else {
                        moreInfoHidable.setVisibility(View.VISIBLE);
                    }
                    Drawable[] compoundDrawables = moreInfoBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    moreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                } else {
                                    moreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                }
                                moreInfoBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            moreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            moreInfoBtn.setEnabled(true);
                        } else {
                            moreInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            moreInfoBtn.setEnabled(true);
                        }
                    }
                }

            });

            //setting the animation for the btn
            reviewInfoBtn.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .setDuration(600)
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (visible) {
                                        reviewHidable.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(reviewHost, set);
                    reviewInfoBtn.setEnabled(false);
                    if (visible) {
                        reviewHidable.setVisibility(View.INVISIBLE);
                    } else {
                        reviewHidable.setVisibility(View.VISIBLE);
                    }
                    Drawable[] compoundDrawables = reviewInfoBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    reviewInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                } else {
                                    reviewInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                }
                                reviewInfoBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            reviewInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            reviewInfoBtn.setEnabled(true);
                        } else {
                            reviewInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            reviewInfoBtn.setEnabled(true);
                        }
                    }
                }
            });

            //setting the animation for the agreement btn
            agreementInfoBtn.setOnClickListener(new VisibleToggleClickListener() {
                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (visible) {
                                        agreementHideable.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(agreementHostLayout, set);
                    agreementInfoBtn.setEnabled(false);
                    //onlyRatingContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    if (visible) {
                        agreementHideable.setVisibility(View.INVISIBLE);
                    } else {
                        agreementHideable.setVisibility(View.VISIBLE);
                    }
                    Drawable[] compoundDrawables = agreementInfoBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    agreementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                } else {
                                    agreementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                }
                                agreementInfoBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            agreementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            agreementInfoBtn.setEnabled(true);
                        } else {
                            agreementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            agreementInfoBtn.setEnabled(true);
                        }
                    }
                }
            });

            //setting the animation for the achievement btn
            achievementInfoBtn.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (visible) {
                                        achievementHidable.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(achievementHost, set);
                    achievementInfoBtn.setEnabled(false);
                    if (visible) {
                        achievementHidable.setVisibility(View.INVISIBLE);
                    } else {
                        achievementHidable.setVisibility(View.VISIBLE);
                    }
                    Drawable[] compoundDrawables = achievementInfoBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    achievementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                } else {
                                    achievementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                }
                                achievementInfoBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            achievementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            achievementInfoBtn.setEnabled(true);
                        } else {
                            achievementInfoBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            achievementInfoBtn.setEnabled(true);
                        }
                    }
                }
            });

            //location one handler
            locationShowBtn.setOnClickListener(new VisibleToggleClickListener() {

                @SuppressLint("CheckResult")
                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionSet set = new TransitionSet()
                            .addTransition(new Fade())
                            .addTransition(new Slide(Gravity.TOP))
                            .setInterpolator(visible ? new LinearOutSlowInInterpolator() : new FastOutLinearInInterpolator())
                            .addListener(new Transition.TransitionListener() {
                                @Override
                                public void onTransitionStart(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionEnd(@NonNull Transition transition) {
                                    if (visible) {
                                        locationHideable.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onTransitionCancel(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionPause(@NonNull Transition transition) {

                                }

                                @Override
                                public void onTransitionResume(@NonNull Transition transition) {

                                }
                            });
                    TransitionManager.beginDelayedTransition(locationHostLayout, set);
                    locationShowBtn.setEnabled(false);
                    if (visible) {
                        locationHideable.setVisibility(View.INVISIBLE);
                    } else {
                        locationHideable.setVisibility(View.VISIBLE);
                    }
                    Drawable[] compoundDrawables = locationShowBtn.getCompoundDrawables();
                    Drawable d = compoundDrawables[3];

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (d instanceof Animatable) {
                            ((Animatable) d).start();
                        }
                        ((Animatable2) d).registerAnimationCallback(new Animatable2.AnimationCallback() {
                            public void onAnimationEnd(Drawable drawable) {
                                //Do something
                                if (visible) {
                                    locationShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                                } else {
                                    locationShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                                }
                                locationShowBtn.setEnabled(true);
                            }
                        });
                    } else {
                        if (visible) {
                            locationShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_down_arrow_small));
                            locationShowBtn.setEnabled(true);
                        } else {
                            locationShowBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.ic_up_arrow_small));
                            locationShowBtn.setEnabled(true);
                        }
                    }
                }
            });
        }

        public ArrayList<CalendarDay> getSelectedDates() {
            final ArrayList<CalendarDay> dates = new ArrayList<>();
            final CalendarDay day = CalendarDay.from(startingYear.intValue(), startingMonth.intValue() + 1, startingDay.intValue());
            LocalDate temp = day.getDate();
            List<Long> selectedDaysInt = (List<Long>) preferred_days.get("selectedDaysInt");
            Double daysPerMonth = selectedDaysInt.size() * 4.4285;
            Integer count = 0;
            while (count < daysPerMonth.intValue()) {
                for (int i = 0; i < selectedDaysInt.size(); i++) {
                    final CalendarDay thisDay = CalendarDay.from(temp);
                    dates.add(thisDay);
                    temp = calcNextDay(temp, returesDaysofWeek(selectedDaysInt.get(i).intValue()));
                    count++;
                }
            }
            return dates;
        }

        private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

            @Override
            protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final ArrayList<CalendarDay> dates = new ArrayList<>();
                final CalendarDay day = CalendarDay.from(startingYear.intValue(), startingMonth.intValue() + 1, startingDay.intValue());
                LocalDate temp = day.getDate();
                List<Long> selectedDaysInt = (List<Long>) preferred_days.get("selectedDaysInt");
                Double daysPerMonth = selectedDaysInt.size() * 4.4285;
                Integer count = 0;
                while (count < daysPerMonth.intValue()) {
                    for (int i = 0; i < selectedDaysInt.size(); i++) {
                        final CalendarDay thisDay = CalendarDay.from(temp);
                        dates.add(thisDay);
                        temp = calcNextDay(temp, returesDaysofWeek(selectedDaysInt.get(i).intValue()));
                        count++;
                    }
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

        private LocalDate calcNextDay(LocalDate d, DayOfWeek nextDay) {
            return d.with(TemporalAdjusters.next(nextDay));
        }

        private DayOfWeek returesDaysofWeek(int weekDays) {
            switch (weekDays) {
                case 1:
                    return DayOfWeek.SUNDAY;
                case 2:
                    return DayOfWeek.MONDAY;
                case 3:
                    return DayOfWeek.TUESDAY;
                case 4:
                    return DayOfWeek.WEDNESDAY;
                case 5:
                    return DayOfWeek.THURSDAY;
                case 6:
                    return DayOfWeek.FRIDAY;
                case 7:
                    return DayOfWeek.SATURDAY;
                default:
                    return DayOfWeek.SUNDAY;

            }
        }

        //testing custom marker code here
        private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

            mMarkerImageView.setImageBitmap(getRoundedCornerBitmap(bitmap, (int) (28 * (getResources().getDisplayMetrics().density))));
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.buildDrawingCache();
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
            Drawable drawable = view.getBackground();
            if (drawable != null)
                drawable.draw(canvas);
            view.draw(canvas);
            return returnedBitmap;

        }

        private void addCustomMarkerFromURL(GoogleMap mMap, String url, LatLng lattitudeLongitude, String gender) {
            if (mMap == null) {
                return;
            }
            if (url != null && !url.equals("")) {
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .apply(new RequestOptions().override((int) (28 * (getResources().getDisplayMetrics().density)), (int) (28 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                mMap.addMarker(new MarkerOptions().position(lattitudeLongitude)
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, resource))));
                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lattitudeLongitude, 15f));
                            }
                        });
            } else {
                if (gender.equals("Male") || gender.equals("")) {
                    defaultUrl = SearchDataStore.DEFAULTMALEAVATER;
                } else {
                    defaultUrl = SearchDataStore.DEFAULTFEMALEAVATER;
                }
                Glide.with(context)
                        .asBitmap()
                        .load(defaultUrl)
                        .apply(new RequestOptions().override((int) (28 * (getResources().getDisplayMetrics().density)), (int) (28 * (getResources().getDisplayMetrics().density))).centerCrop().placeholder(R.drawable.alias_profile_icon))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                mMap.addMarker(new MarkerOptions().position(lattitudeLongitude)
                                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, resource))));
                            }
                        });
            }

        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<DocumentSnapshot> recordDataCurrent;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            recordDataCurrent = new ArrayList<>();
            recordDataCurrent = DumeUtils.filterList(Google.getInstance().getRecords(), "Current");
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position, recordDataCurrent);
        }

        @Override
        public int getCount() {
            Log.e("foo", "accepted: " + recordDataCurrent.size());
            return recordDataCurrent.size();
        }
    }
}

/* do {
temp = temp.plusDays(1);
//final DayOfWeek dayOfWeek = temp.getDayOfWeek();
} while (temp.getDayOfWeek() != DayOfWeek.MONDAY );*/


/*if (chk_monday.isChecked()) {
                        forday(2);
                    } else if (chk_tuesday.isChecked()) {
                        forday(3);
                    } else if (chk_wednesday.isChecked()) {
                        forday(4);
                    } else if (chk_thursday.isChecked()) {
                        forday(5);
                    } else if (chk_friday.isChecked()) {
                        forday(6);
                    } else if (chk_sat.isChecked()) {
                        forday(7);
                    } else if (chk_sunday.isChecked()) {
                        forday(1);
                    }

public void forday(int week) {

        calSet.set(Calendar.DAY_OF_WEEK, week);
        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, minuts);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calSet.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);
    }*/