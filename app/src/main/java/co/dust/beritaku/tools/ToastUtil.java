package co.dust.beritaku.tools;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 */
public class ToastUtil {

    public static void showToast(Context context, String message) {

        if (null != context && !TextUtils.isEmpty(message)) {
//            try {
//                SuperToast.create(context, message, Style.DURATION_MEDIUM)
//                        .setFrame(Style.FRAME_LOLLIPOP)
//                        .setTextColor(ContextCompat.getColor(context, android.R.color.white))
//                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
//                        .setAnimations(Style.ANIMATIONS_POP).show();
//            } catch (Exception ignored) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//            }

        }
    }

    public static void showToast(Context context, int message) {
        if (null != context && message != 0) {

//            try {
//                SuperToast.create(context, context.getString(message), Style.DURATION_MEDIUM)
//                        .setFrame(Style.FRAME_LOLLIPOP)
//                        .setTextColor(ContextCompat.getColor(context, android.R.color.white))
//                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
//                        .setAnimations(Style.ANIMATIONS_POP).show();
//            } catch (Exception ignored) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//            }
        }
    }

}
