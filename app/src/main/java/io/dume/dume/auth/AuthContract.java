package io.dume.dume.auth;

public interface AuthContract {
    interface View {
        void init();

        void findView();


    }

    interface Model {

    }

    interface Presenter {
        void enqueue();

    }
}
