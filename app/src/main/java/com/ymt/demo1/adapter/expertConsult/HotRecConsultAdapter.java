package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.HotConsult;
import com.ymt.demo1.beams.expert_consult.RecentConsult;
import com.ymt.demo1.main.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/5
 */
public class HotRecConsultAdapter extends BaseAdapter {

    public static final int HOT = 0;
    public static final int RECENT = 1;
    private Context context;
    private LayoutInflater inflater;
    private List<HotConsult> hotList;
    private List<RecentConsult> recList;
    private int type;

    public HotRecConsultAdapter(Context context, int hotOrRec) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        switch (hotOrRec) {
            case HOT:
                hotList = new ArrayList<>();
                break;
            case RECENT:
                recList = new ArrayList<>();
                break;
            default:
                break;
        }
        type = hotOrRec;
    }

    public void setHotList(List<HotConsult> hotList) {
        this.hotList = hotList;
        notifyDataSetChanged();
    }

    public void setRecList(List<RecentConsult> recList) {
        this.recList = recList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        switch (type) {
            case HOT:
                count = hotList.size();
                break;
            case RECENT:
                count = recList.size();
                break;
            default:
                break;
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        Object obj = null;
        switch (type) {
            case HOT:
                obj = hotList.get(position);
                break;
            case RECENT:
                obj = recList.get(position);
                break;
            default:
                break;
        }

        return obj;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_hot_rec_consult, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.consult_title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.consult_time);
            viewHolder.content = (TextView) convertView.findViewById(R.id.consult_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case HOT:
                viewHolder.title.setText(hotList.get(position).getArticle_title());
                viewHolder.time.setText(hotList.get(position).getCreate_time());
                viewHolder.content.setText(Html.fromHtml(hotList.get(position).getContent()));
                break;
            case RECENT:
                viewHolder.title.setText(recList.get(position).getArticle_title());
                viewHolder.time.setText(recList.get(position).getCreate_time());
                viewHolder.content.setText(StringUtils.replaceBlank(Html.fromHtml(recList.get(position).getContent()).toString()));
                break;
            default:
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView time;
        TextView content;

    }
}
