package com.ymt.demo1.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_knowledge_item, null);
            viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.icon_label);
            viewHolder.contentView = (TextView) convertView.findViewById(R.id.content_text);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.titleView.setText(list.get(position).getTitle());
        viewHolder.contentView.setText(list.get(position).getContentTxt());

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
        return convertView;
    }

    class ViewHolder {
        TextView titleView;
        ImageView iconView;
        TextView contentView;

    }
}
