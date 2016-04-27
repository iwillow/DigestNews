package com.iwillow.android.digestnews;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.iwillow.android.digestnews.api.RequestAddress;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.http.RxNewsParser;
import com.iwillow.android.lib.log.Log;
import com.iwillow.android.lib.log.LogUtil;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NewsListActivity extends AppCompatActivity implements ProductGuideDialog.OnFragmentInteractionListener {
    private Subscriber<List<Item>> subscriber;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "NewsListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_list);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsGalleryDialog dialog = NewsGalleryDialog.newInstance(null, null);
                dialog.show(getSupportFragmentManager(), "product_tour");
            }
        });

        subscriber = new Subscriber<List<Item>>() {
            @Override
            public void onCompleted() {
                LogUtil.d(TAG, "onCompleted()");
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(TAG, "onError:" + e.getMessage());
            }

            @Override
            public void onNext(List<Item> items) {
                if (items != null && items.size() > 0) {
                    LogUtil.d(TAG, "onNext:" + items.get(0).getTitle());
                } else {
                    Log.e(TAG, "DATA IS NULL");
                }
            }
        };
        RxNewsParser.getNewsContent("74eabc4d-5889-35ca-80da-08ec47e55223")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onDestroy() {
        if (subscriber != null) {
            subscriber.unsubscribe();
        }
        super.onDestroy();

    }


}
