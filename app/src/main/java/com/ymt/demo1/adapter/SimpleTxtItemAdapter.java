package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/28
 */
public class SimpleTxtItemAdapter extends BaseAdapter {
    ArrayList<String> list = new ArrayList<>();
    LayoutInflater inflater;
    private int itemBgColor;
    private int itemTxtColor;

    public SimpleTxtItemAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);

    }

    public void setColor(int itemBgColor, int itemTxtColor) {
        this.itemBgColor = itemBgColor;
        this.itemTxtColor = itemTxtColor;
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
            convertView = inflater.inflate(R.layout.item_view_common_quest_low, null);
            textView = (TextView) convertView.findViewById(R.id.common_quest_item_view);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        if (itemBgColor != 0 && itemTxtColor != 0) {
            textView.setBackgroundColor(itemBgColor);
            textView.setTextColor(itemTxtColor);
        }
        textView.setText(list.get(position));
        return convertView;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}
