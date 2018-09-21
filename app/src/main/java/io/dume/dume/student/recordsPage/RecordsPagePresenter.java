package io.dume.dume.student.recordsPage;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class RecordsPagePresenter implements RecordsPageContract.Presenter {

    private RecordsPageContract.View mView;
    private RecordsPageContract.Model mModel;
    private Context context;
    private Activity activity;

    public RecordsPagePresenter(Context context, RecordsPageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (RecordsPageContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void recordsPageEnqueue() {
        mView.findView();
        mView.initRecordsPage();
        mView.configRecordsPage();

    }

    @Override
    public void onRecordsPageIntracted(View view) {

    }
}
