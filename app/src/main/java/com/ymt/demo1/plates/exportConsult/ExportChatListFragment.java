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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.QQChatListAdapter;
import com.ymt.demo1.beams.expert_consult.QQChatInfo;
import com.ymt.demo1.beams.expert_consult.QQMsg;
import com.ymt.demo1.main.AppContext;
import com.ymt.demo1.main.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Dan on 2015/5/13
 */
public class ExportChatListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "ExportChatFragment";
    private RequestQueue mQueue;
    public QQChatListAdapter chatAdapter;
    private ListView chatListView;

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
        chatListView = (ListView) view.findViewById(R.id.export_chat_list_view);
        chatAdapter = new QQChatListAdapter(getActivity());
        chatListView.setAdapter(chatAdapter);
        List<QQChatInfo> chatInfos = DataSupport.findAll(QQChatInfo.class);
        chatAdapter.setList(chatInfos);
        getMsgs(chatInfos);

        /*
         * todo 会话列表点击事件
         */
        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        getMsgs(chatInfos);

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

    protected void getMsgs(List<QQChatInfo> chats) {
        int length = chats.size();
        for (int i = 0; i < length; i++) {
            mQueue.add(sendQQChatMsgRequest(chats.get(i).getQq_id()));
        }
    }

    /**
     * 一个QQ会话的所有消息
     *
     * @param qq_id ： QQ会话id
     */
    protected StringRequest sendQQChatMsgRequest(final String qq_id) {
        return new StringRequest(BaseURLUtil.getMyAllQQMsgUrl(AppContext.now_session_id, qq_id), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                    JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                    int length = jsonArray.length();
                    //todo 获取所有/部分， 加入所有/部分到数据库
                    for (int i = 0; i < length; i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String content = obj.optString("content");

                        QQMsg qqMsg = new QQMsg();
                        qqMsg.setContent(content);
                        qqMsg.setPro_expert_user_id(obj.optString("pro_expert_user_id"));
                        qqMsg.setStatus(obj.optString("status"));
                        qqMsg.setFk_reply_user_id(obj.optString("fk_reply_user_id"));
                        qqMsg.setReply_time(obj.optString("reply_time"));
                        qqMsg.setReply_role(obj.optString("reply_role"));
                        qqMsg.setType(obj.optString("type"));
                        qqMsg.setReply_user_name(obj.optString("reply_user_name"));
                        String msg_id = obj.optString("id");
                        qqMsg.setMsg_id(msg_id);
                        qqMsg.setFk_qq_id(obj.optString("fk_qq_id"));
                        int size = DataSupport.where("msg_id = ?", msg_id).find(QQMsg.class).size();
                        if (size == 0) {
                            qqMsg.save();
//                            Log.e("TAG", ">>>>>>>>>>>>>>>save>>>>>>>>>" + qqMsg.getMsg_id());
                        } else {
                            qqMsg.updateAll("msg_id = ?", msg_id);
//                            Log.e("TAG", ">>>>>>>>>>>>>>>update>>>>>>>>>" + qqMsg.getMsg_id());
                        }
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
}
