package com.ymt.demo1.adapter.edu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.EasyWrongTopic;

import java.util.ArrayList;
import java.util.List;

/*
   定义listView 中item 的适配器
    */
public class EasyWrongTopicAdapter extends BaseAdapter {
    final int SIMPLE_TYPE = 0;
    List<EasyWrongTopic> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public EasyWrongTopicAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<EasyWrongTopic> list) {
        this.mList = list;
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
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return SIMPLE_TYPE;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case SIMPLE_TYPE:
                    convertView = inflater.inflate(R.layout.item_study_datum_and_easy_wrong_tpic, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) convertView.findViewById(R.id.datum_title);
                    viewHolder.fileIcon = (ImageView) convertView.findViewById(R.id.file_icon);
                    viewHolder.content = (TextView) convertView.findViewById(R.id.datum_content);
                    viewHolder.desrc = (TextView) convertView.findViewById(R.id.time);
                    viewHolder.hitNum = (TextView) convertView.findViewById(R.id.hit_num);
                    convertView.setTag(viewHolder);
                    break;
                default:
                    break;
            }

        } else {
            switch (type) {
                case SIMPLE_TYPE:
                    viewHolder = (ViewHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

            /*
            设置TestInfo字段信息到列表的item中
             */
        switch (type) {
            case SIMPLE_TYPE:
                EasyWrongTopic easyWrongTopic = (EasyWrongTopic) getItem(position);
                String level = null;
                switch (easyWrongTopic.getTopicType()) {
                    case 1:
                        level = "（单选题）";
                        break;
                    case 2:
                        level = "（多选题）";
                        break;
                    case 3:
                        level = "（判断题）";
                        break;
                    case 5:
                        level = "（问答题）";
                        break;
                    default:
                        break;
                }
                viewHolder.title.setText(easyWrongTopic.getLevel() + "-" + level);
                viewHolder.content.setText(String.valueOf(easyWrongTopic.getProblem()));
                viewHolder.desrc.setText(String.valueOf("知识点：" + easyWrongTopic.getSubjects()));
                break;
            default:
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        ImageView fileIcon;
        TextView content;
        TextView desrc;
        TextView hitNum;
    }
}