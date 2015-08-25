package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.launchpages.QQUnreadMsgService;
import com.ymt.demo1.utils.BaseURLUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class QQChatListAdapter extends BaseAdapter {
    List<QQChatInfo> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    SharedPreferences preferences;

    public QQChatListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.preferences = context.getSharedPreferences("unread_info", Context.MODE_PRIVATE);
    }

    public void setList(List<QQChatInfo> mList) {
        this.mList = mList;
        notifyDataSetChanged();
        Intent intent = new Intent(context, QQUnreadMsgService.class);
        context.startService(intent);
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
        Picasso.with(context).load(BaseURLUtil.BASE_URL + "/images/header.png").into(viewHolder.exportHeader);
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

}
