package com.ymt.demo1.main.search;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.SearchString;
import com.ymt.demo1.main.BaseFloatActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/9
 */
public class SearchActivity extends BaseFloatActivity {
    public static final String SEARCH_PREFERENCES = "update_search_index_preferences";
    public static final String UPDATE_SEARCH_INDEX = "index";
    private int updateIndex;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        sharedPreferences = getSharedPreferences(SearchActivity.SEARCH_PREFERENCES, MODE_PRIVATE);
        updateIndex = sharedPreferences.getInt(SearchActivity.UPDATE_SEARCH_INDEX, 0);
        initView();

    }

    /**
     * activity动画
     */

    private int size;

    protected void initView() {
        final Spinner spinner = (Spinner) findViewById(R.id.search_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"全部", "专家", "科研", "规范", "视频", "咨询分类"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示

        final EditText searchTxt = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        /*
        * 初始化适配器控件
        */

        GridView historyView = (GridView) findViewById(R.id.search_history_gridView);
        GridView hotView = (GridView) findViewById(R.id.search_hot_gridView);

        final List<SearchString> searchedStrs = DataSupport.findAll(SearchString.class);
        size = searchedStrs.size();
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
                String kw = searchTxt.getText().toString();
                if (!TextUtils.isEmpty(kw)) {
                    //更新数据
                    if (!searched.contains(kw)) {
                        //获取输入框内容，搜索内容，加入搜索数据库表
                        if (size >= 10) {
                            ContentValues values = new ContentValues();
                            values.put("searchedstring", kw);

                            //更新index，则下次输入后更新到上一次的下一个坐标
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            DataSupport.update(SearchString.class, values, updateIndex + 1);
                            updateIndex++;
                            if (updateIndex > 10) {
                                updateIndex = 1;
                            }
                            editor.putInt(SearchActivity.UPDATE_SEARCH_INDEX, updateIndex);
                            editor.apply();
                        } else {
                            saveString(kw);
                        }

                        searched.add(kw);
                        //刷新适配器
                        historyAdapter.notifyDataSetChanged();

                    }

                } else {
                    Toast.makeText(SearchActivity.this, "请输入搜索关键词...", Toast.LENGTH_SHORT).show();
                }

                 /*
                    跳转到搜索结果界面
                     */
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("position", spinner.getSelectedItemPosition()); //搜索类型
                intent.putExtra("keyword", kw);             //搜索关键字
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);     //界面动画

                //清空输入内容， 输入框改变为不聚焦
//                searchTxt.setText(null);
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


        /*
        清除历史记录
         */
        TextView clearHisView = (TextView) findViewById(R.id.clear_search_history);
        clearHisView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(SearchString.class);
                searchedStrs.clear();
                historyAdapter.clear();
            }
        });

    }

    public void saveString(String str) {
        if (str != null && !str.equals("")) {
            SearchString searchString = new SearchString();
            searchString.setSearchedString(str);
            searchString.save();            //加入数据库
            size++;
        }

    }

}
