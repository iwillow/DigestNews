package com.iwillow.android.digestnews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.iwillow.android.digestnews.util.RxBus;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by https://www.github.com/iwillow on 2016/5/12.
 */
public class SettingsDialog extends DialogFragment {

    private View notifications;
    private View edition;
    private View share;
    private View rate;
    private View works;
    private View privacy;
    private CheckBox push;
    private TextView tvArea;
    private String Urlrate = "https://play.google.com/store/apps/details?id=com.iwillow.android.digestnews";
    private Subscription subscription;
    private Subscription subscription1;
    private int mEdition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings_dialog, container);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        push = (CheckBox) rootView.findViewById(R.id.push);
        privacy = rootView.findViewById(R.id.privacy);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditionDialog aboutDialog = new EditionDialog();
                Bundle bundle = new Bundle();
                bundle.putString(EditionDialog.PAGE, EditionDialog.PAGE_ABOUT);
                aboutDialog.setArguments(bundle);
                aboutDialog.show(getChildFragmentManager(), "noticeDialog");
            }
        });
        notifications = rootView.findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push.setChecked(!push.isChecked());
            }
        });
        edition = rootView.findViewById(R.id.edition);
        tvArea = (TextView) rootView.findViewById(R.id.area);


        edition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditionDialog editionDialog = new EditionDialog();
                Bundle bundle = new Bundle();
                bundle.putString(EditionDialog.PAGE, EditionDialog.PAGE_NOTICE);
                editionDialog.setArguments(bundle);
                editionDialog.show(getChildFragmentManager(), "noticeDialog");
            }
        });

        share = rootView.findViewById(R.id.shareApp);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLogoDialog editionDialog = new ShareLogoDialog();
                editionDialog.show(getChildFragmentManager(), "shareLogoDialog");
            }
        });

        rate = rootView.findViewById(R.id.rateApp);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Urlrate));
                startActivity(i);
            }
        });
        works = rootView.findViewById(R.id.works);
        works.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductGuideDialog productGuideDialog = new ProductGuideDialog();
                productGuideDialog.show(getChildFragmentManager(), "productDialog");
            }
        });
        subscription = getEvent();
        subscription1 = rx.Observable
                .create(new Observable.OnSubscribe<Integer>() {

                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        try {
                            SharedPreferences settings = getActivity().getSharedPreferences(EditionDialog.PREFS_NAME, 0);
                            int edition = settings.getInt(EditionDialog.EDITION, EditionDialog.EDITION_INTERNATIONAL);
                            subscriber.onNext(edition);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            subscriber.onError(e);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        if (subscription1 != null) {
                            subscription1.unsubscribe();
                            subscription1 = null;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mEdition = integer;
                        initArea(mEdition);
                    }
                });

    }

    private Subscription getEvent() {
        return RxBus
                .getInstance()
                .toObserverable(Integer.class)
                .onBackpressureBuffer()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        int edition = integer;
                        initArea(edition);
                        if (edition != mEdition) {
                            mEdition = edition;
                            Toast.makeText(getContext(), "reload data ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void initArea(int edition) {

        if (edition == EditionDialog.EDITION_USA) {
            tvArea.setText("United States");

        } else if (edition == EditionDialog.EDITION_UK) {

            tvArea.setText("United Kingdom");

        } else if (edition == EditionDialog.EDITION_CANADA) {

            tvArea.setText("Canada");

        } else if (edition == EditionDialog.EDITION_INTERNATIONAL) {

            tvArea.setText("International");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
