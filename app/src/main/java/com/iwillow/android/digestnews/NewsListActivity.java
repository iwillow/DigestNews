package com.iwillow.android.digestnews;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.iwillow.android.digestnews.api.RequestAddress;
import com.iwillow.android.digestnews.db.Item2ItemRealm;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.http.RxNewsParser;
import com.iwillow.android.digestnews.util.StatusBarCompat;
import com.iwillow.android.lib.log.LogUtil;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class NewsListActivity extends AppCompatActivity implements ProductGuideDialog.OnFragmentInteractionListener {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "NewsListActivity";
    private Subscription subscription;
    private Realm realm;
    private RealmAsyncTask asyncTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.showSystemUI(this);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        StatusBarUtil.setTransparent(this);
        subscription = newsListSubscription();

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


    private void cancelAsyncTransaction() {
        if (asyncTransaction != null && !asyncTransaction.isCancelled()) {
            asyncTransaction.cancel();
            asyncTransaction = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        cancelAsyncTransaction();
        realm.close();
        super.onDestroy();

    }

    public Subscription newsListSubscription() {
        return RxNewsParser
                .getNewsList(RequestAddress.BASE_URL, RequestAddress.NEWS_LIST_URL)
                .onBackpressureBuffer()
                .map(new Func1<List<Item>, RealmList<ItemRealm>>() {
                    @Override
                    public RealmList<ItemRealm> call(List<Item> items) {
                        RealmList<ItemRealm> list = new RealmList<ItemRealm>();
                        for (Item item : items) {
                            list.add(Item2ItemRealm.convert(item));
                        }
                        return list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RealmList<ItemRealm>>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(NewsListActivity.this, "subscribe onCompleted:", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(NewsListActivity.this, "subscribe onError:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(RealmList<ItemRealm> itemRealms) {
                        Toast.makeText(NewsListActivity.this, "subscribe onNext:" + itemRealms.size(), Toast.LENGTH_SHORT).show();
                        for (ItemRealm itemRealm : itemRealms) {
                            LogUtil.d("item", "publish date:" + itemRealm.getPublished());
                        }
                        cancelAsyncTransaction();
                        final RealmList<ItemRealm> data = itemRealms;
                        asyncTransaction = realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(data);
                                LogUtil.d("executeTransactionAsync", "writing data");
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .add(R.id.container, new NewsListFragment(), "list")
                                        .commit();

                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                                Toast.makeText(NewsListActivity.this, " executeTransactionAsync error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }


    public Subscription searchSubscription() {

        Subscription subscription = realm.where(ItemRealm.class)
                .contains("published", "2016-04-27")
                .findAllSortedAsync("published")
                .asObservable()
                .onBackpressureBuffer()
                .distinct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RealmResults<ItemRealm>>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(NewsListActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(NewsListActivity.this, "onError" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(RealmResults<ItemRealm> itemRealms) {
                        if (itemRealms != null) {
                            StringBuilder sb = new StringBuilder();
                            for (ItemRealm itemRealm : itemRealms) {
                                sb.append("[title:").append(itemRealm.getTitle()).append(",");
                                sb.append("categories:{").append(itemRealm.getCategories()).append("}");
                                sb.append("sources:{").append(itemRealm.getSources()).append("}");
                                sb.append("color:{").append(itemRealm.getColors()).append("}");
                                sb.append("]\n");
                            }
                            LogUtil.d("NewsListActivity", "" + sb.toString());
                            Toast.makeText(NewsListActivity.this, "onNext:" + itemRealms.size(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

        return new CompositeSubscription(subscription);
    }


}
