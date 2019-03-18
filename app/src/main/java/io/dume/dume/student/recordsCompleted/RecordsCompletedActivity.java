package io.dume.dume.student.recordsCompleted;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.ReviewAdapter;
import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Academic;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.VisibleToggleClickListener;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static io.dume.dume.util.DumeUtils.getLast;

public class RecordsCompletedActivity extends CustomStuAppCompatActivity implements RecordsCompletedContract.View {

    private RecordsCompletedContract.Presenter mPresenter;
    private static final int fromFlag = 24;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;
    private String retriveAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu11_activity_records_completed);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new RecordsCompletedPresenter(this, new RecordsCompletedModel());
        mPresenter.recordsCompletedEnqueue();
        DumeUtils.configureAppbar(this, "Completed Records");
        if (getIntent().getAction() != null) {
            retriveAction = getIntent().getAction();
        }

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
        Intent retrivedIntent = getIntent();
        int pageToOpen = retrivedIntent.getIntExtra(DumeUtils.RECORDTAB, -1);
        if (pageToOpen != -1 && pageToOpen < Objects.requireNonNull(pager.getAdapter()).getCount()) {
            // Open the right pager
            pager.setCurrentItem(pageToOpen, true);
        }
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
        private static List<DocumentSnapshot> recordList;
        private RecordsCompletedActivity myThisActivity;
        private ChartProgressBar mChart;
        private ImageView ratingPerformance;
        private ImageView ratingExperience;

        private DocumentSnapshot selectedMentor;
        private Button salaryBtn;
        private carbon.widget.ImageView joinedBadge;
        private carbon.widget.ImageView inauguralBadge;
        private carbon.widget.ImageView leadingBadge;
        private carbon.widget.ImageView premierBadge;
        private TextView timeTV;
        private TextView dateTV;
        private TextView preferredDayTV;
        private TextView daysPerWeekTV;
        private TextView performanceCount;
        private TextView experienceCount;
        private TextView aRatioCount;
        private TextView expertiseCount;
        private String[] splitMainSsss;
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
        private static DocumentSnapshot record;
        private Button requestAgainBtn;
        private BottomSheetDialog mBottomSheetReject;
        private View sheetViewReject;
        private TextView rejectMainText;
        private TextView rejectSubText;
        private Button rejectYesBtn;
        private Button rejectNoBtn;
        private View divider;
        private int fragmentPosition;


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, List<DocumentSnapshot> recordList) {
            PlaceholderFragment.recordList = recordList;
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            PlaceholderFragment fragment;
            fragment = new PlaceholderFragment();
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
            myThisActivity = (RecordsCompletedActivity) getActivity();
            if (getArguments() != null) {
                fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            }
            record = recordList.get(fragmentPosition);
            View rootView = inflater.inflate(R.layout.stu11_viewpager_layout_completed, container, false);
            mChart = (ChartProgressBar) rootView.findViewById(R.id.myChartProgressBar);
            ratingPerformance = rootView.findViewById(R.id.main_rating_performance);
            ratingExperience = rootView.findViewById(R.id.main_rating_experience);

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

            relativeHostLayout = rootView.findViewById(R.id.recordsHostLayout);
            studentDisplayPic = rootView.findViewById(R.id.student_display_pic);
            mentorDisplayPic = rootView.findViewById(R.id.mentor_display_pic);
            studentNameTV = rootView.findViewById(R.id.student_name);
            mentorNameTV = rootView.findViewById(R.id.mentor_name);
            studentRatingBar = rootView.findViewById(R.id.student_rating_bar);
            mentorRatingBar = rootView.findViewById(R.id.mentor_rating_bar);
            subjectInDemand = rootView.findViewById(R.id.subject_in_demand);
            salaryInDemandTV = rootView.findViewById(R.id.salary_in_demand);

            requestAgainBtn = rootView.findViewById(R.id.pendding_cancel_btn);
            divider = rootView.findViewById(R.id.divider2);

            onMentorSelect(record);
            configFragmentBtnClick();
            toggleStatus();
            return rootView;
        }

        public void onMentorSelect(DocumentSnapshot selectedMentor) {
            this.selectedMentor = selectedMentor;
            Map<String, Object> sp_info = (Map<String, Object>) selectedMentor.get("sp_info");

            //basic info input here
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
            salaryInDemand = String.valueOf((Number) documentData.get("salary"));
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
            Number salary = (Number) selectedMentor.get("salary");
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

            //fixing the agreement terms now
            Map<String, Object> self_rating = (Map<String, Object>) sp_info.get("self_rating");
            Map<String, Object> start_time = (Map<String, Object>) selectedMentor.get("start_time");
            Map<String, Object> start_date = (Map<String, Object>) selectedMentor.get("start_date");
            Map<String, Object> preferred_days = (Map<String, Object>) selectedMentor.get("preferred_days");
            String Temp = (String) start_time.get("time_string");
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
        }

        public void toggleStatus() {
            //confirm bottom sheet
            Map<String, Object> documentData = record.getData();
            Map<String, Object> spMap = (Map<String, Object>) documentData.get("sp_info");
            Map<String, Object> forMap = (Map<String, Object>) documentData.get("for_whom");
            String mentorName = spMap.get("first_name") + " " + spMap.get("last_name");
            String studentName = (String) forMap.get("stu_name");
            Number salary = (Number) selectedMentor.get("salary");

            //cancel bottom sheet
            mBottomSheetReject = new BottomSheetDialog(context);
            sheetViewReject = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
            mBottomSheetReject.setContentView(sheetViewReject);
            rejectMainText = mBottomSheetReject.findViewById(R.id.main_text);
            rejectSubText = mBottomSheetReject.findViewById(R.id.sub_text);
            rejectYesBtn = mBottomSheetReject.findViewById(R.id.cancel_yes_btn);
            rejectNoBtn = mBottomSheetReject.findViewById(R.id.cancel_no_btn);
            if (rejectMainText != null && rejectSubText != null && rejectYesBtn != null && rejectNoBtn != null) {
                rejectMainText.setText("Request Again ?");
                rejectSubText.setText("Request " + mentorName + " again for the topic...");
                rejectYesBtn.setText("Yes, Request");
                rejectNoBtn.setText("No");
                rejectYesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*myThisActivity.showProgress();
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
                        });*/
                    }
                });
                rejectNoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetReject.dismiss();
                    }
                });
            }


            switch (myThisActivity.retriveAction) {
                case DumeUtils.STUDENT:

                    break;
                case DumeUtils.TEACHER:
                    requestAgainBtn.setText("Pay Dume Obligation");
                    if (rejectMainText != null) {
                        rejectMainText.setText("Pay Due Dume Obligation");
                        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
                        String format1 = currencyInstance.format(salary.doubleValue() * 0.25);
                        rejectSubText.setText("Dear " + mentorName + "your Dume obligation amount is " + format1.substring(1, format1.length() - 3) + " BDT. Please paid due amount for uninterrupted service.");
                        rejectYesBtn.setText("Pay Now");
                        rejectNoBtn.setText("latter");
                    }
                    showAdditionalRatingBtn.setText("Your Rating");
                    achievementInfoBtn.setText("Your Achievements");
                    break;
                case DumeUtils.BOOTCAMP:
                    requestAgainBtn.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }

            requestAgainBtn.setOnClickListener(view -> {
                mBottomSheetReject.show();
            });
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private List<DocumentSnapshot> recordDataCompleted;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            recordDataCompleted = new ArrayList<>();
            recordDataCompleted = DumeUtils.filterList(Google.getInstance().getRecords(), "Completed");
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position, recordDataCompleted);
        }

        @Override
        public int getCount() {
            return recordDataCompleted.size();
        }
    }
}
