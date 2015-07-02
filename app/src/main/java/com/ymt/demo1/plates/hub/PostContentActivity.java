package com.ymt.demo1.plates.hub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.HubPostContentAdapter;
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
    private int tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);
        tid = getIntent().getIntExtra("tid", 0);
//        Log.e("TAG", ">>>>>>>>>>>>>>>>threadId>>>>>>>>" + tid);
        mQueue = Volley.newRequestQueue(this);
        initTitle();
        initView();
        mQueue.add(getPostContent(tid));

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
        mPostContentList = new ArrayList<>();
        mPostContentAdapter = new HubPostContentAdapter(this);
        ListView contentListView = (ListView) findViewById(R.id.content_listView);
        contentListView.setAdapter(mPostContentAdapter);
        mPostContentAdapter.setList(mPostContentList);

    }

    protected StringRequest getPostContent(int tid) {
        return new StringRequest(BaseURLUtil.getPostContentUrl(tid), new Response.Listener<String>() {
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
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    mPostContentList.clear();
                    mQueue.add(getPostContent(tid));
                }
                break;
        }
    }
}
