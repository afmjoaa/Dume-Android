package io.dume.dume.student.searchLoading;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

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
        mView.findView();
        mView.initSearchLoading();
        mView.configSearchLoading();
    }

    @Override
    public void onSearchLoadingIntracted(View view) {
        switch (view.getId()) {
            case R.id.loading_cancel_btn:
                mView.gotoSearchResult();
                break;
            /*case R.id.package_search_btn:
                mView.executeSearchActivity();
                break;*/

        }
    }
}
