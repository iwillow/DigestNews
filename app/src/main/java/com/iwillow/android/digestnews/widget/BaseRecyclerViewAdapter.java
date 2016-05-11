package com.iwillow.android.digestnews.widget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by https://www.github.com/iwillow on 2016/5/3.
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_FOOTER = 2;
    private View headerView;
    private View footerView;
    private List<T> data = new ArrayList<>();
    private List<OnItemClickListener> onItemClickListenerList = new ArrayList<>();

    private OnHeaderViewClickListener onHeaderViewClickListener;
    private OnFooterViewClickListener onFooterViewClickListener;


    public void addData(List<T> data) {
        data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public T getItem(int index) {
        return data.get(index);
    }


    public View getFooterView() {
        return footerView;
    }

    public void setFooterView(View footerView) {
        this.footerView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public View getHeaderView() {
        return headerView;
    }

    public void setHeaderView(View headerView) {
        this.headerView = headerView;
        notifyItemInserted(0);
    }

    public void addItemClickListenr(OnItemClickListener onItemClickListener) {
        onItemClickListenerList.add(onItemClickListener);

    }

    public void removeItemClickListenr(OnItemClickListener onItemClickListener) {
        onItemClickListenerList.remove(onItemClickListener);
    }

    public void removeAllItemsClickListenr(OnItemClickListener onItemClickListener) {
        onItemClickListenerList.clear();
    }

    public OnHeaderViewClickListener getOnHeaderViewClickListener() {
        return onHeaderViewClickListener;
    }

    public void setOnHeaderViewClickListener(OnHeaderViewClickListener onHeaderViewClickListener) {
        this.onHeaderViewClickListener = onHeaderViewClickListener;
    }

    public OnFooterViewClickListener getOnFooterViewClickListener() {
        return onFooterViewClickListener;
    }

    public void setOnFooterViewClickListener(OnFooterViewClickListener onFooterViewClickListener) {
        this.onFooterViewClickListener = onFooterViewClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER && headerView != null) {
            return createHeaderViewHolder(parent, viewType);
        }
        if (viewType == TYPE_FOOTER && footerView != null) {
            return createFooterViewHolder(parent, viewType);
        }
        return createItemViewHolder(parent, viewType);
    }


    @Override
    public int getItemViewType(int position) {
        if (headerView != null && position == 0) {
            return TYPE_HEADER;
        }
        if (footerView != null && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int pos = getActualItemPosition(holder);
        if (holder.getItemViewType() == TYPE_ITEM) {
            bindItemView(holder, pos);

        } else if (holder.getItemViewType() == TYPE_HEADER && headerView != null) {
            headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onHeaderViewClickListener != null) {
                        onHeaderViewClickListener.onHeaderViewClick(v);
                    }
                }
            });
        } else if (holder.getItemViewType() == TYPE_FOOTER && footerView != null) {
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFooterViewClickListener != null) {
                        onFooterViewClickListener.onFooterViewClick(v);
                    }
                }
            });
        }
    }

    public int getActualItemPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return headerView == null ? position : position - 1;
    }

    public abstract RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent, final int viewType);

    public abstract RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent, final int viewType);

    public abstract RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent, final int viewType);

    public abstract void bindItemView(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        if (headerView != null && footerView != null && data != null) {
            return 2 + data.size();
        } else if (headerView == null && footerView != null && data != null) {
            return 1 + data.size();
        } else if (headerView != null && footerView == null && data != null) {
            return 1 + data.size();
        } else if (headerView == null && footerView == null && data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder holder, int position);
    }

    public interface OnHeaderViewClickListener {
        void onHeaderViewClick(View view);
    }

    public interface OnFooterViewClickListener {
        void onFooterViewClick(View view);
    }
}
