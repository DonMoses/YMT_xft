package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.EduDialogueInfo;

import java.util.ArrayList;

/*
   定义listView 中item 的适配器
    */
public class DialogueAdapter extends BaseAdapter {
    final int SIMPLE_TYPE = 0;
    ArrayList<EduDialogueInfo> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public DialogueAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<EduDialogueInfo> list) {
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
                    convertView = inflater.inflate(R.layout.layout_dialogue_item, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.question = (TextView) convertView.findViewById(R.id.dialogue_qust_txt);
                    viewHolder.answer = (TextView) convertView.findViewById(R.id.dialogue_answer_view);
                    viewHolder.watchedCount = (TextView) convertView.findViewById(R.id.dialogue_watched_count);
                    viewHolder.collectedCount = (TextView) convertView.findViewById(R.id.dialogue_collected_count);
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
                EduDialogueInfo dialogue = (EduDialogueInfo) getItem(position);
                viewHolder.question.setText(dialogue.getQuestion());
                viewHolder.answer.setText(dialogue.getAnswer());
                viewHolder.watchedCount.setText(String.valueOf(dialogue.getWatchedCount()));
                viewHolder.collectedCount.setText(String.valueOf(dialogue.getCollectedCount()));
                break;

            default:
                break;

        }
        return convertView;
    }

    /*
    包含EduDialogueInfo中内容字段的ViewHolder
     */
    class ViewHolder {
        TextView question;
        TextView answer;
        TextView watchedCount;
        TextView collectedCount;
    }
}