
package com.ymt.demo1.plates.eduPlane.pastExams;

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
import com.ymt.demo1.adapter.PastExamsAdapter;
import com.ymt.demo1.beams.edu.PastExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private RequestQueue mQueue;
    private List<PastExamItem> examList;
    private String searchKW;
    private int orderYear;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start = 1;
        mQueue = Volley.newRequestQueue(this);
        examList = new ArrayList<>();
        searchKW = "";
        orderYear = getIntent().getIntExtra("year", 0);
        level = getIntent().getIntExtra("level", 0);
        setContentView(R.layout.activity_edu_over_exams);
        initTitle();
        initView();

        mQueue.add(getExamInfo(start, orderYear, searchKW));
        start++;
        mQueue.add(getExamInfo(start, orderYear, searchKW));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        if (level == 1) {
            title.updateCenterTitle("一级消防工程师真题");
        } else if (level == 2) {
            title.updateCenterTitle("二级消防工程师真题");
        } else if (level == 3) {
            title.updateCenterTitle("初级消防工程师真题");
        } else if (level == 4) {
            title.updateCenterTitle("中级消防工程师真题");
        }

        if (orderYear != 0) {
            title.updateCenterTitle(String.valueOf(orderYear) + "年真题");
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
                startActivity(new Intent(PastExamsListActivity.this, SearchActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                intent.putExtra("exam", examItem);
                startActivity(intent);
            }
        });

        examsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                examList.clear();
//                adapter.setList(examList);
                mQueue.add(getExamInfo(start, orderYear, searchKW));
                start++;
                mQueue.add(getExamInfo(start, orderYear, searchKW));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(getExamInfo(start, orderYear, searchKW));
            }
        });

    }

    protected StringRequest getExamInfo(int start, int dateYear, String searchWhat) {
        String url;
        switch (level) {
            case 1:
                url = BaseURLUtil.getPastExamsByLevel(start, "level001", searchWhat);
                break;
            case 2:
                url = BaseURLUtil.getPastExamsByLevel(start, "level002", searchWhat);
                break;
            case 3:
                url = BaseURLUtil.getPastExamsByLevel(start, "level003", searchWhat);
                break;
            case 4:
                url = BaseURLUtil.getPastExamsByLevel(start, "level004", searchWhat);
                break;
            default:
                url = BaseURLUtil.getPastExamsByYear(start, dateYear, searchWhat);
                break;
        }

        return new StringRequest(url, new Response.Listener<String>() {
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
                            PastExamItem exam = new PastExamItem();
                            exam.setExam_year(obj.optString("exam_year"));
                            exam.setThe_id(obj.optString("id"));
                            exam.setFiles(obj.optString("files"));
                            exam.setArticle_title(obj.optString("article_title"));
                            exam.setLevel(obj.optString("level"));
                            exam.setStatus(obj.optString("status"));
                            exam.setSubject(obj.optString("subject"));
                            exam.setMeta_keys(obj.optString("meta_keys"));
                            exam.setCreate_time(obj.optString("create_time"));
                            exam.setHitnum(obj.optString("hitnum"));
                            exam.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            exam.setPdf_id(obj.optString("pdf_id"));
                            exam.setDowncount(obj.optString("downcount"));
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
