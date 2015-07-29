package com.ymt.demo1.adapter.news;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.main.StringUtils;

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
            convertView = inflater.inflate(R.layout.layout_news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.subject);
            viewHolder.picView = (ImageView) convertView.findViewById(R.id.pic);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.hitView = (TextView) convertView.findViewById(R.id.hit_num);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //todo

        viewHolder.titleView.setText(list.get(position).getArticle_title());
        viewHolder.contentView.setText(StringUtils.replaceBlank(Html.fromHtml(list.get(position).getContent()).toString()));
        viewHolder.hitView.setText(list.get(position).getHitnum() + "跟贴");
        String pic = list.get(position).getPic();
        if (!TextUtils.isEmpty(pic)) {
            viewHolder.picView.setVisibility(View.VISIBLE);
            Picasso.with(context).load(pic).into(viewHolder.picView);
        } else {
            viewHolder.picView.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView picView;
        TextView titleView;
        TextView contentView;
        TextView hitView;

    }
}
