package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.PastExamItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/21
 */
public class PastExamsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    List<PastExamItem> list = new ArrayList<>();

    public PastExamsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<PastExamItem> list) {
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
            convertView = inflater.inflate(R.layout.item_pandect_exams_content_2, null);
            contentHolder = new ContentHolder();
            contentHolder.examName = (TextView) convertView.findViewById(R.id.content);
            contentHolder.downloadCount = (TextView) convertView.findViewById(R.id.download_count);
            convertView.setTag(contentHolder);
        } else {
            contentHolder = (ContentHolder) convertView.getTag();
        }
        contentHolder.examName.setText(list.get(position).getArticle_title());
        contentHolder.downloadCount.setText(list.get(position).getDowncount() + "下载");
        convertView.setBackgroundResource(R.drawable.like_pressed_for_test);

        return convertView;
    }

    class ContentHolder {
        TextView examName;
        TextView downloadCount;
    }

}
