package com.iwillow.android.lib.widget;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by https://github.com/ on 2016/4/19.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        setEventListeners();
    }

    protected abstract void initView();

    protected abstract void setEventListeners();

    /**
     * @param resId
     * @param <T>
     * @return view
     */
    protected <T extends View> T $(@IdRes int resId) {
        return (T) super.findViewById(resId);
    }

    protected <T extends View> T $(View rootView, @IdRes int resId) {
        return (T) rootView.findViewById(resId);
    }
}
