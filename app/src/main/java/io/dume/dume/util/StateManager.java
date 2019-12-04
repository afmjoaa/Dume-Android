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
    public SharedPreferences keyValue() {
        return local;
    }

    //just a temporary function
    public String getStringValue(String key) {
        return local.getString(key, "N/A");
    }

    //current state in the forward flow
    public void setState(String state) {
        edit.putString("state", state);
        edit.apply();
    }
}
