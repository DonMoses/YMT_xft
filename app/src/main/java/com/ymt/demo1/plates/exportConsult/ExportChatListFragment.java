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
import com.ymt.demo1.adapter.QQChatListAdapter;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.launchpages.QQMsgService;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportChatListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportChatFragment";
    private RequestQueue mQueue;
    public QQChatListAdapter chatAdapter;
    private int start;
    private PullToRefreshListView qqListView;

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
        start = 1;
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
        List<QQChatInfo> chatInfos = DataSupport.findAll(QQChatInfo.class);
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

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

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
            stringRequest = new StringRequest(BaseURLUtil.getMyQQMsgs(saveSID), new Response.Listener<String>() {
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
                            int size = DataSupport.where("qq_id = ?", qq_id).find(QQChatInfo.class).size();
                            if (size == 0) {
                                qqChatInfo.save();
                            } else {
                                qqChatInfo.updateAll("qq_id = ?", qq_id);
                            }
                        }

                        List<QQChatInfo> chatInfos = DataSupport.findAll(QQChatInfo.class);
                        chatAdapter.setList(chatInfos);
//                        getMsgs(chatInfos);

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
}
