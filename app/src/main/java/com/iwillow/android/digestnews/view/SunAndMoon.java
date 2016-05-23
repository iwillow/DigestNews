package com.iwillow.android.digestnews.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.iwillow.android.digestnews.R;

/**
 * the second product guide page.
 * Created by https://github.com/iwillow  2016/4/13.
 */
public class SunAndMoon extends View {

    private Path.Direction mCurrentDirection;
    private Paint mPaint;
    private Path mPath;
    private float RADIUS;
    private float XCOORD;
    private float YCOORD;
    private PathMeasure mPathSunMeasure;
    private float mPathLength;
    private float mDistance;
    private float[] mPos;
    private float[] mTan;

    private Bitmap mBitmapSun;
    private Bitmap mBitmapMoon;

    private int mBitmapSunWidth;
    private int mBitmapSunHeight;
    private int mBitmapMoonWidth;
    private int mBitmapMoonHeight;
    private ValueAnimator mValueAnimator;
    private Matrix mMatrix;

    public SunAndMoon(Context context) {
        this(context, null);

    }

    public SunAndMoon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunAndMoon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
        initNewPath(Path.Direction.CW);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mMatrix = new Matrix();
        mBitmapSun = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_2_sun);
        mBitmapMoon = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_2_moon);
        mBitmapSunWidth = mBitmapSun.getWidth() / 2;
        mBitmapSunHeight = mBitmapSun.getHeight() / 2;
        mBitmapMoonWidth = mBitmapMoon.getWidth() / 2;
        mBitmapMoonHeight = mBitmapMoon.getHeight() / 2;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        DashPathEffect dashPath = new DashPathEffect(new float[]{4, 5}, (float) 1.0);
        mPaint.setPathEffect(dashPath);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        XCOORD = size.x / 2;
        YCOORD = (float) (size.y * 0.28);
        RADIUS = (float) (7 * 1.0 / 18) * size.x;

        mPos = new float[2];
        mTan = new float[2];
        mDistance = 0f;
    }

    private void initNewPath(Path.Direction direction) {
        mPath = new Path();
        RectF rectF = new RectF(XCOORD - RADIUS, YCOORD - RADIUS, XCOORD + RADIUS, YCOORD + RADIUS);
        //顺时针
        if (direction == Path.Direction.CW) {

            mPath.addArc(rectF, 50, 359);
        } else {
            mPath.addArc(rectF, 50, -359);
        }
        mPath.close();
        mPathSunMeasure = new PathMeasure(mPath, false);
        mPathLength = mPathSunMeasure.getLength();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);

        mMatrix.reset();
        mPathSunMeasure.getPosTan(mDistance, mPos, mTan);
        mMatrix.postTranslate(mPos[0] - mBitmapSunWidth, mPos[1] - mBitmapSunHeight);
        canvas.drawBitmap(mBitmapSun, mMatrix, null);
        mMatrix.reset();

        float x = 2 * XCOORD - mPos[0];
        float y = 2 * YCOORD - mPos[1];
        mMatrix.postTranslate(x - mBitmapMoonWidth, y - mBitmapMoonHeight);
        canvas.drawBitmap(mBitmapMoon, mMatrix, null);
    }

    public void startAnim() {
        initNewPath(Path.Direction.CW);
        play();
    }

    private void play() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0, mPathLength);
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.setDuration(1500);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mDistance = (Float) animation.getAnimatedValue();
                    mPathSunMeasure.getPosTan(mDistance, mPos, mTan);
                    postInvalidate();
                }
            });
        }
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        mValueAnimator.start();
    }

    public void reverse() {
        initNewPath(Path.Direction.CCW);
        play();
    }

    public void rotationAnim(boolean scrollToLeft, float position) {

        if (scrollToLeft) {   //scroll to left direction,clockwise direction

            if (mCurrentDirection == Path.Direction.CW) {
                mCurrentDirection = Path.Direction.CCW;
                initNewPath(Path.Direction.CCW);

            }

            if (Math.abs(position) > 1) {

            } else {

                mDistance = mPathLength / 2 * (Math.abs(1 + position));
            }


        } else { //counter-clockwise direction

            if (mCurrentDirection == Path.Direction.CCW) {
                mCurrentDirection = Path.Direction.CW;
                initNewPath(Path.Direction.CW);

            }
            mDistance = mPathLength / 2 * (Math.abs(1 + position));

        }
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }

}
