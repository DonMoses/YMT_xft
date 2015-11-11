package com.ymt.demo1.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.consult_cato.ConsultItem;
import com.ymt.demo1.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchedConsultAdapter extends BaseAdapter {
    List<ConsultItem> list = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public SearchedConsultAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setList(List<ConsultItem> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_searched_consult, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.subject);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            viewHolder.commentBtn = (ImageButton) convertView.findViewById(R.id.comment_btn);
            viewHolder.commentedCount = (TextView) convertView.findViewById(R.id.comment_count);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(list.get(position).getTitle());
        viewHolder.content.setText(StringUtils.replaceBlank(Html.fromHtml(list.get(position).getItContent()).toString()));
        viewHolder.commentedCount.setText(String.valueOf(list.get(position).getViews()) + "人");
        viewHolder.commentBtn.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_comment_normal));
        viewHolder.time.setText(list.get(position).getCreateTime());

        return convertView;
    }

    class ViewHolder {
        TextView time;
        TextView title;
        TextView content;
        ImageButton commentBtn;
        TextView commentedCount;
    }
}