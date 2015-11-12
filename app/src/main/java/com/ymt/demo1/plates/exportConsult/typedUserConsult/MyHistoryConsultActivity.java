package com.ymt.demo1.plates.exportConsult.typedUserConsult;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.MyHistoryConsultAdapter;
import com.ymt.demo1.baseClasses.BaseFloatActivity;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.plates.exportConsult.chat.ConsultChatActivity;
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
public class MyHistoryConsultActivity extends BaseFloatActivity {
    private RequestQueue mQueue;
    private PullToRefreshListView listView;
    private int index;
    private MyHistoryConsultAdapter historyConsultAdapter;
    private List<HisConItem> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        mList = new ArrayList<>();
        index = 1;
        setContentView(R.layout.activity_my_his_consult);
        initTitle();
        initView();
    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.LEFT_ICON);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

    }

    private int date;

    private void initView() {
        date = 7;
        Spinner timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        listView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_list_view);
        ProgressBar progressBar = new ProgressBar(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(AppContext.screenWidth / 8, AppContext.screenWidth / 8);
        progressBar.setLayoutParams(layoutParams);
        listView.setEmptyView(progressBar);
        //一周=7  一月=1  三月=3  半年=6
        String[] timeArray = new String[]{"1周内", "30天内", "90天内", "半年内"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, timeArray);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int theDate = 0;
                switch (position) {
                    case 0:
                        theDate = 7;
                        break;
                    case 1:
                        theDate = 1;
                        break;
                    case 2:
                        theDate = 3;
                        break;
                    case 3:
                        theDate = 6;
                        break;
                    default:
                        break;
                }

                if (date == theDate) {
                    return;
                }
                date = theDate;
                //todo 刷新查询结果
                sendRequest();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        historyConsultAdapter = new MyHistoryConsultAdapter(this);
        listView.setAdapter(historyConsultAdapter);
        historyConsultAdapter.setList(mList);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    index = 1;
                    jsonStr = null;
                    mList.clear();
                    sendRequest();
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (AppContext.internetAvialable()) {
                    index++;
                    sendRequest();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MyHistoryConsultActivity.this, ConsultChatActivity.class);
                intent.putExtra("session_id", AppContext.now_session_id);
                intent.putExtra("cId", ((HisConItem) parent.getAdapter().getItem(position)).getConsultId());
                intent.putExtra("title", ((HisConItem) parent.getAdapter().getItem(position)).getTitle());
                startActivity(intent);
                MyHistoryConsultActivity.this.finish();
            }
        });
        sendRequest();

    }

    private void sendRequest() {
        if (AppContext.now_user_id == 0) {
            // 先登录
            Intent intent = new Intent(this, SignInUpActivity.class);
            intent.putExtra("is_back_to_main", false);
            startActivityForResult(intent, 1024);
            finish();
        } else {
            mQueue.add(getHistoryConList(AppContext.now_session_id, date, index));
        }

    }

    //获取咨询历史
    private String jsonStr = null;

    private StringRequest getHistoryConList(String sId, int date, int index) {
        return new StringRequest(BaseURLUtil.getMyHistoryConsult(sId, date, index), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if ((TextUtils.isEmpty(jsonStr)) || ((!TextUtils.isEmpty(jsonStr)) && (!jsonStr.equals(s)))) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("result").equals("Y")) {
                            JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                            int length = jsonArray.length();
                            for (int i = 0; i < length; i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                HisConItem conItem = new HisConItem();
                                conItem.setConsultId(obj.optInt("consultId"));
                                conItem.setContent(obj.optString("content"));
                                conItem.setCreateTime(obj.optString("createTime"));
                                conItem.setTitle(obj.optString("title"));
                                conItem.setType(obj.optString("type"));

                                mList.add(conItem);
                                historyConsultAdapter.notifyDataSetChanged();
                            }

                        }
                    } catch (JSONException e) {
                        AppContext.toastBadJson();
                    }
                }
                listView.onRefreshComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AppContext.toastBadInternet();
                listView.onRefreshComplete();
            }
        });
    }

    public class HisConItem {
        private int consultId;
        private String content;
        private String createTime;
        private String title;
        private String type;

        public int getConsultId() {
            return consultId;
        }

        public void setConsultId(int consultId) {
            this.consultId = consultId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
