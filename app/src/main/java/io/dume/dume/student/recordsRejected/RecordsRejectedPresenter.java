package io.dume.dume.student.recordsRejected;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class RecordsRejectedPresenter implements RecordsRejectedContract.Presenter {

    private RecordsRejectedContract.View mView;
    private RecordsRejectedContract.Model mModel;
    private Context context;
    private Activity activity;

    public RecordsRejectedPresenter(Context context, RecordsRejectedContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (RecordsRejectedContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void recordsRejectedEnqueue() {

    }

    @Override
    public void onRecordsRejectedIntracted(View view) {

    }
}
