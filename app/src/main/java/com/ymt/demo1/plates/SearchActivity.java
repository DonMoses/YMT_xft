package com.ymt.demo1.plates;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;

/**
 * Created by Dan on 2015/4/9
 */
public class SearchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    protected void initView() {
        final EditText searchTxt = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入框内容，搜索内容
                String searchStr = searchTxt.getText().toString();
                // todo 搜索逻辑
                Toast.makeText(SearchActivity.this, "搜索：" + searchStr, Toast.LENGTH_SHORT).show();
            }
        });


        /*
        * 初始化适配器控件
        * */

        GridView historyView = (GridView) findViewById(R.id.search_history_gridView);
        ListView hotView = (ListView) findViewById(R.id.search_hot_listView);

        String[] historyArray = new String[]{"第三方复核", "防火门", "中国消防通", "中国消防安全"};
        String[] hotArray = new String[]{"超高层建筑防火", "中国消防通", "中国消防通", "中国消防安全", "电影院疏散宽度", "第三方复核"};
        ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyArray);
        ArrayAdapter<String> hotAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hotArray);

        historyView.setAdapter(historyAdapter);
        hotView.setAdapter(hotAdapter);
    }

}
