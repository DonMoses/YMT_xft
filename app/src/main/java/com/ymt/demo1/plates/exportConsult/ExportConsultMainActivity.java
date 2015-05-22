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

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.baseClasses.BaseActivity;
import com.ymt.demo1.beams.ConsultItem;
import com.ymt.demo1.beams.OnDutyExport;
import com.ymt.demo1.customViews.MyTitle;
import com.ymt.demo1.dbBeams.SearchString;
import com.ymt.demo1.main.PopActionListener;
import com.ymt.demo1.main.PopActionUtil;
import com.ymt.demo1.main.SearchActivity;
import com.ymt.demo1.main.SearchViewUtil;

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
    private HorizontalScrollView scrollView;
    private LinearLayout linearLayout;
    private OnDutyExport todayExport;
    private OnDutyExport tomorrowExport;
    private SearchViewUtil searchViewUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_consult_main);
        searchViewUtil = new SearchViewUtil();
        initTitle();
        initView();
    }

    protected void initTitle() {
        final MyTitle title = (MyTitle) findViewById(R.id.my_title);
        title.setTitleStyle(MyTitle.TitleStyle.RIGHT_ICON_L);
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
                    case "立即咨询":
                        startActivity(new Intent(ExportConsultMainActivity.this, ExportConsultNowActivity.class));
                        break;
                    case "我的咨询":
                        startActivity(new Intent(ExportConsultMainActivity.this, MyConsultActivity.class));
                        break;
                    case "咨询历史":
                        Toast.makeText(ExportConsultMainActivity.this, "咨询历史", Toast.LENGTH_SHORT).show();

                        break;
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
                PopActionUtil popActionUtil = PopActionUtil.getInstance(ExportConsultMainActivity.this);
                popActionUtil.setActions(new String[]{"立即咨询", "我的咨询", "咨询历史"});
                PopupWindow popupWindow = popActionUtil.getSimpleTxtPopActionMenu();
                popupWindow.showAtLocation(title.getRootView(),
                        Gravity.TOP | Gravity.END, 10, 100);

                popActionUtil.setActionListener(actionListener);

            }

            @Override
            public void onRightRClick() {
                //此视图，右边只包含L按钮

            }
        });
    }

    /**
     *
     */
    protected void initView() {
        searchViewUtil.initSearchView(this);
        scrollView = (HorizontalScrollView) findViewById(R.id.export_scroll_view);
        linearLayout = (LinearLayout) findViewById(R.id.export_linear_layout);

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
     * 今日、明日 值守专家信息
     */
    protected void initTodTomExport() {

        //todo 今日、明日专家（从最近一周值守专家中获取）
        todayExport = new OnDutyExport();
        todayExport.setName("李国栋");
        todayExport.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.moses));
        todayExport.setBirthDay("1964年8月");
        todayExport.setMajor("成都消防大队指导员");
        tomorrowExport = new OnDutyExport();
        tomorrowExport.setName("汪知武");
        tomorrowExport.setIcon(BitmapFactory.decodeResource(getResources(), R.drawable.moses));
        tomorrowExport.setBirthDay("1973年2月");
        tomorrowExport.setMajor("成都消防大队指导员");
        //设置info 到控件
        RelativeLayout todayExportView = (RelativeLayout) findViewById(R.id.today_export_layout);
        RelativeLayout tomorrowExportView = (RelativeLayout) findViewById(R.id.tomorrow_export_layout);
        ImageView todayExportIcon = (ImageView) findViewById(R.id.today_export_icon);
        TextView todayExportName = (TextView) findViewById(R.id.today_export_name);
        TextView todayExportBirth = (TextView) findViewById(R.id.today_export_birth);
        TextView todayExportMajor = (TextView) findViewById(R.id.today_export_major);
        ImageView tomorrowExportIcon = (ImageView) findViewById(R.id.tomorrow_export_icon);
        TextView tomorrowExportName = (TextView) findViewById(R.id.tomorrow_export_name);
        TextView tomorrowExportBirth = (TextView) findViewById(R.id.tomorrow_export_birth);
        TextView tomorrowExportMajor = (TextView) findViewById(R.id.tomorrow_export_major);

        todayExportIcon.setImageBitmap(todayExport.getIcon());
        todayExportName.setText("姓名：" + todayExport.getName());
        todayExportBirth.setText("生日：" + todayExport.getBirthDay());
        todayExportMajor.setText("职业：" + todayExport.getMajor());
        tomorrowExportIcon.setImageBitmap(tomorrowExport.getIcon());
        tomorrowExportName.setText("姓名：" + tomorrowExport.getName());
        tomorrowExportBirth.setText("生日：" + tomorrowExport.getBirthDay());
        tomorrowExportMajor.setText("职业：" + tomorrowExport.getMajor());

        //点击跳转到专家详情
        todayExportView.setOnClickListener(this);
        tomorrowExportView.setOnClickListener(this);
    }

    /**
     * 最近、 热门咨询信息
     */
    protected void initNearlyHotConsult() {
        //todo 最近咨询info
        ConsultItem consultItem1 = new ConsultItem();
        consultItem1.setBitmapIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img_consult_item_1));
        consultItem1.setContentTxt("最新消防摩托车的型号为春风650，从外表上看和普通摩托车区别不大。消防部门的相关负责人介绍说，这两种消防摩托车将作为一个战斗编程。");
        consultItem1.setTitle("天安门、故宫配备新型消防摩托");
        //todo 热门咨询info
        ConsultItem consultItem2 = new ConsultItem();
        consultItem2.setBitmapIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img_consult_item_2));
        consultItem2.setContentTxt("事发地点位于沙坪坝区康居西城附近，消防赶到现场时，跌落下水道的牛已经钻进了下水道中，放牛的老汉不敢贸然下去，守在下水道口束手无策。");
        consultItem2.setTitle("三百斤大牛被困下水道消防用抢险救援车将其吊出");

        //设置info 到控件
        ImageView nearlyConsultIcon = (ImageView) findViewById(R.id.nearly_consult_icon);
        TextView nearlyConsultTitle = (TextView) findViewById(R.id.nearly_consult_title);
        TextView nearlyConsultContent = (TextView) findViewById(R.id.nearly_consult_content);
        ImageView hotConsultIcon = (ImageView) findViewById(R.id.hot_consult_icon);
        TextView hotConsultTitle = (TextView) findViewById(R.id.hot_consult_title);
        TextView hotConsultContent = (TextView) findViewById(R.id.hot_consult_content);
        nearlyConsultIcon.setImageBitmap(consultItem1.getBitmapIcon());
        nearlyConsultTitle.setText(consultItem1.getTitle());
        nearlyConsultContent.setText(consultItem1.getContentTxt());
        hotConsultIcon.setImageBitmap(consultItem2.getBitmapIcon());
        hotConsultTitle.setText(consultItem2.getTitle());
        hotConsultContent.setText(consultItem2.getContentTxt());

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
        final LayoutInflater inflater = LayoutInflater.from(this);
        //获取滑动控件宽度，设置item 宽度（默认显示前4个）
        int screenW = scrollView.getWidth();

