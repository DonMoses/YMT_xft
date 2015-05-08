package com.ymt.demo1.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.KnowledgeItem;
import com.ymt.demo1.plates.knowledge.KnowledgeItemType;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItemAdapter extends BaseAdapter {
    ArrayList<KnowledgeItem> list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public KnowledgeItemAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<KnowledgeItem> list) {
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
            convertView = inflater.inflate(R.layout.layout_knowledge_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.icon_label);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.content_text);
            viewHolder.collectBtn = (ImageButton) convertView.findViewById(R.id.collect_btn);
            viewHolder.commentBtn = (ImageButton) convertView.findViewById(R.id.comment_btn);
            viewHolder.collectedCount = (TextView) convertView.findViewById(R.id.collected_count);
            viewHolder.commentedCount = (TextView) convertView.findViewById(R.id.comment_count);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleView.setText(list.get(position).getTitle());
        viewHolder.contentView.setText(list.get(position).getContentTxt());
        viewHolder.collectedCount.setText(String.valueOf(list.get(position).getCollectedCount()));
        viewHolder.commentedCount.setText(String.valueOf(list.get(position).getCommentedCount()));

        KnowledgeItemType type = list.get(position).getKnowledgeItemType();
        if (type != null) {

            switch (type) {
                case TXT:
                    Picasso.with(context).load(R.drawable.icon_file_txt).into(viewHolder.iconView);
                    break;
                case PPT:
                    Picasso.with(context).load(R.drawable.icon_file_ppt).into(viewHolder.iconView);
                    break;
                case PDF:
                    Picasso.with(context).load(R.drawable.icon_file_pdf).into(viewHolder.iconView);
                    break;
                case MP3:
                    Picasso.with(context).load(R.drawable.icon_file_mp3).into(viewHolder.iconView);
                    break;
                default:
                    break;
            }
        }

        /*
        设置收藏图标状态
         */
        if (list.get(position).isCollected()) {
            viewHolder.collectBtn.setImageBitmap(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_collected));
        } else {
            viewHolder.collectBtn.setImageBitmap(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_collect_normal));
        }
        /*
        评论图标状态
         */
        viewHolder.commentBtn.setImageBitmap(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_comment_normal));

        /*
        收藏按钮点击事件
         */
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.collect_btn:
                        KnowledgeItem item = list.get(position);
                        if (item.isCollected()) {
                            item.setCollectedCount(item.getCollectedCount() - 1);
                        } else {
                            item.setCollectedCount(item.getCollectedCount() + 1);
                        }
                        item.setIsCollected(!item.isCollected());
                        notifyDataSetChanged();
                        break;
                    //todo 评论按钮
                    default:
                        break;
                }
            }
        };

        viewHolder.collectBtn.setOnClickListener(onClickListener);
        return convertView;
    }

    class ViewHolder {
        TextView titleView;
        ImageView iconView;
        TextView contentView;
        ImageButton collectBtn;
        ImageButton commentBtn;
        TextView collectedCount;
        TextView commentedCount;

    }
}
