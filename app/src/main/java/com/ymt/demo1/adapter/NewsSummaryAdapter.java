package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.NewsItem;
import com.ymt.demo1.beams.NewsSummary;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/29
 */
public class NewsSummaryAdapter extends BaseAdapter {
    ArrayList<NewsSummary> list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public NewsSummaryAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<NewsSummary> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_knowledge_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.icon_label);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.collectBtn = (ImageButton) convertView.findViewById(R.id.collect_btn);
            viewHolder.commentBtn = (ImageButton) convertView.findViewById(R.id.comment_btn);
            viewHolder.collectedCount = (TextView) convertView.findViewById(R.id.collected_count);
            viewHolder.commentedCount = (TextView) convertView.findViewById(R.id.comment_count);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //todo

        viewHolder.titleView.setText(list.get(position).getArticle_title());
        viewHolder.contentView.setText(list.get(position).getCreate_time());

        return convertView;
    }

    class ViewHolder {
        TextView titleView;
        ImageView iconView;
        TextView contentView;
        ImageButton collectBtn;
        ImageButton commentBtn;
        TextView collectedCount;
        TextView commentedCount;

    }
}
