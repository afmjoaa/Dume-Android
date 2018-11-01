package io.dume.dume.teacher.skill;

import android.view.View;

import io.dume.dume.R;

public class SkillPresenter implements SkillContract.Presenter {
    private SkillContract.Model model;
    private SkillContract.View view;

    public SkillPresenter(SkillContract.Model model, SkillContract.View view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void enqueue() {
        view.init();
    }

    @Override
    public void onViewInteracted(View element) {
        switch (element.getId()) {
            case R.id.fabAdd:
                view.goToCrudActivity("add");
        }
    }
}
