package io.dume.dume.student.studentSettings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import io.dume.dume.R;
import io.dume.dume.student.common.SettingData;
import io.dume.dume.student.common.SettingsAdapter;
import io.dume.dume.student.heatMap.AccountRecyData;
import io.dume.dume.student.heatMap.HeatMapAccountRecyAda;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.profilePage.ProfilePageActivity;
import io.dume.dume.student.studentPayment.StudentPaymentActivity;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.AlertMsgDialogue;
import io.dume.dume.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbar;
import static io.dume.dume.util.DumeUtils.getAddress;

public class StudentSettingsActivity extends CustomStuAppCompatActivity
        implements StudentSettingsContract.View {
    private static final String TAG = "StudentSettingsActivity";
    private StudentSettingsContract.Presenter mPresenter;
    private static final int fromFlag = 11;
    private RecyclerView settingsRecycleView;
    private SettingsAdapter settingsAdapter;
    private View settingsContent;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private String[] settingNameArr;
    private AppBarLayout appBarLayout;
    private RelativeLayout basicInfoLayout;
    private RelativeLayout basicInfoLayout1;
    private FloatingActionButton fab;
    private StudentSettingsModel mModel;
    private int ADD_HOME_LOCATION = 1001;
    private static SavedPlacesAdapter savedPlacesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity4_settings);
        setActivityContext(this, fromFlag);
        findLoadView();
        mModel = new StudentSettingsModel(this, this);
        mPresenter = new StudentSettingsPresenter(this, mModel);
        mPresenter.studentSettingsEnqueue();
        configureAppbar(this, "Settings");

        //setting the recycler view
        settingsAdapter = new SettingsAdapter(this, getFinalData()) {
            @SuppressLint("RestrictedApi")
            @Override
            protected void OnButtonClicked(View v, int position) {
                switch (position) {
                    case 0:
                        settingsContent.setVisibility(View.GONE);
                        configAppbarTittle(StudentSettingsActivity.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        Toast.makeText(StudentSettingsActivity.this, "0", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.content, new NotificationPreferenceFragment()).commit();
                        break;
                    case 1:
                        settingsContent.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                        configAppbarTittle(StudentSettingsActivity.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new SavedPlacesFragment()).commit();
                        break;
                    case 2:
                        settingsContent.setVisibility(View.GONE);
                        configAppbarTittle(StudentSettingsActivity.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new AccountFragment()).commit();
                        break;
                    case 3:
                        Toast.makeText(StudentSettingsActivity.this, "Chat settings are coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(StudentSettingsActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        inviteAFriendCalled();
                        break;
                    case 6:
                        onSignOut();
                        break;
                }

            }
        };
        settingsRecycleView.setAdapter(settingsAdapter);
        settingsRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void inviteAFriendCalled() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TITLE, "Dume");
            String strShareMessage = "Check out Dume, It's simple just share your skill and earn money.Get it for free from\n\n";
            strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, strShareMessage);
            /*i.setType("image/png");
            Uri screenshotUri = Uri.parse("android.resource://io.dume.dume/drawable/avatar.png");
            i.putExtra(Intent.EXTRA_STREAM, screenshotUri);*/
            startActivity(Intent.createChooser(i, "Share via"));
        } catch (Exception e) {
            Log.e(TAG, "inviteAFriendCalled: " + e.toString());
        }
    }


    @Override
    public void findView() {
        settingNameArr = getResources().getStringArray(R.array.settingsHeader);
        settingsRecycleView = findViewById(R.id.Testing_fuck_recycler);
        settingsContent = findViewById(R.id.settings_content);
        appBarLayout = findViewById(R.id.app_bar);
        basicInfoLayout1 = findViewById(R.id.basic_info_layout);
        fab = findViewById(R.id.fab);
    }

    @Override
    public void initStudentSettings() {

    }

    @Override
    public void configStudentSettings() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
           if (requestCode == ADD_HOME_LOCATION) {
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                if (selectedLocation != null) {
                    SavedPlacesAdaData current = new SavedPlacesAdaData();
                    GeoPoint location = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
                    String secondaryText = getAddress(this, selectedLocation.latitude, selectedLocation.longitude);
                    current.primary_text = "Home";
                    current.secondary_text = secondaryText;
                    current.location = location;
                    savedPlacesAdapter.updateFav(current);

                    Map<String, Object> myMap = new HashMap<>();
                    myMap.put("location", location);
                    myMap.put("primary_text","Home" );
                    myMap.put("secondary_text",secondaryText );

                    mModel.updateFavoritePlaces("home",current, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            flush("on success ");
                        }

                        @Override
                        public void onError(String msg) {
                            flush(msg);

                        }
                    });

                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    public List<SettingData> getFinalData() {
        List<SettingData> data = new ArrayList<>();
        int[] imageIcons = {
                R.drawable.ic_search_settings,
                R.drawable.ic_star_border_black_24dp,
                R.drawable.ic_person_outline_black_24dp,
                R.drawable.ic_settings_chat,
                R.drawable.ic_settings_broadcasts,
                R.drawable.ic_settings_invite_a_friend,
                R.drawable.ic_settings_sign_out
        };

        for (int i = 0; i < settingNameArr.length && i < imageIcons.length; i++) {
            SettingData current = new SettingData();
            current.settingName = settingNameArr[i];
            current.settingIcon = imageIcons[i];
            data.add(current);
        }
        return data;
    }

    public void onStuSettingViewClicked(View view) {
        mPresenter.onStudentSettingsIntracted(view);
    }

    @Override
    public void gotoProfilePage() {
        startActivity(new Intent(this, ProfilePageActivity.class));
    }

    @Override
    public void flush(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    //Account Fragment here
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AccountFragment extends Fragment {
        private StudentSettingsActivity myMainActivity;
        private RecyclerView accountChangerRecycler;
        private int[] imageIcons = {
                R.drawable.ic_default_student_profile,
                R.drawable.ic_default_mentor_profile,
                R.drawable.ic_default_bootcamp_profile
        };
        private String[] accountTypeArr;
        private HeatMapAccountRecyAda heatMapAccountRecyAda;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentSettingsActivity) getActivity();
            View rootView = inflater.inflate(R.layout.stu_setting_account_fragment, container, false);
            accountChangerRecycler = rootView.findViewById(R.id.account_changer_recycler);
            accountTypeArr = myMainActivity.getResources().getStringArray(R.array.AccountType);

            //testing code goes here
            //setting the adapter with the recycler view
            heatMapAccountRecyAda = new HeatMapAccountRecyAda(myMainActivity, getFinalData(0)) {
                @Override
                protected void OnAccouItemClicked(View v, int position) {

                    Bundle Uargs = new Bundle();
                    Uargs.putString("msg", "Switching to " + accountTypeArr[position]);
                    AlertMsgDialogue accountChangerAlertDialogue = new AlertMsgDialogue();
                    accountChangerAlertDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(myMainActivity, "Switching ...", Toast.LENGTH_SHORT).show();
                            heatMapAccountRecyAda.update(getFinalData(position));
                        }
                    });
                    accountChangerAlertDialogue.setArguments(Uargs);
                    accountChangerAlertDialogue.show(myMainActivity.getSupportFragmentManager(), "accountChangerAlertDialogue");

                }
            };
            accountChangerRecycler.setAdapter(heatMapAccountRecyAda);
            accountChangerRecycler.setLayoutManager(new LinearLayoutManager(myMainActivity));
            return rootView;

        }

        public List<AccountRecyData> getFinalData(int selectedItem) {
            List<AccountRecyData> data = new ArrayList<>();
            for (int i = 0; i < accountTypeArr.length && i < imageIcons.length; i++) {
                AccountRecyData current = new AccountRecyData();
                current.accouName = accountTypeArr[i];
                current.iconId = imageIcons[i];
                current.selectedOne = selectedItem;
                data.add(current);
            }
            return data;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }

    //Saved places fragment here
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SavedPlacesFragment extends Fragment {

        private StudentSettingsActivity myMainActivity;
        private RecyclerView savedPlacesRecycler;
        private List<Map<String, Object>> favorites;
        private ArrayList<Map<String, Object>> saved_places;
        private ArrayList<Map<String, Object>> recently_used;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
            myMainActivity = (StudentSettingsActivity) getActivity();

        }

        @SuppressWarnings("unchecked")
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //myMainActivity = (StudentSettingsActivity) getActivity();
            View rootView = inflater.inflate(R.layout.stu_setting_saved_places_fragment, container, false);
            savedPlacesRecycler = rootView.findViewById(R.id.saved_place_recycler);
            //testing code goes here
            List<SavedPlacesAdaData> favoriteAdaData = new ArrayList<>();
            List<SavedPlacesAdaData> savedAdaData = new ArrayList<>();
            List<SavedPlacesAdaData> recentAdaData = new ArrayList<>();
            SavedPlacesAdaData savedPlacesAdaData = new SavedPlacesAdaData();

            myMainActivity.showProgress();
            myMainActivity.mPresenter.retriveSavedPlacesData(new TeacherContract.Model.Listener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot list) {
                    favorites = (List<Map<String, Object>>) list.get("favorite_places");
                    if (favorites != null && favorites.size() > 0) {
                        for (Map<String, Object> foo : favorites) {
                            savedPlacesAdaData.primary_text = (String) foo.get("primary_text");
                            savedPlacesAdaData.secondary_text = (String) foo.get("secondary_text");
                            savedPlacesAdaData.location = (GeoPoint) foo.get("location");
                            favoriteAdaData.add(savedPlacesAdaData);
                            GeoPoint geoPoint = savedPlacesAdaData.location;
                            Log.w(TAG, "onEvent: " + geoPoint.getLongitude());
                            //final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                            //final SavedPlacesAdaData savedPlacesAdaData = mapper.convertValue(foo, SavedPlacesAdaData.class);
                        }
                    }
                    saved_places = (ArrayList<Map<String, Object>>) list.get("saved_places");
                    if (saved_places != null && saved_places.size() > 0) {
                        for (Map<String, Object> foo : saved_places) {
                            savedPlacesAdaData.primary_text = (String) foo.get("primary_text");
                            savedPlacesAdaData.secondary_text = (String) foo.get("secondary_text");
                            savedPlacesAdaData.location = (GeoPoint) foo.get("location");
                            savedAdaData.add(savedPlacesAdaData);
                        }
                    }
                    recently_used = (ArrayList<Map<String, Object>>) list.get("recent_places");
                    if (recently_used != null && recently_used.size() > 0) {
                        for (Map<String, Object> foo : recently_used) {
                            savedPlacesAdaData.primary_text = (String) foo.get("primary_text");
                            savedPlacesAdaData.secondary_text = (String) foo.get("secondary_text");
                            savedPlacesAdaData.location = (GeoPoint) foo.get("location");
                            recentAdaData.add(savedPlacesAdaData);
                        }
                    }

                    savedPlacesAdapter = new SavedPlacesAdapter(myMainActivity, favoriteAdaData, savedAdaData, recentAdaData);
                    savedPlacesRecycler.setAdapter(savedPlacesAdapter);
                    savedPlacesRecycler.setLayoutManager(new LinearLayoutManager(myMainActivity));
                    myMainActivity.hideProgress();
                }

                @Override
                public void onError(String msg) {
                    Toast.makeText(myMainActivity, msg, Toast.LENGTH_SHORT).show();
                    myMainActivity.hideProgress();
                }
            });
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                myMainActivity.fab.hide();
                startActivity(new Intent(getActivity(), StudentSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }

}
