package com.iwillow.android.digestnews.http;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by https://www.github.com/iwillow on 2016/9/11.
 */
public class ToastUtil {
    public static void showShort(Context context, String msg) {
        if (context != null && !TextUtils.isEmpty(msg)) {
            Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showShort(Context context, @StringRes int resId) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLong(Context context, String msg) {
        if (context != null && !TextUtils.isEmpty(msg)) {
            Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLong(Context context, @StringRes int resId) {
        if (context != null) {
            Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_LONG).show();
        }
    }
}
