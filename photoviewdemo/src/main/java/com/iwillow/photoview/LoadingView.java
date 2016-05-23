package com.iwillow.photoview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

/**
 * Created by Administrator on 2016/5/20.
 */
public class LoadingView extends View {
    private int colors[] = new int[6];
    private Paint mPaints[] = new Paint[6];
    private Paint mPaint;
    private static final int DEFAULT_SIZE = 200;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private Path mPathRotation;
    private PathMeasure mPathMeasureRotation;
    private Path mPathRadial;
    private PathMeasure mPathMeasureRadial;
    private float mPathLengthRotation;
    private float mPathLengthRadical;
    private float mBitmapCoordRotation[][] = new float[6][2];
    private float mBitmapCoordRadical[][] = new float[6][2];
    private float mCircleRadiusRotation;
    private float mCircleRadiusRadical;
    private float mRadiusRadical;
    private float mStepRotation;
    private float mStepRadical;
    private static final int STATE_INIT = 0;
    private static final int STATE_ROTATION = 1;
    private static final int STATE_RADICAL = 2;
    private static final int STATE_END = 3;
    private int mState;
    private OnLoadingAnimationListener onLoadingAnimationListener;
    private ValueAnimator valueAnimator;

    public LoadingView(Context context) {
        super(context);
        intColors(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intColors(context);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intColors(context);
    }

    private void intColors(Context context) {
        colors[0] = context.getResources().getColor(R.color.circle1);
        colors[1] = context.getResources().getColor(R.color.circle2);
        colors[2] = context.getResources().getColor(R.color.circle3);
        colors[3] = context.getResources().getColor(R.color.circle4);
        colors[4] = context.getResources().getColor(R.color.circle5);
        colors[5] = context.getResources().getColor(R.color.circle6);
        for (int i = 0; i < colors.length; i++) {
            mPaints[i] = new Paint();
            mPaints[i].setAntiAlias(true);
            mPaints[i].setStyle(Paint.Style.FILL);
            mPaints[i].setColor(colors[i]);
        }
        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStyle(Paint.Style.FILL);
        mPathRotation = new Path();
        mPathMeasureRotation = new PathMeasure();
        mPathRadial = new Path();
        mPathMeasureRadial = new PathMeasure();
        mState = STATE_ROTATION;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int x = getMeasuredWidth();
        final int y = getMeasuredHeight();
        if (x > 0 && y > 0) {
            mCenterX = x / 2;
            mCenterY = y / 2;
            mRadius = 1 * Math.min(x, y) / 8;
            mCircleRadiusRotation = mRadius / 6;
            mPathRotation.reset();
            mPathRotation.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);
            mPathRotation.close();
            mPathMeasureRotation.setPath(mPathRotation, false);
            mPathLengthRotation = mPathMeasureRotation.getLength();
            invalidateCoordinate();
        }
    }

    private void invalidateCoordinate() {
        if (mPathLengthRotation == 0 || mPathMeasureRotation.getLength() == 0) {
            return;
        }
        mStepRotation += mPathLengthRotation / 65;
        if (mStepRotation >= mPathLengthRotation) {
            mStepRotation = 0;
        }

        for (int i = 0; i < 6; i++) {
            float distance = i * mPathLengthRotation / 6 + mStepRotation;
            if (distance >= mPathLengthRotation) {
                distance = distance % mPathLengthRotation;
            }
            mPathMeasureRotation.getPosTan(distance, mBitmapCoordRotation[i], null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == STATE_INIT) {
            canvas.drawPath(mPathRotation, mPaint);
            for (int i = 0; i < 6; i++) {
                canvas.drawCircle(mBitmapCoordRotation[i][0], mBitmapCoordRotation[i][1], mCircleRadiusRotation, mPaints[i]);
            }
        } else if (mState == STATE_ROTATION) {
            canvas.drawPath(mPathRotation, mPaint);
            for (int i = 0; i < 6; i++) {
                canvas.drawCircle(mBitmapCoordRotation[i][0], mBitmapCoordRotation[i][1], mCircleRadiusRotation, mPaints[i]);
            }
            invalidateCoordinate();
            invalidate();
        } else if (mState == STATE_RADICAL) {
            canvas.drawPath(mPathRadial, mPaint);
            for (int i = 0; i < 6; i++) {
                canvas.drawCircle(mBitmapCoordRadical[i][0], mBitmapCoordRadical[i][1], mRadiusRadical, mPaints[i]);
            }
        }
    }


    public void animateRadical() {
        mState = STATE_RADICAL;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        float r = mRadius;
        valueAnimator = ValueAnimator.ofFloat(r, 0f);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new AnticipateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (onLoadingAnimationListener != null) {
                    onLoadingAnimationListener.onLoading();
                }
                mCircleRadiusRadical = (float) animation.getAnimatedValue();
                mRadiusRadical = mCircleRadiusRadical / 6;
                mPathRadial.reset();
                mPathRadial.addCircle(mCenterX, mCenterY, mCircleRadiusRadical, Path.Direction.CW);
                mPathRadial.close();
                mPathMeasureRadial.setPath(mPathRadial, false);
                mPathLengthRadical = mPathMeasureRadial.getLength();

                mStepRadical += mPathLengthRadical / 65;
                if (mStepRadical >= mPathLengthRadical) {
                    mStepRadical = 0;
                }
                for (int i = 0; i < 6; i++) {
                    float distance = i * mPathLengthRadical / 6 + mStepRadical;
                    if (distance >= mPathLengthRadical) {
                        distance = distance % mPathLengthRadical;
                    }
                    mPathMeasureRadial.getPosTan(distance, mBitmapCoordRadical[i], null);
                }
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mState = STATE_END;
                if (onLoadingAnimationListener != null) {
                    onLoadingAnimationListener.onLoadEnd();
                }
            }
        });
        valueAnimator.start();
    }

    public void setOnLoadingAnimationListener(OnLoadingAnimationListener onLoadingAnimationListener) {
        this.onLoadingAnimationListener = onLoadingAnimationListener;
    }

    public interface OnLoadingAnimationListener {


        void onLoading();

        void onLoadEnd();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }
}
