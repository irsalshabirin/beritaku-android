package co.dust.beritaku.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.dust.beritaku.BuildConfig;

/**
 * Created by irsal on 1/29/17.
 */

public class ConstantHelper {

    private static final String TAG = ConstantHelper.class.getSimpleName();

    private static ProgressDialog mProgressDialog;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static int width;
    public static int height;
    public static float density;
    public static int densityDpi;

    public static void showProgressDialog(Activity activity, String title, String message, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(activity);

                if (title != null) {
                    mProgressDialog.setTitle(title);
                }

                if (message != null) {
                    mProgressDialog.setMessage(message);
                }

                mProgressDialog.setCancelable(isCancelable);

                mProgressDialog.setIndeterminate(true);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception ignored) {

        }
    }


    public static void hideProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                mProgressDialog = null;
            }
        } catch (Exception ignored) {

        }
    }

    public static float dpToPixel(float dp, Context context) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }


    public static float pixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static String getTimeAgo(String date) throws ParseException {
        Date date1 = convertDateTime(date);
        long time = date1.getTime();

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "time : " + time);
        }

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

//        long now = getCurrentTime(ctx);
        long now = System.currentTimeMillis();

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "now : " + now);
        }

        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public static Date convertDateTime(String string) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date;
        date = format.parse(string);
        return date;
    }

    public static Date convertTime(String string) throws ParseException {
        DateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        Date date;
        date = format.parse(string);
        return date;
    }

    public static String simpleDate(String string) throws ParseException {
        Date date;
        date = convertDateTime(string);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM, yyyy  HH:mm");

        return format.format(date);
    }

    public static String simpleTime(String string) throws ParseException {
        Date date = convertTime(string);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        return format.format(date);
    }

    public static void initDisplay(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels;
        density = metrics.density;
        densityDpi = metrics.densityDpi;
    }
}
