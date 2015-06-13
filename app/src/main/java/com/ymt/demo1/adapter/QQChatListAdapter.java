package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class QQChatListAdapter extends BaseAdapter {
    List<QQChatInfo> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    private RequestQueue mQueue;

    public QQChatListAdapter(Context context, RequestQueue mQueue) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.mQueue = mQueue;
    }

    public void setList(List<QQChatInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_export_chat, null);
            viewHolder = new ViewHolder();
            viewHolder.exportHeader = (CircleImageView) convertView.findViewById(R.id.export_header);
            viewHolder.exportName = (TextView) convertView.findViewById(R.id.export_name);
            viewHolder.recentTxtMsg = (TextView) convertView.findViewById(R.id.recent_message);
            viewHolder.unReadMsgCount = (TextView) convertView.findViewById(R.id.unread_message_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //todo 头像\名字
//        int exportId = Integer.valueOf(mList.get(position).getFk_pro_id());
//        viewHolder.exportHeader.setImageBitmap(null);
        viewHolder.exportName.setText("expert " + mList.get(position).getFk_pro_id());
        //todo 最近的文字信息


        //todo 请求网络， 加载QQ消息到数据库
        mQueue.add(sendQQChatMsgRequest(mList.get(position).getQq_id(), viewHolder.recentTxtMsg));
        //todo 未读信息
        mQueue.add(sendUnreadQQMsgRequest(mList.get(position).getQq_id()));

        /*
        todo 点击跳转到会话界面
         */
//        viewHolder.exportHeader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ExportInfoActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("export_info", mList.get(position).getExport());
//                intent.putExtra("export_info", bundle);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        CircleImageView exportHeader;
        TextView exportName;
        TextView recentTxtMsg;
        TextView unReadMsgCount;
    }

    /**
     * 一个QQ会话的所有消息
     *
     * @param qq_id     ： QQ会话id
     * @param recentMsg : 显示最近消息的控件
     */
    protected StringRequest sendQQChatMsgRequest(String qq_id, final TextView recentMsg) {
        return new StringRequest(BaseURLUtil.getMyAllQQMsgUrl(AppContext.now_session_id, qq_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", "sendQQChatMsgRequest>>>>>>>>>>>>>" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                    JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                    int length = jsonArray.length();
                    //todo 获取所有/部分， 加入所有/部分到数据库
                    for (int i = 0; i < length; i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (i == 0) {
                            recentMsg.setText(obj.getString("content"));
                        }
                        QQMsg qqMsg = new QQMsg();
                        qqMsg.setId(obj.getString("id"));
                        qqMsg.setContent(obj.getString("content"));
                        qqMsg.setPro_expert_user_id(obj.getString("pro_expert_user_id"));
                        qqMsg.setStatus(obj.getString("status"));
                        qqMsg.setFk_reply_user_id(obj.getString("fk_reply_user_id"));
                        qqMsg.setReply_time(obj.getString("reply_time"));
                        qqMsg.setReply_role(obj.getString("reply_role"));
                        qqMsg.setType(obj.getString("type"));
                        qqMsg.setReply_user_name(obj.getString("reply_user_name"));
                        qqMsg.setFk_qq_id(obj.getString("fk_qq_id"));
                        qqMsg.save();

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

    /**
     * 一个QQ会话的所有消息
     *
     * @param qq_id ： QQ会话id
     */
    protected StringRequest sendUnreadQQMsgRequest(String qq_id) {
        return new StringRequest(BaseURLUtil.getMyUnreadQQMsgUrl(AppContext.now_session_id, qq_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                    int unReadCount = jsonObject1.getInt("size");
//                    Log.e("TAG", "unReadCount>>>>>>>>>>>>>" + s);
//                    if (unReadCount > 0) {
//                        get.setText(String.valueOf(unReadCount));
//                        unReadText.setAlpha(255);
//                    } else {
//                        unReadText.setAlpha(0);
//                    }
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
