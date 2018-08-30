package io.dume.dume.student.searchLoading;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class SearchLoadingPresenter implements SearchLoadingContract.Presenter {

    private SearchLoadingContract.View mView;
    private SearchLoadingContract.Model mModel;
    private Context context;
    private Activity activity;

    public SearchLoadingPresenter(Context context, SearchLoadingContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (SearchLoadingContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void searchLoadingEnqueue() {

    }

    @Override
    public void onSearchLoadingIntracted(View view) {

    }
}
