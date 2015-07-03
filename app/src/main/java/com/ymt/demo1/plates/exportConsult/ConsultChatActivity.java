package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ymt.demo1.adapter.ChatMessageListAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/14
 */
public class ConsultChatActivity extends BaseActivity {

    private RequestQueue requestQueue;
    private ChatMessageListAdapter messageListAdapter;
    private PullToRefreshListView infoListView;
    private List<QQMsg> mQQMsgs;

    private String sessionId;
    private String qq_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQQMsgs = new ArrayList<>();
        setContentView(R.layout.activity_consult_chat);
        Intent intent = getIntent();
        sessionId = intent.getStringExtra("session_id");
        qq_id = intent.getStringExtra("qq_id");
        mQQMsgs.addAll(DataSupport.where("fk_qq_id = ?", qq_id).find(QQMsg.class));
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
        String titleStr = getIntent().getStringExtra("title");
        if (titleStr != null) {
            title.updateCenterTitle(titleStr);
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
                    String info = sendInfo.replaceAll(" ", "%20");
                    //todo exportID 和userId
                    sendMsg(info);
                    //todo 保存chat 到数据库
//                    Toast.makeText(ConsultChatActivity.this, "send >>> " + sendInfo, Toast.LENGTH_SHORT).show();
                    infoListView.getRefreshableView().setSelection(infoListView.getBottom());
                }
                inputView.setText("");
            }
        });
        //message ListView列表
        infoListView = (PullToRefreshListView) findViewById(R.id.list_view_chat);
        //适配器
        infoListView.setAdapter(messageListAdapter);
        messageListAdapter.setMessages(mQQMsgs);
        infoListView.getRefreshableView().setSelection(infoListView.getBottom());

        /*
        todo 刷新消息数据
         */
        infoListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestQueue.add(getQQMsgs(qq_id));
                infoListView.getRefreshableView().setSelection(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                requestQueue.add(getQQMsgs(qq_id));
                infoListView.getRefreshableView().setSelection(infoListView.getBottom());
            }
        });

        requestQueue.add(getQQMsgs(qq_id));
    }

    /**
     * 发送消息
     */
    protected void sendMsg(String sendTxt) {
        requestQueue.add(doSend(sendTxt));
    }

    protected StringRequest doSend(final String sendTxt) {
        return new StringRequest(BaseURLUtil.sendQQMsgUrl(sessionId, sendTxt, qq_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", "volley get >>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        requestQueue.add(getQQMsgs(qq_id));
                    }

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
    }

    /**
     * 一个QQ会话的所有消息
     *
     * @param qq_id ： QQ会话id
     */
    protected StringRequest getQQMsgs(final String qq_id) {
        return new StringRequest(BaseURLUtil.getMyAllQQMsgUrl(AppContext.now_session_id, qq_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", "chat>>>>>>>>>>>>>>...s" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        Intent intent = new Intent();
                        intent.putExtra("unread_count", 0);
                        setResult(1, intent);
                    }
                    JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                    JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                    int length = jsonArray.length();
                    //todo 获取所有/部分， 加入所有/部分到数据库
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String content = obj.optString("content");

                        QQMsg qqMsg = new QQMsg();
                        qqMsg.setContent(content);
                        qqMsg.setPro_expert_user_id(obj.optString("pro_expert_user_id"));
                        qqMsg.setStatus(obj.optString("status"));
                        qqMsg.setFk_reply_user_id(obj.optString("fk_reply_user_id"));
                        qqMsg.setReply_time(obj.optString("reply_time"));
                        qqMsg.setReply_role(obj.optString("reply_role"));
                        qqMsg.setType(obj.optString("type"));
                        qqMsg.setReply_user_name(obj.optString("reply_user_name"));
                        String msg_id = obj.optString("id");
                        qqMsg.setMsg_id(msg_id);
                        qqMsg.setFk_qq_id(obj.optString("fk_qq_id"));
                        int size = DataSupport.where("msg_id = ?", msg_id).find(QQMsg.class).size();
                        if (size == 0) {
                            qqMsg.save();
//                            Log.e("TAG", ">>>>>>>>>>>>>>>save>>>>>>>>>" + qqMsg.getMsg_id());
                        } else {
                            qqMsg.updateAll("msg_id = ?", msg_id);
//                            Log.e("TAG", ">>>>>>>>>>>>>>>update>>>>>>>>>" + qqMsg.getMsg_id());
                        }
                    }

                    mQQMsgs = DataSupport.where("fk_qq_id = ?", qq_id).find(QQMsg.class);
                    messageListAdapter.setMessages(mQQMsgs);
                    infoListView.getRefreshableView().setSelection(infoListView.getBottom());
                    infoListView.onRefreshComplete();

                    /*
                    清空未读记录
                     */
                    SharedPreferences sharedPreferences = ConsultChatActivity.this.getSharedPreferences("unread_info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(qq_id, 0);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                infoListView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConsultChatActivity.this, MyConsultActivity.class));
        super.onBackPressed();
    }


}