//        Log.e("TAG", "screeW/4 = " + screenW / 4);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(screenW / 4, LinearLayout.LayoutParams.MATCH_PARENT);

        //todo 网络获取最近值守专家列表
//        ArrayList<OnDutyExport> onDutyExports = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            final OnDutyExport export = new OnDutyExport();
            export.setName("徐国斌");
            export.setOnDutyDate("6月" + String.valueOf(12 + i) + "号");
//            onDutyExports.add(export);
            final View exportItem = inflater.inflate(R.layout.item_export_scroll, null);
            TextView weekDay = (TextView) exportItem.findViewById(R.id.week_day);
            TextView date = (TextView) exportItem.findViewById(R.id.date);
            TextView exportName = (TextView) exportItem.findViewById(R.id.export_name);
            //todo 背景颜色和星期

            date.setText(export.getOnDutyDate());
            exportName.setText(export.getName());
            switch (i) {
                case 0:
                    weekDay.setText("周一");
                    exportItem.setBackgroundColor(getResources().getColor(R.color.guide_cjgl));
                    break;
                case 1:
                    weekDay.setText("周二");
                    exportItem.setBackgroundColor(getResources().getColor(R.color.guide_khrd));
                    break;
                case 2:
                    weekDay.setText("周三");
                    exportItem.setBackgroundColor(getResources().getColor(R.color.guide_zyfw));
                    break;
                case 3:
                    weekDay.setText("周四");
                    exportItem.setBackgroundColor(getResources().getColor(R.color.guide_kssc));
                    break;
                case 4:
                    weekDay.setText("周五");
                    exportItem.setBackgroundColor(getResources().getColor(R.color.guide_cjgl));
                    break;
                case 5:
                    weekDay.setText("周六");
                    exportItem.setBackgroundColor(getResources().getColor(R.color.guide_khrd));
                    break;
                case 6:
                    weekDay.setText("周日");
                    exportItem.setBackgroundColor(getResources().getColor(R.color.guide_kssc));
                    break;
                default:
                    break;
            }
            exportItem.setLayoutParams(layoutParams);
            linearLayout.addView(exportItem);

            //点击进入专家信息界面
            exportItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ExportConsultMainActivity.this, ExportInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("export_info", export);
                    intent.putExtra("export_info", bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today_export_layout:
                //todo 点击跳转到专家详情界面
                Intent intent1 = new Intent(ExportConsultMainActivity.this, ExportInfoActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelable("export_info", todayExport);
                intent1.putExtra("export_info", bundle1);
                startActivity(intent1);
                break;
            case R.id.tomorrow_export_layout:
                //todo 点击跳转到专家详情界面
                Intent intent2 = new Intent(ExportConsultMainActivity.this, ExportInfoActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("export_info", tomorrowExport);
                intent2.putExtra("export_info", bundle2);
                startActivity(intent2);
                break;
            case R.id.nearly_consult_view:
                //todo 点击进入咨询详细内容界面
                Toast.makeText(ExportConsultMainActivity.this, "最近咨询", Toast.LENGTH_SHORT).show();
                break;
            case R.id.hot_consult_view:
                //todo 点击进入咨询详细内容界面
                Toast.makeText(ExportConsultMainActivity.this, "热门咨询", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * @param view：被测量的view
     */
    public static boolean checkDownPointerInView(View view, float x, float y) {
        int[] location2 = new int[2];
        view.getLocationOnScreen(location2);
        return x >= location2[0] && x <= location2[0] + view.getWidth() && y >= location2[1] && y <= location2[1] + view.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getRawX();
                float y = event.getRawY();
                if (!checkDownPointerInView(findViewById(R.id.search_edit_text), x, y)) {
                    searchViewUtil.clearInputFocus();
                    return true;
                }
        }
        return false;
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
                activity.initExportTable();
            }
        }
    }

}
