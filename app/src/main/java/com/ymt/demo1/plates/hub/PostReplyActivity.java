package com.ymt.demo1.plates.hub;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Dan on 2015/7/2
 */
public class PostReplyActivity extends BaseFloatActivity {
    private RequestQueue mQueue;
    private int tid;
    private EditText replyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reply);
        mQueue = Volley.newRequestQueue(this);
        tid = getIntent().getIntExtra("tid", 0);
        initTitle();
        initView();

    }

    protected void initTitle() {
        MyTitle myTitle = (MyTitle) findViewById(R.id.my_title);
        myTitle.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        myTitle.updateLeftLIcon2Txt("发表");
        myTitle.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        myTitle.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                String content = replyContent.getText().toString();
                String c = content.replaceAll(" ", "%20");
                if (TextUtils.isEmpty(c)) {
                    Toast.makeText(PostReplyActivity.this, "回帖内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    mQueue.add(getPostReply(tid, c, AppContext.now_user_name, 1));
                }
            }

            @Override
            public void onRightRClick() {

            }
        });
    }

    protected void initView() {
        replyContent = (EditText) findViewById(R.id.reply_content);
        Button summitReply = (Button) findViewById(R.id.summit_reply);
        summitReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = replyContent.getText().toString();
                String c = content.replaceAll(" ", "%20");
                if (TextUtils.isEmpty(c)) {
                    Toast.makeText(PostReplyActivity.this, "回帖内容不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    mQueue.add(getPostReply(tid, c, AppContext.now_user_name, 1));
                }
            }
        });
    }

    protected StringRequest getPostReply(int tid, String msg, String user, int reqType) {
        return new StringRequest(BaseURLUtil.getReplyPostUrl(tid, msg, user, reqType), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>>>>>>response s>>>>>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optInt("retCode") == 0 && jsonObject.optBoolean("data")) {
                        Toast.makeText(PostReplyActivity.this, "回帖成功！", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();

                    } else {
                        Toast.makeText(PostReplyActivity.this, "回帖失败，请稍后重试！", Toast.LENGTH_SHORT).show();
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
}
