package io.dume.dume.components.broadcastReceiver;

public interface MyConnectivityHandler {
    void pause();

    void resume();

    void onError(Exception e);
}
