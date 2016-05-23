package com.iwillow.android.digestnews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwillow.android.digestnews.util.Helper;
import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.util.DateUtil;
import com.iwillow.android.lib.view.DonutProgress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by https://www.github.com.com/digest on 2016/5/16.
 */
public class MoreDigestDialog extends DialogFragment {

    public static final String PREFS_NAME = EditionDialog.PREFS_NAME;
    private DonutProgress donutProgress;
    private ValueAnimator valueAnimator;
    private static final String TAG = "MoreDigestDialog";
    private TextView next;
    private TextView tvH, tvM, tvS;
    private ImageView img;
    private TextView tvHour, tvMinute, tvSecond, infoType;
    private View layoutContainer;
    public static final int SECTION_MORNING = 1;
    public static final int SECTION_EVENING = 2;
    private int section;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private Subscription subscription;
    private LinearLayout linearLayout;
    public static final String SECTION_SELECTED = EditionDialog.SECTION_SELECTED;
    public static final String DATE_SELECTED = EditionDialog.DATE_SELECTED;
    private NoticeDialogListener mNoticeDialogListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof NewsListFragment) {
            mNoticeDialogListener = (NoticeDialogListener) getParentFragment();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.more_digest_dialog, container);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.newsChooser);
        Typeface typefaceLight = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Roboto-Light.ttf");
        donutProgress = (DonutProgress) rootView.findViewById(R.id.progress);
        donutProgress.setMax(100);
        layoutContainer = rootView.findViewById(R.id.container);
        img = (ImageView) rootView.findViewById(R.id.img);
        tvH = (TextView) rootView.findViewById(R.id.h);
        tvH.setTypeface(typefaceLight);
        tvM = (TextView) rootView.findViewById(R.id.m);
        tvM.setTypeface(typefaceLight);
        tvS = (TextView) rootView.findViewById(R.id.s);
        tvS.setTypeface(typefaceLight);
        tvHour = (TextView) rootView.findViewById(R.id.hour);
        tvHour.setTypeface(typefaceLight);
        tvMinute = (TextView) rootView.findViewById(R.id.minute);
        tvMinute.setTypeface(typefaceLight);
        tvSecond = (TextView) rootView.findViewById(R.id.second);
        tvSecond.setTypeface(typefaceLight);
        next = (TextView) rootView.findViewById(R.id.next);
        next.setTypeface(typefaceLight);
        infoType = (TextView) rootView.findViewById(R.id.infoType);
        infoType.setTypeface(typefaceLight);
        mHandler = initHandler();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        progressAnimation();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mHandler != null) {
            mHandler.removeMessages(0x110);
        }
    }

    private Subscription getSelected(final int newsSection) {
        return rx.Observable
                .create(new Observable.OnSubscribe<Map<String, String>>() {
                    @Override
                    public void call(Subscriber<? super Map<String, String>> subscriber) {
                        try {
                            SharedPreferences spf = getContext().getSharedPreferences(PREFS_NAME, 0);
                            int selectedSection = spf.getInt(SECTION_SELECTED, newsSection);
                            String dateSection = spf.getString(DATE_SELECTED, Helper.format(new Date()));
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(SECTION_SELECTED, String.valueOf(selectedSection));
                            map.put(DATE_SELECTED, dateSection);
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
                        LogUtil.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Map<String, String> map) {
                        LogUtil.e(TAG, "onNext " + map);
                        initChooseItem(map);
                    }
                });
    }


    private void progressAnimation() {
        next.setVisibility(View.VISIBLE);
        next.setAlpha(1f);
        layoutContainer.setVisibility(View.GONE);
        layoutContainer.setAlpha(0f);
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
        DeltaValue startValue = new DeltaValue();
        startValue.hour = 0;
        startValue.minute = 0;
        startValue.second = 0;
        startValue.progress = 0;
        DeltaValue endValue = calculateProgress();

        valueAnimator = ValueAnimator.ofObject(new MyTypeEvaluator(), startValue, endValue);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                DeltaValue deltaValue = (DeltaValue) animation.getAnimatedValue();
                donutProgress.setProgress(deltaValue.progress);
                if (section == SECTION_MORNING) {
                    mHour = 9 - deltaValue.hour;
                    infoType.setText("Util Evening Digest");
                    img.setImageResource(R.mipmap.countdown_day_moon);
                } else {
                    mHour = 13 - deltaValue.hour;
                    infoType.setText("Util Morning Digest");
                    img.setImageResource(R.mipmap.countdown_day_sun);
                }
                mMinute = 59 - deltaValue.minute;
                mSecond = 60 - deltaValue.second;
                tvHour.setText("" + format(mHour));
                tvMinute.setText("" + format(mMinute));
                tvSecond.setText("" + format(mSecond));
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                layoutContainer.setVisibility(View.VISIBLE);
                next.animate().alpha(0f).setDuration(200).start();
                layoutContainer.animate().alpha(1f).setDuration(200).setStartDelay(200).start();
                mHandler.removeMessages(0x110);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mHandler.sendEmptyMessageDelayed(0x110, 1000);
            }
        });

        valueAnimator.setDuration(1000);
        valueAnimator.start();
        if (subscription != null) {
            subscription.unsubscribe();
        }
        subscription = getSelected(section);
    }


    private String format(int value) {
        if (value < 10) {
            return String.valueOf("0" + value);
        }
        return String.valueOf(value);
    }

    public DeltaValue calculateProgress() {

        DeltaValue deltaValue = new DeltaValue();
        try {
            GregorianCalendar g = new GregorianCalendar();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String ymd = sdf1.format(g.getTime());
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date morning = sdf2.parse(ymd + " 08:00:00");
            java.util.Date evening = sdf2.parse(ymd + " 18:00:00");
            Date present = g.getTime();
            long delta = 0L;
            int day = 0, hour = 0, min = 0, second = 0, progress = 0;

            if (present.before(morning)) {
                section = SECTION_EVENING;
                LogUtil.d(TAG, " yesterday's evening digest");
                delta = morning.getTime() - present.getTime();
                progress = 100 - (int) (100 * delta / (1000 * 14 * 60 * 60));

            } else if (present.after(evening)) {
                section = SECTION_EVENING;
                LogUtil.d(TAG, "today's evening digest ");
                delta = present.getTime() - evening.getTime();
                progress = (int) (100 * delta / (1000 * 14 * 60 * 60));

            } else {
                section = SECTION_MORNING;
                LogUtil.d(TAG, "today's morning digest ");
                delta = present.getTime() - morning.getTime();
                progress = (int) (100 * delta / (1000 * 10 * 60 * 60));
            }

            day = (int) (delta / (24 * 60 * 60 * 1000));
            hour = (int) (delta / (60 * 60 * 1000) - day * 24);
            min = (int) ((delta / (60 * 1000)) - day * 24 * 60 - hour * 60);
            second = (int) (delta / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            deltaValue.hour = hour;
            deltaValue.minute = min;
            deltaValue.second = second;
            deltaValue.progress = progress;

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return deltaValue;
    }

    private void initChooseItem(Map<String, String> map) {
        LogUtil.d(TAG, "initChooseItem" + map);
        linearLayout.removeAllViews();
        for (int i = 5; i >= 0; i--) {
            Date date = DateUtil.getPreNDay(new Date(), i);
            final String str = Helper.format(date);
            View item = LayoutInflater.from(getContext()).inflate(R.layout.news_chooser_item, linearLayout, false);
            TextView newsDate = (TextView) item.findViewById(R.id.newsDate);
            ImageView divider = (ImageView) item.findViewById(R.id.divider);
            newsDate.setText(str.substring(4, 9));
            TextView newsDay = (TextView) item.findViewById(R.id.newsDay);
            newsDay.setText(str.substring(0, 3));
            ImageView daytime = (ImageView) item.findViewById(R.id.daytime);
            daytime.setTag(str);
            daytime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Toast.makeText(v.getContext(), "daytime:" + str, Toast.LENGTH_SHORT).show();
                    if (mNoticeDialogListener != null) {
                        mNoticeDialogListener.onItemclick(SECTION_MORNING, str);
                    }
                    dismiss();
                }
            });
            ImageView night = (ImageView) item.findViewById(R.id.night);
            night.setTag(str);
            night.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(), "night:" + str, Toast.LENGTH_SHORT).show();
                    if (mNoticeDialogListener != null) {
                        mNoticeDialogListener.onItemclick(SECTION_EVENING, str);
                    }
                    dismiss();
                }
            });
            if (section == SECTION_MORNING) {
                if (i == 5) {
                    daytime.setVisibility(View.GONE);
                    divider.setVisibility(View.INVISIBLE);
                    newsDate.setVisibility(View.INVISIBLE);
                    newsDay.setVisibility(View.INVISIBLE);
                } else if (i == 0) {
                    night.setVisibility(View.GONE);
                }
            } else if (section == SECTION_EVENING) {
                if (i == 5) {
                    item.setVisibility(View.GONE);
                } else if (i == 0) {


                }
            }
            int sectionSelected = Integer.parseInt(map.get(SECTION_SELECTED));
            String dateSelected = map.get(DATE_SELECTED);
            if (sectionSelected == SECTION_MORNING && str.equals(dateSelected)) {
                daytime.setBackgroundResource(R.drawable.count_down_check_drawable);
                daytime.setImageResource(R.mipmap.countdown_day_sun_w);
            } else if (sectionSelected == SECTION_EVENING && str.equals(dateSelected)) {
                night.setBackgroundResource(R.drawable.count_down_check_drawable);
                night.setImageResource(R.mipmap.countdown_day_moon_w);
            }

            linearLayout.addView(item);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
