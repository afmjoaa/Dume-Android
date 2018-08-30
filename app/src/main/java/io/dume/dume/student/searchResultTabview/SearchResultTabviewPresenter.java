package io.dume.dume.student.searchResultTabview;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class SearchResultTabviewPresenter implements SearchResultTabviewContract.Presenter {

    private SearchResultTabviewContract.View mView;
    private SearchResultTabviewContract.Model mModel;
    private Context context;
    private Activity activity;

    public SearchResultTabviewPresenter(Context context, SearchResultTabviewContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (SearchResultTabviewContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void searchResultTabviewEnqueue() {

    }

    @Override
    public void onSearchResultTabviewIntracted(View view) {

    }
}
