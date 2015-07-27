package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.hub.HubSubjectI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class Subject1Adapter extends BaseAdapter {
    List<HubSubjectI> subjects = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public Subject1Adapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setSubjects(List<HubSubjectI> subjects) {
        this.subjects = subjects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hub_subject, null);
            viewHolder = new MyViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.subject_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.subject_content);
            viewHolder.author = (TextView) convertView.findViewById(R.id.subject_author);
            viewHolder.time = (TextView) convertView.findViewById(R.id.subject_time);
            viewHolder.followCount = (TextView) convertView.findViewById(R.id.subject_followed_count);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(subjects.get(position).getThreadSubject());
        viewHolder.content.setText(subjects.get(position).getThreadSubject());
        viewHolder.author.setText(subjects.get(position).getAuthor());
        viewHolder.time.setText(subjects.get(position).getDateline());
        viewHolder.followCount.setText(subjects.get(position).getReplies() + "回复");

        return convertView;
    }

    class MyViewHolder {
        TextView title;
        TextView content;
        TextView author;
        TextView time;
        TextView followCount;
    }
}
