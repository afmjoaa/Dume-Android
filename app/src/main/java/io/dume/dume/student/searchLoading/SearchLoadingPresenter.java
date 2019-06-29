package io.dume.dume.student.searchLoading;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.dume.dume.R;
import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.homepage.TeacherContract;

import static android.content.Context.MODE_PRIVATE;

public class SearchLoadingPresenter implements SearchLoadingContract.Presenter {

    private SearchLoadingContract.View view;
    private SearchLoadingContract.Model mModel;
    private static final String TAG = "SearchLoadingPresenter";
    private Context context;
    private Activity activity;
    private int level = 0;
    private List<DocumentSnapshot> sortedResult;
    private ArrayList<String> addedDocList = null;
    private List<DocumentSnapshot> lastFiltered;
    private final SearchDataStore searchDataStore;
    private SharedPreferences prefs;
    private Boolean uniCheckBox1;
    private Boolean degreeCheckBox1;
    private List<String> selectedUnis;
    private List<String> selectedDegrees;

    public SearchLoadingPresenter(Context context, SearchLoadingContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.view = (SearchLoadingContract.View) context;
        this.mModel = mModel;
        searchDataStore = SearchDataStore.getInstance();
        prefs = context.getSharedPreferences("filter", MODE_PRIVATE);
    }

    @Override
    public void searchLoadingEnqueue() {
        view.findView();
        view.initSearchLoading();
        view.configSearchLoading();
        view.saveToDB();

        if (prefs != null) {
            uniCheckBox1 = prefs.getBoolean("uniCheckBox", false);
            degreeCheckBox1 = prefs.getBoolean("degreeCheckBox", false);

            Gson gson = new Gson();
            String json = prefs.getString("selectedUnis", "");
            if (!json.equals("")) {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                selectedUnis = gson.fromJson(json, type);
            } else {
                selectedUnis = new ArrayList<>();
            }

            String json1 = prefs.getString("selectedDegrees", "");
            if (!json1.equals("")) {
                Type type1 = new TypeToken<List<String>>() {
                }.getType();
                selectedDegrees = gson.fromJson(json1, type1);
            } else {
                selectedDegrees = new ArrayList<>();
            }
        }

        //doing filter here
        if (uniCheckBox1 || degreeCheckBox1) {
            view.notifyRadious(4);
            filterSearchFunc();
        } else {
            normalSearchFunc();
        }
    }

