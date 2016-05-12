package com.iwillow.android.digestnews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

/**
 * Created by https://www.github.com/iwillow on 2016/5/12.
 */
public class SettingsDialog extends DialogFragment {
    private View notifications;
    private View edition;
    private View share;
    private View rate;
    private View works;
    private CheckBox push;
    private String Urlrate = "https://play.google.com/store/apps/details?id=com.iwillow.android.digestnews";

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
        notifications = rootView.findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push.setChecked(!push.isChecked());
            }
        });
        edition = rootView.findViewById(R.id.edition);
        edition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        share = rootView.findViewById(R.id.shareApp);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    }
}
