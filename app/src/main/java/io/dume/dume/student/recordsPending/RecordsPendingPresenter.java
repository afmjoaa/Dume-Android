package io.dume.dume.student.recordsPending;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class RecordsPendingPresenter implements RecordsPendingContract.Presenter {

    private RecordsPendingContract.View mView;
    private RecordsPendingContract.Model mModel;
    private Context context;
    private Activity activity;

    public RecordsPendingPresenter(Context context, RecordsPendingContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (RecordsPendingContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void recordsPendingEnqueue() {

    }

    @Override
    public void onRecordsPendingIntracted(View view) {

    }
}
