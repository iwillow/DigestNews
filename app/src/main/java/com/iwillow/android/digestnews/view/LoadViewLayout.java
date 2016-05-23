package com.iwillow.android.digestnews.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.iwillow.android.digestnews.R;

/**
 * Created by https://www.github.com on 2016/5/21.
 */
public class LoadViewLayout extends FrameLayout {
    private final String TAG = "LoadViewLayout";
    private View childView;
    private View error;
    private ErrorGridView gridView;
    private Button reload;
    private LoadingView loadingView;
    private SimpleRippleView simpleRippleView;

    public OnLoadNewsListener onLoadNewsListener;

    public LoadViewLayout(Context context) {
        super(context);
        initChildView(context);
    }

    public LoadViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChildView(context);
    }

    public LoadViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadViewLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initChildView(context);
    }

    private void initChildView(Context context) {
        childView = LayoutInflater.from(context).inflate(R.layout.loading_digest_view, this);
        error = childView.findViewById(R.id.error);
        error.setVisibility(VISIBLE);
        gridView = (ErrorGridView) childView.findViewById(R.id.gvExclamation);
        gridView.setAdapter(new GridAdapter());
        reload = (Button) childView.findViewById(R.id.btReloadButton);
        reload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLoadNewsListener != null) {
                    onLoadNewsListener.onReload();
                }
            }
        });
        loadingView = (LoadingView) childView.findViewById(R.id.loadingView);
        simpleRippleView = (SimpleRippleView) childView.findViewById(R.id.simpleRippleView);
        loadingView.setVisibility(GONE);
        simpleRippleView.setVisibility(GONE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getMeasuredHeight() - left - getPaddingRight();
        int bottom = getMeasuredHeight() - top - getPaddingBottom();
        childView.layout(left, top, right, bottom);
    }

    public static class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 49;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_error, parent, false);
                viewHolder.itemView = convertView;
                viewHolder.textView = (TextView) convertView.findViewById(R.id.id_num);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position == 10 || position == 17 || position == 24 || position == 38) {
                viewHolder.textView.setBackgroundColor(Color.parseColor("#f33692"));
            }
            return convertView;
        }

        public static class ViewHolder {
            public View itemView;
            public TextView textView;
        }
    }

    public OnLoadNewsListener getOnLoadNewsListener() {
        return onLoadNewsListener;
    }

    public void setOnLoadNewsListener(OnLoadNewsListener onLoadNewsListener) {
        this.onLoadNewsListener = onLoadNewsListener;
    }

    public void onLoading() {
        error.setVisibility(GONE);
        simpleRippleView.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        loadingView.setState(LoadingView.STATE_ROTATION);
        if (onLoadNewsListener != null) {
            onLoadNewsListener.onLoading();
        }
    }


    public void onLoadSuccess() {
        error.setVisibility(GONE);
        simpleRippleView.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
        loadingView.setState(LoadingView.STATE_RADICAL);
        loadingView.setOnLoadingAnimationListener(new LoadingView.OnLoadingAnimationListener() {
            @Override
            public void onLoading() {
                if (onLoadNewsListener != null) {
                    onLoadNewsListener.onLoading();
                }
            }

            @Override
            public void onLoadEnd() {
                loadingView.setVisibility(GONE);
                float cx = getMeasuredWidth() / 2;
                float cy = getMeasuredHeight() / 2;
                simpleRippleView.reveal(cx, cy, 30, 400, Color.WHITE, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        simpleRippleView.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        simpleRippleView.setVisibility(GONE);
                        if (onLoadNewsListener != null) {
                            onLoadNewsListener.onLoadSuccess();
                        }
                    }
                });

            }
        });


    }

    public void onLoadError(final Throwable e) {

        loadingView.setVisibility(VISIBLE);
        loadingView.setState(LoadingView.STATE_RADICAL);
        loadingView.setOnLoadingAnimationListener(new LoadingView.OnLoadingAnimationListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoadEnd() {
                loadingView.setVisibility(GONE);
                error.setVisibility(VISIBLE);
                float cx = getMeasuredWidth() / 2;
                float cy = getMeasuredHeight() / 2;
                simpleRippleView.reveal(cx, cy, 30, 400, Color.WHITE, new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        simpleRippleView.setVisibility(VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        simpleRippleView.setVisibility(GONE);
                        if (onLoadNewsListener != null) {
                            onLoadNewsListener.onLoadError(e);
                        }
                    }
                });


            }
        });


    }

    public interface OnLoadNewsListener {


        void onLoading();

        void onReload();

        void onLoadSuccess();

        void onLoadError(Throwable e);
    }
}
