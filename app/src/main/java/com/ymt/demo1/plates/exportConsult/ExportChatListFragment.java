package com.ymt.demo1.plates.exportConsult;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.QQChatListAdapter;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportChatListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportChatFragment";
    RequestQueue mQueue;
    public QQChatListAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export_chat, container, false);
        initChatList(view);
        return view;
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

//        Bundle bundle = getArguments();
//        String emptyInfo = bundle.getString("empty_info");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mQueue = ((MyConsultActivity) activity).mQueue;
    }

    /**
     * 初始化会话界面
     */
    protected void initChatList(View view) {
        ListView chatListView = (ListView) view.findViewById(R.id.export_chat_list_view);
        chatAdapter = new QQChatListAdapter(getActivity(), mQueue);
        chatListView.setAdapter(chatAdapter);
        List<QQChatInfo> chatInfos = DataSupport.findAll(QQChatInfo.class);
        chatAdapter.setList(chatInfos);

        /*
         * todo 会话列表点击事件
         */
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "export " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ConsultChatActivity.class);
                intent.putExtra("session_id", AppContext.now_session_id);
                intent.putExtra("qq_id", ((QQChatInfo) parent.getAdapter().getItem(position)).getQq_id());
                startActivity(intent);
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
//                    Log.e("TAG", "s>>>>>>>>>>>>" + s);
                    try {
                        JSONObject object = new JSONObject(s);
                        JSONObject object1 = object.getJSONObject("datas");
                        JSONArray jsonArray = object1.getJSONArray("listData");
                        int length = jsonArray.length();
                        /*
                        如果数据库中没有对应QQ会话，则加入对应QQ会话
                         */
                        List<QQChatInfo> savedQQChatInfos = DataSupport.findAll(QQChatInfo.class);
                        List<String> savedQQIds = new ArrayList<>();
                        int qqCounts = savedQQChatInfos.size();
                        for (int i = 0; i < qqCounts; i++) {
                            savedQQIds.add(savedQQChatInfos.get(i).getQq_id());
                        }

                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String qq_id = obj.getString("id");

                            if (!(savedQQIds.contains(qq_id))) {
                                String msg_time = obj.getString("msg_time");
                                String fk_user_id = obj.getString("fk_user_id");
                                QQChatInfo qqChatInfo = new QQChatInfo();
                                qqChatInfo.setQq_id(qq_id);
                                qqChatInfo.setMsg_time(msg_time);
                                qqChatInfo.setFk_user_id(fk_user_id);
                                qqChatInfo.setCreate_time(obj.getString("create_time"));
                                qqChatInfo.setStatus(obj.getString("status"));
                                qqChatInfo.setFk_company_id(obj.getString("fk_company_id"));
                                qqChatInfo.setElec_price(obj.getString("elec_price"));
                                qqChatInfo.setFk_pro_id(obj.getString("fk_pro_id"));
                                qqChatInfo.setMsg_num(obj.getInt("msg_num"));
                                qqChatInfo.setFk_contract_id(obj.getString("fk_contract_id"));
                                qqChatInfo.save();

//                                Log.e("TAG", "qqId>>>>" + qq_id + "  msg_time>>>>" + msg_time);
//                                Log.e("TAG", "qqChatInfo size>>>>" + DataSupport.count(QQChatInfo.class));
                            }
                        }

                        List<QQChatInfo> chatInfos = DataSupport.findAll(QQChatInfo.class);
                        chatAdapter.setList(chatInfos);
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

}
