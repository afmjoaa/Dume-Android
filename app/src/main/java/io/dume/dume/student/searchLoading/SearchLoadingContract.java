package io.dume.dume.student.searchLoading;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface SearchLoadingContract {
    interface View {

        void configSearchLoading();

        void initSearchLoading();

        void findView();

        void gotoSearchResult();

        void cancelBtnClicked();

        void saveToDB();

        boolean checkIfInDB(Map<String, Object> jizz);
    }

    interface Presenter {

        void searchLoadingEnqueue();

        void onSearchLoadingIntracted(android.view.View view);

    }

    interface Model {

        void search(double lat, double lon, double radius, String queryString, TeacherContract.Model.Listener<List<DocumentSnapshot>> callbackResult);

        void searchLoadingHawwa();

        void updateRecentSearch(String identify, Map<String, Object> mainMap, TeacherContract.Model.Listener<Void> listener);

    }
}
