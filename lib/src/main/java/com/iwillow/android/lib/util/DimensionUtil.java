package com.iwillow.android.lib.util;

import android.content.res.Resources;

/**
 * Created by https://github.com/iwillow on 2016/4/15.
 */
public class DimensionUtil {

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}
