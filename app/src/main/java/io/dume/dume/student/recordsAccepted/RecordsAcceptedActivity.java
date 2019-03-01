package io.dume.dume.student.recordsAccepted;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import carbon.widget.ImageView;
import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.QualificationAdapter;
import io.dume.dume.student.common.QualificationData;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.student.recordsPending.RecordsPendingActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Academic;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.VisibleToggleClickListener;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static io.dume.dume.util.DumeUtils.getLast;
import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmap;

public class RecordsAcceptedActivity extends CustomStuAppCompatActivity implements RecordsAcceptedContract.View {

    private RecordsAcceptedContract.Presenter mPresenter;
    private static final int fromFlag = 22;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;
    private String retriveAction = null;
    private RecordsAcceptedModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu12_activity_records_accepted);
        setActivityContext(this, fromFlag);
        findLoadView();
        mModel = new RecordsAcceptedModel(this);
        mPresenter = new RecordsAcceptedPresenter(this, mModel);
        mPresenter.recordsAcceptedEnqueue();
        DumeUtils.configureAppbar(this, "Accepted Requests");
        if (getIntent().getAction() != null) {
            retriveAction = getIntent().getAction();
        }
    }

    @Override
    public void findView() {
        pager = (ViewPager) findViewById(R.id.accepted_page_container);


    }

    @Override
    public void initRecordsAccepted() {

    }

    @Override
    public void configRecordsAccepted() {

        myPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);
    }


    public void onRecordsAcceptedViewClicked(View view) {
        mPresenter.onRecordsAcceptedIntracted(view);
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
        private RecordsAcceptedActivity myThisActivity;
        private RecyclerView qualificationRecyView;
        private QualificationAdapter qualificaitonRecyAda;
        private RecyclerView reviewRecyView;
        private ChartProgressBar mChart;
        private ImageView ratingPerformance;
        private ImageView ratingExperience;

        private DocumentSnapshot selectedMentor;
        private Button salaryBtn;
        private ImageView joinedBadge;
        private ImageView inauguralBadge;
        private ImageView leadingBadge;
        private ImageView premierBadge;
        private TextView currentStatusTV;
        private TextView currentlyMentoringTV;
        private TextView maritalStatusTV;
        private TextView religionTV;
        private TextView genderTV;
        private TextView timeTV;
        private TextView dateTV;
        private TextView preferredDayTV;
        private TextView daysPerWeekTV;
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
        private ImageView studentDisplayPic;
        private ImageView mentorDisplayPic;
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
        private Button getDirectionBtn;
        private carbon.widget.ImageView mMarkerImageView;
        private View mCustomMarkerView;
        private BottomSheetDialog mBottomSheetReject;
        private View sheetViewReject;
        private TextView rejectMainText;
        private TextView rejectSubText;
        private Button rejectYesBtn;
        private Button rejectNoBtn;
        private Button acceptContactBtn;
        private Button cancelRequestBtn;
        private View divider;
        private BottomSheetDialog mBottomSheetContactDialog;
        private View contactSheetView;
        private TextView contactMainText;
        private TextView contactSubText;
        private Button callBtn;
        private Button offlineMsgBtn;
        private Button onlineMsgBtn;


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, DocumentSnapshot record) {
            PlaceholderFragment.record = record;
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
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myThisActivity = (RecordsAcceptedActivity) getActivity();
            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.stu12_viewpager_layout_accepted, container, false);

            mCustomMarkerView = ((LayoutInflater) Objects.requireNonNull(context.getSystemService(LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_view, null);
            mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);
            getDirectionBtn = rootView.findViewById(R.id.get_direction_btn);

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

            timeTV = rootView.findViewById(R.id.textview_starting_time);
            dateTV = rootView.findViewById(R.id.textview_starting_date);
            preferredDayTV = rootView.findViewById(R.id.textview_preferred_day);
            daysPerWeekTV = rootView.findViewById(R.id.textview_days_week);

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

            acceptContactBtn = rootView.findViewById(R.id.accepted_contact_btn);
            cancelRequestBtn = rootView.findViewById(R.id.pendding_cancel_btn);
            divider = rootView.findViewById(R.id.divider3);

            //setting the qualification recycler view
            List<Academic> academicList = new ArrayList<>();
            qualificaitonRecyAda = new QualificationAdapter(myThisActivity, academicList);
            qualificationRecyView.setAdapter(qualificaitonRecyAda);
            qualificationRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));

            //setting the review recycler view
            List<ReviewHighlightData> reviewData = new ArrayList<>();
            reviewRecyAda = new ReviewAdapter(myThisActivity, reviewData);
            reviewRecyView.setAdapter(reviewRecyAda);
            reviewRecyView.setLayoutManager(new LinearLayoutManager(myThisActivity));
            onMentorSelect(record);
            configFragmentBtnClick();
            toggleStatus();
            return rootView;
        }

        public void onMentorSelect(DocumentSnapshot selectedMentor) {
            this.selectedMentor = selectedMentor;
            Map<String, Object> sp_info = (Map<String, Object>) selectedMentor.get("sp_info");

            //TODO fuck holder data
            String mentorName;
            String studentName;
            String salaryInDemand;
            String subjectExchange;
            String date;
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
            salaryInDemand = String.valueOf((Double) documentData.get("salary"));
            sGender = (String) forMap.get("request_gender");
            mGender = (String) spMap.get("gender");
            subjectExchange = DumeUtils.getLast((Map<String, Object>) documentData.get("jizz"));
            Date creation = (Date) documentData.get("creation");
            date = creation.toString();
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

            //fill up all info of the mentor
            NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
            Double salary = (Double) selectedMentor.get("salary");
            String format1 = currencyInstance.format(salary);
            salaryBtn.setText("Salary : " + format1.substring(1, format1.length() - 3) + " BDT");
            loadQualificationData(sp_info);

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
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                        if (myThisActivity.retriveAction != null) {
                            switch (myThisActivity.retriveAction) {
                                case DumeUtils.STUDENT:
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

            Map<String, Object> start_time = (Map<String, Object>) selectedMentor.get("start_time");
            Map<String, Object> start_date = (Map<String, Object>) selectedMentor.get("start_date");
            Map<String, Object> preferred_days = (Map<String, Object>) selectedMentor.get("preferred_days");
            Temp = (String) start_time.get("time_string");
            timeTV.setText(timeTV.getText() + Temp);
            Temp = (String) start_date.get("date_string");
            dateTV.setText(dateTV.getText() + Temp);
            Temp = (String) preferred_days.get("selected_days");
            preferredDayTV.setText(preferredDayTV.getText() + Temp);
            Temp = preferred_days.get("days_per_week").toString();
            daysPerWeekTV.setText(daysPerWeekTV.getText() + Temp + " days");

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

            Float beha_value = (Float.parseFloat(self_rating.get("l_communication").toString()) /
                    Float.parseFloat(self_rating.get("l_communication").toString()) + Float.parseFloat(self_rating.get("dl_communication").toString())) * 10;
            Float baha_text = beha_value * 10;
            data = new BarData("Behaviour", beha_value, baha_text.toString().substring(0, baha_text.toString().length() - 2) + " %");
            dataList.add(data);

            Map<String, Object> jizz = (Map<String, Object>) selectedMentor.get("jizz");
            if (getLast(jizz) != null) {
                String mainSsss = (String) getLast(jizz);
                splitMainSsss = mainSsss.split("\\s*(=>|,|\\s)\\s*");
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

        public void toggleStatus() {

            //confirm bottom sheet
            Map<String, Object> documentData = record.getData();
            Map<String, Object> spMap = (Map<String, Object>) documentData.get("sp_info");
            Map<String, Object> forMap = (Map<String, Object>) documentData.get("for_whom");
            String mentorName = spMap.get("first_name") + " " + spMap.get("last_name");
            String studentName = (String) forMap.get("stu_name");

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
                        myThisActivity.showProgress();
                        acceptContactBtn.setEnabled(false);
                        cancelRequestBtn.setEnabled(false);
                        myThisActivity.mModel.changeRecordStatus(record.getId(), "Rejected", new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void list) {
                                myThisActivity.hideProgress();
                                Toast.makeText(myThisActivity, "Status Changed To Rejected", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String msg) {
                                acceptContactBtn.setEnabled(true);
                                cancelRequestBtn.setEnabled(true);
                                myThisActivity.showProgress();
                                Toast.makeText(myThisActivity, msg, Toast.LENGTH_SHORT).show();
                            }
                        });
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

                    }
                });

                offlineMsgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                onlineMsgBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }

            switch (myThisActivity.retriveAction) {
                case DumeUtils.STUDENT:
                    cancelRequestBtn.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    contactMainText.setText("Contact with " + mentorName);
                    break;
                case DumeUtils.TEACHER:
                    contactMainText.setText("Contact with " + studentName);
                    break;
                case DumeUtils.BOOTCAMP:
                    break;
                default:
                    break;
            }


            acceptContactBtn.setOnClickListener(view -> {
                mBottomSheetContactDialog.show();
            });
            cancelRequestBtn.setOnClickListener(view -> {
                mBottomSheetReject.show();
            });

        }

        public void configFragmentBtnClick() {
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
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    if (d instanceof Animatable) {
                        ((Animatable) d).start();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    }
                }
            });
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
                    ;
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

        private List<DocumentSnapshot> recordDataAccepted;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            recordDataAccepted = new ArrayList<>();
            recordDataAccepted = Google.getInstance().getRecords();
            recordDataAccepted = DumeUtils.filterList(Google.getInstance().getRecords(), "Accepted");
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position, recordDataAccepted.get(position));
        }

        @Override
        public int getCount() {
            Log.e("foo", "accepted: " + recordDataAccepted.size());
            return recordDataAccepted.size();
        }
    }
}
