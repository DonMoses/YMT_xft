package com.ymt.demo1.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.SearchedConsultInfo;

import java.util.ArrayList;

public class SearchedConsultAdapter extends BaseAdapter {
    ArrayList<SearchedConsultInfo> list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public SearchedConsultAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(ArrayList<SearchedConsultInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_searched_consult, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.collectBtn = (ImageButton) convertView.findViewById(R.id.collect_btn);
            viewHolder.commentBtn = (ImageButton) convertView.findViewById(R.id.comment_btn);
            viewHolder.collectedCount = (TextView) convertView.findViewById(R.id.collected_count);
            viewHolder.commentedCount = (TextView) convertView.findViewById(R.id.comment_count);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(list.get(position).getConsultTitle());
        viewHolder.content.setText(list.get(position).getConsultAnswer());
        viewHolder.collectedCount.setText(String.valueOf(list.get(position).getCollectedCount()) + "人");
        viewHolder.commentedCount.setText(String.valueOf(list.get(position).getCommentCount()) + "人");
        if (list.get(position).isHasCollected()) {
            viewHolder.collectBtn.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_collected));
        } else {
            viewHolder.collectBtn.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_collect_normal));
        }

        viewHolder.commentBtn.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_comment_normal));

        //todo 设置收藏按钮和评论按钮监听
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.collect_btn:
                        //todo 从服务器更改对应收藏信息（更新本地收藏信息）
                        SearchedConsultInfo consultInfo = (SearchedConsultInfo) getItem(position);
                        if (consultInfo.isHasCollected()) {
                            consultInfo.setCollectedCount(consultInfo.getCollectedCount() - 1);         //减1
                        } else {
                            consultInfo.setCollectedCount(consultInfo.getCollectedCount() + 1);         //加1
                        }
                        consultInfo.setHasCollected(!consultInfo.isHasCollected());                     //收藏  -- 非收藏
                        notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };

        viewHolder.collectBtn.setOnClickListener(onClickListener);
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView content;
        ImageButton collectBtn;
        ImageButton commentBtn;
        TextView collectedCount;
        TextView commentedCount;
    }
}