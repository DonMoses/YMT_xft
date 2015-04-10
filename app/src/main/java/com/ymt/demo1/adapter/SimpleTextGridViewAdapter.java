package com.ymt.demo1.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;

import java.util.ArrayList;

/**
 * GridViewAdapter继承自BaseAdapter：
 * 1.提供showHideView、hideView两个方法用于显示和隐藏选中Item的Text
 * <p/>
 * 2.提供swapView方法用于拖动过程中更新GridView中item显示
 */

public class SimpleTextGridViewAdapter extends BaseAdapter {
    ArrayList<String> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    private int hidePosition = AdapterView.INVALID_POSITION;

    public SimpleTextGridViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();

    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    public void setList(ArrayList<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.test_year_layout, null);
            view = (TextView) convertView.findViewById(R.id.text_year);
            convertView.setTag(view);
        } else {
            view = (TextView) convertView.getTag();
        }

        //hide时隐藏Text
        if (position != hidePosition) {
            view.setText(getItem(position).toString());
            switch (getItem(position).toString()) {
                case "全部":
                    view.setBackgroundColor(context.getResources().getColor(R.color.bg_view_blue));
                    view.setTextColor(ColorStateList.valueOf(Color.WHITE));
                    break;
                default:
                    break;
            }
        } else {
            view.setText("");
            view.getBackground().setAlpha(0);
        }

        view.setId(position);

        return convertView;
    }

    public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    public void removeView(int pos) {
        mList.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if (draggedPos < destPos) {
            mList.add(destPos + 1, getItem(draggedPos).toString());
            mList.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if (draggedPos > destPos) {
            mList.add(destPos, getItem(draggedPos).toString());
            mList.remove(draggedPos + 1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }
}