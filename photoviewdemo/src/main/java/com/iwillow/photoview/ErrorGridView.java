package com.iwillow.photoview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2016/5/20.
 */
public class ErrorGridView extends GridView {

    public ErrorGridView(Context context) {
        super(context);
    }

    public ErrorGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
