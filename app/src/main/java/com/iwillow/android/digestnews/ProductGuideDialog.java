package com.iwillow.android.digestnews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.iwillow.android.digestnews.util.BakedBezierInterpolator;
import com.iwillow.android.digestnews.view.CircleView;
import com.iwillow.android.digestnews.view.CircularRevealView;
import com.iwillow.android.digestnews.view.SunAndMoon;
import com.iwillow.android.lib.view.CirclePageIndicator;
import com.iwillow.android.lib.widget.BaseFragment;


/**
 * Created by https://github.com/iwillow  2016/4/13.
 */
public class ProductGuideDialog extends DialogFragment {

    private final String TAG = ProductGuideDialog.class.getSimpleName();
    private CirclePageIndicator mCirclePageIndicator;

    private ViewPager mViewPager;
    private boolean mViewPagerScrollingLeft;
    private float mPreviousPositionOffset;
    private int mPreviousPosition;
    private int mPageSelected;
    private int mViewPagerScrollState = ViewPager.SCROLL_STATE_SETTLING;
    private CircleView mCircleView;
    private TextView mSkip;

    private FragmentPagerAdapter adapter;

    private CircularRevealView mCircularRevealView;

    public ProductGuideDialog() {


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Black_NoTitleBar);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_product_guide_dialog, container, false);
        mCirclePageIndicator = (CirclePageIndicator) root.findViewById(R.id.indicator);
        mViewPager = (ViewPager) root.findViewById(R.id.viewPager);
        mSkip = (TextView) root.findViewById(R.id.skip);
        mCircularRevealView = (CircularRevealView) root.findViewById(R.id.revalView);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        mSkip.setTypeface(typeFace);
        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductGuideDialog.this.dismiss();
            }
        });
        adapter = new ProductTourPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                // Scrolling to left or right
                if ((positionOffset > mPreviousPositionOffset && position == mPreviousPosition) || (positionOffset < mPreviousPositionOffset && position > mPreviousPosition)) {
                    mViewPagerScrollingLeft = true;
                } else if (positionOffset < mPreviousPositionOffset) {

                    mViewPagerScrollingLeft = false;
                }
                //LogUtil.d(TAG,"position="+position+",mPreviousPosition="+mPreviousPosition+",positionOffset="+positionOffset+",mPreviousPositionOffset="+mPreviousPositionOffset);

                mPreviousPositionOffset = positionOffset;
                mPreviousPosition = position;
                //LogUtil.d(TAG, "onPageScrolled called(position=" + position + ",positionOffset=" + positionOffset + ",positionOffsetPixels=" + positionOffsetPixels + ")");
                if (position == 0) {
                    mSkip.setVisibility(View.VISIBLE);
                    mSkip.setAlpha(1.0f);
                } else if (position == 1) {
                    mSkip.setVisibility(View.VISIBLE);
                    mCirclePageIndicator.setVisibility(View.VISIBLE);
                    mSkip.setAlpha(1.0f - positionOffset);
                    mCirclePageIndicator.setAlpha(1.0f - positionOffset);
                } else if (position == 2) {
                    mCirclePageIndicator.setVisibility(View.GONE);
                    mSkip.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageSelected(int position) {
                mPageSelected = position;
                // LogUtil.i(TAG, "onPageSelected (position=" + mPageSelected + ")");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                mViewPagerScrollState = state;
                // LogUtil.i(TAG, "onPageScrollStateChanged (mViewPagerScrollState=" + mViewPagerScrollState + ")");
                if (mViewPagerScrollState == ViewPager.SCROLL_STATE_IDLE && mCircleView != null && mViewPager.getCurrentItem() == 2) {
                    mCircleView.setState(CircleView.BitmapState.ROTATION);
                }
            }
        });

        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {


                if (page.findViewById(R.id.mailbox) != null) {

                    animatePage1(page, position);
                    // LogUtil.i(TAG, "page1 transformPage(position=" + position + ";mViewPagerScrollState=" + mViewPagerScrollState + ";currentItem=" + mViewPager.getCurrentItem() + ")");

                }
                if (page.findViewById(R.id.sunAndMoon) != null) {

                    animatePage2(page, position);
                    // LogUtil.i(TAG, "page2 transformPage(position=" + position + ";mViewPagerScrollState=" + mViewPagerScrollState + ";currentItem=" + mViewPager.getCurrentItem() + ")");

                }

                if (page.findViewById(R.id.circleView) != null) {

                    animatePage3(page, position);


                    // LogUtil.i(TAG, "page3 transformPage(position=" + position + ";mViewPagerScrollState=" + mViewPagerScrollState + ";currentItem=" + mViewPager.getCurrentItem() + ")");
                }

            }
        });
        mCirclePageIndicator.setViewPager(mViewPager);

        return root;
    }


    private void animatePage1(View page, float position) {
        final View video = page.findViewById(R.id.video);
        final View infoGraphic = page.findViewById(R.id.infoGraphic);
        final View stockChart = page.findViewById(R.id.stockChart);
        final View sound = page.findViewById(R.id.sound);
        final View map = page.findViewById(R.id.map);
        final View wikipedia = page.findViewById(R.id.wikipedia);
        final View quote = page.findViewById(R.id.quote);
        if (mViewPager.getCurrentItem() == 0 && mViewPagerScrollState == ViewPager.SCROLL_STATE_SETTLING && position == 0f) {

            if (video.getAlpha() < 1) {
                AnimatorSet set = new AnimatorSet();
                Animator animatorVideo = ObjectAnimator.ofFloat(video, "alpha", 0f, 1f);
                Animator animatorInfoGraphic = ObjectAnimator.ofFloat(infoGraphic, "alpha", 0f, 1f);
                Animator animatorStockChart = ObjectAnimator.ofFloat(stockChart, "alpha", 0f, 1f);
                Animator animatorSound = ObjectAnimator.ofFloat(sound, "alpha", 0f, 1f);
                Animator animatorMap = ObjectAnimator.ofFloat(map, "alpha", 0f, 1f);
                Animator animatorQuote = ObjectAnimator.ofFloat(quote, "alpha", 0f, 1f);
                Animator animatorWikipedia = ObjectAnimator.ofFloat(wikipedia, "alpha", 0f, 1f);
                set.setDuration(250);
                set.playSequentially(animatorVideo, animatorInfoGraphic, animatorStockChart, animatorSound, animatorMap, animatorQuote, animatorWikipedia);
                set.start();
            }
        }

        if (position <= 0f && position > -1.0f && mViewPager.getCurrentItem() == 0) {

            int pageWidth = page.getWidth();
            video.setTranslationX(-position * 0.25f * pageWidth);
            sound.setTranslationX(-position * 0.15f * pageWidth);
            infoGraphic.setTranslationX(-position * 0.20f * pageWidth);
            stockChart.setTranslationX(-position * 0.64f * pageWidth);
            map.setTranslationX(position * 0.95f * pageWidth);
            quote.setTranslationX(position * 0.85f * pageWidth);
            wikipedia.setTranslationX(position * 0.74f * pageWidth);

        }

        if (position <= -1.0f) {
            //reset alpha
            video.setAlpha(0f);
            infoGraphic.setAlpha(0f);
            stockChart.setAlpha(0f);
            sound.setAlpha(0f);
            map.setAlpha(0f);
            wikipedia.setAlpha(0f);
            quote.setAlpha(0f);

            //reset position
            video.setTranslationX(0f);
            infoGraphic.setTranslationX(0f);
            stockChart.setTranslationX(0f);
            sound.setTranslationX(0f);
            map.setTranslationX(0f);
            wikipedia.setTranslationX(0f);
            quote.setTranslationX(0f);
        }
    }


    private void animatePage2(View page, float position) {
        SunAndMoon sunAndMoon = (SunAndMoon) page.findViewById(R.id.sunAndMoon);
        sunAndMoon.rotationAnim(mViewPagerScrollingLeft, position);

        final View img1 = page.findViewById(R.id.img1);
        final View img2 = page.findViewById(R.id.img2);
        final View img3 = page.findViewById(R.id.img3);
        final View img4 = page.findViewById(R.id.img4);
        if (mViewPager.getCurrentItem() == 1 && mViewPagerScrollState == ViewPager.SCROLL_STATE_SETTLING && position == 0f) {
            if (img1.getAlpha() < 1) {
                AnimatorSet animatorSet = new AnimatorSet();
                Animator animator1 = ObjectAnimator.ofFloat(img1, "alpha", 0f, 1f);
                Animator animator2 = ObjectAnimator.ofFloat(img2, "alpha", 0f, 1f);
                Animator animator3 = ObjectAnimator.ofFloat(img3, "alpha", 0f, 1f);
                Animator animator4 = ObjectAnimator.ofFloat(img4, "alpha", 0f, 1f);
                animatorSet.setDuration(200);
                animatorSet.playSequentially(animator1, animator2, animator3, animator4);
                animatorSet.start();
            }
        }
        if (mViewPager.getCurrentItem() != 1 && mViewPagerScrollState == ViewPager.SCROLL_STATE_SETTLING) {
            img1.setAlpha(0f);
            img2.setAlpha(0f);
            img3.setAlpha(0f);
            img4.setAlpha(0f);
        }
    }

    private void animatePage3(View page, float position) {
        CircleView circleView = (CircleView) page.findViewById(R.id.circleView);
        int pos[] = new int[2];
        circleView.getLocationOnScreen(pos);
      /*  final DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int cx = displayMetrics.widthPixels / 2;
        final int cy = displayMetrics.heightPixels / 2;*/
        final int cx = pos[0] + circleView.getMeasuredWidth() / 2;
        final int cy = pos[1] + circleView.getMeasuredHeight() / 2;
        mCircleView = circleView;
        if (mViewPager.getCurrentItem() >= 1 && position >= 0.0f && position <= 1.0f) {
            circleView.translateTheSpheres(position);
        }
        circleView.setOnStateChangedListener(new CircleView.OnStateChangedListener() {
            @Override
            public void onRadicalMoveOver() {
                final int color = Color.parseColor("#FFFAF0");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mCircularRevealView.setBackgroundColor(color);
                    mCircularRevealView.setVisibility(View.INVISIBLE);
                    int startRadius = 20;
                    int x = mCircularRevealView.getWidth() / 2;
                    int y = mCircularRevealView.getHeight() / 2;
                    final float endRadius = (float) Math.hypot(x, y);

                    Animator animator = ViewAnimationUtils.createCircularReveal(mCircularRevealView, cx, cy, startRadius, endRadius);
                    animator.setDuration(500);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ProductGuideDialog.this.dismiss();
                        }
                    });
                    animator.setInterpolator(BakedBezierInterpolator.getInstance());
                    mCircularRevealView.setVisibility(View.VISIBLE);
                    animator.start();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mCircularRevealView.reveal(cx, cy, color, 20, 500, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            ProductGuideDialog.this.dismiss();
                        }
                    });
                } else {
                    ProductGuideDialog.this.dismiss();
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public class ProductTourPagerAdapter extends FragmentPagerAdapter {


        public ProductTourPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    public static class PlaceholderFragment extends BaseFragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        protected int getLayoutId() {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    return R.layout.product_guide_page1;
                case 1:
                    return R.layout.product_guide_page2;
                case 2:
                    return R.layout.product_guide_page3;
            }
            return -1;
        }

        @Override
        protected void initView(View rootView) {

            final Button start = $(rootView, R.id.start);
            final CircleView circleView = $(rootView, R.id.circleView);
            final TextView textView1 = $(rootView, R.id.descptTitle1);
            final TextView textView2 = $(rootView, R.id.descptTitle2);
            Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
            textView1.setTypeface(typeFace);
            textView2.setTypeface(typeFace);
            if (start != null && circleView != null) {

                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        circleView.setState(CircleView.BitmapState.RADICAL);
                    }
                });


            }
        }
    }
}
