package com.ymt.demo1.adapter.edu;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.VideoTrainItem;
import com.ymt.demo1.beams.knowledge.KnowledgeItem;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.bitmap.BitmapCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Dan on 2015/6/16
 */
public class VideoTrainAdapter extends BaseAdapter {
    List<VideoTrainItem> videos = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    int width;
    int height;
    private ImageLoader imageLoader;
    private RequestQueue mQueue;

    //    private String[] imgTemUrls = new String[]{
//            "http://s2.sinaimg.cn/middle/70cfd396ha4aa53d902d1&690&690",
//            "http://s10.sinaimg.cn/middle/70cfd396ha4aa49eb6fa9&690&690",
//            "http://s5.sinaimg.cn/middle/70cfd396ha4aa49ef1fc4&690&690",
//            "http://s7.sinaimg.cn/middle/70cfd396ha4aa49d304d6&690&690",
//            "http://s5.sinaimg.cn/middle/70cfd396ha4aa49ccc1f4&690&690"
//    };
    private Random random = new Random();

    public VideoTrainAdapter(Context context, int screenWidth) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.width = screenWidth - 10;
        this.height = (screenWidth - 6) / 2;
        mQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(mQueue, BitmapCache.getInstance());
    }

    public void setVideos(List<VideoTrainItem> videos) {
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
        final MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_video_list_item, null);
            viewHolder = new MyViewHolder();
            viewHolder.time = (TextView) convertView.findViewById(R.id.video_time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
            viewHolder.watchCount = (TextView) convertView.findViewById(R.id.video_watched_count);
            viewHolder.commCount = (TextView) convertView.findViewById(R.id.video_comm_count);
            viewHolder.cover = (NetworkImageView) convertView.findViewById(R.id.video_cover);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        String time = videos.get(position).getOpTime();
        String timeWithoutTime = time.substring(0, 10);
        viewHolder.time.setText(timeWithoutTime + "上传");
        viewHolder.title.setText(videos.get(position).getTitle());
        viewHolder.watchCount.setText(videos.get(position).getViews() + "观看");
        viewHolder.commCount.setText(+videos.get(position).getDownNum() + "下载");
        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(width, height);
        params.gravity = Gravity.CENTER;
        viewHolder.cover.setLayoutParams(params);
        viewHolder.cover.setDefaultImageResId(android.R.color.darker_gray);
        viewHolder.cover.setErrorImageResId(R.color.background_material_dark);
        //// TODO: 2015/10/30 视频封面
//        viewHolder.cover.setImageUrl(imgTemUrls[random.nextInt(imgTemUrls.length)], imageLoader);
        viewHolder.cover.setImageUrl(BaseURLUtil.getEduTrainVideoUrl(videos.get(position).getUrl()), imageLoader);
        return convertView;
    }

    public static class MyViewHolder {
        NetworkImageView cover;
        TextView title;
        TextView time;
        TextView watchCount;
        TextView commCount;
    }
}
