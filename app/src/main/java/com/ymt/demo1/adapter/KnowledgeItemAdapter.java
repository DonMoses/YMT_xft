package com.ymt.demo1.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.knowledge.KnowledgeItemBZGF;
import com.ymt.demo1.beams.knowledge.KnowledgeItemKYWX;
import com.ymt.demo1.plates.knowledge.KnowledgeItemListViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/4/29
 */
public class KnowledgeItemAdapter extends BaseAdapter {
    List<KnowledgeItemBZGF> listBZGF = new ArrayList<>();
    List<KnowledgeItemKYWX> listKYWX = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    String knowledgeType;

    public KnowledgeItemAdapter(Context context, String knowledgeType) {
        this.context = context;
        this.knowledgeType = knowledgeType;
        this.inflater = LayoutInflater.from(context);
    }

    public void setBZGFList(List<KnowledgeItemBZGF> list) {
        this.listBZGF = list;
        notifyDataSetChanged();
    }

    public void setKYWXList(List<KnowledgeItemKYWX> list) {
        this.listKYWX = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count;
        switch (knowledgeType) {
            case KnowledgeItemListViewFragment.KNOWLEDGE_BZGF:
                count = listBZGF.size();
                break;
            case KnowledgeItemListViewFragment.KNOWLEDGE_KYWX:
                count = listKYWX.size();
                break;
            case KnowledgeItemListViewFragment.KNOWLEDGE_SPZL:
                count = 0;
                break;
            default:
                count = 0;
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object object;
        switch (knowledgeType) {
            case KnowledgeItemListViewFragment.KNOWLEDGE_BZGF:
                object = listBZGF.get(position);
                break;
            case KnowledgeItemListViewFragment.KNOWLEDGE_KYWX:
                object = listKYWX.get(position);
                break;
            case KnowledgeItemListViewFragment.KNOWLEDGE_SPZL:
                object = null;
                break;
            default:
                object = null;
                break;
        }
        return object;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder myViewHolder = null;
        if (convertView == null) {
            switch (knowledgeType) {
                case KnowledgeItemListViewFragment.KNOWLEDGE_BZGF:
                case KnowledgeItemListViewFragment.KNOWLEDGE_KYWX:
                    convertView = inflater.inflate(R.layout.layout_knowledge_item, null);
                    myViewHolder = new MyViewHolder();
                    myViewHolder.titleView = (TextView) convertView.findViewById(R.id.subject);
                    myViewHolder.createTimeView = (TextView) convertView.findViewById(R.id.create_time);
                    myViewHolder.contentView = (TextView) convertView.findViewById(R.id.content_text);
                    myViewHolder.downloadBtn = (ImageButton) convertView.findViewById(R.id.download_btn);
                    myViewHolder.viewBtn = (ImageButton) convertView.findViewById(R.id.view_btn);
                    myViewHolder.downloadCount = (TextView) convertView.findViewById(R.id.download_count);
                    myViewHolder.viewCount = (TextView) convertView.findViewById(R.id.view_count);
                    convertView.setTag(myViewHolder);
                    break;
                case KnowledgeItemListViewFragment.KNOWLEDGE_SPZL:
                    break;
                default:
                    break;

            }


        } else {
            switch (knowledgeType) {
                case KnowledgeItemListViewFragment.KNOWLEDGE_BZGF:
                case KnowledgeItemListViewFragment.KNOWLEDGE_KYWX:
                    myViewHolder = (MyViewHolder) convertView.getTag();
                    break;
                case KnowledgeItemListViewFragment.KNOWLEDGE_SPZL:
                    break;
                default:
                    break;
            }
        }

        switch (knowledgeType) {
            case KnowledgeItemListViewFragment.KNOWLEDGE_BZGF:
                myViewHolder.titleView.setText(listBZGF.get(position).getArticle_title());
                myViewHolder.contentView.setText(Html.fromHtml(listBZGF.get(position).getContent()));
                myViewHolder.createTimeView.setText(listBZGF.get(position).getCreate_time());
                myViewHolder.viewCount.setText(listBZGF.get(position).getHitnum() + "查看");
                myViewHolder.downloadCount.setText(listBZGF.get(position).getDowncount() + "下载");
                break;
            case KnowledgeItemListViewFragment.KNOWLEDGE_KYWX:
                myViewHolder.titleView.setText(listKYWX.get(position).getArticle_title());
                myViewHolder.contentView.setText(Html.fromHtml(listKYWX.get(position).getContent()));
                myViewHolder.createTimeView.setText(listKYWX.get(position).getCreate_time());
                myViewHolder.viewCount.setText(listKYWX.get(position).getHitnum() + "查看");
                myViewHolder.downloadCount.setText(listKYWX.get(position).getDowncount() + "下载");
                break;
            case KnowledgeItemListViewFragment.KNOWLEDGE_SPZL:
                break;
            default:
                break;
        }


//        KnowledgeItemType type = list.get(position).getKnowledgeItemType();
//        if (type != null) {
//
//            switch (type) {
//                case TXT:
//                    Picasso.with(context).load(R.drawable.icon_file_txt).into(myViewHolder.iconView);
//                    break;
//                case PPT:
//                    Picasso.with(context).load(R.drawable.icon_file_ppt).into(myViewHolder.iconView);
//                    break;
//                case PDF:
//                    Picasso.with(context).load(R.drawable.icon_file_pdf).into(myViewHolder.iconView);
//                    break;
//                case MP3:
//                    Picasso.with(context).load(R.drawable.icon_file_mp3).into(myViewHolder.iconView);
//                    break;
//                default:
//                    break;
//            }
//        }


//        /*
//        收藏按钮点击事件
//         */
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.collect_btn:
//                        KnowledgeItemBZGF item = list.get(position);
//                        if (item.isCollected()) {
//                            item.setCollectedCount(item.getCollectedCount() - 1);
//                        } else {
//                            item.setCollectedCount(item.getCollectedCount() + 1);
//                        }
//                        item.setIsCollected(!item.isCollected());
//                        notifyDataSetChanged();
//                        break;
//                    //todo 评论按钮
//                    default:
//                        break;
//                }
//            }
//        };
//
//        myViewHolder.collectBtn.setOnClickListener(onClickListener);
        return convertView;
    }

    class MyViewHolder {
        TextView titleView;
        TextView contentView;
        TextView createTimeView;
        ImageButton viewBtn;
        ImageButton downloadBtn;
        TextView viewCount;
        TextView downloadCount;

    }
}
