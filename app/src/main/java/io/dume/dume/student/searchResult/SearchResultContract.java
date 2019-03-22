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

        void goHome(DocumentReference documentReference);

        void initSearchResult();

        DocumentSnapshot getSelectedMentor();

        void cancelBtnClicked();

        void flush(String msg);

        void findView();

        void centerTheMapCamera();

        void onSwipeLeftRight(Boolean swipeRight);

        void showRequestDialogue();
    }

    interface Presenter {
        void onMapLoaded();

        void searchResultEnqueue();

        void onSearchResultIntracted(android.view.View view);

    }

    interface Model {

        void searchResultHawwa();

        void riseNewRecords(Map<String, Object> data, TeacherContract.Model.Listener<DocumentReference> listener);

        void riseNewPushNoti(TeacherContract.Model.Listener<Void> listener);
    }
}
