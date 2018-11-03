package io.dume.dume.teacher.crudskill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.dume.dume.R;

import io.dume.dume.customView.HorizontalLoadView;
import io.dume.dume.teacher.adapters.CategoryAdapter;
import io.dume.dume.util.DumeUtils;

public class CrudSkillActivity extends AppCompatActivity implements CrudContract.View {
    @BindView(R.id.crudLoad)
    HorizontalLoadView loadView;
    private CrudContract.Presenter presenter;
    @BindView(R.id.categoryRV)
    RecyclerView categoryGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_skill);
        ButterKnife.bind(this);
        presenter = new CrudPresent(new CrudModel(), this);
        presenter.enqueue();
    }

    @Override
    public void init() {
        if (getIntent().getAction().equals("add")) {
            DumeUtils.configureAppbar(this, "Add New Skill");
        } else if (getIntent().getAction().equals("edit")) {
            DumeUtils.configureAppbar(this, "Edit Skill");
        }


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
    public void setUpRecyclerView(List<String> categoryList, List<Integer> drawableList) {
        categoryGrid.setLayoutManager(new GridLayoutManager(this, 3));
        categoryGrid.setAdapter(new CategoryAdapter(categoryList, drawableList));
    }
}
