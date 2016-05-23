package com.iwillow.android.digestnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.iwillow.android.digestnews.api.RequestAddress;
import com.iwillow.android.digestnews.db.EntityHelper;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.http.RxNewsParser;
import com.iwillow.android.digestnews.util.StatusBarCompat;
import com.iwillow.android.digestnews.widget.BatchLoadNewsIntentService;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.util.DateUtil;
import com.iwillow.android.lib.util.IntentUtil;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class NewsListActivity extends AppCompatActivity  implements DigestLoadDialog.OnNewsLoadInActivityListener{
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "NewsListActivity";
    private Subscription subscription;
    private Subscription subscriptionInstall;
    private Subscription subscriptionSave;
    private Realm realm;
    private RealmAsyncTask asyncTransaction;
    private DigestLoadDialog digestLoadDialog;
    private MyReceiver myReceiver;
    private int taskCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.showSystemUI(this);
        realm = Realm.getDefaultInstance();
        setContentView(R.layout.activity_news_list);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyReceiver.ACTION_TASK_COUNT);
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, intentFilter);
        subscription = checkDataBase();
        subscriptionInstall = checkInstall();
        digestLoadDialog = new DigestLoadDialog();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, digestLoadDialog, "loading")
                .commit();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

    }

    private Subscription checkInstall() {

        return rx.Observable
                .create(new Observable.OnSubscribe<Boolean>() {

                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        try {
                            SharedPreferences settings = getSharedPreferences(EditionDialog.PREFS_NAME, 0);
                            boolean first = settings.getBoolean(EditionDialog.OPENED, false);
                            subscriber.onNext(first);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            ProductGuideDialog productGuideDialog = new ProductGuideDialog();
                            productGuideDialog.show(getSupportFragmentManager(), "productGuideDialog");
                            subscriptionSave = saveInstall();
                        }

                    }
                });

    }


    private Subscription saveInstall() {
        return rx.Observable
                .create(new Observable.OnSubscribe<Boolean>() {

                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        try {
                            SharedPreferences settings = getSharedPreferences(EditionDialog.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean(EditionDialog.OPENED, true);
                            subscriber.onNext(editor.commit());
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

    private Subscription checkDataBase() {

        return realm.asObservable()
                .map(new Func1<Realm, Boolean>() {
                    @Override
                    public Boolean call(Realm realm) {
                        Date date=new Date();
                        String str = DateUtil.format(date, "yyyy-MM-dd");
                        int section=  DateUtil.getTimeSection();
                        if(section==DateUtil.SECTION_EVENING_YESTERDAY||section==DateUtil.SECTION_MORNING_TODAY){
                            str=DateUtil.format(DateUtil.getPreDay(date), "yyyy-MM-dd");
                        }
                        RealmResults<ItemRealm> list = realm.where(ItemRealm.class).contains("published", str).findAll();
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
                        LogUtil.e(TAG, "  checkDataBase from database onError called");
                    }

                    @Override
                    public void onNext(Boolean hasLocalData) {
                        LogUtil.d(TAG, "  checkDataBase from database onNext called");
                        if (!hasLocalData) {
                            if (subscription != null && !subscription.isUnsubscribed()) {
                                subscription.unsubscribe();
                                subscription = null;
                            }
                            if (IntentUtil.isNetworkConnected(NewsListActivity.this)) {
                                subscription = newsListSubscription();
                            } else {
                                digestLoadDialog.onLoadError();
                            }
                        } else {
                            digestLoadDialog.onLoadSuccess();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.container, new NewsListFragment(), "list")
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
        if (subscriptionInstall != null) {
            subscriptionInstall.unsubscribe();
        }
        if (subscriptionSave != null) {
            subscriptionSave.unsubscribe();
        }
        cancelAsyncTransaction();
        realm.close();
        unregisterReceiver(myReceiver);
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
                            list.add(EntityHelper.convert(item));
                        }
                        return list;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RealmList<ItemRealm>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, " load data from internet onCompleted called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, " load data from internet onError called");
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(RealmList<ItemRealm> itemRealms) {
                        LogUtil.d(TAG, " load data from internet onNext called");
                        cancelAsyncTransaction();
                        final RealmList<ItemRealm> data = itemRealms;
                        asyncTransaction = realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(data);
                                LogUtil.d("executeTransactionAsync", "writing data into database");
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                LogUtil.d(TAG, "writing data into database successfully");
                                taskCount = data.size();
                                for (ItemRealm itemRealm : data) {
                                    Intent intent = new Intent(NewsListActivity.this, BatchLoadNewsIntentService.class);
                                    intent.setAction(BatchLoadNewsIntentService.ACTION_BATCH_LOAD);
                                    intent.putExtra(BatchLoadNewsIntentService.UUID, itemRealm.getId());
                                    startService(intent);
                                }


                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                LogUtil.e(TAG, " writing data into database unsuccessfully");
                                error.printStackTrace();

                            }
                        });
                    }
                });
    }

    @Override
    public void onLoad() {
        if (IntentUtil.isNetworkConnected(NewsListActivity.this)) {
            if(subscription!=null){
                subscription.unsubscribe();
            }
            subscription = newsListSubscription();
        } else {
            digestLoadDialog.onLoadError();
        }
    }

    public class MyReceiver extends BroadcastReceiver {
        public static final String ACTION_TASK_COUNT = "com.iwillow.android.digestnews.action.TASK_COUNT";
        public static final String TASK_COUNT = "task_count";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                final String action = intent.getAction();
                if (ACTION_TASK_COUNT.equals(action)) {
                    int count = intent.getIntExtra(TASK_COUNT, 0);
                    if (count == taskCount && count > 0) {
                        LogUtil.d(TAG, " all news loaded");
                        digestLoadDialog.onLoadSuccess();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, new NewsListFragment(), "list")
                                .commit();
                    } else {
                        LogUtil.d(TAG, count + " news loaded");
                    }

                }
            }
        }
    }


}
