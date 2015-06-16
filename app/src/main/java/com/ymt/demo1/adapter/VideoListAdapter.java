package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.knowledge.KnowledgeVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class VideoListAdapter extends BaseAdapter {
    List<KnowledgeVideo> videos = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    int width;
    int height;

    public VideoListAdapter(Context context, int screenWidth) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.width = screenWidth - 10;
        this.height = (screenWidth - 6) / 2;
    }

    public void setVideos(List<KnowledgeVideo> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_video_list_item, null);
            viewHolder = new MyViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.video_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
            viewHolder.watchCount = (TextView) convertView.findViewById(R.id.video_watched_count);
            viewHolder.commCount = (TextView) convertView.findViewById(R.id.video_comm_count);
            viewHolder.cover = (ImageView) convertView.findViewById(R.id.video_cover);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }
        viewHolder.time.setText(videos.get(position).getCreate_time().substring(14, 19));
        viewHolder.title.setText(videos.get(position).getArticle_title());
        viewHolder.watchCount.setText("观看" + videos.get(position).getHitnum());
        viewHolder.commCount.setText("下载" + videos.get(position).getDowncount());
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(width, height);
        viewHolder.cover.setLayoutParams(params);
        return convertView;
    }

    class MyViewHolder {
        ImageView cover;
        TextView title;
        TextView time;
        TextView watchCount;
        TextView commCount;
    }
}
