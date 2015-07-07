package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.hub.PostContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/1
 */
public class HubPostContentAdapter extends BaseAdapter {
    private List<PostContent> mList = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public HubPostContentAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<PostContent> mList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hub_post_content, null);
            viewHolder = new ViewHolder();
            viewHolder.subject = (TextView) convertView.findViewById(R.id.content_subject);
            viewHolder.msg = (TextView) convertView.findViewById(R.id.content_msg);
            viewHolder.dateline = (TextView) convertView.findViewById(R.id.content_dateline);
            viewHolder.user = (TextView) convertView.findViewById(R.id.content_user);
            viewHolder.tags = (TextView) convertView.findViewById(R.id.content_tags);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PostContent content = mList.get(position);
        viewHolder.subject.setText(content.getSubject());
        viewHolder.msg.setText(content.getMessage());
        viewHolder.dateline.setText(String.valueOf(content.getDateline()));
        viewHolder.user.setText(content.getAuthor());
        viewHolder.tags.setText(content.getTags());
        return convertView;
    }

    class ViewHolder {
        TextView subject;
        TextView msg;
        TextView dateline;
        TextView user;
        TextView tags;

    }
}
