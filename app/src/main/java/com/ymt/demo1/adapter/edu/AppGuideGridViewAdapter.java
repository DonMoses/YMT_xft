package com.ymt.demo1.adapter.edu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.ymt.demo1.beams.edu.EduAppGuide;
import com.ymt.demo1.customViews.AppGuideButton;

import java.util.ArrayList;

/**
 * GridViewAdapter继承自BaseAdapter：
 * 1.提供showHideView、hideView两个方法用于显示和隐藏选中Item的Text
 * <p/>
 * 2.提供swapView方法用于拖动过程中更新GridView中item显示
 */

public class AppGuideGridViewAdapter extends BaseAdapter {
    ArrayList<EduAppGuide> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    private int hidePosition = AdapterView.INVALID_POSITION;

    public AppGuideGridViewAdapter(Context context) {
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

    public void setList(ArrayList<EduAppGuide> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        每次返回一个AppGuideButton
            根据位置和内容，显现不同文本和背景
         */
        AppGuideButton guideButton;
        if (convertView == null) {
            convertView = new AppGuideButton(context);
            guideButton = (AppGuideButton) convertView;
            convertView.setTag(guideButton);
        } else {
            guideButton = (AppGuideButton) convertView.getTag();
        }

        //hide时隐藏Text
        if (position != hidePosition) {
            guideButton.setColor(((EduAppGuide) getItem(position)).getBgColor());
            guideButton.setText(((EduAppGuide) getItem(position)).getTitle());
        } else {
            guideButton.setColor(Color.WHITE);
            guideButton.setText("");
        }
        guideButton.setId(position);

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
            mList.add(destPos + 1, (EduAppGuide) getItem(draggedPos));
            mList.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if (draggedPos > destPos) {
            mList.add(destPos, (EduAppGuide) getItem(draggedPos));
            mList.remove(draggedPos + 1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }
}