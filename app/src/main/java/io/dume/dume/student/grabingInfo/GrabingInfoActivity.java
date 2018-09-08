package io.dume.dume.student.grabingInfo;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.grabingPackage.GrabingPackageActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;

public class GrabingInfoActivity extends CustomStuAppCompatActivity implements GrabingInfoContract.View {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private View decor;
    private ViewPager mViewPager;
    private int[] navIcons = {
            R.drawable.ic_medium,
            R.drawable.ic_class,
            R.drawable.ic_subject,
            R.drawable.ic_payment,
            R.drawable.ic_gender_preference,
            R.drawable.ic_cross_check

    };
    private int[] navLabels = {
            R.string.tob_medium,
            R.string.tab_class,
            R.string.tab_subject,
            R.string.tab_payment,
            R.string.tab_gender_preference,
            R.string.tab_cross_ckeck
    };
    private String[] givenInfo = {"Ex.Others", "Ex.O level", "Ex.Physics", "Ex.3k - 6k", "Ex.Both", "Ex.→←"};
    private TabLayout tabLayout;
    private TextView hintIdOne;
    private TextView hintIdTwo;
    private TextView hintIdThree;
    private static final String TAG = "GrabingInfoActivity";
    private static final int fromFlag = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu2_activity_grabing_info);
        setActivityContext(this, fromFlag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        hintIdOne = findViewById(R.id.hint_id_1);
        hintIdTwo = findViewById(R.id.hint_id_2);
        hintIdThree = findViewById(R.id.hint_id_3);

        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);

        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tablayout_tab, null);

            // get child TextView and ImageView from this layout for the icon and label
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
            tab_label.setText(getResources().getString(navLabels[i]));
            tab_icon.setImageResource(navIcons[i]);

            // finally publish this custom view to navigation tab
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
        // finishes here ................

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (tabLayout.getSelectedTabPosition() == 5) {
                    gotoGrabingPackage();
                }

            }
        });
//        statrting my fucking code
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
                        hintIdOne.setText(givenInfo[tabPosition]);
                        hintIdTwo.setText(givenInfo[tabPosition + 1]);
                        hintIdThree.setText(givenInfo[tabPosition + 2]);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        hintIdOne.setText(givenInfo[tabPosition - 1]);
                        hintIdTwo.setText(givenInfo[tabPosition]);
                        hintIdThree.setText(givenInfo[tabPosition + 1]);
                        break;
                    case 5:
                        hintIdOne.setText(givenInfo[tabPosition - 2]);
                        hintIdTwo.setText(givenInfo[tabPosition - 1]);
                        hintIdThree.setText(givenInfo[tabPosition]);
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
                        hintIdOne.setText(givenInfo[tabPosition]);
                        hintIdTwo.setText(givenInfo[tabPosition + 1]);
                        hintIdThree.setText(givenInfo[tabPosition + 2]);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        hintIdOne.setText(givenInfo[tabPosition - 1]);
                        hintIdTwo.setText(givenInfo[tabPosition]);
                        hintIdThree.setText(givenInfo[tabPosition + 1]);
                        break;
                    case 5:
                        hintIdOne.setText(givenInfo[tabPosition - 2]);
                        hintIdTwo.setText(givenInfo[tabPosition - 1]);
                        hintIdThree.setText(givenInfo[tabPosition]);
                        break;
                    default:
                        break;
                }
            }
        });

        decor = getWindow().getDecorView();
