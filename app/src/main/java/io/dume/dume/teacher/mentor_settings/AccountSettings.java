package io.dume.dume.teacher.mentor_settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.model.DumeModel;
import io.dume.dume.student.common.SettingData;
import io.dume.dume.student.common.SettingsAdapter;
import io.dume.dume.student.heatMap.AccountRecyData;
import io.dume.dume.student.heatMap.HeatMapAccountRecyAda;
import io.dume.dume.student.homePage.HomePageActivity;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.teacher.adapters.BasicInfoAdapter;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.model.KeyValueModel;
import io.dume.dume.util.AlertMsgDialogue;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;

public class AccountSettings extends BaseAppCompatActivity implements AccountSettingsContract.MentorView {
    private CoordinatorLayout transitionContainer;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private HorizontalLoadView horizontalLoadView;
    private AccountSettingsContract.Presenter presenter;

    private RecyclerView basicRecyclerView;
    private Animation slideDown;
    private LinearLayout profileContainer, profileSec;
    private ImageView indicator;
    private FrameLayout frameLayout;
    private static final String TAG = "AccountSettings";
    private ToolTipsManager toolTipsManager;
    private TextView profileSecTxtm, userName, userPhone, userMail;
    private Map<String, Object> data;
    private carbon.widget.ImageView avatarIV;

