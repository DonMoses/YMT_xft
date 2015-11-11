package com.ymt.demo1.adapter.expertConsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.FollowedExpert;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.plates.exportConsult.MyConsultActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportFollowAdapter extends BaseAdapter {
    List<FollowedExpert> mList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    private RequestQueue queue;

    public ExportFollowAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        queue = ((MyConsultActivity) context).mQueue;
    }

    public void setList(List<FollowedExpert> mList) {
        this.mList = mList;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_export_follow, null);
            viewHolder = new ViewHolder();
            viewHolder.exportHeader = (CircleImageView) convertView.findViewById(R.id.export_header);
            viewHolder.exportName = (TextView) convertView.findViewById(R.id.export_name);
            viewHolder.unFollow = (TextView) convertView.findViewById(R.id.un_follow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context).load(mList.get(position).getHeadPic()).into(viewHolder.exportHeader);
        viewHolder.exportName.setText(mList.get(position).getUserName());
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.un_follow:
                        queue.add(unFollow(position, AppContext.now_session_id, mList.get(position).getFkExpertId()));
                        break;
                    default:
                        break;
                }
            }
        };
        viewHolder.unFollow.setOnClickListener(onClickListener);
        return convertView;
    }

    class ViewHolder {
        CircleImageView exportHeader;
        TextView exportName;
        TextView unFollow;
    }

    /**
     * 取消关注
     */
    private StringRequest unFollow(final int position, String sId, int expertId) {
        return new StringRequest(BaseURLUtil.unfollowExpert(sId, expertId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")
                            && jsonObject.getJSONObject("datas").optInt("listData") > 0) {
                        Toast.makeText(context, "已取消关注！", Toast.LENGTH_SHORT).show();
                        mList.remove(position);
                        setList(mList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
