package io.dume.dume.inter_face;

public interface usefulListeners {

    interface uploadToDBListerer {

        void onSuccessDB(Object obj);

        void onFailDB(Object obj);
    }

    interface uploadToSTGListererMin {

        void onSuccessSTG(Object obj);

        void onFailSTG(Object obj);
    }

    interface uploadToSTGListererMax {

        void onSuccessSTG(Object obj);

        void onFailSTG(Object obj);

        void onProgressSTG(Object obj);

    }

    interface withKeyboardListener {

        void onOpen();

        void onClose();
    }
}
