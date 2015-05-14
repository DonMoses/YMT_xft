package com.ymt.demo1.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.ChatBeam;
import com.ymt.demo1.beams.ChatMessage;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.plates.exportConsult.ExportInfoActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportChatAdapter extends BaseAdapter {
    ArrayList<ChatBeam> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public ExportChatAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<ChatBeam> mList) {
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
        //头像
        viewHolder.exportHeader.setImageBitmap(mList.get(position).getExport().getIcon());
        viewHolder.exportName.setText(mList.get(position).getExport().getName());
        //最近的文字信息
        ArrayList<ChatMessage> messages = mList.get(position).getMessages();
        int i = messages.size() - 1;
        while (i > 0) {
            if (messages.get(i).getMsgType() == ChatMessage.ChatMsgType.TXT) {
                // 如果最后一项是非文字内容，则移动到上一个，类推
                viewHolder.recentTxtMsg.setText(messages.get(i).getMsgTxt());
//                Log.e("TAG", "msg>>>>>" + messages.get(i).getMsgTxt());
                break;
            }
            i--;
        }
        //未读信息
        int count = 0;
        for (ChatMessage message : messages) {
            if (!message.isRead()) {
                count++;
            }
        }
        if (count > 0) {
            viewHolder.unReadMsgCount.setText(String.valueOf(count));
            viewHolder.unReadMsgCount.setAlpha(255);
        } else {
            viewHolder.unReadMsgCount.setAlpha(0);
        }

        viewHolder.exportHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExportInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("export_info", mList.get(position).getExport());
                intent.putExtra("export_info", bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder {
        CircleImageView exportHeader;
        TextView exportName;
        TextView recentTxtMsg;
        TextView unReadMsgCount;
    }
}
