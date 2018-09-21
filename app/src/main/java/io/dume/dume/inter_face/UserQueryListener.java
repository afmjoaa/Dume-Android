package io.dume.dume.inter_face;

import java.util.ArrayList;
import java.util.Map;

import io.dume.dume.teacher.pojo.Education;

public interface UserQueryListener {

    void onSuccess(Map<String, Object> objs);

    void onFailure(String error);

    void onStart();
}

