package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.HotRecConsultAdapter;
import com.ymt.demo1.beams.expert_consult.ConsultInfo;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/5
 */
public class HotRecConsultListActivity extends BaseFloatActivity {
    private RequestQueue mRequestQueue;
    private String type;
    private int start;
    private int pageSize;
    private HotRecConsultAdapter consultAdapter;
    private List<ConsultInfo> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_consult_list);
        type = getIntent().getStringExtra("type");
        start = 1;
        pageSize = 20;
        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(hotRecConsultRequest(type, start, pageSize));
        initTitle();
        initView();
    }

    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        if (type.equals("hot")) {
            myTitle.updateCenterTitle("热点咨询");
        } else {
            myTitle.updateCenterTitle("最新咨询");
        }
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    protected void initView() {
        mList = new ArrayList<>();
        mList.addAll(DataSupport.where("type = ?", type).find(ConsultInfo.class));
        final PullToRefreshListView hotListView = (PullToRefreshListView) findViewById(R.id.hot_consult_list_view);
        consultAdapter = new HotRecConsultAdapter(this, HotRecConsultAdapter.CONSULT_LIST_TYPE);
        hotListView.setAdapter(consultAdapter);
        consultAdapter.setHotList(mList);

        hotListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    start = 1;
                    mList.clear();
                    DataSupport.deleteAll(ConsultInfo.class, "type = ?", type);
                    mRequestQueue.add(hotRecConsultRequest(type, start, pageSize));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    start = start + 1;
                    mRequestQueue.add(hotRecConsultRequest(type, start, pageSize));
                }
            }
        });

        hotListView.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) parent.getAdapter();
                HotRecConsultAdapter adapter = (HotRecConsultAdapter) headerViewListAdapter.getWrappedAdapter();
                ConsultInfo consult = (ConsultInfo) adapter.getItem(position - 1);
                Intent intent = new Intent(HotRecConsultListActivity.this, ConsultDetailActivity.class);
                intent.putExtra("cid", consult.getCid());
                startActivity(intent);
            }
        });
    }

    protected StringRequest hotRecConsultRequest(final String type, int start, int pageSize) {
        return new StringRequest(BaseURLUtil.getRecentHotConsultByPage(type, start, pageSize), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ConsultInfo consultInfo = new ConsultInfo();
                            consultInfo.setCid(obj.optInt("cid"));
                            consultInfo.setTitle(obj.optString("title"));
                            switch (type) {
                                case "hot":
                                    consultInfo.setType("hot");
                                    break;
                                case "new":
                                    consultInfo.setType("new");
                                    break;
                                default:
                                    break;
                            }

                            if (DataSupport.where("cid = ?", String.valueOf(consultInfo.getCid())).find(ConsultInfo.class).size() == 0) {
                                consultInfo.save();
                            } else {
                                consultInfo.updateAll("cid = ?", String.valueOf(consultInfo.getCid()));
                            }
                        }
                        mList.clear();
                        mList.addAll(DataSupport.where("type = ?", type).find(ConsultInfo.class));
                        consultAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });
    }


}
