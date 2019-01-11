package io.dume.dume.teacher.mentor_settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.preference.RingtonePreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
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

import de.hdodenhof.circleimageview.CircleImageView;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;
import io.dume.dume.R;
import io.dume.dume.auth.auth.AuthActivity;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.common.SettingData;
import io.dume.dume.student.common.SettingsAdapter;
import io.dume.dume.student.heatMap.AccountRecyData;
import io.dume.dume.student.heatMap.HeatMapAccountRecyAda;
import io.dume.dume.student.homePage.MapsActivity;
import io.dume.dume.student.studentSettings.SavedPlacesAdaData;
import io.dume.dume.student.studentSettings.SavedPlacesAdapter;
import io.dume.dume.student.studentSettings.StudentSettingsActivity;
import io.dume.dume.teacher.adapters.BasicInfoAdapter;
import io.dume.dume.teacher.mentor_settings.academic.AcademicActivity;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.model.KeyValueModel;
import io.dume.dume.teacher.pojo.Education;
import io.dume.dume.util.AlertMsgDialogue;
import io.dume.dume.util.DumeUtils;

import static io.dume.dume.util.DumeUtils.configAppbarTittle;

public class AccountSettings extends AppCompatActivity implements AccountSettingsContract.MentorView {
    private CoordinatorLayout transitionContainer;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private HorizontalLoadView horizontalLoadView;
    private AccountSettingsContract.Presenter presenter;

    private RecyclerView basicRecyclerView, badgeRV;
    private Animation slideDown;
    private LinearLayout profileContainer, profileSec;
    private ImageView indicator;
    private FrameLayout frameLayout;
    private static final String TAG = "AccountSettings";
    private ToolTipsManager toolTipsManager;
    private TextView profileSecTxtm, userName, userPhone, userMail;
    private RecyclerView academicRV;
    private Map<String, Object> data;
    private CircleImageView avatarIV;

