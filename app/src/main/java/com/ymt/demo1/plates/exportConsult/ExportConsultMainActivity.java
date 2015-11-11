/*
 * Copyright 2015 DonMoses
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ymt.demo1.plates.exportConsult;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.expertConsult.DutyExpertAdapter;
import com.ymt.demo1.adapter.expertConsult.HotRecConsultAdapter;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.ConsultInfo;
import com.ymt.demo1.beams.expert_consult.OnDutyExpert;
import com.ymt.demo1.beams.expert_consult.PreExpert;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.main.sign.SignInUpActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;
import com.ymt.demo1.main.search.SearchActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 专家咨询界面
 */
public class ExportConsultMainActivity extends BaseActivity implements View.OnClickListener {
    private PopActionListener actionListener;
    private Handler mHandler = new MyHandler(this);
    private PreExpert todayExpert;
    private PreExpert tomorrowExpert;
    private ImageView todayExportIcon;
    private TextView todayExportName;
    private TextView todayExportCount;
    private ImageView tomorrowExportIcon;
    private TextView tomorrowExportName;
    private TextView tomorrowExportCount;

    private List<OnDutyExpert> onDutyAmExperts = new ArrayList<>();
    private List<OnDutyExpert> onDutyPmExperts = new ArrayList<>();
    private DutyExpertAdapter amAdapter;
    private DutyExpertAdapter pmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(hotConsultRequest("hot"));
        mQueue.add(recentConsultRequest("new"));
        mQueue.add(getExperts(6, 1, ""));
        mQueue.add(getOnDutyExperts());
        setContentView(R.layout.activity_export_consult_main);
        initTitle();
        initView();
    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L_R);
        title.setOnLeftActionClickListener(new MyTitle.OnLeftActionClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        actionListener = new PopActionListener() {
            @Override
            public void onAction(String action) {
                switch (action) {
                    case "我的咨询":
                        if (TextUtils.isEmpty(AppContext.now_session_id)) {
                            //先登录
                            Intent intent = new Intent(ExportConsultMainActivity.this, SignInUpActivity.class);
                            intent.putExtra("is_back_to_main", false);
                            startActivityForResult(intent, 1024);
                        } else {
                            //我的咨询
                            startActivity(new Intent(ExportConsultMainActivity.this, MyConsultActivity.class));
                        }
                        break;
//                    case "咨询历史":
//                        Toast.makeText(ExportConsultMainActivity.this, "咨询历史", Toast.LENGTH_SHORT).show();
//
//                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDismiss() {

            }
        };

        title.setOnRightActionClickListener(new MyTitle.OnRightActionClickListener() {
            @Override
            public void onRightLClick() {
                startActivity(new Intent(ExportConsultMainActivity.this, SearchActivity.class));
            }

            @Override
            public void onRightRClick() {
                PopActionUtil popActionUtil = PopActionUtil.getInstance(ExportConsultMainActivity.this);
//                popActionUtil.setActions(new String[]{"立即咨询", "我的咨询", "咨询历史"});
                popActionUtil.setActions(new String[]{"我的咨询"});
                PopupWindow popupWindow = popActionUtil.getSimpleTxtPopActionMenu();
                popupWindow.showAtLocation(title.getRootView(),
                        Gravity.TOP | Gravity.END, 10, 100);
                popActionUtil.setActionListener(actionListener);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1024 && resultCode == RESULT_OK) {
            actionListener.onAction("我的咨询");
        }
    }

    protected void initView() {
        TextView moreExpert = (TextView) findViewById(R.id.more_export);
        moreExpert.setOnClickListener(this);
        TextView dutyTime = (TextView) findViewById(R.id.nearly_export);
        String str = dutyTime.getText().toString();
        dutyTime.setText(str + "(" + getDay() + ")");
        initTodTomExport();
        initNearlyHotConsult();
        initOnDutyExpertView();
    }

    /**
     * 资深专家信息
     */
    protected void initTodTomExport() {
        //设置info 到控件
        RelativeLayout todayExportView = (RelativeLayout) findViewById(R.id.today_export_layout);
        RelativeLayout tomorrowExportView = (RelativeLayout) findViewById(R.id.tomorrow_export_layout);
        todayExportIcon = (ImageView) findViewById(R.id.today_export_icon);
        todayExportName = (TextView) findViewById(R.id.today_export_name);
        //        TextView todayExportBirth = (TextView) findViewById(R.id.today_export_birth);
        todayExportCount = (TextView) findViewById(R.id.today_export_major);
        tomorrowExportIcon = (ImageView) findViewById(R.id.tomorrow_export_icon);
        tomorrowExportName = (TextView) findViewById(R.id.tomorrow_export_name);
        //        TextView tomorrowExportBirth = (TextView) findViewById(R.id.tomorrow_export_birth);
        tomorrowExportCount = (TextView) findViewById(R.id.tomorrow_export_major);

        //点击跳转到专家详情
        todayExportView.setOnClickListener(this);
        tomorrowExportView.setOnClickListener(this);
    }

    private List<ConsultInfo> recList;
    private List<ConsultInfo> hotList;
    private HotRecConsultAdapter recAdapter;
    private HotRecConsultAdapter hotAdapter;

    protected void initNearlyHotConsult() {
        /*
      最近、 热门咨询信息
     */
        ListView recListView = (ListView) findViewById(R.id.rec_consult_list_view);
        ListView hotListView = (ListView) findViewById(R.id.hot_consult_list_view);
        recList = new ArrayList<>();
        hotList = new ArrayList<>();
        recAdapter = new HotRecConsultAdapter(this, HotRecConsultAdapter.CONSULT_MAIN_TYPE);
        hotAdapter = new HotRecConsultAdapter(this, HotRecConsultAdapter.CONSULT_MAIN_TYPE);
        recAdapter.setHotList(recList);
        hotAdapter.setHotList(hotList);
        recListView.setAdapter(recAdapter);
        hotListView.setAdapter(hotAdapter);
        TextView newListBtn = (TextView) findViewById(R.id.nearly_consult);
        TextView hotListBtn = (TextView) findViewById(R.id.hot_consult);
        newListBtn.setOnClickListener(this);
        hotListBtn.setOnClickListener(this);

        //// TODO: 2015/11/3 详情界面
        recListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultInfo consult = (ConsultInfo) recAdapter.getItem(position);
                Intent intent = new Intent(ExportConsultMainActivity.this, ConsultDetailActivity.class);
                intent.putExtra("cid", consult.getCid());
                startActivity(intent);
            }
        });

        hotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultInfo consult = (ConsultInfo) hotAdapter.getItem(position);
                Intent intent = new Intent(ExportConsultMainActivity.this, ConsultDetailActivity.class);
                intent.putExtra("cid", consult.getCid());
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化专家值守列表
     */
    protected void initOnDutyExpertView() {
        GridView amDutyView = (GridView) findViewById(R.id.am_expert_gridView);
        GridView pmDutyView = (GridView) findViewById(R.id.pm_expert_gridView);
        amAdapter = new DutyExpertAdapter(this);
        pmAdapter = new DutyExpertAdapter(this);
        amAdapter.setExpertList(onDutyAmExperts);
        pmAdapter.setExpertList(onDutyPmExperts);
        amDutyView.setAdapter(amAdapter);
        pmDutyView.setAdapter(pmAdapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OnDutyExpert expert = (OnDutyExpert) parent.getAdapter().getItem(position);
                Intent intent1 = new Intent(ExportConsultMainActivity.this, ExpertInfoActivity.class);
                intent1.putExtra("id", Integer.valueOf(expert.getFkUserId()));
                startActivity(intent1);
            }
        };
        amDutyView.setOnItemClickListener(itemClickListener);
        pmDutyView.setOnItemClickListener(itemClickListener);
    }

    private void onDutyGet() {
        amAdapter.notifyDataSetChanged();
        pmAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today_export_layout:
                // 点击跳转到专家详情界面
                if (todayExpert != null) {
                    Intent intent1 = new Intent(ExportConsultMainActivity.this, ExpertInfoActivity.class);
                    intent1.putExtra("id", todayExpert.getFkUserId());
                    startActivity(intent1);
                }
                break;
            case R.id.tomorrow_export_layout:
                // 点击跳转到专家详情界面
                if (tomorrowExpert != null) {
                    Intent intent2 = new Intent(ExportConsultMainActivity.this, ExpertInfoActivity.class);
                    intent2.putExtra("id", tomorrowExpert.getFkUserId());
                    startActivity(intent2);
                }
                break;
            case R.id.nearly_consult:
                Intent newIntent = new Intent(ExportConsultMainActivity.this, HotRecConsultListActivity.class);
                newIntent.putExtra("type", "new");
                startActivity(newIntent);
                break;
            case R.id.hot_consult:
                Intent hotIntent = new Intent(ExportConsultMainActivity.this, HotRecConsultListActivity.class);
                hotIntent.putExtra("type", "hot");
                startActivity(hotIntent);
                break;
            case R.id.more_export:
                // 更多专家
                startActivity(new Intent(ExportConsultMainActivity.this, MoreExpertActivity.class));
                break;
            default:
                break;
        }
    }

    static class MyHandler extends Handler {
        private WeakReference<ExportConsultMainActivity> reference;

        public MyHandler(ExportConsultMainActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ExportConsultMainActivity activity = reference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:          //值日表
                        activity.onDutyGet();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 热点咨询
     */
    protected StringRequest hotConsultRequest(String type) {
        return new StringRequest(BaseURLUtil.getRecentHotConsult(type) + "&pagesize=3", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ConsultInfo consultInfo = new ConsultInfo();
                            consultInfo.setCid(obj.optInt("cid"));
                            consultInfo.setTitle(obj.optString("title"));
                            hotList.add(consultInfo);
                            hotAdapter.notifyDataSetChanged();
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

    /**
     * 最新咨询
     */
    protected StringRequest recentConsultRequest(String type) {
        return new StringRequest(BaseURLUtil.getRecentHotConsult(type) + "&pagesize=3", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ConsultInfo consultInfo = new ConsultInfo();
                            consultInfo.setCid(obj.optInt("cid"));
                            consultInfo.setTitle(obj.optString("title"));

                            recList.add(consultInfo);
                            recAdapter.notifyDataSetChanged();
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

    /**
     * 获取资深专家
     */
    protected StringRequest getExperts(int pageSize, int start, String searchWho) {
        return new StringRequest(BaseURLUtil.doGetExpertList(pageSize, start, searchWho), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            PreExpert preExpert = new PreExpert();
                            preExpert.setCount(obj.optInt("count"));
                            preExpert.setUsername(obj.optString("username"));
                            preExpert.setLevel(obj.optString("level"));
                            preExpert.setFkUserId(obj.optInt("fkUserId"));
                            preExpert.setWaitCount(obj.optInt("waitCount"));
                            preExpert.setHeadImage(obj.optString("headImage"));
                            int size = DataSupport.where("fkUserId = ?", String.valueOf(preExpert.getFkUserId())).find(PreExpert.class).size();
                            if (size == 0) {
                                preExpert.save();
                            } else {
                                preExpert.updateAll("fkUserId = ?", String.valueOf(preExpert.getFkUserId()));
                            }
                        }

                        List<PreExpert> list = DataSupport.where("level = ? or level = ?", "level2", "level1").find(PreExpert.class);
                        todayExpert = list.get(0);
                        tomorrowExpert = list.get(1);
                        if (todayExpert != null && tomorrowExpert != null) {
                            Picasso.with(ExportConsultMainActivity.this).load(todayExpert.getHeadImage()).into(todayExportIcon);
                            todayExportName.setText("姓名：" + todayExpert.getUsername());
                            todayExportCount.setText(todayExpert.getCount() + "个咨询");
                            Picasso.with(ExportConsultMainActivity.this).load(tomorrowExpert.getHeadImage()).into(tomorrowExportIcon);
                            tomorrowExportName.setText("姓名：" + tomorrowExpert.getUsername());
                            tomorrowExportCount.setText(tomorrowExpert.getCount() + "个咨询");
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

    /**
     * 获取值班专家表
     */
    protected StringRequest getOnDutyExperts() {
        onDutyAmExperts.clear();
        onDutyPmExperts.clear();
        return new StringRequest(BaseURLUtil.getOnDutyExpert(), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONArray jsonArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            OnDutyExpert dutyExpert = new OnDutyExpert();
                            dutyExpert.setTheId(obj.optInt("id"));
                            dutyExpert.setDutyTime(obj.optString("dutyTime"));
                            dutyExpert.setExName(obj.optString("exName"));
                            dutyExpert.setFkUserId(obj.optString("fkUserId"));
                            dutyExpert.setOndutyDate(obj.optString("ondutyDate"));
                            if (dutyExpert.getDutyTime().equals("09:00-12:00")) {
                                onDutyAmExperts.add(dutyExpert);
                            } else {
                                onDutyPmExperts.add(dutyExpert);
                            }
                        }
                        mHandler.sendEmptyMessage(1);
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

    /**
     * 获取当前星期几
     */
    public String getDay() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
//        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
//        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "1";
        } else if ("3".equals(mWay)) {
            mWay = "2";
        } else if ("4".equals(mWay)) {
            mWay = "3";
        } else if ("5".equals(mWay)) {
            mWay = "4";
        } else if ("6".equals(mWay)) {
            mWay = "5";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }
}
