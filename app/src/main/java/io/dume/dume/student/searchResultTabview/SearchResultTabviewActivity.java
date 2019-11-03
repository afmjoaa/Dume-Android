package io.dume.dume.student.searchResultTabview;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

public class SearchResultTabviewActivity extends CustomStuAppCompatActivity implements SearchResultTabviewContract.View {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private SearchResultTabviewContract.Presenter mPresenter;
    private static final int fromFlag = 7;

    private int[] navIcons = {
            R.drawable.ic_tic_salary,
            R.drawable.ic_tic_performance,
            R.drawable.ic_tic_expertise,
            R.drawable.ic_tic_accept_ratio
    };
    private TabLayout tabLayout;
    private LinearLayout noDataBlock;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu5_activity_search_result_tabview);
        setActivityContext(this, fromFlag);
        findLoadView();
        mPresenter = new SearchResultTabviewPresenter(this, new SearchResultTabviewModel());
        mPresenter.searchResultTabviewEnqueue();
        configureAppbarWithoutColloapsing(this, "Search Results");

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
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout_with_up_icon, null);
            String navLabels[] = getResources().getStringArray(R.array.SearchResultTabViewTabtext);

            // get child TextView and ImageView from this layout for the icon and label
            LinearLayout horizontalContainer = tab.findViewById(R.id.horizontal_container);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
            tab_label.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Cairo-Light.ttf"));
            tab_label.setText(navLabels[i]);
            tab_icon.setImageResource(navIcons[i]);
            horizontalContainer.setLayoutParams(textParam);

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
        noDataBlock = findViewById(R.id.no_data_block);
    }

    @Override
    public void initSearchResultTabview() {

    }

    @Override
    public void configSearchResultTabview() {
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
        getMenuInflater().inflate(R.menu.menu_search_result_tabview, menu);
        return true;
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String TAG = "PlaceholderFragment";
        private static final String ARG_SECTION_NUMBER = "section_number";
        private SearchResultTabviewActivity myThisActivity;
        private RecyclerView searchResultTabRecyclerView;
        private SwipeRefreshLayout swipeRefreshLayout;
        private SearchResultTabRecyAda recordsRecyAda;

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
            myThisActivity = (SearchResultTabviewActivity) getActivity();
            View rootView = inflater.inflate(R.layout.stu5_fragment_search_result_tabview, container, false);
            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);
            searchResultTabRecyclerView = rootView.findViewById(R.id.search_result_tabview_recycle_view);
            swipeRefreshLayout = rootView.findViewById(R.id.swipeToRefreshSearchTab);

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
                    }, 1000);
                }
            });
            List<DocumentSnapshot> availableProfileDocu = new ArrayList<>();
            availableProfileDocu = SearchDataStore.getInstance().getResultList();
            Log.e(TAG, "availableProfileDocu.size(); " + availableProfileDocu.size());
            List<SearchResultTabData> availableProfile = generateDataFromDoc(availableProfileDocu);
            if (availableProfile.size() <= 0) {
                myThisActivity.noDataBlock.setVisibility(View.VISIBLE);
            } else {
                myThisActivity.noDataBlock.setVisibility(View.GONE);
            }

            switch (fragmentPosition) {
                case 1:
                    List<SearchResultTabData> salaryFilterData = availableProfile;
                    //salaryFilterData.sort((o1, o2) -> o1.getSalary().compareTo(o2.getSalary()));
                    for (int i = 0; i < salaryFilterData.size(); i++) {
                        salaryFilterData.get(i).setMentorFilterImage(1);
                    }

                    Collections.sort(salaryFilterData, (t1, t2) -> t1.mSalary - t2.mSalary);
                    // salaryFilterData.sort(Comparator.comparing(SearchResultTabData::getSalary));
                    recordsRecyAda = new SearchResultTabRecyAda(myThisActivity, salaryFilterData) {
                        @Override
                        void OnItemClicked(View v, String identify) {
                            myThisActivity.searchDataStore.setSelectedMentor(identify);
                            myThisActivity.onBackPressed();
                        }
                    };
                    break;
                case 2:
                    List<SearchResultTabData> ratingFilterData = availableProfile;
                    for (int i = 0; i < ratingFilterData.size(); i++) {
                        ratingFilterData.get(i).setMentorFilterImage(2);
                    }
                    Collections.sort(ratingFilterData, (t1, t2) -> t1.mRating.compareTo(t2.mRating));

                    // ratingFilterData.sort(Comparator.comparing(SearchResultTabData::getRating));
                    recordsRecyAda = new SearchResultTabRecyAda(myThisActivity, ratingFilterData) {
                        @Override
                        void OnItemClicked(View v, String identify) {
                            myThisActivity.searchDataStore.setSelectedMentor(identify);
                            myThisActivity.onBackPressed();
                        }
                    };
                    break;
                case 3:
                    List<SearchResultTabData> expertiseFilterData = availableProfile;
                    for (int i = 0; i < expertiseFilterData.size(); i++) {
                        expertiseFilterData.get(i).setMentorFilterImage(3);
                    }
                    Collections.sort(expertiseFilterData, (t1, t2) -> t1.mExpirtise - t2.mExpirtise);

                    //expertiseFilterData.sort(Comparator.comparing(SearchResultTabData::getExpertise));
                    recordsRecyAda = new SearchResultTabRecyAda(myThisActivity, expertiseFilterData) {
                        @Override
                        void OnItemClicked(View v, String identify) {
                            myThisActivity.searchDataStore.setSelectedMentor(identify);
                            myThisActivity.onBackPressed();
                        }
                    };
                    break;
                case 4:
                    List<SearchResultTabData> aRatioFilterData = availableProfile;
                    for (int i = 0; i < aRatioFilterData.size(); i++) {
                        aRatioFilterData.get(i).setMentorFilterImage(4);
                    }
                    Collections.sort(aRatioFilterData, (t1, t2) -> t1.mAcceptRatio - t2.mAcceptRatio);

                    //  aRatioFilterData.sort(Comparator.comparing(SearchResultTabData::getA_ratio));
                    recordsRecyAda = new SearchResultTabRecyAda(myThisActivity, aRatioFilterData) {
                        @Override
                        void OnItemClicked(View v, String identify) {
                            myThisActivity.searchDataStore.setSelectedMentor(identify);
                            myThisActivity.onBackPressed();
                        }
                    };
                    break;
            }


            searchResultTabRecyclerView.setAdapter(recordsRecyAda);
            searchResultTabRecyclerView.setLayoutManager(new LinearLayoutManager(myThisActivity));
            return rootView;
        }

        public List<SearchResultTabData> generateDataFromDoc(List<DocumentSnapshot> documentSnapshots) {
            List<SearchResultTabData> returnedData = new ArrayList<>();

            for (int i = 0; i < documentSnapshots.size(); i++) {
                DocumentSnapshot currentDocument = documentSnapshots.get(i);
                SearchResultTabData searchResultTabData = new SearchResultTabData();
                Map<String, Object> sp_info = (Map<String, Object>) currentDocument.get("sp_info");
                Map<String, Object> selfRating = (Map<String, Object>) sp_info.get("self_rating");
                Map<String, Object> unread_records = (Map<String, Object>) sp_info.get("unread_records");
                final String name = String.format("%s %s", sp_info.get("first_name"), sp_info.get("last_name"));
                searchResultTabData.setMentorName(name);
                String avatar = (String) sp_info.get("avatar");
                searchResultTabData.setMentorDPUrl(avatar);
                searchResultTabData.setGender((String) sp_info.get("gender"));
                searchResultTabData.setMentorFilterImage(1);
                searchResultTabData.setIdentify(i);

                NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
                Number mentorAskedSalary = (Number) currentDocument.get("salary");

                SearchDataStore searchDataStore = SearchDataStore.getInstance();
                int threshold = searchDataStore.getPackageName().equals(SearchDataStore.DUME_GANG) ? 4 : 3;
                List<String> localQueryList = searchDataStore.getQueryList();
                List<String> dbQueryList = (List<String>) currentDocument.get("query_list");

                String localSB = localQueryList.get(localQueryList.size() - threshold);
                String dbSB = dbQueryList.get(dbQueryList.size() - threshold);

                String localtrim = localSB.replaceAll("\\s", "");
                String dbtrim = dbSB.replaceAll("\\s", "");

                String[] localsplit = localtrim.split(",");
                String[] dbsplit = dbtrim.split(",");

                Number salary = mentorAskedSalary.floatValue()*0.25 +((mentorAskedSalary.floatValue() - (mentorAskedSalary.floatValue()*0.25))/
                        (dbsplit.length))*localsplit.length;

                searchResultTabData.mSalary = salary.intValue();

                String format1 = currencyInstance.format(salary);
                searchResultTabData.setSalary("Salary : " + format1.substring(1, format1.length() - 3) + " BDT");
                searchResultTabData.setRating((String) selfRating.get("star_rating"));

                Integer a_ratio_value = ((Integer.parseInt(unread_records.get("accepted_count").toString())
                        + Integer.parseInt(unread_records.get("completed_count").toString())
                        + Integer.parseInt(unread_records.get("current_count").toString())
                        + Integer.parseInt(unread_records.get("pending_count").toString()) + 1) /
                        (Integer.parseInt(unread_records.get("accepted_count").toString())
                                + Integer.parseInt(unread_records.get("completed_count").toString())
                                + Integer.parseInt(unread_records.get("current_count").toString())
                                + Integer.parseInt(unread_records.get("pending_count").toString())
                                + Integer.parseInt(unread_records.get("rejected_count").toString()) + 1)) * 100;
                searchResultTabData.setA_ratio(a_ratio_value + " %");
                searchResultTabData.mAcceptRatio = a_ratio_value;


                Integer expertise_value = (Integer.parseInt(selfRating.get("l_expertise").toString()) /
                        Integer.parseInt(selfRating.get("l_expertise").toString()) + Integer.parseInt(selfRating.get("dl_expertise").toString())) * 100;
                searchResultTabData.mExpirtise = expertise_value;
                searchResultTabData.mRating = Float.parseFloat((String) selfRating.get("star_rating"));
                searchResultTabData.setExpertise(expertise_value + " %");
                searchResultTabData.setMentorUid(currentDocument.getString("mentor_uid"));
                searchResultTabData.setDocumentUid(currentDocument.getId());
                returnedData.add(searchResultTabData);
            }

            return returnedData;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_help:
                //Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                testingCustomDialogue(R.string.search_result_tab_info);
                break;
            case R.id.action_scatter_view:
                searchDataStore.setSelectedMentor("select_0");
                onBackPressed();
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

    public void testingCustomDialogue(int infoStringId) {
        // custom dialog
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_obligation_dialogue);

        //all find view here
        Button dismissBtn = dialog.findViewById(R.id.dismiss_btn);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        carbon.widget.ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        dialogText.setGravity(Gravity.START);

        dialogImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_info_icon_green));
        dialogText.setText(infoStringId);

        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
