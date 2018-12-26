package io.dume.dume.student.recordsPage;

import android.app.ActivityOptions;
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
import android.support.v4.util.Pair;
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

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity;
import io.dume.dume.student.recordsCompleted.RecordsCompletedActivity;
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity;
import io.dume.dume.student.recordsPending.RecordsPendingActivity;
import io.dume.dume.student.recordsRejected.RecordsRejectedActivity;
import io.dume.dume.util.DumeUtils;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu8_activity_records_page);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new RecordsPagePresenter(this, new RecordsPageModel());
        mPresenter.recordsPageEnqueue();
        configureAppbarWithoutColloapsing(this, "Records");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //making the custom tab here
        int[] wh = DumeUtils.getScreenSize(this);
        int tabMinWidth = ((wh[0] / 3) - (int) (24 * (getResources().getDisplayMetrics().density)));
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                (tabMinWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);
            String navLabels[] = getResources().getStringArray(R.array.RecordsPageTabtext);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
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

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    @Override
    public void findView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);


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
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                    ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
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
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                View tabView = tab.getCustomView();
                // get inflated children Views the icon and the label by their id
                if (tabView != null) {
                    ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
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
        switch (id){
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
        private LinearLayout leftTransitionLayout;
        private LinearLayout centerTransitionLayoutOne;
        private LinearLayout centerTransitionLayoutTwo;
        private LinearLayout rightTransitionLayout;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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

            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.stu8_fragment_records_page, container, false);
            recordRecyclerView = rootView.findViewById(R.id.records_page_recycle_view);
            swipeRefreshLayout = rootView.findViewById(R.id.swipeToRefreshRecords);

            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_green_light,
                    android.R.color.holo_red_light, android.R.color.holo_blue_light);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 5000);
                }
            });

            //setting the recycler view
            switch (fragmentPosition) {
                case 1:
                    List<RecordsRecyData> recordDataPending = new ArrayList<>();
                    RecordsRecyAdapter recordsRecyAdapending = new RecordsRecyAdapter(myThisActivity, recordDataPending) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            /*leftTransitionLayout = v.findViewById(R.id.left_vertical_host_layout);
                            centerTransitionLayoutOne = v.findViewById(R.id.center_vertical_host_layout);
                            rightTransitionLayout = v.findViewById(R.id.right_vertical_host_layout);
                            android.util.Pair[] pairsPending = new android.util.Pair[3];
                            pairsPending[0] = new android.util.Pair<View, String>(leftTransitionLayout, "leftTransistion");
                            pairsPending[1] = new android.util.Pair<View, String>(centerTransitionLayoutOne, "centerTransistionOne");
                            pairsPending[2] = new android.util.Pair<View, String>(rightTransitionLayout, "rightTransition");
                            ActivityOptions optionsPending = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairsPending);
                            startActivity(new Intent(myThisActivity, RecordsPendingActivity.class).setAction("student"), optionsPending.toBundle());*/

                            startActivity(new Intent(myThisActivity, RecordsPendingActivity.class).setAction("student"));
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Pending Records aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    recordRecyclerView.setAdapter(recordsRecyAdapending);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;
                case 2:
                    List<RecordsRecyData> recordDataAccepted = new ArrayList<>();
                    RecordsRecyAdapter recordsRecyAdaAccepted = new RecordsRecyAdapter(myThisActivity, recordDataAccepted) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            startActivity(new Intent(myThisActivity, RecordsAcceptedActivity.class).setAction("student"));
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Accepted Records aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    recordRecyclerView.setAdapter(recordsRecyAdaAccepted);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;
                case 3:
                    List<RecordsRecyData> recordDataCurrent = new ArrayList<>();
                    RecordsRecyAdapter recordsRecyAdaCurrent = new RecordsRecyAdapter(myThisActivity, recordDataCurrent) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            startActivity(new Intent(myThisActivity, RecordsCurrentActivity.class).setAction("student"));
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Current Records aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    recordRecyclerView.setAdapter(recordsRecyAdaCurrent);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;
                case 4:
                    List<RecordsRecyData> recordDataCompletded = new ArrayList<>();
                    RecordsRecyAdapter recordsRecyAdaCompleted = new RecordsRecyAdapter(myThisActivity, recordDataCompletded) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            startActivity(new Intent(myThisActivity, RecordsCompletedActivity.class).setAction("student"));
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Completed Records aren't deletable", Toast.LENGTH_SHORT).show();
                        }
                    };
                    recordRecyclerView.setAdapter(recordsRecyAdaCompleted);
                    recordRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    break;

                case 5:
                    List<RecordsRecyData> recordDataRejected = new ArrayList<>();
                    RecordsRecyAdapter recordsRecyAdaRejected = new RecordsRecyAdapter(myThisActivity, recordDataRejected) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            startActivity(new Intent(myThisActivity, RecordsRejectedActivity.class).setAction("student"));
                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "from Rejected longClick", Toast.LENGTH_SHORT).show();
                        }
                    };
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
            // Show 3 total pages.
            return 5;
        }
    }
}
