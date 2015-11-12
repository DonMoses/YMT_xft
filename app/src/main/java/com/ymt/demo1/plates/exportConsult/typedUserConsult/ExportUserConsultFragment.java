package com.ymt.demo1.plates.exportConsult.typedUserConsult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.launchpages.QQUnreadMsgService;
import com.ymt.demo1.plates.exportConsult.chat.ConsultChatActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportUserConsultFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportUserConsultFragment";
    private RequestQueue mQueue;
    public UndoConAdapter undoConAdapter;
    private List<UndoConItem> conItems;
    private int index;
    private PullToRefreshListView pullToRefreshListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_only_pull_refresh_list_view, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getActivity(), QQUnreadMsgService.class);
        getActivity().startService(intent);

    }

    public static ExportUserConsultFragment newInstance(String emptyInfo) {
        ExportUserConsultFragment exportChatFragment = new ExportUserConsultFragment();
        Bundle args = new Bundle();
        args.putString("empty_info", emptyInfo);
        exportChatFragment.setArguments(args);
        return exportChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conItems = new ArrayList<>();
        index = 1;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueue = Volley.newRequestQueue(activity);
    }

    /**
     * 初始化会话界面
     */
    protected void initView(View view) {
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_list_view);
        undoConAdapter = new UndoConAdapter(getActivity());
        pullToRefreshListView.setAdapter(undoConAdapter);
        undoConAdapter.setUndoConItems(conItems);

        /*
         * todo 会话列表点击事件
         */
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConsultChatActivity.class);
                intent.putExtra("session_id", AppContext.now_session_id);
                intent.putExtra("cId", ((QQChatInfo) parent.getAdapter().getItem(position)).getConsultId());
                intent.putExtra("title", ((QQChatInfo) parent.getAdapter().getItem(position)).getTitle());
                startActivityForResult(intent, 1);
                startActivity(intent);
            }
        });

        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    index = 1;
                    conItems.clear();
                    jsonStr = null;
                    mQueue.add(getUndoConlist(AppContext.user_type, AppContext.now_session_id, index));
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    index++;
                    mQueue.add(getUndoConlist(AppContext.user_type, AppContext.now_session_id, index));
                }
            }
        });

        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ConsultChatActivity.class);
                intent.putExtra("session_id", AppContext.now_session_id);
                intent.putExtra("cId", ((UndoConItem) parent.getAdapter().getItem(position)).getConsultId());
                intent.putExtra("title", ((UndoConItem) parent.getAdapter().getItem(position)).getTitle());
                startActivity(intent);
            }
        });

        mQueue.add(getUndoConlist(AppContext.user_type, AppContext.now_session_id, index));

    }

    private String jsonStr = null;

    /**
     * 获取未分配的咨询列表
     */
    protected StringRequest getUndoConlist(String expertType, String sId, int index) {

        return new StringRequest(BaseURLUtil.getUndoConsultList(expertType, sId, index), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if ((TextUtils.isEmpty(jsonStr)) || ((!TextUtils.isEmpty(jsonStr)) && (jsonStr.equals(s)))) {
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONArray jsonArray = object.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            UndoConItem undoConItem = new UndoConItem();
                            undoConItem.setConsultId(obj.optInt("consultId"));
                            undoConItem.setContent(obj.optString("content"));
                            undoConItem.setCreateTime(obj.optString("createTime"));
                            undoConItem.setExpectationsName(obj.optString("expectationsName"));
                            undoConItem.setExpertId(obj.optString("expertId"));
                            undoConItem.setExpertName(obj.optString("expertName"));
                            undoConItem.setIsEnd(obj.optInt("isEnd"));
                            undoConItem.setOdExid(obj.optInt("odExid"));
                            undoConItem.setReplays(obj.optInt("replays"));
                            undoConItem.setScore(obj.optInt("score"));
                            undoConItem.setStatus(obj.optInt("status"));
                            undoConItem.setTitle(obj.optString("title"));
                            undoConItem.setType(obj.optString("type"));
                            undoConItem.setUserId(obj.optString("userId"));
                            undoConItem.setUserName(obj.optString("userName"));
                            undoConItem.setViews(obj.optInt("views"));

                            conItems.add(undoConItem);
                            undoConAdapter.notifyDataSetChanged();

                        }

                    } catch (JSONException e) {
                        AppContext.toastBadJson();
                    }

                    jsonStr = s;
                }
                pullToRefreshListView.onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                pullToRefreshListView.onRefreshComplete();

            }
        });
    }

    class UndoConAdapter extends BaseAdapter {
        private List<UndoConItem> undoConItems = new ArrayList<>();
        private LayoutInflater memInflater;

        private UndoConAdapter(Context context) {
            memInflater = LayoutInflater.from(context);
        }

        public void setUndoConItems(List<UndoConItem> undoConItems) {
            this.undoConItems = undoConItems;
        }

        @Override
        public int getCount() {
            return undoConItems.size();
        }

        @Override
        public Object getItem(int position) {
            return undoConItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = memInflater.inflate(R.layout.item_undo_list, null);
                holder = new Holder();
                holder.undo_title = (TextView) convertView.findViewById(R.id.undo_title);
                holder.undo_type = (TextView) convertView.findViewById(R.id.undo_type);
                holder.undo_time = (TextView) convertView.findViewById(R.id.undo_time);
                holder.undo_user = (TextView) convertView.findViewById(R.id.undo_user);
                holder.undo_expert = (TextView) convertView.findViewById(R.id.undo_expert);
                holder.undo_content = (TextView) convertView.findViewById(R.id.undo_content);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.undo_title.setText(undoConItems.get(position).getTitle());
            holder.undo_type.setText("咨询分类：" + undoConItems.get(position).getType());
            holder.undo_time.setText("创建于：" + undoConItems.get(position).getCreateTime());
            String userName = undoConItems.get(position).getUserName();
            if (TextUtils.isEmpty(userName)) {
                holder.undo_user.setText("用户：其他用户");
            } else {
                holder.undo_user.setText("用户：" + userName);
            }
            String expert = undoConItems.get(position).getExpectationsName();
            if (TextUtils.isEmpty(expert)) {
                holder.undo_expert.setText("期望专家：无");
            } else {
                holder.undo_expert.setText("期望专家：" + expert);
            }
            holder.undo_content.setText("咨询内容：" + undoConItems.get(position).getContent());

            return convertView;
        }

        class Holder {
            private TextView undo_title;
            private TextView undo_type;
            private TextView undo_time;
            private TextView undo_user;
            private TextView undo_expert;
            private TextView undo_content;
        }
    }
}
