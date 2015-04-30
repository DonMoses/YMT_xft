package com.ymt.demo1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;

import java.util.ArrayList;
import java.util.List;

public class LongClickItemsAdapter extends BaseAdapter {
    List<String> mList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private int selectedPosition = -1;

    public LongClickItemsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setmList(List<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void setSelectedItemPosition(int position) {
        selectedPosition = position;
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
        TextView textView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_common_quest_high, null);
            textView = (TextView) convertView.findViewById(R.id.common_quest_item_view);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }

        textView.setText(mList.get(position));
        if (selectedPosition != position) {
            convertView.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.DKGRAY);
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            textView.setTextColor(Color.WHITE);
        }

        return convertView;
    }
}