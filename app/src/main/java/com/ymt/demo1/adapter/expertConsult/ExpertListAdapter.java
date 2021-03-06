package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.PreExpert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class ExpertListAdapter extends BaseAdapter {
    private List<PreExpert> experts = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private int width;
    private int height;

    public ExpertListAdapter(Context context, int screenWidth) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.width = (screenWidth - 28) / 2;
        this.height = (screenWidth - 28) / 2;
    }

    public void setExperts(List<PreExpert> experts) {
        this.experts = experts;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return experts.size();
    }

    @Override
    public Object getItem(int position) {
        return experts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_expert_grid_item, null);
            viewHolder = new MyViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.expert_name);
            viewHolder.type = (TextView) convertView.findViewById(R.id.expert_type);
            viewHolder.consultingCount = (TextView) convertView.findViewById(R.id.consulting_count);
            viewHolder.waitingCount = (TextView) convertView.findViewById(R.id.waiting_count);
            viewHolder.header = (ImageView) convertView.findViewById(R.id.expert_header);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(experts.get(position).getUsername());
        switch (experts.get(position).getLevel()) {
            case "level1":
                viewHolder.type.setText("普通专家");
                break;
            case "level2":
                viewHolder.type.setText("资深专家");
                break;
            case "level3":
                viewHolder.type.setText("国家专家");
                break;
            default:
                break;
        }
        viewHolder.consultingCount.setText("咨询人数：" + experts.get(position).getCount() + "人");
        viewHolder.waitingCount.setText("等待人数：" + experts.get(position).getCount() + "人");
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);
        viewHolder.header.setLayoutParams(params);
        Picasso.with(context).load(experts.get(position).getHeadImage()).into(viewHolder.header);
        return convertView;
    }

    class MyViewHolder {
        ImageView header;
        TextView name;
        TextView type;
        TextView consultingCount;
        TextView waitingCount;
    }
}
