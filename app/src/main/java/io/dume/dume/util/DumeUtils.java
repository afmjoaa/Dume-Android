package io.dume.dume.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dume.dume.R;

public class DumeUtils {
    public static String getApplicationName() {
        return "Dume";
    }

    public static boolean isInteger(String string) {
        return TextUtils.isDigitsOnly(string);
    }

    public static boolean isValidEmailAddress(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int id, int color, int textColor, int count, float x, float y) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(id);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context, x, y);
        }
        badge.setCircleColor(color);
        badge.setCircleTextColor(textColor);

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(id, badge);
    }

    public static void setBadgeChar(Context context, LayerDrawable icon, int color, int textColor, char character, float x, float y) {

        BadgeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context, x, y);
        }
        badge.setCircleColor(color);
        badge.setCircleTextColor(textColor);

        badge.setChar(character);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
