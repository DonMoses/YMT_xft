package com.ymt.demo1.main;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.dbTables.SearchString;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/9
 */
public class SearchActivity extends BaseFloatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Connector.getDatabase();
        setContentView(R.layout.activity_search);
        initView();

    }

    protected void initView() {
        final EditText searchTxt = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        /*
        * 初始化适配器控件
        * */

        GridView historyView = (GridView) findViewById(R.id.search_history_gridView);
        ListView hotView = (ListView) findViewById(R.id.search_hot_listView);

        List<SearchString> searchedStrs = DataSupport.findAll(SearchString.class);
        final ArrayList<String> searched = new ArrayList<>();
        for (int i = 0; i < searchedStrs.size(); i++) {
            searched.add(searchedStrs.get(i).getSearchedString());
        }

        String[] hotArray = new String[]{"超高层建筑防火", "中国消防通", "消防设备", "中国消防安全", "电影院疏散宽度", "第三方复核"};
        final ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(this, R.layout.item_text_pop_action, searched);
        final ArrayAdapter<String> hotAdapter = new ArrayAdapter<>(this, R.layout.item_text_pop_action, hotArray);

        historyView.setAdapter(historyAdapter);
        hotView.setAdapter(hotAdapter);

        /*
        searchBtn 事件
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新数据
                if (!searched.contains(searchTxt.getText().toString())) {
                    //获取输入框内容，搜索内容，加入搜索数据库表
                    saveString(searchTxt.getText().toString());

                    searched.add(searchTxt.getText().toString());
                    //刷新适配器
                    historyAdapter.notifyDataSetChanged();
                }
                // todo 搜索逻辑
                Toast.makeText(SearchActivity.this, "搜索：" + searchTxt.getText().toString(), Toast.LENGTH_SHORT).show();
                //清空输入内容， 输入框改变为不聚焦
                searchTxt.setText(null);
                searchTxt.clearFocus();
            }
        });

        /*
        热门搜索listView 单击事件
         */
        hotView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getAdapter().getItem(position).toString();
                searchTxt.setText(str);
            }
        });
        /*
        最近搜索gridView 单击事件
         */
        historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getAdapter().getItem(position).toString();
                searchTxt.setText(str);
            }
        });

    }

    public void saveString(String str) {
        if (str != null && !str.equals("")) {
            SearchString searchString = new SearchString();
            searchString.setSearchedString(str);
            searchString.save();            //加入数据库

        }

    }

}
