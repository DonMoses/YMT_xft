package com.ymt.demo1.plates.exportConsult;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.ymt.demo1.beams.Export;
import com.ymt.demo1.dbBeams.SimpleMessage;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.dbBeams.Chat;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Dan on 2015/5/14
 */
public class ConsultChatActivity extends BaseActivity {

    Export export;
    private RequestQueue requestQueue;

    //todo 这里使用图灵机器人模拟聊天
    String apiUrl = "http://www.tuling123.com/openapi/api";
    String apiKey = "efa2c6f33c546dddec1fb917c8980a68";
    String baseUrl = apiUrl + "?key=" + apiKey + "&info=";      //请求格式 ： baseUrl+"info"
    private List<SimpleMessage> messages;
    private ChatMessageListAdapter messageListAdapter;
    private Chat chat;
    private PullToRefreshListView infoListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_chat);
        requestQueue = Volley.newRequestQueue(this);
        messageListAdapter = new ChatMessageListAdapter(this);
        initTitle();
        initView();
        //todo 从数据库读取chat
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Chat> chatList = DataSupport.findAll(Chat.class);
                if (chatList.size() > 0) {
                    chat = chatList.get(0);
                    messages = chat.getMessages(1);
                    messageListAdapter.setChat(chat);
                } else {
                    //todo 模拟数据
                    chat = new Chat();
                    messages = new ArrayList<>();
                }
            }
        }).start();

    }

    protected void initTitle() {
        //todo 调用网络接口，获取聊天记录
        export = getIntent().getBundleExtra("export_info").getParcelable("export_info");
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
        title.updateLeftLIcon2Txt("预约");
        title.updateCenterTitle(export.getName());
        if (title.getChildCount() == 3) {
            title.getChildAt(2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo 跳转到预约界面
                    Toast.makeText(ConsultChatActivity.this, "预约...", Toast.LENGTH_SHORT).show(); //打开预约界面
                }
            });
        }

        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
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
                    SimpleMessage message = new SimpleMessage();
                    message.setMsgTxt(sendInfo);
                    message.setType("OUT");
                    message.save();
                    messages.add(message);
                    //todo 这里没有设置exportID 和userId
                    chat.setMessages(messages);
                    messageListAdapter.setChat(chat);
                    volleyGet(sendInfo);

                    //todo 保存chat 到数据库
                    chat.save();
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
        requestQueue.add(doRequest(baseUrl + sendTxt));
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
                    SimpleMessage message = new SimpleMessage();
                    message.setMsgTxt(inInfo);
                    message.setType("IN");
                    message.save();
                    messages.add(message);
                    //todo 这里没有设置exportID 和userId
                    chat.setMessages(messages);
                    messageListAdapter.setChat(chat);

                    //todo 保存chat 到数据库
                    chat.save();
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
}
