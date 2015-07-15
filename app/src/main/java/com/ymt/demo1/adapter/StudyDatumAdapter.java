package com.ymt.demo1.adapter;

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

/*
   定义listView 中item 的适配器
    */
public class StudyDatumAdapter extends BaseAdapter {
    final int SIMPLE_TYPE = 0;
    ArrayList<StudyDatumItem> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;

    public StudyDatumAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setList(ArrayList<StudyDatumItem> list) {
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
                    convertView = inflater.inflate(R.layout.item_study_datum, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.title = (TextView) convertView.findViewById(R.id.datum_title);
                    viewHolder.fileIcon = (ImageView) convertView.findViewById(R.id.file_icon);
                    viewHolder.content = (TextView) convertView.findViewById(R.id.datum_content);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.time);
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
                viewHolder.title.setText(studyDatum.getArticle_title());
//                StudyDatumItem.TypeO typeO = studyDatum.getTypeO();
//                switch (typeO) {
//                    case WORD:
//                        Picasso.with(context).load(R.drawable.icon_file_txt).into(viewHolder.fileIcon);
//                        break;
//                    case PPT:
//                        Picasso.with(context).load(R.drawable.icon_file_ppt).into(viewHolder.fileIcon);
//                        break;
//                    case PDF:
//                        Picasso.with(context).load(R.drawable.icon_file_pdf).into(viewHolder.fileIcon);
//                        break;
//                    case MP3:
//                        Picasso.with(context).load(R.drawable.icon_file_mp3).into(viewHolder.fileIcon);
//                        break;
//                    default:
//                        break;
//
//                }
                Picasso.with(context).load(R.drawable.icon_file_pdf).into(viewHolder.fileIcon);
                viewHolder.content.setText(String.valueOf(studyDatum.getContent()) + ".pdf");
                viewHolder.time.setText(String.valueOf(studyDatum.getCreate_time()));
                viewHolder.hitNum.setText(String.valueOf(studyDatum.getHitnum()) + "查看");
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
        TextView time;
        TextView hitNum;
    }
}