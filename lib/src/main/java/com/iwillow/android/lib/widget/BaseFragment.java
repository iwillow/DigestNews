package com.iwillow.android.lib.widget;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by https://github.com/iwillow on 2016/4/19.
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            initView(mRootView);
        }
        return mRootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View rootView);

    protected <T extends View> T $(View rootView, @IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }
}
