package io.dume.dume;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.IntDef;

public class StateManager {

    public static StateManager instance;
    private SharedPreferences local;
    private SharedPreferences.Editor edit;

    private StateManager() {
    }

    public StateManager getInstance(Context context) {
        if (instance == null) {
            instance = new StateManager();
            local = context.getSharedPreferences("state", Context.MODE_PRIVATE);
        }
        return instance;
    }

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
        }
        edit.apply();
    }

    public SharedPreferences keyValue() {
        return local;
    }

    public String getStringValue(String key) {
        return local.getString(key, "N/A");
    }

    public void setState(String state) {
        edit.putString("state", state);
        edit.apply();
    }
}
