package com.iwillow.android.digestnews.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

import com.iwillow.android.lib.log.LogUtil;

/**
 * Created by https://www.github.com/iwillow on 2016/5/21.
 */
public class SimpleRippleView extends View {

    private static final String TAG = "SimpleRippleView";

    private Path mPathCircle;
    private Path mPathRect;
    private Paint mPaintRect;
    private float mStartRadius;
    private float mEndRadius;
    private float mCenterX;
    private float mCenterY;
    private ValueAnimator valueAnimator;
    private float mDefaultEndRadius;

    public SimpleRippleView(Context context) {
        super(context);
        initPath(context);
    }

    public SimpleRippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPath(context);
    }

    public SimpleRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPath(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleRippleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPath(context);
    }

    private void initPath(Context context) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int w = displayMetrics.widthPixels / 2;
        int h = displayMetrics.heightPixels / 2;
        mDefaultEndRadius = (float) (1.5f * Math.hypot(w, h));
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

    public void reveal(final float centerX, final float centerY, float startRadius, int duration, @ColorInt int color, final Animator.AnimatorListener animatorListener) {
        requestLayout();
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        if (startRadius > mDefaultEndRadius) {
            startRadius = mDefaultEndRadius;
        }
        if (startRadius < 0) {
            startRadius = 0;
        }
        float endRadius = mEndRadius;
        if (endRadius == 0) {
            endRadius = mDefaultEndRadius;
        }
        mPaintRect.setColor(color);
        valueAnimator = ValueAnimator.ofFloat(startRadius, endRadius);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AnticipateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float r = (float) animation.getAnimatedValue();
                mPathCircle.reset();
                mPathCircle.addCircle(centerX, centerY, r, Path.Direction.CCW);
                mPathCircle.close();
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationStart(animation);
                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationRepeat(animation);
                }
            }
        });
        valueAnimator.start();
    }

    public void shrink(final float centerX, final float centerY, float endRadius, int duration, @ColorInt int color, final Animator.AnimatorListener animatorListener) {
        requestLayout();
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        if (endRadius > mDefaultEndRadius) {
            endRadius = mDefaultEndRadius;
        }
        if (endRadius < 0) {
            endRadius = 0;
        }
        float startRadius = mEndRadius;
        if (startRadius == 0) {
            startRadius = mDefaultEndRadius;
        }
        mPaintRect.setColor(color);
        valueAnimator = ValueAnimator.ofFloat(startRadius, endRadius);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AnticipateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float r = (float) animation.getAnimatedValue();
                mPathCircle.reset();
                mPathCircle.addCircle(centerX, centerY, r, Path.Direction.CCW);
                mPathCircle.close();
                invalidate();
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationStart(animation);
                }

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationRepeat(animation);
                }
            }
        });
        valueAnimator.start();
    }

}
