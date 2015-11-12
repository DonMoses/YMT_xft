package com.ymt.demo1.main.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.AllSearchListAdapter;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.beams.FullSearchItem;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/8
 */
public class FullSearchResultActivity extends BaseFloatActivity {
    private int start;
    private int pageSize;
    private PullToRefreshListView resultListView;
    private RequestQueue mQueue;
    private int typePos;
    private String keyW;
    private Spinner spinner;
    private EditText searchTxt;

    private AllSearchListAdapter allSearchListAdapter;
    private List<FullSearchItem> allSearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allSearchList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);
        //创建时搜索类型和关键字
        typePos = getIntent().getIntExtra("position", 0);
        keyW = getIntent().getStringExtra("keyword");
        setContentView(R.layout.activity_full_search_result);
        start = 0;
        pageSize = 10;
        initBaseView();
        initListView();
    }

    protected void initBaseView() {
        spinner = (Spinner) findViewById(R.id.search_spinner);
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"全部", "咨询", "知识", "论坛", "教育", "商品", "资讯"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示

        searchTxt = (EditText) findViewById(R.id.search_edit_text);
        ImageButton searchBtn = (ImageButton) findViewById(R.id.search_btn);

        spinner.setSelection(typePos);
        searchTxt.setText(keyW);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initListView();
                typePos = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         /*
        searchBtn 事件
         */
        searchBtn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             String inKw = searchTxt.getText().toString();
                                             if (!TextUtils.isEmpty(inKw)) {

                                                 int position = spinner.getSelectedItemPosition();
                                                 if ((typePos != position) || (!keyW.equals(inKw))) {
                                                     initListView();
                                                     typePos = position;
                                                     keyW = inKw;
                                                 }

                                             } else {
                                                 Toast.makeText(FullSearchResultActivity.this, "请输入搜索关键词...", Toast.LENGTH_SHORT).show();
                                             }

                                             //清空输入内容， 输入框改变为不聚焦
                                             //searchTxt.setText(null);
                                             searchTxt.clearFocus();
                                         }
                                     }

        );
        searchBtn.callOnClick();
        ProgressBar progressBar = new ProgressBar(FullSearchResultActivity.this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar.setLayoutParams(params);

    }

    protected void initListView() {
        allSearchList.clear();
        resultListView = (PullToRefreshListView) findViewById(R.id.result_list_view);
        resultListView.setDividerPadding(1);

        int pos = spinner.getSelectedItemPosition();
        String kword = searchTxt.getText().toString();
        start = 0;
        allSearchListAdapter = new AllSearchListAdapter(FullSearchResultActivity.this);
        resultListView.setAdapter(allSearchListAdapter);
        allSearchListAdapter.setList(allSearchList, kword);
        mQueue.add(getAllSearch(AppContext.now_user_id, pos, start, pageSize, kword));

        resultListView.onRefreshComplete();

        resultListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                searchStr = null;
                int pos = spinner.getSelectedItemPosition();
                String inKw = searchTxt.getText().toString();
                start = 0;
                allSearchList.clear();
                allSearchListAdapter.setList(allSearchList, inKw);
                mQueue.add(getAllSearch(AppContext.now_user_id, pos, start, pageSize, inKw));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                int pos = spinner.getSelectedItemPosition();
                String inKw = searchTxt.getText().toString();
                mQueue.add(getAllSearch(AppContext.now_user_id, pos, start, pageSize, inKw));
            }
        });

    }

    /**
     * 全文检索
     */
    private String searchStr = null;

    protected StringRequest getAllSearch(int userId, int queryType, int start, int limit, final String queryInfo) {
        Log.e("TAG", ">>>>>>>>.full search url: " + BaseURLUtil.getFullSearch(userId, queryType, start, limit, queryInfo));
        return new StringRequest(BaseURLUtil.getFullSearch(userId, queryType, start, limit, queryInfo), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("TAG", ">>>>>>>>>>>>json: " + s);
                if ((TextUtils.isEmpty(searchStr)) || ((!TextUtils.isEmpty(searchStr)) && (searchStr.equals(s)))) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        JSONArray array = jsonObject1.getJSONArray("docs");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            FullSearchItem item = new FullSearchItem();
                            item.setThe_id(obj.optString("id"));
                            item.setDoc_title(obj.optString("doc_title"));
                            item.setDoc_url(obj.optString("doc_url"));
                            item.setDoc_url_app(obj.optString("doc_url_app"));
                            item.setDoc_type(obj.optString("doc_type"));
                            item.setSub_type(obj.optString("sub_type"));
                            item.setCreate_date(obj.optString("create_date"));
                            item.setPic_url(obj.optString("pic_url"));
                            item.setDoc_content(obj.optString("doc_content"));
                            allSearchList.add(item);
                            allSearchListAdapter.setList(allSearchList, queryInfo);
                        }

                        resultListView.onRefreshComplete();
                        searchStr = s;
                    } catch (JSONException e) {
                        AppContext.toastBadJson();
                        resultListView.onRefreshComplete();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                resultListView.onRefreshComplete();
            }
        });
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
