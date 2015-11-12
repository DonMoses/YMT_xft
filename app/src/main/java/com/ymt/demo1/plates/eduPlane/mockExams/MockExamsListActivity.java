
package com.ymt.demo1.plates.eduPlane.mockExams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
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
import com.ymt.demo1.adapter.edu.MockExamsAdapter;
import com.ymt.demo1.beams.edu.MockExamItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.main.sign.SignInFragment;

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
    public static final String MOCK_EXAM_LEVEL_1 = "一级注册消防工程师";
    public static final String MOCK_EXAM_LEVEL_2 = "二级注册消防工程师";
    public static final String MOCK_EXAM_LEVEL_3 = "初级注册消防工程师";
    public static final String MOCK_EXAM_LEVEL_4 = "中级注册消防工程师";
    private PullToRefreshListView examsListView;
    private MockExamsAdapter adapter;
    private RequestQueue mQueue;
    private List<MockExamItem> examList;
    private String level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        examList = new ArrayList<>();
        level = getIntent().getStringExtra("level");
        setContentView(R.layout.activity_edu_over_exams);
        initTitle();
        initView();

        mQueue.add(getExamInfo(level));
    }

    protected void initTitle() {
        MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        switch (level) {
            case "1001":
                title.updateCenterTitle(MOCK_EXAM_LEVEL_1);
                break;
            case "1002":
                title.updateCenterTitle(MOCK_EXAM_LEVEL_2);
                break;
            case "1003":
                title.updateCenterTitle(MOCK_EXAM_LEVEL_3);
                break;
            case "1004":
                title.updateCenterTitle(MOCK_EXAM_LEVEL_4);
                break;
            default:
                break;
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
                startActivity(new Intent(MockExamsListActivity.this, FullSearchActivity.class));
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
                if (!TextUtils.isEmpty(AppContext.now_session_id)) {
                    Intent intent = new Intent(MockExamsListActivity.this, ReadyActivity.class);
                    MockExamItem item = (MockExamItem) parent.getAdapter().getItem(position);
                    intent.putExtra("item", item);      //跳转到答题界面
                    intent.putExtra("mockFrom",ReadyActivity.TYPE_NEW_MOCK);
                    startActivity(intent);
                } else {
                    Toast.makeText(MockExamsListActivity.this, "请登录!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MockExamsListActivity.this, SignInFragment.class));
                }

            }
        });

        examsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                examList.clear();
                mQueue.add(getExamInfo(level));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mQueue.add(getExamInfo(level));
            }
        });

    }

    protected StringRequest getExamInfo(String level) {

        return new StringRequest(BaseURLUtil.getMockExamList(level), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONArray array = object.getJSONObject("datas").getJSONArray("listData");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            MockExamItem exam = new MockExamItem();
                            exam.setBank_num(obj.optInt("bank_num"));
                            exam.setUids(obj.optInt("uids"));
                            exam.setScores(obj.optInt("scores"));
                            exam.setTimes(obj.optString("times"));
                            exam.setExaId(obj.optString("exaId"));
                            exam.setExaName(obj.optString("exaName"));
                            examList.add(exam);

                        }
                        adapter.setList(examList);
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
