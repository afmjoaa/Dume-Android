package io.dume.dume.teacher.homepage;

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
        /**
         * it returns a random number bound of 5000
         * @return int
         */
        int getRandomNumber();

        /**
         * set a callback listener before calling model data its nullable
         * @param listener
         */
        void setListener(CallBack listener);

        interface CallBack {
            void onSuccess();

            void onError(String msg);
        }
    }

}
