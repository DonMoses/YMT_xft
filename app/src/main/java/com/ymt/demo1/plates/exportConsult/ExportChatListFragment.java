package com.ymt.demo1.plates.exportConsult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.QQChatListAdapter;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.launchpages.QQUnreadMsgService;
import com.ymt.demo1.plates.exportConsult.chat.ConsultChatActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportChatListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportChatFragment";
    private RequestQueue mQueue;
    public QQChatListAdapter chatAdapter;
    private ListView qqListView;
    private List<QQChatInfo> chatInfos;
    private boolean firstIn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_chat, container, false);
        initChatList(view);
        return view;
    }

    private MyHandler myHandler = new MyHandler(this);

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Intent intent = new Intent(getActivity(), QQUnreadMsgService.class);
        getActivity().startService(intent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ExportChatListFragment.this.isVisible()) {
                    myHandler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        qqListView.setSelection(qqListView.getBottom());

        if (!firstIn) {
            Message msg = Message.obtain();
            for (int i = 0; i < chatInfos.size(); i++) {
                msg.obj = chatInfos.get(i).getConsultId();
                msg.what = 1024;
                myHandler.sendMessage(msg);
            }
        }

    }

    public static ExportChatListFragment newInstance(String emptyInfo) {
        ExportChatListFragment exportChatFragment = new ExportChatListFragment();
        Bundle args = new Bundle();
        args.putString("empty_info", emptyInfo);
        exportChatFragment.setArguments(args);
        return exportChatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatInfos = new ArrayList<>();
        chatInfos.addAll(DataSupport.findAll(QQChatInfo.class));
        firstIn = true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueue = Volley.newRequestQueue(activity);
        doRefresh();
    }

    /**
     * 初始化会话界面
     */
    protected void initChatList(View view) {
        qqListView = (ListView) view.findViewById(R.id.export_chat_list_view);
        chatAdapter = new QQChatListAdapter(getActivity());
        qqListView.setAdapter(chatAdapter);
        chatAdapter.setList(chatInfos);

        /*
         * todo 会话列表点击事件
         */
        qqListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    }

    /**
     * 获取QQ会话列表
     */
    protected StringRequest getMyQQMsgs(final String sId, int index, int pageNum) {

        return new StringRequest(BaseURLUtil.getMyQQMsg(sId, index, pageNum), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray jsonArray = object.getJSONObject("datas").getJSONArray("listData");
                    int length = jsonArray.length();
                        /*
                        如果数据库中没有对应QQ会话，则加入对应QQ会话
                         */
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        QQChatInfo qqChatInfo = new QQChatInfo();
                        qqChatInfo.setTitle(obj.optString("title"));
                        qqChatInfo.setTime(obj.optString("time"));
                        qqChatInfo.setIsEnd(obj.optInt("isEnd"));
                        qqChatInfo.setConsultId(obj.optInt("consultId"));
                        qqChatInfo.setUserName(obj.optString("userName"));
                        qqChatInfo.setReplays(obj.optInt("replays"));
                        qqChatInfo.setUser_id(obj.optInt("user_id"));

                        int size = DataSupport.where("consultId = ?", String.valueOf(qqChatInfo.getConsultId())).find(QQChatInfo.class).size();
                        if (size == 0) {
                            qqChatInfo.save();
                            chatInfos.add(qqChatInfo);
                        } else {
                            qqChatInfo.updateAll("consultId = ?", String.valueOf(qqChatInfo.getConsultId()));
                        }

                        Message msg = Message.obtain();
                        msg.obj = qqChatInfo.getConsultId();
                        msg.what = 1024;
                        myHandler.sendMessage(msg);

                    }

                    chatAdapter.setList(chatInfos);

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 1:
                this.onResume();
                break;
            default:
                break;
        }
    }

    protected void doRefresh() {
        mQueue.add(getMyQQMsgs(AppContext.now_session_id, 1, 100));
    }

    protected void doGetUnreadInfo(String sId, int cId) {
        mQueue.add(getUnreadCount(sId, cId));
    }

    static class MyHandler extends Handler {
        private WeakReference<ExportChatListFragment> reference;

        public MyHandler(ExportChatListFragment activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ExportChatListFragment activity = reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        activity.doRefresh();
                        break;
                    case 1024:
                        int qq_id = (int) msg.obj;
                        activity.doGetUnreadInfo(AppContext.now_session_id, qq_id);
                        break;
                    default:
                        break;
                }
            }
        }

    }

    /**
     * 未读消息
     */
    protected StringRequest getUnreadCount(String sId, final int cId) {

        return new StringRequest(BaseURLUtil.getMyUnreadQQinfo(sId, cId), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>cid: " + cId);
//                Log.e("TAG", ">>s: " + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    int length = jsonArray.length();
                    //更新未读消息ui
                    for (int i = 0; i < chatInfos.size(); i++) {
                        if (cId == chatInfos.get(i).getConsultId()) {
                            chatInfos.get(i).setUnReadCount(length);
                            chatAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    AppContext.toastBadJson();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });
    }
}
