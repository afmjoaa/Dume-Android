package io.dume.dume.student.studentSettings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import io.dume.dume.R;
import io.dume.dume.student.common.SettingData;
import io.dume.dume.student.common.SettingsAdapter;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.profilePage.ProfilePageActivity;

import java.util.ArrayList;
import java.util.List;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class StudentSettingsActivity extends CustomStuAppCompatActivity
        implements StudentSettingsContract.View {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity4_settings);
        setActivityContext(this, fromFlag);
        mPresenter = new StudentSettingsPresenter(this, new StudentSettingsModel());
        mPresenter.studentSettingsEnqueue();
        configureAppbar(this, "Settings");


        //setting the recycler view
        settingsAdapter = new SettingsAdapter(this, getFinalData()) {
            @Override
            protected void OnButtonClicked(View v, int position) {
                settingsContent.setVisibility(View.GONE);
                switch (position) {
                    case 0:
                        Toast.makeText(StudentSettingsActivity.this, "0", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.content, new NotificationPreferenceFragment()).commit();
                        break;
                    case 1:
                        getFragmentManager().beginTransaction().replace(R.id.content, new NotificationPreferenceFragment()).commit();
                        break;
                    case 2:
                        getFragmentManager().beginTransaction().replace(R.id.content, new DataSyncPreferenceFragment()).commit();
                        break;
                    case 3:
                        Toast.makeText(StudentSettingsActivity.this, "3", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(StudentSettingsActivity.this, "4", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(StudentSettingsActivity.this, "5", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(StudentSettingsActivity.this, "6", Toast.LENGTH_SHORT).show();
                        break;
                }
                configAppbarTittle(StudentSettingsActivity.this, settingNameArr[position]);
                appBarLayout.setExpanded(false);
            }
        };
        settingsRecycleView.setAdapter(settingsAdapter);
        settingsRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public void findView() {
        settingNameArr = getResources().getStringArray(R.array.settingsHeader);
        settingsRecycleView = findViewById(R.id.Testing_fuck_recycler);
        settingsContent = findViewById(R.id.settings_content);
        appBarLayout = findViewById(R.id.app_bar);
        basicInfoLayout1 = findViewById(R.id.basic_info_layout);
    }

    @Override
    public void gotoProfilePage() {
        startActivity(new Intent(this, ProfilePageActivity.class));
    }

    @Override
    public void initStudentSettings() {

    }

    @Override
    public void configStudentSettings() {

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
                R.drawable.ic_settings_notifications,
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
}
