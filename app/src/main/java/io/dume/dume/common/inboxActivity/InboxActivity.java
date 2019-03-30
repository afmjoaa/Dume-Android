package io.dume.dume.common.inboxActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jackandphantom.circularprogressbar.CircleProgressbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.dume.dume.Google;
import io.dume.dume.R;
import io.dume.dume.common.chatActivity.ChatActivity;
import io.dume.dume.common.chatActivity.DemoModel;
import io.dume.dume.common.chatActivity.Room;
import io.dume.dume.common.contactActivity.ContactActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.studentHelp.StudentHelpActivity;
import io.dume.dume.student.studentSettings.StudentSettingsActivity;
import io.dume.dume.teacher.adapters.AcademicAdapter;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.homepage.fragments.AcademicFragment;
import io.dume.dume.teacher.mentor_settings.AccountSettings;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configToolbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbarWithoutColloapsing;

import io.dume.dume.common.chatActivity.*;

public class InboxActivity extends CustomStuAppCompatActivity implements InboxActivityContact.View {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private InboxActivityContact.Presenter mPresenter;
    private static final String TAG = "foo";
    private static final int fromFlag = 30;
    private AppBarLayout myAppbarLayout;
    private FloatingActionButton fab;
    int[] iconIntArray = {R.drawable.ic_inbox_chat,
            R.drawable.ic_inbox_notification_creater,
            R.drawable.ic_inbox_call};
    private TabLayout tabLayout;
    private int currentTabPosition = 0;
    private Intent contactFromIntent;
    private Toolbar myToolbar;
    private static InboxChatAdapter recordsRecyAda;
    public Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common1_activity_inbox);
        setActivityContext(this, fromFlag);
        context = this;
        mPresenter = new InboxActivityPresenter(this, new InboxActivityModel(this));
        mPresenter.inboxEnqueue();
        configureAppbarWithoutColloapsing(this, "Inbox");
        findLoadView();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        //making the custom tab here
        int[] wh = DumeUtils.getScreenSize(this);
        int tabMinWidth = ((wh[0] / 3) - (int) (24 * (getResources().getDisplayMetrics().density)));
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                (tabMinWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        // loop through all navigation tabs
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab_layout_inbox, null);

            // get child TextView and ImageView from this layout for the icon and label
            String[] tab_label_name = getResources().getStringArray(R.array.inboxHeader);
            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
            TextView tab_state = (TextView) tab.findViewById(R.id.active_state_dot);
            tab.setLayoutParams(textParam);
            tab_label.setText(tab_label_name[i]);
            tab_state.setText("");//"" or "*"

            // finally publish this custom view to navigation tab
            Objects.requireNonNull(tabLayout.getTabAt(i)).setCustomView(tab);
        }
        // finishes here ................

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentTabPosition) {
                    case 0:
                        contactFromIntent.putExtra("contactFrom", 0);
                        contactFromIntent.setAction("from_message");
                        startActivityForResult(contactFromIntent, 4321);
                        break;
                    case 1:
                        Toast.makeText(InboxActivity.this, "Send notification feature is coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        contactFromIntent.putExtra("contactFrom", 2);
                        contactFromIntent.setAction("from_call");
                        startActivity(contactFromIntent);
                        break;
                }
            }
        });


        //testing code here
        Intent notiIntent = getIntent();
        int tabToOpen = notiIntent.getIntExtra("notiTab", -1);
        if (tabToOpen != -1) {
            // Open the right tab
            TabLayout.Tab tab = tabLayout.getTabAt(tabToOpen);
            Objects.requireNonNull(tab).select();
        }
        String retrieveAction = notiIntent.getAction();
        if (retrieveAction != null && retrieveAction.equals("from_record")) {
            flush("Press the bottom right corner floating btn to add participants..");
        }
    }

    public void flush(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inbox, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete:
                break;
            case R.id.action_settings:
                if (Google.getInstance().getAccountMajor().equals(DumeUtils.STUDENT)) {
                    startActivity(new Intent(this, StudentSettingsActivity.class));
                } else if (Google.getInstance().getAccountMajor().equals(DumeUtils.TEACHER)) {
                    startActivity(new Intent(this, AccountSettings.class));
                }
                break;
            case R.id.action_mute:
                break;
            case R.id.action_starred:
                break;
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void findView() {
        myAppbarLayout = findViewById(R.id.app_bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);
        myToolbar = findViewById(R.id.accountToolbar);

    }

    @Override
    public void initInbox() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());
                currentTabPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void configInbox() {
        contactFromIntent = new Intent(InboxActivity.this, ContactActivity.class);
    }

    @Override
    public void onBackPressed() {
        if (myToolbar.getTitle().equals("Inbox")) {
            super.onBackPressed();
        } else {
            recordsRecyAda.unSelectAllItem();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4321:
                if (resultCode == InboxActivity.RESULT_OK) {
                    switch (mViewPager.getCurrentItem()) {
                        case 0:
                            PlaceholderFragment messageFragment = Google.getInstance().getMessageFragment();
                            if (messageFragment != null) {
                                messageFragment.loadAgain();
                            }
                            break;
                    }
                }
                break;
        }
    }

    protected void animateFab(int position) {
        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and
                fab.setImageDrawable(ContextCompat.getDrawable(InboxActivity.this, iconIntArray[position]));
                // Scale up animation
                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView inboxRecyclerView;
        private InboxActivity myThisActivity;
        private int position = -1;
        private View rootView;
        private RecyclerView inboxRecycler;
        private RecyclerView inboxRecyclerRecent;
        private CircleProgressbar selectedIndicatorCirPro;
        private carbon.widget.ImageView chatUserDP;
        private CircleProgressbar onlineIndicatorCirPro;
        private CircleProgressbar offlineIndicatorCirPro;
        private TextView chatUserName;
        private carbon.widget.ImageView chatUserDP1;
        public DemoModel demoModel;
        private Context context;
        private InboxCallAdapter callRecyAda;
        private InboxNotiAdapter notiRecyAda;
        private LinearLayout noDataBlockMsg;
        private LinearLayout noDataBlockCall;
        private LinearLayout noDataBlockNoti;
        private TextView noDataBlockTV;


        public PlaceholderFragment() {

        }

        @Override
        public void onAttach(Context context) {
            this.context = context;
            myThisActivity = (InboxActivity) getActivity();
            super.onAttach(context);
            demoModel = new DemoModel(context);
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            if (sectionNumber == 1) {
                Google.getInstance().setMessageFragment(fragment);
            }
            return fragment;
        }


        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                if (getArguments() != null && myThisActivity != null) {
                    position = Objects.requireNonNull(getArguments()).getInt(ARG_SECTION_NUMBER);
                    switch (position) {
                        case 1:
                            myThisActivity.showProgress();
                            demoModel.getRoom(FirebaseAuth.getInstance().getUid(), new TeacherContract.Model.Listener<List<Room>>() {
                                @Override
                                public void onSuccess(List<Room> list) {
                                    myThisActivity.hideProgress();
                                    if (list.size() < 1) {
                                        myThisActivity.flush("No Message History Found");
                                        return;
                                    }
                                    List<String> foo = new ArrayList<>();
                                    for (Room room : list) {
                                        foo.add(room.getRoomId());
                                    }
                                    Google.getInstance().setRoomIdList(foo);
                                    Google.getInstance().setRooms(list);
                                    Log.w(TAG, "onSuccess: " + list.toString());
                                    inboxRecycler = rootView.findViewById(R.id.inbox_recycler_view);
                                    recordsRecyAda = new InboxChatAdapter(myThisActivity, list) {
                                        @Override
                                        void OnItemClicked(View v, int position) {
                                            Google.getInstance().setCurrentRoom(list.get(position).getRoomId());
                                            selectedIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                                            chatUserDP = v.findViewById(R.id.chat_user_display_pic);
                                            onlineIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                                            offlineIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                                            chatUserName = v.findViewById(R.id.chat_user_name);

                                            Pair[] pairsPending = new Pair[5];
                                            pairsPending[0] = new Pair<View, String>(selectedIndicatorCirPro, "tn0ne");
                                            pairsPending[1] = new Pair<View, String>(chatUserDP, "tnTwo");
                                            pairsPending[2] = new Pair<View, String>(onlineIndicatorCirPro, "tnThree");
                                            pairsPending[3] = new Pair<View, String>(offlineIndicatorCirPro, "tnFour");
                                            pairsPending[4] = new Pair<View, String>(chatUserName, "tnFive");
                                            ActivityOptions optionsPending = null;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                optionsPending = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairsPending);
                                            }
                                            if (optionsPending != null) {
                                                startActivity(new Intent(myThisActivity, ChatActivity.class).setAction("testing"), optionsPending.toBundle());
                                            } else {
                                                startActivity(new Intent(myThisActivity, ChatActivity.class).setAction("testing"));
                                            }

                                        }

                                        @Override
                                        void OnItemLongClicked(View v, int position) {
                                            Toast.makeText(myThisActivity, "Inbox Chat long click", Toast.LENGTH_SHORT).show();
                                            chatUserDP1 = v.findViewById(R.id.chat_user_display_pic);
                                            chatUserDP1.setHeight((int) (44 * myThisActivity.getResources().getDisplayMetrics().density));
                                            chatUserDP1.setWidth((int) (44 * myThisActivity.getResources().getDisplayMetrics().density));
                                            configToolbarTittle(myThisActivity, "1");
                                        }
                                    };
                                    inboxRecycler.setAdapter(recordsRecyAda);
                                    inboxRecycler.setLayoutManager(new LinearLayoutManager(myThisActivity));
                                    if (list.size() <= 0) {
                                        noDataBlockMsg.setVisibility(View.VISIBLE);
                                    } else {
                                        noDataBlockMsg.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onError(String msg) {
                                    myThisActivity.flush(msg);
                                    myThisActivity.hideProgress();
                                }
                            });
                            break;
                    }
                }
            } else {
                //nothing to do here
            }
        }


        //enam in upppercase 😄 😂 😄 😂 😄 😂   😄    😂 😄   😂 😄 😂 
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            myThisActivity = (InboxActivity) getActivity();
            position = Objects.requireNonNull(getArguments()).getInt(ARG_SECTION_NUMBER);
            Log.w(TAG, "onCreateView: " + context == null ? "NullFucker" : "Not Null Dear");

            switch (position) {
                case 1:
                    rootView = inflater.inflate(R.layout.common1_fragment_default_inbox, container, false);
                    noDataBlockMsg = rootView.findViewById(R.id.no_data_block);
                    Log.w(TAG, "onCreateView: Switch Case :" + position);
                    myThisActivity.showProgress();
                    demoModel.getRoom(FirebaseAuth.getInstance().getUid(), new TeacherContract.Model.Listener<List<Room>>() {
                        @Override
                        public void onSuccess(List<Room> list) {

                            myThisActivity.hideProgress();
                            if (list.size() < 1) {
                                noDataBlockMsg.setVisibility(View.VISIBLE);
                                return;
                            }
                            List<String> foo = new ArrayList<>();
                            for (Room room : list) {
                                foo.add(room.getRoomId());
                            }
                            Google.getInstance().setRoomIdList(foo);
                            Google.getInstance().setRooms(list);
                            Log.w(TAG, "onSuccess: " + list.toString());
                            inboxRecycler = rootView.findViewById(R.id.inbox_recycler_view);
                            recordsRecyAda = new InboxChatAdapter(myThisActivity, list) {
                                @Override
                                void OnItemClicked(View v, int position) {
                                    Google.getInstance().setCurrentRoom(list.get(position).getRoomId());
                                    selectedIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                                    chatUserDP = v.findViewById(R.id.chat_user_display_pic);
                                    onlineIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                                    offlineIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                                    chatUserName = v.findViewById(R.id.chat_user_name);

                                    Pair[] pairsPending = new Pair[5];
                                    pairsPending[0] = new Pair<View, String>(selectedIndicatorCirPro, "tn0ne");
                                    pairsPending[1] = new Pair<View, String>(chatUserDP, "tnTwo");
                                    pairsPending[2] = new Pair<View, String>(onlineIndicatorCirPro, "tnThree");
                                    pairsPending[3] = new Pair<View, String>(offlineIndicatorCirPro, "tnFour");
                                    pairsPending[4] = new Pair<View, String>(chatUserName, "tnFive");
                                    ActivityOptions optionsPending = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        optionsPending = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairsPending);
                                    }
                                    if (optionsPending != null) {
                                        startActivity(new Intent(myThisActivity, ChatActivity.class).setAction("testing"), optionsPending.toBundle());
                                    } else {
                                        startActivity(new Intent(myThisActivity, ChatActivity.class).setAction("testing"));
                                    }

                                }

                                @Override
                                void OnItemLongClicked(View v, int position) {
                                    Toast.makeText(myThisActivity, "Inbox Chat long click", Toast.LENGTH_SHORT).show();
                                    chatUserDP1 = v.findViewById(R.id.chat_user_display_pic);
                                    chatUserDP1.setHeight((int) (44 * myThisActivity.getResources().getDisplayMetrics().density));
                                    chatUserDP1.setWidth((int) (44 * myThisActivity.getResources().getDisplayMetrics().density));
                                    configToolbarTittle(myThisActivity, "1");
                                }
                            };
                            inboxRecycler.setAdapter(recordsRecyAda);
                            inboxRecycler.setLayoutManager(new LinearLayoutManager(myThisActivity));
                            if (list.size() <= 0) {
                                noDataBlockMsg.setVisibility(View.VISIBLE);
                            } else {
                                noDataBlockMsg.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            //myThisActivity.flush(msg);
                            myThisActivity.hideProgress();
                        }
                    });
                    //setting the recycler view
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.common1_fragment_inbox_notification, container, false);
                    inboxRecyclerRecent = rootView.findViewById(R.id.inbox_recycler_view_recent);
                    noDataBlockNoti = rootView.findViewById(R.id.no_data_block);
                    demoModel.getNotification(FirebaseAuth.getInstance().getUid(), new TeacherContract.Model.Listener<List<InboxNotiData>>() {
                        @Override
                        public void onSuccess(List<InboxNotiData> list) {
                            if (notiRecyAda != null && notiRecyAda.getItemCount() != list.size()) {
                                notiRecyAda = new InboxNotiAdapter(myThisActivity, list);
                                inboxRecyclerRecent.setAdapter(notiRecyAda);
                                inboxRecyclerRecent.setLayoutManager(new LinearLayoutManager(myThisActivity));
                                if (list.size() <= 0) {
                                    noDataBlockNoti.setVisibility(View.VISIBLE);
                                } else {
                                    noDataBlockNoti.setVisibility(View.GONE);
                                }
                            } else if (notiRecyAda == null) {
                                notiRecyAda = new InboxNotiAdapter(myThisActivity, list);
                                inboxRecyclerRecent.setAdapter(notiRecyAda);
                                inboxRecyclerRecent.setLayoutManager(new LinearLayoutManager(myThisActivity));
                                if (list.size() <= 0) {
                                    noDataBlockNoti.setVisibility(View.VISIBLE);
                                } else {
                                    noDataBlockNoti.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            //Toast.makeText(myThisActivity, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.common1_fragment_default_inbox, container, false);
                    inboxRecycler = rootView.findViewById(R.id.inbox_recycler_view);
                    noDataBlockCall = rootView.findViewById(R.id.no_data_block);
                    noDataBlockTV = rootView.findViewById(R.id.no_item_text);
                    noDataBlockTV.setText("Sorry, no call to show right now...");

                    demoModel.getPhoneNumberList(new TeacherContract.Model.Listener<List<InboxCallData>>() {
                        @Override
                        public void onSuccess(List<InboxCallData> list) {
                            callRecyAda = new InboxCallAdapter(myThisActivity, list);
                            inboxRecycler.setAdapter(callRecyAda);
                            inboxRecycler.setLayoutManager(new LinearLayoutManager(myThisActivity));
                            if (list.size() <= 0) {
                                noDataBlockCall.setVisibility(View.VISIBLE);
                            } else {
                                noDataBlockCall.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            //Toast.makeText(myThisActivity, msg, Toast.LENGTH_SHORT).show();
                            noDataBlockCall.setVisibility(View.VISIBLE);
                        }
                    });
                    break;
            }
            return rootView;
        }

        public void loadAgain() {
            myThisActivity.showProgress();
            demoModel.getRoom(FirebaseAuth.getInstance().getUid(), new TeacherContract.Model.Listener<List<Room>>() {
                @Override
                public void onSuccess(List<Room> list) {
                    myThisActivity.hideProgress();
                    if (list.size() < 1) {
                        myThisActivity.flush("No Message History Found");
                        return;
                    }
                    List<String> foo = new ArrayList<>();
                    for (Room room : list) {
                        foo.add(room.getRoomId());
                    }
                    Google.getInstance().setRoomIdList(foo);
                    Google.getInstance().setRooms(list);
                    Log.w(TAG, "onSuccess: " + list.toString());
                    inboxRecycler = rootView.findViewById(R.id.inbox_recycler_view);
                    recordsRecyAda = new InboxChatAdapter(myThisActivity, list) {
                        @Override
                        void OnItemClicked(View v, int position) {
                            Google.getInstance().setCurrentRoom(list.get(position).getRoomId());
                            selectedIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                            chatUserDP = v.findViewById(R.id.chat_user_display_pic);
                            onlineIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                            offlineIndicatorCirPro = v.findViewById(R.id.selected_indicator);
                            chatUserName = v.findViewById(R.id.chat_user_name);

                            Pair[] pairsPending = new Pair[5];
                            pairsPending[0] = new Pair<View, String>(selectedIndicatorCirPro, "tn0ne");
                            pairsPending[1] = new Pair<View, String>(chatUserDP, "tnTwo");
                            pairsPending[2] = new Pair<View, String>(onlineIndicatorCirPro, "tnThree");
                            pairsPending[3] = new Pair<View, String>(offlineIndicatorCirPro, "tnFour");
                            pairsPending[4] = new Pair<View, String>(chatUserName, "tnFive");
                            ActivityOptions optionsPending = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                optionsPending = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pairsPending);
                            }
                            if (optionsPending != null) {
                                startActivity(new Intent(myThisActivity, ChatActivity.class).setAction("testing"), optionsPending.toBundle());
                            } else {
                                startActivity(new Intent(myThisActivity, ChatActivity.class).setAction("testing"));
                            }

                        }

                        @Override
                        void OnItemLongClicked(View v, int position) {
                            Toast.makeText(myThisActivity, "Inbox Chat long click", Toast.LENGTH_SHORT).show();
                            chatUserDP1 = v.findViewById(R.id.chat_user_display_pic);
                            chatUserDP1.setHeight((int) (44 * myThisActivity.getResources().getDisplayMetrics().density));
                            chatUserDP1.setWidth((int) (44 * myThisActivity.getResources().getDisplayMetrics().density));
                            configToolbarTittle(myThisActivity, "1");
                        }
                    };
                    inboxRecycler.setAdapter(recordsRecyAda);
                    inboxRecycler.setLayoutManager(new LinearLayoutManager(myThisActivity));
                    if (list.size() <= 0) {
                        noDataBlockMsg.setVisibility(View.VISIBLE);
                    } else {
                        noDataBlockMsg.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(String msg) {
                    myThisActivity.flush(msg);
                    myThisActivity.hideProgress();
                }
            });
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
            return 3;
        }
    }
}
