package io.dume.dume.teacher;

public interface BaseContract {

    interface View<T extends Presenter> {
        void setPresenter(T presenter);

        void init();

        void showLoading();

        void hideLoading();

        void flush(String message);
    }

    interface Presenter {
        void onViewInteracted(android.view.View view);

        void enqueue();

        void stop();
    }
}