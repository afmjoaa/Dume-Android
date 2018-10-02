package io.dume.dume.student.studentSettings;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class StudentSettingsPresenter implements StudentSettingsContract.Presenter {

    private StudentSettingsContract.View mView;
    private StudentSettingsContract.Model mModel;
    private Context context;
    private Activity activity;

    public StudentSettingsPresenter(Context context, StudentSettingsContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (StudentSettingsContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void studentSettingsEnqueue() {
        mView.findView();
        mView.initStudentSettings();
        mView.configStudentSettings();

    }

    @Override
    public void onStudentSettingsIntracted(View view) {
        switch (view.getId()) {
            case R.id.basic_info_layout:
                mView.gotoProfilePage();
                break;
        }
    }
}