    private RecyclerView joarBaccha;
    private SettingsAdapter settingsAdapter;
    private String[] settingNameArr;
    private NestedScrollView scrollView;
    private AppBarLayout appBarLayout;
    private static final int fromFlag = 287;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        setActivityContext(this,fromFlag);
        presenter = new AccountSettingsPresenter(this, new AccountSettingsModel());
        presenter.enqueue();
        presenter.loadData();
        findLoadView();
    }

    @Override
    public void setViewConfig() {
        DumeUtils.configureAppbar(this, "Account Settings");
        horizontalLoadView = findViewById(R.id.loadView);
        transitionContainer = findViewById(R.id.parent_coor_layout);
        profileContainer = findViewById(R.id.profileSection);
        indicator = findViewById(R.id.indicatorImg);
        frameLayout = findViewById(R.id.framLayout);
        basicRecyclerView = findViewById(R.id.basicInfoRecyclerView);
        joarBaccha = findViewById(R.id.joaarBacca);
        userName = findViewById(R.id.userNameTV);
        userMail = findViewById(R.id.userMailTV);
        userPhone = findViewById(R.id.userPhoneTV);
        profileSec = findViewById(R.id.profile_container);
        avatarIV = findViewById(R.id.avatarImageView);
        scrollView = findViewById(R.id.accountScrolling);

        appBarLayout = findViewById(R.id.settingsAppbar);
        basicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        basicRecyclerView.setNestedScrollingEnabled(false);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        toolTipsManager = new ToolTipsManager();
        settingNameArr = getResources().getStringArray(R.array.settingsHeader_mentor);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateUserInfo(Map<String, Object> mData) {
        this.data = mData;
        userPhone.setText(data.get("phone_number").toString());
        userMail.setText(data.get("email").toString());
        userName.setText(data.get("first_name").toString() + " " + data.get("last_name").toString());
        ArrayList<KeyValueModel> keyValueModels = new ArrayList<>();
        keyValueModels.add(new KeyValueModel("Gender", data.get("gender").toString()));
        keyValueModels.add(new KeyValueModel("Marital Status", data.get("marital").toString()));
        keyValueModels.add(new KeyValueModel("Religion", data.get("religion").toString()));
        basicRecyclerView.setAdapter(new BasicInfoAdapter(keyValueModels));
        final RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.avatar);
        Glide.with(AccountSettings.this).load(data.get("avatar") == null ? "" : data.get("avatar").toString()).apply(requestOptions).into(avatarIV);
        profileSec.setAlpha(1.0f);
    }


    @Override
    public void showLoading() {
        if (!horizontalLoadView.isRunningAnimation()) {
            horizontalLoadView.setVisibility(View.VISIBLE);
            horizontalLoadView.startLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (horizontalLoadView.isRunningAnimation()) {
            horizontalLoadView.stopLoading();
            horizontalLoadView.setVisibility(View.GONE);
        }
    }

    @Override
    public void gatherDataInListView(ArrayList<String> datalist) {

    }


    @Override
    public void showBasicInfo() {
        TransitionManager.beginDelayedTransition(transitionContainer, new TransitionSet().addTransition(new ChangeBounds().setDuration(200)).addTransition(new Recolor()).addTransition(new Rotate()));
        basicRecyclerView.setVisibility(View.VISIBLE);
        indicator.setRotation(180);
        frameLayout.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
    }

    @Override
    public void hideBasicInfo() {
        TransitionManager.beginDelayedTransition(transitionContainer, new TransitionSet().addTransition(new ChangeBounds().setDuration(200)).addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                TransitionManager.beginDelayedTransition(transitionContainer, new Recolor());
                frameLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shadow_background));
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
        }).addTransition(new Rotate()));
        basicRecyclerView.setVisibility(View.GONE);
        indicator.setRotation(0);


    }

    @Override
    public boolean isBasicInfoShowing() {
        return basicRecyclerView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void toast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    @Override
    public void editAccount() {
        Bundle bundle = new Bundle();
        bundle.putString("avatar", data.get("avatar") == null ? "" : data.get("avatar").toString());
        bundle.putString("religion", data.get("religion").toString());
        bundle.putString("first_name", data.get("first_name").toString());
        bundle.putString("last_name", data.get("last_name").toString());
        bundle.putString("marital", data.get("marital").toString());
        bundle.putString("gender", data.get("gender").toString());
        bundle.putString("phone_number", data.get("phone_number").toString());
        bundle.putString("email", data.get("email").toString());
        bundle.putString("birth_date", data.get("birth_date").toString());
        startActivity(new Intent(this, EditAccount.class).putExtra("user_data", bundle));
    }



    public List<SettingData> getFinalData() {
        List<SettingData> data = new ArrayList<>();
        int[] imageIcons = {
                R.drawable.ic_search_settings,
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

    @Override
    public void initJoaaRV() {
        joarBaccha.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingsAdapter = new SettingsAdapter(this, getFinalData()) {
            @SuppressLint("RestrictedApi")
            @Override
            protected void OnButtonClicked(View v, int position) {
                switch (position) {
                    case 0:
                        scrollView.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        configAppbarTittle(AccountSettings.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountSettings.NotificationPreferenceFragment()).commit();
                        break;
                    case 1:
                        scrollView.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);
                        configAppbarTittle(AccountSettings.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountSettings.AccountFragment()).commit();
                        break;
                    case 2:
                        toast("Chat settings are coming soon");
                        break;
                    case 3:
                        Toast.makeText(AccountSettings.this, "Coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        inviteAFriendCalled();
                        break;
                    case 5:
                        onSignOut();
                        break;
                }

            }
        };
        joarBaccha.setAdapter(settingsAdapter);
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

    public void onViewClicked(View view) {
      /*  ToolTip.Builder builder = new ToolTip.Builder(this, view, transitionContainer, "Tip message", ToolTip.POSITION_ABOVE);
        toolTipsManager.show(builder.build());*/
        presenter.onViewClicked(view);
    }

    //Account Fragment here
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AccountFragment extends Fragment {
        private AccountSettings myMainActivity;
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

        @Override
        public void onAttach(Context context) {
            this.context = context;
            super.onAttach(context);

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

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (AccountSettings) getActivity();
            View rootView = inflater.inflate(R.layout.stu_setting_account_fragment, container, false);
            accountChangerRecycler = rootView.findViewById(R.id.account_changer_recycler);
            accountTypeArr = myMainActivity.getResources().getStringArray(R.array.AccountType);

            heatMapAccountRecyAda = new HeatMapAccountRecyAda(myMainActivity, getFinalData(1)) {
                @Override
                protected void OnAccouItemClicked(View v, int position) {
                    if(position == 2){
                        toast("Bootcamp service is under development...");
                    }else if(position ==1 ){
                        toast("You are already inside your mentor profile. Press back to navigate to homepage...");
                    }else{
                        Bundle Uargs = new Bundle();
                        Uargs.putString("msg", "Switching to " + accountTypeArr[position]);
                        AlertMsgDialogue accountChangerAlertDialogue = new AlertMsgDialogue();
                        accountChangerAlertDialogue.setItemChoiceListener((dialogInterface, i) -> {
                            heatMapAccountRecyAda.update(getFinalData(position));
                            String toChange = accountTypeArr[position].equals("Mentor") ? DumeUtils.TEACHER : accountTypeArr[position].equals("Student") ? DumeUtils.STUDENT : accountTypeArr[position];
                            if (toChange.equals("Boot Camp")) {
                                toast("Bootcamp service is under development.");
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

        public void toast(String msg) {
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);
            toast.show();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), AccountSettings.class));

            }
            return super.onOptionsItemSelected(item);
        }

    }

    //testing TODO
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        private SharedPreferences.Editor editor;
        private Context context;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            this.context = context;
            editor = context.getSharedPreferences(DumeUtils.SETTING_PREFERENCE, MODE_PRIVATE).edit();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);
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

            Preference searchRadiusPreference = findPreference("pref_key_login_settings");
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceScreen.removePreference(searchRadiusPreference);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), AccountSettings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
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
                //preference.getTitle();
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


}
