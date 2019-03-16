package io.dume.dume.student.recordsPage;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity;
import io.dume.dume.student.recordsCompleted.RecordsCompletedActivity;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.student.recordsPending.RecordsPendingActivity;
import io.dume.dume.student.recordsRejected.RecordsRejectedActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.RECORDTAB;
import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

public class RecordsPageActivity extends CustomStuAppCompatActivity implements RecordsPageContract.View {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private RecordsPageContract.Presenter mPresenter;
    private static final int fromFlag = 25;
    private TabLayout tabLayout;

    private int[] navIcons = {
            R.drawable.ic_task_pending,
            R.drawable.ic_task_accepted,
            R.drawable.ic_task_current,
            R.drawable.ic_task_completed,
            R.drawable.ic_task_rejected
    };
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noDataBlockMain;
    private List<Record> recordListMain = new ArrayList<>();
    public String retriveAction = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu8_activity_records_page);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new RecordsPagePresenter(this, new RecordsPageModel(this));
        mPresenter.recordsPageEnqueue();
        configureAppbarWithoutColloapsing(this, "Record");

        //making the custom tab here
        int[] wh = DumeUtils.getScreenSize(this);
        int tabMinWidth = ((wh[0] / 3) - (int) (24 * (getResources().getDisplayMetrics().density)));
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                (tabMinWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_with_badge, null);
            String navLabels[] = getResources().getStringArray(R.array.RecordsPageTabtext);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = tab.findViewById(R.id.nav_label);
            ImageView tab_icon = tab.findViewById(R.id.nav_icon);
            carbon.widget.TextView badgeTV = tab.findViewById(R.id.badgeTV);
            switch (i) {
                case 0:
                    badgeTV.setBackgroundColor(getResources().getColor(R.color.status_pending_badge));
                    break;
                case 1:
                    badgeTV.setBackgroundColor(getResources().getColor(R.color.status_accepted_badge));
                    badgeTV.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 2:
                    badgeTV.setBackgroundColor(getResources().getColor(R.color.status_current_badge));
                    break;
                case 3:
                    badgeTV.setBackgroundColor(getResources().getColor(R.color.status_completed_badge));
                    break;
                case 4:
                    badgeTV.setBackgroundColor(getResources().getColor(R.color.status_rejected_badge));
                    break;
            }
            badgeTV.setVisibility(View.GONE);
            tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
            /*tab_label.setTextColor(getResources().getColorStateList(R.color.tab_colorstate_light));
             */
            tab_label.setText(navLabels[i]);
            tab_icon.setImageResource(navIcons[i]);
            tab_label.setLayoutParams(textParam);
            // finally publish this custom view to navigation tab
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
        // finishes here ................
        mPresenter.recordsPageLoadData(new TeacherContract.Model.Listener<Void>() {
            @Override
            public void onSuccess(Void list) {
                //nothing to do
            }
            @Override
            public void onError(String msg) {
                //already handled
            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        if (getIntent().getAction() != null) {
            retriveAction = getIntent().getAction();
        }
    }

    @Override
    public void findView() {
        tabLayout = findViewById(R.id.tabs);
        mViewPager = findViewById(R.id.container);
        noDataBlockMain = findViewById(R.id.no_data_block);
    }

    @Override
    public void onDataLoadFinsh() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        recordListMain = Google.getInstance().getRecordList();
        if (recordListMain.size() > 0) {
            noDataBlockMain.setVisibility(View.GONE);
        } else {
            noDataBlockMain.bringToFront();
            noDataBlockMain.setVisibility(View.VISIBLE);
        }
        List<Record> recordDataPending = DumeUtils.filterRecord(recordListMain, "Pending");
        List<Record> recordDataAccepted = DumeUtils.filterRecord(recordListMain, "Accepted");
        List<Record> recordDataCurrent = DumeUtils.filterRecord(recordListMain, "Current");
        List<Record> recordDataCompleted = DumeUtils.filterRecord(recordListMain, "Completed");
        List<Record> recordDataRejected = DumeUtils.filterRecord(recordListMain, "Rejected");

        View customView = Objects.requireNonNull(tabLayout.getTabAt(0)).getCustomView();
        carbon.widget.TextView badgeTV = Objects.requireNonNull(customView).findViewById(R.id.badgeTV);
        badgeTV.setText("" + recordDataPending.size());
        if (recordDataPending.size() > 0) {
            badgeTV.setVisibility(View.VISIBLE);
        } else {
            badgeTV.setVisibility(View.GONE);
        }
        customView = Objects.requireNonNull(tabLayout.getTabAt(1)).getCustomView();
        badgeTV = Objects.requireNonNull(customView).findViewById(R.id.badgeTV);
        badgeTV.setText("" + recordDataAccepted.size());
        if (recordDataAccepted.size() > 0) {
            badgeTV.setVisibility(View.VISIBLE);
        } else {
            badgeTV.setVisibility(View.GONE);
        }
        customView = Objects.requireNonNull(tabLayout.getTabAt(2)).getCustomView();
        badgeTV = Objects.requireNonNull(customView).findViewById(R.id.badgeTV);
        badgeTV.setText("" + recordDataCurrent.size());
        if (recordDataCurrent.size() > 0) {
            badgeTV.setVisibility(View.VISIBLE);
        } else {
            badgeTV.setVisibility(View.GONE);
        }
        customView = Objects.requireNonNull(tabLayout.getTabAt(3)).getCustomView();
        badgeTV = Objects.requireNonNull(customView).findViewById(R.id.badgeTV);
        badgeTV.setText("" + recordDataCompleted.size());
        if (recordDataCompleted.size() > 0) {
            badgeTV.setVisibility(View.VISIBLE);
        } else {
            badgeTV.setVisibility(View.GONE);
        }
        customView = Objects.requireNonNull(tabLayout.getTabAt(4)).getCustomView();
        badgeTV = Objects.requireNonNull(customView).findViewById(R.id.badgeTV);
        badgeTV.setText("" + recordDataRejected.size());
        if (recordDataRejected.size() > 0) {
            badgeTV.setVisibility(View.VISIBLE);
        } else {
            badgeTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void load() {
        super.showProgress();
    }

    @Override
    public void stopLoad() {
        super.hideProgress();
    }

    @Override
    public void flush(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initRecordsPage() {

    }

    @Override
    public void configRecordsPage() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    TextView tab_label = tabView.findViewById(R.id.nav_label);
                    ImageView tab_icon = tabView.findViewById(R.id.nav_icon);
                    Drawable drawableIcon = tab_icon.getDrawable();

                    if (drawableIcon instanceof Animatable) {
                        ((Animatable) drawableIcon).start();
                    }
                }
                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                    default:
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    TextView tab_label = tabView.findViewById(R.id.nav_label);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    ImageView tab_icon = tabView.findViewById(R.id.nav_icon);
                    Drawable drawableIcon = tab_icon.getDrawable();

                    if (drawableIcon instanceof Animatable) {
                        ((Animatable) drawableIcon).start();
                    }
                }
                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_records_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_help:
                break;
            case R.id.action_sync_now:
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


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView recordRecyclerView;
        private RecordsPageActivity myThisActivity;
        private SwipeRefreshLayout swipeRefreshLayout;
        private FragmentActivity activity;
        private TextView noItemText;
        private LinearLayout noDataBlock;
        private List<Record> recordList;
        private RecordsRecyAdapter recordsRecyAdapending;
        private RecordsRecyAdapter recordsRecyAdaAccepted;
        private RecordsRecyAdapter recordsRecyAdaCurrent;
        private RecordsRecyAdapter recordsRecyAdaCompleted;
        private RecordsRecyAdapter recordsRecyAdaRejected;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

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
            myThisActivity = (RecordsPageActivity) getActivity();
            activity = getActivity();
            recordList = Google.getInstance().getRecordList();
            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.stu8_fragment_records_page, container, false);
            recordRecyclerView = rootView.findViewById(R.id.records_page_recycle_view);
            swipeRefreshLayout = rootView.findViewById(R.id.swipeToRefreshRecords);
            noItemText = rootView.findViewById(R.id.no_item_text);
            noDataBlock = rootView.findViewById(R.id.no_data_block);

            swipeRefreshLayout.setColorSchemeResources( android.R.color.holo_green_light,android.R.color.holo_orange_light,
                    android.R.color.holo_red_light, android.R.color.holo_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(recordsRecyAdaRejected!= null){
                        myThisActivity.mPresenter.recordsPageLoadData(new TeacherContract.Model.Listener<Void>() {
                            @Override
                            public void onSuccess(Void list) {
                                //nothing to do
                                switch (fragmentPosition) {
                                    case 1:
                                        List<Record> recordDataPending = new ArrayList<>();
                                        recordDataPending = Google.getInstance().getRecordList();
                                        recordDataPending = DumeUtils.filterRecord(recordDataPending, "Pending");
                                        noItemText.setText("Sorry, no pending records to show right now...");
                                        if (recordDataPending.size() > 0) {
                                            noDataBlock.setVisibility(View.GONE);
                                        } else {
                                            noDataBlock.setVisibility(View.VISIBLE);
                                        }
                                        recordRecyclerView.setAdapter(recordsRecyAdapending);
                                        break;
                                    case 2:
                                        List<Record> recordDataAccepted = new ArrayList<>();
                                        recordDataAccepted = Google.getInstance().getRecordList();
                                        recordDataAccepted = DumeUtils.filterRecord(recordDataAccepted, "Accepted");
                                        noItemText.setText("Sorry, no accepted records to show right now...");
                                        if (recordDataAccepted.size() > 0) {
                                            noDataBlock.setVisibility(View.GONE);
                                        } else {
                                            noDataBlock.setVisibility(View.VISIBLE);
                                        }
                                        recordRecyclerView.setAdapter(recordsRecyAdaAccepted);
                                        break;
                                    case 3:
                                        List<Record> recordDataCurrent = new ArrayList<>();
                                        recordDataCurrent = Google.getInstance().getRecordList();
                                        recordDataCurrent = DumeUtils.filterRecord(recordDataCurrent, "Current");
                                        noItemText.setText("Sorry, no current records to show right now...");
                                        if (recordDataCurrent.size() > 0) {
                                            noDataBlock.setVisibility(View.GONE);
                                        } else {
                                            noDataBlock.setVisibility(View.VISIBLE);
                                        }
                                        recordRecyclerView.setAdapter(recordsRecyAdaCurrent);
                                        break;
                                    case 4:
                                        List<Record> recordDataCompletded = new ArrayList<>();
                                        recordDataCompletded = Google.getInstance().getRecordList();
                                        recordDataCompletded = DumeUtils.filterRecord(recordDataCompletded, "Completed");
                                        noItemText.setText("Sorry, no completed records to show right now...");
                                        if (recordDataCompletded.size() > 0) {
                                            noDataBlock.setVisibility(View.GONE);
                                        } else {
                                            noDataBlock.setVisibility(View.VISIBLE);
                                        }
                                        recordRecyclerView.setAdapter(recordsRecyAdaCompleted);
                                        break;

                                    case 5:
                                        List<Record> recordDataRejected = new ArrayList<>();
                                        recordDataRejected = Google.getInstance().getRecordList();
                                        recordDataRejected = DumeUtils.filterRecord(recordDataRejected, "Rejected");
                                        noItemText.setText("Sorry, no rejected records to show right now...");
                                        if (recordDataRejected.size() > 0) {
                                            noDataBlock.setVisibility(View.GONE);
                                        } else {
                                            noDataBlock.setVisibility(View.VISIBLE);
                                        }
                                        recordRecyclerView.setAdapter(recordsRecyAdaRejected);
                                        break;
                                    default:
                                        break;
                                }
                                swipeRefreshLayout.setRefreshing(false);
                            }
                            @Override
                            public void onError(String msg) {
                                //already handled
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }else {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });

            //setting the recycler view
            switch (fragmentPosition) {
                case 1:
                    List<Record> recordDataPending = new ArrayList<>();
                    recordDataPending = Google.getInstance().getRecordList();
                    recordDataPending = DumeUtils.filterRecord(recordDataPending, "Pending");
                    recordsRecyAdapending = new RecordsRecyAdapter(myThisActivity, recordDataPending) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            if (myThisActivity.retriveAction != null) {
                                switch (myThisActivity.retriveAction) {
                                    case DumeUtils.STUDENT:
                                        Intent stuIntent = new Intent(myThisActivity, RecordsPendingActivity.class).setAction(DumeUtils.STUDENT);
                                        stuIntent.putExtra(RECORDTAB, position);
                                        startActivity(stuIntent);
                                        break;
                                    case DumeUtils.TEACHER:
                                        Intent mntIntent = new Intent(myThisActivity, RecordsPendingActivity.class).setAction(DumeUtils.TEACHER);
                                        mntIntent.putExtra(RECORDTAB, position);
                                        startActivity(mntIntent);
                                        break;
                                    case DumeUtils.BOOTCAMP:
                                        startActivity(new Intent(myThisActivity, RecordsPendingActivity.class).setAction(DumeUtils.BOOTCAMP));
                                        break;
                                    default:
                                        startActivity(new Intent(myThisActivity, RecordsPendingActivity.class).setAction(DumeUtils.STUDENT));
                                        break;
                                }
                            }
                        }
                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Pending Record aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    noItemText.setText("Sorry, no pending records to show right now...");
                    if (recordDataPending.size() > 0) {
                        noDataBlock.setVisibility(View.GONE);
                    } else {
                        noDataBlock.setVisibility(View.VISIBLE);
                    }
                    recordRecyclerView.setAdapter(recordsRecyAdapending);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;
                case 2:
                    List<Record> recordDataAccepted = new ArrayList<>();
                    recordDataAccepted = Google.getInstance().getRecordList();
                    recordDataAccepted = DumeUtils.filterRecord(recordDataAccepted, "Accepted");
                    recordsRecyAdaAccepted = new RecordsRecyAdapter(myThisActivity, recordDataAccepted) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            if (myThisActivity.retriveAction != null) {
                                switch (myThisActivity.retriveAction) {
                                    case DumeUtils.STUDENT:
                                        Intent stuIntent = new Intent(myThisActivity, RecordsAcceptedActivity.class).setAction(DumeUtils.STUDENT);
                                        stuIntent.putExtra(RECORDTAB, position);
                                        startActivity(stuIntent);
                                        break;
                                    case DumeUtils.TEACHER:
                                        final Intent mntIntent = new Intent(myThisActivity, RecordsAcceptedActivity.class).setAction(DumeUtils.TEACHER);
                                        mntIntent.putExtra(RECORDTAB, position);
                                        startActivity(mntIntent);
                                        break;
                                    case DumeUtils.BOOTCAMP:
                                        startActivity(new Intent(myThisActivity, RecordsAcceptedActivity.class).setAction(DumeUtils.BOOTCAMP));
                                        break;
                                    default:
                                        startActivity(new Intent(myThisActivity, RecordsAcceptedActivity.class).setAction(DumeUtils.STUDENT));
                                        break;
                                }
                            }
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Accepted Record aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    noItemText.setText("Sorry, no accepted records to show right now...");
                    if (recordDataAccepted.size() > 0) {
                        noDataBlock.setVisibility(View.GONE);
                    } else {
                        noDataBlock.setVisibility(View.VISIBLE);
                    }
                    recordRecyclerView.setAdapter(recordsRecyAdaAccepted);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;
                case 3:
                    List<Record> recordDataCurrent = new ArrayList<>();
                    recordDataCurrent = Google.getInstance().getRecordList();
                    recordDataCurrent = DumeUtils.filterRecord(recordDataCurrent, "Current");
                    recordsRecyAdaCurrent = new RecordsRecyAdapter(myThisActivity, recordDataCurrent) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            if (myThisActivity.retriveAction != null) {
                                switch (myThisActivity.retriveAction) {
                                    case DumeUtils.STUDENT:
                                        final Intent stuIntent = new Intent(myThisActivity, RecordsCurrentActivity.class).setAction(DumeUtils.STUDENT);
                                        stuIntent.putExtra(RECORDTAB, position);
                                        startActivity(stuIntent);
                                        break;
                                    case DumeUtils.TEACHER:
                                        final Intent mntIntent = new Intent(myThisActivity, RecordsCurrentActivity.class).setAction(DumeUtils.TEACHER);
                                        mntIntent.putExtra(RECORDTAB, position);
                                        startActivity(mntIntent);
                                        break;
                                    case DumeUtils.BOOTCAMP:
                                        startActivity(new Intent(myThisActivity, RecordsCurrentActivity.class).setAction(DumeUtils.BOOTCAMP));
                                        break;
                                    default:
                                        startActivity(new Intent(myThisActivity, RecordsCurrentActivity.class).setAction(DumeUtils.STUDENT));
                                        break;
                                }
                            }
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Current Record aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    noItemText.setText("Sorry, no current records to show right now...");
                    if (recordDataCurrent.size() > 0) {
                        noDataBlock.setVisibility(View.GONE);
                    } else {
                        noDataBlock.setVisibility(View.VISIBLE);
                    }
                    recordRecyclerView.setAdapter(recordsRecyAdaCurrent);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;
                case 4:
                    List<Record> recordDataCompletded = new ArrayList<>();
                    recordDataCompletded = Google.getInstance().getRecordList();
                    recordDataCompletded = DumeUtils.filterRecord(recordDataCompletded, "Completed");
                    recordsRecyAdaCompleted = new RecordsRecyAdapter(myThisActivity, recordDataCompletded) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            if (myThisActivity.retriveAction != null) {
                                switch (myThisActivity.retriveAction) {
                                    case DumeUtils.STUDENT:
                                        Intent stuIntent = new Intent(myThisActivity, RecordsCompletedActivity.class).setAction(DumeUtils.STUDENT);
                                        stuIntent.putExtra(RECORDTAB, position);
                                        startActivity(stuIntent);
                                        break;
                                    case DumeUtils.TEACHER:
                                        Intent mntIntent = new Intent(myThisActivity, RecordsCompletedActivity.class).setAction(DumeUtils.TEACHER);
                                        mntIntent.putExtra(RECORDTAB, position);
                                        startActivity(mntIntent);
                                        break;
                                    case DumeUtils.BOOTCAMP:
                                        startActivity(new Intent(myThisActivity, RecordsCompletedActivity.class).setAction(DumeUtils.BOOTCAMP));
                                        break;
                                    default:
                                        startActivity(new Intent(myThisActivity, RecordsCompletedActivity.class).setAction(DumeUtils.STUDENT));
                                        break;
                                }
                            }
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Completed Record aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    noItemText.setText("Sorry, no completed records to show right now...");
                    if (recordDataCompletded.size() > 0) {
                        noDataBlock.setVisibility(View.GONE);
                    } else {
                        noDataBlock.setVisibility(View.VISIBLE);
                    }
                    recordRecyclerView.setAdapter(recordsRecyAdaCompleted);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;

                case 5:
                    List<Record> recordDataRejected = new ArrayList<>();
                    recordDataRejected = Google.getInstance().getRecordList();
                    recordDataRejected = DumeUtils.filterRecord(recordDataRejected, "Rejected");
                    recordsRecyAdaRejected = new RecordsRecyAdapter(myThisActivity, recordDataRejected) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            if (myThisActivity.retriveAction != null) {
                                switch (myThisActivity.retriveAction) {
                                    case DumeUtils.STUDENT:
                                        Intent stuIntent = new Intent(myThisActivity, RecordsRejectedActivity.class).setAction(DumeUtils.STUDENT);
                                        stuIntent.putExtra(RECORDTAB, position);
                                        startActivity(stuIntent);
                                        break;
                                    case DumeUtils.TEACHER:
                                        Intent mntIntent = new Intent(myThisActivity, RecordsRejectedActivity.class).setAction(DumeUtils.TEACHER);
                                        mntIntent.putExtra(RECORDTAB, position);
                                        startActivity(mntIntent);
                                        break;
                                    case DumeUtils.BOOTCAMP:
                                        startActivity(new Intent(myThisActivity, RecordsRejectedActivity.class).setAction(DumeUtils.BOOTCAMP));
                                        break;
                                    default:
                                        startActivity(new Intent(myThisActivity, RecordsRejectedActivity.class).setAction(DumeUtils.STUDENT));
                                        break;
                                }
                            }
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "from Rejected longClick", Toast.LENGTH_SHORT).show();
                        }
                    };
                    noItemText.setText("Sorry, no rejected records to show right now...");
                    if (recordDataRejected.size() > 0) {
                        noDataBlock.setVisibility(View.GONE);
                    } else {
                        noDataBlock.setVisibility(View.VISIBLE);
                    }
                    recordRecyclerView.setAdapter(recordsRecyAdaRejected);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;
                default:
                    break;
            }
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
            return 5;
        }
    }
}
