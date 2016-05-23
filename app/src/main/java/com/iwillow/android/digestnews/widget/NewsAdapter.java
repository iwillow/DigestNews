package com.iwillow.android.digestnews.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iwillow.android.digestnews.EditionDialog;
import com.iwillow.android.digestnews.MoreDigestDialog;
import com.iwillow.android.digestnews.R;
import com.iwillow.android.digestnews.entity.DetailItem;
import com.iwillow.android.digestnews.view.CircularRevealView;
import com.iwillow.android.lib.view.CircleLayout;
import com.iwillow.android.lib.view.DonutProgress;

/**
 * Created by https://www.githhub.com/iwillow on 2016/5/3.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<DetailItem> {

    private int editon;
    private int section;
    private String date;
    private boolean allChecked;

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
    public void bindItemView(RecyclerView.ViewHolder srcHolder1, final int position) {
        ViewHolder holder = (ViewHolder) srcHolder1;
        holder.itemRealm = getItem(position);


        if (holder.itemRealm != null) {

            if (position == 0) {
                holder.imgArea.setVisibility(View.VISIBLE);
                holder.img.setVisibility(View.VISIBLE);
                holder.placeHolder.setVisibility(View.VISIBLE);
                holder.section.setVisibility(View.VISIBLE);
                holder.date.setVisibility(View.VISIBLE);
                holder.sectionArea.setVisibility(View.VISIBLE);
                Glide.with((holder.itemView.getContext())).load(holder.itemRealm.img).crossFade().into(holder.img);
                if (!TextUtils.isEmpty(date) && date.length() > 32) {
                    holder.date.setText(date.substring(27, 31) + "" + date.substring(7, 9));
                    String ed;
                    if (editon == EditionDialog.EDITION_CANADA) {
                        ed = "Canada";

                    } else if (editon == EditionDialog.EDITION_UK) {
                        ed = "UK";

                    } else if (editon == EditionDialog.EDITION_USA) {
                        ed = "US";

                    } else {
                        ed = "Intl.";

                    }
                    String sec = date.substring(31) +
                            (section == MoreDigestDialog.SECTION_MORNING ? " morning" : " evening")
                            + "|" + ed;
                    holder.section.setText(sec);


                }
            } else {
                holder.imgArea.setVisibility(View.GONE);
                holder.img.setVisibility(View.GONE);
                holder.placeHolder.setVisibility(View.GONE);
                holder.section.setVisibility(View.GONE);
                holder.date.setVisibility(View.GONE);
                holder.sectionArea.setVisibility(View.GONE);
            }
            holder.section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Tast", Toast.LENGTH_SHORT).show();
                }
            });
            //index
            holder.donutProgress.setText("" + (position + 1));
            //label
            Typeface typeFaceLabel = Typeface.createFromAsset(holder.view.getContext().getAssets(), "fonts/Roboto-Bold.ttf");
            holder.label.setTypeface(typeFaceLabel);
            holder.label.setText(holder.itemRealm.label);

            //title
            Typeface typeFaceTitle = Typeface.createFromAsset(holder.view.getContext().getAssets(), "fonts/Roboto-Light.ttf");
            holder.title.setTypeface(typeFaceTitle);
            holder.title.setText("" + holder.itemRealm.getTitle());


            //press
            Typeface typeFacePress = Typeface.createFromAsset(holder.view.getContext().getAssets(), "fonts/Roboto-Light.ttf");
            holder.sources.setTypeface(typeFacePress);
            holder.sources.setText(holder.itemRealm.press);
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


            int stateColor = holder.itemRealm.color;
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
                int cl = android.graphics.Color.parseColor("#55" + holder.itemRealm.getHexCode().substring(1));
                stateListDrawable.addState(new int[]{android.R.attr.state_pressed},
                        new ColorDrawable(cl));
                holder.view.setBackground(stateListDrawable);
            }
            if (holder.itemRealm.order != null) {
                if (!holder.itemRealm.order.contains("wiki")) {
                    holder.images.findViewById(R.id.wiki).setVisibility(View.GONE);
                }
                if (!holder.itemRealm.order.contains("location")) {
                    holder.images.findViewById(R.id.map).setVisibility(View.GONE);
                }
                if (!holder.itemRealm.order.contains("video")) {
                    holder.images.findViewById(R.id.video).setVisibility(View.GONE);
                }
                if (!holder.itemRealm.order.contains("slideshow")) {
                    holder.images.findViewById(R.id.images).setVisibility(View.GONE);
                }
                if (!holder.itemRealm.order.contains("statDetail")) {
                    holder.images.findViewById(R.id.stats).setVisibility(View.GONE);
                }
                if (!holder.itemRealm.order.contains("infograph")) {
                    holder.images.findViewById(R.id.diagram).setVisibility(View.GONE);
                }
                if (!holder.itemRealm.order.contains("tweetKeyword")) {
                    holder.images.findViewById(R.id.twitter).setVisibility(View.GONE);
                }
            }


        }

    }

    @Override
    public void bindHeaderView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public void bindFooterView(RecyclerView.ViewHolder footViewHolder, int position) {

        final FooterViewHolder holder = (FooterViewHolder) footViewHolder;
        final int readColor = holder.revealView.getContext().getResources().getColor(R.color.read_color);
        Typeface typeface = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface ty = Typeface.createFromAsset(holder.itemView.getContext().getAssets(), "fonts/Roboto-Light.ttf");
        holder.bigTitle.setTypeface(typeface);
        holder.smallTitle.setTypeface(typeface);
        holder.urd.setTypeface(ty);
        holder.textView.setTypeface(ty);
        final int count = getAllItems().size();

        holder.textView.setText(holder.circleLayout.getActiveCount() + " of " + count);

        int index = 1;
        if (!allChecked) {
            for (int i = 0; i < count; i++) {
                int activeColor = getItem(i).color;
                holder.circleLayout.addItem("" + index, activeColor);
                index++;
            }

            for (int i = 0; i < count; i++) {
                if (getItem(i).isChecked()) {
                    holder.circleLayout.activeItem(i);
                }
            }
        }
        if (holder.circleLayout.getActiveCount() == count) {
            allChecked = true;
            holder.toggleButton.setTextColor(android.graphics.Color.WHITE);
            holder.readIndicator.setVisibility(View.GONE);
            holder.circleLayout.setVisibility(View.GONE);
            holder.toggleButton.setTextColor(android.graphics.Color.WHITE);
            Drawable bottom = holder.readIndicator.getContext().getResources().getDrawable(R.mipmap.extranews_arrow_down_w);
            holder.toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bottom);
            holder.doYouKnow.setVisibility(View.VISIBLE);
            holder.circleLayout.setClickable(false);
            holder.circleLayout.setEnabled(false);
            holder.revealView.setBackgroundColor(readColor);

        } else {

            holder.readIndicator.setVisibility(View.VISIBLE);
            holder.doYouKnow.setVisibility(View.INVISIBLE);
            holder.circleLayout.setOnChildViewClickListener(new CircleLayout.OnChildViewClickListener() {
                @Override
                public void onChildViewClick(View childView, int index) {
                    //holder.circleLayout.activeItem(index);
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(null, index);

                    }
                    holder.textView.setText(holder.circleLayout.getActiveCount() + " of " + count);
                }
            });
            holder.circleLayout.setCircleLayoutAnimationListener(new CircleLayout.CircleLayoutAnimationListener() {
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
                    //final int cl = android.graphics.Color.parseColor("#00AA00");
                    holder.readIndicator.setVisibility(View.GONE);
                    holder.circleLayout.setVisibility(View.GONE);
                    holder.circleLayout.setClickable(false);
                    holder.circleLayout.setEnabled(false);
                    holder.revealView.reveal(holder.revealView.getMeasuredWidth() / 2 - 10, holder.revealView.getMeasuredHeight() / 2 - 10, readColor, 20, 500, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            holder.toggleButton.setTextColor(android.graphics.Color.WHITE);
                            Drawable bottom = holder.readIndicator.getContext().getResources().getDrawable(R.mipmap.extranews_arrow_down_w);
                            holder.toggleButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, bottom);
                            holder.doYouKnow.setVisibility(View.VISIBLE);
                            allChecked = true;
                        }
                    });
                }
            });
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
        final View imgArea;
        final View placeHolder;
        final DonutProgress donutProgress;
        final TextView label;
        final TextView title;
        final ImageView img;
        final TextView sources;
        final LinearLayout images;
        final View sectionArea;
        final TextView date;
        final TextView section;
        public DetailItem itemRealm;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imgArea = itemView.findViewById(R.id.imgArea);
            placeHolder = itemView.findViewById(R.id.placeHolder);
            img = (ImageView) itemView.findViewById(R.id.img);
            donutProgress = (DonutProgress) itemView.findViewById(R.id.index);
            label = (TextView) itemView.findViewById(R.id.label);
            title = (TextView) itemView.findViewById(R.id.title);
            sources = (TextView) itemView.findViewById(R.id.sources);
            images = (LinearLayout) itemView.findViewById(R.id.order);
            sectionArea = itemView.findViewById(R.id.sectionArea);
            date = (TextView) itemView.findViewById(R.id.date);
            section = (TextView) itemView.findViewById(R.id.section);
        }

    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        final CircleLayout circleLayout;
        final CircularRevealView revealView;
        final TextView textView;
        final TextView bigTitle;
        final TextView smallTitle;
        final View readIndicator;
        final View doYouKnow;
        final TextView toggleButton;
        final TextView urd;

        public FooterViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.circleLayout = (CircleLayout) itemView.findViewById(R.id.circleLayout);
            this.revealView = (CircularRevealView) itemView.findViewById(R.id.revalView);
            this.textView = (TextView) itemView.findViewById(R.id.read);
            this.bigTitle = (TextView) itemView.findViewById(R.id.bigTitle);
            this.smallTitle = (TextView) itemView.findViewById(R.id.smallTitle);
            this.readIndicator = itemView.findViewById(R.id.readIndicator);
            this.doYouKnow = itemView.findViewById(R.id.know);
            this.toggleButton = (TextView) itemView.findViewById(R.id.toggleButton);
            this.urd = (TextView) itemView.findViewById(R.id.uread);

        }
    }


    public interface OnItemClickListener {
        void onItemClick(ViewHolder holder, int position);
    }

    public void resetArea(int edition, int section, String date) {
        this.editon = edition;
        this.section = section;
        this.date = date;
        notifyDataSetChanged();
    }

    public boolean isAllChecked() {
        return allChecked;
    }
}
