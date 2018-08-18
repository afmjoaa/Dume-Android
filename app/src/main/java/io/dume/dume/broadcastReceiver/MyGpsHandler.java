package io.dume.dume.broadcastReceiver;

public interface MyGpsHandler {
    void onGpsPause();
    void onGpsResume();
    void onGpsError(Exception e);
}
