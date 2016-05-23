package com.iwillow.android.digestnews;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iwillow.android.digestnews.widget.BaseRecyclerViewAdapter;
import com.iwillow.android.lib.util.IntentUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by https://www.github.com.com/iwillow on 2016/5/11.
 */
public class ShareDialog extends DialogFragment {
    public static final String LINK = "link";
    public static final String TITLE = "title";
    public static final String SOURCE = "source";
    private String link;
    private String title;
    private String source;
    private TextView tvTitle;
    private TextView tvPress;
    private RecyclerView recyclerView;
    private AppInfoAdapter appInfoAdapter;
    private String shareContent;

    public ShareDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(TITLE);
        link = getArguments().getString(LINK);
        source = getArguments().getString(SOURCE);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.share_dialog, container);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        Typeface typefaceLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        tvTitle = (TextView) rootView.findViewById(R.id.title);
        tvTitle.setTypeface(typefaceLight);
        tvPress = (TextView) rootView.findViewById(R.id.press);
        tvPress.setTypeface(typefaceLight);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 3));
        //recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        appInfoAdapter = new AppInfoAdapter();
        View headerView = LayoutInflater.from(rootView.getContext()).inflate(R.layout.place_holder, recyclerView, false);
        appInfoAdapter.setHeaderView(headerView);
        recyclerView.setAdapter(appInfoAdapter);
        tvTitle.setText("" + title);
        tvPress.setText("" + source);
        shareContent = title + " via Digest News\t\t\n" + link + "\t\n get the app and day's need to know news. https://github.com/iwillow/DigestNews ";
        loadList(shareContent);
    }

    private List<ResolveInfo> queryApps(String msg) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
        sendIntent.setType("text/plain");
        return IntentUtil.queryIntents(getContext(), sendIntent);
    }

    private Observable<ResolveInfo> getApps(final String msg) {

        return Observable.create(new Observable.OnSubscribe<ResolveInfo>() {
            @Override
            public void call(Subscriber<? super ResolveInfo> subscriber) {
                List<ResolveInfo> list = queryApps(msg);
                if (list != null && !list.isEmpty()) {
                    for (ResolveInfo resolveInfo : list) {
                        subscriber.onNext(resolveInfo);
                    }
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception("no applications to share this  news"));
                }
            }
        });
    }

    private void loadList(String msg) {

        getApps(msg)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResolveInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        addFooterView(e);
                    }

                    @Override
                    public void onNext(ResolveInfo resolveInfo) {
                        appInfoAdapter.addItem(resolveInfo);
                    }
                });

    }

    private void addFooterView(Throwable e) {
        View footerView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.place_holder, recyclerView, false);
        TextView textView = (TextView) footerView.findViewById(R.id.errorHint);
        textView.setText("" + e.getMessage());
        textView.setVisibility(View.VISIBLE);
        appInfoAdapter.setFooterView(footerView);
    }

    public class AppInfoAdapter extends BaseRecyclerViewAdapter<ResolveInfo> {

        @Override
        public RecyclerView.ViewHolder createHeaderViewHolder(ViewGroup parent, int viewType) {
            return new HeaderFooterViewHolder(getHeaderView());
        }

        @Override
        public RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent, int viewType) {
            return new HeaderFooterViewHolder(getHeaderView());
        }

        @Override
        public RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.share_item, parent, false);
            return new ResolveInfoHolder(view);
        }

        @Override
        public void bindItemView(RecyclerView.ViewHolder src, int position) {
            final ResolveInfoHolder holder = (ResolveInfoHolder) src;
            holder.resolveInfo = getItem(position);
            // holder.textView.setText("" + holder.resolveInfo.);
            // holder.resolveInfo.

            //  holder.resolveInfo.activityInfo.loadIcon(holder.icon.getContext().getPackageManager());
            loadIcon(holder.resolveInfo, holder.icon);
            loadLabel(holder.resolveInfo, holder.textView);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        ActivityInfo activityInfo = holder.resolveInfo.activityInfo;
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, shareContent);
                        intent.setType("text/plain");
                        intent.setClassName(activityInfo.applicationInfo.packageName,
                                activityInfo.name);
                        startActivity(intent);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            });
            // holder.textView.setText(holder.resolveInfo.loadLabel(holder.textView.getContext().getPackageManager()));
        }

        @Override
        public void bindHeaderView(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public void bindFooterView(RecyclerView.ViewHolder holder, int position) {

        }

        public class ResolveInfoHolder extends RecyclerView.ViewHolder {
            final View itemView;
            final TextView textView;
            final ImageView icon;
            public ResolveInfo resolveInfo;

            public ResolveInfoHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                textView = (TextView) itemView.findViewById(R.id.appName);
                icon = (ImageView) itemView.findViewById(R.id.icon);
            }
        }

        public class HeaderFooterViewHolder extends RecyclerView.ViewHolder {
            final View itemView;

            public HeaderFooterViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;

            }
        }

    }

    private void loadLabel(final ResolveInfo resolveInfo, final TextView textView) {
        if (resolveInfo == null || textView == null) {
            return;
        }

        Observable
                .create(new Observable.OnSubscribe<CharSequence>() {

                    @Override
                    public void call(Subscriber<? super CharSequence> subscriber) {
                        CharSequence name = resolveInfo.loadLabel(textView.getContext().getPackageManager());
                        if (!TextUtils.isEmpty(name)) {
                            subscriber.onNext(name);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("app name is null"));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.setText("" + e.getMessage());
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        textView.setText(charSequence);
                    }
                });
    }

    private void loadIcon(final ResolveInfo resolveInfo, final ImageView imageView) {

        if (resolveInfo == null || imageView == null) {
            return;
        }

        Observable
                .create(new Observable.OnSubscribe<Drawable>() {

                    @Override
                    public void call(Subscriber<? super Drawable> subscriber) {
                        Drawable drawable = resolveInfo.loadIcon(imageView.getContext().getPackageManager());
                        if (drawable != null) {
                            subscriber.onNext(drawable);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("app icon is null"));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        imageView.setImageResource(R.mipmap.ic_launcher);
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

}
