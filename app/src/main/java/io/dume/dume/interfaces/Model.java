package io.dume.dume.interfaces;

public interface Model {

    interface StringData {
        int getRandomNumber();
        void setListener(DataListener listener);

        interface DataListener {
            void onSuccess();

            void onError(String msg);
        }
    }

    interface FirebaseData {

    }
}
