package com.ymt.demo1.adapter.edu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.edu.StudyDatumItem;

import java.util.ArrayList;
import java.util.List;

/*
   定义listView 中item 的适配器
    */
public class StudyDatumAdapter extends BaseAdapter {
    final int SIMPLE_TYPE = 0;
    List<StudyDatumItem> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public StudyDatumAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<StudyDatumItem> list) {
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
                StudyDatumItem studyDatum = (StudyDatumItem) getItem(position);
                viewHolder.title.setText(studyDatum.getTitle());
                Picasso.with(context).load(R.drawable.icon_file_pdf).into(viewHolder.fileIcon);
                viewHolder.content.setText(String.valueOf("主要内容：" + studyDatum.getSubjects()));
                viewHolder.desrc.setText(String.valueOf("资源出处：" + studyDatum.getDescs()));
                viewHolder.hitNum.setText(String.valueOf(studyDatum.getDownNum()) + "下载 " + studyDatum.getViews() + "查看");
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
        TextView title;
        ImageView fileIcon;
        TextView content;
        TextView desrc;
        TextView hitNum;
    }
}