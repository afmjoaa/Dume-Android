package io.dume.dume.bootCamp.student_addvertise;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;


public class StudentAddvertisePresenter implements StudentAddvertiseContact.Presenter {
    private StudentAddvertiseContact.View mView;
    private StudentAddvertiseContact.Model mModel;
    private Context context;
    private Activity activity;

    public StudentAddvertisePresenter(Context context, StudentAddvertiseContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (StudentAddvertiseContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void studentAddvertiseEnqueue() {
        mView.findView();
        mView.initStudentAddvertise();
        mView.configStudentAddvertise();
    }

    @Override
    public void onStudentAddvertiseViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.enhance_skill_imageview:
                mView.onAnimationImage();
                break;
        }
    }
}
