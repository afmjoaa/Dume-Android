package io.dume.dume.student.studentHelp;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class StudentHelpPresenter implements StudentHelpContract.Presenter {

    private StudentHelpContract.View mView;
    private StudentHelpContract.Model mModel;
    private Context context;
    private Activity activity;

    public StudentHelpPresenter(Context context, StudentHelpContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (StudentHelpContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void studentHelpEnqueue() {
        mView.findView();
        mView.initStudentHelp();
        mView.configStudentHelp();
    }

    @Override
    public void onStudentHelpIntracted(View view) {

    }
}
