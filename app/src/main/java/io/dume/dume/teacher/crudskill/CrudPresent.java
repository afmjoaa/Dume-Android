package io.dume.dume.teacher.crudskill;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.R;

public class CrudPresent implements CrudContract.Presenter {
    private CrudModel model;
    private CrudContract.View view;

    public CrudPresent(CrudModel model, CrudContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void enqueue() {
        view.findView();
        view.init();
        List<Integer> iconList = new ArrayList<>();
        iconList.add(R.drawable.education);
        iconList.add(R.drawable.software);
        iconList.add(R.drawable.programming);
        iconList.add(R.drawable.language);
        iconList.add(R.drawable.dance);
        iconList.add(R.drawable.art);
        iconList.add(R.drawable.cooking);
        iconList.add(R.drawable.music);
        iconList.add(R.drawable.sewing);
        view.setUpRecyclerView(model.getCategories(), iconList);


    }

    @Override
    public void onViewInteracted(View mView) {

    }
}
