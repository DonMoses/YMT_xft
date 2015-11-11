package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.ConsultInfo;
import com.ymt.demo1.utils.AppContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/7/5
 */
public class HotRecConsultAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<ConsultInfo> infoList;
    public final static String CONSULT_MAIN_TYPE = "CONSULT_MAIN_TYPE";
    public final static String CONSULT_LIST_TYPE = "CONSULT_LIST_TYPE";
    private String type;

    public HotRecConsultAdapter(Context context, String type) {
        this.inflater = LayoutInflater.from(context);
        infoList = new ArrayList<>();
        this.type = type;
    }

    public void setHotList(List<ConsultInfo> infoList) {
        this.infoList = infoList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoList.get(position);
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case CONSULT_MAIN_TYPE:
                int i = 0;
                switch (position) {
                    case 0:
                        i = 1;
                        break;
                    case 1:
                        i = 2;
                        break;
                    case 2:
                        i = 3;
                        break;
                    default:
                        break;
                }
                viewHolder.title.setText(String.valueOf(i) + "、" + infoList.get(position).getTitle());
                break;
            case CONSULT_LIST_TYPE:
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, AppContext.screenWidth / 10);
                params.gravity = Gravity.CENTER_VERTICAL;
                viewHolder.title.setLayoutParams(params);
                viewHolder.title.setText(infoList.get(position).getTitle());
                break;
            default:
                break;
        }

        return convertView;
    }

    class ViewHolder {
        TextView title;
    }
}
