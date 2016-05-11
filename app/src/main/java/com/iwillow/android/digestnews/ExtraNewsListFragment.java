package com.iwillow.android.digestnews;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iwillow.android.digestnews.entity.Color;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.entity.Source;
import com.iwillow.android.digestnews.widget.BaseRecyclerViewAdapter;
import com.iwillow.android.lib.widget.BaseFragment;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by https://www.github.com/iwillow on 2016/4/29.
 */
public class ExtraNewsListFragment extends BaseFragment {
    private Realm realm;
    private Subscription subscription;
    private RecyclerView recyclerView;
    private ExtraNewsAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        subscription = load();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_extra_news_list;
    }

    @Override
    protected void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        TextView back = (TextView) rootView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        View headerView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.extra_news_header_view, recyclerView, false);
        adapter = new ExtraNewsAdapter();
        adapter.setHeaderView(headerView);
        recyclerView.setAdapter(adapter);

    }

    private Subscription load() {

        return realm.asObservable()
                .onBackpressureBuffer()
                .map(new Func1<Realm, RealmResults<ItemRealm>>() {
                    @Override
                    public RealmResults<ItemRealm> call(Realm realm) {
                        return realm.where(ItemRealm.class).contains("published", "2016-05-08").findAllSorted("published");
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
                        Toast.makeText(getContext(), "onError" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(RealmResults<ItemRealm> itemRealms) {
                        int size = itemRealms == null ? -1 : itemRealms.size();
                        if (adapter != null) {
                            for (ItemRealm itemRealm : itemRealms) {
                                adapter.addItem(itemRealm);
                            }
                            addFooterView();
                        }
                        Toast.makeText(getContext(), "onNext" + size, Toast.LENGTH_SHORT).show();

                    }
                });


    }

    private void addFooterView() {
        View footerView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.extra_news_footer_view, recyclerView, false);
        adapter.setFooterView(footerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }


    public class ExtraNewsAdapter extends BaseRecyclerViewAdapter<ItemRealm> {

        public ExtraNewsAdapter() {
        }

        @Override
        public RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent, int viewType) {
            return new HeaderViewHolder(getHeaderView());
        }

        @Override
        public RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent, int viewType) {
            return new FooterViewHolder(getFooterView());
        }

        @Override
        public RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.extra_news_item, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void bindItemView(RecyclerView.ViewHolder srcHolder, int position) {
            ItemViewHolder holder = (ItemViewHolder) srcHolder;
            holder.itemRealm = getItem(position);
            if (holder.itemRealm != null) {

                Typeface typeFaceLabel = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/Roboto-Bold.ttf");
                holder.label.setTypeface(typeFaceLabel);
                if (holder.itemRealm.getCategories() != null && holder.itemRealm.getCategories().size() > 0) {
                    holder.label.setText("" + holder.itemRealm.getCategories().get(0).getLabel());
                } else {
                    holder.label.setText("World");
                }

                //title
                Typeface typeFaceTitle = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/Roboto-Light.ttf");
                holder.title.setTypeface(typeFaceTitle);
                holder.title.setText("" + holder.itemRealm.getTitle());


                //press
                Typeface typeFacePress = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
                holder.sources.setTypeface(typeFacePress);
                RealmList<Source> presses = holder.itemRealm.getSources();

                if (presses != null && presses.size() > 0) {

                    StringBuilder sb = new StringBuilder();
                    if (presses.size() == 1) {
                        sb.append(presses.get(0).getPublisher());
                    } else if (presses.size() == 2) {
                        sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
                    } else if (presses.size() == 3) {
                        sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
                        sb.append(" + 1 more");
                    } else if (presses.size() == 4) {
                        sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
                        sb.append(" + 2 more");
                    } else {
                        sb.append(presses.get(0).getPublisher()).append(",").append(presses.get(1).getPublisher());
                        sb.append(" + 3 more");
                    }
                    holder.sources.setText("" + sb.toString());
                } else {
                    holder.sources.setText("Yahoo News");
                }

                if (holder.itemRealm.getColors() != null && holder.itemRealm.getColors().size() > 0) {

                    Color color = holder.itemRealm.getColors().get(0);
                    int stateColor = android.graphics.Color.parseColor(color.getHexcode());
                    holder.label.setTextColor(stateColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                        StateListDrawable stateListDrawable = new StateListDrawable();
                        stateListDrawable.addState(new int[]{android.R.attr.state_empty},
                                new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        int cl = android.graphics.Color.parseColor("#55" + color.getHexcode().substring(1));
                        stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                                new ColorDrawable(cl));
                        holder.itemView.setBackground(stateListDrawable);

                    }
                }
                final int index = position;
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "index:" + index, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        public final TextView label;
        public final TextView title;
        public final TextView sources;
        public ItemRealm itemRealm;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            label = (TextView) itemView.findViewById(R.id.label);
            title = (TextView) itemView.findViewById(R.id.title);
            sources = (TextView) itemView.findViewById(R.id.sources);
        }

    }

}
