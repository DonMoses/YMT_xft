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
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.expert_consult.Expert;
import com.ymt.demo1.beams.expert_consult.HotConsult;
import com.ymt.demo1.beams.expert_consult.RecentConsult;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.utils.PopActionListener;
import com.ymt.demo1.utils.PopActionUtil;
import com.ymt.demo1.utils.StringUtils;
import com.ymt.demo1.main.search.SearchActivity;
import com.ymt.demo1.main.sign.SignInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 专家咨询界面
 */
public class ExportConsultMainActivity extends BaseActivity implements View.OnClickListener {
    private PopActionListener actionListener;
    private Handler mHandler = new MyHandler(this);
    private RequestQueue mQueue;
    private TextView nearlyConsultTitle;
    TextView nearlyConsultContent;
    TextView hotConsultTitle;
    TextView hotConsultContent;
    TextView hotConsultTime;
    TextView nearlyConsultTime;
    private Expert todayExpert;
    private Expert tomorrowExpert;
    private ImageView todayExportIcon;
    private TextView todayExportName;
    private TextView todayExportMajor;
    private ImageView tomorrowExportIcon;
    private TextView tomorrowExportName;
    private TextView tomorrowExportMajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(this);
        mQueue.add(hotConsultRequest(1, 10));
        mQueue.add(recentConsultRequest(1, 10));
        mQueue.add(getExperts(6, 1, ""));
        setContentView(R.layout.activity_export_consult_main);
        initTitle();
        initView();
    }

    @Override
    protected void onDestroy() {
        DataSupport.deleteAll(HotConsult.class);
        DataSupport.deleteAll(RecentConsult.class);
        super.onDestroy();
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
//                    case "立即咨询":
//                        startActivity(new Intent(ExportConsultMainActivity.this, ExportConsultNowActivity.class));
//                        break;
                    case "我的咨询":
                        if (TextUtils.isEmpty(AppContext.now_session_id)) {
                            //先登录
                            startActivity(new Intent(ExportConsultMainActivity.this, SignInActivity.class));
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

    protected void initView() {
        TextView moreExpert = (TextView) findViewById(R.id.more_export);
        moreExpert.setOnClickListener(this);
        /**
         * 先加载view，然后测量才能获得视图尺寸
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();

        initTodTomExport();

        initNearlyHotConsult();


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
        todayExportMajor = (TextView) findViewById(R.id.today_export_major);
        tomorrowExportIcon = (ImageView) findViewById(R.id.tomorrow_export_icon);
        tomorrowExportName = (TextView) findViewById(R.id.tomorrow_export_name);
        //        TextView tomorrowExportBirth = (TextView) findViewById(R.id.tomorrow_export_birth);
        tomorrowExportMajor = (TextView) findViewById(R.id.tomorrow_export_major);

        //点击跳转到专家详情
        todayExportView.setOnClickListener(this);
        tomorrowExportView.setOnClickListener(this);
    }

    /**
     * 最近、 热门咨询信息
     */
    protected void initNearlyHotConsult() {
        //todo 最近咨询info
        RecentConsult recentConsult = DataSupport.findFirst(RecentConsult.class);
        // todo 热门咨询info
        HotConsult hotConsult = DataSupport.findFirst(HotConsult.class);

        //设置info 到控件
        nearlyConsultTitle = (TextView) findViewById(R.id.nearly_consult_title);
        nearlyConsultContent = (TextView) findViewById(R.id.nearly_consult_content);
        hotConsultTitle = (TextView) findViewById(R.id.hot_consult_title);
        hotConsultContent = (TextView) findViewById(R.id.hot_consult_content);
        hotConsultTime = (TextView) findViewById(R.id.hot_consult_time);
        nearlyConsultTime = (TextView) findViewById(R.id.nearly_consult_time);

        if (recentConsult != null) {
            nearlyConsultTitle.setText(recentConsult.getArticle_title());
            nearlyConsultContent.setText(StringUtils.replaceBlank(Html.fromHtml(recentConsult.getContent()).toString()));
            nearlyConsultTime.setText(recentConsult.getCreate_time().substring(0, 10));
        }
        if (hotConsult != null) {
            hotConsultTitle.setText(hotConsult.getArticle_title());
            hotConsultContent.setText(StringUtils.replaceBlank(Html.fromHtml(hotConsult.getContent()).toString()));
            hotConsultTime.setText(hotConsult.getCreate_time().substring(0, 10));
        }

        //点击进入咨询详细内容界面
        RelativeLayout nearlyConsultView = (RelativeLayout) findViewById(R.id.nearly_consult_view);
        RelativeLayout hotConsultView = (RelativeLayout) findViewById(R.id.hot_consult_view);
        nearlyConsultView.setOnClickListener(this);
        hotConsultView.setOnClickListener(this);

    }

    /**
     * 初始化专家值守列表
     */
    protected void initExportTable() {
        GridView amDutyView = (GridView) findViewById(R.id.am_expert_gridView);
        GridView pmDutyView = (GridView) findViewById(R.id.pm_expert_gridView);
        DutyExpertAdapter amAdapter = new DutyExpertAdapter(this);
        DutyExpertAdapter pmAdapter = new DutyExpertAdapter(this);
        amDutyView.setAdapter(amAdapter);
        pmDutyView.setAdapter(pmAdapter);
        List<Expert> experts = DataSupport.findAll(Expert.class);
        int length = experts.size();
        List<Expert> pmExperts = new ArrayList<>();
        List<Expert> amExperts = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (i < 3) {
                amExperts.add(experts.get(i));
            } else if (i < 8) {
                pmExperts.add(experts.get(i));
            }
        }

        amAdapter.setExpertList(amExperts);
        pmAdapter.setExpertList(pmExperts);

        mHandler.sendEmptyMessage(1);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expert expert = (Expert) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ExportConsultMainActivity.this, ExpertInfoActivity.class);
                intent.putExtra("expert_info", expert);
                startActivity(intent);
            }
        };
        amDutyView.setOnItemClickListener(itemClickListener);
        pmDutyView.setOnItemClickListener(itemClickListener);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today_export_layout:
                // 点击跳转到专家详情界面
                if (todayExpert != null) {
                    Intent intent1 = new Intent(ExportConsultMainActivity.this, ExpertInfoActivity.class);
                    intent1.putExtra("expert_info", todayExpert);
                    startActivity(intent1);
                }
                break;
            case R.id.tomorrow_export_layout:
                // 点击跳转到专家详情界面
                if (tomorrowExpert != null) {
                    Intent intent2 = new Intent(ExportConsultMainActivity.this, ExpertInfoActivity.class);
                    intent2.putExtra("expert_info", tomorrowExpert);
                    startActivity(intent2);
                }
                break;
            case R.id.nearly_consult_view:
                startActivity(new Intent(ExportConsultMainActivity.this, RecentConsultListActivity.class));
                break;
            case R.id.hot_consult_view:
                startActivity(new Intent(ExportConsultMainActivity.this, HotConsultListActivity.class));
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
                    case 0:
                        activity.initExportTable();
                        break;
                    case 1:          //值日表
                        ((LinearLayout) activity.findViewById(R.id.duty_parent_view)).getChildAt(1).setVisibility(View.GONE);
                        break;
                    case 2:         //专家表
                        ((LinearLayout) activity.findViewById(R.id.expert_parent_view)).getChildAt(0).setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected StringRequest hotConsultRequest(int start, int pageSize) {
//        Log.e("TAG", ">>>>hot>>>>>>>>.url>>>>>>>>>>>>>" + BaseURLUtil.doGetHotConsult(start, pageSize));
        return new StringRequest(BaseURLUtil.doGetHotConsult(start, pageSize), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonObject1.getInt("size");
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            HotConsult consult = new HotConsult();
                            consult.setGjc(obj.optString("gjc"));
                            consult.setCreate_time(obj.optString("create_time"));
                            consult.setStatus(obj.optString("status"));
                            consult.setArticle_title(obj.optString("article_title"));
                            consult.setContent(obj.optString("content"));
                            consult.setFk_consult_user_id(obj.optString("fk_consult_user_id"));
                            consult.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            consult.setFk_expert_id(obj.optString("fk_expert_id"));
                            consult.setGg(obj.optString("gg"));
                            consult.setHitnum(obj.optString("hitnum"));
                            String id = obj.optString("id");
                            consult.setThe_id(id);
                            consult.setJz(obj.optString("jz"));
                            consult.setZy(obj.optString("zy"));
                            consult.setIshot(obj.optString("ishot"));
                            int size = DataSupport.where("the_id = ?", id).find(HotConsult.class).size();
                            if (size == 0) {
                                consult.save();
                            } else {
                                consult.updateAll("the_id = ?", id);
                            }

                            if (i == 0) {
                                hotConsultTitle.setText(consult.getArticle_title());
                                hotConsultContent.setText(StringUtils.replaceBlank(Html.fromHtml(consult.getContent()).toString()));
                                hotConsultTime.setText(consult.getCreate_time().substring(0, 10));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ExportConsultMainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected StringRequest recentConsultRequest(int start, int pageSize) {
//        Log.e("TAG", ">>>>recent>>>>>>>>.url>>>>>>>>>>>>>" + BaseURLUtil.doGetRecentConsult(start, pageSize));
        return new StringRequest(BaseURLUtil.doGetRecentConsult(start, pageSize), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonObject1.getInt("size");
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            RecentConsult consult = new RecentConsult();
                            consult.setGjc(obj.optString("gjc"));
                            consult.setCreate_time(obj.optString("create_time"));
                            consult.setStatus(obj.optString("status"));
                            consult.setArticle_title(obj.optString("article_title"));
                            consult.setContent(obj.optString("content"));
                            consult.setFk_consult_user_id(obj.optString("fk_consult_user_id"));
                            consult.setFk_create_user_id(obj.optString("fk_create_user_id"));
                            consult.setFk_expert_id(obj.optString("fk_expert_id"));
                            consult.setGg(obj.optString("gg"));
                            consult.setHitnum(obj.optString("hitnum"));
                            String id = obj.optString("id");
                            consult.setThe_id(id);
                            consult.setJz(obj.optString("jz"));
                            consult.setZy(obj.optString("zy"));
                            consult.setIshot(obj.optString("ishot"));
                            int size = DataSupport.where("the_id = ?", id).find(RecentConsult.class).size();
//                            Log.e("TAG", ">>>>>>>>>>recent id>>>>>>>>>>>" + id);
                            if (size == 0) {
                                consult.save();
                            } else {
                                consult.updateAll("the_id = ?", id);
                            }

                            if (i == 0) {
                                nearlyConsultTitle.setText(consult.getArticle_title());
                                nearlyConsultContent.setText(StringUtils.replaceBlank(Html.fromHtml(consult.getContent()).toString()));
                                nearlyConsultTime.setText(consult.getCreate_time().substring(0, 10));
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ExportConsultMainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected StringRequest getExperts(int pageSize, int start, String searchWho) {
        return new StringRequest(BaseURLUtil.doGetExpertList(pageSize, start, searchWho), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("result").equals("Y")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("datas");
                        JSONArray jsonArray = jsonObject1.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Expert expert = new Expert();
                            String id = obj.optString("id");
                            expert.setThe_id(id);
                            expert.setCount(obj.optString("count"));
                            expert.setPro_life(obj.optString("pro_life"));
                            expert.setHead_pic(BaseURLUtil.BASE_URL + obj.optString("head_pic"));
                            expert.setTel(obj.optString("tel"));
                            expert.setFk_user_id(obj.optString("fk_user_id"));
                            expert.setAddr(obj.optString("addr"));
                            expert.setEducation(obj.optString("education"));
                            expert.setReporting_methods(obj.optString("reporting_methods"));
                            expert.setHome_zip_code(obj.optString("home_zip_code"));
                            expert.setPolitics(obj.optString("politics"));
                            expert.setQualification(obj.optString("qualification"));
                            expert.setLevel(obj.optString("level"));
                            expert.setCapacity(obj.optString("capacity"));
                            expert.setExperience(obj.optString("experience"));
                            expert.setIndustry(obj.optString("industry"));
                            expert.setNote(obj.optString("note"));
                            expert.setWork_addr(obj.optString("work_addr"));
                            expert.setOthers(obj.optString("others"));
                            expert.setId_number(obj.optString("id_number"));
                            expert.setHome_addr(obj.optString("home_addr"));
                            expert.setUser_name(obj.optString("user_name"));
                            expert.setSchool(obj.optString("school"));
                            expert.setDegree(obj.optString("degree"));
                            expert.setMajor_works(obj.optString("major_works"));
                            expert.setWork_zip_code(obj.optString("work_zip_code"));
                            expert.setCreate_time(obj.optString("create_time"));
                            expert.setPosition_title(obj.optString("position_title"));
                            expert.setWork_experience(obj.optString("work_experience"));
                            expert.setWork_name(obj.optString("work_name"));

                            int size = DataSupport.where("the_id = ?", id).find(Expert.class).size();
                            if (size == 0) {
                                expert.save();
                            } else {
                                expert.updateAll("the_id = ?", id);
                            }
                        }

                        todayExpert = DataSupport.findFirst(Expert.class);
                        tomorrowExpert = DataSupport.findLast(Expert.class);
                        if (todayExpert != null && tomorrowExpert != null) {
                            Picasso.with(ExportConsultMainActivity.this).load(todayExpert.getHead_pic()).into(todayExportIcon);
                            todayExportName.setText("姓名：" + todayExpert.getUser_name());
                            todayExportMajor.setText("职业：" + todayExpert.getMajor_works());
                            Picasso.with(ExportConsultMainActivity.this).load(tomorrowExpert.getHead_pic()).into(tomorrowExportIcon);
                            tomorrowExportName.setText("姓名：" + tomorrowExpert.getUser_name());
                            tomorrowExportMajor.setText("职业：" + tomorrowExpert.getMajor_works());
                        }

                        mHandler.sendEmptyMessage(2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ExportConsultMainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
