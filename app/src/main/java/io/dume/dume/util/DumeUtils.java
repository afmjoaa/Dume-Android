package io.dume.dume.util;

import android.text.TextUtils;

public class DumeUtils {
    public static String getApplicationName() {
        return "Dume";
    }

    public static boolean isInteger(String string) {
        return TextUtils.isDigitsOnly(string);
    }
}
