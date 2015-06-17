package com.ymt.demo1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.plates.exportConsult.ExpertInfoActivity;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportFollowAdapter extends BaseAdapter {
    ArrayList<Expert> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public ExportFollowAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<Expert> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_export_follow, null);
            viewHolder = new ViewHolder();
            viewHolder.exportHeader = (CircleImageView) convertView.findViewById(R.id.export_header);
            viewHolder.exportName = (TextView) convertView.findViewById(R.id.export_name);
            viewHolder.unFollow = (TextView) convertView.findViewById(R.id.un_follow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(mList.get(position).getHead_pic()).into(viewHolder.exportHeader);
        viewHolder.exportName.setText(mList.get(position).getUser_name());
        viewHolder.exportHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExpertInfoActivity.class);
                intent.putExtra("expert_info", mList.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        CircleImageView exportHeader;
        TextView exportName;
        TextView unFollow;
    }
}
