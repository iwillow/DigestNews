package com.iwillow.photoview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/5/19.
 */
public class CellViewActivity extends AppCompatActivity {
    FrameLayout c;
    ErrorGridView errorGridView;
    private LoadingView loadingView;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_view);
        button = (Button) findViewById(R.id.btReloadButton);
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.animateRadical();
            }
        });
        errorGridView = (ErrorGridView) findViewById(R.id.gvExclamation);
        errorGridView.setAdapter(new GridAdapter());

    }

    public static class GridAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 49;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
                viewHolder.itemView = convertView;
                viewHolder.textView = (TextView) convertView.findViewById(R.id.id_num);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position == 10 || position == 17 || position == 24 || position == 38) {
                viewHolder.textView.setBackgroundColor(Color.parseColor("#f33692"));
            }
            return convertView;
        }

        public static class ViewHolder {
            public View itemView;
            public TextView textView;
        }
    }
}
