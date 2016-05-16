package com.iwillow.android.digestnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iwillow.android.digestnews.util.RxBus;
import com.iwillow.android.digestnews.util.StatusBarCompat;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.widget.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsDetailActivity extends AppCompatActivity {

    public static String TAG = "NewsDetailActivity";
    public static final String INDEX = "index";
    public static final String DATA = "data";

    private ArrayList<DetailItem> data;
    private ViewPager viewPager;
    private NewsDetailAdapter adapter;
    private int index;
    private RxBus rxBus;
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.showSystemUI(this);
        setContentView(R.layout.activity_news_detail);
        Intent intent = getIntent();
        rxBus = new RxBus();
        index = intent.getIntExtra(INDEX, -1);
        data = intent.getParcelableArrayListExtra(DATA);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new NewsDetailAdapter(getSupportFragmentManager(), data);
        viewPager.setAdapter(adapter);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setCurrentItem(index);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //int preIndex = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (currentIndex != position) {
                    currentIndex = position;
                  //  LogUtil.e(TAG, "onPageScrolled position:" + currentIndex);
                    rxBus.post(Integer.valueOf(currentIndex));
                }
            }

            @Override
            public void onPageSelected(int position) {
                //  LogUtil.d(TAG,"onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
               /* if (state == ViewPager.SCROLL_STATE_IDLE) {
                    LogUtil.e(TAG, "onPageScrollStateChanged");
                }*/

            }
        });


        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

    }

    public RxBus getRxBus() {
        return rxBus;
    }

    public static class NewsDetailAdapter extends FragmentPagerAdapter {
        private ArrayList<DetailItem> detailItemArrayList;

        public NewsDetailAdapter(FragmentManager fm, ArrayList<DetailItem> detailItemArrayList) {
            super(fm);
            this.detailItemArrayList = detailItemArrayList;
        }

        @Override
        public Fragment getItem(int position) {
            if (detailItemArrayList != null && !detailItemArrayList.isEmpty()) {
                if ((position % getCount()) < detailItemArrayList.size()) {
                    DetailItem item = detailItemArrayList.get(position % getCount());
                    return NewsDetailFragment.newInstance(item.uuid, item.color, (position + 1) % getCount());
                } else {
                    return new ExtraFragment();
                }
            } else {
                return new ExtraFragment();
            }
        }

        @Override
        public int getCount() {
            return detailItemArrayList == null ? 0 : detailItemArrayList.size() + 1;
        }
    }


    public static class DetailItem implements Serializable, Parcelable {
        public int color;
        // public int index;
        public String uuid;

        public DetailItem() {

        }

        public DetailItem(Parcel source) {
            color = source.readInt();
            // index = source.readInt();
            uuid = source.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(color);
            // dest.writeInt(index);
            dest.writeString(uuid);
        }

        public static final Parcelable.Creator<DetailItem> CREATOR = new Parcelable.Creator<DetailItem>()

        {
            @Override
            public DetailItem createFromParcel(Parcel source) {
                return new DetailItem(source);
            }

            @Override
            public DetailItem[] newArray(int size) {
                return new DetailItem[size];
            }
        };
    }

    public static class ExtraFragment extends BaseFragment {
        public ExtraFragment() {
        }

        @Override
        protected int getLayoutId() {
            return R.layout.fragment_read_extra_news;
        }

        @Override
        protected void initView(View rootView) {

        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
