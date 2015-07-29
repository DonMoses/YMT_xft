package com.ymt.demo1.adapter.hub;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ymt.demo1.R;
import com.ymt.demo1.beams.hub.MyHubReply;
import com.ymt.demo1.beams.hub.MyHubSysInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/6/16
 */
public class MyHubSysInfoAdapter extends BaseAdapter {
    List<MyHubSysInfo> myHubSysInfos = new ArrayList<>();
    LayoutInflater inflater;
    Context context;

    public MyHubSysInfoAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setSubjects(List<MyHubSysInfo> myHubSysInfos) {
        this.myHubSysInfos = myHubSysInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myHubSysInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return myHubSysInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_hub_sys_info, null);
            viewHolder = new MyViewHolder();
            viewHolder.msgType = (TextView) convertView.findViewById(R.id.msg_type);
            viewHolder.msgDateline = (TextView) convertView.findViewById(R.id.date_line);
            viewHolder.msgContent = (TextView) convertView.findViewById(R.id.msg_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyViewHolder) convertView.getTag();
        }

        switch (myHubSysInfos.get(position).getFrom_idtype()) {
            case "welcomemsg":
                viewHolder.msgType.setText("系统消息>>");
                break;
            case "post":
                viewHolder.msgType.setText("新的回复>>");
                break;
            case "friendrequest":
                viewHolder.msgType.setText("好友请求>>");
                break;
            default:
                break;
        }
        viewHolder.msgDateline.setText(String.valueOf(myHubSysInfos.get(position).getDateline()));
        viewHolder.msgContent.setText(Html.fromHtml(myHubSysInfos.get(position).getNote()));

        return convertView;
    }

    class MyViewHolder {
        TextView msgType;
        TextView msgContent;
        TextView msgDateline;
    }
}
