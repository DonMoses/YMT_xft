package com.ymt.demo1.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.SimpleMessage;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.beams.expert_consult.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/15
 */
public class ChatMessageListAdapter extends BaseAdapter {
    Chat chat = new Chat();
    private List<SimpleMessage> messages = new ArrayList<>();
    private static final int INFO_IN = 0;
    private static final int INFO_OUT = 1;
    LayoutInflater inflater;
    Context context;

    public ChatMessageListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        setMessages(chat.getMessages());
    }

    private void setMessages(List<SimpleMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getType().equals("IN")) {
            return INFO_IN;
        } else {
            return INFO_OUT;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InViewHolder inViewHolder = null;
        OutViewHolder outViewHolder = null;
        int infoType = getItemViewType(position);
        if (convertView == null) {
            switch (infoType) {
                case INFO_IN:
                    convertView = inflater.inflate(R.layout.layout_message_in, parent, false);
                    inViewHolder = new InViewHolder();
                    inViewHolder.exportHeader = (CircleImageView) convertView.findViewById(R.id.header_img);
                    inViewHolder.inContent = (TextView) convertView.findViewById(R.id.content_txt);
                    convertView.setTag(inViewHolder);
                    break;
                case INFO_OUT:
                    convertView = inflater.inflate(R.layout.layout_message_out, parent, false);
                    outViewHolder = new OutViewHolder();
                    outViewHolder.userHeader = (CircleImageView) convertView.findViewById(R.id.header_img);
                    outViewHolder.outContent = (TextView) convertView.findViewById(R.id.content_txt);
                    convertView.setTag(outViewHolder);
                    break;
                default:
                    break;
            }
        } else {
            switch (infoType) {
                case INFO_IN:
                    inViewHolder = (InViewHolder) convertView.getTag();
                    break;
                case INFO_OUT:
                    outViewHolder = (OutViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        //todo 这里简单模拟专家 、 用户信息。实际中使用Export 和Account 获取
        switch (infoType) {
            case INFO_IN:
                inViewHolder.exportHeader.setImageBitmap(
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.moses));
                inViewHolder.inContent.setText(messages.get(position).getMsgTxt());
                break;
            case INFO_OUT:
                outViewHolder.userHeader.setImageBitmap(
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.moses));
                outViewHolder.outContent.setText(messages.get(position).getMsgTxt());
                break;
            default:
                break;
        }

        return convertView;
    }

    class InViewHolder {
        CircleImageView exportHeader;
        TextView inContent;
    }

    class OutViewHolder {
        CircleImageView userHeader;
        TextView outContent;
    }
}
