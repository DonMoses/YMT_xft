package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.ExpertListAdapter;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.expert_consult.PreExpert;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class MoreExpertActivity extends BaseFloatActivity {
    private RequestQueue mQueue;
    private ExpertListAdapter expertListAdapter;
    private PullToRefreshGridView expertGirdView;
    private int pageSize;
    private int startIndex;
    private List<PreExpert> experts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);

        experts = new ArrayList<>();
        experts.addAll(DataSupport.findAll(PreExpert.class));
        pageSize = 6;
        startIndex = 2;
        setContentView(R.layout.activity_more_expert);
        mQueue.add(getExperts(pageSize, startIndex, ""));

        initTitle();
        initView();

    }

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

    protected void initView() {
        expertGirdView = (PullToRefreshGridView) findViewById(R.id.expert_grid_view);
        expertListAdapter = new ExpertListAdapter(this, AppContext.screenWidth);
        expertGirdView.setAdapter(expertListAdapter);
        expertListAdapter.setExperts(experts);
        expertGirdView.onRefreshComplete();

        expertGirdView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (AppContext.internetAvialable()) {
                    startIndex = 1;
                    experts.clear();
                    DataSupport.deleteAll(Expert.class);
                    expertListAdapter.setExperts(experts);
                    mQueue.add(getExperts(pageSize, startIndex, ""));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                if (AppContext.internetAvialable()) {
                    startIndex++;
                    mQueue.add(getExperts(pageSize, startIndex, ""));
                }
            }
        });

        expertGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PreExpert expert = (PreExpert) expertGirdView.getRefreshableView().getAdapter().getItem(position);
                Intent intent2 = new Intent(MoreExpertActivity.this, ExpertInfoActivity.class);
                intent2.putExtra("id", expert.getFkUserId());
                startActivity(intent2);
            }
        });
    }

    protected StringRequest getExperts(int pageSize, int start, String searchWho) {
        return new StringRequest(BaseURLUtil.doGetExpertList(pageSize, start, searchWho), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            PreExpert preExpert = new PreExpert();
                            preExpert.setCount(obj.optInt("count"));
                            preExpert.setUsername(obj.optString("username"));
                            preExpert.setLevel(obj.optString("level"));
                            preExpert.setFkUserId(obj.optInt("fkUserId"));
                            preExpert.setWaitCount(obj.optInt("waitCount"));
                            preExpert.setHeadImage(obj.optString("headImage"));
                            int size = DataSupport.where("fkUserId = ?", String.valueOf(preExpert.getFkUserId())).find(PreExpert.class).size();
                            if (size == 0) {
                                preExpert.save();
                            } else {
                                preExpert.updateAll("fkUserId = ?", String.valueOf(preExpert.getFkUserId()));
                            }
                        }

                        experts.clear();
                        experts.addAll(DataSupport.findAll(PreExpert.class));
                        expertListAdapter.notifyDataSetChanged();
                        expertGirdView.onRefreshComplete();
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
