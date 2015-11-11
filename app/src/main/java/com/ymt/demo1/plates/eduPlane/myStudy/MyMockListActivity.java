
package com.ymt.demo1.plates.eduPlane.myStudy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.edu.MyMockAdapter;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.beams.edu.MockExamItem;
import com.ymt.demo1.beams.edu.MyExamsItem;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.sign.SignInFragment;
import com.ymt.demo1.plates.eduPlane.mockExams.ReadyActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/11/9
 * 我的学习界面
 */
public class MyMockListActivity extends BaseFloatActivity {
    private PullToRefreshListView examsListView;
    private MyMockAdapter adapter;
    private RequestQueue mQueue;
    private List<MyExamsItem> examList;
    private int index;
    private int pageNum;
    private MockExamItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        examList = new ArrayList<>();
        index = 1;
        pageNum = 10;
        setContentView(R.layout.activity_edu_over_exams);
        initTitle();
        initView();

        mQueue.add(getMyExamInfo(AppContext.now_session_id, index, pageNum));
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    protected void initView() {
        examsListView = (PullToRefreshListView) findViewById(R.id.past_exams_list_view);
        adapter = new MyMockAdapter(this);
        examsListView.setAdapter(adapter);
        ProgressBar progressBar = new ProgressBar(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AppContext.screenWidth / 8, AppContext.screenWidth / 8);
        progressBar.setLayoutParams(layoutParams);
        examsListView.setEmptyView(progressBar);
        adapter.setList(examList);
        examsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!TextUtils.isEmpty(AppContext.now_session_id)) {
                    MyExamsItem myExamsItem = (MyExamsItem) parent.getAdapter().getItem(position);
                    mQueue.add(getQuestionInfo(myExamsItem.getExaId(), myExamsItem.getTheId(), AppContext.now_session_id, 1, 1));

                    item = new MockExamItem();
                    item.setExaName(myExamsItem.getExaName());
                    item.setExaId(myExamsItem.getExaId());

                } else {
                    Toast.makeText(MyMockListActivity.this, "请登录!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyMockListActivity.this, SignInFragment.class));
                }

            }
        });

        examsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    examList.clear();
                    index = 1;
                    mQueue.add(getMyExamInfo(AppContext.now_session_id, index, pageNum));
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    index++;
                    mQueue.add(getMyExamInfo(AppContext.now_session_id, index, pageNum));
                }

            }
        });

    }

    protected StringRequest getMyExamInfo(String sId, int index, int pageNum) {

        return new StringRequest(BaseURLUtil.getMyMockExamList(sId, index, pageNum), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONArray array = object.getJSONObject("datas").getJSONArray("listData");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            MyExamsItem exam = new MyExamsItem();
                            exam.setTheId(obj.optString("id"));
                            exam.setScore(obj.optInt("score"));
                            exam.setOtime(obj.optString("otime"));
                            exam.setExaId(obj.optString("exaId"));
                            exam.setExaName(obj.optString("exaName"));
                            if (!examList.contains(exam)) {
                                examList.add(exam);
                            }
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

    //获得题目信息【//todo 因为我的学习列表中没有试题的题数，所以这里通过获取试题的接口来获得】
    private StringRequest getQuestionInfo(String examId, String userMockId, String sId, int index, int pageNum) {
        // TODO: 2015/11/5
        return new StringRequest(BaseURLUtil.getMockTTT(examId, userMockId, sId, index, pageNum), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        int bank_num = jsonObject.getJSONObject("datas").optInt("size");
                        Intent intent = new Intent(MyMockListActivity.this, ReadyActivity.class);
                        item.setBank_num(bank_num);//// TODO: 2015/11/10
                        intent.putExtra("item", item);      //跳转到答题界面
                        intent.putExtra("mockFrom", ReadyActivity.TYPE_MY_MOCK);
                        startActivity(intent);
                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadJson();
            }
        });
    }


}
