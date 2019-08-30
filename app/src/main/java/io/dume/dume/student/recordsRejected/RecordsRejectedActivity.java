package io.dume.dume.student.recordsRejected;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.student.homePage.adapter.HomePageRecyclerData;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.VisibleToggleClickListener;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RecordsRejectedActivity extends CustomStuAppCompatActivity implements RecordsRejectedContract.View {

    private RecordsRejectedContract.Presenter mPresenter;
    private static final int fromFlag = 21;
    private ViewPager pager;
    private SectionsPagerAdapter myPagerAdapter;
    private String retriveAction;
    private RecordsRejectedModel mModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu10_activity_records_rejected);
        setActivityContext(this, fromFlag);
        findLoadView();
        mModel = new RecordsRejectedModel(this);
        mPresenter = new RecordsRejectedPresenter(this, mModel);
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
        Intent retrivedIntent = getIntent();
        int pageToOpen = retrivedIntent.getIntExtra(DumeUtils.RECORDTAB, -1);
        String recordId = retrivedIntent.getStringExtra("recordId");

        if (pageToOpen != -1 && pageToOpen< Objects.requireNonNull(pager.getAdapter()).getCount()) {
            // Open the right pager
            pager.setCurrentItem(pageToOpen,true);
        }else if(recordId != null && !recordId.equals("")){
        List<DocumentSnapshot> rejectedRecords = DumeUtils.filterList(Google.getInstance().getRecords(), "Rejected");
        for (int i = 0; i < rejectedRecords.size(); i++) {
            DocumentSnapshot record = rejectedRecords.get(i);
            if (recordId.equals(record.getId())) {
                pager.setCurrentItem(i, true);
                break;
            }
        }
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
        private static DocumentSnapshot record;
        private static List<DocumentSnapshot> recordList;
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
        private Button deleteRecordBtn;
        private BottomSheetDialog mBottomSheetReject;
        private View sheetViewReject;
        private TextView rejectMainText;
        private TextView rejectSubText;
        private Button rejectYesBtn;
        private Button rejectNoBtn;
        private int fragmentPosition;
        private Button rejectedByBtn;
        private SearchDataStore searchDataStore;
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
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myThisActivity = (RecordsRejectedActivity) getActivity();
            if (getArguments() != null) {
                fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            }
            record = recordList.get(fragmentPosition);

            View rootView = inflater.inflate(R.layout.stu10_viewpager_layout_rejected, container, false);
            salaryBtn = rootView.findViewById(R.id.show_salary_btn);
            saleImageView = rootView.findViewById(R.id.sale_image);
            rejectedByBtn = rootView.findViewById(R.id.show_rejectedby_btn);
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

            deleteRecordBtn = rootView.findViewById(R.id.pendding_cancel_btn);

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
            if(bal != null){
                mentorRating = Float.parseFloat((String) Objects.requireNonNull(bal.get("star_rating")));
            }else {
                mentorRating = 5.0f;
            }
            Map<String, Object> forMap = (Map<String, Object>) documentData.get("for_whom");
            Map<String, Object> shMap = (Map<String, Object>) forMap.get("request_sr");
            if(shMap!= null){
                studentRating = Float.parseFloat((String) Objects.requireNonNull(shMap.get("star_rating")));
            }else {
                studentRating = 5.0f;
            }
            studentName = (String) forMap.get("stu_name");
            studentDpUrl = (String) forMap.get("request_avatar");
            salaryInDemand = String.valueOf((Number) documentData.get("salary"));
            sGender = (String) forMap.get("request_gender");
            mGender = (String) spMap.get("gender");
            subjectExchange = DumeUtils.getLast((Map<String, Object>) documentData.get("jizz"));

            Date creation = (Date) documentData.get("creation");

            status = (String) documentData.get("record_status");
            Record record = new Record(mentorName, studentName, salaryInDemand, subjectExchange, creation, mentorDpUrl, studentDpUrl, studentRating, mentorRating, status, Record.DELIVERED, sGender, mGender);
            String rejected_by = selectedMentor.getString("rejected_by");
            if (DumeUtils.STUDENT.equals(rejected_by)) {
                rejectedByBtn.setText(String.format("Rejected By : %s", studentName));
            }else if(DumeUtils.TEACHER.equals(rejected_by) || DumeUtils.BOOTCAMP.equals(rejected_by)) {
                rejectedByBtn.setText(String.format("Rejected By : %s", mentorName));
            }else{
                rejectedByBtn.setText("Rejected By : Dume(For Inactivity)");
            }

            mentorNameTV.setText(record.getMentorName());
            studentNameTV.setText(record.getStudentName());
            salaryInDemand = record.getSalaryInDemand();
            salaryInDemand = NumberFormat.getCurrencyInstance(Locale.US).format(Double.parseDouble(salaryInDemand));
            salaryInDemandTV.setText(salaryInDemand.substring(1, salaryInDemand.length() - 3) + " BDT");
            subjectInDemand.setText(record.getSubjectExchange());

            if (record.getStudentDpUrl() != null && !record.getStudentDpUrl().equals("")) {
                Glide.with(context).load(record.getStudentDpUrl()).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(studentDisplayPic);
            } else {
                if (record.getsGender() != null || record.getsGender().equals("Male") || record.getsGender().equals("")) {
                    defaultUrl = SearchDataStore.DEFAULTMALEAVATER;
                } else {
                    defaultUrl = SearchDataStore.DEFAULTFEMALEAVATER;
                }
                Glide.with(context).load(defaultUrl).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(studentDisplayPic);
            }
            Glide.with(context).load(record.getMentorDpUrl()).apply(new RequestOptions().override((int) (50 * mDensity), (int) (50 * mDensity)).placeholder(R.drawable.demo_default_avatar_dark)).into(mentorDisplayPic);

            mentorRatingBar.setRating(record.getMentorRating());
            studentRatingBar.setRating(record.getStudentRating());

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
                rejectMainText.setText("Delete this record ?");
                rejectSubText.setText("Deleting will delete this record and you will never find this again...");
                rejectYesBtn.setText("Yes, Delete");
                rejectNoBtn.setText("No");
                rejectYesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetReject.dismiss();
                        myThisActivity.showProgress();
                        rejectYesBtn.setEnabled(false);
                        rejectNoBtn.setEnabled(false);
                        String key = "s_show_status";
                        switch (myThisActivity.retriveAction) {
                            case DumeUtils.STUDENT:
                                key = "s_show_status";
                                break;
                            case DumeUtils.TEACHER:
                                key = "t_show_status";
                                break;
                            case DumeUtils.BOOTCAMP:
                                key = "t_show_status";
                                break;
                        }

                        myThisActivity.mModel.changeRecordValues(record.getId(), key, false, new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void list) {
                                myThisActivity.hideProgress();
                                rejectYesBtn.setEnabled(true);
                                rejectNoBtn.setEnabled(true);
                                Toast.makeText(myThisActivity, "Rejected record deleted successfully...", Toast.LENGTH_SHORT).show();
                                myThisActivity.mModel.getRecords(new TeacherContract.Model.Listener<List<Record>>() {
                                    @Override
                                    public void onSuccess(List<Record> list) {
                                        Google.getInstance().setRecordList(list);
                                        searchDataStore.setRecordStatusChanged(true);
                                        searchDataStore.setFromPACCR(4);
                                        myThisActivity.onBackPressed();
                                    }
                                    @Override
                                    public void onError(String msg) {
                                        Toast.makeText(myThisActivity, msg, Toast.LENGTH_SHORT).show();
                                        myThisActivity.hideProgress();
                                    }
                                });
                            }
                            @Override
                            public void onError(String msg) {
                                rejectYesBtn.setEnabled(true);
                                rejectNoBtn.setEnabled(true);
                                myThisActivity.hideProgress();
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

            switch (myThisActivity.retriveAction) {
                case DumeUtils.STUDENT:
                    break;
                case DumeUtils.TEACHER:
                    break;
                case DumeUtils.BOOTCAMP:
                    break;
                default:
                    break;
            }

            deleteRecordBtn.setOnClickListener(view -> {
                mBottomSheetReject.show();
            });
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<DocumentSnapshot> recordDataRejected;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            recordDataRejected = new ArrayList<>();
            recordDataRejected = DumeUtils.filterList(Google.getInstance().getRecords(), "Rejected");
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position, recordDataRejected);
        }

        @Override
        public int getCount() {
            return recordDataRejected.size();
        }
    }

}
