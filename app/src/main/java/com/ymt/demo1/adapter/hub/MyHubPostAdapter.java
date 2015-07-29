package com.ymt.demo1.adapter.hub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.hub.MyHubPost;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class MyHubPostAdapter extends BaseAdapter {
    List<MyHubPost> myHubPosts = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public MyHubPostAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setSubjects(List<MyHubPost> myHubPosts) {
        this.myHubPosts = myHubPosts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myHubPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return myHubPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_hub_post, null);
            viewHolder = new MyViewHolder();
            viewHolder.post_subject = (TextView) convertView.findViewById(R.id.post_subject);
            viewHolder.post_plate_name = (TextView) convertView.findViewById(R.id.post_plate_name);
            viewHolder.post_info = (TextView) convertView.findViewById(R.id.post_info);
            viewHolder.last_post_info = (TextView) convertView.findViewById(R.id.last_post_info);
            viewHolder.view_count = (TextView) convertView.findViewById(R.id.view_count);
            viewHolder.reply_count = (TextView) convertView.findViewById(R.id.reply_count);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.post_subject.setText(myHubPosts.get(position).getSubject());
        viewHolder.post_plate_name.setText(myHubPosts.get(position).getName());
        viewHolder.post_info.setText(myHubPosts.get(position).getAuthor() + " 发表于 " + myHubPosts.get(position).getDateline());
        viewHolder.last_post_info.setText("最新回复： " + myHubPosts.get(position).getLastposter() + " 在" + myHubPosts.get(position).getLastpost());
        viewHolder.view_count.setText(myHubPosts.get(position).getViews() + "跟帖");
        viewHolder.reply_count.setText(myHubPosts.get(position).getReplies() + "回复");

        return convertView;
    }

    class MyViewHolder {
        TextView post_subject;
        TextView post_plate_name;
        TextView post_info;
        TextView last_post_info;
        TextView view_count;
        TextView reply_count;
    }
}
