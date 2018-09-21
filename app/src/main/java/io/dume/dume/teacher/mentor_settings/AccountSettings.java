package io.dume.dume.teacher.mentor_settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.dume.dume.R;
import io.dume.dume.custom_view.HorizontalLoadView;
import io.dume.dume.student.homepage.MapsActivity;
import io.dume.dume.teacher.adapters.BasicInfoAdapter;
import io.dume.dume.teacher.mentor_settings.academic.AcademicActivity;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.model.KeyValueModel;
import io.dume.dume.teacher.pojo.Education;
import io.dume.dume.util.DumeUtils;

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
        horizontalLoadView = findViewById(R.id.horizontalLoading);
        transitionContainer = findViewById(R.id.transitionContainer);
        profileContainer = findViewById(R.id.profileSection);
        indicator = findViewById(R.id.indicatorImg);
        frameLayout = findViewById(R.id.framLayout);
        basicRecyclerView = findViewById(R.id.basicInfoRecyclerView);
        academicRV = findViewById(R.id.academic_list);
        badgeRV = findViewById(R.id.badgeRecyclerView);

        userName = findViewById(R.id.userNameTV);
        userMail = findViewById(R.id.userMailTV);
        userPhone = findViewById(R.id.userPhoneTV);
        profileSec = findViewById(R.id.profile_container);
        academicError = findViewById(R.id.itemSubTextView);
        avatarIV = findViewById(R.id.avatarImageView);
        basicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        basicRecyclerView.setNestedScrollingEnabled(false);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        toolTipsManager = new ToolTipsManager();

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateUserInfo(Map<String, Object> mData) {
        this.data = mData;
        userPhone.setText(data.get("phone_number").toString());
        userMail.setText(data.get("email").toString());
        userName.setText(data.get("first_name").toString() + " " + data.get("last_name").toString());
        ArrayList<KeyValueModel> keyValueModels = new ArrayList<>();
        keyValueModels.add(new KeyValueModel("Sex", data.get("gender").toString()));
        keyValueModels.add(new KeyValueModel("Marital Status", data.get("marital").toString()));
        keyValueModels.add(new KeyValueModel("Religion", data.get("religion").toString()));
        basicRecyclerView.setAdapter(new BasicInfoAdapter(keyValueModels));
        Glide.with(this).load(data.get("avatar") == null ? "" : data.get("avatar").toString()).apply(new RequestOptions().override(100, 100)).into(avatarIV);
        profileSec.setAlpha(1.0f);

    }

    @Override
    public void updatAcademicList(ArrayList<Education> data) {
        if (data != null) {
            if (!data.isEmpty()) {
                academicRV.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.VERTICAL, false));
                academicRV.setAdapter(new EducationAdapter(data));
            }
        } else academicError.setVisibility(View.VISIBLE);
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
    public void setUpBadge() {
        badgeRV.setLayoutManager(new LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false));
        badgeRV.setAdapter(new BadgeAdapter());
    }

    @Override
    public void setUpAcademic() {

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

    @Override
    public void addAcademic() {
        startActivity(new Intent(this, AcademicActivity.class).setAction("edit"));
    }


    public void onViewClicked(View view) {
      /*  ToolTip.Builder builder = new ToolTip.Builder(this, view, transitionContainer, "Tip message", ToolTip.POSITION_ABOVE);
        toolTipsManager.show(builder.build());*/
        presenter.onViewClicked(view);
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
        holder.degree.setText("("+educationArrayList.get(position).getDegree()+")");
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
