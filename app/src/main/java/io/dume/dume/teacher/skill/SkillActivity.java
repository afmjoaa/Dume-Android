package io.dume.dume.teacher.skill;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.teacher.adapters.SkillAdapter;
import io.dume.dume.teacher.crudskill.CrudSkillActivity;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.util.DumeUtils;

public class SkillActivity extends AppCompatActivity implements SkillContract.View, View.OnClickListener {
    @BindView(R.id.loadView)
    HorizontalLoadView loadView;
    @BindView(R.id.skillRV)
    RecyclerView skillRV;
    private SkillContract.Presenter presenter;
    private FloatingActionButton fabAdd;

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
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);
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
        skillRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        skillRV.setAdapter(new SkillAdapter(SkillAdapter.ACTIVITY,list));
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


}
