package com.iwillow.android.digestnews.widget;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import com.iwillow.android.digestnews.NewsListActivity;
import com.iwillow.android.digestnews.db.EntityHelper;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.http.RxNewsParser;
import com.iwillow.android.lib.log.LogUtil;

import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * helper methods.
 */
public class BatchLoadNewsIntentService extends IntentService {
    private static final String TAG = BatchLoadNewsIntentService.class.getSimpleName();
    private volatile int count = 0;
    private Realm realm;

    public static final String ACTION_BATCH_LOAD = "com.iwillow.android.digestnews.action.BATCH_LOAD";

    public static final String UUID = "uuid";

    public BatchLoadNewsIntentService() {
        super("BatchLoadNewsIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        realm = Realm.getDefaultInstance();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_BATCH_LOAD.equals(action)) {
                final String uuid = intent.getStringExtra(UUID);
                if (!TextUtils.isEmpty(uuid)) {
                    loadNewsBy(uuid);
                }
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void loadNewsBy(String uuid) {

        RxNewsParser
                .getNewsContent(uuid)
                .filter(new Func1<List<Item>, Boolean>() {
                    @Override
                    public Boolean call(List<Item> items) {
                        return items != null && items.size() > 0;
                    }
                })
                .map(new Func1<List<Item>, ItemRealm>() {
                    @Override
                    public ItemRealm call(List<Item> items) {
                        return EntityHelper.convert(items.get(0));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ItemRealm>() {
                    @Override
                    public void onCompleted() {

                        LogUtil.d(TAG, "onCompleted called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG, "onError called");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ItemRealm itemRealm) {
                        LogUtil.d(TAG, "onNext called ：" + itemRealm.getId());
                        final ItemRealm item = itemRealm;
                        if (item != null && item.getMultiSummary() != null && item.getMultiSummary().size() > 0) {

                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(item);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    LogUtil.d(TAG, "save news: " + item.getId() + " onSuccess called");
                                    count++;
                                    Intent intent = new Intent(NewsListActivity.MyReceiver.ACTION_TASK_COUNT);
                                    intent.putExtra(NewsListActivity.MyReceiver.TASK_COUNT, count);
                                    sendBroadcast(intent);

                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable e) {
                                    LogUtil.e(TAG, "executeTransactionAsync onError called");
                                    e.printStackTrace();
                                }
                            });
                        }

                    }
                });
    }

    @Override
    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy  called");
        super.onDestroy();
        realm.close();
    }
}
