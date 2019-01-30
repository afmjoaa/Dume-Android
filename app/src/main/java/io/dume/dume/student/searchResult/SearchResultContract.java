package io.dume.dume.student.searchResult;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface SearchResultContract {
    interface View {


        void pushProfile(DocumentSnapshot singleSkill);

        void configSearchResult();

        void goHome();

        void initSearchResult();

        DocumentSnapshot getSelectedMentor();

        void flush(String msg);

        void findView();

        void centerTheMapCamera();

    }

    interface Presenter {
        void onMapLoaded();

        void searchResultEnqueue();

        void onSearchResultIntracted(android.view.View view);

    }

    interface Model {

        void searchResultHawwa();

        void riseNewRecords(Map<String, Object> data, TeacherContract.Model.Listener<DocumentReference> listener);

    }
}
