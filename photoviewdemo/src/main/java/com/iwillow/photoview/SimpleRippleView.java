package com.iwillow.photoview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

/**
 * Created by Administrator on 2016/5/21.
 */
public class SimpleRippleView extends View {

    private Path mPathCircle;
    private Path mPathRect;
    private Paint mPaintRect;
    private float mStartRadius;
    private float mEndRadius;
    private float mCenterX;
    private float mCenterY;
    private ValueAnimator valueAnimator;

    public SimpleRippleView(Context context) {
        super(context);
        initPath();
    }

    public SimpleRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPath();
    }

    public SimpleRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPath();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleRippleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPath();
    }

    private void initPath() {
        mPathRect = new Path();
        mPaintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintRect.setColor(Color.RED);
        mPaintRect.setStyle(Paint.Style.FILL);
        mPathCircle = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = getWidth() / 2.0f;
        mCenterY = getHeight() / 2.0f;
        mStartRadius = Math.min(mCenterX, mCenterX) / 4;
        mEndRadius = (float) (1.5f * Math.hypot(mCenterX, mCenterY));

        mPathRect.reset();
        mPathRect.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CCW);
        mPathRect.close();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.clipPath(mPathCircle, Region.Op.DIFFERENCE);
        canvas.drawPath(mPathRect, mPaintRect);
    }

    public void reaval() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(mStartRadius, mEndRadius);
        valueAnimator.setDuration(400);
        valueAnimator.setInterpolator(new AnticipateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float radius = (float) animation.getAnimatedValue();
                mPathCircle.reset();
                mPathCircle.addCircle(mCenterX, mCenterX, radius, Path.Direction.CCW);
                mPathCircle.close();
                invalidate();
            }
        });
        valueAnimator.start();

    }


}
