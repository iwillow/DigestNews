package com.iwillow.photoview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.iwillow.android.lib.util.DimensionUtil;

public class MainActivity extends AppCompatActivity {
    private TextView menu;
    private NestedScrollView scrollView;
    private String TAG = "MainActivity";
    private float distance;
    private ImageView pic;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_main);
        pic = (ImageView) findViewById(R.id.pic);
        imageView = (ImageView) findViewById(R.id.img);
        menu = (TextView) findViewById(R.id.menu);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ScrollingActivity.class);
                startActivity(intent);
            }
        });
        Button changeColor = (Button) findViewById(R.id.changeColor);
        final ImageView imageView = (ImageView) findViewById(R.id.img);
        changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawableCompat.setTint(imageView.getDrawable(), ContextCompat.getColor(imageView.getContext(), R.color.colorPrimaryDark));
            }
        });
        distance = DimensionUtil.dp2px(getResources(), 300);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= distance) {
                    float fraction = 1.0f * scrollY / distance;
                    if (menu.getVisibility() == View.GONE) {
                        menu.setVisibility(View.VISIBLE);
                    }

                    menu.setScaleX(1.0f - 0.35f * fraction);
                    menu.setScaleY(1.0f - 0.35f * fraction);
                    menu.setAlpha(1.0f - 0.35f * fraction);
                    pic.setTranslationY(0.65f * fraction * scrollY);
                } else {
                    if (menu.getVisibility() == View.VISIBLE) {
                        menu.setVisibility(View.GONE);
                        menu.setScaleX(1.0f);
                        menu.setScaleY(1.0f);
                        menu.setAlpha(1.0f);
                    }
                }
            }
        });
        Button blur = (Button) findViewById(R.id.blur);
        blur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PathTypeActivity.class);
                startActivity(intent);
            }
        });
    }


    private void fullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        // | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
}
