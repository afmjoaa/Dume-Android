package io.dume.dume.util;

import android.content.Context;
import android.content.SharedPreferences;

public class StateManager {

    public static StateManager instance;
    private static SharedPreferences local;
    private SharedPreferences.Editor edit;

    private StateManager() {
    }

    //get the singleton of the stateManager
    public static StateManager getInstance(Context context) {
        if (instance == null) {
            instance = new StateManager();
            local = context.getSharedPreferences("state", Context.MODE_PRIVATE);
        }
        return instance;
    }

    //set any value to the sharePreference
    public void setValue(String key,  Object  value) {
        edit = local.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        }else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        }
        edit.apply();
    }

    //get replacement
    public SharedPreferences getLocal() {
        return local;
    }

    //current state in the forward flow
    public void setTeacherCurrentState(String state) {
        edit.putString("teacherCurrentState", state);
        edit.apply();
    }

    public void setStudentCurrentState(String state) {
        edit.putString("teacherCurrentState", state);
        edit.apply();
    }

    public void setRole(String role) {
        edit.putString("role", role);
        edit.apply();
    }

    public void setFirstTimeUser(Boolean state) {
        edit.putBoolean("firstTimeUser", state);
        edit.apply();
    }
}
