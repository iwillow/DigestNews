package com.iwillow.android.digestnews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.util.RxBus;
import com.iwillow.android.digestnews.view.CircularRevealView;
import com.iwillow.android.lib.log.Log;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.view.CircleLayout;
import com.iwillow.android.lib.widget.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/17.
 */
public class ExtraFragment extends BaseFragment {
    private String TAG = "ExtraFragment";
    private TextView toggleButton;
    private CircularRevealView circularRevealView;
    private CircleLayout circleLayout;
    private LinearLayout readIndicator;
    private TextView read;
    private LinearLayout know;
    private TextView bigTitle;
    private TextView smallTitle;
    ArrayList<NewsDetailActivity.DetailItem> detailItemArrayList;
    private Realm realm;
    private List<Subscription> subscriptions = new ArrayList<>();

    public ExtraFragment() {
    }

    public RxBus rxBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        rxBus = new RxBus();
        detailItemArrayList = getArguments().getParcelableArrayList("data");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read_extra_news;
    }

    @Override
    protected void initView(View rootView) {
        Typeface typeface = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        toggleButton = $(rootView, R.id.toggleButton);
        circularRevealView = $(rootView, R.id.revalView);
        readIndicator = $(rootView, R.id.readIndicator);
        circleLayout = $(rootView, R.id.circleLayout);
        bigTitle = $(rootView, R.id.bigTitle);
        smallTitle = $(rootView, R.id.smallTitle);
        bigTitle.setTypeface(typeface);
        smallTitle.setTypeface(typeface);
        read = $(rootView, R.id.read);
        know = $(rootView, R.id.know);
        read.setText("");
        if (detailItemArrayList != null && detailItemArrayList.size() > 0) {
            read.setText(circleLayout.getActiveCount() + " of " + detailItemArrayList.size());
            for (int i = 0; i < detailItemArrayList.size(); i++) {
                int activeColor = detailItemArrayList.get(i).color;
                circleLayout.addItem("" + (i + 1), activeColor);
            }
  /*          for (int i = 0; i < detailItemArrayList.size(); i++) {
                if (detailItemArrayList.get(i).checked) {
                    circleLayout.activeItem(i);
                    LogUtil.e("ExtraFragment", "active " + i);
                }
            }*/
            if (circleLayout.getActiveCount() == detailItemArrayList.size()) {

                readIndicator.setVisibility(View.GONE);
                circleLayout.setVisibility(View.GONE);
                toggleButton.setTextColor(android.graphics.Color.WHITE);
                Drawable bottom = readIndicator.getContext().getResources().getDrawable(R.mipmap.extranews_arrow_down_w);
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bottom);
                know.setVisibility(View.VISIBLE);
                circleLayout.setClickable(false);
                circleLayout.setEnabled(false);
                circularRevealView.setBackgroundColor(android.graphics.Color.parseColor("#00AA00"));

            } else {

                readIndicator.setVisibility(View.VISIBLE);
                know.setVisibility(View.INVISIBLE);
                circleLayout.setOnChildViewClickListener(new CircleLayout.OnChildViewClickListener() {
                    @Override
                    public void onChildViewClick(View childView, int index) {
                        circleLayout.activeItem(index);
                    }
                });

                circleLayout.setCircleLayoutAnimationListener(new CircleLayout.CircleLayoutAnimationListener() {
                    @Override
                    public void onAnimationMarqueeStart() {

                    }

                    @Override
                    public void onAnimationMarqueeEnd() {

                    }

                    @Override
                    public void onAnimationShrinkStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationShrinkEnd(Animator animation) {
                        final int cl = android.graphics.Color.parseColor("#00AA00");
                        readIndicator.setVisibility(View.GONE);
                        circleLayout.setVisibility(View.GONE);
                        circleLayout.setClickable(false);
                        circleLayout.setEnabled(false);
                        circularRevealView.reveal(circularRevealView.getMeasuredWidth() / 2 - 10, circularRevealView.getMeasuredHeight() / 2 - 10, cl, 20, 500, new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);

                                toggleButton.setTextColor(android.graphics.Color.WHITE);
                                Drawable bottom = readIndicator.getContext().getResources().getDrawable(R.mipmap.extranews_arrow_down_w);
                                toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bottom);
                                know.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });
            }


        }
    }

    @Override
    public void onResume() {
        LogUtil.e(TAG, "onResume ");
        Toast.makeText(getContext(), "context onResumess", Toast.LENGTH_SHORT).show();
        super.onResume();
        update();
    }

    private void update() {
        if (detailItemArrayList != null && !detailItemArrayList.isEmpty()) {
            for (Subscription sub : subscriptions) {
                if (sub != null) {
                    sub.unsubscribe();
                }
            }
            subscriptions.clear();
            for (int i = 0; i < detailItemArrayList.size(); i++) {
                subscriptions.add(updateIndex(i, detailItemArrayList.get(i).uuid));
            }
        } else {
            LogUtil.e(TAG, "no data ");
        }
    }

    public Subscription updateIndex(final int index, final String uuid) {

        return realm.asObservable()
                .map(new Func1<Realm, Boolean>() {
                    @Override
                    public Boolean call(Realm realm) {
                        try {
                            LogUtil.e(TAG, "begin search ");
                            ItemRealm itemRealm = realm.where(ItemRealm.class)
                                    .equalTo("id", uuid).findAll().first();
                            if (itemRealm == null) {
                                LogUtil.e(TAG, "itemRealm==null ");
                                return false;

                            }
                            LogUtil.e(TAG, "itemRealm!=null ");
                            return itemRealm.isChecked();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
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

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtil.e(TAG, "onNext == " + aBoolean);
                        if (aBoolean) {
                            circleLayout.activeItem(index);
                            LogUtil.e(TAG, "update " + index);
                        }
                    }
                })
                ;
    }

    private void query(final int index, final String uuid) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ItemRealm itemRealm = realm.where(ItemRealm.class)
                        .equalTo("id", uuid).findAll().first();
                Map<String, Object> map = new HashMap<String, Object>();
                if (itemRealm == null) {
                    map.put("checked", false);
                }else{
                    map.put("checked", itemRealm.isChecked());
                }
                map.put("index", index);
                rxBus.post(map);
            }
        });
    }
   public  void getEvent(){

   }
    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        if (!subscriptions.isEmpty()) {
            for (Subscription sub : subscriptions) {
                if (sub != null) {
                    sub.unsubscribe();
                }
            }
            subscriptions.clear();
        }
    }
}
