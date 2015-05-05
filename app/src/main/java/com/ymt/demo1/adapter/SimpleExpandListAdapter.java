package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/4
 */
public class SimpleExpandListAdapter extends BaseExpandableListAdapter {

    List<String> parentList = new ArrayList<>();
    List<List<String>> childList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public SimpleExpandListAdapter(Context context) {
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
        TextView textView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expand_parent, null);
            textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        textView.setText(parentList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_view_common_quest_high, null);
            textView = (TextView) convertView.findViewById(R.id.common_quest_item_view);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        String txt = childList.get(groupPosition).get(childPosition);
        textView.setText(txt);
        switch (txt.length() % 4) {
            case 0:
                textView.setTextColor(context.getResources().getColor(R.color.guide_bmtj));
                break;
            case 1:
                textView.setTextColor(context.getResources().getColor(R.color.guide_kskm));
                break;
            case 2:
                textView.setTextColor(context.getResources().getColor(R.color.guide_kssj));
                break;
            case 3:
                textView.setTextColor(context.getResources().getColor(R.color.guide_zyfw));
                break;
            default:
                break;
        }
        return convertView;
    }
}
