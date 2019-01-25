package io.dume.dume.student.searchResult;

import java.util.Map;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface SearchResultContract {
    interface View {

        void configSearchResult();

        void initSearchResult();

        void findView();

        void centerTheMapCamera();

    }

    interface Presenter {

        void searchResultEnqueue();

        void onSearchResultIntracted(android.view.View view);

    }

    interface Model {

        void searchResultHawwa();

    }
}
