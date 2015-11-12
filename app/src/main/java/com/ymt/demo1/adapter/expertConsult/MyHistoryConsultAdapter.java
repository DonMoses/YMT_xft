package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.plates.exportConsult.typedUserConsult.MyHistoryConsultActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/5
 */
public class MyHistoryConsultAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<MyHistoryConsultActivity.HisConItem> infoList;

    public MyHistoryConsultAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        infoList = new ArrayList<>();
    }

    public void setList(List<MyHistoryConsultActivity.HisConItem> infoList) {
        this.infoList = infoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_study_datum_and_easy_wrong_tpic, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.datum_title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.datum_content);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.type = (TextView) convertView.findViewById(R.id.hit_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        convertView.findViewById(R.id.file_icon).setVisibility(View.GONE);
        viewHolder.title.setText(infoList.get(position).getTitle());
        viewHolder.content.setText(infoList.get(position).getContent());
        viewHolder.time.setText("创建于：" + infoList.get(position).getCreateTime());
        viewHolder.type.setText("咨询分类：" + infoList.get(position).getType());

        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView content;
        TextView time;
        TextView type;
    }
}
