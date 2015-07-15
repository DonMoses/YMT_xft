package com.ymt.demo1.launchpages;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Dan on 2015/6/19
 */
public class QQMsgService extends IntentService {
    private Boolean isSignedIn = false;
    private SharedPreferences preferences;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isSignedIn) {
            List<QQChatInfo> chats = DataSupport.findAll(QQChatInfo.class);
            int length = chats.size();
            for (int i = 0; i < length; i++) {
                QQChatInfo info = chats.get(i);
                String qq_id = info.getQq_id();
                unreadMsg(qq_id);
//                sendQQChatMsgRequest(qq_id);
            }
        }
    }

    public QQMsgService() {
        super("QQMsgService");
        if (TextUtils.isEmpty(AppContext.now_session_id)) {
            isSignedIn = true;
            preferences = getSharedPreferences("unread_info", Context.MODE_PRIVATE);
        }
    }

    /**
     * 未读消息
     */
    protected void unreadMsg(String qq_id) {
        final String urlStr = BaseURLUtil.getMyUnreadQQMsgUrl(AppContext.now_session_id, qq_id, AppContext.now_user_id);
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(300000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            InputStream ins = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
//            Log.e("TAG", " >>>>>>>>>>>>>> s>>>>>>>>>>" + response);


            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                int unReadCount = jsonObject1.getInt("size");
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(qq_id, unReadCount);
                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("TAG", ">>>>>>>>>>>>.error>>>>>>>>>>>" + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 一个QQ会话的所有消息
     *
     * @param qq_id ： QQ会话id
     */
    protected void sendQQChatMsgRequest(final String qq_id) {
        final String urlStr = BaseURLUtil.getMyAllQQMsgUrl(AppContext.now_session_id, qq_id);
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(300000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            InputStream ins = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
//            Log.e("TAG", " >>>>>>>>>>>>>> s>>>>>>>>>>" + response);
            try {
                JSONObject jsonObject = new JSONObject(response.toString());
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("TAG", ">>>>>>>>>>>>.error>>>>>>>>>>>" + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }
}
