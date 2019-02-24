package io.dume.dume.student.searchResult;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadViewTwo;
import io.dume.dume.library.RouteOverlayView;
import io.dume.dume.library.TrailSupportMapFragment;
import io.dume.dume.model.DumeModel;
import io.dume.dume.service.MyNotification;
import io.dume.dume.student.common.QualificationAdapter;
import io.dume.dume.student.common.QualificationData;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.searchResultTabview.SearchResultTabviewActivity;
import io.dume.dume.teacher.adapters.AcademicAdapter;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.model.KeyValueModel;
import io.dume.dume.teacher.pojo.Academic;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.OnSwipeTouchListener;
import io.dume.dume.util.VisibleToggleClickListener;

import static io.dume.dume.util.ImageHelper.getRoundedCornerBitmap;

public class SearchResultActivity extends CusStuAppComMapActivity implements OnMapReadyCallback,
        SearchResultContract.View, MyGpsLocationChangeListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        RoutingListener {

    private GoogleMap mMap;
    private SearchResultContract.Presenter mPresenter;
    private static final int fromFlag = 6;
    private Toolbar toolbar;
    private TrailSupportMapFragment mapFragment;
    private FrameLayout viewMusk;
    private Toolbar secondaryToolbar;
    private AppBarLayout defaultAppbarLayout;
    private AppBarLayout secondaryAppbarLayout;
    private LinearLayout llBottomSheet;
    private CoordinatorLayout coordinatorLayout;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout verticalTextViewContainer;
    private ImageView mentorDisplayPic;
    private LinearLayout changingOrientationContainer;
    private LinearLayout.LayoutParams changingOrientationParams;
    private Boolean dragFirst = true;
    private TextView ageText;
    private TextView mentorNameText;
    private ImageView ratingPerformance;
    private ImageView ratingExperience;
    private CircleProgressbar circleProgressbarExpertise;
    private CircleProgressbar circleProgressbarARatio;
    private final static int ANIMATIONDURATION = 2500;
    private ChartProgressBar mChart;
    private LinearLayout ratingHostVertical;
    private Button showAdditionalRatingBtn;
    private LinearLayout onlyRatingContainer;
    private RecyclerView qualificationRecyView;
    private RecyclerView reviewRecyView;
    private QualificationAdapter qualificaitonRecyAda;
    private ReviewAdapter reviewRecyAda;
    private Button moreInfoBtn;
    private Button reviewInfoBtn;
    private LinearLayout reviewHidable;
    private LinearLayout reviewHost;
    private LinearLayout moreInfoHidable;
    private LinearLayout moreInfoHost;
    private LinearLayout achievementHidable;
    private LinearLayout achievementHost;
    private Button achievementInfoBtn;
    private Button swipeRight;
    private Button swipeLeft;
    private LinearLayout basicInfo;
    private LinearLayout basicInfoInsider;
    private LinearLayout.LayoutParams basicInfoInsiderLayoutParams;
    private OnSwipeTouchListener onSwipeTouchListener;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;
    private LatLng mDummyLatLng;
    double latitude = Double.parseDouble("23.788632");
    double longitude = Double.parseDouble("90.419437");
    double latitude1 = Double.parseDouble("23.847440");
    double longitude1 = Double.parseDouble("90.415854");
    private IconGenerator iconFactory;
    private FloatingActionButton fab;
    private LatLng mDummyLatLngOne;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.black, R.color.badge_red, R.color.badge_green, R.color.blue, R.color.badge_yellow};
    private CollapsingToolbarLayout secondaryCollapsingToolbarLayout;
    private ActionBar supportActionBarMain;
    private ActionBar supportActionBarSecond;
    private int optionMenu = R.menu.menu_search_result;
    private LinearLayout agreementHostLayout;
    private Button agreementInfoBtn;
    private LinearLayout agreementHideable;
    /*private LinearLayout locationHostLayout;
    private Button locationInfoBtn;
    private LinearLayout locationHideable;*/
    private NestedScrollView bottomSheetNSV;
    private HorizontalLoadViewTwo loadViewBS;
    private List<LatLng> route;
    private Marker mMarkerA;
    private String defaultUrl;
    private String TAG = "foo";
    private DocumentSnapshot selectedMentor;
    private Button requestBTN;
    private BottomSheetDialog mMakeRequestBSD;
    private View cancelsheetRootView;
    private BottomSheetDialog mBackBSD;
    private View backsheetRootView;
    private TextView confirmMainText;
    private TextView confirmSubText;
    private Button comfirmYesBtn;
    private Button confirmNoBtn;
    private SearchResultModel mModel;
    private TextView backMainText;
    private TextView backSubText;
    private Button backYesBtn;
    private Button backNoBtn;
    private String retrivedAction;
    private boolean isConfirmedOrCanceled = false;
    private Map<String, Object> pushNotiData;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu6_activity_search_result);
        setActivityContextMap(this, fromFlag);
        findLoadView();
        mModel = new SearchResultModel(this);
        mPresenter = new SearchResultPresenter(this, mModel);
        mPresenter.searchResultEnqueue();

        mapFragment = (TrailSupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //setting the qualification recycler view
        //qualificaitonRecyAda = new QualificationAdapter(this, getQualificationData());
        List<Academic> academicList = new ArrayList<>();
        qualificaitonRecyAda = new QualificationAdapter(this, academicList);
        qualificationRecyView.setAdapter(qualificaitonRecyAda);
        qualificationRecyView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //setting the review recycler view
        List<ReviewHighlightData> reviewData = new ArrayList<>();
        reviewRecyAda = new ReviewAdapter(this, reviewData);
        reviewRecyView.setAdapter(reviewRecyAda);
        reviewRecyView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public DocumentSnapshot getSelectedMentor() {
        return selectedMentor;
    }

    @Override
    public void findView() {
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.my_main_container);
        llBottomSheet = findViewById(R.id.searchBottomSheet);
        secondaryAppbarLayout = findViewById(R.id.secondary_Appbar);
        secondaryCollapsingToolbarLayout = findViewById(R.id.secondary_collapsing_toolbar);
        defaultAppbarLayout = findViewById(R.id.my_appbarLayout);
        secondaryToolbar = findViewById(R.id.secondary_toolbar);
        viewMusk = findViewById(R.id.view_musk);
        verticalTextViewContainer = findViewById(R.id.vertical_textview_container);
        mentorDisplayPic = findViewById(R.id.mentor_dp);
        changingOrientationContainer = findViewById(R.id.changing_orientation_layout);
        changingOrientationParams = (LinearLayout.LayoutParams) changingOrientationContainer.getLayoutParams();
        ageText = findViewById(R.id.text_two);
        mentorNameText = findViewById(R.id.text_one);

        mChart = (ChartProgressBar) findViewById(R.id.myChartProgressBar);
        ratingHostVertical = findViewById(R.id.rating_host_linearlayout);
        showAdditionalRatingBtn = findViewById(R.id.show_additional_rating_btn);
        onlyRatingContainer = findViewById(R.id.rating_layout_vertical);
        qualificationRecyView = findViewById(R.id.recycler_view_qualifications);
        reviewRecyView = findViewById(R.id.recycler_view_reviews);
        requestBTN = findViewById(R.id.requestBTN);
        moreInfoBtn = findViewById(R.id.show_more_info_btn);
        moreInfoHost = findViewById(R.id.more_info_host_linearlayout);
        moreInfoHidable = findViewById(R.id.more_info_layout_vertical);

        reviewHost = findViewById(R.id.review_host_linearlayout);
        reviewHidable = findViewById(R.id.review_layout_vertical);
        reviewInfoBtn = findViewById(R.id.review_info_btn);

        achievementInfoBtn = findViewById(R.id.show_achievement_btn);
        achievementHost = findViewById(R.id.achievement_host_linearlayout);
        achievementHidable = findViewById(R.id.achievement_layout_vertical);

        basicInfo = findViewById(R.id.basic_info);
        basicInfoInsider = findViewById(R.id.basic_info_insider_layout);
        basicInfoInsiderLayoutParams = (LinearLayout.LayoutParams) basicInfoInsider.getLayoutParams();
        swipeLeft = findViewById(R.id.swipe_left);
        swipeRight = findViewById(R.id.swipe_right);
        mCustomMarkerView = ((LayoutInflater) Objects.requireNonNull(getSystemService(LAYOUT_INFLATER_SERVICE))).inflate(R.layout.custom_marker_view, null);
        mMarkerImageView = mCustomMarkerView.findViewById(R.id.profile_image);
        fab = findViewById(R.id.fab);
        mDummyLatLng = new LatLng(latitude, longitude);
        mDummyLatLngOne = new LatLng(latitude1, longitude1);
        //iconFactory instance
        iconFactory = new IconGenerator(this);

        agreementHostLayout = findViewById(R.id.agreement_term_host_linearlayout);
        agreementInfoBtn = findViewById(R.id.show_agreement_terms_btn);
        agreementHideable = findViewById(R.id.agreement_term_layout_vertical);

        //locationHostLayout = findViewById(R.id.location_host_linearlayout);
        //locationInfoBtn = findViewById(R.id.show_location_btn);
        //locationHideable = findViewById(R.id.location_layout_vertical);
        bottomSheetNSV = findViewById(R.id.bottom_sheet_scroll_view);
        loadViewBS = findViewById(R.id.loadViewTwo);

        salaryBtn = findViewById(R.id.show_salary_btn);
        joinedBadge = findViewById(R.id.achievement_joined_image);
        inauguralBadge = findViewById(R.id.achievement_inaugural_image);
        leadingBadge = findViewById(R.id.achievement_leading_image);
        premierBadge = findViewById(R.id.achievement_premier_image);

        currentStatusTV = findViewById(R.id.textview_current_status);
        currentlyMentoringTV = findViewById(R.id.textview_currently_mentoring);
        maritalStatusTV = findViewById(R.id.textview_marital_status);
        religionTV = findViewById(R.id.textview_religion);
        genderTV = findViewById(R.id.textview_gender);

        timeTV = findViewById(R.id.textview_starting_time);
        dateTV = findViewById(R.id.textview_starting_date);
        preferredDayTV = findViewById(R.id.textview_preferred_day);
        daysPerWeekTV = findViewById(R.id.textview_days_week);

        performanceCount = findViewById(R.id.txtStatus);
        ratingPerformance = findViewById(R.id.main_rating_performance);
        ratingExperience = findViewById(R.id.main_rating_experience);
        experienceCount = findViewById(R.id.txtStatus_experience);
        circleProgressbarARatio = (CircleProgressbar) findViewById(R.id.rating_main_accept_ratio);
        aRatioCount = findViewById(R.id.txtStatus_accept_ratio);
        circleProgressbarExpertise = (CircleProgressbar) findViewById(R.id.rating_main_professionalism);
        expertiseCount = findViewById(R.id.txtStatus_professionalism);

        loadMoreReviewBtn = findViewById(R.id.load_more_review_btn);
        noDataBlockReview = findViewById(R.id.no_data_block);

        route = new ArrayList<>();
    }

    @Override
    public void goHome() {
        final Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void initSearchResult() {
        retrivedAction = getIntent().getAction();
        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);
        //setting typeface for the secondary appbar layout
        secondaryCollapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsingToolbarLayout.setExpandedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        secondaryCollapsingToolbarLayout.setTitle("Details");
        Drawable drawableWhite = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_white_24dp);
        secondaryToolbar.setOverflowIcon(drawableWhite);
        settingStatusBarTransparent();
        setDarkStatusBarIcon();

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llBottomSheet.getLayoutParams().height = (llBottomSheet.getHeight() - secondaryAppbarLayout.getHeight() - (int) (8 * (getResources().getDisplayMetrics().density)));
                llBottomSheet.requestLayout();
                bottomSheetBehavior.onLayoutChild(coordinatorLayout, llBottomSheet, ViewCompat.LAYOUT_DIRECTION_LTR);
                ViewTreeObserver obs = llBottomSheet.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }
        });

        //init the confirm dialogue
        mMakeRequestBSD = new BottomSheetDialog(this);
        cancelsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mMakeRequestBSD.setContentView(cancelsheetRootView);
        confirmMainText = mMakeRequestBSD.findViewById(R.id.main_text);
        confirmSubText = mMakeRequestBSD.findViewById(R.id.sub_text);
        comfirmYesBtn = mMakeRequestBSD.findViewById(R.id.cancel_yes_btn);
        confirmNoBtn = mMakeRequestBSD.findViewById(R.id.cancel_no_btn);
        if (confirmMainText != null && confirmSubText != null && comfirmYesBtn != null && confirmNoBtn != null) {
            confirmMainText.setText("Confirm Request");
            confirmSubText.setText("By confirming request will be sent to ____...");
            comfirmYesBtn.setText("Yes, Confirm");
            confirmNoBtn.setText("No");

            comfirmYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    comfirmYesBtn.setEnabled(false);
                    comfirmYesBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
                    showProgressBS();
                    setConfirmedOrCanceled(true);
                    mMakeRequestBSD.dismiss();
                    DocumentSnapshot selectedMentor = getSelectedMentor();
                    Map<String, Object> recordsData = new HashMap<>();
                    Map<String, Object> skillMap = selectedMentor.getData();
                    String spUid = "T_" + skillMap.get("mentor_uid");

                    Map<String, Object> searchMap = searchDataStore.genRetMainMap();
                    String shUid = "S_" + searchDataStore.getUserUid();
                    if (skillMap == null || searchMap == null) {
                        return;
                    }
                    recordsData.putAll(searchMap);
                    recordsData.putAll(skillMap);
                    recordsData.put("skill_uid", selectedMentor.getId());
                    recordsData.put("record_status", SearchDataStore.STATUSPENDING);
                    recordsData.put("sp_uid", spUid);
                    recordsData.put("sh_uid", shUid);
                    recordsData.put("t_rate_status", "dialog");
                    recordsData.put("s_rate_status", "dialog");

                    List<String> participants = new ArrayList<>();
                    participants.add((String) skillMap.get("mentor_uid"));
                    participants.add((String) searchDataStore.getUserUid());
                    recordsData.put("participants", participants);
                    mModel.riseNewRecords(recordsData, new TeacherContract.Model.Listener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference list) {
                            goHome();
                            Log.w("foo", "onSuccess: " + list.toString());
                            hideProgressBS();
                            comfirmYesBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            //comfirmYesBtn.setEnabled(true);
                        }

                        @Override
                        public void onError(String msg) {
                            flush(msg);
                            comfirmYesBtn.setEnabled(true);
                            comfirmYesBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            hideProgressBS();
                        }
                    });
                }
            });

            confirmNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mMakeRequestBSD.dismiss();
                }
            });
        }

        pushNotiData = new HashMap<>();
        pushNotiData.put("uid", searchDataStore.getUserUid());
        pushNotiData.put("name", searchDataStore.getUserName() == null ? "" : searchDataStore.getUserName());
        pushNotiData.put("reason", "not_confirming");
        pushNotiData.put("token", MyNotification.getToken(this));
        //init the back dialog
        mBackBSD = new BottomSheetDialog(this);
        backsheetRootView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        mBackBSD.setContentView(backsheetRootView);
        backMainText = mBackBSD.findViewById(R.id.main_text);
        backSubText = mBackBSD.findViewById(R.id.sub_text);
        backYesBtn = mBackBSD.findViewById(R.id.cancel_yes_btn);
        backNoBtn = mBackBSD.findViewById(R.id.cancel_no_btn);
        if (backMainText != null && backSubText != null && backYesBtn != null && backNoBtn != null) {
            backMainText.setText("Going back !!");
            backSubText.setText("Going back without making request to any mentor...");
            if (retrivedAction != null) {
                switch (retrivedAction) {
                    case "from_HPA":
                        backYesBtn.setText("Yes, Homepage");
                        backNoBtn.setText("No");
                        break;
                    case "from_GPA":
                        backYesBtn.setText("Yes, Homepage");
                        backNoBtn.setText("Edit, Query");
                        break;
                }
            }
            backYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgressBS();
                    if (getIntent().getAction().equals("from_HPA")) {
                        backYesBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
                        backYesBtn.setEnabled(false);
                    } else if (getIntent().getAction().equals("from_GPA")) {
                        backYesBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
                        backYesBtn.setEnabled(false);
                        backNoBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
                        backNoBtn.setEnabled(false);
                    }
                    mBackBSD.dismiss();
                    mModel.riseNewPushNoti(pushNotiData, new TeacherContract.Model.Listener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference list) {
                            setConfirmedOrCanceled(true);
                            if (getIntent().getAction().equals("from_HPA")) {
                                searchDataStore.setFirstTime(false);
                                onBackPressed();
                            } else if (getIntent().getAction().equals("from_GPA")) {
                                Intent intent = new Intent(SearchResultActivity.this, HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            hideProgressBS();
                            comfirmYesBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            comfirmYesBtn.setEnabled(true);
                        }

                        @Override
                        public void onError(String msg) {
                            Log.e(TAG, "onError: " + msg);
                            if (getIntent().getAction().equals("from_HPA")) {
                                searchDataStore.setFirstTime(false);
                                onBackPressed();
                            } else if (getIntent().getAction().equals("from_GPA")) {
                                Intent intent = new Intent(SearchResultActivity.this, HomePageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            hideProgressBS();
                            comfirmYesBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                            comfirmYesBtn.setEnabled(true);
                        }
                    });

                }
            });

            backNoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getIntent().getAction().equals("from_HPA")) {
                        mBackBSD.dismiss();
                    } else if (getIntent().getAction().equals("from_GPA")) {
                        showProgressBS();
                        backNoBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
                        backNoBtn.setEnabled(false);
                        backYesBtn.setBackgroundColor(getResources().getColor(R.color.disable_color));
                        backYesBtn.setEnabled(false);
                        mBackBSD.dismiss();
                        mModel.riseNewPushNoti(pushNotiData, new TeacherContract.Model.Listener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference list) {
                                setConfirmedOrCanceled(true);
                                searchDataStore.setFirstTime(false);
                                onBackPressed();
                                hideProgressBS();
                                backYesBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                                backYesBtn.setEnabled(true);
                                backNoBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                                backNoBtn.setEnabled(true);
                            }

                            @Override
                            public void onError(String msg) {
                                Log.e(TAG, "onError: " + msg);
                                searchDataStore.setFirstTime(false);
                                onBackPressed();
                                hideProgressBS();
                                backYesBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                                backYesBtn.setEnabled(true);
                                backNoBtn.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                                backNoBtn.setEnabled(true);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isConfirmedOrCanceled()) {
            mModel.riseNewPushNoti(pushNotiData, new TeacherContract.Model.Listener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference list) {
                    setConfirmedOrCanceled(true);
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, "onError: " + msg);
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setConfirmedOrCanceled(false);
    }

    @Override
    public void configSearchResult() {
        TransitionSet setBasicInfo = new TransitionSet()
                .addTransition(new Fade())
                .addTransition(new Slide(Gravity.TOP))
                .setInterpolator(new LinearInterpolator());
        TransitionManager.beginDelayedTransition(basicInfo, setBasicInfo);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    dragFirst = true;
                    setDarkStatusBarIcon();
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppbarLayout.setVisibility(View.INVISIBLE);
                    defaultAppbarLayout.setVisibility(View.VISIBLE);
                    changingOrientationContainer.setOrientation(LinearLayout.HORIZONTAL);
                    changingOrientationParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    changingOrientationContainer.setLayoutParams(changingOrientationParams);
                    ageText.setGravity(Gravity.START);
                    mentorNameText.setGravity(Gravity.START);
                    verticalTextViewContainer.animate()
                            .translationYBy((float) (-0.0f * (getResources().getDisplayMetrics().density)))
                            .setDuration(60)
                            .start();
                    mentorDisplayPic.animate()
                            .translationYBy((float) (-6.0f * (getResources().getDisplayMetrics().density)))
                            .setDuration(60)
                            .start();
                    swipeLeft.setVisibility(View.GONE);
                    swipeRight.setVisibility(View.GONE);
                    basicInfoInsiderLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    basicInfoInsider.setLayoutParams(basicInfoInsiderLayoutParams);
                    //hack
                    setSupportActionBar(toolbar);
                    supportActionBarMain = getSupportActionBar();
                    if (supportActionBarMain != null) {
                        supportActionBarMain.setDisplayHomeAsUpEnabled(true);
                        supportActionBarMain.setDisplayShowHomeEnabled(true);
                        optionMenu = R.menu.menu_search_result;
                        invalidateOptionsMenu();
                    }
                    bottomSheetNSV.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.smoothScrollTo(0, 0);
                        }
                    });
                    bottomSheetNSV.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetNSV.fling(0);
                            bottomSheetNSV.scrollTo(0, 0);
                        }
                    }, 200L);
                    hideProgress();

                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);
                    swipeLeft.setVisibility(View.VISIBLE);
                    swipeRight.setVisibility(View.VISIBLE);
                    //hack
                    setSupportActionBar(secondaryToolbar);
                    supportActionBarSecond = getSupportActionBar();
                    if (supportActionBarSecond != null) {
                        supportActionBarSecond.setDisplayHomeAsUpEnabled(true);
                        supportActionBarSecond.setDisplayShowHomeEnabled(true);
                        optionMenu = R.menu.menu_only_help;
                        invalidateOptionsMenu();
                    }
                    //showProgress();
                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);
                    changingOrientationContainer.setOrientation(LinearLayout.VERTICAL);
                    changingOrientationParams.height = (int) (150 * (getResources().getDisplayMetrics().density));
                    changingOrientationContainer.setLayoutParams(changingOrientationParams);
                    ageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    mentorNameText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                    if (dragFirst) {
                        dragFirst = false;
                        verticalTextViewContainer.animate()
                                .translationYBy((float) (0.0f * (getResources().getDisplayMetrics().density)))
                                .setDuration(60)
                                .start();
                        mentorDisplayPic.animate()
                                .translationYBy((float) (6.0f * (getResources().getDisplayMetrics().density)))
                                .setDuration(60)
                                .start();
                    }

                    swipeLeft.setVisibility(View.VISIBLE);
                    swipeRight.setVisibility(View.VISIBLE);
                    basicInfoInsiderLayoutParams.height = (int) (100 * (getResources().getDisplayMetrics().density));
                    basicInfoInsider.setLayoutParams(basicInfoInsiderLayoutParams);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                llBottomSheet.animate().scaleX(1 + (slideOffset * 0.058f)).scaleY(1 + (slideOffset * 0.030f)).setDuration(0).start();
                viewMusk.animate().alpha(2 * slideOffset).setDuration(0).start();
                secondaryAppbarLayout.animate().alpha(slideOffset).scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
                mentorDisplayPic.animate()
                        .scaleX((float) (1 + (0.55 * slideOffset)))
                        .scaleY((float) (1 + (0.55 * slideOffset)))
                        .setDuration(0).start();
                verticalTextViewContainer.animate()
                        .scaleX((float) (1 + (0.22 * slideOffset)))
                        .scaleY((float) (1 + (0.22 * slideOffset)))
                        .setDuration(0).start();
                fab.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                if (COMPASSBTN != null) {
                    COMPASSBTN.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
                }
            }
        });

        //setting text over drawable
        LayerDrawable performanceLayDraw = (LayerDrawable) ratingPerformance.getDrawable();
        DumeUtils.setTextOverDrawable(this, performanceLayDraw, R.id.ic_badge, Color.BLACK, "4.89");
        //ratingPerformance.setImageDrawable(performanceLayDraw);

        LayerDrawable experienceLayDraw = (LayerDrawable) ratingExperience.getDrawable();
        DumeUtils.setTextOverDrawable(this, experienceLayDraw, R.id.ic_badge, Color.BLACK, "00");
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
                if (d instanceof Animatable2) {
                    ((Animatable2) d).start();
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
        });
    }

    @Override
    public void pushProfile(DocumentSnapshot singleSkill) {
        Map<String, Object> sp_info = (Map<String, Object>) singleSkill.get("sp_info");
        Map<String, Object> selfRating = (Map<String, Object>) sp_info.get("self_rating");
        String avatar = (String) sp_info.get("avatar");
        String gender = (String) sp_info.get("gender");
        GeoPoint location = singleSkill.getGeoPoint("location");
        String starRating = (String) selfRating.get("star_rating");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        route.add(latLng);
        final Marker marker = addCustomMarkerFromURL(avatar, latLng, gender, starRating);
    }

    public void onMentorSelect(DocumentSnapshot selectedMentor) {
        this.selectedMentor = selectedMentor;
        Map<String, Object> sp_info = (Map<String, Object>) selectedMentor.get("sp_info");
        final String name = String.format("%s %s", sp_info.get("first_name"), sp_info.get("last_name"));
        mentorNameText.setText(name);
        String birthDate = (String) sp_info.get("birth_date");
        if (birthDate == null || birthDate.equals("")) {
            ageText.setText("Age - unknown");
        } else {
            String[] splited = birthDate.split("\\s+");
            final String age = getAge(Integer.parseInt(splited[2]), Integer.parseInt(splited[1].replace(",", "")), splited[0]);
            ageText.setText(age + " year old");
        }
        GeoPoint location = selectedMentor.getGeoPoint("location");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        final String avatar = (String) sp_info.get("avatar");
        if (avatar != null && !avatar.equals("")) {
            Glide.with(getApplicationContext()).load(avatar).into(mentorDisplayPic);
        }
        List<LatLng> pathRoute = new ArrayList<>();
        pathRoute.add(searchDataStore.getAnchorPoint());
        pathRoute.add(latLng);
        mapFragment.setUpPath(pathRoute, mMap, RouteOverlayView.AnimType.ARC);
        loadQualificationData(sp_info);

        //fill up all info of the mentor TODO
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
        Double salary = (Double) selectedMentor.get("salary");
        String format1 = currencyInstance.format(salary);
        salaryBtn.setText("Salary : " + format1.substring(1, format1.length() - 3) + " BDT");
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
        Temp = (String) searchDataStore.getStartTime().get("time_string");
        timeTV.setText(timeTV.getText() + Temp);
        Temp = (String) searchDataStore.getStartDate().get("date_string");
        dateTV.setText(dateTV.getText() + Temp);
        Temp = (String) searchDataStore.getPreferredDays().get("selected_days");
        preferredDayTV.setText(preferredDayTV.getText() + Temp);
        Temp = searchDataStore.getPreferredDays().get("days_per_week").toString();
        daysPerWeekTV.setText(daysPerWeekTV.getText() + Temp + " days");

        //fixing the rating now
        performanceCount.setText(self_rating.get("star_count").toString());
        LayerDrawable performanceLayDraw = (LayerDrawable) ratingPerformance.getDrawable();
        DumeUtils.setTextOverDrawable(this, performanceLayDraw, R.id.ic_badge, Color.BLACK, self_rating.get("star_rating").toString());
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
        DumeUtils.setTextOverDrawable(this, experienceLayDraw, R.id.ic_badge, Color.BLACK, experience_value.toString());
        ratingExperience.setImageDrawable(experienceLayDraw);

        //now the other rating
        ArrayList<BarData> dataList = new ArrayList<>();

        Float comm_value = (Float.parseFloat(self_rating.get("l_communication").toString()) /
                Float.parseFloat(self_rating.get("l_communication").toString()) + Float.parseFloat(self_rating.get("dl_communication").toString())) * 10;
        Float comm_text = comm_value * 10;
        BarData data = new BarData("Comm.", comm_value, comm_text.toString().substring(0,comm_text.toString().length()-2)+ " %");
        dataList.add(data);

        Float beha_value = (Float.parseFloat(self_rating.get("l_communication").toString()) /
                Float.parseFloat(self_rating.get("l_communication").toString()) + Float.parseFloat(self_rating.get("dl_communication").toString())) * 10;
        Float baha_text = beha_value * 10;
        data = new BarData("Behaviour", beha_value, baha_text.toString().substring(0,baha_text.toString().length()-2) + " %");
        dataList.add(data);

        Map<String, Object> jizz = (Map<String, Object>) selectedMentor.get("jizz");
        if (getLast(jizz) != null) {
            String mainSsss = (String) jizz.get(getLast(jizz));
            splitMainSsss = mainSsss.split("\\s*(=>|,|\\s)\\s*");
        }
        Map<String, Object> likes = (Map<String, Object>) selectedMentor.get("likes");
        Map<String, Object> dislikes = (Map<String, Object>) selectedMentor.get("dislikes");
        for (String splited : splitMainSsss) {
            Float loop_value = (Float.parseFloat(likes.get(splited).toString()) /
                    Float.parseFloat(likes.get(splited).toString()) + Float.parseFloat(dislikes.get(splited).toString())) * 10;
            Float loop_text = (loop_value * 10);
            data = new BarData(splited, loop_value, loop_text.toString().substring(0,loop_text.toString().length()-2) + " %");
            dataList.add(data);
        }
        mChart.setDataList(dataList);
        mChart.build();

        //now fixing the review data
        new DumeModel(context).loadReview(selectedMentor.getId(), null, new TeacherContract.Model.Listener<List<ReviewHighlightData>>() {
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
    }

    public String getLast(Map<String, Object> jizz) {
        List<String> endOFNest = DumeUtils.getEndOFNest();
        for (int j = 0; j < endOFNest.size(); j++) {
            if (jizz.containsKey(endOFNest.get(j))) {
                return endOFNest.get(j);
            }
        }
        return null;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.loading_map_style);
        googleMap.setMapStyle(style);
        mMap = googleMap;
        //config the map here
        onMapReadyListener(mMap);
        onMapReadyGeneralConfig();
        mMap.setPadding((int) (6 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (150 * (getResources().getDisplayMetrics().density)));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        mMap.setMyLocationEnabled(false);
        // sob kaj sesh a ai duita lagate hobe

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                addCustomMarkerFromURL(searchDataStore.getAvatarString(), searchDataStore.getAnchorPoint(), searchDataStore.getGender(), null);
                mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                    @Override
                    public void onCameraMove() {
                        mapFragment.onCameraMove(mMap);
                    }
                });
                mPresenter.onMapLoaded();
                zoomRoute(route);
                if(searchDataStore.getSelectedMentor()!= null && searchDataStore.getSelectedMentor().startsWith("select")){
                    String[] split = retrivedAction.split("\\s*_\\s*");
                    onMentorSelect(searchDataStore.getResultList().get(Integer.parseInt(split[1])));
                    searchDataStore.setSelectedMentor(null);
                }
            }
        });
    }

    public void zoomRoute(List<LatLng> lstLatLngRoute) {
        lstLatLngRoute.add(searchDataStore.getAnchorPoint());
        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);
        int routePadding = 150;
        LatLngBounds latLngBounds = boundsBuilder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding), 2000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        final float thisZoom = mMap.getCameraPosition().zoom;
                        mMap.setMaxZoomPreference((thisZoom + 0.5f));
                        mMap.setMinZoomPreference((thisZoom - 0.6f));

                        //init the first item click
                        List<LatLng> pathRoute = new ArrayList<>();
                        pathRoute.add(searchDataStore.getAnchorPoint());
                        pathRoute.add(route.get(0));
                        mapFragment.setUpPath(pathRoute, mMap, RouteOverlayView.AnimType.ARC);
                        onMentorSelect(searchDataStore.getResultList().get(0));
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });
    }


    @Override
    public void centerTheMapCamera() {
        List<LatLng> lstLatLngRoute = route;
        lstLatLngRoute.add(searchDataStore.getAnchorPoint());
        if (mMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 70;
        LatLngBounds latLngBounds = boundsBuilder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding), 2000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {
                        final float thisZoom = mMap.getCameraPosition().zoom;
                        mMap.setMaxZoomPreference((thisZoom + 0.5f));
                        mMap.setMinZoomPreference((thisZoom - 0.6f));
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(optionMenu, menu);
        if (optionMenu == R.menu.menu_search_result) {

        } else if (optionMenu == R.menu.menu_only_help) {

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search_set:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_help:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_list_view:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                setConfirmedOrCanceled(true);
                startActivity(new Intent(this, SearchResultTabviewActivity.class));
                //finish();
                break;
            case android.R.id.home:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    onBackPressed();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchDataStore.getFirstTime()) {
            cancelBtnClicked();
        } else {
            //show dialogue of discard changes
            super.onBackPressed();
            searchDataStore.setFirstTime(true);
        }
    }

    @Override
    public void cancelBtnClicked() {
        mBackBSD.show();
    }


    @Override
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMyGpsLocationChanged(Location location) {

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

    private Marker addCustomMarkerFromURL(String url, LatLng lattitudeLongitude, String gender, String stars) {
        if (mMap == null) {
            return null;
        }
        if (url != null && !url.equals("")) {
            Glide.with(getApplicationContext())
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
                defaultUrl = SearchDataStore.DEFAULTFEMALEAVATER;;
            }
            Glide.with(getApplicationContext())
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

        iconFactory.setStyle(IconGenerator.STYLE_DEFAULT);
        iconFactory.setTextAppearance(this, R.style.MyCustomInfoWindowTextApp);
        iconFactory.setBackground(getDrawable(R.drawable.custom_info_window_vector));
        iconFactory.setContentPadding((int) (27 * (getResources().getDisplayMetrics().density)), (int) (2 * (getResources().getDisplayMetrics().density)), 0, (int) (6 * (getResources().getDisplayMetrics().density)));

        double v = SphericalUtil.computeDistanceBetween(searchDataStore.getAnchorPoint(), lattitudeLongitude);
        if (searchDataStore.getAnchorPoint() != lattitudeLongitude) {
            addCustomInfoWindow(iconFactory, makeCharSequence(stars + " ★", formatNumber(v)), lattitudeLongitude);
        }
        return mMarkerA;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        for (DocumentSnapshot item : searchDataStore.getResultList()) {
            GeoPoint location = item.getGeoPoint("location");
            LatLng itemLoc = new LatLng(location.getLatitude(), location.getLongitude());
            if (marker.getPosition().equals(itemLoc)) {
                onMentorSelect(item);
                Log.w(TAG, "onMarkerClick: " + item.toString());
                break;
            } else {
                Log.w(TAG, "onMarkerClick: Not Matching with any loc Marker : " + marker.getPosition() + " --" + itemLoc.toString());
            }
        }
        return false;
        //true means no default behaviour
    }

    @Override
    public void showRequestDialogue() {
        confirmSubText = mMakeRequestBSD.findViewById(R.id.sub_text);
        Map<String, Object> sp_info = (Map<String, Object>) selectedMentor.get("sp_info");
        String name = String.format("%s %s", sp_info.get("first_name"), sp_info.get("last_name"));
        confirmSubText.setText("By confirming request will be sent to " + name + "...");
        mMakeRequestBSD.show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        onMarkerClick(marker);
    }

    public void onSearchResultViewClicked(View view) {
        mPresenter.onSearchResultIntracted(view);
    }

    private String formatNumber(double distance) {
        String unit = "m";
        if (distance < 1) {
            distance *= 1000;
            unit = "mm";
        } else if (distance > 1000) {
            distance /= 1000;
            unit = "km";
        }

        return "" + Math.round(distance) + " " + unit;
    }

    public void showProgressBS() {
        if (loadViewBS.getVisibility() == View.INVISIBLE || loadViewBS.getVisibility() == View.GONE) {
            loadViewBS.setVisibility(View.VISIBLE);
        }
        if (!loadViewBS.isRunningAnimation()) {
            loadViewBS.startLoading();
        }
    }

    public void hideProgressBS() {
        if (loadViewBS.isRunningAnimation()) {
            loadViewBS.stopLoading();
        }
        if (loadViewBS.getVisibility() == View.VISIBLE) {
            loadViewBS.setVisibility(View.INVISIBLE);
        }
    }

    private String getAge(int year, int day, String sMonth) {
        int month;
        switch (sMonth) {
            case "Jan":
                month = 1;
                break;
            case "Feb":
                month = 2;
                break;
            case "Mar":
                month = 3;
                break;
            case "Apr":
                month = 4;
                break;
            case "May":
                month = 5;
                break;
            case "Jun":
                month = 6;
                break;
            case "Jul":
                month = 7;
                break;
            case "Aug":
                month = 8;
                break;
            case "Sep":
                month = 9;
                break;
            case "Oct":
                month = 10;
                break;
            case "Nov":
                month = 11;
                break;
            case "Dec":
                month = 12;
                break;
            default:
                month = 6;
                break;
        }

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public boolean isConfirmedOrCanceled() {
        return isConfirmedOrCanceled;
    }

    public void setConfirmedOrCanceled(boolean confirmedOrCanceled) {
        isConfirmedOrCanceled = confirmedOrCanceled;
    }

    private void getRouteBetweenMarker(LatLng One, LatLng Two) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(One, Two)
                .key(getResources().getString(R.string.google_direction_key))
                .build();
        routing.execute();
    }

    private void erasePolylines() {
        for (Polyline line : polylines) {
            line.remove();
        }
        polylines.clear();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("fuck", "onRoutingFailure: " + e.getMessage());
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(7 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}

//testing the swipe here
/* onSwipeTouchListener = new OnSwipeTouchListener(this) {
    @Override
    protected void onSwipeRight() {
        Toast.makeText(SearchResultActivity.this, "Right Swipe", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onSwipeLeft() {
        Toast.makeText(SearchResultActivity.this, "Left Swipe", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onSwipeTop() {
    }
    @Override
    protected void onSwipeBottom() {
    }
};
basicInfo.setOnTouchListener(onSwipeTouchListener);
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
    return super.dispatchTouchEvent(ev);
}
reviewRecyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && scrollFirstTime) {
                    Toast.makeText(SearchResultActivity.this, "Last", Toast.LENGTH_SHORT).show();
                    scrollFirstTime = false;
                }
            }
        });

  /* PolylineOptions line =
        new PolylineOptions().add(mDummyLatLng, mDummyLatLngOne)
                .width(5).color(Color.RED);
Polyline polyline = mMap.addPolyline(line);
polyline.remove();*/


/*PolygonOptions area=
        new PolygonOptions().add(mDummyLatLng, mDummyLatLngOne,
                new LatLng(40.765136435316755, -73.97989511489868))
                .strokeColor(Color.BLUE);
mMap.addPolygon(area);*/
//getRouteBetweenMarker(mDummyLatLng, mDummyLatLngOne);
/* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }, 1000);*/
/*<fragment xmlns:android="http://schemas.android.com/apk/res/android"
                            xmlns:map="http://schemas.android.com/apk/res-auto"
                            android:id="@+id/mapOne"
                            android:name="com.google.android.gms.maps.MapFragment"
                            android:layout_width="match_parent"
                            android:layout_height="match_parent"
                            map:cameraZoom="15"
                            map:liteMode="true"
                            map:mapType="normal" />*/

 /*private void showDistance(LatLng anchor, LatLng skillAnchor) {
       double distance = SphericalUtil.computeDistanceBetween(mMarkerA.getPosition(), mMarkerB.getPosition());
      mTextView.setText("The markers are " + formatNumber(distance) + " apart.");
    }*/