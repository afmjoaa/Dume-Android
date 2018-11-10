package io.dume.dume.util;

import java.util.List;

public interface MyBaseContract {

    interface View {
        void init();

        void showLoading();

        void hideLoading();

        void flush(String message);


    }

    interface Presenter {
        void enqueue();

        void onViewInteracted(android.view.View view);
    }

    interface Model {

    }

}
