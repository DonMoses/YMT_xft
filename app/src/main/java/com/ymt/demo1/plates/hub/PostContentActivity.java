package com.ymt.demo1.plates.hub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.hub.HubPostContentAdapter;
import com.ymt.demo1.beams.hub.PostContent;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseFloatActivity;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/1
 */
public class PostContentActivity extends BaseFloatActivity {
    private RequestQueue mQueue;
    private List<PostContent> mPostContentList;
    private HubPostContentAdapter mPostContentAdapter;
    private PullToRefreshListView contentListView;
    private int tid;
    private int index;
    private String mAuthor;
    private String mSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        tid = getIntent().getIntExtra("tid", 0);
        mAuthor = getIntent().getStringExtra("author");
        mSubject = getIntent().getStringExtra("subject");
        index = 1;
//        Log.e("TAG", ">>>>>>>>>>>>>>>>threadId>>>>>>>>" + tid);
        mQueue = Volley.newRequestQueue(this);
        initTitle();
        initView();
        mQueue.add(getPostContent(tid, index));

    }

    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        myTitle.updateLeftLIcon2Txt("回帖");
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        myTitle.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                Intent intent = new Intent(PostContentActivity.this, PostReplyActivity.class);
                intent.putExtra("tid", tid);
                startActivityForResult(intent, 1);
            }

            @Override
            public void onRightRClick() {

            }
        });
    }

    protected void initView() {
        //标题信息
        TextView subjectInfo = (TextView) findViewById(R.id.subject_info);
        if ((!TextUtils.isEmpty(mAuthor)) && (!TextUtils.isEmpty(mSubject))) {
            subjectInfo.setText(mAuthor + "  " + mSubject);
        }

        mPostContentList = new ArrayList<>();
        mPostContentAdapter = new HubPostContentAdapter(this);
        contentListView = (PullToRefreshListView) findViewById(R.id.content_listView);
        contentListView.setAdapter(mPostContentAdapter);
        mPostContentAdapter.setList(mPostContentList);

        contentListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                index = 1;
                mPostContentList.clear();
                mPostContentAdapter.setList(mPostContentList);
                mQueue.add(getPostContent(tid, index));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                index++;
                mQueue.add(getPostContent(tid, index));
            }
        });

    }

    protected StringRequest getPostContent(int tid, int index) {
        return new StringRequest(BaseURLUtil.getPostContentUrl(tid, index), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>>>>>>response s>>>>>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
//                    if(jsonObject.optInt("retCode") == 0){
//
//                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        PostContent content = new PostContent();
                        JSONObject object = jsonArray.getJSONObject(i);
                        content.setAuthor(object.optString("author"));
                        content.setMessage(object.optString("message"));
                        content.setSubject(object.optString("subject"));
                        content.setTags(object.optString("tags"));
                        content.setDateline(object.optInt("dateline"));
                        mPostContentList.add(content);
                        //todo 刷新适配器

                        mPostContentAdapter.setList(mPostContentList);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                contentListView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                contentListView.onRefreshComplete();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    mPostContentList.clear();
                    mQueue.add(getPostContent(tid, index));
                }
                break;
        }
    }
}
