package com.iwillow.android.digestnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.iwillow.android.digestnews.db.EntityHelper;
import com.iwillow.android.digestnews.entity.DetailItem;
import com.iwillow.android.digestnews.entity.ItemRealm;
import com.iwillow.android.digestnews.util.Helper;
import com.iwillow.android.digestnews.widget.NewsAdapter;
import com.iwillow.android.lib.util.DateUtil;
import com.iwillow.android.lib.widget.BaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by https://www.github.com/iwillow on 2016/4/29.
 */
public class NewsListFragment extends BaseFragment implements MoreDigestDialog.NoticeDialogListener {
    private final String TAG = "NewsListFragment";
    private NewsAdapter adapter;
    private Realm realm;
    private Subscription subscription;
    private RecyclerView recyclerView;
    private ImageButton menu;
    private boolean initFooterView = false;
    private ArrayList<DetailItem> list = new ArrayList<>();
    private int mSection;
    private int mEdition;
    private String mDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    private void getConfg() {
        Observable
                .create(new Observable.OnSubscribe<Map<String, String>>() {
                    @Override
                    public void call(Subscriber<? super Map<String, String>> subscriber) {
                        try {
                            SharedPreferences spf = getContext().getSharedPreferences(EditionDialog.PREFS_NAME, 0);
                            int selectedSection = spf.getInt(EditionDialog.SECTION_SELECTED, MoreDigestDialog.SECTION_MORNING);
                            String dateSection = spf.getString(EditionDialog.DATE_SELECTED, Helper.format(new Date()));
                            int edition = spf.getInt(EditionDialog.EDITION, EditionDialog.EDITION_INTERNATIONAL);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(EditionDialog.SECTION_SELECTED, String.valueOf(selectedSection));
                            map.put(EditionDialog.DATE_SELECTED, dateSection);
                            map.put(EditionDialog.EDITION, String.valueOf(edition));
                            subscriber.onNext(map);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, String> map) {
                        mEdition = Integer.parseInt(map.get(EditionDialog.EDITION));
                        mDate = map.get(EditionDialog.SECTION_SELECTED);
                        mSection = Integer.parseInt(map.get(EditionDialog.SECTION_SELECTED));
                        adapter.resetArea(mEdition, mSection, mDate);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news_list;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (initFooterView) {
            updateFooterView();
            getConfg();
        }
    }

    @Override
    protected void initView(final View rootView) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        menu = (ImageButton) rootView.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.news_list_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.more_digest:
                                moreDigest();
                                return true;
                            case R.id.send_feedback:
                                sendEmail();
                                return true;
                            case R.id.settings:
                                setting();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        adapter = new NewsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsAdapter.ViewHolder holder, int position) {

                Intent intent = new Intent(rootView.getContext(), NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.INDEX, position);
                intent.putExtra(NewsDetailActivity.MORE, true);
                intent.putParcelableArrayListExtra(NewsDetailActivity.DATA, list);
                startActivityForResult(intent, 0x110);

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int firstCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition > 1) {
                    if (menu.getVisibility() == View.VISIBLE) {
                        menu.setVisibility(View.GONE);
                    }
                } else {
                    if (menu.getVisibility() == View.GONE) {
                        menu.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
        subscription = load();
    }

    private void moreDigest() {
        MoreDigestDialog moreDigestDialog = new MoreDigestDialog();
        Bundle bundle = new Bundle();
        bundle.putString(MoreDigestDialog.DATE_SELECTED, mDate);
        bundle.putInt(MoreDigestDialog.SECTION_SELECTED, mSection);
        moreDigestDialog.show(getChildFragmentManager(), "moreDigest");
    }

    private void setting() {
        SettingsDialog settingsDialog = new SettingsDialog();
        settingsDialog.show(getChildFragmentManager(), "setting");
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

    private Subscription load() {

        return realm.asObservable()
                .onBackpressureBuffer()
                .map(new Func1<Realm, ArrayList<DetailItem>>() {
                    @Override
                    public ArrayList<DetailItem> call(Realm realm) {
                        ArrayList<DetailItem> list = new ArrayList<DetailItem>();
                        Date date=new Date();
                        String str = DateUtil.format(date, "yyyy-MM-dd");
                        int section=  DateUtil.getTimeSection();
                        if(section==DateUtil.SECTION_EVENING_YESTERDAY||section==DateUtil.SECTION_MORNING_TODAY){
                            str=DateUtil.format(DateUtil.getPreDay(date), "yyyy-MM-dd");
                        }
                        RealmResults<ItemRealm> data = realm.where(ItemRealm.class).contains("published", str).findAllSorted("published");
                        for (ItemRealm itemRealm : data) {
                            DetailItem detailItem = EntityHelper.ItemRealm2DetailItem(itemRealm);
                            list.add(detailItem);
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<DetailItem>>() {
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
                    public void onNext(ArrayList<DetailItem> itemRealms) {

                        if (adapter != null) {
                            adapter.clear();
                            list.clear();
                            list.addAll(itemRealms);
                            for (DetailItem itemRealm : itemRealms) {
                                adapter.addItem(itemRealm);
                            }
                            addFooterView(itemRealms);
                            getConfg();
                        }


                    }
                });


    }

    private void addFooterView(ArrayList<DetailItem> itemRealms) {
        View footer = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.news_list_footer_view, recyclerView, false);
        adapter.setFooterView(footer);
        View toggleButton = adapter.getFooterView().findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extraNews();
            }
        });

        initFooterView = true;
    }

    private void updateFooterView() {
        if (adapter != null) {

        }

    }

    public void extraNews() {
        Intent intent = new Intent(getContext(), ExtraNewsListActivity.class);
        intent.putExtra(ExtraNewsListActivity.ALL_CHECKED, adapter.isAllChecked());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.move_in, R.anim.move_out);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }


    @Override
    public void onItemclick(int section, String date) {
        mSection = section;
        mDate = date;
        Observable
                .create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        try {
                            SharedPreferences settings = getActivity().getSharedPreferences(EditionDialog.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt(EditionDialog.SECTION_SELECTED, mSection);
                            editor.putString(EditionDialog.DATE_SELECTED, mDate);
                            subscriber.onNext(editor.commit());
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            adapter.resetArea(mEdition, mSection, mDate);
                            Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x110 && resultCode == 0x110) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    extraNews();
                }
            }, 300);
        }
    }
}
