package io.dume.dume.homepage;

public interface MainContract {


    interface View {
        void showRandomNumber(int i);

        void init();
    }


    interface Presenter {
        void onButtonClicked();

        void init();
    }

    interface Model {
        int getRandomNumber();

        void setListener(CallBack listener);

        interface CallBack {
            void onSuccess();

            void onError(String msg);
        }
    }

}
