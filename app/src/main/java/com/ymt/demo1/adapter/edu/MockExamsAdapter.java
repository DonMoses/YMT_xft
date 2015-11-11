package com.ymt.demo1.adapter.edu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.MockExamItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/21
 */
public class MockExamsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    List<MockExamItem> list = new ArrayList<>();

    public MockExamsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<MockExamItem> list) {
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ContentHolder contentHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mock_exams_content, null);
            contentHolder = new ContentHolder();
            contentHolder.examName = (TextView) convertView.findViewById(R.id.content);
            contentHolder.totalItem = (TextView) convertView.findViewById(R.id.total_item);
            contentHolder.totalScore = (TextView) convertView.findViewById(R.id.total_score);
            convertView.setTag(contentHolder);
        } else {
            contentHolder = (ContentHolder) convertView.getTag();
        }
        contentHolder.examName.setText(list.get(position).getExaName());
        contentHolder.totalItem.setText(list.get(position).getBank_num() + "题");
        contentHolder.totalScore.setText(list.get(position).getScores() + "分");
        convertView.setBackgroundResource(R.drawable.like_pressed_for_test);

        return convertView;
    }

    class ContentHolder {
        TextView examName;
        TextView totalItem;
        TextView totalScore;
    }

}
