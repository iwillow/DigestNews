package com.iwillow.photoview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.iwillow.android.lib.log.LogUtil;
import com.iwillow.android.lib.util.BitmapUtil;

/**
 * Created by Administrator on 2016/5/19.
 */
public class BulrAcitivty extends AppCompatActivity {
    private final String TAG = "BulrAcitivty";
    FrameLayout container;
    ImageView img;
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blur_activity);
        container = (FrameLayout) findViewById(R.id.container);
        img = (ImageView) findViewById(R.id.img);
        //img.setVisibility(View.GONE);

        // blur(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.dog)), container, 100);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            LogUtil.d(TAG, "width:" + container.getWidth() + ";height:" + container.getHeight());
            //Bitmap bitmap = BitmapUtil.fastblur(BitmapFactory.decodeResource(getResources(), R.drawable.dog), container.getWidth(), container.getHeight(), 200);
            Bitmap bitmap = BitmapUtil.blur(this, BitmapFactory.decodeResource(getResources(), R.drawable.dog), container.getWidth(), container.getHeight(), 25);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                container.setBackground(bitmapDrawable);
            } else {
                container.setBackgroundDrawable(bitmapDrawable);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap image, int width, int height, int blurRadius) {

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(blurRadius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        rs.destroy();
        return outputBitmap;
    }

}
