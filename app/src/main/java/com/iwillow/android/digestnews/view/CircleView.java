package com.iwillow.android.digestnews.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.iwillow.android.digestnews.R;
import com.iwillow.android.lib.util.BitmapUtil;

/**
 * the third product guide page.
 * Created by https://github.com/iwillow  2016/4/13.
 */
public class CircleView extends View {

    private final String TAG = CircleView.class.getSimpleName();

    private Bitmap mBitmap[] = new Bitmap[6];

    private Paint mPaint;

    private int size;

    private Path mPathRotation;

    private Path mPathRadical[] = new Path[6];
    private float mPathRadicalLength[] = new float[6];
    private float mPathRadicalDistance[] = new float[6];
    private PathMeasure mPathMeasureRadical[] = new PathMeasure[6];
    private float mBitmapScaleCounter[] = new float[6];    // sacle counter

    private float mSpeedGradient;

    private int mRandomArr[] = new int[]{500, 400, 300, 900, 800, 1100};

    private Matrix mMatrix[] = new Matrix[6];
    private int mCircleX;
    private int mCircleY;
    private int mRadius;
    private float mTempDistanceX;
    private float mTempDistanceY;
    private PathMeasure mPathMeasureRotation;
    private float mPathLength;
    private boolean mUpperBoundHit;
    private int mRadicalCounter = 0;
    private boolean mInitPathRadical = false;


    BitmapState mState = BitmapState.TRANSLATION;

    public enum BitmapState {

        /**
         * 刚进来的时候移动状态
         */
        TRANSLATION,

        /**
         * 旋转状态
         */
        ROTATION,

        /**
         * 径向收缩
         */
        RADICAL
    }

    private OnStateChangedListener mOnStateChangedListener;


    private float mBitmapCoord[][] = new float[6][2];


