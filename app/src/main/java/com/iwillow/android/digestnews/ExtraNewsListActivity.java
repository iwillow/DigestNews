package com.iwillow.android.digestnews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.iwillow.android.digestnews.util.StatusBarCompat;

/**
 * Created by https://www.github.com/iwillow on 2016/5/3.
 */
public class ExtraNewsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.showSystemUI(this);
        setContentView(R.layout.extra_news_activity);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.move_in1, R.anim.move_out1);
    }
}
