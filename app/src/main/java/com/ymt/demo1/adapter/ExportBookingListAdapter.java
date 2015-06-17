package com.ymt.demo1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.ExportBookingPast;
import com.ymt.demo1.beams.expert_consult.ExportBookingRecent;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.plates.exportConsult.ExpertInfoActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportBookingListAdapter extends BaseExpandableListAdapter {
    ArrayList<String> parentList = new ArrayList<>();
    ArrayList<ArrayList<ExportBookingPast>> childList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    private static final int PAST_BOOKING = 0;
    private static final int RECENT_BOOKING = 1;

    public ExportBookingListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<String> parentList, ArrayList<ArrayList<ExportBookingPast>> childList) {
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
    public int getChildTypeCount() {
        return 2;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            return PAST_BOOKING;
        } else {
            return RECENT_BOOKING;
        }
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        PastHolder pastHolder = null;
        RecentHolder recentHolder = null;
        int bookingType = getChildType(groupPosition, childPosition);
        if (convertView == null) {
            switch (bookingType) {
                case PAST_BOOKING:
                    convertView = inflater.inflate(R.layout.item_export_booking_past, parent, false);
                    pastHolder = new PastHolder();
                    pastHolder.exportHeader = (CircleImageView) convertView.findViewById(R.id.export_header);
                    pastHolder.exportName = (TextView) convertView.findViewById(R.id.export_name);
                    pastHolder.bookingDate = (TextView) convertView.findViewById(R.id.booking_date);
                    convertView.setTag(pastHolder);
                    break;
                case RECENT_BOOKING:
                    convertView = inflater.inflate(R.layout.item_export_booking_recent, parent, false);
                    recentHolder = new RecentHolder();
                    recentHolder.exportHeader = (CircleImageView) convertView.findViewById(R.id.export_header);
                    recentHolder.exportName = (TextView) convertView.findViewById(R.id.export_name);
                    recentHolder.bookingDate = (TextView) convertView.findViewById(R.id.booking_date);
                    recentHolder.bookingDateHour = (TextView) convertView.findViewById(R.id.booking_date_hour);
                    convertView.setTag(recentHolder);
                    break;
                default:
                    break;
            }
        } else {
            switch (bookingType) {
                case PAST_BOOKING:
                    pastHolder = (PastHolder) convertView.getTag();
                    break;
                case RECENT_BOOKING:
                    recentHolder = (RecentHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        switch (bookingType) {
            case PAST_BOOKING:
                Picasso.with(context).load(childList.get(groupPosition).
                        get(childPosition).getExpert().getHead_pic()).into(pastHolder.exportHeader);
                pastHolder.exportName.setText(childList.get(groupPosition).get(childPosition).getExpert().getUser_name());
                pastHolder.bookingDate.setText(childList.get(groupPosition).get(childPosition).getDate());
                pastHolder.exportHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ExpertInfoActivity.class);
                        intent.putExtra("expert_info", childList.get(groupPosition).get(childPosition).getExpert());
                        context.startActivity(intent);
                    }
                });
                break;
            case RECENT_BOOKING:
                Picasso.with(context).load(childList.get(groupPosition).get(childPosition).
                        getExpert().getHead_pic()).into(recentHolder.exportHeader);
                recentHolder.exportName.setText(
                        childList.get(groupPosition).get(childPosition).getExpert().getUser_name());
                recentHolder.bookingDate.setText(
                        childList.get(groupPosition).get(childPosition).getDate());
                recentHolder.bookingDateHour.setText(
                        ((ExportBookingRecent) childList.get(groupPosition).get(childPosition)).getDateHour());
                recentHolder.exportHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ExpertInfoActivity.class);
                        intent.putExtra("expert_info", childList.get(groupPosition).get(childPosition).getExpert());
                        context.startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
        return convertView;
    }

    class PastHolder {
        private CircleImageView exportHeader;
        private TextView exportName;
        private TextView bookingDate;
    }

    class RecentHolder {
        private CircleImageView exportHeader;
        private TextView exportName;
        private TextView bookingDate;
        private TextView bookingDateHour;
    }

}
