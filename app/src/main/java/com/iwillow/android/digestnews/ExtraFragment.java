package com.iwillow.android.digestnews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwillow.android.digestnews.entity.DetailItem;
import com.iwillow.android.digestnews.view.CircularRevealView;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.view.CircleLayout;
import com.iwillow.android.lib.widget.BaseFragment;

import java.util.ArrayList;

import io.realm.Realm;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by https://www.github.com/iwillow on 2016/5/17.
 */
public class ExtraFragment extends BaseFragment {
    private String TAG = "ExtraFragment";
    private TextView toggleButton;
    private CircularRevealView circularRevealView;
    private CircleLayout circleLayout;
    private LinearLayout readIndicator;
    private TextView read;
    private LinearLayout know;
    private TextView bigTitle;
    private TextView smallTitle;
    ArrayList<DetailItem> detailItemArrayList;
    private boolean allChecked;
    private Realm realm;
    private Subscription subscription;
    private Handler mHandler;

    public ExtraFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        detailItemArrayList = getArguments().getParcelableArrayList("data");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read_extra_news;
    }

    @Override
    protected void initView(View rootView) {
        Typeface typeface = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        final int readColor = getResources().getColor(R.color.read_color);
        toggleButton = $(rootView, R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(0x110);
                getActivity().finish();
            }
        });
        circularRevealView = $(rootView, R.id.revalView);
        readIndicator = $(rootView, R.id.readIndicator);
        circleLayout = $(rootView, R.id.circleLayout);
        bigTitle = $(rootView, R.id.bigTitle);
        smallTitle = $(rootView, R.id.smallTitle);
        bigTitle.setTypeface(typeface);
        smallTitle.setTypeface(typeface);
        read = $(rootView, R.id.read);
        know = $(rootView, R.id.know);
        TextView urd = $(rootView, R.id.uread);
        Typeface ty = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Light.ttf");
        urd.setTypeface(ty);
        read.setTypeface(ty);
        read.setText("");
        if (detailItemArrayList != null && detailItemArrayList.size() > 0) {
            read.setText(circleLayout.getActiveCount() + " of " + detailItemArrayList.size());
            for (int i = 0; i < detailItemArrayList.size(); i++) {
                int activeColor = detailItemArrayList.get(i).color;
                circleLayout.addItem("" + (i + 1), activeColor);
            }
            for (int i = 0; i < detailItemArrayList.size(); i++) {
                if (detailItemArrayList.get(i).checked && !circleLayout.isItemActive(i)) {
                    circleLayout.activeItem(i);
                    read.setText(circleLayout.getActiveCount() + " of " + detailItemArrayList.size());
                }
            }

            if (circleLayout.getActiveCount() == detailItemArrayList.size()) {
                toggleButton.setTextColor(android.graphics.Color.WHITE);
                readIndicator.setVisibility(View.GONE);
                circleLayout.setVisibility(View.GONE);
                toggleButton.setTextColor(android.graphics.Color.WHITE);
                Drawable bottom = readIndicator.getContext().getResources().getDrawable(R.mipmap.extranews_arrow_down_w);
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bottom);
                know.setVisibility(View.VISIBLE);
                circleLayout.setClickable(false);
                circleLayout.setEnabled(false);
                circularRevealView.setBackgroundColor(readColor);

            } else {

                readIndicator.setVisibility(View.VISIBLE);
                know.setVisibility(View.INVISIBLE);
                circleLayout.setOnChildViewClickListener(new CircleLayout.OnChildViewClickListener() {
                    @Override
                    public void onChildViewClick(View childView, int index) {
                        // circleLayout.activeItem(index);
                        if (getActivity() instanceof NewsDetailActivity) {
                            ((NewsDetailActivity) getActivity()).setCurrentPosition(index);
                        }
                    }
                });

                circleLayout.setCircleLayoutAnimationListener(new CircleLayout.CircleLayoutAnimationListener() {
                    @Override
                    public void onAnimationMarqueeStart() {

                    }

                    @Override
                    public void onAnimationMarqueeEnd() {

                    }

                    @Override
                    public void onAnimationShrinkStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationShrinkEnd(Animator animation) {
                        readIndicator.setVisibility(View.GONE);
                        circleLayout.setVisibility(View.GONE);
                        circleLayout.setClickable(false);
                        circleLayout.setEnabled(false);
                        circularRevealView.reveal(circularRevealView.getMeasuredWidth() / 2 - 10, circularRevealView.getMeasuredHeight() / 2 - 10, readColor, 20, 500, new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                toggleButton.setTextColor(android.graphics.Color.WHITE);
                                Drawable bottom = readIndicator.getContext().getResources().getDrawable(R.mipmap.extranews_arrow_down_w);
                                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bottom);
                                know.setVisibility(View.VISIBLE);
                            }
                        });
                        allChecked = true;
                    }
                });
            }
        }
        initHandler();
        subscription = update();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initHandler() {
        if (mHandler == null) {

            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    switch (msg.what) {
                        case 0x110:
                            int index = (int) msg.obj;
                            circleLayout.activeItem(index);
                            read.setText(circleLayout.getActiveCount() + " of " + detailItemArrayList.size());
                            break;
                        default:
                            break;

                    }
                }
            };
        }

    }


    private Subscription update() {
        if (getActivity() instanceof NewsDetailActivity) {
            return ((NewsDetailActivity) getActivity())
                    .getRxBus()
                    .toObserverable(ArrayList.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList>() {
                        @Override
                        public void onCompleted() {
                            LogUtil.e(TAG, "onCompleted ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            LogUtil.e(TAG, "onError ");
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(ArrayList arrayList) {
                            if (arrayList != null && !allChecked) {
                                detailItemArrayList = arrayList;
                                for (int i = 0; i < detailItemArrayList.size(); i++) {
                                    if (detailItemArrayList.get(i).checked && !circleLayout.isItemActive(i)) {
                                        Message msg = mHandler.obtainMessage(0x110, i);
                                        mHandler.sendMessageDelayed(msg, 500);
                                    }
                                }
                            }
                        }
                    });
        }
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        if (mHandler != null) {
            mHandler.removeMessages(0x110);
        }
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
