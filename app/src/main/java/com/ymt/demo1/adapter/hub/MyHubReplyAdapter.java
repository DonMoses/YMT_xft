package com.ymt.demo1.adapter.hub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.hub.MyHubPost;
import com.ymt.demo1.beams.hub.MyHubReply;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class MyHubReplyAdapter extends BaseAdapter {
    List<MyHubReply> myHubReplies = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public MyHubReplyAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setSubjects(List<MyHubReply> myHubReplies) {
        this.myHubReplies = myHubReplies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myHubReplies.size();
    }

    @Override
    public Object getItem(int position) {
        return myHubReplies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_hub_reply, null);
            viewHolder = new MyViewHolder();
            viewHolder.reply_info = (TextView) convertView.findViewById(R.id.reply_content);
            viewHolder.subject = (TextView) convertView.findViewById(R.id.post_subject);
            viewHolder.post_plate_name = (TextView) convertView.findViewById(R.id.post_plate_name);
            viewHolder.create_time = (TextView) convertView.findViewById(R.id.create_time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.reply_info.setText("我的回复： " + myHubReplies.get(position).getSubject());
        viewHolder.subject.setText(myHubReplies.get(position).getSubject());
        viewHolder.post_plate_name.setText(myHubReplies.get(position).getName());
        viewHolder.create_time.setText(myHubReplies.get(position).getDateline());

        return convertView;
    }

    class MyViewHolder {
        TextView reply_info;
        TextView subject;
        TextView post_plate_name;
        TextView create_time;
    }
}
