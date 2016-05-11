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
import com.iwillow.android.lib.view.CircleLayout;
import com.iwillow.android.lib.widget.BaseFragment;

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
    protected void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        adapter = new NewsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsAdapter.ViewHolder holder, int position) {

                Intent intent = new Intent(recyclerView.getContext(), NewsDetailActivity.class);
                Color color = holder.itemRealm.getColors().get(0);
                int stateColor = android.graphics.Color.parseColor(color.getHexcode());
                intent.putExtra("color", stateColor);
                intent.putExtra("index", position + 1);
                intent.putExtra("uuid", holder.itemRealm.getId());
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
                        return realm.where(ItemRealm.class).contains("published","2016-05-10").findAllSorted("published");
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
                            adapter.clear();
                            for (ItemRealm itemRealm : itemRealms) {
                                adapter.addItem(itemRealm);
                            }
                            addFooterView(itemRealms);
                        }
                        // Toast.makeText(getContext(), "onNext" + size, Toast.LENGTH_SHORT).show();

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

/*    public static class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {
        private List<ItemRealm> list;
        private OnItemClickListener onItemClickListener;

        public NewsRecyclerViewAdapter(List<ItemRealm> list) {
            this.list = list;
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }


        public interface OnItemClickListener {
            void onItemClick(ViewHolder viewHolder, int position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_item, parent, false);
            return new ViewHolder(view);
        }

        public void addItems(List<ItemRealm> items) {
            if (items != null) {
                if (list != null) {
                    list.addAll(items);
                } else {
                    list = items;
                }
                notifyDataSetChanged();
            }
        }

        public void addItem(ItemRealm item) {
            if (item != null) {

                if (list != null) {
                    list.add(item);
                } else {
                    list = new ArrayList<ItemRealm>();
                    list.add(item);
                }
                notifyDataSetChanged();
            }
        }


        public void activeItem(int position) {
            if (list != null && position >= 0 && position < list.size()) {
                list.get(position).setChecked(true);
                notifyDataSetChanged();
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (list != null) {
                holder.itemRealm = list.get(position);

                if (holder.itemRealm != null) {

                    //index
                    holder.donutProgress.setText("" + position);

                    //label
                    Typeface typeFaceLabel = Typeface.createFromAsset(holder.view.getContext().getAssets(), "fonts/Roboto-Bold.ttf");
                    holder.label.setTypeface(typeFaceLabel);
                    if (holder.itemRealm.getCategories() != null && holder.itemRealm.getCategories().size() > 0) {
                        holder.label.setText("" + holder.itemRealm.getCategories().get(0).getLabel());
                    } else {
                        holder.label.setText("World");
                    }


                    //title
                    Typeface typeFaceTitle = Typeface.createFromAsset(holder.view.getContext().getAssets(), "fonts/Roboto-Light.ttf");
                    holder.title.setTypeface(typeFaceTitle);
                    holder.title.setText("" + holder.itemRealm.getTitle());


                    //press
                    Typeface typeFacePress = Typeface.createFromAsset(holder.view.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
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

                    final ViewHolder holder1 = holder;
                    final int position1 = position;
                    holder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(holder1, position1);
                            }
                        }
                    });


                    if (holder.itemRealm.getColors() != null && holder.itemRealm.getColors().size() > 0) {

                        Color color = holder.itemRealm.getColors().get(0);
                        int stateColor = android.graphics.Color.parseColor(color.getHexcode());

                        //index


                        if (holder.itemRealm.isChecked()) {
                            holder.donutProgress.setFinishedStrokeColor(stateColor);
                            holder.donutProgress.setUnfinishedStrokeColor(stateColor);
                            holder.donutProgress.setInnerBackgroundColor(stateColor);
                            holder.donutProgress.setTextColor(android.graphics.Color.WHITE);

                        } else {
                            holder.donutProgress.setFinishedStrokeColor(stateColor);
                            holder.donutProgress.setUnfinishedStrokeColor(stateColor);
                            holder.donutProgress.setTextColor(stateColor);
                            holder.donutProgress.setInnerBackgroundColor(android.graphics.Color.TRANSPARENT);
                        }

                        //label
                        holder.label.setTextColor(stateColor);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                            StateListDrawable stateListDrawable = new StateListDrawable();
                            stateListDrawable.addState(new int[]{android.R.attr.state_empty},
                                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            int cl = android.graphics.Color.parseColor("#55" + color.getHexcode().substring(1));
                            stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                                    new ColorDrawable(cl));
                            holder.view.setBackground(stateListDrawable);

                        }

                    }


                }

            }

        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final View view;
            final DonutProgress donutProgress;
            final TextView label;
            final TextView title;
            final TextView sources;
            final LinearLayout images;
            public ItemRealm itemRealm;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                donutProgress = (DonutProgress) itemView.findViewById(R.id.index);
                label = (TextView) itemView.findViewById(R.id.label);
                title = (TextView) itemView.findViewById(R.id.title);
                sources = (TextView) itemView.findViewById(R.id.sources);
                images = (LinearLayout) itemView.findViewById(R.id.images);
            }
        }


    }*/


}
