package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.HeaderById;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.customViews.CircleImageView;

import org.litepal.crud.DataSupport;

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
        String role = messages.get(position).getReply_role();
        if (role.equals("002") || role.equals("001")) {
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
                    inViewHolder.name = (TextView) convertView.findViewById(R.id.in_name);
                    inViewHolder.reply = (TextView) convertView.findViewById(R.id.reply_time);
                    convertView.setTag(inViewHolder);
                    break;
                case INFO_OUT:
                    convertView = inflater.inflate(R.layout.layout_message_out, parent, false);
                    outViewHolder = new OutViewHolder();
                    outViewHolder.userHeader = (CircleImageView) convertView.findViewById(R.id.header_img);
                    outViewHolder.outContent = (TextView) convertView.findViewById(R.id.content_txt);
                    outViewHolder.name = (TextView) convertView.findViewById(R.id.out_name);
                    outViewHolder.reply = (TextView) convertView.findViewById(R.id.reply_time);
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
                List<HeaderById> list1 = DataSupport.where("the_id = ?", messages.get(position).getFk_reply_user_id()).find(HeaderById.class);

                if (list1.size() > 0) {
                    String headerIn = list1.get(0).getHeaderUrl();
                    Picasso.with(context).load(headerIn).into(inViewHolder.exportHeader);
                }
                inViewHolder.inContent.setText(messages.get(position).getContent());
                inViewHolder.name.setText(messages.get(position).getPro_expert_user_id());
                inViewHolder.reply.setText(messages.get(position).getReply_time());
                break;
            case INFO_OUT:
                List<HeaderById> list2 = DataSupport.where("the_id = ?", messages.get(position).getFk_reply_user_id()).find(HeaderById.class);

                if (list2.size() > 0) {
                    String headerOut = list2.get(0).getHeaderUrl();
                    Picasso.with(context).load(headerOut).into(outViewHolder.userHeader);
                }
                outViewHolder.outContent.setText(messages.get(position).getContent());
                outViewHolder.name.setText(messages.get(position).getPro_expert_user_id());
                outViewHolder.reply.setText(messages.get(position).getReply_time());
                break;
            default:
                break;
        }

        return convertView;
    }

    class InViewHolder {
        CircleImageView exportHeader;
        TextView inContent;
        TextView name;
        TextView reply;
    }

    class OutViewHolder {
        CircleImageView userHeader;
        TextView outContent;
        TextView name;
        TextView reply;
    }
}
