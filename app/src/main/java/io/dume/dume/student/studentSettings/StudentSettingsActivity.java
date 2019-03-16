package io.dume.dume.student.studentSettings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.SettingData;
import io.dume.dume.student.common.SettingsAdapter;
import io.dume.dume.student.grabingLocation.GrabingLocationActivity;
import io.dume.dume.student.heatMap.AccountRecyData;
import io.dume.dume.student.heatMap.HeatMapAccountRecyAda;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.CustomStuAppCompatActivity;
import io.dume.dume.student.profilePage.ProfilePageActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.AlertMsgDialogue;
import io.dume.dume.util.DumeUtils;

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
    private int ADD_WORK_LOCATION = 1002;
    private int ADD_SAVED_PLACES = 1003;
    private int ADD_RECENT_PLACES = 1004;
    private static SavedPlacesAdapter savedPlacesAdapter;
    private static Map<String, Map<String, Object>> favorites;
    private static Map<String, Map<String, Object>> saved_places;
    private static Map<String, Map<String, Object>> recently_used;
    private ImageView userDP;
    private TextView userNameTV;
    private TextView userPhoneNumTV;
    private TextView userEmailTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stunav_activity4_settings);
        setActivityContext(this, fromFlag);
        findLoadView();
        mModel = new StudentSettingsModel(this);
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
                        getFragmentManager().beginTransaction().replace(R.id.content, new NotificationPreferenceFragment()).commit();
                        break;
                    case 1:
                        settingsContent.setVisibility(View.GONE);
                        fab.show();
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
        userDP = findViewById(R.id.user_display_pic);
        userNameTV = findViewById(R.id.user_name);
        userPhoneNumTV = findViewById(R.id.user_phone_num);
        userEmailTV = findViewById(R.id.user_email);
    }

    @Override
    public void initStudentSettings() {
        String avatarString = searchDataStore.getAvatarString();
        String userName = searchDataStore.getUserName();
        String phoneNum = searchDataStore.getUserNumber();
        String email = searchDataStore.getUserMail();

        if (avatarString != null) {
            if (!avatarString.equals("")) {
                setAvatar(avatarString);
            }
        }
        if (userName != null) {
            if (!userName.equals("")) {
                setUserName(userName);
            }
        }
        if (phoneNum != null) {
            if (!phoneNum.equals("")) {
                setPhoneNum(phoneNum);
            }
        }
        if (email != null) {
            if (!email.equals("")) {
                setEmail(email);
            }
        }
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
                    distributeResult(selectedLocation, "Home");
                }
            } else if (requestCode == ADD_WORK_LOCATION) {
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                if (selectedLocation != null) {
                    distributeResult(selectedLocation, "Work");
                }
            } else if (requestCode == ADD_SAVED_PLACES) {
                LatLng selectedLocation = data.getParcelableExtra("selected_location");
                String addressName = data.getStringExtra("addressName");
                GeoPoint location = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
                String secondaryText = getAddress(this, selectedLocation.latitude, selectedLocation.longitude);
                testingCustomDialogue(secondaryText, location, addressName);
            }
        }
    }

    private void distributeResult(LatLng selectedLocation, String Name) {
        showProgress();
        SavedPlacesAdaData current = new SavedPlacesAdaData();
        GeoPoint location = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
        String secondaryText = getAddress(this, selectedLocation.latitude, selectedLocation.longitude);
        current.primary_text = Name;
        current.secondary_text = secondaryText;
        current.location = location;
        savedPlacesAdapter.updateFav(Name, current);

        Map<String, Object> myMap = new HashMap<>();
        myMap.put("location", location);
        myMap.put("primary_text", Name);
        myMap.put("secondary_text", secondaryText);

        mModel.updateFavoritePlaces(Name, myMap, new TeacherContract.Model.Listener<Void>() {
            @Override
            public void onSuccess(Void list) {
                flush("Successfully Added");
                hideProgress();
            }

            @Override
            public void onError(String msg) {
                flush(msg);
                hideProgress();
            }
        });
    }

    public void testingCustomDialogue(String address, GeoPoint location, String addressName) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_add_saved_places_dialogue);
        dialog.setCanceledOnTouchOutside(false);

        //all find view here
        TextView title = dialog.findViewById(R.id.rating_secondary_text);
        AutoCompleteTextView nameTextView = dialog.findViewById(R.id.name_textView);
        AutoCompleteTextView addressTextView = dialog.findViewById(R.id.address_textView);
        Button discardBtn = dialog.findViewById(R.id.skip_btn);
        Button saveBtn = dialog.findViewById(R.id.save_btn);
        ImageView emptyNameFound = dialog.findViewById(R.id.empty_name_found);
        ImageView emptyAddressFound = dialog.findViewById(R.id.empty_address_found);
        addressTextView.setText(address);
        if (addressName != null) {
            if (!addressName.equals("")) {
                nameTextView.setText(addressName);
                nameTextView.setFocusable(false);
                nameTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        flush("Sorry !! This field can't be edited");
                    }
                });
            }
        }

        nameTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nameTextView.setHint(R.string.home_example);
                } else {
                    nameTextView.setHint("");
                }
            }
        });
        addressTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    addressTextView.setHint(R.string.address_example);
                } else {
                    addressTextView.setHint("");
                }
            }
        });
        nameTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyNameFound.setVisibility(View.GONE);
                } else {
                    emptyNameFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyNameFound.setVisibility(View.VISIBLE);
                }
            }
        });
        addressTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.equals("")) {
                    emptyAddressFound.setVisibility(View.GONE);
                } else {
                    emptyAddressFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    emptyAddressFound.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.show();

        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameTextView.getText().toString().equals("") || addressTextView.getText().toString().equals("")) {
                    flush("Field can't be kept empty");
                    if (nameTextView.getText().toString().equals("")) {
                        emptyNameFound.setVisibility(View.VISIBLE);
                    }
                    if (addressTextView.getText().toString().equals("")) {
                        emptyAddressFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    showProgress();
                    dialog.dismiss();
                    //save data to db and update view
                    Map<String, Object> myMap = new HashMap<>();
                    myMap.put("location", location);
                    myMap.put("primary_text", nameTextView.getText().toString());
                    myMap.put("secondary_text", addressTextView.getText().toString());
                    mModel.updateSavedPlaces(nameTextView.getText().toString(), myMap, new TeacherContract.Model.Listener<Void>() {
                        @Override
                        public void onSuccess(Void list) {
                            flush("Successfully saved");
                            SavedPlacesAdaData current = new SavedPlacesAdaData();
                            current.primary_text = nameTextView.getText().toString();
                            current.secondary_text = addressTextView.getText().toString();
                            current.location = location;
                            savedPlacesAdapter.updateSaved(nameTextView.getText().toString(), current);
                            hideProgress();
                        }

                        @Override
                        public void onError(String msg) {
                            flush(msg);
                            hideProgress();
                        }
                    });
                }
            }
        });
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
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                SharedPreferences.Editor editor = preference.getContext().getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE).edit();
                editor.putString(preference.getKey().toString(), index >= 0 ? listPreference.getEntries()[index].toString() : null);
                editor.apply();

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);
                    SharedPreferences.Editor editor = preference.getContext().getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE).edit();
                    editor.putString(preference.getKey().toString(), null);
                    editor.apply();

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                        SharedPreferences.Editor editor = preference.getContext().getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE).edit();
                        editor.putString(preference.getKey().toString(), null);
                        editor.apply();
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                        SharedPreferences.Editor editor = preference.getContext().getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE).edit();
                        editor.putString(preference.getKey().toString(), stringValue);
                        editor.apply();
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
                SharedPreferences.Editor editor = preference.getContext().getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE).edit();
                editor.putString(preference.getKey().toString(), stringValue);
                editor.apply();
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
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void fabClicked() {
        startActivityForResult(new Intent(context, GrabingLocationActivity.class).setAction("fromSPAS"), ADD_SAVED_PLACES);
        //testingCustomDialogue();
    }

    @Override
    public void setUserName(String name) {
        userNameTV.setText(name);
    }

    @Override
    public void setAvatar(String avatarString) {
        RequestOptions override = new RequestOptions().override(100, 100);
        override.placeholder(R.drawable.avatar);
        Glide.with(this).load(avatarString).apply(override).into(userDP);
    }

    @Override
    public void setPhoneNum(String phoneNum) {
        userPhoneNumTV.setText(phoneNum);
    }

    @Override
    public void setEmail(String email) {
        userEmailTV.setText(email);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {

        private Context context;
        private Activity activity;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
            this.activity = (Activity)context;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
            SharedPreferences.Editor editor = context.getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE).edit();

            bindPreferenceSummaryToValue(findPreference("notifications_ringtone"));
            bindPreferenceSummaryToValue(findPreference("reminder_ringtone"));
            bindPreferenceSummaryToValue(findPreference("search_radius"));

            //testing here
            SwitchPreference notificationSwitch = (SwitchPreference) findPreference("notifications_new_message");
            SwitchPreference reminderSwitch = (SwitchPreference) findPreference("reminder_new_message");

            notificationSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(notificationSwitch.isChecked()){
                        // Checked the switch programmatically
                        notificationSwitch.setChecked(false);
                    }else {
                        // Unchecked the switch programmatically
                        notificationSwitch.setChecked(true);
                    }
                    editor.putBoolean("notification", notificationSwitch.isChecked());
                    editor.apply();
                    return false;
                }
            });

            reminderSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    if(reminderSwitch.isChecked()){
                        // Checked the switch programmatically
                        reminderSwitch.setChecked(false);
                    }else {
                        // Unchecked the switch programmatically
                        reminderSwitch.setChecked(true);
                    }
                    editor.putBoolean("reminder", reminderSwitch.isChecked());
                    editor.apply();
                    return false;
                }
            });

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
        private Context context;

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
                    if (position == 2) {
                        toast("Bootcamp service is under development...");
                    } else if (position == 0) {
                        toast("You are already inside your student profile. Press back to navigate to homepage...");
                    } else {
                        Bundle Uargs = new Bundle();
                        Uargs.putString("msg", "Switching to " + accountTypeArr[position]);
                        AlertMsgDialogue accountChangerAlertDialogue = new AlertMsgDialogue();
                        accountChangerAlertDialogue.setItemChoiceListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                heatMapAccountRecyAda.update(getFinalData(position));
                                String toChange = accountTypeArr[position].equals("Mentor") ? DumeUtils.TEACHER : accountTypeArr[position].equals("Student") ? DumeUtils.STUDENT : accountTypeArr[position];
                                if (toChange.equals("Boot Camp")) {
                                    toast("Bootcamp service is under development...");
                                } else {
                                    Toast.makeText(myMainActivity, "Switching ...", Toast.LENGTH_SHORT).show();
                                    new DumeModel(context).switchAcount(toChange, new TeacherContract.Model.Listener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (toChange.equals(DumeUtils.TEACHER))
                                                gotoTeacherActivity();
                                            else if (toChange.equals(DumeUtils.STUDENT))
                                                gotoStudentActivity();
                                        }

                                        @Override
                                        public void onError(String msg) {
                                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
                        });
                        accountChangerAlertDialogue.setArguments(Uargs);
                        accountChangerAlertDialogue.show(myMainActivity.getSupportFragmentManager(), "accountChangerAlertDialogue");
                    }
                }
            };
            accountChangerRecycler.setAdapter(heatMapAccountRecyAda);
            accountChangerRecycler.setLayoutManager(new LinearLayoutManager(myMainActivity));
            return rootView;

        }

        public void gotoTeacherActivity() {
            Intent intent = new Intent(context, TeacherActivtiy.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ((Activity) context).finish();
        }


        public void gotoStudentActivity() {
            Intent intent = new Intent(context, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ((Activity) context).finish();

        }

        public void toast(String msg) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if (v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }

        @Override
        public void onAttach(Context context) {
            this.context = context;
            super.onAttach(context);
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

            myMainActivity.showProgress();
            myMainActivity.mPresenter.retriveSavedPlacesData(new TeacherContract.Model.Listener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot list) {
                    List<SavedPlacesAdaData> favoriteAdaData = new ArrayList<>();
                    List<SavedPlacesAdaData> savedAdaData = new ArrayList<>();
                    List<SavedPlacesAdaData> recentAdaData = new ArrayList<>();

                    favorites = (Map<String, Map<String, Object>>) list.get("favourite_places");
                    if (favorites != null && favorites.size() > 0) {
                        for (Map.Entry<String, Map<String, Object>> entry : favorites.entrySet()) {
                            if (entry.getKey().equals("home") || entry.getKey().equals("work")) {
                                SavedPlacesAdaData savedPlacesAdaData = new SavedPlacesAdaData();
                                savedPlacesAdaData.setPrimary_text((String) entry.getValue().get("primary_text"));
                                savedPlacesAdaData.setSecondary_text((String) entry.getValue().get("secondary_text"));
                                savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                                favoriteAdaData.add(savedPlacesAdaData);
                            }
                        }
                    }
                    saved_places = (Map<String, Map<String, Object>>) list.get("saved_places");
                    if (saved_places != null && saved_places.size() > 0) {
                        for (Map.Entry<String, Map<String, Object>> entry : saved_places.entrySet()) {
                            SavedPlacesAdaData savedPlacesAdaData = new SavedPlacesAdaData();
                            savedPlacesAdaData.setPrimary_text((String) entry.getValue().get("primary_text"));
                            savedPlacesAdaData.setSecondary_text((String) entry.getValue().get("secondary_text"));
                            savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                            savedAdaData.add(savedPlacesAdaData);
                        }
                    }
                    recently_used = (Map<String, Map<String, Object>>) list.get("recent_places");
                    if (recently_used != null && recently_used.size() > 0) {
                        for (Map.Entry<String, Map<String, Object>> entry : recently_used.entrySet()) {
                            SavedPlacesAdaData savedPlacesAdaData = new SavedPlacesAdaData();
                            savedPlacesAdaData.setPrimary_text((String) entry.getValue().get("primary_text"));
                            savedPlacesAdaData.setSecondary_text((String) entry.getValue().get("secondary_text"));
                            savedPlacesAdaData.setLocation((GeoPoint) entry.getValue().get("location"));
                            recentAdaData.add(savedPlacesAdaData);
                        }
                    }

                    savedPlacesAdapter = new SavedPlacesAdapter(myMainActivity, favoriteAdaData, savedAdaData, recentAdaData) {
                        @Override
                        void OnItemDeleteClicked(View v, int position, String identify) {
                            myMainActivity.showProgress();
                            if (position >= 1 && position <= 2) {
                                myMainActivity.mModel.deleteFavouritePlaces(identify, new TeacherContract.Model.Listener<Void>() {
                                    @Override
                                    public void onSuccess(Void list) {
                                        Toast.makeText(myMainActivity, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                        myMainActivity.hideProgress();
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        Toast.makeText(myMainActivity, msg, Toast.LENGTH_SHORT).show();
                                        myMainActivity.hideProgress();
                                    }
                                });
                            } else if (position >= 4 && position < (savedAdaData.size() + 4)) {
                                myMainActivity.mModel.deleteSavedPlaces(identify, new TeacherContract.Model.Listener<Void>() {
                                    @Override
                                    public void onSuccess(Void list) {
                                        Toast.makeText(myMainActivity, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                        myMainActivity.hideProgress();
                                    }

                                    @Override
                                    public void onError(String msg) {
                                        Toast.makeText(myMainActivity, msg, Toast.LENGTH_SHORT).show();
                                        myMainActivity.hideProgress();
                                    }
                                });
                            }
                        }
                    };
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

        private void flush(String msg) {
            Toast.makeText(myMainActivity, msg, Toast.LENGTH_SHORT).show();
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
