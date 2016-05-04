package com.iwillow.android.lib.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;

import com.iwillow.android.lib.R;
import com.iwillow.android.lib.log.Log;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.util.DimensionUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by https://github.com/iwillow on 2016/4/15.
 */
public class CircleLayout extends ViewGroup {
    private static final String TAG = CircleLayout.class.getSimpleName();
    private List<CircleItem> mData = new ArrayList<>();
    private int mActiveCount;
    private int mCenterX;
    private int mCenterY;

    private CircleLayoutAnimationListener mCircleLayoutAnimationListener;

    private OnChildViewClickListener mOnChildViewClickListener;

    public CircleLayout(Context context) {
        super(context);
    }

    public CircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtil.d(TAG, "onMeasure called");
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));
        int minh = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();
        int h = Math.max(minh, MeasureSpec.getSize(heightMeasureSpec));
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(w, h);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 300;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 300;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.d(TAG, "onLayout called");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() > 0) {
            mCenterX = getMeasuredWidth() / 2;
            mCenterY = getMeasuredHeight() / 2;
            int radius = Math.min(mCenterX, mCenterY);
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                if (view.getVisibility() != GONE) {
                    int cx = (int) (mCenterX + 7 * radius * Math.sin(i * 2 * Math.PI / count) / 9);
                    int cy = (int) (mCenterY - 7 * radius * Math.cos(i * 2 * Math.PI / count) / 9);
                    int left = cx - view.getMeasuredWidth() / 2;
                    int top = cy - view.getMeasuredHeight() / 2;
                    int right = cx + view.getMeasuredWidth() / 2;
                    int bottom = cy + view.getMeasuredHeight() / 2;

                    view.layout(left, top, right, bottom);
                }
            }
        } else {
            LogUtil.d(TAG, "getChildCount() <= 0");

        }

    }

    private class CircleItem {
        String text;
        int color;
        boolean activation;
    }

    public int addItem(String text, @ColorInt int color) {
        LogUtil.d(TAG, "addItem called");
        CircleItem item = new CircleItem();
        item.text = text;
        item.color = color;
        mData.add(item);
        final DonutProgress textView = new DonutProgress(getContext());
        LayoutParams layoutParams = new LayoutParams((int) DimensionUtil.dp2px(getResources(), 30f), (int) DimensionUtil.dp2px(getResources(), 30f));
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setTextSize(DimensionUtil.dp2px(getResources(), 20f));
        textView.setTextColor(Color.GRAY);
        textView.setInnerBackgroundColor(Color.TRANSPARENT);
        textView.setUnfinishedStrokeWidth(DimensionUtil.dp2px(getResources(), 1f));
        textView.setFinishedStrokeWidth(DimensionUtil.dp2px(getResources(), 1f));
        textView.setUnfinishedStrokeColor(Color.GRAY);
        textView.setFinishedStrokeColor(Color.GRAY);
        addView(textView);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = indexOfChild(v);
                if (mOnChildViewClickListener != null && index >= 0 && index < getChildCount()) {
                    mOnChildViewClickListener.onChildViewClick(v, index);
                }
            }
        });

        return mData.size() - 1;
    }


    public int addItem(@LayoutRes int resId, String text, @ColorInt int color) {
        LogUtil.d(TAG, "addItem called");
        CircleItem item = new CircleItem();
        item.text = text;
        item.color = color;
        mData.add(item);
        final DonutProgress textView = (DonutProgress) LayoutInflater.from(getContext()).inflate(resId, null);
        textView.setText(text);
        textView.setUnfinishedStrokeColor(Color.GRAY);
        textView.setFinishedStrokeColor(Color.GRAY);
        textView.setBackgroundColor(Color.TRANSPARENT);
        textView.setInnerBackgroundColor(Color.TRANSPARENT);
        addView(textView);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final int index = indexOfChild(v);
                if (mOnChildViewClickListener != null && index >= 0 && index < getChildCount()) {
                    mOnChildViewClickListener.onChildViewClick(v, index);
                }
            }
        });

        return mData.size() - 1;
    }

    public void activeItem(int index) {

        if (index > -1 && index < getChildCount() && mData.size() > 0 && index < mData.size()) {
            if (!mData.get(index).activation) {
                mData.get(index).activation = true;
                mActiveCount++;
                if (mActiveCount > mData.size()) {
                    mActiveCount = mData.size();
                }
                DonutProgress textView = (DonutProgress) getChildAt(index);
                CircleItem item = mData.get(index);
                textView.setTextColor(Color.WHITE);
                textView.setInnerBackgroundColor(item.color);
                textView.setUnfinishedStrokeColor(item.color);
                textView.setFinishedStrokeColor(item.color);
                LogUtil.d(TAG, "active item: " + index);
            } else {
                LogUtil.d(TAG, "item: " + index + " actived");
            }
            if (mActiveCount == mData.size()) {
                marquee();
            }
        }

    }

    public int getActiveCount() {
        return mActiveCount;
    }

    public boolean isAllActived() {
        return mActiveCount > 0 && mActiveCount == mData.size();
    }

    public void marquee() {
        if (mCircleLayoutAnimationListener != null) {
            mCircleLayoutAnimationListener.onAnimationMarqueeStart();
        }
        mHandler.sendEmptyMessage(0x110);
    }

    private void shrink() {
        if (getChildCount() > 0) {
            AnimatorSet animatorSet = new AnimatorSet();
            Collection<Animator> list = new ArrayList<>();
            for (int i = 0; i < getChildCount(); i++) {
                DonutProgress textView = (DonutProgress) getChildAt(i);
                //textView.animate().scaleX(0f).scaleY(0f).alpha(0f).x(mCenterX - textView.getMeasuredWidth() / 2).y(mCenterY - textView.getMeasuredHeight() / 2).setDuration(1000).setInterpolator(new AnticipateInterpolator());
                PropertyValuesHolder pvX = PropertyValuesHolder.ofFloat("x", mCenterX - textView.getMeasuredWidth() / 2.0f);
                PropertyValuesHolder pvY = PropertyValuesHolder.ofFloat("y", mCenterY - textView.getMeasuredHeight() / 2.0f);
                PropertyValuesHolder pvScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f);
                PropertyValuesHolder pvScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f);
                PropertyValuesHolder pvAlpha = PropertyValuesHolder.ofFloat("alpha", 0f);
                Animator animator = ObjectAnimator.ofPropertyValuesHolder(textView, pvX, pvY, pvScaleX, pvScaleY, pvAlpha);
                // animator.setDuration(1000).setInterpolator(new AnticipateInterpolator());
                list.add(animator);

            }
            animatorSet.playTogether(list);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (mCircleLayoutAnimationListener != null) {
                        mCircleLayoutAnimationListener.onAnimationShrinkStart(animation);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mCircleLayoutAnimationListener != null) {
                        mCircleLayoutAnimationListener.onAnimationShrinkEnd(animation);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorSet.setDuration(300).setInterpolator(new AnticipateInterpolator());
            animatorSet.start();
        }
    }

    private Handler mHandler = new Handler() {
        int index = 0;

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0x110:
                    if (!isAllActived()) {
                        activeItem(index);
                        index++;
                        sendEmptyMessageDelayed(0x110, 150);
                    } else {
                        removeMessages(0x110);
                        index = 0;
                        if (mCircleLayoutAnimationListener != null) {
                            mCircleLayoutAnimationListener.onAnimationMarqueeEnd();
                        }
                        shrink();
                        Log.d(TAG, "all the items have been actived");
                    }
                    break;
                default:
                    break;

            }
        }


    };

    @Override
    protected void onDetachedFromWindow() {
        if (mHandler != null && mHandler.hasMessages(0x110)) {
            mHandler.removeMessages(0x110);
        }
        mHandler = null;
        super.onDetachedFromWindow();
    }

    public void setOnChildViewClickListener(OnChildViewClickListener onChildViewClickListener) {
        mOnChildViewClickListener = onChildViewClickListener;

    }

    public void setCircleLayoutAnimationListener(CircleLayoutAnimationListener animationListener) {
        mCircleLayoutAnimationListener = animationListener;
    }


    public interface CircleLayoutAnimationListener {


        void onAnimationMarqueeStart();

        void onAnimationMarqueeEnd();

        void onAnimationShrinkStart(Animator animation);

        void onAnimationShrinkEnd(Animator animation);
    }

    public interface OnChildViewClickListener {
        /**
         * @param childView the child view you click on
         * @param index     the child view's index  you click on
         */
        void onChildViewClick(View childView, int index);
    }

}
