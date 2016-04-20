package com.iwillow.android.digestnews;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iwillow.android.lib.widget.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsListActivityFragment extends BaseFragment {


    public NewsListActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView(View rootView) {

    }
}
