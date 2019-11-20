package io.dume.dume.teacher.skill;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.teacher.adapters.SkillAdapter;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.teacher.homepage.TeacherActivtiy;
import io.dume.dume.teacher.homepage.TeacherDataStore;
import io.dume.dume.teacher.mentor_settings.basicinfo.EditAccount;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.NetworkUtil;

public class SkillActivity extends BaseAppCompatActivity implements SkillContract.View, View.OnClickListener {
    @BindView(R.id.loadView)
    HorizontalLoadView loadView;
    @BindView(R.id.skillRV)
    RecyclerView skillRV;
    private SkillContract.Presenter presenter;
    private LinearLayout noDataBlock;
    private com.getbase.floatingactionbutton.FloatingActionButton fabGang;
    private com.getbase.floatingactionbutton.FloatingActionButton fabRegular;
    private FloatingActionsMenu multiFab;
    private FrameLayout viewMusk;
    private NestedScrollView nestedScrollView;
    private com.getbase.floatingactionbutton.FloatingActionButton fabInstant;
    private SkillAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar enamSnackbar;
    private int ADD_PARMANENT_ADDRESS = 1005;
    private Map<String, Object> documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        setActivityContext(this, fromFlag);
        ButterKnife.bind(this);
        presenter = new SkillPresenter(new SkillModel(this), this, this);
        presenter.enqueue();
        documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
        //setting my snackbar callback
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                multiFab.setTranslationY(0);
                multiFab.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    multiFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
            }

            @Override
            public void onShown(Snackbar snackbar) {
                multiFab.setTranslationY(-30 * mDensity);
                multiFab.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    multiFab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                }
            }
        });

        enamSnackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                multiFab.setTranslationY(0);
            }

            @Override
            public void onShown(Snackbar snackbar) {
                multiFab.setTranslationY(-30 * mDensity);
            }
        });
        changeAddSkillBtnColor();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeAddSkillBtnColor();
    }

    private void changeAddSkillBtnColor() {
        /*final Map<String, Boolean> achievements = (Map<String, Boolean>) documentSnapshot.get("achievements");
        Boolean premier = achievements.get("premier");
        if (premier) {
            fabInstant.setColorNormalResId(R.color.colorBlack);
            fabInstant.setImageDrawable(getResources().getDrawable(R.drawable.dume_instant_image));
        } else {
            fabInstant.setColorNormalResId(R.color.recordsBgColor);
            fabInstant.setImageDrawable(getResources().getDrawable(R.drawable.dume_instant_grayscale_image));
        }*/
        documentSnapshot = TeacherDataStore.getInstance().getDocumentSnapshot();
        String beh = (String) documentSnapshot.get("pro_com_%");
        int percentage = Integer.parseInt(beh);
        if (percentage >= 95) {
            fabRegular.setColorNormalResId(R.color.colorBlack);
            fabRegular.setImageDrawable(getResources().getDrawable(R.drawable.dume_regular_image));
            fabGang.setColorNormalResId(R.color.colorBlack);
            fabGang.setImageDrawable(getResources().getDrawable(R.drawable.dume_gang_image));
            fabInstant.setColorNormalResId(R.color.colorBlack);
            fabInstant.setImageDrawable(getResources().getDrawable(R.drawable.dume_instant_image));
        } else {
            fabRegular.setColorNormalResId(R.color.recordsBgColor);
            fabRegular.setImageDrawable(getResources().getDrawable(R.drawable.dume_regular_grayscale_image));
            fabGang.setColorNormalResId(R.color.recordsBgColor);
            fabGang.setImageDrawable(getResources().getDrawable(R.drawable.dume_gang_grayscale_image));
            fabInstant.setColorNormalResId(R.color.recordsBgColor);
            fabInstant.setImageDrawable(getResources().getDrawable(R.drawable.dume_instant_grayscale_image));
        }
    }

    @Override
    public void init() {
        DumeUtils.configureAppbar(this, "Skill Management");
        noDataBlock = findViewById(R.id.no_data_block);
        multiFab = findViewById(R.id.multiple_actions);
        fabRegular = findViewById(R.id.fab_regular);
        fabGang = findViewById(R.id.fab_gang);
        fabInstant = findViewById(R.id.fab_instant);
        viewMusk = findViewById(R.id.view_musk);
        nestedScrollView = findViewById(R.id.hosting_nestedScroll_layout);

        coordinatorLayout = findViewById(R.id.parent_coor_layout);
        enamSnackbar = Snackbar.make(coordinatorLayout, "Replace with your own action", Snackbar.LENGTH_LONG);
        //initial error fix
        multiFab.collapse();
        fabRegular.setVisibility(View.GONE);
        fabGang.setVisibility(View.GONE);
        fabInstant.setVisibility(View.GONE);
        adapter = new SkillAdapter(SkillAdapter.ACTIVITY);
        skillRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        skillRV.setAdapter(adapter);
        TransitionSet set1 = new TransitionSet()
                .addTransition(new Fade())
                .setInterpolator(new LinearOutSlowInInterpolator());
        TransitionManager.beginDelayedTransition(viewMusk, set1);
        //TransitionManager.beginDelayedTransition(multiFab, set1);
        multiFab.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                viewMusk.setVisibility(View.VISIBLE);
                fabRegular.setVisibility(View.VISIBLE);
                fabGang.setVisibility(View.VISIBLE);
                fabInstant.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                viewMusk.setVisibility(View.INVISIBLE);
                viewMusk.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewMusk.setVisibility(View.GONE);
                        fabRegular.setVisibility(View.GONE);
                        fabGang.setVisibility(View.GONE);
                        fabInstant.setVisibility(View.GONE);
                    }
                }, 400L);
            }
        });
    }

    @Override
    public void showLoading() {
        if (!loadView.isRunningAnimation()) {
            loadView.setVisibility(View.VISIBLE);
            loadView.startLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (loadView.isRunningAnimation()) {
            loadView.setVisibility(View.GONE);
            loadView.stopLoading();
        }
    }

    @Override
    public void flush(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }


    @Override
    public void onClick(View view) {
        presenter.onViewInteracted(view);
    }

    @Override
    public void goToCrudActivity(String action) {
        Intent intent = new Intent(this, CrudSkillActivity.class).setAction(action);
        startActivity(intent);
    }

    @Override
    public void loadSkillRV(ArrayList<Skill> list) {
        TeacherDataStore.getInstance().setSkillArrayList(list);
        adapter.update(list);
        if (list.size() == 0) {
            noDataBlock.setVisibility(View.VISIBLE);
        } else {
            noDataBlock.setVisibility(View.GONE);
        }
    }

    @Override
    public void performMultiFabClick() {
        multiFab.collapse();
    }

    @Override
    public void defaultSnak(String message, String actionName) {
    }

    @Override
    public void showSnack(String message, String actionName) {
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) enamSnackbar.getView();
        TextView textView = (TextView) layout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        LayoutInflater inflater = LayoutInflater.from(this);
        View snackView = inflater.inflate(R.layout.teachers_snakbar_layout, null);
        // layout.setBackgroundColor(R.color.red);
        TextView textViewStart = snackView.findViewById(R.id.custom_snackbar_text);
        textViewStart.setText(message);
        TextView actionTV = snackView.findViewById(R.id.actionTV);
        actionTV.setTextColor(getResources().getColor(R.color.snack_action));
        actionTV.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditAccount.class));
        });
        actionTV.setText(actionName);
        layout.setPadding(0, 0, 0, 0);
        CoordinatorLayout.LayoutParams parentParams = (CoordinatorLayout.LayoutParams) layout.getLayoutParams();
        parentParams.height = (int) (30 * (getResources().getDisplayMetrics().density));
       /* parentParams.setAnchorId(R.id.Secondary_toolbar);
        parentParams.anchorGravity = Gravity.BOTTOM;*/
        layout.setLayoutParams(parentParams);
        layout.addView(snackView, 0);
        int status = NetworkUtil.getConnectivityStatusString(this);
        enamSnackbar.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADD_PARMANENT_ADDRESS) {

            }
        }
    }

    public void showProgress() {
        if (loadView.getVisibility() == View.INVISIBLE || loadView.getVisibility() == View.GONE) {
            loadView.setVisibility(View.VISIBLE);
        }
        if (!loadView.isRunningAnimation()) {
            loadView.startLoading();
        }
    }

    public void hideProgress() {
        if (loadView.isRunningAnimation()) {
            loadView.stopLoading();
        }
        if (loadView.getVisibility() == View.VISIBLE) {
            loadView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (isTaskRoot()) {
                startActivity(new Intent(this, TeacherActivtiy.class));
                finish();
            } else {
                super.onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(this, TeacherActivtiy.class));
            finish();
        } else {
            super.onBackPressed();
        }
    }

}
