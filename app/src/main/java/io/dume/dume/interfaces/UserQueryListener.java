package io.dume.dume.interfaces;

import java.util.Map;

public interface UserQueryListener {

    void onSuccess(Map<String, Object> objs);

    void onFailure(String error);

    void onStart();
}

