package com.ymt.demo1.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class QQChatListAdapter extends BaseAdapter {
    List<QQChatInfo> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
//    RequestQueue mQueue;
    SharedPreferences preferences;

    public QQChatListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
//        this.mQueue = Volley.newRequestQueue(context);
        this.preferences = context.getSharedPreferences("unread_info", Context.MODE_PRIVATE);
    }

    public void setList(List<QQChatInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
        int length = mList.size();
        for (int i = 0; i < length; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    unreadMsg(((QQChatInfo) getItem(finalI)).getQq_id());
                }
            }).start();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int unreadCount = preferences.getInt(mList.get(position).getQq_id(), 0);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_export_chat, null);
            viewHolder = new ViewHolder();
            viewHolder.exportHeader = (CircleImageView) convertView.findViewById(R.id.export_header);
            viewHolder.exportName = (TextView) convertView.findViewById(R.id.export_name);
            viewHolder.msgTitle = (TextView) convertView.findViewById(R.id.recent_message);
            viewHolder.unReadMsgCount = (TextView) convertView.findViewById(R.id.unread_message_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //todo 头像\名字
//        int exportId = Integer.valueOf(mList.get(position).getFk_pro_id());
//        viewHolder.exportHeader.setImageBitmap(null);
        viewHolder.exportName.setText(mList.get(position).getFk_pro_id());
        //todo chat的标题
        viewHolder.msgTitle.setText(mList.get(position).getMsg_title());
        if (unreadCount > 0) {
            viewHolder.unReadMsgCount.setVisibility(View.VISIBLE);
            viewHolder.unReadMsgCount.setText(String.valueOf(unreadCount));
        } else {
            viewHolder.unReadMsgCount.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        CircleImageView exportHeader;
        TextView exportName;
        TextView msgTitle;
        TextView unReadMsgCount;
    }


//    /**
//     * 一个QQ会话的所有未读消息
//     *
//     * @param qq_id ： QQ会话id
//     */
//    protected StringRequest sendUnreadQQMsgRequest(final String qq_id) {
//        final String url = BaseURLUtil.getMyUnreadQQMsgUrl(AppContext.now_session_id, qq_id, AppContext.now_user_id);
////        Log.e("TAG", " >>>>>>>>>>>>>> url>>>>>>>>>>" + url);
//        return new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
////                Log.e("TAG", " >>>>>>>>>>>>>> s>>>>>>>>>>" + s);
//                try {
//                    JSONObject jsonObject = new JSONObject(s);
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
//                    int unReadCount = jsonObject1.getInt("size");
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putInt(qq_id, unReadCount);
//                    editor.apply();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
////                Log.e("TAG", ">>>>>>>>>>>>.error>>>>>>>>>>>" + volleyError.toString());
//            }
//        });
//    }


    /**
     * 未读消息
     */
    protected void unreadMsg(String qq_id) {
        final String urlStr = BaseURLUtil.getMyUnreadQQMsgUrl(AppContext.now_session_id, qq_id, AppContext.now_user_id);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(300000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            InputStream ins = connection.getInputStream();
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            Log.e("TAG", " >>>>>>>>>>>>>> s>>>>>>>>>>" + response);

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

}
