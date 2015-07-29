package com.ymt.demo1.adapter.edu;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymt.demo1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/4
 */
public class ExamsGuideExpandListAdapter extends BaseExpandableListAdapter {

    List<String> parentList = new ArrayList<>();
    List<List<String>> childList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public ExamsGuideExpandListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<String> parentList, List<List<String>> childList) {
        this.parentList = parentList;
        this.childList = childList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expand_parent, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.expan_parent_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(parentList.get(groupPosition));
        viewHolder.textView.setTextColor(context.getResources().getColor(android.R.color.white));

        if (isExpanded) {
            viewHolder.imageView.setBackgroundResource(R.drawable.icon_arrows_b);
        } else {
            viewHolder.imageView.setBackgroundResource(R.drawable.icon_arrows_r);
        }

        convertView.setBackgroundColor(context.getResources().getColor(R.color.bg_view_blue));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_common_quest_high, null);
            textView = (TextView) convertView.findViewById(R.id.common_quest_item_view);
            textView.setGravity(Gravity.START);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }

        textView.setText(childList.get(groupPosition).get(childPosition));

        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
