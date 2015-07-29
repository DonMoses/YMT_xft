package com.ymt.demo1.plates.eduPlane.studyDatum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.edu.StudyDatumAdapter;
import com.ymt.demo1.beams.edu.StudyDatumItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;
import com.ymt.demo1.main.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/9
 * 学习资料
 */
public class StudyDatumActivity extends BaseFloatActivity {

    private ArrayList<StudyDatumItem> datumItems;
    private RequestQueue mQueue;
    private StudyDatumAdapter studyDatumAdapter;
    private PullToRefreshListView pullToRefreshListView;
    private int start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        datumItems = new ArrayList<>();
        start = 1;
        setContentView(R.layout.activity_edu_study_datum);
        initTitle();
        initView();
        mQueue.add(getStudyDatum(start, ""));

    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //todo
                startActivity(new Intent(StudyDatumActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                //todo

            }
        });
    }

    protected void initView() {
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.studyDatum_list);
        studyDatumAdapter = new StudyDatumAdapter(this);
        pullToRefreshListView.setAdapter(studyDatumAdapter);
        pullToRefreshListView.setEmptyView(new ProgressBar(this));
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StudyDatumItem item = (StudyDatumItem) parent.getAdapter().getItem(position);
                Intent intent = new Intent(StudyDatumActivity.this, StudyItemDetailActivity.class);
                intent.putExtra("study", item);
                startActivity(intent);
            }
        });

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                datumItems.clear();
                studyDatumAdapter.setList(datumItems);
                mQueue.add(getStudyDatum(start, ""));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(getStudyDatum(start, ""));
            }
        });

    }


    protected StringRequest getStudyDatum(int start, String searchWhat) {
        return new StringRequest(BaseURLUtil.getStudyDatum(start, searchWhat), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject object = jsonObject.getJSONObject("datas");
                        JSONArray array = object.getJSONArray("listData");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            StudyDatumItem study = new StudyDatumItem();
                            study.setThe_id(obj.optString("id"));
                            study.setContent(obj.optString("content"));
                            study.setAuthor(obj.optString("author"));
                            study.setTime(obj.optString("time"));
                            study.setArticle_title(obj.optString("article_title"));
                            study.setLevel(obj.optString("level"));
                            study.setStatus(obj.optString("status"));
                            study.setSubject(obj.optString("subject"));
                            study.setCreate_time(obj.optString("create_time"));
                            study.setFk_create_user_id(obj.optString("fk_creat_user_id"));
                            study.setHitnum(obj.optString("hitnum"));
                            study.setPdf_id(BaseURLUtil.PDF_BASE + obj.optString("pdf_id"));
                            datumItems.add(study);
                        }
                        studyDatumAdapter.setList(datumItems);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pullToRefreshListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pullToRefreshListView.onRefreshComplete();
            }
        });
    }


}
