package com.iwillow.android.digestnews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iwillow.android.digestnews.db.Item2ItemRealm;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.http.RxNewsParser;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.widget.BaseFragment;

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


/**
 * Created by https://www.github.com/iwillow on 2016/5/3.
 * <p>
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailFragment extends BaseFragment {
    private static final String TAG = NewsDetailFragment.class.getSimpleName();

    private static final String UUID = "uuid";
    private static final String COLOR = "color";
    private Subscription subscription;
    private String uuid;
    private int color;
    private Realm realm;
    private RealmAsyncTask asyncTransaction;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uuid  Parameter 1.
     * @param color Parameter 2.
     * @return A new instance of fragment NewsDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsDetailFragment newInstance(String uuid, int color) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(UUID, uuid);
        args.putInt(COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    public Subscription loadFromDataBase() {
        return realm.where(ItemRealm.class)
                .equalTo("id", uuid)
                .notEqualTo("multiSummary", "")
                .findAllSortedAsync("published")
                .asObservable()
                .onBackpressureBuffer()
                .distinct()
                .filter(new Func1<RealmResults<ItemRealm>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<ItemRealm> itemRealms) {
                        return itemRealms != null && itemRealms.size() > 0;
                    }
                })
                .map(new Func1<RealmResults<ItemRealm>, ItemRealm>() {
                    @Override
                    public ItemRealm call(RealmResults<ItemRealm> itemRealms) {
                        return itemRealms.get(0);
                    }
                })
                .filter(new Func1<ItemRealm, Boolean>() {
                    @Override
                    public Boolean call(ItemRealm itemRealm) {
                        return itemRealm != null
                                && itemRealm.getMultiSummary() != null
                                && itemRealm.getMultiSummary().size() > 0;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ItemRealm>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "load news' UUID:" + uuid + " from database completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(ItemRealm itemRealm) {

                    }
                });
    }

    public Subscription loadFromNetwork() {

        return RxNewsParser
                .getNewsContent(uuid)
                .onBackpressureBuffer()
                .filter(new Func1<List<Item>, Boolean>() {
                    @Override
                    public Boolean call(List<Item> items) {
                        return items != null && items.size() > 0;
                    }
                })
                .map(new Func1<List<Item>, ItemRealm>() {
                    @Override
                    public ItemRealm call(List<Item> items) {
                        return Item2ItemRealm.convert(items.get(0));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ItemRealm>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "load news' UUID:" + uuid + " from internet completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ItemRealm itemRealm) {
                        final ItemRealm item = itemRealm;
                        asyncTransaction = realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                item.setChecked(true);
                                realm.copyToRealmOrUpdate(item);
                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {

                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                error.printStackTrace();
                            }
                        });
                    }
                });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString(UUID);
            color = getArguments().getInt(COLOR);
        }
        realm = Realm.getDefaultInstance();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_detail;
    }

    @Override
    protected void initView(View rootView) {


    }

    private void initItem() {

    }

    private void cancelAsyncTransaction() {
        if (asyncTransaction != null && !asyncTransaction.isCancelled()) {
            asyncTransaction.cancel();
            asyncTransaction = null;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        cancelAsyncTransaction();
        if (subscription != null
                && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
