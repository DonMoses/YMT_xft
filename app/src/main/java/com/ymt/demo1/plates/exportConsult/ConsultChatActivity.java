package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.ChatMessageListAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dan on 2015/5/14
 */
public class ConsultChatActivity extends BaseActivity {

    Expert expert;
    private RequestQueue requestQueue;
    private ChatMessageListAdapter messageListAdapter;
    private PullToRefreshListView infoListView;

    private String sessionId;
    private String qq_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_chat);
        Intent intent = getIntent();
        sessionId = intent.getStringExtra("session_id");
        qq_id = intent.getStringExtra("qq_id");
//        Log.e("TAG",">>>>>>>>>>>qq_id>>>>>>>>>>"+qq_id);
        requestQueue = Volley.newRequestQueue(this);
        messageListAdapter = new ChatMessageListAdapter(this);
        initTitle();
        initView();


    }

    protected void initTitle() {
        //todo 调用网络接口，获取聊天记录
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        expert = getIntent().getParcelableExtra("export_info");
        if (expert != null) {
            title.updateCenterTitle(expert.getUser_name());
        }

        title.updateLeftLIcon2Txt("关注");
        if (title.getChildCount() == 3) {
            title.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo 跳转到预约界面
                    Toast.makeText(ConsultChatActivity.this, "关注...", Toast.LENGTH_SHORT).show(); //打开预约界面
                }
            });
        }

        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                startActivity(new Intent(ConsultChatActivity.this, MyConsultActivity.class));
                finish();
            }
        });

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                //上面已经使用textView 替换布局中的ImageView
            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    /**
     * 初始化聊天界面
     */
    protected void initView() {
        //输入框
        final EditText inputView = (EditText) findViewById(R.id.edit_text_chat);
        //发送按钮
        Button sendInfoBtn = (Button) findViewById(R.id.button_send_info);
        sendInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendInfo = inputView.getText().toString();
                if (!TextUtils.isEmpty(sendInfo)) {

                    //todo exportID 和userId

                    volleyGet(sendInfo);

                    //todo 保存chat 到数据库

                    infoListView.getRefreshableView().setSelection(infoListView.getBottom());

                }
                inputView.setText("");
            }
        });
        //message ListView列表
        infoListView = (PullToRefreshListView) findViewById(R.id.list_view_chat);
        //适配器

        infoListView.setAdapter(messageListAdapter);
    }

    /**
     * volley队列
     */
    protected void volleyGet(String sendTxt) {
        requestQueue.add(doRequest(BaseURLUtil.sendQQMsgUrl(sessionId, "", sendTxt, qq_id)));
    }

    /**
     * volley请求  todo[考虑封装多种内容请求]
     */
    protected StringRequest doRequest(String url) {
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", "volley get >>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String inInfo = jsonObject.getString("text");

                    //todo 设置exportID 和userId


                    //todo 保存chat 到数据库

                    infoListView.getRefreshableView().setSelection(infoListView.getBottom());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.e("TAG", "volley get failed!");
            }
        });
        return stringRequest;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConsultChatActivity.this, MyConsultActivity.class));
        super.onBackPressed();
    }
}
