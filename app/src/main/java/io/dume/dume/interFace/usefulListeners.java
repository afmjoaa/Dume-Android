package io.dume.dume.interFace;

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
