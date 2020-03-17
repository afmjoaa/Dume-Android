package io.dume.dume.interfaces;

public interface usefulListeners {

    interface uploadToDBListerer {

        void onSuccessDB(Object obj);

        void onFailDB(Object obj);
    }

    interface uploadToSTGListererMin {

        void onSuccessSTG(Object obj);

        void onFailSTG(Object obj);
    }
}
