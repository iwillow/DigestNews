package com.iwillow.android.digestnews;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.iwillow.android.digestnews.util.StatusBarCompat;

public class NewsDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.showSystemUI(this);
        setContentView(R.layout.activity_news_detail);
        //StatusBarUtil.setTransparent(this);
        //StatusBarCompat.compat(this, Color.TRANSPARENT);
        String uuid = getIntent().getStringExtra("uuid");
        int color = getIntent().getIntExtra("color", 0);
        int index = getIntent().getIntExtra("index", -1);

        Fragment fragment = NewsDetailFragment.newInstance(uuid, color, index);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment, "NewsList")
                .commit();


        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }

}