/*
    public static String format(Date date) {
        SimpleDateFormat myFmt1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss EEE");
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return String.valueOf(days[day - 1] + " " + myFmt1.format(date));

    }*/

    private Handler mHandler;


    private Handler initHandler() {
        return new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x110:

                        if (mSecond > 0 && mSecond < 60) {
                            mSecond--;
                        } else if (mSecond == 0) {
                            mSecond = 59;
                            if (mMinute > 0 && mMinute < 60) {
                                mMinute--;
                            } else {
                                mMinute = 59;
                                mSecond--;
                                if (mHour == 0) {
                                    progressAnimation();
                                    return;
                                } else {
                                    mHour--;
                                }

                            }
                        }
                        if (tvHour != null && tvMinute != null && tvSecond != null) {
                            tvHour.setText("" + format(mHour));
                            tvMinute.setText("" + format(mMinute));
                            tvSecond.setText("" + format(mSecond));
                        }
                        sendEmptyMessageDelayed(0x110, 1000);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public static class MyTypeEvaluator implements TypeEvaluator<DeltaValue> {


        @Override
        public DeltaValue evaluate(float fraction, DeltaValue startValue, DeltaValue endValue) {

            DeltaValue result = new DeltaValue();
            result.hour = (int) (startValue.hour + fraction * (endValue.hour - startValue.hour));
            result.minute = (int) (startValue.minute + fraction * (endValue.minute - startValue.minute));
            result.second = (int) (startValue.second + fraction * (endValue.second - startValue.second));
            result.progress = (int) (startValue.progress + fraction * (endValue.progress - startValue.progress));

            return result;
        }
    }

    public static class DeltaValue {
        public int hour;
        public int minute;
        public int second;
        public int progress;

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public int getProgress() {
            return progress;
        }

        public void setProgress(int progress) {
            this.progress = progress;
        }
    }

    public interface NoticeDialogListener {
        public void onItemclick(int section, String date);
    }

}
