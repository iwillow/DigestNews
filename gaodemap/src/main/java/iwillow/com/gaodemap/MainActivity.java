package iwillow.com.gaodemap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonMap;
    Button expandListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonMap = (Button) findViewById(R.id.button_map);
        expandListView = (Button) findViewById(R.id.button_expand_list_view);
        buttonMap.setOnClickListener(this);
        expandListView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_map:
                Intent i1 = new Intent(MainActivity.this, MapActivity.class);
                startActivity(i1);
                break;
            case R.id.button_expand_list_view:
                Intent i2 = new Intent(MainActivity.this, ExpandListViewActivity.class);
                startActivity(i2);
                break;
        }
    }
}
