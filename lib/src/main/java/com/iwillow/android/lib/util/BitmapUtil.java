package com.iwillow.android.lib.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by https://github.com/iwillow  2016/4/13.
 */
public class BitmapUtil {

    /**
     * clip source bitmap into a circle bitmap
     *
     * @param src     the source bitmap
     * @param recycle whether recycle the source bitmap
     * @param config  bitmap config
     * @return clipped  circle bitmap
     */
    public Bitmap createCircleBitmap(Bitmap src, boolean recycle, Bitmap.Config config) {
        if (src == null) {
            return null;
        }
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        Bitmap out = Bitmap.createBitmap(src.getWidth(), src.getHeight(), config);
        final Rect rect = new Rect(0, 0, Math.min(src.getWidth(), src.getHeight()), Math.min(src.getWidth(), src.getHeight()));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(out);
        canvas.drawARGB(0, 0, 0, 0);
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src, rect, rect, paint);

        if (recycle) {
            src.recycle();
        }

        return out;

    }


}
