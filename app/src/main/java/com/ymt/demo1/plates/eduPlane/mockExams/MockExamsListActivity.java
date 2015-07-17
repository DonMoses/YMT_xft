
package com.ymt.demo1.plates.eduPlane.mockExams;

import android.content.Intent;
import android.os.Bundle;
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
import com.ymt.demo1.adapter.MockExamsAdapter;
import com.ymt.demo1.adapter.PastExamsAdapter;
import com.ymt.demo1.beams.edu.MockExamItem;
import com.ymt.demo1.beams.edu.PastExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.search.SearchActivity;
import com.ymt.demo1.plates.eduPlane.pastExams.PastExamDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/9
 * 模拟试题界面
 */
public class MockExamsListActivity extends BaseFloatActivity {

    private PullToRefreshListView examsListView;
    private MockExamsAdapter adapter;
    private int start;
    private RequestQueue mQueue;
    private List<MockExamItem> examList;
    private String searchKW;
    private int orderYear;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = 1;
        mQueue = Volley.newRequestQueue(this);
        examList = new ArrayList<>();
        searchKW = "";
        orderYear = getIntent().getIntExtra("year", 0);
        level = getIntent().getStringExtra("level");
        setContentView(R.layout.activity_edu_over_exams);
        initTitle();
        initView();

        mQueue.add(getExamInfo(start, level, orderYear, searchKW));
        start++;
        mQueue.add(getExamInfo(start, level, orderYear, searchKW));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        switch (level) {
            case "001":
                title.updateCenterTitle("一级消防工程师模拟题");
                break;
            case "002":
                title.updateCenterTitle("二级消防工程师模拟题");
                break;
            case "003":
                title.updateCenterTitle("初级消防工程师模拟题");
                break;
            case "004":
                title.updateCenterTitle("中级消防工程师模拟题");
                break;
            default:
                break;
        }

        if (orderYear != 0) {
            title.updateCenterTitle(String.valueOf(orderYear) + "年模拟题");
        }

        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {

            }

            @Override
            public void onRightRClick() {
                startActivity(new Intent(MockExamsListActivity.this, SearchActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    protected void initView() {
        examsListView = (PullToRefreshListView) findViewById(R.id.past_exams_list_view);
        adapter = new MockExamsAdapter(this);
        examsListView.setAdapter(adapter);
        adapter.setList(examList);
        examsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PastExamItem examItem = (PastExamItem) parent.getAdapter().getItem(position);
//                Intent intent = new Intent(MockExamsListActivity.this, PastExamDetailActivity.class);
//                intent.putExtra("exam", examItem);
//                startActivity(intent);
                //todo
            }
        });

        examsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                examList.clear();
//                adapter.setList(examList);
                mQueue.add(getExamInfo(start, level, orderYear, searchKW));
                start++;
                mQueue.add(getExamInfo(start, level, orderYear, searchKW));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(getExamInfo(start, level, orderYear, searchKW));
            }
        });

    }

    protected StringRequest getExamInfo(int start, String level, int dateYear, String searchWhat) {
        return new StringRequest(BaseURLUtil.getMockExams(start, level, dateYear, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONObject object1 = object.getJSONObject("datas");
                        JSONArray array = object1.getJSONArray("listData");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            MockExamItem exam = new MockExamItem();
                            exam.setThe_id(obj.optString("id"));
                            exam.setStatus(obj.optString("status"));
                            exam.setTotal_item(obj.optString("total_item"));
                            exam.setSubject(obj.optString("subject"));
                            exam.setTotal_score(obj.optString("total_score"));
                            exam.setTop_score(obj.optString("top_score"));
                            exam.setCreate_time(obj.optString("create_time"));
                            exam.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            exam.setExam_time(obj.optString("exam_time"));
                            exam.setType(obj.optString("type"));
                            exam.setExam_title(obj.optString("exam_title"));
                            examList.add(exam);

                        }
                        adapter.setList(examList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                examsListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                examsListView.onRefreshComplete();
            }
        });

    }


}
