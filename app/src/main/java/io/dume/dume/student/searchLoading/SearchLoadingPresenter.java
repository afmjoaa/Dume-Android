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

    private SearchLoadingContract.View view;
    private SearchLoadingContract.Model mModel;
    private static final String TAG = "SearchLoadingPresenter";
    private Context context;
    private Activity activity;
    private int level = 0;
    private final SearchDataStore searchDataStore;

    public SearchLoadingPresenter(Context context, SearchLoadingContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.view = (SearchLoadingContract.View) context;
        this.mModel = mModel;
        searchDataStore = SearchDataStore.getInstance();
    }

    @Override
    public void searchLoadingEnqueue() {
        view.findView();
        view.initSearchLoading();
        view.configSearchLoading();
        view.saveToDB();

        mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 2, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> list) {

                /*Found - Level 1*/
                if (list.size() >= 3) {
                    //view.flush("Found - Level 1");
                    searchDataStore.setLevelNum(1);
                    searchDataStore.setResultList(list);
                    view.showResultActivty();

                }/*Not Found - Level 1*/ else {
                    //view.flush("Not Found - Level 1");
                    view.notifyRadious(8);
                    mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 8, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {

                        @Override
                        public void onSuccess(List<DocumentSnapshot> list) {
                            /*Found - Level 2*/
                            if (list.size() >= 3) {
                                //view.flush("Found - Level 2");
                                searchDataStore.setLevelNum(2);
                                searchDataStore.setResultList(list);
                                view.showResultActivty();

                            }/*Not Found - Level 2*/ else {
                                //view.flush("Not Found - Level 2");
                                view.notifyRadious(16);
                                mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 16, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {

                                    @Override
                                    public void onSuccess(List<DocumentSnapshot> list) {
                                        /*Found - Level 3*/
                                        if (list.size() >= 3) {
                                            //view.flush("Found - Level 3");
                                            searchDataStore.setLevelNum(3);
                                            searchDataStore.setResultList(list);
                                            view.showResultActivty();

                                        }/*Not Found - Level 3*/ else {
                                            //view.flush("Not Found - Level 3");
                                            view.notifyRadious(32);
                                            mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 32, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {

                                                @Override
                                                public void onSuccess(List<DocumentSnapshot> list) {
                                                    /*Found - Level 4*/
                                                    if (list.size() >= 1) {
                                                        //view.flush("Found - Level 4");
                                                        searchDataStore.setLevelNum(4);
                                                        searchDataStore.setResultList(list);
                                                        view.showResultActivty();


                                                    }/*Not Found - Level 4*/ else {
                                                        //view.flush("Not Found - Level 4");
                                                        //show dialogue that no one available for that skill !!!
                                                        view.noResultDialogue();
                                                    }
                                                }

                                                @Override
                                                public void onError(String msg) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onError(String msg) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    });
                }
            }

            @Override
            public void onError(String msg) {
                Log.e(TAG, "onError: " + msg);
            }
        });

    }

    @Override
    public void onSearchLoadingIntracted(View element) {
        switch (element.getId()) {
            case R.id.loading_cancel_btn:
                view.cancelBtnClicked();
                break;
            case R.id.searching_imageView:
                view.gotoSearchResult();
                break;
            /*case R.id.package_search_btn:
                view.executeSearchActivity();
                break;*/

        }
    }
}
