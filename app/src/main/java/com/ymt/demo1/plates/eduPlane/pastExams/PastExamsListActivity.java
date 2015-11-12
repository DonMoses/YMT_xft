
package com.ymt.demo1.plates.eduPlane.pastExams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.edu.PastExamsAdapter;
import com.ymt.demo1.beams.edu.PastExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/9
 * 历年真题界面
 */
public class PastExamsListActivity extends BaseFloatActivity {

    private PullToRefreshListView examsListView;
    private PastExamsAdapter adapter;
    private int start;
    private int pagesize;
    private RequestQueue mQueue;
    private List<PastExamItem> examList;
    private String orderYear;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = 1;
        pagesize = 10;
        mQueue = Volley.newRequestQueue(this);
        examList = new ArrayList<>();
        orderYear = getIntent().getStringExtra("year");
        level = getIntent().getStringExtra("level");
        if (!TextUtils.isEmpty(orderYear)) {
            examList.clear();
            if (!TextUtils.isEmpty(orderYear)) {
                examList.addAll(DataSupport.where("yuer = ?", orderYear).find(PastExamItem.class));
            } else {
                examList.addAll(DataSupport.findAll(PastExamItem.class));
            }
        }
        if (!TextUtils.isEmpty(level)) {
            examList.clear();
            examList.addAll(DataSupport.where("levelId = ?", level).find(PastExamItem.class));
        }

        setContentView(R.layout.activity_edu_over_exams);
        initTitle();
        initView();

        mQueue.add(getExamInfo(start, pagesize, level, orderYear, null));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        if (!TextUtils.isEmpty(level)) {
            switch (level) {
                case "1001":
                    title.updateCenterTitle("一级消防工程师真题");
                    break;
                case "1002":
                    title.updateCenterTitle("二级消防工程师真题");
                    break;
                case "1003":
                    title.updateCenterTitle("初级建(构)筑物消防员真题");
                    break;
                case "1004":
                    title.updateCenterTitle("中级建(构)筑物消防员真题");
                    break;
                default:
                    break;
            }
        }

        if (!TextUtils.isEmpty(orderYear)) {
            title.updateCenterTitle(orderYear + "年真题");
        }

        title.setOnLeftActionClickListener(
                new MyTitle.OnLeftActionClickListener() {
                    @Override
                    public void onClick() {
                        finish();
                    }
                }

        );

        title.setOnRightActionClickListener(
                new MyTitle.OnRightActionClickListener() {
                    @Override
                    public void onRightLClick() {

                    }

                    @Override
                    public void onRightRClick() {
                        startActivity(new Intent(PastExamsListActivity.this, FullSearchActivity.class));
                    }
                }

        );
    }

    protected void initView() {
        examsListView = (PullToRefreshListView) findViewById(R.id.past_exams_list_view);
        adapter = new PastExamsAdapter(this);
        examsListView.setAdapter(adapter);
        adapter.setList(examList);
        examsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PastExamItem examItem = (PastExamItem) parent.getAdapter().getItem(position);
                Intent intent = new Intent(PastExamsListActivity.this, PastExamDetailActivity.class);
                intent.putExtra("historyId", examItem.getHistoryId());
                startActivity(intent);
            }
        });

        examsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    start = 1;
                    examList.clear();
                    mQueue.add(getExamInfo(start, pagesize, level, orderYear, null));
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {

                    start++;
                    mQueue.add(getExamInfo(start, pagesize, level, orderYear, null));
                }
            }
        });

    }

    protected StringRequest getExamInfo(int index, int pageNum, final String level, String opTime, String searchWhat) {
        return new StringRequest(BaseURLUtil.getPastExamListByLevel(index, pageNum, level, opTime, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONArray array = object.getJSONObject("datas").getJSONObject("listData")
                                .getJSONArray("edu");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            PastExamItem exam = new PastExamItem();
                            exam.setTitle(obj.optString("title"));
                            exam.setLevel(obj.optString("level"));
                            exam.setSubjects(obj.optString("subjects"));
                            exam.setLevelId(obj.optInt("levelId"));
                            exam.setHistoryId(obj.optString("historyId"));
                            exam.setViews(obj.optInt("views"));
                            exam.setDATE(obj.optString("DATE"));
                            exam.setYuer(obj.optString("yuer"));
                            exam.setDescs(obj.optString("descs"));

                            if (DataSupport.where("historyId = ?", exam.getHistoryId()).find(PastExamItem.class)
                                    .size() == 0) {
                                exam.save();
                            } else {
                                exam.updateAll("historyId = ?", exam.getHistoryId());
                            }

                        }

                        if (!TextUtils.isEmpty(orderYear)) {
                            examList.clear();
                            examList.addAll(DataSupport.where("yuer = ?", orderYear).find(PastExamItem.class));
                        }
                        if (!TextUtils.isEmpty(level)) {
                            examList.clear();
                            examList.addAll(DataSupport.where("levelId = ?", level).find(PastExamItem.class));
                        }
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
                examsListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                examsListView.onRefreshComplete();
            }
        });

    }


}
