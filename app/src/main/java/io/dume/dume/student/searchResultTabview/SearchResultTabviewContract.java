package io.dume.dume.student.searchResultTabview;

public interface SearchResultTabviewContract {
    interface View {

        void configSearchResultTabview();

        void initSearchResultTabview();

        void findView();

    }

    interface Presenter {

        void searchResultTabviewEnqueue();

        void onSearchResultTabviewIntracted(android.view.View view);

    }

    interface Model {

        void searchResultTabviewHawwa();
    }
}
