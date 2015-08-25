package com.ymt.demo1.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.SearchString;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/22
 */
public class SearchViewUtil {
    private EditText inputView;
    private int updateIndex;
    SharedPreferences sharedPreferences;
    public static final String SEARCH_PREFERENCES = "search_keywords_preferences";
    public static final String UPDATE_SEARCH_INDEX = "search_keywords_index";

    /**
     * 数据库大小
     */
    private int size;

    public void initSearchView(final Context context) {
        sharedPreferences = context.getSharedPreferences(SEARCH_PREFERENCES, Context.MODE_PRIVATE);
        updateIndex = sharedPreferences.getInt(UPDATE_SEARCH_INDEX, 0);

        //输入框
        inputView = (EditText) ((Activity) context).findViewById(R.id.search_edit_text);
        //搜索按钮
        final ImageView searchBtn = (ImageView) ((Activity) context).findViewById(R.id.search_btn);
        /*
        * 初始化适配器控件
        * */

        final GridView historyView = (GridView) ((Activity) context).findViewById(R.id.search_history_gridView);

        //从数据库获得已搜索的关键字
        final List<SearchString> searchedStrs = DataSupport.findAll(SearchString.class);
        size = searchedStrs.size();
        final ArrayList<String> searched = new ArrayList<>();
        for (int i = 0; i < searchedStrs.size(); i++) {
            searched.add(searchedStrs.get(i).getSearchedString());
        }

        //todo 为热门话题创建数据
        final ArrayAdapter<String> historyAdapter = new ArrayAdapter<>(context, R.layout.item_view_common_quest_low, searched);
        historyView.setAdapter(historyAdapter);

        inputView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    historyView.setVisibility(View.VISIBLE);
                } else {
                    historyView.setVisibility(View.GONE);
                }
            }
        });

        /*
        searchBtn 事件
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新数据
                String str = inputView.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    Toast.makeText(context, "请输入关键字...", Toast.LENGTH_SHORT).show();
                } else if (!searched.contains(inputView.getText().toString())) {
                    //获取输入框内容，搜索内容，加入搜索数据库表. 只保存之多20条历史记录
                    //获取输入框内容，搜索内容，加入搜索数据库表
                    if (size >= 10) {
                        ContentValues values = new ContentValues();
                        values.put("searchedstring", inputView.getText().toString());

                        //更新index，则下次输入后更新到上一次的下一个坐标
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        DataSupport.update(SearchString.class, values, updateIndex + 1);
                        updateIndex++;
                        if (updateIndex > 10) {
                            updateIndex = 1;
                        }
                        editor.putInt(UPDATE_SEARCH_INDEX, updateIndex);
                        editor.apply();
                    } else {
                        saveString(inputView.getText().toString());
                    }

                    searched.add(inputView.getText().toString());
                    //刷新适配器
                    historyAdapter.notifyDataSetChanged();
                    Toast.makeText(context, "搜索：" + inputView.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                //清空输入内容， 输入框改变为不聚焦
                inputView.setText("");
                inputView.clearFocus();
            }
        });

        /*
        最近搜索gridView 单击事件
         */
        historyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getAdapter().getItem(position).toString();
                inputView.setText(str);
                inputView.setSelection(inputView.getText().toString().length());        //移动光标到最后
            }
        });
    }

    /**
     * 保存搜索记录到数据库
     */
    private void saveString(String str) {
        SearchString searchString = new SearchString();
        searchString.setSearchedString(str);
        searchString.save();            //加入数据库
        size++;
    }

    public void clearInputFocus() {
        inputView.clearFocus();
    }
}
