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
import com.ymt.demo1.adapter.ExpertListAdapter;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.SearchViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class MoreExpertActivity extends BaseFloatActivity {
    private SearchViewUtil searchViewUtil;
    private RequestQueue mQueue;
    private ExpertListAdapter expertListAdapter;
    private PullToRefreshGridView expertGirdView;
    private int pageSize;
    private int startIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        pageSize = 6;
        startIndex = 1;
        setContentView(R.layout.activity_more_expert);
        searchViewUtil = new SearchViewUtil();
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
        searchViewUtil.initSearchView(this);
        expertGirdView = (PullToRefreshGridView) findViewById(R.id.expert_grid_view);

        expertListAdapter = new ExpertListAdapter(this, AppContext.screenWidth);
        expertGirdView.setAdapter(expertListAdapter);
        final List<Expert> experts = DataSupport.findAll(Expert.class);
        expertListAdapter.setExperts(experts);
        expertGirdView.onRefreshComplete();

        expertGirdView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                startIndex = 1;
                experts.clear();
                DataSupport.deleteAll(Expert.class);
                expertListAdapter.setExperts(experts);
                mQueue.add(getExperts(pageSize, startIndex, ""));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                startIndex++;
                mQueue.add(getExperts(pageSize, startIndex, ""));
            }
        });

        expertGirdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expert expert = (Expert) expertGirdView.getRefreshableView().getAdapter().getItem(position);
                Intent intent = new Intent(MoreExpertActivity.this, ExpertInfoActivity.class);
                intent.putExtra("expert_info", expert);
                startActivity(intent);
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
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Expert expert = new Expert();
                            String id = obj.optString("id");
                            expert.setThe_id(id);
                            expert.setCreate_time(obj.optString("create_time"));
                            expert.setBio(obj.optString("bio"));
                            expert.setCapacity(obj.optString("capacity"));
                            expert.setCount(obj.optString("count"));
                            expert.setExperience(obj.optString("experience"));
                            expert.setFk_user_id(obj.optString("fk_user_id"));
                            expert.setGood(obj.optString("good"));
                            expert.setHead_pic(obj.optString("head_pic"));
                            expert.setLevel(obj.optString("level"));
                            expert.setMajor_works(obj.optString("major_works"));
                            expert.setNote(obj.optString("note"));
                            expert.setRemark(obj.optString("remark"));
                            expert.setResume(obj.optString("resume"));
                            expert.setSocial_part_time(obj.optString("social_part_time"));
                            expert.setUser_name(obj.optString("user_name"));
                            expert.setWork_time(obj.optString("work_time"));

                            int size = DataSupport.where("the_id = ?", id).find(Expert.class).size();
                            if (size == 0) {
                                expert.save();
                            } else {
                                expert.updateAll("the_id = ?", id);
                            }
                        }

                        List<Expert> experts = DataSupport.findAll(Expert.class);
                        expertListAdapter.setExperts(experts);
                        expertGirdView.onRefreshComplete();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

}
