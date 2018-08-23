package io.dume.dume.broadcastReceiver;

public interface MyGpsHandlerOne {
    void onGpsPause();

    void onGpsResume();

    void onGpsError(Exception e);
}
