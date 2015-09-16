package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
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
import com.ymt.demo1.adapter.expertConsult.ChatMessageListAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.HeaderById;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
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
    private Expert expert;

    private String sessionId;
    private String qq_id;

    private MyHandler myHandler = new MyHandler(this);
    private ChatMsgRefreshThread refreshTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQQMsgs = new ArrayList<>();
        setContentView(R.layout.activity_consult_chat);
        Intent intent = getIntent();
        sessionId = intent.getStringExtra("session_id");
        qq_id = intent.getStringExtra("qq_id");
        expert = intent.getParcelableExtra("expert");
        mQQMsgs.addAll(DataSupport.where("fk_qq_id = ?", qq_id).find(QQMsg.class));
//        Log.e("TAG",">>>>>>>>>>>qq_id>>>>>>>>>>"+qq_id);
        requestQueue = Volley.newRequestQueue(this);
        messageListAdapter = new ChatMessageListAdapter(this);
        initTitle();
        initView();

        infoListView.getRefreshableView().setSelection(infoListView.getRefreshableView().getBottom());
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
                    follow(AppContext.now_session_id, expert.getFk_user_id());
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
//                    String info = URLEncoder.encode(sendInfo,"utf-8");
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

        //开启刷新线程
        refreshTread = new ChatMsgRefreshThread(this);
        refreshTread.start();

        infoListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        refreshTread.setStopState(true);
                        break;
//                    case MotionEvent.ACTION_UP:
//                        refreshTread.setStopState(false);
//                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        infoListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        refreshTread.setStopState(false);
                        break;
                    case SCROLL_STATE_FLING:
                    case SCROLL_STATE_TOUCH_SCROLL:
                        refreshTread.setStopState(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        requestQueue.add(getQQMsgs(qq_id));
    }

    /**
     * 发送消息
     */
    protected void sendMsg(String sendTxt) {
        requestQueue.add(doSend(sendTxt));
        infoListView.getRefreshableView().setSelection(infoListView.getBottom());
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
        refreshTread.setStopState(true);
        return new StringRequest(BaseURLUtil.getMyAllQQMsgUrl(AppContext.now_session_id, qq_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", "chat>>>>>>>>>>>>>>...s" + s);
                String ss = null;
                try {
                    ss = new String(s.getBytes(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject jsonObject = new JSONObject(ss);
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
//                        String content = URLEncoder.encode(obj.optString("content"), "utf-8");

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

                    List<QQMsg> msgs = DataSupport.where("fk_qq_id = ?", qq_id).find(QQMsg.class);
                    if (mQQMsgs != msgs) {
                        mQQMsgs.clear();
                        mQQMsgs.addAll(msgs);
                        messageListAdapter.setMessages(mQQMsgs);
                    }

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

                refreshTread.setStopState(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                refreshTread.setStopState(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ConsultChatActivity.this, MyConsultActivity.class));
        super.onBackPressed();
    }

    /**
     * 关注
     */
    private StringRequest follow(String sId, String expertId) {
        return new StringRequest(BaseURLUtil.followExpert(sId, expertId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        Toast.makeText(ConsultChatActivity.this, "已关注！", Toast.LENGTH_SHORT).show();
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

    protected void doRefresh() {
        requestQueue.add(getQQMsgs(qq_id));
        int size = mQQMsgs.size();
        for (int i = 0; i < size; i++) {
            requestQueue.add(getHeader(mQQMsgs.get(i).getFk_reply_user_id()));
        }
    }

    public static class MyHandler extends Handler {
        private WeakReference<ConsultChatActivity> reference;

        public MyHandler(ConsultChatActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ConsultChatActivity activity = reference.get();
            if (activity != null && (!activity.isFinishing())) {
                switch (msg.what) {
                    case 0:
                        activity.doRefresh();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 获取header头像
     */
    protected StringRequest getHeader(final String reply_user_id) {
        return new StringRequest(BaseURLUtil.getInfoById(reply_user_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONObject obj = object.getJSONObject("data");
                        HeaderById header = new HeaderById();
                        header.setThe_id(reply_user_id);
                        String pic = obj.optString("headPic");

                        if (!TextUtils.isEmpty(pic)) {
                            String hd = BaseURLUtil.BASE_URL + "/" + pic;
                            header.setHeaderUrl(hd);
                            int size = DataSupport.where("the_id = ? and headerUrl = ?", reply_user_id, hd).find(HeaderById.class).size();
                            if (size == 0) {
                                header.save();
//                            Log.e("TAG", ">>>>>>>>>>>>>>>save>>>>>>>>>" + qqMsg.getMsg_id());
                            } else {
                                header.updateAll("the_id = ?", reply_user_id);
//                            Log.e("TAG", ">>>>>>>>>>>>>>>update>>>>>>>>>" + qqMsg.getMsg_id());
                            }

                        }
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

    public static class ChatMsgRefreshThread extends Thread {
        private boolean stopKey;
        private WeakReference<ConsultChatActivity> reference;

        public ChatMsgRefreshThread(ConsultChatActivity activity) {
            reference = new WeakReference<>(activity);
            stopKey = false;
        }

        public void setStopState(boolean stopKey) {
            this.stopKey = stopKey;
        }

        @Override
        public void run() {
            ConsultChatActivity activity = reference.get();
            while (activity != null) {
                if (!this.stopKey) {
                    doRefresh(activity.myHandler);
                }

                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        private void doRefresh(ConsultChatActivity.MyHandler myHandler) {
            myHandler.sendEmptyMessage(0);
        }
    }


}