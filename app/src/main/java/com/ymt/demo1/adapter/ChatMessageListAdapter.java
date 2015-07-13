package com.ymt.demo1.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/15
 */
public class ChatMessageListAdapter extends BaseAdapter {
    private List<QQMsg> messages = new ArrayList<>();
    private static final int INFO_IN = 0;
    private static final int INFO_OUT = 1;
    LayoutInflater inflater;
    Context context;

    public ChatMessageListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setMessages(List<QQMsg> messages) {
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
        if (messages.get(position).getReply_role().equals("001")) {
            return INFO_OUT;
        } else {
            return INFO_IN;
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
                Picasso.with(context).load(BaseURLUtil.BASE_URL + "/images/header.png").into(inViewHolder.exportHeader);
                inViewHolder.inContent.setText(messages.get(position).getContent());
                break;
            case INFO_OUT:
                outViewHolder.userHeader.setImageBitmap(AppContext.headerPic);
                outViewHolder.outContent.setText(messages.get(position).getContent());
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
