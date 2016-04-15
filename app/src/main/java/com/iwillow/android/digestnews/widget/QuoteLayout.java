package com.iwillow.android.digestnews.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.iwillow.android.digestnews.R;
import com.iwillow.android.lib.util.DimensionUtil;

/**
 * This class is used to display quote information . See the <a href="https://play.google.com/store/apps/details?id=com.yahoo.mobile.client.android.atom"> yahoo news digest app </a>'s news detail app page for more information
 * Created by https://github.com/iwillow on 2016/4/15.
 */
public class QuoteLayout extends LinearLayout {

    private float mLeftBorderWidth;
    private final float DEFAULT_LEFT_BORDER_WIDTH = DimensionUtil.dp2px(getResources(), 20f);

    private int mLeftBorderColor;
    private final int DEFAULT_LEFT_BORDER_COLOR = Color.BLUE;

    private Paint mPaintText;
    private Paint mPaintLine;
    private String txt = "“";

    public QuoteLayout(Context context) {
        this(context, null);

    }

    public QuoteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuoteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.QuoteLayout, defStyleAttr, 0);
        mLeftBorderWidth = a.getDimension(R.styleable.QuoteLayout_left_border_width, DEFAULT_LEFT_BORDER_WIDTH);
        mLeftBorderColor = a.getColor(R.styleable.QuoteLayout_left_border_color, DEFAULT_LEFT_BORDER_COLOR);
        a.recycle();
        initPaintes();

    }


    private void initPaintes() {

        mPaintText = new TextPaint();
        mPaintText.setColor(Color.RED);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(DimensionUtil.sp2px(getResources(), 20));
        mPaintText.setTextAlign(Paint.Align.LEFT);

        mPaintLine = new Paint();
        mPaintLine.setColor(mLeftBorderColor);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setStrokeWidth(mLeftBorderWidth);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final float textHeight = Math.abs(mPaintText.ascent());
        canvas.drawText(txt, 0, textHeight, mPaintText);
        canvas.drawLine(0, textHeight, 0, getHeight(), mPaintLine);
    }
}
