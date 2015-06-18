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

    public QQChatListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        return convertView;
    }

    class ViewHolder {
        CircleImageView exportHeader;
        TextView exportName;
        TextView msgTitle;
        TextView unReadMsgCount;
    }


}
