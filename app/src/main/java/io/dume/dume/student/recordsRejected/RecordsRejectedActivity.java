package io.dume.dume.student.recordsRejected;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hadiidbouk.charts.BarData;
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

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.recordsCompleted.RecordsCompletedActivity;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.Pred;
import io.dume.dume.util.VisibleToggleClickListener;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static io.dume.dume.util.DumeUtils.getLast;

public class RecordsRejectedActivity extends CustomStuAppCompatActivity implements RecordsRejectedContract.View {

    private RecordsRejectedContract.Presenter mPresenter;
    private static final int fromFlag = 21;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;
    private String retriveAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu10_activity_records_rejected);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new RecordsRejectedPresenter(this, new RecordsRejectedModel());
        mPresenter.recordsRejectedEnqueue();
        DumeUtils.configureAppbar(this, "Rejected Requests");
        if (getIntent().getAction() != null) {
            retriveAction = getIntent().getAction();
        }
    }

    @Override
    public void findView() {
        pager = (ViewPager) findViewById(R.id.rejected_page_container);
    }

    @Override
    public void initRecordsRejected() {

    }

    @Override
    public void configRecordsRejected() {
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
        private static DocumentSnapshot record;
        private RecordsRejectedActivity myThisActivity;
        private Context context;
        private float mDensity;
        private TextView timeTV;
        private TextView dateTV;
        private TextView preferredDayTV;
        private TextView daysPerWeekTV;
        private carbon.widget.ImageView studentDisplayPic;
        private carbon.widget.ImageView mentorDisplayPic;
        private TextView studentNameTV;
        private TextView mentorNameTV;
        private MaterialRatingBar studentRatingBar;
        private MaterialRatingBar mentorRatingBar;
        private TextView subjectInDemand;
        private TextView salaryInDemandTV;
        private RelativeLayout relativeHostLayout;
        private Button salaryBtn;
        private LinearLayout agreementHostLayout;
        private Button agreementInfoBtn;
        private LinearLayout agreementHideable;
        private DocumentSnapshot selectedMentor;
        private String defaultUrl;


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
            mDensity = context.getResources().getDisplayMetrics().density;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myThisActivity = (RecordsRejectedActivity) getActivity();
            View rootView = inflater.inflate(R.layout.stu10_viewpager_layout_rejected, container, false);

            salaryBtn = rootView.findViewById(R.id.show_salary_btn);
            agreementHostLayout = rootView.findViewById(R.id.agreement_term_host_linearlayout);
            agreementInfoBtn = rootView.findViewById(R.id.show_agreement_terms_btn);
            agreementHideable = rootView.findViewById(R.id.agreement_term_layout_vertical);

            relativeHostLayout = rootView.findViewById(R.id.recordsHostLayout);
            studentDisplayPic = rootView.findViewById(R.id.student_display_pic);
            mentorDisplayPic = rootView.findViewById(R.id.mentor_display_pic);
            studentNameTV = rootView.findViewById(R.id.student_name);
            mentorNameTV = rootView.findViewById(R.id.mentor_name);
            studentRatingBar = rootView.findViewById(R.id.student_rating_bar);
            mentorRatingBar = rootView.findViewById(R.id.mentor_rating_bar);
            subjectInDemand = rootView.findViewById(R.id.subject_in_demand);
            salaryInDemandTV = rootView.findViewById(R.id.salary_in_demand);

            timeTV = rootView.findViewById(R.id.textview_starting_time);
            dateTV = rootView.findViewById(R.id.textview_starting_date);
            preferredDayTV = rootView.findViewById(R.id.textview_preferred_day);
            daysPerWeekTV = rootView.findViewById(R.id.textview_days_week);
            onMentorSelect(record);
            configFragmentBtnClick();
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

        }

        public void configFragmentBtnClick() {
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
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<DocumentSnapshot> recordDataRejected;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            recordDataRejected = new ArrayList<>();
            recordDataRejected = DumeUtils.filterList(Google.getInstance().getRecords(),"Rejected");
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position, recordDataRejected.get(position));
        }

        @Override
        public int getCount() {
            return recordDataRejected.size();
        }
    }

}
