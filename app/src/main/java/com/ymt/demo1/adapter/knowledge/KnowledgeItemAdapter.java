package com.ymt.demo1.adapter.knowledge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.knowledge.KnowledgeItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItemAdapter extends BaseAdapter {
    List<KnowledgeItem> knowledgeItemList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public KnowledgeItemAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setKnowledgeItemList(List<KnowledgeItem> knowledgeItemList) {
        this.knowledgeItemList = knowledgeItemList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return knowledgeItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return knowledgeItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_knowledge_item, null);
            myViewHolder = new MyViewHolder();
            myViewHolder.titleView = (TextView) convertView.findViewById(R.id.subject);
            myViewHolder.updateTimeView = (TextView) convertView.findViewById(R.id.create_time);
            myViewHolder.downloadCount = (TextView) convertView.findViewById(R.id.download_count);
            myViewHolder.viewCount = (TextView) convertView.findViewById(R.id.view_count);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (MyViewHolder) convertView.getTag();
        }

        KnowledgeItem knowledgeItem = knowledgeItemList.get(position);

        myViewHolder.titleView.setText(knowledgeItem.getDocTitle());
        myViewHolder.updateTimeView.setText(knowledgeItem.getUpDateTime());
        myViewHolder.viewCount.setText(String.valueOf(knowledgeItem.getReadTimes()) + "查看");
        myViewHolder.downloadCount.setText(String.valueOf(knowledgeItem.getDownTimes()) + "下载");

        return convertView;
    }

}

class MyViewHolder {
    TextView titleView;
    TextView updateTimeView;
    TextView viewCount;
    TextView downloadCount;
}

