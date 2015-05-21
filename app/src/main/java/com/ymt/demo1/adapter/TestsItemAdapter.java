package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.XXFExam;

import java.util.ArrayList;

/**
 * 历年真题列表
 */
public class TestsItemAdapter extends BaseAdapter {
    public static final int SIMPLE_TYPE = 0;

    ArrayList<XXFExam> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public TestsItemAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<XXFExam> list) {
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
                    convertView = inflater.inflate(R.layout.layout_test_item, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) convertView.findViewById(R.id.test_title_txt);
                    viewHolder.count = (TextView) convertView.findViewById(R.id.tests_count_txt);
                    viewHolder.totalTime = (TextView) convertView.findViewById(R.id.tests_time_txt);
                    viewHolder.totalScore = (TextView) convertView.findViewById(R.id.tests_score_txt);
                    viewHolder.watchedCount = (TextView) convertView.findViewById(R.id.watched_count_txt);
                    viewHolder.collectedCount = (TextView) convertView.findViewById(R.id.collected_count_txt);
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
                XXFExam test = (XXFExam) getItem(position);
                viewHolder.title.setText(test.getTitle());
                viewHolder.count.setText(test.getCount());
                viewHolder.totalTime.setText(test.getTotalTime());
                viewHolder.totalScore.setText(test.getTotalScore());
                viewHolder.watchedCount.setText(test.getWatchedCount());
                viewHolder.collectedCount.setText(test.getCollectedCount());
                break;

            default:
                break;

        }
        return convertView;
    }

    /*
    包含TestInfo中内容字段的ViewHolder
     */
    class ViewHolder {
        TextView title;
        TextView count;
        TextView totalTime;
        TextView totalScore;
        TextView watchedCount;
        TextView collectedCount;
    }
}