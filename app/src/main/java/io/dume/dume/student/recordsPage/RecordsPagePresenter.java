package io.dume.dume.student.recordsPage;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import io.dume.dume.util.Google;
import io.dume.dume.teacher.homepage.TeacherContract;

public class RecordsPagePresenter implements RecordsPageContract.Presenter {

    private RecordsPageContract.View view;
    private RecordsPageContract.Model mModel;
    private Context context;
    private Activity activity;

    public RecordsPagePresenter(Context context, RecordsPageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.view = (RecordsPageContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void recordsPageEnqueue() {
        view.findView();
        view.initRecordsPage();
        view.configRecordsPage();


    }

    @Override
    public void recordsPageLoadData(TeacherContract.Model.Listener<Void> listener) {
        view.load();
        mModel.getRecords(new TeacherContract.Model.Listener<List<Record>>() {
            @Override
            public void onSuccess(List<Record> list) {
                view.stopLoad();
                Google.getInstance().setRecordList(list);
                view.onDataLoadFinsh();
                listener.onSuccess(null);
            }

            @Override
            public void onError(String msg) {
                view.stopLoad();
                view.flush(msg);
                listener.onError(msg);
            }
        });
    }

    @Override
    public void onRecordsPageIntracted(View element) {

    }
}
