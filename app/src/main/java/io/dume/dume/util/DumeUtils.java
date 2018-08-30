package io.dume.dume.util;

import android.app.Activity;
import android.content.Context;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.LocationManager;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dume.dume.R;

public class DumeUtils {
    public static int GALLARY_IMAGE = 1;
    public static int CAMERA_IMAGE = 2;
    private static final int WIDTH_INDEX = 0;
    private static final int HEIGHT_INDEX = 1;


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

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = Objects.requireNonNull(lm).isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.e(getApplicationName(), "isGpsEnabled method exception : ", ex);
        }
        return gps_enabled;
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

    public static void configureAppbar(Context context, String title) {
        AppCompatActivity activity = (AppCompatActivity) context;
        Toolbar toolbar = activity.findViewById(R.id.accountToolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar supportActionBar = activity.getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }
        CollapsingToolbarLayout collapsingToolbarLayout = activity.findViewById(R.id.accountCollapsing);
        collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.createFromAsset(activity.getAssets(), "fonts/Cairo-Light.ttf"));
        collapsingToolbarLayout.setTitle(title);
    }


    public static int[] getScreenSize(Context context) {
        int[] widthHeight = new int[2];
        widthHeight[WIDTH_INDEX] = 0;
        widthHeight[HEIGHT_INDEX] = 0;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        widthHeight[WIDTH_INDEX] = size.x;
        widthHeight[HEIGHT_INDEX] = size.y;

        if (isScreenSizeRetrieved(widthHeight)) {
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            widthHeight[0] = metrics.widthPixels;
            widthHeight[1] = metrics.heightPixels;
        }

        // Last defense. Use deprecated API that was introduced in lower than API 13
        if (isScreenSizeRetrieved(widthHeight)) {
            widthHeight[0] = display.getWidth(); // deprecated
            widthHeight[1] = display.getHeight(); // deprecated
        }

        return widthHeight;
    }

    private static boolean isScreenSizeRetrieved(int[] widthHeight) {
        return widthHeight[WIDTH_INDEX] == 0 || widthHeight[HEIGHT_INDEX] == 0;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void toggleKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager)   activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        Objects.requireNonNull(imm).showSoftInput(view, 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

