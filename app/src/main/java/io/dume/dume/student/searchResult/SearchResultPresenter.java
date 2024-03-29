package io.dume.dume.student.searchResult;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dume.dume.R;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;

public class SearchResultPresenter implements SearchResultContract.Presenter {

    private SearchResultContract.View view;
    private SearchResultContract.Model mModel;
    private Context context;
    private Activity activity;

    public SearchResultPresenter(Context context, SearchResultContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.view = (SearchResultContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void onMapLoaded() {
        List<DocumentSnapshot> resultList = SearchDataStore.getInstance().getResultList();
        if (resultList != null) {
            for (int i = 0; i < resultList.size(); i++) {
                DocumentSnapshot profile = resultList.get(i);
                view.pushProfile(profile, resultList.get(i).getId());
                //Log.w("foo", resultList.size() + "/searchResultEnqueue: " + profile.toString());
            }
        }
    }

    @Override
    public void searchResultEnqueue() {
        view.findView();
        view.initSearchResult();
        view.configSearchResult();
    }

    @Override
    public void onSearchResultIntracted(View element) {
        switch (element.getId()) {
            case R.id.fab:
                view.centerTheMapCamera();
                break;
            case R.id.requestBTN:
                view.showRequestDialogue();
                break;
            case R.id.swipe_left:
                view.onSwipeLeftRight(false);
                break;
            case R.id.swipe_right:
                view.onSwipeLeftRight(true);
                break;
        }
    }
}
