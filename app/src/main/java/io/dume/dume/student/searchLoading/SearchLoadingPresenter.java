package io.dume.dume.student.searchLoading;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import io.dume.dume.R;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;

public class SearchLoadingPresenter implements SearchLoadingContract.Presenter {

    private SearchLoadingContract.View mView;
    private SearchLoadingContract.Model mModel;
    private static final String TAG = "SearchLoadingPresenter";
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
        mView.saveToDB();

        mModel.search(SearchDataStore.getInstance().getAnchorPoint().latitude, SearchDataStore.getInstance().getAnchorPoint().longitude, 1000, SearchDataStore.getInstance().getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> list) {
                Log.e(TAG, "onSuccess: " + list.toString());
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG, "onError: " + msg);
            }
        });

    }

    @Override
    public void onSearchLoadingIntracted(View view) {
        switch (view.getId()) {
            case R.id.loading_cancel_btn:
                mView.cancelBtnClicked();
                break;
            case R.id.searching_imageView:
                mView.gotoSearchResult();
                break;
            /*case R.id.package_search_btn:
                mView.executeSearchActivity();
                break;*/

        }
    }
}