//        settingStatusBarTransparent(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        AppBarLayout mAppBarLayout = findViewById(R.id.appbar);
        mAppBarLayout.getBackground().setAlpha(120);
        toolbar.getBackground().setAlpha(0);
    }

    //unused function here
    private void gotoGrabingLocation() {
        Log.d(TAG, "gotoGrabingLocation: fucking function called");
        startActivity(new Intent(this, GrabingLocationActivity.class));
    }

    private void gotoGrabingPackage() {
        startActivity(new Intent(this, GrabingPackageActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grabing_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerAdapter recyclerAdapter;
        private final int VERTICAL_ITEM_SPACE = -20;
        private GrabingInfoActivity myMainActivity;
        private RecyclerView mRecyclerView;
        private static final String TAG = "PlaceholderFragment";

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
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                // load data here
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 6) {
                    if (getActivity() != null) {
                        recyclerAdapter = new RecyclerAdapter(getActivity(), getFinalData()) {
                            @Override
                            protected void OnButtonClicked(View v, int position) {
                                //toolbar button clicked
                                if (position == 0) {

                                } else {
                                    assert myMainActivity != null;
                                    TabLayout.Tab tab = myMainActivity.tabLayout.getTabAt(position - 1);
                                    if (tab != null) {
                                        tab.select();
                                    }
                                }

                            }
                        };
                        mRecyclerView.setAdapter(recyclerAdapter);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }

                }
            } else {
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 6) {
                    onDestroyView();
                }
                // fragment is no longer visible
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            myMainActivity = (GrabingInfoActivity) getActivity();
            String[] headers = {"Select yuor medium", "Select your class",
                    "Select the subject your want mentor", "Choose selary in offer", "Select gender preference", "Verify the given info"};
            View rootView = inflater.inflate(R.layout.stu2_fragment_grabing_info, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            LinearLayout radioButtonLayout = rootView.findViewById(R.id.radio_button_linear_layout);
            LinearLayout textViewLayout = rootView.findViewById(R.id.textView_linear_layout);
            View rootViewRadioButtonLayoutContent;

            int fragmentPosition = getArguments().getInt(ARG_SECTION_NUMBER);

            //  background color alpha changing here
            LinearLayout fragmentBg = rootView.findViewById(R.id.fragment_bg);
            fragmentBg.getBackground().setAlpha(90);
            // Setting the header text view
            textView.setText(headers[fragmentPosition - 1]);

//            checking which fragment it is !!
            switch (fragmentPosition) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    textViewLayout.setVisibility(View.GONE);
                    radioButtonLayout.setVisibility(View.VISIBLE);
                    rootViewRadioButtonLayoutContent = inflater.inflate(R.layout.custom_grading_info_radio_group, container, false);
                    radioButtonLayout.addView(rootViewRadioButtonLayoutContent, 0);
                    RadioButton[] radioButtonsArr = {
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_one),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_two),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_three),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_four),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_five),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_six),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_seven),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_eight),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_nine),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_ten),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_eleven),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_twelve),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_thirteen),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_fourteen),
                            rootViewRadioButtonLayoutContent.findViewById(R.id.radio_btn_fifteen),

                    };
                    String currentFragData[] = getData();
                    for (int i = 0; i < radioButtonsArr.length; i++) {
                        if (i < currentFragData.length) {
                            radioButtonsArr[i].setText(currentFragData[i]);
                        } else {
                            radioButtonsArr[i].setVisibility(View.GONE);
                        }
                    }

                    RadioGroup fragRadioGroup = (RadioGroup) rootViewRadioButtonLayoutContent.findViewById(R.id.frag_radio_group);
                    fragRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            // checkedId is the RadioButton selected
                            RadioButton rb = (RadioButton) group.findViewById(checkedId);
                            String currentSelectedBtnText = rb.getText().toString();
                            myMainActivity.givenInfo[fragmentPosition - 1] = currentSelectedBtnText;
                        }
                    });
                    break;
                case 6:
                    radioButtonLayout.setVisibility(View.GONE);
                    textViewLayout.setVisibility(View.VISIBLE);
                    mRecyclerView = textViewLayout.findViewById(R.id.recycler_view_list);
                    recyclerAdapter = new RecyclerAdapter(getActivity(), getFinalData()) {
                        @Override
                        protected void OnButtonClicked(View v, int position) {
                            //toolbar button clicked
                            if (position == 0) {

                            } else {
                                assert myMainActivity != null;
                                TabLayout.Tab tab = myMainActivity.tabLayout.getTabAt(position - 1);
                                if (tab != null) {
                                    tab.select();
                                }
                            }

                        }
                    };
                    mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
                    mRecyclerView.setAdapter(recyclerAdapter);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    break;
            }

            return rootView;
        }

        public String[] getData() {
            String[] titles = {"can't match anything"};
            String[] mediums = {"Bangla Medium", "Bangla Version", "English Medium", "English Version", "Others"};
            String[] classes = {"University", "A-Level", "O-Level", "Class-8", "Class-7", "Class-6", "Class-5", "Class-4", "Class-3", "Class-2", "Class-1", "Others"};
            String[] subjects = {"Physics", "Chemistry", "Mathematics", "Biology", "English", "Bengali"};
            String[] salaries = {"4k-5k", "5k-6k", "6k-8k", "8k-10k", "10k-15k", "15k-20k"};
            String[] genderPreference = {"Male", "Female", "Both"};
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    return mediums;
                case 2:
                    return classes;
                case 3:
                    return subjects;
                case 4:
                    return salaries;
                case 5:
                    return genderPreference;
                default:
                    return titles;
            }
        }

        public List<RecycleData> getFinalData() {
            List<RecycleData> data = new ArrayList<>();
            String[] finalInfo = {"For :", "Medium :", "Class :", "Subject :", "Salary :", "Gender Preference :"};
            for (int i = 0; i < finalInfo.length; i++) {
                if (i == 0) {

                } else {
                    if (!(myMainActivity.givenInfo[i - 1].startsWith("Ex"))) {
                        finalInfo[i] += " " + myMainActivity.givenInfo[i - 1];
                    }
                    if (i == 5) {
                        Log.d("tagtagtag", " " + myMainActivity.givenInfo[i] + myMainActivity.givenInfo[i - 1]);
                    }
                }
            }
            for (String title : finalInfo) {
                RecycleData current = new RecycleData();
                current.options = title;
                data.add(current);
            }
            return data;
        }

        public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

            private final int verticalSpaceHeight;

            VerticalSpaceItemDecoration(int verticalSpaceHeight) {
                this.verticalSpaceHeight = verticalSpaceHeight;
            }

            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {

                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = verticalSpaceHeight;
                }
            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 6;
        }
    }


    @Override
    public void configGrabingInfoPage() {

    }

    @Override
    public void initGrabingInfoPage() {

    }

    @Override
    public void findView() {

    }
}
