package com.ymt.demo1.main.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 */
public class SearchActivity extends BaseFloatActivity {
    private ArrayList<String> hisList;
    private ArrayList<String> hotList;
    private ArrayAdapter<String> historyAdapter;
    private ArrayAdapter<String> hotAdapter;
    private MyHandler myHandler = new MyHandler(this);
    private GridView historyView;
    private GridView hotView;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        hisList = new ArrayList<>();
        hotList = new ArrayList<>();
        setContentView(R.layout.activity_search);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hisList.size() > 0) {
            hisList.clear();
        }
        if (hotList.size() > 0) {
            hotList.clear();
        }
        mQueue.add(getHisKW(AppContext.now_user_id, 0, 100));
        mQueue.add(getHotKW(0, 12));
    }

    /**
     * activity动画
     */

    protected void initView() {
        final Spinner spinner = (Spinner) findViewById(R.id.search_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"全部", "专家", "科研", "规范", "视频", "咨询"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示

        final EditText searchTxt = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        /*
        * 历史搜索、热门搜索
        */
        historyView = (GridView) findViewById(R.id.search_history_gridView);
        hotView = (GridView) findViewById(R.id.search_hot_gridView);
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(80, 80);
        ProgressBar hisProgress = new ProgressBar(this);
        hisProgress.setIndeterminate(true);
        hisProgress.setLayoutParams(layoutParams);
        historyView.setEmptyView(hisProgress);
        ProgressBar hotProgress = new ProgressBar(this);
        hotProgress.setIndeterminate(true);
        hotProgress.setLayoutParams(layoutParams);
        hotView.setEmptyView(hotProgress);

        historyAdapter = new ArrayAdapter<>(this, R.layout.item_text_pop_action, hisList);
        hotAdapter = new ArrayAdapter<>(this, R.layout.item_text_pop_action, hotList);

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

                    //跳转到搜索结果界面
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("position", spinner.getSelectedItemPosition()); //搜索类型
                    intent.putExtra("keyword", kw);             //搜索关键字
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);    //界面动画

                    //清空输入内容， 输入框改变为不聚焦
//                searchTxt.setText(null);
                    searchTxt.clearFocus();
                } else {
                    Toast.makeText(SearchActivity.this, "请输入搜索关键词...", Toast.LENGTH_SHORT).show();
                }


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
                hisList.clear();
                historyAdapter.notifyDataSetChanged();
            }
        });

    }

    protected StringRequest getHisKW(String user_id, int start, int limit) {
        return new StringRequest(BaseURLUtil.getHistoryKW(user_id, start, limit), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str = object.getString("attr");
                            if ((!hisList.contains(str)) && (hisList.size() <= 16)) {
                                hisList.add(str);
                                historyAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                myHandler.sendEmptyMessage(0);
            }
        });
    }

    protected StringRequest getHotKW(int start, int limit) {
        return new StringRequest(BaseURLUtil.getHotKW(start, limit), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = array.getJSONObject(i);
                            hotList.add(object.getString("attr"));
                            hotAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    myHandler.sendEmptyMessage(1);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                myHandler.sendEmptyMessage(1);
            }
        });
    }

    static class MyHandler extends Handler {
        private WeakReference<SearchActivity> reference;

        public MyHandler(SearchActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SearchActivity activity = reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        View view1 = activity.historyView.getEmptyView();
                        if (view1 != null) {
                            view1.setVisibility(View.GONE);
                        }
                        break;
                    case 1:
                        View view2 = activity.hotView.getEmptyView();
                        if (view2 != null) {
                            view2.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
