package com.iwillow.android.lib.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by https://www.github.com/iwillow on 2016/5/11.
 */
public class IntentUtil {
    
    public static List<ResolveInfo> queryIntents(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, 0);

    }
}
