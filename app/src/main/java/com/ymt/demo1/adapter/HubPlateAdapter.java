package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.hub.HubPlate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/4
 */
public class HubPlateAdapter extends BaseExpandableListAdapter {
    LayoutInflater inflater;
    Context context;
    List<HubPlate> parentList = new ArrayList<>();
    List<List<HubPlate>> childList = new ArrayList<>();

    public HubPlateAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<HubPlate> parentList, List<List<HubPlate>> childList) {
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
        Level1ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expand_parent, null);
            viewHolder = new Level1ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.expan_parent_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Level1ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(parentList.get(groupPosition).getName());
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
        Level2Holder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hub_parent, null);
            holder = new Level2Holder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (Level2Holder) convertView.getTag();
        }
        holder.textView.setText(childList.get(groupPosition).get(childPosition).getName());

        return convertView;
    }

    class Level1ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    class Level2Holder {
        TextView textView;
    }


}
