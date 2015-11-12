package com.ymt.demo1.plates.eduPlane.studyDatum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
 * 学习资料
 */
public class StudyDatumActivity extends BaseFloatActivity {

    private List<StudyDatumItem> datumItems;
    private RequestQueue mQueue;
    private StudyDatumAdapter studyDatumAdapter;
    private PullToRefreshListView pullToRefreshListView;
    private int start;
    private int pageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        datumItems = new ArrayList<>();
        datumItems.addAll(DataSupport.findAll(StudyDatumItem.class));
        start = 1;
        pageNum = 10;
        setContentView(R.layout.activity_edu_study_datum);
        initTitle();
        initView();
        mQueue.add(getStudyDatum(start, pageNum));
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
                startActivity(new Intent(StudyDatumActivity.this, FullSearchActivity.class));
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
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        studyDatumAdapter.setList(datumItems);
        ProgressBar progressBar = new ProgressBar(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AppContext.screenWidth / 8, AppContext.screenWidth / 8);
        progressBar.setLayoutParams(layoutParams);
        pullToRefreshListView.setEmptyView(progressBar);

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
                if (AppContext.internetAvialable()) {
                    start = 1;
                    datumItems.clear();
                    mQueue.add(getStudyDatum(start, pageNum));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    start++;
                    mQueue.add(getStudyDatum(start, pageNum));
                }
            }
        });

    }

    protected StringRequest getStudyDatum(int index, int pageNum) {
        return new StringRequest(BaseURLUtil.getStudyDatum(index, pageNum), new Response.Listener<String>() {
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
                            study.setDownNum(obj.optInt("downNum"));
                            study.setTitle(obj.optString("title"));
                            study.setLevel(obj.optString("level"));
                            study.setSubjects(obj.optString("subjects"));
                            study.setLevelId(obj.optInt("levelId"));
                            study.setViews(obj.optInt("views"));
                            study.setHistoryId(obj.optString("historyId"));
                            study.setScore(obj.optInt("score"));
                            study.setReplays(obj.optInt("replays"));
                            study.setYuer(obj.optString("yuer"));
                            study.setDate(obj.optString("date"));
                            study.setDescs(obj.optString("descs"));

                            if (DataSupport.where("historyId = ?", study.getHistoryId()).find(StudyDatumItem.class).size() == 0) {
                                study.save();
                            } else {
                                study.updateAll("historyId = ?", study.getHistoryId());
                            }
                        }
                        datumItems.addAll(DataSupport.findAll(StudyDatumItem.class));
                        studyDatumAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
                pullToRefreshListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                pullToRefreshListView.onRefreshComplete();
            }
        });
    }


}
