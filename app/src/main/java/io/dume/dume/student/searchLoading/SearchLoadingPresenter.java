package io.dume.dume.student.searchLoading;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    private List<DocumentSnapshot> sortedResult;
    private List<DocumentSnapshot> lastFiltered;
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
                    if (list.size() > 5) {
                        searchDataStore.setResultList(getFilteredResultList(list));
                    } else {
                        searchDataStore.setResultList(list);
                    }
                    view.showResultActivty();
                }/*Not Found - Level 1*/ else {
                    view.notifyRadious(4);
                    mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 4, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {
                        @Override
                        public void onSuccess(List<DocumentSnapshot> list) {
                            /*Found - Level 4*/
                            if (list.size() >= 3) {
                                //view.flush("Found - Level 4");
                                searchDataStore.setLevelNum(2);
                                if (list.size() > 5) {
                                    searchDataStore.setResultList(getFilteredResultList(list));
                                } else {
                                    searchDataStore.setResultList(list);
                                }
                                view.showResultActivty();

                            }/*Not Found - Level 4*/ else {
                                //view.flush("Not Found - Level 1");
                                view.notifyRadious(8);
                                mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 8, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {

                                    @Override
                                    public void onSuccess(List<DocumentSnapshot> list) {
                                        /*Found - Level 2*/
                                        if (list.size() >= 3) {
                                            //view.flush("Found - Level 2");
                                            searchDataStore.setLevelNum(3);
                                            if (list.size() > 5) {
                                                searchDataStore.setResultList(getFilteredResultList(list));
                                            } else {
                                                searchDataStore.setResultList(list);
                                            }
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
                                                        searchDataStore.setLevelNum(4);
                                                        //TODO tesing
                                                        if (list.size() > 5) {
                                                            searchDataStore.setResultList(getFilteredResultList(list));
                                                        } else {
                                                            searchDataStore.setResultList(list);
                                                        }
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
                                                                    searchDataStore.setLevelNum(5);
                                                                    if (list.size() > 5) {
                                                                        searchDataStore.setResultList(getFilteredResultList(list));
                                                                    } else {
                                                                        searchDataStore.setResultList(list);
                                                                    }
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
        }
    }

    public List<DocumentSnapshot> getFilteredResultList(List<DocumentSnapshot> resultList) {

        if (resultList != null) {
            sortedResult = new ArrayList<>();
            lastFiltered = new ArrayList<>();
            sortedResult = resultList;

            Collections.sort(sortedResult, new Comparator<DocumentSnapshot>() {
                @Override
                public int compare(DocumentSnapshot t1, DocumentSnapshot t2) {
                    Map<String, Object> t1Info = (Map<String, Object>) t1.get("sp_info");
                    Map<String, Object> t2Info = (Map<String, Object>) t1.get("sp_info");
                    Map<String, Object> t1Map = (Map<String, Object>) t1Info.get("self_rating");
                    Map<String, Object> t2Map = (Map<String, Object>) t2Info.get("self_rating");
                    Float t1f = Float.parseFloat((String) t1Map.get("student_guided"));
                    Float t2f = Float.parseFloat((String) t2Map.get("student_guided"));
                    return t1f.compareTo(t2f);
                }
            });
            if (sortedResult.size() > 0) {
                Map<String, Object> sp_info = (Map<String, Object>) sortedResult.get(0).get("sp_info");
                Map<String, Object> self_rating = (Map<String, Object>) sp_info.get("self_rating");
                int studentGuided = Integer.parseInt((String) self_rating.get("student_guided"));
                if (studentGuided > 0) {
                    lastFiltered.add(sortedResult.get(0));
                    sortedResult.remove(0);
                }
            }

            Collections.sort(sortedResult, new Comparator<DocumentSnapshot>() {
                @Override
                public int compare(DocumentSnapshot t1, DocumentSnapshot t2) {
                    Map<String, Object> t1Info = (Map<String, Object>) t1.get("sp_info");
                    Map<String, Object> t2Info = (Map<String, Object>) t1.get("sp_info");
                    Map<String, Object> t1Map = (Map<String, Object>) t1Info.get("self_rating");
                    Map<String, Object> t2Map = (Map<String, Object>) t2Info.get("self_rating");
                    Float t1f = Float.parseFloat((String) t1Map.get("star_rating"));
                    Float t2f = Float.parseFloat((String) t2Map.get("star_rating"));
                    return t1f.compareTo(t2f);
                }
            });
            if (lastFiltered.size() == 1) {
                for (int i = 0; i < sortedResult.size(); i++) {
                    if (i == 3) {
                        break;
                    }
                    lastFiltered.add(sortedResult.get(i));
                    sortedResult.remove(i);
                }
            } else {
                for (int i = 0; i < sortedResult.size(); i++) {
                    if (i == 4) {
                        break;
                    }
                    lastFiltered.add(sortedResult.get(i));
                    sortedResult.remove(i);
                }
            }

            if (sortedResult.size() > 0) {
                Random random = new Random();
                lastFiltered.add(sortedResult.get(random.nextInt(sortedResult.size())));
            }
            return lastFiltered;
        }
        return resultList;
    }
}
