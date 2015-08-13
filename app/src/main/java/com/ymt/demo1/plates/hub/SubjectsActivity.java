package com.ymt.demo1.plates.hub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.hub.Subject1Adapter;
import com.ymt.demo1.beams.hub.HubPlate;
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
 * Created by Dan on 2015/7/23
 */
public class SubjectsActivity extends BaseFloatActivity {

    private RequestQueue mQueue;
    private HubPlate plate;
    private List<HubSubjectI> subjects;
    private PullToRefreshListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        plate = getIntent().getParcelableExtra("plate");
        subjects = new ArrayList<>();
        setContentView(R.layout.activity_subject_list);
        initTitle();
        initView();
    }

    /**
     * 初始化title 和 action事件
     */
    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.updateCenterTitle(getIntent().getStringExtra("group_name") + " - " + plate.getName());     //设置title
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

    /**
     * 初始化控件（ListView）【关键字搜索结果列表】
     */
    private Subject1Adapter adapter;

    protected void initView() {
        listView = (PullToRefreshListView) findViewById(R.id.subject_list_view);

        //延时进度条
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        progressBar.setLayoutParams(params);
        listView.setEmptyView(progressBar);

        adapter = new Subject1Adapter(this);

        listView.setAdapter(adapter);
        adapter.setSubjects(subjects);

        //设置listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubjectsActivity.this, PostContentActivity.class);
                intent.putExtra("tid", Integer.valueOf(((HubSubjectI) (parent.getAdapter().getItem(position))).getThreadTid()));
                intent.putExtra("author", ((HubSubjectI) (parent.getAdapter().getItem(position))).getAuthor());
                intent.putExtra("subject", ((HubSubjectI) (parent.getAdapter().getItem(position))).getThreadSubject());
                startActivity(intent);
            }
        });

        mQueue.add(getSubjectByFid(plate.getFid()));

    }

    protected StringRequest getSubjectByFid(int fId) {
        return new StringRequest(BaseURLUtil.getSubjectsById(fId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("count") > 0) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        int length = array.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject object = array.getJSONObject(i);
                            HubSubjectI subject = new HubSubjectI();
                            subject.setAuthor(object.optString("author"));
                            subject.setAuthorid(object.optInt("authorid"));
                            subject.setDateline(object.optString("dateline"));
                            subject.setFavtimes(object.optInt("favtimes"));
                            subject.setForumFid(object.optString("forumFid"));
                            subject.setForumFup(object.optString("forumFup"));
                            subject.setForumName(object.optString("forumName"));
                            subject.setForumType(object.optString("forumType"));
                            subject.setLastpost(object.optString("lastpost"));
                            subject.setLastposter(object.optString("lastposter"));
                            subject.setRank(object.optInt("rank"));
                            subject.setReplies(object.optInt("replies"));
                            subject.setThreadFid(object.optString("threadFid"));
                            subject.setThreadSubject(object.optString("threadSubject"));
                            subject.setThreadTid(object.optString("threadTid"));
                            subject.setTodayPosts(object.optInt("todayposts"));
                            subject.setViews(object.optInt("views"));
                            subjects.add(subject);
                            adapter.setSubjects(subjects);
                        }

                    } else {
                        progressBar.setVisibility(View.GONE);
                        TextView textView = new TextView(SubjectsActivity.this);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT);
                        textView.setText("抱歉，没有找到主题！");
                        textView.setTextColor(Color.RED);
                        textView.setLayoutParams(params);
                        listView.setEmptyView(textView);
                    }
                } catch (JSONException e) {
                    Toast.makeText(SubjectsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(SubjectsActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
