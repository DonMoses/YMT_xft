package com.ymt.demo1.launchpages;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

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
public class QQUnreadMsgService extends IntentService {
    private Boolean isSignedIn = false;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isSignedIn) {
            List<QQChatInfo> chats = DataSupport.findAll(QQChatInfo.class);
            int length = chats.size();
            for (int i = 0; i < length; i++) {
                QQChatInfo info = chats.get(i);
                int qq_id = info.getConsultId();
                unreadMsg(qq_id);
//                sendQQChatMsgRequest(qq_id);
            }
        }
    }

    public QQUnreadMsgService() {
        super("QQMsgService");
        if (TextUtils.isEmpty(AppContext.now_session_id)) {
            isSignedIn = true;
        }
    }

    /**
     * 未读消息
     */
    protected void unreadMsg(int qq_id) {
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
               //// TODO: 2015/11/4  未读消息
            } catch (JSONException e) {
                AppContext.toastBadJson();
            }
        } catch (IOException e) {
            e.printStackTrace();
            AppContext.toastBadInternet();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


}
