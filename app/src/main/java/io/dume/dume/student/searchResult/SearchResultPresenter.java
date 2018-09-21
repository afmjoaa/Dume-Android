package io.dume.dume.student.searchResult;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class SearchResultPresenter implements SearchResultContract.Presenter {

    private SearchResultContract.View mView;
    private SearchResultContract.Model mModel;
    private Context context;
    private Activity activity;

    public SearchResultPresenter(Context context, SearchResultContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (SearchResultContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void searchResultEnqueue() {
        mView.findView();
        mView.initSearchResult();
        mView.configSearchResult();
    }

    @Override
    public void onSearchResultIntracted(View view) {
        switch (view.getId()) {
            /*case R.id.package_search_btn:
                mView.executeSearchActivity();
                break;*/

        }
    }
}
