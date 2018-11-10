package io.dume.dume.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.util.DumeUtils;

public abstract class BaseActivity extends AppCompatActivity {


    @BindView(R.id.baseLoad)
    HorizontalLoadView loadView;
    @BindView(R.id.baseContainer)
    NestedScrollView container;
    @BindView(R.id.baseFloatingAB)
    FloatingActionButton fab;
    @BindView(R.id.baseWrapper)
    CoordinatorLayout wrapper;

    private String activityTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        ButterKnife.bind(this);
        DumeUtils.configureAppbar(this, activityTitle());


    }

    public abstract String activityTitle();

    public void setContentToBase(int layoutId) {
        if (layoutId == 0) throw new Error("Layout may not be initialized");
        LayoutInflater.from(this).inflate(layoutId, container, true);

    }
}