package io.dume.dume.interfaces;

public interface Presenter {
    interface MainActivityPresenter {
        void onButtonClicked();
        void init();

    }

    interface WelcomeActivityPresenter {
        void showPages();

        void destroy();
    }

}
