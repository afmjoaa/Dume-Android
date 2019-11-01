package io.dume.dume.teacher.addskill;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;

import io.dume.dume.teacher.BasePresenter;

public class AddSkillPresenter extends BasePresenter implements AddSkillContract.Presenter {

    private AddSkillContract.View mView;

    private Context mContext;

    public AddSkillPresenter(@NonNull Context context, @NonNull AddSkillContract.View view) {
        this.mView = view;
        this.mContext = context;
        this.mView.setPresenter(this);
    }

    @Override
    public void onViewInteracted(View view) {

    }

    @Override
    public void enqueue() {

    }

    @Override
    public void stop() {

    }
}
