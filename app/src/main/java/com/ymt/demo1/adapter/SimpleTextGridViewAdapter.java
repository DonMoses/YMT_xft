package com.ymt.demo1.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
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

    private int itemWidth;      //item宽
    private int itemHeight;     //item高

    /**
     * @param numColumns : 列数
     * @param spacing    ：间隔
     */
    public SimpleTextGridViewAdapter(Context context, int numColumns, int spacing) {
        this.context = context;
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        /*
      屏幕的宽高
      为了显示正方形的item ，则item宽 = 高。xml布局中不指定其宽高，直接match_parent即可。
     */
        int scWidth = size.x;
        //根据GridView的列和间隔，先将dp 转化为 px，然后如下计算item宽高。
        itemHeight = itemWidth =
                (scWidth - (numColumns) * (Dp2Px(context, spacing))) / (numColumns + 1);

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();

    }

    /**
     * 设置返回item 的view 的宽高
     * 将dp 转换为 px
     */
    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
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
            view.setBackgroundColor(context.getResources().getColor(R.color.guide_bg));
            view.setText(getItem(position).toString());
        } else {
            view.setBackgroundColor(Color.WHITE);
            view.setText("");
        }

        if (getItem(position).toString().equals("全部")) {
            view.setBackgroundColor(context.getResources().getColor(R.color.bg_view_blue));
            view.setTextColor(ColorStateList.valueOf(Color.WHITE));
        } else {
            view.setBackgroundColor(context.getResources().getColor(R.color.guide_bg));
            view.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        }

        view.setId(position);
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(itemWidth, itemHeight);
        convertView.setLayoutParams(param);

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