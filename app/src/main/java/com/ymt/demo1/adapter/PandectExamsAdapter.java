package com.ymt.demo1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.XXFExam;

import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/21
 */
public class PandectExamsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    ArrayList<XXFExam> list = new ArrayList<>();
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_CONTENT = 1;

    public PandectExamsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(ArrayList<XXFExam> list) {
        this.list = list;
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {         //不是试题， 就是试题类型title
            return TYPE_TITLE;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TitleHolder titleHolder = null;
        ContentHolder contentHolder = null;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            switch (viewType) {
                case TYPE_TITLE:
                    convertView = inflater.inflate(R.layout.item_pandect_exams_title, null);
                    titleHolder = new TitleHolder();
                    titleHolder.examTitleType = (TextView) convertView.findViewById(R.id.cato_title);
                    titleHolder.viewAllExams = (TextView) convertView.findViewById(R.id.view_all_exam);
                    convertView.setTag(titleHolder);
                    break;
                case TYPE_CONTENT:
                    convertView = inflater.inflate(R.layout.item_pandect_exams_content, null);
                    contentHolder = new ContentHolder();
                    contentHolder.examName = (TextView) convertView.findViewById(R.id.content);
                    convertView.setTag(contentHolder);
                    break;
                default:
                    break;
            }
        } else {
            switch (viewType) {
                case TYPE_TITLE:
                    titleHolder = (TitleHolder) convertView.getTag();
                    break;
                case TYPE_CONTENT:
                    contentHolder = (ContentHolder) convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        switch (viewType) {
            case TYPE_TITLE:
                titleHolder.examTitleType.setText(list.get(position + 1).getTitleType());
                //"显示全部" 已经在xml中写死，所以这里不再重复设置，只添加监听。
                titleHolder.viewAllExams.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //todo 显示对应类型全部试题
                        XXFExam exam = list.get(position + 1);
                        Toast.makeText(context, "题库>>>>" + exam.getTitleType(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case TYPE_CONTENT:
                XXFExam exam = list.get(position);
                contentHolder.examName.setText(String.valueOf(exam.getExamYear()) +
                        exam.getTitleType() + "《" + exam.getExamSubject() + "》考试真题");
                convertView.setBackgroundResource(R.drawable.like_pressed_for_test);
                break;
            default:
                break;
        }

        return convertView;
    }

    class TitleHolder {
        TextView examTitleType;
        TextView viewAllExams;
    }

    class ContentHolder {
        TextView examName;
    }

}
