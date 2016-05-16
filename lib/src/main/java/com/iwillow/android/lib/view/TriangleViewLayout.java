package com.iwillow.android.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.iwillow.android.lib.R;

/**
 * Created by https://www.github.com/iwillow on 2016/5/14.
 */
public class TriangleViewLayout extends LinearLayout {
    private static final int DEFAULT_COLOR_SOLID = Color.TRANSPARENT;
    private static final int DEFAULT_COLOR_STROKE = Color.TRANSPARENT;
    private Paint mPaintSolid;
    private Paint mPaintStroke;
    private float mStrokeWidth = 5f;
    private int mColorSolid = DEFAULT_COLOR_SOLID;
    private int mColorStroke = DEFAULT_COLOR_STROKE;
    private static final int TYPE_UP = 0;
    private static final int TYPE_DOWN = 1;
    private int type = TYPE_UP;
    private Path mPath;

    private static final String COLOR_SOLID = "COLOR_SOLID";
    private static final String COLOR_STROKE = "COLOR_STROKE";
    private static final String STROKE_WIDTH = "STROKE_WIDTH";
    private static final String TYPE = "TYPE";
    private static final String INSTANCE_STATE = "saved_instance";


    public TriangleViewLayout(Context context) {
        this(context, null);

    }

    public TriangleViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = null;
        try {
            attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TriangleViewLayout, defStyleAttr, 0);
            initByAttributes(attributes);
        } finally {
            if (attributes != null) {
                attributes.recycle();
            }
        }
        initPaint();
    }

    private void initByAttributes(TypedArray attributes) {
        mColorSolid = attributes.getColor(R.styleable.TriangleViewLayout_triangleViewSolidColor, DEFAULT_COLOR_SOLID);
        mColorStroke = attributes.getColor(R.styleable.TriangleViewLayout_triangleViewStrokeColor, DEFAULT_COLOR_STROKE);
        mStrokeWidth = attributes.getDimension(R.styleable.TriangleViewLayout_triangleViewStrokeWidth, 0);
        type = attributes.getInt(R.styleable.TriangleViewLayout_triangleViewType, TYPE_UP);
    }

    private void initPaint() {
        mPaintSolid = new Paint();
        mPaintSolid.setAntiAlias(true);
        mPaintSolid.setStyle(Paint.Style.FILL);
        mPaintSolid.setColor(mColorSolid);

        mPaintStroke = new Paint();
        mPaintStroke.setAntiAlias(true);
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setStrokeWidth(mStrokeWidth);
        mPaintStroke.setColor(mColorStroke);
        mPath = new Path();

       // setBackgroundColor(Color.TRANSPARENT);
    }


    public int getColorSolid() {
        return mColorSolid;
    }

    public void setColorSolid(int colorSolid) {
        this.mColorSolid = colorSolid;
        mPaintSolid.setColor(colorSolid);
        requestLayout();
    }

    public int getColorStroke() {
        return mColorStroke;
    }

    public void setColorStroke(int colorStroke) {
        this.mColorStroke = colorStroke;
        mPaintStroke.setColor(colorStroke);
        requestLayout();
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        mPaintStroke.setStrokeWidth(strokeWidth);
        requestLayout();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        requestLayout();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.reset();
        mPath.moveTo(getPaddingLeft(), getPaddingTop());
        if (type == TYPE_UP) {
            mPath.lineTo(getPaddingLeft(), getHeight() - getPaddingTop() - getPaddingBottom());
            mPath.lineTo(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom());
        } else if (type == TYPE_DOWN) {
            mPath.lineTo(getWidth() - getPaddingLeft() - getPaddingRight(),  getPaddingTop());
            mPath.lineTo(getWidth() - getPaddingLeft() - getPaddingRight(), getHeight() - getPaddingTop() - getPaddingBottom());
        }
        mPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawPath(mPath, mPaintSolid);
        canvas.drawPath(mPath, mPaintStroke);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(COLOR_SOLID, mColorSolid);
        bundle.putInt(COLOR_STROKE, mColorStroke);
        bundle.putInt(TYPE, type);
        bundle.putFloat(STROKE_WIDTH, mStrokeWidth);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            mColorSolid = bundle.getInt(COLOR_SOLID, DEFAULT_COLOR_SOLID);
            mColorStroke = bundle.getInt(COLOR_STROKE, DEFAULT_COLOR_STROKE);
            type = bundle.getInt(TYPE, TYPE_UP);
            mStrokeWidth = bundle.getFloat(STROKE_WIDTH, 0);
            initPaint();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
