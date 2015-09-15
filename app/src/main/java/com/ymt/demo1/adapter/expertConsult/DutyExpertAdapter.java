package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.expert_consult.OnDutyExpert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/17
 */
public class DutyExpertAdapter extends BaseAdapter {
    List<OnDutyExpert> expertList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public DutyExpertAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setExpertList(List<OnDutyExpert> expertList) {
        this.expertList = expertList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return expertList.size();
    }

    @Override
    public Object getItem(int position) {
        return expertList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView expertName;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_common_quest_high, null);
            expertName = (TextView) convertView.findViewById(R.id.common_quest_item_view);
            convertView.setTag(expertName);
        } else {
            expertName = (TextView) convertView.getTag();
        }
        expertName.setText(expertList.get(position).getName());

        return convertView;
    }
}
