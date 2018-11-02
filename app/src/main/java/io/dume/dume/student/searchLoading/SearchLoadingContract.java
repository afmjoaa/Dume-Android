package io.dume.dume.student.searchLoading;

public interface SearchLoadingContract {
    interface View {

        void configSearchLoading();

        void initSearchLoading();

        void findView();

        void gotoSearchResult();

        void cancelBtnClicked();
    }

    interface Presenter {

        void searchLoadingEnqueue();

        void onSearchLoadingIntracted(android.view.View view);

    }

    interface Model {

        void searchLoadingHawwa();
    }
}
