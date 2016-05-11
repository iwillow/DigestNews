package com.iwillow.android.digestnews.widget;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iwillow.android.digestnews.R;
import com.iwillow.android.digestnews.entity.Color;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.entity.Source;
import com.iwillow.android.digestnews.entity.Summary;
import com.iwillow.android.digestnews.util.ItemRealUtil;
import com.iwillow.android.lib.view.DonutProgress;

import io.realm.RealmList;

/**
 * Created by https://www.githhub.com/iwillow on 2016/5/3.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<ItemRealm> {
    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NewsAdapter() {

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
                .inflate(R.layout.news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindItemView(RecyclerView.ViewHolder srcHolder1, int position) {
        ViewHolder holder = (ViewHolder) srcHolder1;
        holder.itemRealm = getItem(position);

        if (holder.itemRealm != null) {

            //index
            holder.donutProgress.setText("" + (position + 1));

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
            Typeface typeFacePress = Typeface.createFromAsset(holder.view.getContext().getAssets(), "fonts/Roboto-Light.ttf");
            holder.sources.setTypeface(typeFacePress);
 /*           RealmList<Source> presses = holder.itemRealm.getSources();
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
            }*/
            holder.sources.setText(ItemRealUtil.getPress(holder.itemRealm));
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

    public void activationItem(int index) {
        getItem(index).setChecked(true);
        notifyDataSetChanged();
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
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

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, int position);
    }

}
