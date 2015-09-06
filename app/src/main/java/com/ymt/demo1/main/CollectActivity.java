package com.ymt.demo1.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.MyTitle;

/**
 * Created by DonMoses on 2015/9/6
 */
public class CollectActivity extends BaseFloatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    private void initView() {
        ListView listView = (ListView) findViewById(R.id.collect_list_view);
        final String[] array = getResources().getStringArray(R.array.collect);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_collect, R.id.collect, array);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CollectActivity.this, array[position], Toast.LENGTH_SHORT).show();
                //todo 收藏界面
            }
        });
    }
}
