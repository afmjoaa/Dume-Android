package io.dume.dume.student.searchResult;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hadiidbouk.charts.BarData;
import com.hadiidbouk.charts.ChartProgressBar;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.student.common.QualificationAdapter;
import io.dume.dume.student.common.QualificationData;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.pojo.CusStuAppComMapActivity;
import io.dume.dume.student.pojo.MyGpsLocationChangeListener;
import io.dume.dume.student.searchResultTabview.SearchResultTabviewActivity;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.OnSwipeTouchListener;
import io.dume.dume.util.VisibleToggleClickListener;

public class SearchResultActivity extends CusStuAppComMapActivity implements OnMapReadyCallback,
        SearchResultContract.View, MyGpsLocationChangeListener {

    private GoogleMap mMap;
    private SearchResultContract.Presenter mPresenter;
    private static final int fromFlag = 6;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
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
    boolean scrollFirstTime = true;
    private Button swipeRight;
    private Button swipeLeft;
    private LinearLayout basicInfo;
    private LinearLayout basicInfoInsider;
    private LinearLayout.LayoutParams basicInfoInsiderLayoutParams;
    private OnSwipeTouchListener onSwipeTouchListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu6_activity_search_result);
        setActivityContextMap(this, fromFlag);
        mPresenter = new SearchResultPresenter(this, new SearchResultModel());
        mPresenter.searchResultEnqueue();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        getLocationPermission(mapFragment);

        //setting the qualification recycler view
        qualificaitonRecyAda = new QualificationAdapter(this, getQualificationData());
        qualificationRecyView.setAdapter(qualificaitonRecyAda);
        qualificationRecyView.setLayoutManager(new LinearLayoutManager(this));

        //setting the review recycler view
        List<ReviewHighlightData> reviewData = new ArrayList<>();
        reviewRecyAda = new ReviewAdapter(this, reviewData);
        reviewRecyView.setAdapter(reviewRecyAda);
        reviewRecyView.setLayoutManager(new LinearLayoutManager(this));
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

    }

    @Override
    public void findView() {
        toolbar = findViewById(R.id.toolbar);
        coordinatorLayout = findViewById(R.id.my_main_container);
        llBottomSheet = findViewById(R.id.searchBottomSheet);
        secondaryAppbarLayout = findViewById(R.id.secondary_Appbar);
        defaultAppbarLayout = findViewById(R.id.my_appbarLayout);
        secondaryToolbar = findViewById(R.id.secondary_toolbar);
        viewMusk = findViewById(R.id.view_musk);
        verticalTextViewContainer = findViewById(R.id.vertical_textview_container);
        mentorDisplayPic = findViewById(R.id.mentor_dp);
        changingOrientationContainer = findViewById(R.id.changing_orientation_layout);
        changingOrientationParams = (LinearLayout.LayoutParams) changingOrientationContainer.getLayoutParams();
        ageText = findViewById(R.id.text_two);
        mentorNameText = findViewById(R.id.text_one);
        ratingPerformance = findViewById(R.id.main_rating_performance);
        ratingExperience = findViewById(R.id.main_rating_experience);
        circleProgressbarARatio = (CircleProgressbar) findViewById(R.id.rating_main_accept_ratio);
        circleProgressbarExpertise = (CircleProgressbar) findViewById(R.id.rating_main_professionalism);
        mChart = (ChartProgressBar) findViewById(R.id.myChartProgressBar);
        ratingHostVertical = findViewById(R.id.rating_host_linearlayout);
        showAdditionalRatingBtn = findViewById(R.id.show_additional_rating_btn);
        onlyRatingContainer = findViewById(R.id.rating_layout_vertical);
        qualificationRecyView = findViewById(R.id.recycler_view_qualifications);
        reviewRecyView = findViewById(R.id.recycler_view_reviews);
        reviewInfoBtn = findViewById(R.id.review_info_btn);
        moreInfoBtn = findViewById(R.id.show_more_info_btn);
        moreInfoHost = findViewById(R.id.more_info_host_linearlayout);
        moreInfoHidable = findViewById(R.id.more_info_layout_vertical);
        reviewHost = findViewById(R.id.review_host_linearlayout);
        reviewHidable = findViewById(R.id.review_layout_vertical);
        achievementInfoBtn = findViewById(R.id.show_achievement_btn);
        achievementHost = findViewById(R.id.achievement_host_linearlayout);
        achievementHidable = findViewById(R.id.achievement_layout_vertical);
        basicInfo = findViewById(R.id.basic_info);
        basicInfoInsider = findViewById(R.id.basic_info_insider_layout);
        basicInfoInsiderLayoutParams = (LinearLayout.LayoutParams) basicInfoInsider.getLayoutParams();
        swipeLeft = findViewById(R.id.swipe_left);
        swipeRight = findViewById(R.id.swipe_right);

    }

    @Override
    public void initSearchResult() {
        setSupportActionBar(toolbar);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_black_24dp);
        toolbar.setOverflowIcon(drawable);
        settingStatusBarTransparent();
        setDarkStatusBarIcon();

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        ViewTreeObserver vto = llBottomSheet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                llBottomSheet.getLayoutParams().height = (llBottomSheet.getHeight() - secondaryAppbarLayout.getHeight() - (int) (6 * (getResources().getDisplayMetrics().density)));
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
                if (BottomSheetBehavior.STATE_HIDDEN == newState) {

                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    dragFirst = true;
                    setDarkStatusBarIcon();
                    viewMusk.setVisibility(View.GONE);
                    secondaryAppbarLayout.setVisibility(View.INVISIBLE);
                    defaultAppbarLayout.setVisibility(View.VISIBLE);
                    changingOrientationContainer.setOrientation(LinearLayout.HORIZONTAL);
                    changingOrientationParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    changingOrientationContainer.setLayoutParams(changingOrientationParams);
                    ageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
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


                } else if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    setLightStatusBarIcon();
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);

                    circleProgressbarARatio.setProgressWithAnimation(80, ANIMATIONDURATION);
                    circleProgressbarExpertise.setProgressWithAnimation(50, ANIMATIONDURATION);
                    swipeLeft.setVisibility(View.VISIBLE);
                    swipeRight.setVisibility(View.VISIBLE);

                } else if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    viewMusk.setVisibility(View.VISIBLE);
                    secondaryAppbarLayout.setVisibility(View.VISIBLE);
                    defaultAppbarLayout.setVisibility(View.INVISIBLE);
                    changingOrientationContainer.setOrientation(LinearLayout.VERTICAL);
                    changingOrientationParams.height = (int) (150 * (getResources().getDisplayMetrics().density));
                    changingOrientationContainer.setLayoutParams(changingOrientationParams);
                    ageText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
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

                } /*else if (BottomSheetBehavior.STATE_SETTLING == newState) {
                    loaderView.setVisibility(View.VISIBLE);
                }*/
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
                //onlyRatingContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                if (visible) {
                    onlyRatingContainer.setVisibility(View.INVISIBLE);
                } else {
                    onlyRatingContainer.setVisibility(View.VISIBLE);
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
                                if (!visible) {
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
                if (visible) {
                    moreInfoHidable.setVisibility(View.VISIBLE);
                } else {
                    moreInfoHidable.setVisibility(View.INVISIBLE);
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
                        .setDuration(1000)
                        .addListener(new Transition.TransitionListener() {
                            @Override
                            public void onTransitionStart(@NonNull Transition transition) {

                            }

                            @Override
                            public void onTransitionEnd(@NonNull Transition transition) {
                                if (!visible) {
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
                //onlyRatingContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                if (visible) {
                    reviewHidable.setVisibility(View.VISIBLE);
                } else {
                    reviewHidable.setVisibility(View.INVISIBLE);
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
                                if (!visible) {
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
                //onlyRatingContainer.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                if (visible) {
                    achievementHidable.setVisibility(View.VISIBLE);
                } else {
                    achievementHidable.setVisibility(View.INVISIBLE);
                }

            }

        });


        //testing the swipe here
        onSwipeTouchListener = new OnSwipeTouchListener(this) {
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


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        onSwipeTouchListener.getGestureDetector().onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style_default_json);
        googleMap.setMapStyle(style);
        mMap = googleMap;
        mMap.setPadding((int) (6 * (getResources().getDisplayMetrics().density)), 0, 0, (int) (150 * (getResources().getDisplayMetrics().density)));

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);
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
                startActivity(new Intent(this, SearchResultTabviewActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMyGpsLocationChanged(Location location) {

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