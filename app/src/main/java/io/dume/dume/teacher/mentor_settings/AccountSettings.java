package io.dume.dume.teacher.mentor_settings;

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

import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Recolor;
import com.transitionseverywhere.Rotate;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;

import io.dume.dume.R;
import io.dume.dume.custom_view.HorizontalLoadView;
import io.dume.dume.student.homePage.MapsActivity;
import io.dume.dume.teacher.adapters.BasicInfoAdapter;
import io.dume.dume.teacher.mentor_settings.academic.AcademicActivity;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.model.KeyValueModel;
import io.dume.dume.util.DumeUtils;

public class AccountSettings extends AppCompatActivity implements AccountSettingsContract.MentorView {
    private CoordinatorLayout transitionContainer;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private HorizontalLoadView horizontalLoadView;
    private AccountSettingsContract.Presenter presenter;

    private RecyclerView basicRecyclerView, badgeRV;
    private Animation slideDown;
    private LinearLayout profileContainer;
    private ImageView indicator;
    private FrameLayout frameLayout;
    private static final String TAG = "AccountSettings";
    private ToolTipsManager toolTipsManager;
    private TextView profileSecTxt;
    private RecyclerView academicRV;


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
        basicRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        basicRecyclerView.setNestedScrollingEnabled(false);
        slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        toolTipsManager = new ToolTipsManager();
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
        ArrayList<KeyValueModel> keyValueModels = new ArrayList<>();
        keyValueModels.add(new KeyValueModel("Age", "20"));
        keyValueModels.add(new KeyValueModel("Sex", "Male"));
        keyValueModels.add(new KeyValueModel("Marital Status", "Unmarried"));
        keyValueModels.add(new KeyValueModel("Nationality", "Bangladesh"));
        keyValueModels.add(new KeyValueModel("Religion", "Islam"));
        basicRecyclerView.setAdapter(new BasicInfoAdapter(keyValueModels));
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
        startActivity(new Intent(this, EditAccount.class));
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
        ToolTip.Builder builder = new ToolTip.Builder(this, view, transitionContainer, "Tip message", ToolTip.POSITION_ABOVE);
        toolTipsManager.show(builder.build());
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
