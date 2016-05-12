package com.iwillow.android.digestnews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iwillow.android.digestnews.entity.Color;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.view.CircularRevealView;
import com.iwillow.android.digestnews.widget.NewsAdapter;
import com.iwillow.android.lib.util.DateUtil;
import com.iwillow.android.lib.view.CircleLayout;
import com.iwillow.android.lib.widget.BaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by https://www.github.com/iwillow on 2016/4/29.
 */
public class NewsListFragment extends BaseFragment {

    private NewsAdapter adapter;
    private Realm realm;
    private Subscription subscription;
    private RecyclerView recyclerView;
    private ArrayList<NewsDetailActivity.DetailItem> list = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected void initView(final View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        adapter = new NewsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsAdapter.ViewHolder holder, int position) {

                Intent intent = new Intent(rootView.getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.INDEX, position);
                intent.putParcelableArrayListExtra(NewsDetailActivity.DATA, list);
                startActivity(intent);

            }
        });
        subscription = load();
    }

    private Subscription load() {

        return realm.asObservable()
                .onBackpressureBuffer()
                .map(new Func1<Realm, RealmResults<ItemRealm>>() {
                    @Override
                    public RealmResults<ItemRealm> call(Realm realm) {
                        String preDate = DateUtil.format(DateUtil.getPreDay(new Date()), "yyyy-MM-dd");
                        return realm.where(ItemRealm.class).contains("published", preDate).findAllSorted("published");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RealmResults<ItemRealm>>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getContext(), "onCompleted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "onError" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(RealmResults<ItemRealm> itemRealms) {

                        if (adapter != null) {
                            adapter.clear();
                            for (ItemRealm itemRealm : itemRealms) {
                                NewsDetailActivity.DetailItem detailItem = new NewsDetailActivity.DetailItem();
                                int stateColor = android.graphics.Color.parseColor(itemRealm.getColors().get(0).getHexcode());
                                detailItem.color = stateColor;
                                detailItem.uuid = itemRealm.getId();
                                list.add(detailItem);
                                adapter.addItem(itemRealm);
                            }
                            addFooterView(itemRealms);
                        }


                    }
                });


    }

    private void addFooterView(RealmResults<ItemRealm> itemRealms) {
        View footer = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.news_list_footer_view, recyclerView, false);
        final CircleLayout circleLayout = (CircleLayout) footer.findViewById(R.id.circleLayout);
        final CircularRevealView revealView = (CircularRevealView) footer.findViewById(R.id.revalView);
        final TextView textView = (TextView) footer.findViewById(R.id.read);
        final TextView bigTitle = (TextView) footer.findViewById(R.id.bigTitle);
        final TextView smallTitle = (TextView) footer.findViewById(R.id.smallTitle);
        final View readIndicator = footer.findViewById(R.id.readIndicator);
        final View doYouKnow = footer.findViewById(R.id.know);
        final TextView toggleButton = (TextView) footer.findViewById(R.id.toggleButton);
        readIndicator.setVisibility(View.VISIBLE);
        doYouKnow.setVisibility(View.INVISIBLE);
        Typeface typeface = Typeface.createFromAsset(recyclerView.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        bigTitle.setTypeface(typeface);
        smallTitle.setTypeface(typeface);
        final int count = itemRealms.size();

        textView.setText(circleLayout.getActiveCount() + " of " + count);

        int index = 1;

        for (int i = 0; i < count; i++) {
            Color color = itemRealms.get(i).getColors().get(0);
            int activeColor = android.graphics.Color.parseColor(color.getHexcode());
            circleLayout.addItem("" + index, activeColor);
            index++;
        }
        circleLayout.setOnChildViewClickListener(new CircleLayout.OnChildViewClickListener() {
            @Override
            public void onChildViewClick(View childView, int index) {
                circleLayout.activeItem(index);
                textView.setText(circleLayout.getActiveCount() + " of " + count);
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
                revealView.reveal(revealView.getMeasuredWidth() / 2 - 10, revealView.getMeasuredHeight() / 2 - 10, cl, 20, 500, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        toggleButton.setTextColor(android.graphics.Color.WHITE);
                        Drawable bottom = getResources().getDrawable(R.mipmap.extranews_arrow_down_w);
                        toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bottom);
                        doYouKnow.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExtraNewsListActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.move_in, R.anim.move_out);
            }
        });
        adapter.setFooterView(footer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

}
