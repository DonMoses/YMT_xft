package com.ymt.demo1.plates.exportConsult.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DonMoses on 2015/11/12
 */
public class ChatMemberFragment extends Fragment {
    private static ChatMemberFragment memberFragment;
    private RequestQueue requestQueue;
    private int cId;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new ListView(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(params);
        return listView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getActivity());
        cId = getArguments().getInt("cid");
    }

    public static ChatMemberFragment getInstance(int cid) {
        if (memberFragment == null) {
            memberFragment = new ChatMemberFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt("cid", cid);
        memberFragment.setArguments(bundle);
        return memberFragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        memberList.clear();
        requestQueue.add(getChatMember(cId));
        memberAdapter = new MemberAdapter(getActivity());
        listView.setAdapter(memberAdapter);
        memberAdapter.setMemberList(memberList);
    }

    private List<Member> memberList = new ArrayList<>();
    private MemberAdapter memberAdapter;

    private StringRequest getChatMember(int cid) {
//        Log.e("TAG", ">>>>>>>>>>>.url:" + BaseURLUtil.getQqChatMember(cid));
        return new StringRequest(BaseURLUtil.getQqChatMember(cid), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("TAG", ">>>>>>>>>>>.s:" + s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.optString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Member member = new Member();
                            member.setAccount(obj.optString("loginName"));
                            member.setName(obj.optString("userName"));
                            memberList.add(member);
                            memberAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
            }
        });
    }

    class Member {
        private String account;
        private String name;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    class MemberAdapter extends BaseAdapter {
        private List<Member> memberList = new ArrayList<>();
        private LayoutInflater memInflater;

        private MemberAdapter(Context context) {
            memInflater = LayoutInflater.from(context);
        }

        public void setMemberList(List<Member> memberList) {
            this.memberList = memberList;
        }

        @Override
        public int getCount() {
            return memberList.size();
        }

        @Override
        public Object getItem(int position) {
            return memberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = memInflater.inflate(R.layout.item_chat_member, null);
                holder = new Holder();
                holder.account = (TextView) convertView.findViewById(R.id.member_account);
                holder.name = (TextView) convertView.findViewById(R.id.member_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.account.setText(memberList.get(position).getAccount());
            if (memberList.get(position).getAccount().equals(memberList.get(position).getName())) {
                holder.name.setVisibility(View.INVISIBLE);
            } else {
                holder.name.setText("(昵称: " + memberList.get(position).getName() + ")");
            }
            return convertView;
        }

        class Holder {
            private TextView account;
            private TextView name;
        }
    }


}
