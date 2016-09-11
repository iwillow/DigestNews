package com.iwillow.android.digestnews;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer.util.Util;
import com.iwillow.android.digestnews.db.EntityHelper;
import com.iwillow.android.digestnews.entity.Category;
import com.iwillow.android.digestnews.entity.Image;
import com.iwillow.android.digestnews.entity.ImageAsset;
import com.iwillow.android.digestnews.entity.Infograph;
import com.iwillow.android.digestnews.entity.Item;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.entity.Location;
import com.iwillow.android.digestnews.entity.LongRead;
import com.iwillow.android.digestnews.entity.Photo;
import com.iwillow.android.digestnews.entity.Quote;
import com.iwillow.android.digestnews.entity.SlideItem;
import com.iwillow.android.digestnews.entity.Slideshow;
import com.iwillow.android.digestnews.entity.Source;
import com.iwillow.android.digestnews.entity.StatDetail;
import com.iwillow.android.digestnews.entity.Stream;
import com.iwillow.android.digestnews.entity.StringRealmWrapper;
import com.iwillow.android.digestnews.entity.Summary;
import com.iwillow.android.digestnews.entity.TweetItemRealm;
import com.iwillow.android.digestnews.entity.TweetRealm;
import com.iwillow.android.digestnews.entity.Video;
import com.iwillow.android.digestnews.entity.WikiRealm;
import com.iwillow.android.digestnews.http.RxNewsParser;
import com.iwillow.android.digestnews.util.ItemRealUtil;
import com.iwillow.android.digestnews.util.ReferenceUtil;
import com.iwillow.android.digestnews.util.TweetTransformer;
import com.iwillow.android.digestnews.util.URLSpanNoUnderline;
import com.iwillow.android.digestnews.widget.BaseRecyclerViewAdapter;
import com.iwillow.android.digestnews.widget.GalleryAdapter;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.util.DimensionUtil;
import com.iwillow.android.lib.util.IntentUtil;
import com.iwillow.android.lib.view.DonutProgress;
import com.iwillow.android.lib.widget.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmList;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by https://www.github.com/iwillow on 2016/5/3.
 * <p/>
 * A simple {@link BaseFragment} subclass.
 * Use the {@link NewsDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailFragment extends BaseFragment {
    private static final String TAG = NewsDetailFragment.class.getSimpleName();

    private static final String UUID = "uuid";
    private static final String COLOR = "color";
    private static final String INDEX = "index";
    private Subscription subscription;
    private Subscription event;
    private String uuid;
    private int color;
    private int index;
    private Realm realm;
    private RealmAsyncTask asyncTransaction;


    private ImageButton back;
    private ImageButton share;
    private ImageButton menu;
    private ImageView banner;
    private DonutProgress donutProgress;
    private TextView summaryEdition;
    private View line;
    private TextView lable;
    private TextView title;
    private ViewGroup summary;
    private ViewGroup statDetail;
    private ViewGroup infographs;
    private ViewGroup longreads;
    private ViewGroup locations;
    private ViewGroup slideshow;
    private ViewGroup videos;
    private ViewGroup wikis;
    private ViewGroup tweets;
    private ViewGroup anchorArea;
    private ViewGroup references;
    private RecyclerView gallery;
    private ImageView singleImage;
    private TextView referCount;
    private ImageView toggleImage;
    private ImageView anchor;
    private NestedScrollView nestedScrollView;
    private View functionBar;
    private Typeface typefaceBold;
    private Typeface typefaceLight;
    private Typeface typefaceThin;
    private GalleryAdapter galleryAdapter;
    private TextView error;
    //private Context mContext;
    private Subscription updateIndexSubscription;
    private int distance;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param uuid  news' uuid
     * @param color news' color.
     * @param index whether we jump to this page from extra news list. if its value is -1,it indicates we jump from  {@link ExtraNewsListActivity} page.
     * @return A new instance of fragment NewsDetailFragment.
     */

    public static NewsDetailFragment newInstance(String uuid, int color, int index) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(UUID, uuid);
        args.putInt(COLOR, color);
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        LogUtil.d(TAG, "uuid:" + uuid);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        distance = (int) DimensionUtil.dp2px(getResources(), 350);
        if (getArguments() != null) {
            uuid = getArguments().getString(UUID);
            color = getArguments().getInt(COLOR);
            index = getArguments().getInt(INDEX);
        }
        realm = Realm.getDefaultInstance();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_news;
        //  return R.layout.fragment_news_detail_page;
    }

    @Override
    protected void initView(View rootView) {
        // mContext=rootView.getContext();
        back = $(rootView, R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        share = $(rootView, R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog();
                Bundle bundle = new Bundle();
                bundle.putString(ShareDialog.TITLE, title.getText().toString());
                bundle.putString(ShareDialog.LINK, title.getTag().toString());
                bundle.putString(ShareDialog.SOURCE, lable.getTag().toString());
                shareDialog.setArguments(bundle);
                shareDialog.show(getChildFragmentManager(), "share");
            }
        });
        menu = $(rootView, R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.news_detail_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.send_feedback:
                                sendEmail();
                                return true;
                            case R.id.settings:
                                SettingsDialog settingsDialog = new SettingsDialog();
                                settingsDialog.show(getChildFragmentManager(), "setting");
                                return true;
                            default:
                                return false;
                        }
                    }
                });

                popup.show();
            }
        });

        nestedScrollView = $(rootView, R.id.nestedScrollView);
        functionBar = $(rootView, R.id.functionBar);
        banner = $(rootView, R.id.banner);
        donutProgress = $(rootView, R.id.index);
        if (index == -1) {
            donutProgress.setVisibility(View.GONE);
        } else {
            donutProgress.setText("" + (index + 1));
            donutProgress.setTextColor(color);
            donutProgress.setInnerBackgroundColor(Color.TRANSPARENT);
            donutProgress.setFinishedStrokeColor(color);
            donutProgress.setUnfinishedStrokeColor(color);
        }
        lable = $(rootView, R.id.label);
        lable.setTextColor(color);
        typefaceBold = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        typefaceLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Light.ttf");
        typefaceThin = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Thin.ttf");
        lable.setTypeface(typefaceBold);
        title = $(rootView, R.id.title);
        title.setTypeface(typefaceLight);
        summary = $(rootView, R.id.summary);
        summary.removeAllViews();
        statDetail = $(rootView, R.id.statDetail);
        statDetail.removeAllViews();
        infographs = $(rootView, R.id.infographs);
        infographs.removeAllViews();
        longreads = $(rootView, R.id.longreads);
        longreads.removeAllViews();
        locations = $(rootView, R.id.locations);
        locations.removeAllViews();
        videos = $(rootView, R.id.videos);
        videos.removeAllViews();
        wikis = $(rootView, R.id.wikis);
        wikis.removeAllViews();
        tweets = $(rootView, R.id.tweets);
        tweets.removeAllViews();
        references = $(rootView, R.id.sources);
        references.removeAllViews();
        anchorArea = $(rootView, R.id.anchorArea);
        anchor = $(rootView, R.id.anchor);
        toggleImage = $(rootView, R.id.toggleImage);
        referCount = $(rootView, R.id.referCount);
        summaryEdition = $(rootView, R.id.summaryEdition);
        line = $(rootView, R.id.line);
        // referCount.setTypeface(typefaceThin);
        singleImage = $(rootView, R.id.singleImage);
        error = $(rootView, R.id.error);
        gallery = $(rootView, R.id.gallery);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        gallery.setLayoutManager(linearLayoutManager);
        gallery.setItemViewCacheSize(2);
        galleryAdapter = new GalleryAdapter();
        gallery.setAdapter(galleryAdapter);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float fraction = 1.0f * scrollY / distance;

                if (0.75f * fraction <= 1.0f) {
                    banner.setTranslationY(0.75f * fraction * scrollY);
                } else {
                    banner.setTranslationY(distance);
                }
                if (fraction < 0.8f) {
                    if (functionBar.getVisibility() == View.GONE) {
                        functionBar.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (functionBar.getVisibility() == View.VISIBLE) {
                        functionBar.setVisibility(View.GONE);
                    }
                }

            }
        });
        subscription = loadFromDataBase();
    }

    private void sendEmail() {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        String[] addresses = {"iwillow@163.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        String subject = "Please replace this text with your issues";
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Your Phone does not install email application.", Toast.LENGTH_SHORT).show();
        }

    }

    public Subscription loadFromDataBase() {

        return rx.Observable
                .create(new rx.Observable.OnSubscribe<ItemRealm>() {

                    @Override
                    public void call(Subscriber<? super ItemRealm> subscriber) {
                        ItemRealm itemRealm = realm.where(ItemRealm.class)
                                .equalTo("id", uuid).findAll().first();
                        if (itemRealm != null && itemRealm.getMultiSummary() != null && itemRealm.getMultiSummary().size() > 0) {
                            subscriber.onNext(itemRealm);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("news content is null"));
                        }
                    }
                })
                .onBackpressureBuffer()
                .asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ItemRealm>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "  load news' UUID:" + uuid + " from database onCompleted called");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG, "  load news' UUID:" + uuid + " from database onError called");
                        e.printStackTrace();
                        loadNetworkData();
                    }

                    @Override
                    public void onNext(ItemRealm itemRealm) {
                        LogUtil.d(TAG, "  load news' UUID:" + uuid + " from database onNext called");
                        initItem(itemRealm);
                    }
                });

    }

    private void loadNetworkData() {
        if (IntentUtil.isNetworkConnected(getActivity())) {
            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }
            subscription = loadFromNetwork();
        } else {
            Toast.makeText(getActivity(), "please connect the network", Toast.LENGTH_SHORT).show();
        }


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
                        return EntityHelper.convert(items.get(0));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ItemRealm>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "load news' UUID:" + uuid + " from internet completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d(TAG, "load news' UUID:" + uuid + " from internet onError");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ItemRealm itemRealm) {
                        final ItemRealm item = itemRealm;
                        if (item != null && item.getMultiSummary() != null && item.getMultiSummary().size() > 0) {
                            // item.setChecked(true);
                            cancelAsyncTransaction();
                            asyncTransaction = realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    realm.copyToRealmOrUpdate(item);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    initItem(item);
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    error.printStackTrace();
                                }
                            });

                        } else {
                            LogUtil.d(TAG, "data is null");
                        }

                    }
                });
    }


    public void activeItem() {

        rx.Observable
                .create(new rx.Observable.OnSubscribe<Boolean>() {

                    @Override
                    public void call(final Subscriber<? super Boolean> subscriber) {

                        cancelAsyncTransaction();

                        asyncTransaction = realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                ItemRealm itemRealm = realm.where(ItemRealm.class)
                                        .equalTo("id", uuid).findAll().first();
                                if (itemRealm != null) {
                                    itemRealm.setChecked(true);
                                    realm.copyToRealmOrUpdate(itemRealm);
                                    subscriber.onNext(true);
                                } else {
                                    subscriber.onNext(false);
                                }

                            }
                        }, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                subscriber.onCompleted();
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                                subscriber.onError(error);
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            if (index != -1) {

                                donutProgress.setInnerBackgroundColor(color);
                                donutProgress.setTextColor(Color.WHITE);
                                donutProgress.setVisibility(View.VISIBLE);
                                if (getActivity() instanceof NewsDetailActivity) {
                                    ((NewsDetailActivity) getActivity()).updateIndex(index);
                                }
                            } else {
                                donutProgress.setVisibility(View.GONE);
                            }
                        } else {
                            LogUtil.e(TAG, "active  the data failed");
                        }
                    }
                });


    }


    private void initItem(ItemRealm itemRealm) {

        if (itemRealm != null && itemRealm != null && itemRealm.getMultiSummary() != null) {

            if (index != -1 && itemRealm.isChecked()) {
                donutProgress.setVisibility(View.VISIBLE);
                donutProgress.setInnerBackgroundColor(color);
                donutProgress.setTextColor(Color.WHITE);
            } else if (index == -1) {
                donutProgress.setVisibility(View.GONE);
            } else {
                donutProgress.setVisibility(View.VISIBLE);
            }

            //label
            RealmList<Category> categories = itemRealm.getCategories();
            String labl = categories == null || categories.isEmpty() ? "World" : categories.get(0).getLabel();
            lable.setText(labl);
            lable.setVisibility(View.VISIBLE);
            lable.setTag(ItemRealUtil.getPress(itemRealm));
            Glide.with(banner.getContext()).load(EntityHelper.getImageSrc(itemRealm.getImages())).crossFade().into(banner);
            //title
            title.setText("" + itemRealm.getTitle());
            title.setTag("" + itemRealm.getLink());
            title.setVisibility(View.VISIBLE);

            //quote
            addQuote(itemRealm);

            summaryEdition.setVisibility(View.VISIBLE);

            //statics
            addStatics(itemRealm);


            //infographs

            addInfographs(itemRealm);


            //longreads
            addLongreads(itemRealm);

            //locations
            addLocations(itemRealm);

            //slideshow

            addSlideShow(itemRealm);

            //videos
            addVideos(itemRealm);

            //wiki
            addWiki(itemRealm);

            //tweet
            addTweet(itemRealm);
            //reference
            addReference(itemRealm);
            line.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            if (getActivity() instanceof NewsDetailActivity) {
                int pageIndex = ((NewsDetailActivity) getActivity()).getCurrentIndex();
                if (pageIndex == index && !itemRealm.isChecked()) {
                    // LogUtil.e(TAG, "Fragment index:" + (index - 1) + ";viewpager index:" + pageIndex);
                    activeItem();
                }
            }
            if (index != -1) {
                event = getEvent(itemRealm);
            }
        } else {
            error.setVisibility(View.VISIBLE);
        }

    }

    private void addReference(ItemRealm itemRealm) {


        RealmList<Source> sources = itemRealm.getSources();


        if (sources != null && sources.size() > 0) {
            anchorArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (references.getVisibility() == View.GONE) {
                        references.setVisibility(View.VISIBLE);
                        toggleImage.setImageResource(R.drawable.reference_open);
                        DrawableCompat.setTint(toggleImage.getDrawable(), color);
                        references.post(new Runnable() {
                            @Override
                            public void run() {
                                nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    } else if (references.getVisibility() == View.VISIBLE) {
                        references.setVisibility(View.GONE);
                        toggleImage.setImageResource(R.drawable.reference_close);
                        DrawableCompat.setTint(toggleImage.getDrawable(), color);
                    }

                }
            });
        } else {
            anchorArea.setVisibility(View.GONE);
            referCount.setVisibility(View.GONE);
        }
        if (sources.size() == 1) {
            anchorArea.setVisibility(View.VISIBLE);
            ShapeDrawable shapeDrawableAnchor = new ShapeDrawable(new OvalShape());
            shapeDrawableAnchor.getPaint().setColor(color);
            shapeDrawableAnchor.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawableAnchor.getPaint().setStrokeWidth(DimensionUtil.dp2px(getResources(), 1f));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                anchor.setBackground(shapeDrawableAnchor);
                // toggleImage.setBackground(shapeDrawableAnchor);
            }
            DrawableCompat.setTint(anchor.getDrawable(), color);
            DrawableCompat.setTint(toggleImage.getDrawable(), color);

            referCount.setText("1 Reference");

            final View referencesItemView =
                    LayoutInflater.from(references.getContext()).inflate(R.layout.item_source, references, false);
            TextView publisher = $(referencesItemView, R.id.publisher);
            //publisher.setTypeface(typefaceBold);
            publisher.setText(sources.get(0).getPublisher());
            ViewGroup titleContainer = $(referencesItemView, R.id.titleContainer);

            View referencestitleItemView =
                    LayoutInflater.from(titleContainer.getContext()).inflate(R.layout.item_source_title, titleContainer, false);
            View view = $(referencestitleItemView, R.id.dot);
            ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(color);
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(shapeDrawable);
            }
            TextView sourceTitle = $(referencestitleItemView, R.id.sourceTitle);
            sourceTitle.setTypeface(typefaceLight);
            StringBuilder sb = new StringBuilder();
            sb.append("<a href=\"").append(sources.get(0).getUrl()).append("\">").append(sources.get(0).getTitle()).append("</a>");
            sourceTitle.setText(Html.fromHtml(sb.toString()));
            sourceTitle.setLinkTextColor(Color.BLACK);
            sourceTitle.setMovementMethod(LinkMovementMethod.getInstance());
            URLSpanNoUnderline.stripUnderlines(sourceTitle);
            titleContainer.addView(referencestitleItemView);

            references.addView(referencesItemView);

        } else {
            anchorArea.setVisibility(View.VISIBLE);
            ShapeDrawable shapeDrawableAnchor = new ShapeDrawable(new OvalShape());
            shapeDrawableAnchor.getPaint().setColor(color);
            shapeDrawableAnchor.getPaint().setStyle(Paint.Style.STROKE);
            shapeDrawableAnchor.getPaint().setStrokeWidth(DimensionUtil.dp2px(getResources(), 1f));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                anchor.setBackground(shapeDrawableAnchor);
                // toggleImage.setBackground(shapeDrawableAnchor);
            }
            DrawableCompat.setTint(anchor.getDrawable(), color);
            DrawableCompat.setTint(toggleImage.getDrawable(), color);

            referCount.setText(sources.size() + " References");

            ReferenceUtil.
                    groupSource(sources)
                    .subscribe(new Subscriber<Source>() {
                        String lastPublisher = "the elder";
                        List<List<Source>> bucketList = new ArrayList<>();
                        List<Source> bucket = null;

                        @Override
                        public void onCompleted() {

                            bucketList.add(bucket);

                            for (List<Source> list : bucketList) {

                                final View referencesItemView =
                                        LayoutInflater.from(references.getContext()).inflate(R.layout.item_source, references, false);
                                TextView publisher = $(referencesItemView, R.id.publisher);
                                publisher.setText(list.get(0).getPublisher());
                                // publisher.setTypeface(typefaceBold);
                                ViewGroup titleContainer = $(referencesItemView, R.id.titleContainer);
                                //title
                                for (Source source : list) {
                                    View referencestitleItemView =
                                            LayoutInflater.from(titleContainer.getContext()).inflate(R.layout.item_source_title, titleContainer, false);
                                    View view = $(referencestitleItemView, R.id.dot);
                                    ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
                                    shapeDrawable.getPaint().setColor(color);
                                    shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        view.setBackground(shapeDrawable);
                                    }
                                    TextView sourceTitle = $(referencestitleItemView, R.id.sourceTitle);
                                    sourceTitle.setTypeface(typefaceLight);
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("<a href=\"").append(source.getUrl()).append("\">").append(source.getTitle()).append("</a>");
                                    sourceTitle.setText(Html.fromHtml(sb.toString()));
                                    sourceTitle.setLinkTextColor(Color.BLACK);
                                    sourceTitle.setMovementMethod(LinkMovementMethod.getInstance());
                                    URLSpanNoUnderline.stripUnderlines(sourceTitle);
                                    titleContainer.addView(referencestitleItemView);
                                    // LogUtil.d(TAG, "---------onCompleted getPublisher: " + source.getPublisher() + " -----");
                                }
                                //LogUtil.d(TAG, "---------onCompleted round-----");

                                references.addView(referencesItemView);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Source source) {
                            if (!lastPublisher.equalsIgnoreCase(source.getPublisher())) {
                                if (bucket == null) {
                                    bucket = new ArrayList<Source>();
                                    bucket.add(source);
                                } else {
                                    bucketList.add(bucket);
                                    bucket = new ArrayList<Source>();
                                    bucket.add(source);
                                }
                                lastPublisher = source.getPublisher();
                            } else {
                                bucket.add(source);
                            }

                        }
                    });
        }
    }

    private void addTweet(ItemRealm itemRealm) {
        tweets.removeAllViews();
        TweetRealm tweetRealm = itemRealm.getTweets();
        if (tweetRealm != null && tweetRealm.getTweets() != null
                && tweetRealm.getTweets().size() > 0) {
            RealmList<TweetItemRealm> tweetItemRealms = tweetRealm.getTweets();
            for (TweetItemRealm tweetItemRealm : tweetItemRealms) {

                View tweetItemView =
                        LayoutInflater.from(tweets.getContext()).inflate(R.layout.item_twitter, tweets, false);

                TextView tweetName = $(tweetItemView, R.id.tweetName);
                tweetName.setText("" + tweetItemRealm.getUser().getName());
                // tweetName.setTypeface(typefaceBold);

                TextView tweetScreenName = $(tweetItemView, R.id.tweetScreenName);
                //  tweetScreenName.setTypeface(typefaceBold);
                StringBuilder sb = new StringBuilder();
                sb.append("<a href=\"https://mobile.twitter.com/").append(tweetItemRealm.getUser().getScreen_name()).append("\">");
                sb.append("@").append(tweetItemRealm.getUser().getScreen_name()).append("<a>");
                tweetScreenName.setText(Html.fromHtml(sb.toString()));
                tweetScreenName.setMovementMethod(LinkMovementMethod.getInstance());
                tweetScreenName.setLinkTextColor(Color.parseColor("#FF95CEFB"));
                URLSpanNoUnderline.stripUnderlines(tweetScreenName);

                TextView tweetTime = $(tweetItemView, R.id.tweetTime);
                String d = TweetTransformer.twitterTime(tweetItemRealm.getCreated_at());
                tweetTime.setText(d);


                TextView tweetText = $(tweetItemView, R.id.tweetText);
                tweetText.setTypeface(typefaceLight);
                String text = tweetItemRealm.getText();
                Spanned s = Html.fromHtml(TweetTransformer.convert(text));
                tweetText.setText(s);

                tweetText.setLinkTextColor(getResources().getColor(R.color.twitter_border));
                tweetText.setMovementMethod(LinkMovementMethod.getInstance());
                URLSpanNoUnderline.stripUnderlines(tweetText);

                if (!TextUtils.isEmpty(tweetItemRealm.getId())) {

                    final String replyUrl = "https://twitter.com/intent/tweet?in_reply_to=" + tweetItemRealm.getId();
                    ImageView twitterReply = $(tweetItemView, R.id.twitterReply);
                    twitterReply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(replyUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                    final String retweet = "https://twitter.com/intent/retweet?tweet_id=" + tweetItemRealm.getId() + "&related=twitterapi,twittermedia,twitter,support";
                    ImageView twitterRetweet = $(tweetItemView, R.id.twitterRetweet);
                    twitterRetweet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(retweet);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                    final String favoriteUrl = "https://twitter.com/intent/favorite?tweet_id=" + tweetItemRealm.getId();
                    ImageView twitterFavorite = $(tweetItemView, R.id.twitterFavorite);
                    twitterFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(favoriteUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                }


                tweets.addView(tweetItemView);

            }


        }
    }

    private void addWiki(ItemRealm itemRealm) {
        wikis.removeAllViews();
        RealmList<WikiRealm> wikiRealms = itemRealm.getWikis();
        if (wikiRealms != null && !wikiRealms.isEmpty()) {

            for (WikiRealm wikiRealm : wikiRealms) {

                View wikiItemView =
                        LayoutInflater.from(wikis.getContext()).inflate(R.layout.item_wiki, wikis, false);

                TextView wikiTitle = $(wikiItemView, R.id.wikiTitle);
                wikiTitle.setText("" + wikiRealm.getTitle());
                // wikiTitle.setTypeface(typefaceBold);
                TextView wikiText = $(wikiItemView, R.id.wikiText);
                wikiText.setText("" + wikiRealm.getText());
                wikiText.setTypeface(typefaceLight);

                ImageView wikiSearch = $(wikiItemView, R.id.wikiSearch);
                DrawableCompat.setTint(wikiSearch.getDrawable(), color);

                TextView searchTerms = $(wikiItemView, R.id.searchTerms);
                StringBuilder sb = new StringBuilder();
                for (StringRealmWrapper sw : wikiRealm.getSearchTerms()) {
                    sb.append(sw.getValue()).append("");
                }

                searchTerms.setText("learn more:" + sb.toString());

                searchTerms.setTextColor(color);
                searchTerms.setTypeface(typefaceLight);
                if (!TextUtils.isEmpty(wikiRealm.getUrl())) {
                    final String wikiUrl = wikiRealm.getUrl();
                    wikiItemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(wikiUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
                }

                wikis.addView(wikiItemView);

            }
        }
    }

    private void addVideos(ItemRealm itemRealm) {
        videos.removeAllViews();
        RealmList<Video> videoList = itemRealm.getVideos();
        if (videoList != null && !videoList.isEmpty()) {
            for (Video video : videoList) {

                View videoItemView =
                        LayoutInflater.from(videos.getContext()).inflate(R.layout.item_video, videos, false);
                TextView title = $(videoItemView, R.id.title);
                title.setText("" + video.getTitle());
                //title.setTypeface(typefaceBold);
                ImageView playIcon = $(videoItemView, R.id.playIcon);
                ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
                shapeDrawable.getPaint().setColor(color);
                shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    playIcon.setBackground(shapeDrawable);
                }

                ImageView imageView = $(videoItemView, R.id.thumbnail);
                Glide.with(imageView.getContext()).load(video.getThumbnail()).centerCrop().crossFade().into(imageView);
                RealmList<Stream> streams = video.getStreams();
                if (streams != null && streams.size() > 0) {
                    String url = null;
                    for (Stream stream : streams) {
                        if (!TextUtils.isEmpty(stream.getUrl())
                                && !TextUtils.isEmpty(stream.getMime_type())
                                && !"application/vnd.apple.mpegurl".equalsIgnoreCase(stream.getMime_type())
                                ) {
                            url = stream.getUrl();
                            break;
                        }
                    }
                    if (url != null) {
                        final String src = url;
                        videoItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent mpdIntent = new Intent(v.getContext(), MediaPlayerActivity.class);
                                mpdIntent.setData(Uri.parse(src));
                                mpdIntent.putExtra(MediaPlayerActivity.CONTENT_TYPE_EXTRA, Util.TYPE_OTHER);
                                mpdIntent.putExtra(MediaPlayerActivity.CONTENT_ID_EXTRA, src);
                                mpdIntent.putExtra(MediaPlayerActivity.PROVIDER_EXTRA, "");
                                startActivity(mpdIntent);
                            }
                        });

                    }
                }

                videos.addView(videoItemView);


            }

        }
    }

    private void addInfographs(ItemRealm itemRealm) {
        infographs.removeAllViews();
        RealmList<Infograph> infographList = itemRealm.getInfographs();
        if (infographList != null && infographList.size() > 0) {
            for (Infograph infograph : infographList) {

                View infographItemView =
                        LayoutInflater.from(infographs.getContext()).inflate(R.layout.item_infograph, infographs, false);

                TextView infographTitle = $(infographItemView, R.id.infographTitle);
                infographTitle.setText(infograph.getTitle());

                TextView infographCaption = $(infographItemView, R.id.infographCaption);
                infographCaption.setText(infograph.getCaption());

                ImageView infographImg = $(infographItemView, R.id.infographImg);

                String src = EntityHelper.getImageSrc(infograph.getImages());
                Glide.with(infographImg.getContext()).load(src).crossFade().into(infographImg);

                infographs.addView(infographItemView);
            }
        }
    }

    private void addStatics(ItemRealm itemRealm) {
        statDetail.removeAllViews();
        RealmList<StatDetail> statDetails = itemRealm.getStatDetail();
        if (statDetails != null && !statDetails.isEmpty()) {

            for (StatDetail stat : statDetails) {

                View staticItemView =
                        LayoutInflater.from(statDetail.getContext()).inflate(R.layout.item_statics, statDetail, false);
                TextView statDetailTitle = $(staticItemView, R.id.statDetailTitle);
                statDetailTitle.setText(stat.getTitle().getText());
                statDetailTitle.setTextColor(color);


                TextView statDetailValue = $(staticItemView, R.id.statDetailValue);
                statDetailValue.setText(stat.getValue().getText());
                statDetailValue.setTypeface(typefaceLight);

                TextView statDetailUnits = $(staticItemView, R.id.statDetailUnits);
                statDetailUnits.setText(stat.getUnits().getText());
                statDetailUnits.setTypeface(typefaceLight);

                TextView statDetailDescription = $(staticItemView, R.id.statDetailDescription);
                statDetailDescription.setText(stat.getDescription().getText());
                statDetailDescription.setTypeface(typefaceLight);
                statDetail.addView(staticItemView);

            }

        }
    }

    private void addQuote(ItemRealm itemRealm) {
        summary.removeAllViews();
        RealmList<Summary> summaries = itemRealm.getMultiSummary();
        if (summaries != null && !summaries.isEmpty()) {
            for (Summary item : summaries) {
                View summaryItemView =
                        LayoutInflater.from(summary.getContext()).inflate(R.layout.item_summary, summary, false);
                TextView textView = $(summaryItemView, R.id.summaryText);
                textView.setTypeface(typefaceLight);

                ViewGroup quoteContainer = $(summaryItemView, R.id.quoteContainer);
                textView.setText(item.getText());
                if (item.getQuote() == null || item.getQuote().getText() == null) {
                    quoteContainer.removeAllViews();
                } else {
                    Quote quote = item.getQuote();
                    TextView quoteSymbol = $(quoteContainer, R.id.quoteSymbol);
                    quoteSymbol.setTextColor(color);
                    TextView quoteText = $(quoteContainer, R.id.quoteText);
                    quoteText.setTextColor(color);
                    // quoteText.setTypeface(typefaceLight);
                    quoteText.setText(quote.getText());
                    TextView quoteSource = $(quoteContainer, R.id.quoteSource);
                    quoteSource.setText(quote.getSource());
                    //quoteSource.setTypeface(typefaceBold);
                    View verticalLine = $(quoteContainer, R.id.verticalLine);
                    verticalLine.setBackgroundColor(color);
                }
                summary.addView(summaryItemView);
            }
        }
    }

    private void addLongreads(ItemRealm itemRealm) {
        longreads.removeAllViews();
        RealmList<LongRead> longReads = itemRealm.getLongreads();
        if (longReads != null && !longReads.isEmpty()) {
            for (LongRead longRead : longReads) {

                View longReadItemView =
                        LayoutInflater.from(longreads.getContext()).inflate(R.layout.item_topic_in_depth, longreads, false);


                Image image = longRead.getImages();

                String src = getImageSource(image);

                if (src != null && src.length() > 0) {
                    ImageView longreadImg = $(longReadItemView, R.id.longreadImg);

                    Glide.with(longreads.getContext()).load(src).centerCrop().crossFade().into(longreadImg);
                }


                TextView longreadTitle = $(longReadItemView, R.id.longreadTitle);

                longreadTitle.setText(longRead.getTitle());
                longreadTitle.setTextColor(color);
                //longreadTitle.setTypeface(typefaceBold);

                TextView longreadPublisher = $(longReadItemView, R.id.longreadPublisher);
                longreadPublisher.setText(longRead.getPublisher());
                //longreadPublisher.setTypeface(typefaceBold);


                TextView longreadDescription = $(longReadItemView, R.id.longreadDescription);
                longreadDescription.setText(longRead.getDescription());
                longreadDescription.setTypeface(typefaceLight);
                if (!TextUtils.isEmpty(longRead.getUrl())) {
                    final String depthUrl = longRead.getUrl();
                    longReadItemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri = Uri.parse(depthUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });


                }

                longreads.addView(longReadItemView);

            }
        }
    }

    private void addLocations(ItemRealm itemRealm) {
        locations.removeAllViews();
        RealmList<Location> locationList = itemRealm.getLocations();
        if (locationList != null && !locationList.isEmpty()) {
            for (Location location : locationList) {

                View locationItemView =
                        LayoutInflater.from(locations.getContext()).inflate(R.layout.item_location, locations, false);
                TextView caption = $(locationItemView, R.id.caption);
                caption.setText(location.getCaption());
                final double latitude = Double.valueOf(TextUtils.isEmpty(location.getLatitude()) ? "0" : location.getLatitude());
                final double longitude = Double.valueOf(TextUtils.isEmpty(location.getLongtitude()) ? "0" : location.getLongtitude());
                final int zoomLevel = Integer.valueOf(TextUtils.isEmpty(location.getZoonLevel()) ? "0" : location.getZoonLevel());
                AMapOptions aMapOptions = new AMapOptions();
                aMapOptions.scaleControlsEnabled(false).scrollGesturesEnabled(false);
                SupportMapFragment supportMapFragment = SupportMapFragment.newInstance(aMapOptions);

                LatLng latLng = new LatLng(latitude, longitude);
                final ImageView errorTag = $(locationItemView, R.id.errorTag);
                final ImageView locationImg = $(locationItemView, R.id.locationImg);
                AMap aMap = supportMapFragment.getMap();
                aMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latLng, zoomLevel, 0, 0));
                aMap.moveCamera(cameraUpdate);
                aMap.getUiSettings().setAllGesturesEnabled(false);
                aMap.getUiSettings().setZoomControlsEnabled(false);
                aMap.getUiSettings().setZoomGesturesEnabled(false);
                aMap.getUiSettings().setMyLocationButtonEnabled(false);
                aMap.setMapLanguage(AMap.ENGLISH);
                aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                    @Override
                    public void onMapLoaded() {
                        errorTag.setVisibility(View.GONE);
                        locationImg.setVisibility(View.GONE);
                    }
                });
                getChildFragmentManager().beginTransaction().add(R.id.mapContainer, supportMapFragment).commit();
                final String captionStr = location.getCaption();
                final String name = location.getName();
                $(locationItemView, R.id.mask).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), LocationActivity.class);
                        intent.putExtra(LocationActivity.LATITUDE, latitude);
                        intent.putExtra(LocationActivity.LONGITUDE, longitude);
                        intent.putExtra(LocationActivity.ZOOM_LEVER, zoomLevel);
                        intent.putExtra(LocationActivity.CAPTION, captionStr);
                        intent.putExtra(LocationActivity.NAME, name);
                        startActivity(intent);
                    }
                });
                // caption.setTypeface(typefaceBold);
                locations.addView(locationItemView);
            }
        }
    }

    private void addSlideShow(ItemRealm itemRealm) {
        Slideshow slideshow1 = itemRealm.getSlideshow();
        RealmList<Photo> photos = slideshow1.getPhotos();
        final ArrayList<SlideItem> slideItems = new ArrayList<SlideItem>();
        for (Photo photo : photos) {
            SlideItem slideItem = new SlideItem();
            slideItem.caption = photo.getCaption();
            slideItem.headline = photo.getHeadline();
            slideItem.provider_name = photo.getProvider_name();
            slideItem.url = EntityHelper.getImageSrc(photo.getImages());
            slideItems.add(slideItem);
            galleryAdapter.addItem(slideItem);
        }
        if (slideItems.isEmpty()) {
            singleImage.setVisibility(View.GONE);
            gallery.setVisibility(View.GONE);
        } else if (slideItems.size() == 1) {
            singleImage.setVisibility(View.VISIBLE);
            gallery.setVisibility(View.GONE);
            String src = EntityHelper.getImageSrc(photos.get(0).getImages());
            Glide.with(singleImage.getContext()).load(src).centerCrop().crossFade().into(singleImage);
            singleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SlideShowActivity.class);
                    intent.putParcelableArrayListExtra(SlideShowActivity.DATA, slideItems);
                    intent.putExtra(SlideShowActivity.CURRENT_INDEX, 0);
                    startActivity(intent);
                }
            });

        } else {
            singleImage.setVisibility(View.GONE);
            gallery.setVisibility(View.VISIBLE);
            galleryAdapter.addItemClickListenr(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(gallery.getContext(), SlideShowActivity.class);
                    intent.putParcelableArrayListExtra(SlideShowActivity.DATA, slideItems);
                    intent.putExtra(SlideShowActivity.CURRENT_INDEX, position);
                    startActivity(intent);
                }
            });


        }


    }

    private Subscription getEvent(final ItemRealm itemRealm) {
        if (getActivity() instanceof NewsDetailActivity) {
            return ((NewsDetailActivity) getActivity())
                    .getRxBus()
                    .toObserverable(Integer.class)
                    .onBackpressureBuffer()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Integer>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Integer integer) {
                            if (index == integer) {
                                // LogUtil.e(TAG, "Fragment index:" + (index - 1) + ";viewpager index:" + integer);
                                if (!itemRealm.isChecked()) {
                                    activeItem();
                                }
                            }

                        }
                    })
                    ;
        }
        return null;
    }


    private String getImageSource(Image image) {

        String src = null;
        if (image != null) {
            src = image.getUrl();
            RealmList<ImageAsset> imageAssets = image.getImage_assets();
            for (ImageAsset imageAsset : imageAssets) {
                if ("pc:size=square".equalsIgnoreCase(imageAsset.getTag()) || "pc:size=square_large".equalsIgnoreCase(imageAsset.getTag())) {
                    src = imageAsset.getUrl();
                    break;

                }
                src = imageAsset.getUrl();
            }

        }
        return src;

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
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
        if (updateIndexSubscription != null) {
            updateIndexSubscription.isUnsubscribed();
            updateIndexSubscription = null;
        }
        if (event != null) {
            event.unsubscribe();
            event = null;
        }
    }
}
