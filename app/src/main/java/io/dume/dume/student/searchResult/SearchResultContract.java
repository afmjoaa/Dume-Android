package io.dume.dume.student.searchResult;

public interface SearchResultContract {
    interface View {

        void configSearchResult();

        void initSearchResult();

        void findView();

    }

    interface Presenter {

        void searchResultEnqueue();

        void onSearchResultIntracted(android.view.View view);

    }

    interface Model {

        void searchResultHawwa();
    }
}
