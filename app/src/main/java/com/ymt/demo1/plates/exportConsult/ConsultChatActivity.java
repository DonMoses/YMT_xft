package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.ChatMessageListAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 2015/5/14
 */
public class ConsultChatActivity extends BaseActivity {

    private RequestQueue requestQueue;
    private ChatMessageListAdapter messageListAdapter;
    private PullToRefreshListView infoListView;
    private List<QQMsg> mQQMsgs;

    private int cId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQQMsgs = new ArrayList<>();
        setContentView(R.layout.activity_consult_chat);
        cId = getIntent().getIntExtra("cId", 0);
        mQQMsgs.addAll(DataSupport.where("cId = ?", String.valueOf(cId)).find(QQMsg.class));
        requestQueue = Volley.newRequestQueue(this);
        messageListAdapter = new ChatMessageListAdapter(this);
        initTitle();
        initView();
    }

    protected void initTitle() {
        //todo 调用网络接口，获取聊天记录
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        String titleStr = getIntent().getStringExtra("title");
        if (titleStr != null) {
            title.updateCenterTitle(titleStr);
        }
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
                    requestQueue.add(doSend(sendInfo, cId, AppContext.now_user_id));
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

        requestQueue.add(getQQMsgs(cId));
    }

    protected StringRequest doSend(String msg, final int cId, final int userId) {
        try {
            msg = URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new StringRequest(Request.Method.GET, BaseURLUtil.sendQQMsgUrl(msg, cId, userId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getJSONObject("datas").optInt("listData") > 0) {
                        //// TODO: 2015/11/4  发送消息成功，刷新页面
                        requestQueue.add(getQQMsgs(cId));
                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppContext.toastBadInternet();
            }
        }) {//设置get请求的头，编码格式也为UTF-8
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Charset", "utf-8");
                headers.put("Content-Type", "application/x-javascript");
                headers.put("Accept-Encoding", "gzip,deflate");
                return headers;
            }
        };

    }

    /**
     * 一个QQ会话的所有消息
     */
    protected StringRequest getQQMsgs(final int cId) {
        return new StringRequest(BaseURLUtil.getMyAllQQMsgUrl(cId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    int length = jsonArray.length();
                    //todo 获取所有/部分， 加入所有/部分到数据库
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        QQMsg qqMsg = new QQMsg();
                        qqMsg.setHeadImage(obj.optString("headImage"));
                        qqMsg.setUserName(obj.optString("userName"));
                        qqMsg.setcId(obj.optInt("cid"));
                        qqMsg.setCmdType(obj.optString("cmdType"));
                        qqMsg.setMsg(obj.optString("msg"));
                        qqMsg.setName(obj.optString("name"));
                        qqMsg.setOpTime(obj.optString("opTime"));
                        qqMsg.setTheId(obj.optInt("id"));
                        qqMsg.setTitle(obj.optString("title"));
                        qqMsg.setUserId(obj.optInt("userId"));
                        qqMsg.setUserType(obj.optString("userType"));
                        int size = DataSupport.where("theId = ?", String.valueOf(qqMsg.getTheId())).find(QQMsg.class).size();
                        if (size == 0) {
                            qqMsg.save();
                        } else {
                            qqMsg.updateAll("theId = ?", String.valueOf(qqMsg.getTheId()));
                        }
                    }

                    List<QQMsg> msgs = DataSupport.where("cId = ?", String.valueOf(cId)).find(QQMsg.class);
                    if (mQQMsgs != msgs) {
                        mQQMsgs.clear();
                        mQQMsgs.addAll(msgs);
                        messageListAdapter.notifyDataSetChanged();
                        infoListView.getRefreshableView().setSelection(infoListView.getBottom());
                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConsultChatActivity.this, MyConsultActivity.class));
        super.onBackPressed();
    }
}