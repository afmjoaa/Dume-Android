package io.dume.dume.teacher.skill;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.teacher.adapters.SkillAdapter;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.VisibleToggleClickListener;

public class SkillActivity extends AppCompatActivity implements SkillContract.View, View.OnClickListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);
        ButterKnife.bind(this);
        presenter = new SkillPresenter(new SkillModel(), this);
        presenter.enqueue();
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
        fabInstant.setEnabled(false);

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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
