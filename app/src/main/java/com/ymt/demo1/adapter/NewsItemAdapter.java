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

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/29
 */
public class NewsItemAdapter extends BaseAdapter {
    ArrayList<NewsItem> list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public NewsItemAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<NewsItem> list) {
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
            convertView = inflater.inflate(R.layout.layout_news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.commentBtn = (ImageButton) convertView.findViewById(R.id.comment_btn);
            viewHolder.commentedCount = (TextView) convertView.findViewById(R.id.comment_count);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleView.setText(list.get(position).getTitle());
        viewHolder.contentView.setText(list.get(position).getContentTxt());

        return convertView;
    }

    class ViewHolder {
        TextView titleView;
        TextView contentView;
        ImageButton commentBtn;
        TextView commentedCount;

    }
}
