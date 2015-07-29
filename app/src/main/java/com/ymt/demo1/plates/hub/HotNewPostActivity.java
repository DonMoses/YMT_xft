package com.ymt.demo1.plates.hub;

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
import com.ymt.demo1.adapter.hub.Subject2Adapter;
import com.ymt.demo1.beams.hub.HubSubjectI;
import com.ymt.demo1.beams.hub.HubSubjectII;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/27
 */
public class HotNewPostActivity extends BaseFloatActivity {
    public static final String TYPE_HOT = "HOT";
    public static final String TYPE_NEW = "NEW";
    private RequestQueue mQueue;
    private Subject2Adapter subject2Adapter;
    private List<HubSubjectII> mSubjects = new ArrayList<>();
    private PullToRefreshListView subjectListView;
    private String subType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        subType = getIntent().getStringExtra("type");
        setContentView(R.layout.activity_hub_hot_new);
        initTitle();
        initView();
        mQueue.add(getHotPost());
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        switch (subType) {
            case TYPE_HOT:
                title.updateCenterTitle("热门主题");
                break;
            case TYPE_NEW:
                title.updateCenterTitle("最新回复");
                break;
        }
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    protected void initView() {
        subjectListView = (PullToRefreshListView) findViewById(R.id.subject_list_view);
        subject2Adapter = new Subject2Adapter(this);
        subjectListView.setAdapter(subject2Adapter);
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HotNewPostActivity.this, PostContentActivity.class);
                intent.putExtra("tid", ((HubSubjectII) (parent.getAdapter().getItem(position))).getTid());
                intent.putExtra("author", ((HubSubjectII) (parent.getAdapter().getItem(position))).getAuthor());
                intent.putExtra("subject", ((HubSubjectII) (parent.getAdapter().getItem(position))).getThreadSubject());
                startActivity(intent);
            }
        });

        subjectListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mSubjects.clear();
                mQueue.add(getHotPost());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    protected StringRequest getHotPost() {
        String url = null;
        switch (subType) {
            case TYPE_HOT:
                url = BaseURLUtil.HUB_HOT_URL;
                break;
            case TYPE_NEW:
                url = BaseURLUtil.HUB_NEW_URL;
                break;
        }
        return new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("retCode") == 0) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = array.getJSONObject(i);
                            HubSubjectII subject = new HubSubjectII();
                            subject.setThreadSubject(obj.optString("threadSubject"));
                            subject.setFid(obj.optInt("fid"));
                            subject.setAuthor(obj.optString("author"));
                            subject.setLastposter(obj.optString("lastposter"));
                            subject.setReplies(obj.optInt("replies"));
                            subject.setViews(obj.optInt("views"));
                            subject.setAuthorid(obj.optInt("authorid"));
                            subject.setPosttableid(obj.optInt("posttableid"));
                            subject.setName(obj.optString("name"));
                            subject.setLastpost(obj.optString("lastpost"));
                            subject.setDateline(obj.optString("dateline"));
                            subject.setTid(obj.optInt("tid"));
                            mSubjects.add(subject);
                        }
                        subject2Adapter.setSubjects(mSubjects);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                subjectListView.onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                subjectListView.onRefreshComplete();
            }
        });
    }

}
