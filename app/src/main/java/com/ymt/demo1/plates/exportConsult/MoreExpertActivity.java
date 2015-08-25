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
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class MoreExpertActivity extends BaseFloatActivity {
    //    private SearchViewUtil searchViewUtil;
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
        startIndex = 2;
        setContentView(R.layout.activity_more_expert);
//        searchViewUtil = new SearchViewUtil();
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
//        searchViewUtil.initSearchView(this);
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
                            expert.setCount(obj.optString("count"));
                            expert.setPro_life(obj.optString("pro_life"));
                            expert.setHead_pic(BaseURLUtil.BASE_URL + obj.optString("head_pic"));
                            expert.setTel(obj.optString("tel"));
                            expert.setFk_user_id(obj.optString("fk_user_id"));
                            expert.setAddr(obj.optString("addr"));
                            expert.setEducation(obj.optString("education"));
                            expert.setReporting_methods(obj.optString("reporting_methods"));
                            expert.setHome_zip_code(obj.optString("home_zip_code"));
                            expert.setPolitics(obj.optString("politics"));
                            expert.setQualification(obj.optString("qualification"));
                            expert.setLevel(obj.optString("level"));
                            expert.setCapacity(obj.optString("capacity"));
                            expert.setExperience(obj.optString("experience"));
                            expert.setIndustry(obj.optString("industry"));
                            expert.setNote(obj.optString("note"));
                            expert.setWork_addr(obj.optString("work_addr"));
                            expert.setOthers(obj.optString("others"));
                            expert.setId_number(obj.optString("id_number"));
                            expert.setHome_addr(obj.optString("home_addr"));
                            expert.setUser_name(obj.optString("user_name"));
                            expert.setSchool(obj.optString("school"));
                            expert.setDegree(obj.optString("degree"));
                            expert.setMajor_works(obj.optString("major_works"));
                            expert.setWork_zip_code(obj.optString("work_zip_code"));
                            expert.setCreate_time(obj.optString("create_time"));
                            expert.setPosition_title(obj.optString("position_title"));
                            expert.setWork_experience(obj.optString("work_experience"));
                            expert.setWork_name(obj.optString("work_name"));

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