    private TextView academicError;
    private RecyclerView joarBaccha;
    private SettingsAdapter settingsAdapter;
    private String[] settingNameArr;
    private NestedScrollView scrollView;
    private AppBarLayout appBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        presenter = new AccountSettingsPresenter(this, new AccountSettingsModel());
        presenter.enqueue();
        presenter.loadData();

    }

    @Override
    public void setViewConfig() {
        DumeUtils.configureAppbar(this, "Account Settings");
        horizontalLoadView = findViewById(R.id.loadView);
        transitionContainer = findViewById(R.id.transitionContainer);
        profileContainer = findViewById(R.id.profileSection);
        indicator = findViewById(R.id.indicatorImg);
        frameLayout = findViewById(R.id.framLayout);
        basicRecyclerView = findViewById(R.id.basicInfoRecyclerView);
        academicRV = findViewById(R.id.academic_list);
        badgeRV = findViewById(R.id.badgeRecyclerView);
        joarBaccha = findViewById(R.id.joaarBacca);
        userName = findViewById(R.id.userNameTV);
        userMail = findViewById(R.id.userMailTV);
        userPhone = findViewById(R.id.userPhoneTV);
        profileSec = findViewById(R.id.profile_container);
        academicError = findViewById(R.id.itemSubTextView);
        avatarIV = findViewById(R.id.avatarImageView);
        scrollView = findViewById(R.id.accountScrolling);

        appBarLayout = findViewById(R.id.settingsAppbar);
        basicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        basicRecyclerView.setNestedScrollingEnabled(false);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        toolTipsManager = new ToolTipsManager();
        settingNameArr = getResources().getStringArray(R.array.settingsHeader);

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
        Glide.with(this).load(data.get("avatar") == null ? "" : data.get("avatar").toString()).apply(new RequestOptions().override(100, 100)).into(avatarIV);
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
    public void toast(String toast) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();

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
        startActivity(new Intent(this, EditAccount.class).putExtra("user_data", bundle));
    }

    @Override
    public void addLocation() {
        startActivity(new Intent(this, MapsActivity.class));
    }

    @Override
    public void updateLocation() {

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
                        configAppbarTittle(AccountSettings.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        Toast.makeText(AccountSettings.this, "0", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountSettings.NotificationPreferenceFragment()).commit();
                        break;
                    case 1:
                        scrollView.setVisibility(View.GONE);

                        configAppbarTittle(AccountSettings.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountSettings.SavedPlacesFragment()).commit();
                        break;
                    case 2:
                        scrollView.setVisibility(View.GONE);
                        configAppbarTittle(AccountSettings.this, settingNameArr[position]);
                        appBarLayout.setExpanded(false);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AccountSettings.AccountFragment()).commit();
                        break;
                    case 3:
                        Toast.makeText(AccountSettings.this, "Chat settings are coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(AccountSettings.this, "Coming soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        inviteAFriendCalled();
                        break;
                    case 6:

                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), AuthActivity.class));

                    }

                    break;
                }

            }
        };
        joarBaccha.setAdapter(settingsAdapter);
    }

    private void inviteAFriendCalled(){
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
        } catch(Exception e) {
            Log.e(TAG, "inviteAFriendCalled: "+ e.toString());
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
            myMainActivity = (AccountSettings) getActivity();
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
                startActivity(new Intent(getActivity(), AccountSettings.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    }

    //Saved places fragment here
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SavedPlacesFragment extends Fragment{

        private AccountSettings myMainActivity;
        private RecyclerView savedPlacesRecycler;
        private SavedPlacesAdapter savedPlacesAdapter;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            myMainActivity = (AccountSettings) getActivity();
            View rootView = inflater.inflate(R.layout.stu_setting_saved_places_fragment, container, false);
            savedPlacesRecycler = rootView.findViewById(R.id.saved_place_recycler);
            //testing code goes here
            List<SavedPlacesAdaData> recordDataCurrent = new ArrayList<>();
            savedPlacesAdapter = new SavedPlacesAdapter(myMainActivity, recordDataCurrent);
            savedPlacesRecycler.setAdapter(savedPlacesAdapter);
            savedPlacesRecycler.setLayoutManager(new LinearLayoutManager(myMainActivity));
            return rootView;
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

    //testing TODO
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

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

class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeImageHolder> {


    @NonNull
    @Override
    public BadgeImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View badgeImage = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_settings_badge_item, parent, false);
        return new BadgeImageHolder(badgeImage);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeImageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class BadgeImageHolder extends RecyclerView.ViewHolder {
        ImageView badgeImageView;

        BadgeImageHolder(View itemView) {
            super(itemView);
            badgeImageView = itemView.findViewById(R.id.badgeImageView);
        }
    }



}




class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.MyViewVH> {
    private ArrayList<Education> educationArrayList;

    public EducationAdapter(ArrayList<Education> educationArrayList) {
        this.educationArrayList = educationArrayList;
    }

    @NonNull
    @Override
    public MyViewVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.education_item, parent, false);
        return new MyViewVH(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewVH holder, int position) {
        holder.institution.setText(educationArrayList.get(position).getTitle());
        holder.degree.setText("(" + educationArrayList.get(position).getDegree() + ")");
        holder.duration.setText(new StringBuilder().append("[").append(educationArrayList.get(position).getFrom()).append("-").append(educationArrayList.get(position).getTo()).append("]").toString());
    }

    @Override
    public int getItemCount() {
        return educationArrayList.size();
    }

    class MyViewVH extends RecyclerView.ViewHolder {
        TextView institution, degree, description, duration;

        public MyViewVH(View itemView) {
            super(itemView);
            institution = itemView.findViewById(R.id.institutionTV);
            degree = itemView.findViewById(R.id.degreeNameTV);
            duration = itemView.findViewById(R.id.durationTV);
        }
    }

}
