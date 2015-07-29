package com.ymt.demo1.plates.exportConsult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.QQChatListAdapter;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.launchpages.QQMsgService;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportChatListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportChatFragment";
    private RequestQueue mQueue;
    public QQChatListAdapter chatAdapter;
    private int start = 1;
    private PullToRefreshListView qqListView;
    private List<QQChatInfo> chatInfos;

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
        Intent intent = new Intent(getActivity(), QQMsgService.class);
        getActivity().startService(intent);

        qqListView.setRefreshing(true);

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

        qqListView.getRefreshableView().setSelection(qqListView.getBottom());

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
//        chatInfos.addAll(DataSupport.findAll(QQChatInfo.class));
//        Bundle bundle = getArguments();
//        String emptyInfo = bundle.getString("empty_info");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueue = ((MyConsultActivity) activity).mQueue;
    }

    @Override
    public void onResume() {
        super.onResume();
        qqListView.setRefreshing(true);
    }

    /**
     * 初始化会话界面
     */
    protected void initChatList(View view) {
        qqListView = (PullToRefreshListView) view.findViewById(R.id.export_chat_list_view);
        chatAdapter = new QQChatListAdapter(getActivity());
        qqListView.setAdapter(chatAdapter);
        chatAdapter.setList(chatInfos);
//        getMsgs(chatInfos);

        /*
         * todo 会话列表点击事件
         */
        qqListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "export " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ConsultChatActivity.class);
                intent.putExtra("session_id", AppContext.now_session_id);
                intent.putExtra("qq_id", ((QQChatInfo) parent.getAdapter().getItem(position)).getQq_id());
                intent.putExtra("title", ((QQChatInfo) parent.getAdapter().getItem(position)).getMsg_title());
                startActivityForResult(intent, 1);
                startActivity(intent);
            }
        });

        qqListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                start = 1;
                chatInfos.clear();
                chatAdapter.setList(chatInfos);
                mQueue.add(getMyQQMsgs());
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                start++;
                mQueue.add(getMyQQMsgs());
            }
        });

        mQueue.add(getMyQQMsgs());

    }

    /**
     * 获取QQ会话列表
     */
    protected StringRequest getMyQQMsgs() {
        String saveSID = AppContext.now_session_id;
        StringRequest stringRequest = null;
        if (!TextUtils.isEmpty(saveSID)) {
            stringRequest = new StringRequest(BaseURLUtil.getMyQQMsgs(saveSID, start), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONObject object1 = object.getJSONObject("datas");
                        JSONArray jsonArray = object1.getJSONArray("listData");
                        int length = jsonArray.length();
                        /*
                        如果数据库中没有对应QQ会话，则加入对应QQ会话
                         */
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            QQChatInfo qqChatInfo = new QQChatInfo();
                            String qq_id = obj.optString("id");
                            qqChatInfo.setQq_id(qq_id);
                            qqChatInfo.setMsg_title(obj.optString("msg_title"));
                            qqChatInfo.setMsg_time(obj.optString("msg_time"));
                            qqChatInfo.setFk_user_id(obj.optString("fk_user_id"));
                            qqChatInfo.setCreate_time(obj.optString("create_time"));
                            qqChatInfo.setStatus(obj.optString("status"));
                            qqChatInfo.setFk_company_id(obj.optString("fk_company_id"));
                            qqChatInfo.setElec_price(obj.optString("elec_price"));
                            qqChatInfo.setFk_pro_id(obj.optString("fk_pro_id"));
                            qqChatInfo.setMsg_num(obj.optInt("msg_num"));
                            qqChatInfo.setFk_contract_id(obj.optString("fk_contract_id"));
//                            int size = DataSupport.where("qq_id = ?", qq_id).find(QQChatInfo.class).size();
//                            if (size == 0) {
//                                qqChatInfo.save();
//                                chatInfos.add(qqChatInfo);
//                            } else {
//                                qqChatInfo.updateAll("qq_id = ?", qq_id);
//                            }
                            chatInfos.add(qqChatInfo);

                        }

                        chatAdapter.setList(chatInfos);
//                        getMsgs(chatInfos);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    qqListView.onRefreshComplete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    qqListView.onRefreshComplete();
                }
            });
        }
        return stringRequest;

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
        qqListView.setRefreshing(true);
    }

    protected void doRefresh() {
        if (!qqListView.isRefreshing() && !qqListView.isScrollingWhileRefreshingEnabled()) {
            mQueue.add(getMyQQMsgs());
        }

//        infoListView.getRefreshableView().setSelection(infoListView.getBottom());
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
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 未读消息
     */
    protected void unreadMsg(String qq_id) {
        final String urlStr = BaseURLUtil.getMyUnreadQQMsgUrl(AppContext.now_session_id, qq_id, AppContext.now_user_id);
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(300000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            InputStream ins = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
//            Log.e("TAG", " >>>>>>>>>>>>>> s>>>>>>>>>>" + response);


            try {
                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                int unReadCount = jsonObject1.getInt("size");
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putInt(qq_id, unReadCount);
//                editor.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("TAG", ">>>>>>>>>>>>.error>>>>>>>>>>>" + e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
