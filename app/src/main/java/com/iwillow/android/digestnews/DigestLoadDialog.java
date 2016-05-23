package com.iwillow.android.digestnews;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.executor.FifoPriorityThreadPoolExecutor;
import com.iwillow.android.digestnews.view.LoadViewLayout;

/**
 * Created by https://www.github.com/iwillow on 2016/5/20.
 */
public class DigestLoadDialog extends DialogFragment {
    LoadViewLayout loadViewLayout;
    FifoPriorityThreadPoolExecutor d;
    OnNewsLoadInActivityListener mOnNewsLoadInActivityListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof NewsListActivity){
            mOnNewsLoadInActivityListener= (OnNewsLoadInActivityListener) activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.loading_dialog_view, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        loadViewLayout = (LoadViewLayout) root;
        loadViewLayout.setOnLoadNewsListener(new LoadViewLayout.OnLoadNewsListener() {
            @Override
            public void onReload() {
                loadInfo();
                if(mOnNewsLoadInActivityListener!=null){
                    mOnNewsLoadInActivityListener.onLoad();
                }
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onLoadSuccess() {
                DigestLoadDialog.this.dismiss();
            }

            @Override
            public void onLoadError(Throwable e) {

            }
        });
        loadInfo();
    }

    public void onLoading() {
        loadViewLayout.onLoading();
    }

    public void onLoadSuccess() {
        loadViewLayout.onLoadSuccess();
    }

    public void onLoadError() {
        loadViewLayout.onLoadError(null);
    }

    public void loadInfo() {
        onLoading();
    }

    public interface OnNewsLoadInActivityListener{
      void  onLoad();
    }
}