    private float mStep;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        Bitmap srcBitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_3_atom01);
        mBitmap[0] = BitmapUtil.createCircleBitmap(srcBitmap1, true, Bitmap.Config.ARGB_8888);


        Bitmap srcBitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_3_atom02);
        mBitmap[1] = BitmapUtil.createCircleBitmap(srcBitmap2, true, Bitmap.Config.ARGB_8888);


        Bitmap srcBitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_3_atom03);
        mBitmap[2] = BitmapUtil.createCircleBitmap(srcBitmap3, true, Bitmap.Config.ARGB_8888);


        Bitmap srcBitmap4 = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_3_atom04);
        mBitmap[3] = BitmapUtil.createCircleBitmap(srcBitmap4, true, Bitmap.Config.ARGB_8888);


        Bitmap srcBitmap5 = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_3_atom05);
        mBitmap[4] = BitmapUtil.createCircleBitmap(srcBitmap5, true, Bitmap.Config.ARGB_8888);


        Bitmap srcBitmap6 = BitmapFactory.decodeResource(getResources(), R.mipmap.onboarding_3_atom06);
        mBitmap[5] = BitmapUtil.createCircleBitmap(srcBitmap6, true, Bitmap.Config.ARGB_8888);


        size = mBitmap[0].getWidth();


        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.STROKE);
        mPathRotation = new Path();
        mPathMeasureRotation = new PathMeasure();

    }


    public void setState(BitmapState state) {
        mState = state;
        invalidate();
    }

    public void translateTheSpheres(float speedGradient) {
        mSpeedGradient = speedGradient;
        if (mState != BitmapState.TRANSLATION) {
            mState = BitmapState.TRANSLATION;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(size, size);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode != MeasureSpec.AT_MOST) {
            setMeasuredDimension(size, heightMeasure);
        } else if (widthMode != MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasure, size);
        } else {
            setMeasuredDimension(widthMeasure, heightMeasure);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            final int x = getWidth() - getPaddingLeft() - getPaddingRight();
            final int y = getHeight() - getPaddingTop() - getPaddingBottom();
            mCircleX = getPaddingLeft() + x / 2;
            mCircleY = getPaddingTop() + y / 2;
            mTempDistanceX = (float) (1.0 / 14) * mCircleX;
            mTempDistanceY = (float) (1.0 / 14) * mCircleY;
            mRadius = 1 * Math.min(x, y) / 4;
            mPathRotation.reset();
            mPathRotation.addCircle(mCircleX, mCircleY, mRadius, Path.Direction.CW);

            mPathRotation.close();
            mPathMeasureRotation.setPath(mPathRotation, false);
            mPathLength = mPathMeasureRotation.getLength();
            invalidateRotaitonCoordinates();

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == BitmapState.TRANSLATION) {
            mRadicalCounter = 0;
            for (int i = 0; i < 6; i++) {
                float random = mSpeedGradient * mRandomArr[i];
                float left = mBitmapCoord[i][0] + random - mBitmap[i].getWidth() / 2;
                float top = mBitmapCoord[i][1] - mBitmap[i].getHeight() / 2;
                canvas.drawBitmap(mBitmap[i], left, top, null);
            }


        } else if (mState == BitmapState.ROTATION) {

            mRadicalCounter = 0;
            canvas.drawPath(mPathRotation, mPaint);

            for (int i = 0; i < 6; i++) {
                float left = mBitmapCoord[i][0] - mBitmap[i].getWidth() / 2;
                float top = mBitmapCoord[i][1] - mBitmap[i].getHeight() / 2;
                canvas.drawBitmap(mBitmap[i], left, top, null);
            }
            invalidateRotaitonCoordinates();
            invalidate();

        } else if (mState == BitmapState.RADICAL) {

            if (!mInitPathRadical) {
                initPathRadical();
                mInitPathRadical = true;
            }

            moveCircleInOut(canvas);

            if (mRadicalCounter < 6) {
                invalidate();
            } else {
                if (mOnStateChangedListener != null) {
                    mOnStateChangedListener.onRadicalMoveOver();
                }
            }

        }

    }


    private void invalidateRotaitonCoordinates() {

        if (mPathLength == 0 || mPathMeasureRotation.getLength() == 0) {
            return;
        }
        mStep += mPathLength / 400;

        if (mStep >= mPathLength) {
            mStep = 0;
        }

        for (int i = 0; i < 6; i++) {
            float distance = i * mPathLength / 6 + mStep;
            if (distance >= mPathLength) {
                distance = distance % mPathLength;
            }
            mPathMeasureRotation.getPosTan(distance, mBitmapCoord[i], null);
        }
    }


    private void initPathRadical() {


        for (int i = 0; i < 6; i++) {

            mMatrix[i] = new Matrix();

            double len = Math.hypot((mBitmapCoord[i][0] - mCircleX), (mBitmapCoord[i][1] - mCircleY));
            double dx = (mBitmapCoord[i][0] - mCircleX) / len;
            double dy = (mBitmapCoord[i][1] - mCircleY) / len;

            float x = (float) (mBitmapCoord[i][0] + mTempDistanceX * dx);
            float y = (float) (mBitmapCoord[i][1] + mTempDistanceY * dy);


            mPathRadical[i] = new Path();
            mPathRadical[i].moveTo(mCircleX, mCircleY);
            mPathRadical[i].lineTo(mBitmapCoord[i][0], mBitmapCoord[i][1]);
            mPathRadical[i].lineTo(x, y);
            mPathRadical[i].close();
            mPathMeasureRadical[i] = new PathMeasure();
            mPathMeasureRadical[i].setPath(mPathRadical[i], false);
            mPathRadicalLength[i] = mPathMeasureRadical[i].getLength();
            mPathRadicalDistance[i] = mRadius;
            mBitmapScaleCounter[i] = 0f;

        }
    }

    private void moveCircleInOut(Canvas canvas) {

        for (int i = 0; i < 6; i++) {

            if (mPathRadicalDistance[i] >= (mPathRadicalLength[i] / 2)) {
                mUpperBoundHit = true;
            }

            if (mUpperBoundHit) {

                int newWidth = (int) (mBitmap[i].getWidth() - 8 * (mBitmapScaleCounter[i] / 1));
                int newHeight = (int) (mBitmap[i].getHeight() - 8 * (mBitmapScaleCounter[i] / 1));
                if (newWidth >= 5 && newHeight >= 5) {
                    mBitmap[i] = Bitmap.createScaledBitmap(mBitmap[i], newWidth, newHeight, true);
                    mBitmapScaleCounter[i] += 1;

                } else {
                    mBitmap[i] = Bitmap.createScaledBitmap(mBitmap[i], newWidth, newHeight, true);
                }
            }
            if (mPathRadicalDistance[i] < mPathRadicalLength[i]) {

                float position[] = new float[2];
                float tangent[] = new float[2];

                mPathMeasureRadical[i].getPosTan(mPathRadicalDistance[i], position, tangent);
                mMatrix[i].reset();
                mMatrix[i].postTranslate(position[0] - mBitmap[i].getWidth() / 2, position[1] - mBitmap[i].getHeight() / 2);
                canvas.drawBitmap(mBitmap[i], mMatrix[i], null);

                if (mUpperBoundHit) {

                    mPathRadicalDistance[i] += 20;

                } else {

                    mPathRadicalDistance[i] += 2;
                }
            } else {

                mRadicalCounter++;
            }
        }

    }


    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        mOnStateChangedListener = onStateChangedListener;
    }

    public interface OnStateChangedListener {

        void onRadicalMoveOver();
    }
}
