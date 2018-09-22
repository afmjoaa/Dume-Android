package io.dume.dume.student.recordsCompleted;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class RecordsCompletedPresenter implements  RecordsCompletedContract.Presenter {

    private RecordsCompletedContract.View mView;
    private RecordsCompletedContract.Model mModel;
    private Context context;
    private Activity activity;

    public RecordsCompletedPresenter(Context context, RecordsCompletedContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (RecordsCompletedContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void recordsCompletedEnqueue() {
        mView.findView();
        mView.initRecordsCompleted();
        mView.configRecordsCompleted();

    }

    @Override
    public void onRecordsCompletedIntracted(View view) {

    }
}
