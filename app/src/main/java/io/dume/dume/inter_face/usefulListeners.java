package io.dume.dume.inter_face;

public interface usefulListeners {

    interface uploadListenerMin {

        void onStart(Object obj);

        void onSuccess(Object obj);

        void onFail(Object obj);
    }

    interface uploadListenerMax {

        void onSuccess(Object obj);

        //used for pause cancel and exception
        void onFail(Object obj);

        void onProgress(Object obj);

    }
}
