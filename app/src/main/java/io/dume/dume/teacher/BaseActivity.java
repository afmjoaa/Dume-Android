package io.dume.dume.teacher;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.widget.NestedScrollView;

import android.view.LayoutInflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.util.DumeUtils;

public abstract class BaseActivity extends BaseAppCompatActivity {

    private static final int fromFlag = 31;
    private static final String TAG = "BaseActivity";

    @BindView(R.id.baseContainer)
    NestedScrollView container;
    @BindView(R.id.baseFloatingAB)
    FloatingActionButton fab;
    @BindView(R.id.parent_coor_layout)
    CoordinatorLayout wrapper;

    private String activityTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        setActivityContext(this, fromFlag);
        findLoadView();
        ButterKnife.bind(this);
        DumeUtils.configureAppbar(this, activityTitle());
    }

    public abstract String activityTitle();

    public void setContentToBase(int layoutId) {
        if (layoutId == 0) throw new Error("Layout may not be initialized");
        LayoutInflater.from(this).inflate(layoutId, container, true);
    }
}