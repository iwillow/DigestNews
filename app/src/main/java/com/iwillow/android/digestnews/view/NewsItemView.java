package com.iwillow.android.digestnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iwillow.android.digestnews.R;
import com.iwillow.android.digestnews.entity.Color;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.entity.Source;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.view.DonutProgress;

import io.realm.RealmList;

/**
 * Created by https://wwww.github.com/iwillow on 2016/4/28.
 */
public class NewsItemView extends ViewGroup {
    private static final String TAG = NewsItemView.class.getSimpleName();
    View mItemView;
    private DonutProgress mDonutProgress;
    private TextView mTitle;
    private TextView mLabel;
    private TextView mSources;
    private LinearLayout images;
    private int mStateColor;

    public NewsItemView(Context context) {
        super(context);
        addItemView();
    }

    public NewsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addItemView();
    }

    public NewsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addItemView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minW, MeasureSpec.getSize(widthMeasureSpec));
        int minH = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();
        int h = Math.max(minH, MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogUtil.d(TAG, "onLayout called");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mItemView != null) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();

            int width = mItemView.getMeasuredWidth();
            int height = mItemView.getMeasuredHeight();
            int right = paddingLeft + width + paddingRight;
            int bottom = paddingTop + height + paddingBottom;
            if (paddingLeft + width > getMeasuredWidth()) {
                right = getMeasuredWidth();
            }
            mItemView.layout(paddingLeft, paddingTop, right, bottom);
        }

    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return 300;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return 300;
    }

    private void addItemView() {
        final View itemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, null);
        itemView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mItemView = itemView;
        mDonutProgress = (DonutProgress) mItemView.findViewById(R.id.index);
        mLabel = (TextView) mItemView.findViewById(R.id.label);
        mTitle = (TextView) mItemView.findViewById(R.id.title);
        mSources = (TextView) mItemView.findViewById(R.id.sources);
        images = (LinearLayout) mItemView.findViewById(R.id.images);
        addView(mItemView);
    }

    public void setItemRealm(ItemRealm itemRealm, int index) {
        if (itemRealm != null) {
            mDonutProgress.setText("" + index);
            if (itemRealm.getColors() != null && itemRealm.getColors().size() > 0) {
                Color color = itemRealm.getColors().get(0);
                mStateColor = android.graphics.Color.parseColor(color.getHexcode());
                mDonutProgress.setFinishedStrokeColor(mStateColor);
                mDonutProgress.setUnfinishedStrokeColor(mStateColor);
                mDonutProgress.setTextColor(mStateColor);
                mDonutProgress.setInnerBackgroundColor(android.graphics.Color.TRANSPARENT);
            }
            if (itemRealm.getCategories() != null && itemRealm.getCategories().size() > 0) {
                mLabel.setText("" + itemRealm.getCategories().get(0).getLabel());
            }
            mTitle.setText("" + itemRealm.getTitle());
            RealmList<Source> sources = itemRealm.getSources();
            if (sources != null && sources.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Source source : sources) {
                    sb.append(source.getPublisher()).append(",");
                }
                mSources.setText("" + sb.subSequence(0, sb.lastIndexOf(",")));
            }
        }

    }


    public void active() {
        mDonutProgress.setInnerBackgroundColor(mStateColor);
        mDonutProgress.setTextColor(android.graphics.Color.WHITE);
    }

}