    private void normalSearchFunc() {
        mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 2, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> list) {

                /*Found - Level 1*/
                if (list.size() >= 4) {
                    //view.flush("Found - Level 1");
                    searchDataStore.setLevelNum(1);
                    if (list.size() > 6) {
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
                            if (list.size() >= 4) {
                                //view.flush("Found - Level 4");
                                searchDataStore.setLevelNum(2);
                                if (list.size() > 6) {
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
                                        if (list.size() >= 4) {
                                            //view.flush("Found - Level 2");
                                            searchDataStore.setLevelNum(3);
                                            if (list.size() > 6) {
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
                                                    if (list.size() >= 4) {
                                                        //view.flush("Found - Level 3");
                                                        searchDataStore.setLevelNum(4);
                                                        //TODO tesing
                                                        if (list.size() > 6) {
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
                                                                    if (list.size() > 6) {
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

    private void filterSearchFunc() {
        view.notifyRadious(4);
        mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 4, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {
            @Override
            public void onSuccess(List<DocumentSnapshot> list) {
                /*Found - Level 1*/
                if (list.size() >= 5) {
                    //view.flush("Found - Level 1");
                    searchDataStore.setLevelNum(1);
                    if (list.size() > 5) {
                        searchDataStore.setResultList(getSpecificFilteredResultList(list));
                    } else {
                        searchDataStore.setResultList(list);
                    }
                    view.showResultActivty();
                }/*Not Found - Level 1*/ else {
                    view.notifyRadious(8);
                    mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 8, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {
                        @Override
                        public void onSuccess(List<DocumentSnapshot> list) {
                            /*Found - Level 4*/
                            if (list.size() >= 5) {
                                //view.flush("Found - Level 4");
                                searchDataStore.setLevelNum(2);
                                if (list.size() > 5) {
                                    searchDataStore.setResultList(getSpecificFilteredResultList(list));
                                } else {
                                    searchDataStore.setResultList(list);
                                }
                                view.showResultActivty();

                            }/*Not Found - Level 4*/ else {
                                //view.flush("Not Found - Level 1");
                                view.notifyRadious(16);
                                mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 16, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {

                                    @Override
                                    public void onSuccess(List<DocumentSnapshot> list) {
                                        /*Found - Level 2*/
                                        if (list.size() >= 5) {
                                            //view.flush("Found - Level 2");
                                            searchDataStore.setLevelNum(3);
                                            if (list.size() > 5) {
                                                searchDataStore.setResultList(getSpecificFilteredResultList(list));
                                            } else {
                                                searchDataStore.setResultList(list);
                                            }
                                            view.showResultActivty();

                                        }/*Not Found - Level 2*/ else {
                                            //view.flush("Not Found - Level 2");
                                            view.notifyRadious(32);
                                            mModel.search(searchDataStore.getAnchorPoint().latitude, searchDataStore.getAnchorPoint().longitude, 32, searchDataStore.getQueryString(), new TeacherContract.Model.Listener<List<DocumentSnapshot>>() {

                                                @Override
                                                public void onSuccess(List<DocumentSnapshot> list) {
                                                    /*Found - Level 3*/
                                                    if (list.size() >= 1) {
                                                        //view.flush("Found - Level 3");
                                                        searchDataStore.setLevelNum(4);
                                                        //TODO tesing
                                                        if (list.size() > 5) {
                                                            searchDataStore.setResultList(getSpecificFilteredResultList(list));
                                                        } else {
                                                            searchDataStore.setResultList(list);
                                                        }
                                                        view.showResultActivty();

                                                    }/*Not Found - Level 3*/ else {
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

                    Map<String, Object> t1Map = (Map<String, Object>) t1Info.get("unread_records");
                    Map<String, Object> t2Map = (Map<String, Object>) t2Info.get("unread_records");
                    Float t1f = Float.parseFloat((String) t1Map.get("completed_count"));
                    Float t2f = Float.parseFloat((String) t2Map.get("completed_count"));
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
                    if (i == 2) {
                        break;
                    }
                    lastFiltered.add(sortedResult.get(i));
                    sortedResult.remove(i);
                }
            } else {
                for (int i = 0; i < sortedResult.size(); i++) {
                    if (i == 3) {
                        break;
                    }
                    lastFiltered.add(sortedResult.get(i));
                    sortedResult.remove(i);
                }
            }

            if (sortedResult.size() > 0) {
                Random random = new Random();
                lastFiltered.add(sortedResult.get(random.nextInt(sortedResult.size())));
                sortedResult.remove(random.nextInt(sortedResult.size()));
            }
            if (sortedResult.size() > 0) {
                Random random = new Random();
                lastFiltered.add(sortedResult.get(random.nextInt(sortedResult.size())));
                sortedResult.remove(random.nextInt(sortedResult.size()));
            }
            if (sortedResult.size() > 0) {
                Random random = new Random();
                lastFiltered.add(sortedResult.get(random.nextInt(sortedResult.size())));
                sortedResult.remove(random.nextInt(sortedResult.size()));
            }
            return lastFiltered;
        }
        return resultList;
    }


    public List<DocumentSnapshot> getSpecificFilteredResultList(List<DocumentSnapshot> resultList) {
        sortedResult = new ArrayList<>();
        addedDocList = new ArrayList<>();

        if (uniCheckBox1 && !degreeCheckBox1) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> spInfo = (Map<String, Object>) resultList.get(i).get("sp_info");
                Map<String, Map<String, Object>> academic = (Map<String, Map<String, Object>>) spInfo.get("academic");
                String doc_uid = (String) resultList.get(i).getId();

                for (Map.Entry<String, Map<String, Object>> entry : academic.entrySet()) {
                    if (!addedDocList.contains(doc_uid)) {
                        String mainString = entry.getValue().get("institution").toString().toLowerCase();
                        StringBuilder secondMainString = new StringBuilder();
                        //make the thing here with only first words
                        String[] split = mainString.split("\\s");
                        for (String aSplit : split) {
                            if (!aSplit.equals("of") && !aSplit.equals("and") && !aSplit.equals("&")) {
                                secondMainString.append(aSplit.substring(0, 1));
                            }
                        }

                        for (int j = 0; j < selectedUnis.size(); j++) {
                            if (mainString.contains(selectedUnis.get(j).toLowerCase())) {
                                sortedResult.add(resultList.get(i));
                                addedDocList.add(doc_uid);
                                break;
                            } else if (secondMainString.toString().toLowerCase().equals(selectedUnis.get(j).toLowerCase())) {
                                sortedResult.add(resultList.get(i));
                                addedDocList.add(doc_uid);
                                break;
                            }
                        }
                    }
                }
            }


        } else if (degreeCheckBox1 && !uniCheckBox1) {
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> spInfo = (Map<String, Object>) resultList.get(i).get("sp_info");
                Map<String, Map<String, Object>> academic = (Map<String, Map<String, Object>>) spInfo.get("academic");
                String doc_uid = (String) resultList.get(i).getId();

                for (Map.Entry<String, Map<String, Object>> entry : academic.entrySet()) {
                    if (!addedDocList.contains(doc_uid)) {
                        String mainString = entry.getKey().toString().toLowerCase();
                        for (int j = 0; j < selectedDegrees.size(); j++) {
                            if (mainString.contains(selectedDegrees.get(j).toLowerCase())) {
                                sortedResult.add(resultList.get(i));
                                addedDocList.add(doc_uid);
                                break;
                            }
                        }
                    }
                }
            }
        } else if (degreeCheckBox1 && uniCheckBox1) {
            //both filter applied
            for (int i = 0; i < resultList.size(); i++) {
                Map<String, Object> spInfo = (Map<String, Object>) resultList.get(i).get("sp_info");
                Map<String, Map<String, Object>> academic = (Map<String, Map<String, Object>>) spInfo.get("academic");
                String doc_uid = (String) resultList.get(i).getId();


                for (Map.Entry<String, Map<String, Object>> entry : academic.entrySet()) {
                    if (!addedDocList.contains(doc_uid)) {
                        String mainString = entry.getValue().get("institution").toString().toLowerCase();
                        StringBuilder secondMainString = new StringBuilder();
                        //make the thing here with only first words
                        String[] split = mainString.split("\\s");
                        for (String aSplit : split) {
                            if (!aSplit.equals("of") && !aSplit.equals("and") && !aSplit.equals("&")) {
                                secondMainString.append(aSplit.substring(0, 1));
                            }
                        }

                        for (int j = 0; j < selectedUnis.size(); j++) {
                            if (mainString.contains(selectedUnis.get(j).toLowerCase())) {
                                sortedResult.add(resultList.get(i));
                                addedDocList.add(doc_uid);
                                break;
                            } else if (secondMainString.toString().toLowerCase().equals(selectedUnis.get(j).toLowerCase())) {
                                sortedResult.add(resultList.get(i));
                                addedDocList.add(doc_uid);
                                break;
                            }
                        }
                        if (!addedDocList.contains(doc_uid)) {
                            String degreeString = entry.getKey().toString().toLowerCase();
                            for (int k = 0; k < selectedDegrees.size(); k++) {
                                if (degreeString.contains(selectedDegrees.get(k).toLowerCase())) {
                                    sortedResult.add(resultList.get(i));
                                    addedDocList.add(doc_uid);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        int remained = 5-sortedResult.size();
        if(sortedResult.size()>=5){
            return sortedResult;
        }else{
            //add the remaining to sortedResult here
            Collections.sort(resultList, new Comparator<DocumentSnapshot>() {
                @Override
                public int compare(DocumentSnapshot t1, DocumentSnapshot t2) {
                    Map<String, Object> t1Info = (Map<String, Object>) t1.get("sp_info");
                    Map<String, Object> t2Info = (Map<String, Object>) t1.get("sp_info");
                    Map<String, Object> t1Map = (Map<String, Object>) t1Info.get("unread_records");
                    Map<String, Object> t2Map = (Map<String, Object>) t2Info.get("unread_records");
                    Float t1f = Float.parseFloat((String) t1Map.get("completed_count"));
                    Float t2f = Float.parseFloat((String) t2Map.get("completed_count"));
                    return t1f.compareTo(t2f);
                }
            });

            for (int i = 0; i < resultList.size(); i++) {
                String doc_uid = (String) resultList.get(i).getId();
                if(remained>0){
                    if (!addedDocList.contains(doc_uid)) {
                        sortedResult.add(resultList.get(i));
                        addedDocList.add(doc_uid);
                        remained = remained -1;
                    }
                }
            }
            return sortedResult;
        }
    }
}
