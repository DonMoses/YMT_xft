package com.ymt.demo1.adapter.collect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.collect.CollectCon;
import com.ymt.demo1.beams.collect.CollectEdu;
import com.ymt.demo1.beams.collect.CollectKno;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class CollectItemAdapter extends BaseAdapter {
    public static final int COLLECT_TYPE_CON = 1;       //咨询
    public static final int COLLECT_TYPE_KNO = 2;       //知识
    public static final int COLLECT_TYPE_EDU = 3;       //教育
    List<Object> objects = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    private int type;

    public CollectItemAdapter(Context context, int type) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.type = type;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_video_list_item, null);
            viewHolder = new MyViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.video_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
            viewHolder.viewCount = (TextView) convertView.findViewById(R.id.video_watched_count);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        convertView.findViewById(R.id.cover_layout).setVisibility(View.GONE);

        switch (type) {
            case COLLECT_TYPE_CON:
                CollectCon con = (CollectCon) objects.get(position);
                viewHolder.time.setText(con.getTime().substring(0, 10) + "收藏");
                viewHolder.title.setText(con.getTitle());
                viewHolder.viewCount.setText(con.getViews() + "查看");
                break;
            case COLLECT_TYPE_KNO:
                CollectKno kno = (CollectKno) objects.get(position);
                viewHolder.time.setText(kno.getCollectionTime().substring(0, 10) + "收藏");
                viewHolder.title.setText(kno.getDocTitle());
                viewHolder.viewCount.setText(kno.getAvr_scor() + "积分");
                break;
            case COLLECT_TYPE_EDU:
                CollectEdu edu = (CollectEdu) objects.get(position);
                viewHolder.time.setText(edu.getTime().substring(0, 10) + "收藏");
                viewHolder.title.setText(edu.getTitle());
                viewHolder.viewCount.setText(edu.getScore() + "积分");
                break;
            default:
                break;
        }
        return convertView;
    }

    public static class MyViewHolder {
        TextView title;
        TextView time;
        TextView viewCount;
    }
}
