package io.dume.dume.student.studentHelp;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.common.appInfoActivity.AppInfoActivity;
import io.dume.dume.common.privacyPolicy.PrivacyPolicyActivity;
import io.dume.dume.student.common.SettingData;
import io.dume.dume.student.common.SettingsAdapter;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.studentSettings.SavedPlacesAdaData;
import io.dume.dume.student.studentSettings.SavedPlacesAdapter;
import io.dume.dume.student.studentSettings.StudentSettingsActivity;
import io.dume.dume.util.AlertMsgDialogue;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;
import static io.dume.dume.util.DumeUtils.configureAppbar;

public class StudentHelpActivity extends CustomStuAppCompatActivity implements StudentHelpContract.View {

    private StudentHelpContract.Presenter mPresenter;
    private static final int fromFlag = 12;
    private String[] helpNameArr;
    private RecyclerView helpRecyclerView;
    private AppBarLayout appBarLayout;
    private View helpContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity3_student_help);
        setActivityContext(this, fromFlag);
        mPresenter = new StudentHelpPresenter(this, new StudentHelpModel());
        mPresenter.studentHelpEnqueue();
        configureAppbar(this, "Help");

        //setting the recycler view
        SettingsAdapter helpAdapter = new SettingsAdapter(this, getFinalData()) {
            @Override
            protected void OnButtonClicked(View v, int position) {
                switch (position) {
                    case 0:
                        Toast.makeText(StudentHelpActivity.this, "Coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        helpContent.setVisibility(View.GONE);
                        configAppbarTittle(StudentHelpActivity.this, helpNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getFragmentManager().beginTransaction().replace(R.id.content, new NotificationPreferenceFragment()).commit();
                        break;
                    case 2:
                        helpContent.setVisibility(View.GONE);
                        configAppbarTittle(StudentHelpActivity.this, helpNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new FAQFragment()).commit();
                        break;
                    case 3:
                        helpContent.setVisibility(View.GONE);
                        configAppbarTittle(StudentHelpActivity.this, helpNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, new ContactUsFragment()).commit();
                        break;
                    case 4:
                        updateAppCalled();
                        break;
                    case 5:
                        startActivity(new Intent(StudentHelpActivity.this, PrivacyPolicyActivity.class).setAction("fromHelp"));
                        break;
                    case 6:
                        startActivity(new Intent(StudentHelpActivity.this, AppInfoActivity.class).setAction("fromHelp"));
                        break;
                }

            }
        };
        helpRecyclerView.setAdapter(helpAdapter);
        helpRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void updateAppCalled() {

        Bundle Uargs = new Bundle();
        Uargs.putString("msg", "Sorry! No update available.");
        AlertMsgDialogue updateAlertDialogue = new AlertMsgDialogue();
        updateAlertDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(StudentHelpActivity.this, "Ok", Toast.LENGTH_SHORT).show();
            }
        });
        updateAlertDialogue.setArguments(Uargs);
        updateAlertDialogue.show(getSupportFragmentManager(), "updateAlertDialogue");
    }

    @Override
    public void findView() {
        helpNameArr = getResources().getStringArray(R.array.helpHeader);
        helpRecyclerView = findViewById(R.id.help_recycler);
        appBarLayout = findViewById(R.id.app_bar);
        helpContent = findViewById(R.id.help_content);

    }

    @Override
    public void initStudentHelp() {

    }

    @Override
    public void configStudentHelp() {

    }

    public List<SettingData> getFinalData() {
        List<SettingData> data = new ArrayList<>();
        int[] imageIcons = {
                R.drawable.ic_help_whats_new,
                R.drawable.ic_help_feature,
                R.drawable.ic_help_faq,
                R.drawable.ic_help_contact_us,
                R.drawable.ic_sync,
                R.drawable.ic_help_privacy_policy,
                R.drawable.ic_help_app_info
        };

        for (int i = 0; i < helpNameArr.length && i < imageIcons.length; i++) {
            SettingData current = new SettingData();
            current.settingName = helpNameArr[i];
            current.settingIcon = imageIcons[i];
            data.add(current);
        }
        return data;
    }

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
                startActivity(new Intent(getActivity(), StudentHelpActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

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


    //testing the contact up
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ContactUsFragment extends Fragment {

        private StudentHelpActivity myMainActivity;
        private AutoCompleteTextView queryTextView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentHelpActivity) getActivity();
            View rootView = inflater.inflate(R.layout.custom_contact_up_fragment, container, false);
            queryTextView = rootView.findViewById(R.id.feedback_textview);

            queryTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        queryTextView.setHint("Please describe your problem");
                    } else {
                        queryTextView.setHint("Please describe your problem");
                    }
                }
            });
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), StudentHelpActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    //testing the faq here
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FAQFragment extends Fragment {

        private StudentHelpActivity myMainActivity;
        private WebView webView;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (StudentHelpActivity) getActivity();
            View rootView = inflater.inflate(R.layout.custom_faq_fragment, container, false);
            webView = rootView.findViewById(R.id.activity_main_webview);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl("https://www.google.com/");
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }else {
                    startActivity(new Intent(getActivity(), StudentHelpActivity.class));
                }
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


    }
}
