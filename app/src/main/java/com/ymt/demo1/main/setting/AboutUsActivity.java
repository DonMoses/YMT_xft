package com.ymt.demo1.main.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by Dan on 2015/4/3
 */
public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
    }

    protected void initView() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        ListView listView = (ListView) findViewById(R.id.about_us_list_view);
        final String[] array = getResources().getStringArray(R.array.about_us);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_view_common_quest_high, R.id.common_quest_item_view, array);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AboutUsActivity.this, array[position], Toast.LENGTH_SHORT).show();
            }
        });
    }

}
