package com.iwillow.android.digestnews;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.iwillow.android.digestnews.view.CircularRevealView;
import com.iwillow.android.lib.widget.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsListActivityFragment extends BaseFragment {


    public NewsListActivityFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView(View rootView) {


        final Button reval = $(rootView, R.id.reval);
        final Button hide = $(rootView, R.id.hide);
        final CircularRevealView revalView = $(rootView, R.id.revalView);

        int position[] = new int[2];
        rootView.getLocationOnScreen(position);

        final int x = position[0] / 2;
        final int y = position[1] / 2;
        // int size = Math.max(rootView.getWidth(), rootView.getHeight());
        reval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ViewCompat.animate(revalView).scaleX(scale).scaleY(scale).setInterpolator(new AnticipateOvershootInterpolator()).setDuration(500).start();
                final int color = Color.parseColor("#ff0000");
                revalView.reveal(0, 0, color, 10, 440, null);

            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int color = Color.TRANSPARENT;
                revalView.hide(x, y, color, 10, 1000, null);
            }
        });
      

    }
}
