package io.dume.dume.broadcastReceiver;

public interface MyConnectivityHandler {
    void pause();

    void resume();

    void onError(Exception e);
}
