package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.HotRecConsultAdapter;
import com.ymt.demo1.beams.expert_consult.HotConsult;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/5
 */
public class HotConsultListActivity extends BaseFloatActivity {
    private RequestQueue mRequestQueue;
    private int start;
    private int pageSize;
    private HotRecConsultAdapter consultAdapter;
    private List<HotConsult> mList;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_consult_list);

        start = 1;
        pageSize = 10;
        mRequestQueue = Volley.newRequestQueue(this);
        initTitle();
        initView();

    }

    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    protected void initView() {

        mList = new ArrayList<>();
        mList.addAll(DataSupport.findAll(HotConsult.class));
        PullToRefreshListView hotListView = (PullToRefreshListView) findViewById(R.id.hot_consult_list_view);
        consultAdapter = new HotRecConsultAdapter(this, HotRecConsultAdapter.HOT);
        hotListView.setAdapter(consultAdapter);
        consultAdapter.setHotList(mList);

        mHandler = new MyHandler(hotListView);
        mHandler.sendEmptyMessageAtTime(0, 1800);

        hotListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                mList.clear();
                mRequestQueue.add(hotConsultRequest(start, pageSize));
                Log.e("TAG", "start>>>>>>>>>>" + start);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = start + 1;
                mRequestQueue.add(hotConsultRequest(start, pageSize));
                Log.e("TAG", "start>>>>>>>>>>" + start);
            }
        });

        hotListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) parent.getAdapter();
                HotRecConsultAdapter adapter = (HotRecConsultAdapter) headerViewListAdapter.getWrappedAdapter();
                HotConsult consult = (HotConsult) adapter.getItem(position - 1);
                Intent intent = new Intent(HotConsultListActivity.this, HRConsultDetailActivity.class);
                intent.putExtra("consult", consult);
                intent.putExtra("type", "hot");
                startActivity(intent);
            }
        });
    }

    protected StringRequest hotConsultRequest(int start, int pageSize) {
//        Log.e("TAG", ">>>>hot>>>>>>>>.url>>>>>>>>>>>>>" + BaseURLUtil.doGetHotConsult(start, pageSize));
        return new StringRequest(BaseURLUtil.doGetHotConsult(start, pageSize), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonObject1.getInt("size");
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            HotConsult consult = new HotConsult();
                            consult.setGjc(obj.optString("gjc"));
                            consult.setCreate_time(obj.optString("create_time"));
                            consult.setStatus(obj.optString("status"));
                            consult.setArticle_title(obj.optString("article_title"));
                            consult.setContent(obj.optString("content"));
                            consult.setFk_consult_user_id(obj.optString("fk_consult_user_id"));
                            consult.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            consult.setFk_expert_id(obj.optString("fk_expert_id"));
                            consult.setGg(obj.optString("gg"));
                            consult.setHitnum(obj.optString("hitnum"));
                            String id = obj.optString("id");
                            consult.setThe_id(id);
                            consult.setJz(obj.optString("jz"));
                            consult.setZy(obj.optString("zy"));
                            consult.setIshot(obj.optString("ishot"));
                            mList.add(consult);
                        }
                        consultAdapter.setHotList(mList);
                        mHandler.sendEmptyMessageAtTime(0, 1800);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    consultAdapter.setHotList(mList);
                    mHandler.sendEmptyMessageAtTime(0, 1800);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(HotConsultListActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                consultAdapter.setHotList(mList);
                mHandler.sendEmptyMessageAtTime(0, 1800);
            }
        });
    }

    static class MyHandler extends Handler {
        private WeakReference<PullToRefreshListView> refreshListViewWeakReference;

        public MyHandler(PullToRefreshListView refreshListView) {
            this.refreshListViewWeakReference = new WeakReference<>(refreshListView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PullToRefreshListView refreshListView = refreshListViewWeakReference.get();
            if (refreshListView != null) {
                switch (msg.what) {
                    case 0:
                        refreshListView.onRefreshComplete();
                        break;
                    default:
                        break;
                }
            }
        }
    }


}
