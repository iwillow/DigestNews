package com.iwillow.android.digestnews;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.iwillow.android.digestnews.api.RequestAddress;
import com.iwillow.android.digestnews.db.Item2ItemRealm;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.http.RxNewsParser;
import com.iwillow.android.digestnews.util.StatusBarCompat;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.util.DateUtil;
import com.iwillow.android.lib.util.IntentUtil;

import java.util.Date;
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


public class NewsListActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "NewsListActivity";
    private Subscription subscription;
    private Realm realm;
    private RealmAsyncTask asyncTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.showSystemUI(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_news_list);
        subscription = checkDataBase();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
    }


    private Subscription checkDataBase() {

        return realm.asObservable()
                .map(new Func1<Realm, Boolean>() {
                    @Override
                    public Boolean call(Realm realm) {
                        String preDate = DateUtil.format(DateUtil.getPreDay(new Date()), "yyyy-MM-dd");
                        // String preDate = DateUtil.format(new Date(), "yyyy-MM-dd");
                        RealmResults<ItemRealm> list = realm.where(ItemRealm.class).contains("published", preDate).findAll();
                        return list != null && list.size() > 0;
                    }
                })
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "  checkDataBase from database onCompleted called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtil.d(TAG, "  checkDataBase from database onError called");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.d(TAG, "  checkDataBase from database onNext called");
                        if (!aBoolean) {
                            if (subscription != null && !subscription.isUnsubscribed()) {
                                subscription.unsubscribe();
                                subscription = null;
                            }

                            if (IntentUtil.isNetworkConnected(NewsListActivity.this)) {
                                subscription = newsListSubscription();
                            } else {
                                Toast.makeText(NewsListActivity.this, "please connect the network", Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .add(R.id.container, new NewsListFragment(), "list")
                                    .commit();
                        }
                    }
                });

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


}
