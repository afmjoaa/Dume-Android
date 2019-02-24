package io.dume.dume.student.recordsCurrent;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class RecordsCurrentPresenter implements RecordsCurrentContract.Presenter {

    private RecordsCurrentContract.View mView;
    private RecordsCurrentContract.Model mModel;
    private Context context;
    private Activity activity;

    public RecordsCurrentPresenter(Context context, RecordsCurrentContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (RecordsCurrentContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void recordsCurrentEnqueue() {
        mView.findView();
        mView.initRecordsCurrent();
        mView.configRecordsCurrent();
    }

    @Override
    public void onRecordsCurrentIntracted(View view) {
        switch (view.getId()) {
            /*case R.id.package_search_btn:
                mView.executeSearchActivity();
                break;*/

        }
    }
}
