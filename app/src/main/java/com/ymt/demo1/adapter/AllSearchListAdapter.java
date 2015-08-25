package com.ymt.demo1.adapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.AllSearchItem;
import com.ymt.demo1.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/22
 */
public class AllSearchListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<AllSearchItem> list = new ArrayList<>();
    private String keyWord;

    public void setList(List<AllSearchItem> list, String keyWord) {
        this.list = list;
        this.keyWord = keyWord;
        notifyDataSetChanged();
    }

    public AllSearchListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_all_search_knowledge_result, null);
            holder = new ViewHolder();
            holder.resultTitle = (TextView) convertView.findViewById(R.id.result_title);
            holder.resultType = (TextView) convertView.findViewById(R.id.result_type);
            holder.resultFile = (TextView) convertView.findViewById(R.id.result_file);
            holder.resultContent = (TextView) convertView.findViewById(R.id.result_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.resultTitle.setText(list.get(position).getDoc_title());
        holder.resultType.setText("分类：" + list.get(position).getDoc_type_name());
        String fileName = list.get(position).getFile_name();
        String filePath = list.get(position).getFile_path();
        if ((!TextUtils.isEmpty(fileName)) && (!TextUtils.isEmpty(filePath))) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            holder.resultFile.setLayoutParams(params);
            holder.resultFile.setText("附件：" + fileName);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            holder.resultFile.setLayoutParams(params);
        }

        String resultContent = StringUtils.replaceBlank(Html.fromHtml(list.get(position).getDoc_content()).toString());
        int length = keyWord.length();
        int index = resultContent.indexOf(keyWord);
        if (index >= 0) {
            SpannableString spannableString = new SpannableString(resultContent);
            BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(context.getResources().getColor(android.R.color.darker_gray));
            spannableString.setSpan(backgroundColorSpan, index, index + length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.resultContent.setText(spannableString);
        }

        return convertView;
    }

    class ViewHolder {
        TextView resultTitle;
        TextView resultType;
        TextView resultFile;
        TextView resultContent;
    }
}
