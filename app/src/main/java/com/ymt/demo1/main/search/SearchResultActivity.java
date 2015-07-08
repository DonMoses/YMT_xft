package com.ymt.demo1.main.search;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.SearchString;
import com.ymt.demo1.main.BaseFloatActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/8
 */
public class SearchResultActivity extends BaseFloatActivity {
    private int updateIndex;
    SharedPreferences sharedPreferences;
    private PullToRefreshListView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SearchActivity.SEARCH_PREFERENCES, MODE_PRIVATE);
        updateIndex = sharedPreferences.getInt(SearchActivity.UPDATE_SEARCH_INDEX, 0);
        setContentView(R.layout.activity_search_result);
        initView();
    }

    private int size;

    protected void initView() {
        Spinner spinner = (Spinner) findViewById(R.id.search_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"全部", "资讯", "知识平台", "论坛", "咨询"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示

        final EditText searchTxt = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        //创建时搜索类型和关键字
        int typePosi = getIntent().getIntExtra("position", 0);
        String keyW = getIntent().getStringExtra("keyword");

        spinner.setSelection(typePosi);
        searchTxt.setText(keyW);

        final List<SearchString> searchedStrs = DataSupport.findAll(SearchString.class);
        size = searchedStrs.size();
        final ArrayList<String> searched = new ArrayList<>();
        for (int i = 0; i < searchedStrs.size(); i++) {
            searched.add(searchedStrs.get(i).getSearchedString());
        }

         /*
        searchBtn 事件
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(searchTxt.getText().toString())) {
                    //更新数据
                    if (!searched.contains(searchTxt.getText().toString())) {
                        //获取输入框内容，搜索内容，加入搜索数据库表
                        if (size >= 10) {
                            ContentValues values = new ContentValues();
                            values.put("searchedstring", searchTxt.getText().toString());

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
                            saveString(searchTxt.getText().toString());
                        }

                        searched.add(searchTxt.getText().toString());

                    }

                    ProgressBar progressBar = new ProgressBar(SearchResultActivity.this);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    progressBar.setLayoutParams(params);
                    resultView.setEmptyView(progressBar);

                } else {
                    Toast.makeText(SearchResultActivity.this, "请输入搜索关键词...", Toast.LENGTH_SHORT).show();
                }

                //清空输入内容， 输入框改变为不聚焦
//                searchTxt.setText(null);
                searchTxt.clearFocus();
            }
        });
        initResultView();

    }

    protected void initResultView() {
        resultView = (PullToRefreshListView) findViewById(R.id.result_list_view);
        //todo
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
