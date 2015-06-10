package com.ymt.demo1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.HubPlate;
import com.ymt.demo1.beams.HubSubject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/4
 */
public class HubExpandListAdapter extends BaseExpandableListAdapter {

    List<HubPlate> parentList = new ArrayList<>();
    List<List<HubSubject>> childList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public HubExpandListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<HubPlate> parentList, List<List<HubSubject>> childList) {
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
            convertView = inflater.inflate(R.layout.item_hub_parent, null);
            textView = (TextView) convertView.findViewById(R.id.parent_view);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        textView.setText(parentList.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hub_child, null);
            textView = (TextView) convertView.findViewById(R.id.child_view);
            convertView.setTag(textView);
        } else {
            textView = (TextView) convertView.getTag();
        }
        String txt = childList.get(groupPosition).get(childPosition).getThreadSubject();
        textView.setText(txt);

        return convertView;
    }
}
