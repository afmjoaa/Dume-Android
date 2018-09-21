package io.dume.dume.student.recordsAccepted;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class RecordsAcceptedPresenter implements RecordsAcceptedContract.Presenter {

    private RecordsAcceptedContract.View mView;
    private RecordsAcceptedContract.Model mModel;
    private Context context;
    private Activity activity;

    public RecordsAcceptedPresenter(Context context, RecordsAcceptedContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (RecordsAcceptedContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void recordsAcceptedEnqueue() {
        mView.findView();
        mView.initRecordsAccepted();
        mView.configRecordsAccepted();

    }

    @Override
    public void onRecordsAcceptedIntracted(View view) {

    }
}
