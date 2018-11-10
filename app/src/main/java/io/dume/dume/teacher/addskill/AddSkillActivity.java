package io.dume.dume.teacher.addskill;

import android.os.Bundle;
import android.widget.Button;

import io.dume.dume.R;
import io.dume.dume.teacher.BaseActivity;

//todo create BaseActivity and import to this class
public class AddSkillActivity extends BaseActivity implements AddSkillContract.View {
    AddSkillContract.Presenter mPresenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentToBase(R.layout.activity_add_skill);
        mPresenter = new AddSkillPresenter(this, this);
        ((Button) findViewById(R.id.button2)).setText("Joaa Rocks");
    }

    @Override
    public String activityTitle() {
        return "Add Skill Activity";
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.enqueue();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.stop();
    }


    @Override
    public void setPresenter(AddSkillContract.Presenter presenter) {

    }

    @Override
    public void init() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void flush(String message) {

    }
}
