package com.iwillow.android.digestnews;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.iwillow.android.digestnews.entity.SlideItem;
import com.iwillow.android.digestnews.util.StatusBarCompat;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class SlideShowActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TextView press;
    TextView headLine;
    TextView caption;
    TextView label;
    public static final String DATA = "data";

    public static final String CURRENT_INDEX = "current_index";
    private int current_index = 0;
    private ArrayList<SlideItem> slideItems;
    private ViewGroup infoArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.showSystemUI(this);
        setContentView(R.layout.activity_slide_show);
        infoArea = (ViewGroup) findViewById(R.id.infoArea);
        press = (TextView) findViewById(R.id.press);
        headLine = (TextView) findViewById(R.id.headline);
        caption = (TextView) findViewById(R.id.caption);
        label = (TextView) findViewById(R.id.section_label);
        mViewPager = (ViewPager) findViewById(R.id.container);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        Intent intent = getIntent();
        slideItems = intent.getParcelableArrayListExtra(DATA);
        current_index = intent.getIntExtra(CURRENT_INDEX, 0);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), slideItems);
        // Set up the ViewPager with the sections adapter.;
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setCurrentItem(current_index);
        SlideItem slideItem = slideItems.get(current_index);
        press.setText("" + slideItem.getProvider_name());
        headLine.setText("" + slideItem.getHeadline());
        caption.setText("" + slideItem.getCaption());
        label.setText((current_index + 1) + " of " + slideItems.size());


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SlideItem slideItem = slideItems.get(position);
                press.setText("" + slideItem.getProvider_name());
                headLine.setText("" + slideItem.getHeadline());
                caption.setText("" + slideItem.getCaption());
                label.setText((position + 1) + " of " + slideItems.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void toggleMsg() {
        if (infoArea.getVisibility() == View.VISIBLE) {
            infoArea.setVisibility(View.GONE);
            //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            StatusBarCompat.hideSystemUI(this);
        } else {
            infoArea.setVisibility(View.VISIBLE);
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            StatusBarCompat.showSystemUI(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_slide_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String SLIDE_ITEM = "item";


        public PlaceholderFragment() {

        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(SlideItem slideItem) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();

            args.putParcelable(SLIDE_ITEM, slideItem);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Bundle args = getArguments();
            SlideItem slideItem = args.getParcelable(SLIDE_ITEM);
            final View rootView = inflater.inflate(R.layout.fragment_slide_show, container, false);
            final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            PhotoView photoView = (PhotoView) rootView.findViewById(R.id.photoView);
            Glide.with(rootView.getContext()).load(slideItem.getUrl()).crossFade().into(new GlideDrawableImageViewTarget(photoView) {
                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                    super.onResourceReady(resource, animation);
                    progressBar.setVisibility(View.GONE);
                }
            });
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    if (getActivity() instanceof SlideShowActivity) {
                        ((SlideShowActivity) getActivity()).toggleMsg();
                    }

                }
            });
            return rootView;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();

        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<SlideItem> slideItems;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<SlideItem> slideItems) {
            super(fm);

            this.slideItems = slideItems;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (slideItems == null) {
                return PlaceholderFragment.newInstance(null);
            }
            return PlaceholderFragment.newInstance(slideItems.get(position));
        }

        @Override
        public int getCount() {

            return slideItems == null ? 0 : slideItems.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }


}
