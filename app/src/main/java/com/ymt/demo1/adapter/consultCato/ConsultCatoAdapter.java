package com.ymt.demo1.adapter.consultCato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.consult_cato.ConsultCato;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/19
 */
public class ConsultCatoAdapter extends BaseAdapter {

    List<ConsultCato> list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public ConsultCatoAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<ConsultCato> list) {
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_common_quest_high, null);
            textView = (TextView) convertView.findViewById(R.id.common_quest_item_view);
            textView.setTextColor(context.getResources().getColor(android.R.color.white));
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        switch (position) {
            case 0:
                textView.setText("建筑");
                textView.setBackgroundResource(R.drawable.bg_cato_parent);
                break;
            case 5:
                textView.setText("专业");
                textView.setBackgroundResource(R.drawable.bg_cato_parent);
                break;
            case 10:
                textView.setText("关键词");
                textView.setBackgroundResource(R.drawable.bg_cato_parent);
                break;
            default:
                textView.setText(list.get(position).getNote());
                textView.setBackgroundResource(R.drawable.bg_cato_child);
                break;
        }

        return convertView;
    }
}
