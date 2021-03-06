package com.ymt.demo1.mainStyles;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.CyclePagerAdapter;
import com.ymt.demo1.beams.consult_cato.ConsultCato;
import com.ymt.demo1.customViews.IndicatorView;
import com.ymt.demo1.main.ShareActivity;
import com.ymt.demo1.main.advice.AdviceActivity;
import com.ymt.demo1.main.collect.CollectActivity;
import com.ymt.demo1.main.help.HelpActivity;
import com.ymt.demo1.main.search.FullSearchActivity;
import com.ymt.demo1.main.setting.ManageAppearanceActivity;
import com.ymt.demo1.main.setting.SettingActivity;
import com.ymt.demo1.plates.MoreCatoActivity;
import com.ymt.demo1.plates.consultCato.CatoConsultListActivity;
import com.ymt.demo1.plates.consultCato.ConsultCatoMainActivity;
import com.ymt.demo1.plates.eduPlane.EduMainActivity;
import com.ymt.demo1.plates.exportConsult.ExportConsultMainActivity;
import com.ymt.demo1.plates.hub.FireHubMainActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeMainActivity;
import com.ymt.demo1.plates.news.NewsTabActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 2015/5/8
 */
public class TabMenuActivity extends ActionBarActivity implements ManageAppearanceActivity.StyleChangeListener, View.OnClickListener, View.OnTouchListener {

    private static boolean ALWAYS_ON = true;
    private static final int SHOW_NEXT_PAGE = 0;
    private ViewPager adViewPager;
    private final MyHandler myHandler = new MyHandler(this);
    public static ManageAppearanceActivity.StyleChangeListener styleChangeListener;
    private boolean doAutoChange;

    private ImageView signIcon, adviceIcon, helpIcon, settingIcon, collectionIcon;
    private TextView signText, adviceText, helpText, settingText, collectionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(getCatoRequest(ConsultCatoMainActivity.CATO_JZ
                , ConsultCatoMainActivity.CATO_ZY
                , ConsultCatoMainActivity.CATO_KW));
        styleChangeListener = this;
        doAutoChange = true;
        setContentView(R.layout.activity_tab_menu);
        initView();
    }

    protected void initView() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        toolbar.setLogo(R.drawable.logo_tb_rect);
        // toolbar.setSubtitle("副标题");
        setSupportActionBar(toolbar);
        /* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
        // getSupportActionBar().setTitle("标题");
        // getSupportActionBar().setSubtitle("副标题");
        // getSupportActionBar().setLogo(R.drawable.ic_launcher);

        /* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理 */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_settings:
//                        Toast.makeText(NavigationMenuActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_share:
//                        Toast.makeText(NavigationMenuActivity.this, "action_share", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initAdViewPager();
        initMainView();
    }

    protected void initGate() {
        View gateView = findViewById(R.id.gate);
        //顶部板块入口
        initPagerGate(gateView);
    }

    /**
     * 初始化顶部广告ViewPager
     */
    protected void initAdViewPager() {
        adViewPager = (ViewPager) findViewById(R.id.ad_viewPager);

        /*
        设置适配器
         */
        final CyclePagerAdapter adPagerAdapter = new CyclePagerAdapter();
        adViewPager.setAdapter(adPagerAdapter);

        /*
        更新数据源（Views）
         */
        LayoutInflater inflater = LayoutInflater.from(this);
        final ArrayList<View> views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.banner_page_5, null));
        views.add(inflater.inflate(R.layout.banner_page_1, null));
        views.add(inflater.inflate(R.layout.banner_page_2, null));
        views.add(inflater.inflate(R.layout.banner_page_3, null));
        views.add(inflater.inflate(R.layout.banner_page_4, null));
        views.add(inflater.inflate(R.layout.banner_page_5, null));
        views.add(inflater.inflate(R.layout.banner_page_1, null));

        adPagerAdapter.setViews(views);
        //指示器Indicator
        final IndicatorView indicator = (IndicatorView) findViewById(R.id.myPointIndicator);
        indicator.updateTotal(adPagerAdapter.getCount() - 2);   //设置指示器显示item个数（适配adapter中元素个数）
        indicator.setCurr(0);

        /*
        pager 滑动事件
         */
        adViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageIndex = position;
                int indicatorIndex;
                if (position == 0) {
                    pageIndex = views.size() - 2;
                    indicatorIndex = adPagerAdapter.getCount() - 2 - 1;
                } else if (position == views.size() - 1) {
                    pageIndex = 1;
                    indicatorIndex = 0;
                } else {
                    indicatorIndex = position - 1;
                }
                adViewPager.setCurrentItem(pageIndex, false);
                indicator.setCurr(indicatorIndex);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        doAutoChange = false;
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        doAutoChange = true;
                        break;
                    default:
                        break;

                }
            }
        });

        /*
        开启线程，让使Viewpager轮播
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (ALWAYS_ON) {

                    if (adPagerAdapter.getCount() == 0) {
                        continue;
                    }

                    if (doAutoChange) {

                        int toPosition;
                        int curPosition = adViewPager.getCurrentItem();
                        toPosition = curPosition + 1;

                        Message msg = Message.obtain();
                        msg.what = SHOW_NEXT_PAGE;
                        msg.arg1 = toPosition;
                        myHandler.sendMessage(msg);

                        try {
                            Thread.sleep(6000);         //每6 s切换到下一page
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();

    }

    protected void initMainView() {
        //板块view
        initGate();
        //咨询分类列表数据
        setCatoView();
        //底部tab
        initTab();
    }

    /**
     * banner中的各个板块入口
     */
    private void initPagerGate(View view) {
        //资讯  入口
        View newsBtn = view.findViewById(R.id.news_layout);
        //专家咨询 入口
        View exportBtn = view.findViewById(R.id.expert_layout);
        //教育平台 入口
        View eduBtn = view.findViewById(R.id.edu_layout);
        //知识平台 入口
        View knowledgeBtn = view.findViewById(R.id.knowledge_layout);
        //论坛 入口
        View hubBtn = view.findViewById(R.id.hub_layout);
        //更多 入口
        View moreBtn = view.findViewById(R.id.more_layout);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.news_layout:
                        startActivity(new Intent(TabMenuActivity.this, NewsTabActivity.class));             //资讯平台
                        break;
                    case R.id.expert_layout:
                        startActivity(new Intent(TabMenuActivity.this, ExportConsultMainActivity.class));    //专家咨询
                        break;
                    case R.id.edu_layout:
                        startActivity(new Intent(TabMenuActivity.this, EduMainActivity.class));              //教育平台
                        break;
                    case R.id.knowledge_layout:
                        startActivity(new Intent(TabMenuActivity.this, KnowledgeMainActivity.class));        //知识平台
                        break;
                    case R.id.hub_layout:
                        startActivity(new Intent(TabMenuActivity.this, FireHubMainActivity.class));      //论坛
                        break;
                    case R.id.more_layout:
                        startActivity(new Intent(TabMenuActivity.this, MoreCatoActivity.class));             //更多
                        break;
                    default:
                        break;

                }
            }
        };
        newsBtn.setOnClickListener(onClickListener);
        exportBtn.setOnClickListener(onClickListener);
        moreBtn.setOnClickListener(onClickListener);
        eduBtn.setOnClickListener(onClickListener);
        knowledgeBtn.setOnClickListener(onClickListener);
        hubBtn.setOnClickListener(onClickListener);
    }

    /**
     * 底部tab
     */
    private void initTab() {
        View menuShare = findViewById(R.id.share_layout);
        View menuAdvice = findViewById(R.id.advice_layout);
        View menuHelp = findViewById(R.id.help_layout);
        View menuSetting = findViewById(R.id.setting_layout);
        View menuCollect = findViewById(R.id.collect_layout);

        signIcon = (ImageView) menuShare.findViewById(R.id.share_icon);
        signText = (TextView) menuShare.findViewById(R.id.share_text);
        adviceIcon = (ImageView) menuAdvice.findViewById(R.id.advice_icon);
        adviceText = (TextView) menuAdvice.findViewById(R.id.advice_text);
        helpIcon = (ImageView) menuHelp.findViewById(R.id.help_icon);
        helpText = (TextView) menuHelp.findViewById(R.id.help_text);
        settingIcon = (ImageView) menuSetting.findViewById(R.id.setting_icon);
        settingText = (TextView) menuSetting.findViewById(R.id.setting_text);
        collectionIcon = (ImageView) findViewById(R.id.collect_icon);
        collectionText = (TextView) findViewById(R.id.collect_text);

        menuShare.setOnTouchListener(this);
        menuAdvice.setOnTouchListener(this);
        menuHelp.setOnTouchListener(this);
        menuSetting.setOnTouchListener(this);
        menuCollect.setOnTouchListener(this);
        menuShare.setOnClickListener(this);
        menuAdvice.setOnClickListener(this);
        menuHelp.setOnClickListener(this);
        menuSetting.setOnClickListener(this);
        menuCollect.setOnClickListener(this);
    }

    /**
     * 引入菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onSearchBtnClicked(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "action_search":
                startActivity(new Intent(this, FullSearchActivity.class));
                break;
        }
    }

    public void onScanBtnClicked(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "action_scan":
                Toast.makeText(this, "扫二维码", Toast.LENGTH_SHORT).show();
                //todo 二维码扫描
                Intent scanIntent = new Intent(TabMenuActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, 12345);
                break;
        }
    }

    /**
     * ViewPager轮播
     */
    protected void autoNextPage(int toPosition) {
        adViewPager.setCurrentItem(toPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_layout:
                //跳转到注册界面
                startActivity(new Intent(this, ShareActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.advice_layout:
                //跳转到意见反馈
                startActivity(new Intent(this, AdviceActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.help_layout:
                //跳转到帮助中心
                startActivity(new Intent(this, HelpActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.setting_layout:
                //跳转到设置
                startActivity(new Intent(this, SettingActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                break;
            case R.id.collect_layout:
                //跳转到收藏
                startActivity(new Intent(this, CollectActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                //todo 这里放入一个测试界面，方便开发中测验。 最后修改为收藏界面

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /*
        触摸时改变tab 背景 和字体颜色
         */
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.share_layout:
                    signIcon.setImageResource(R.drawable.icon_share_grey);
                    signText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.advice_layout:
                    adviceIcon.setImageResource(R.drawable.icon_suggest);
                    adviceText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.help_layout:
                    helpIcon.setImageResource(R.drawable.icon_help);
                    helpText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.setting_layout:
                    settingIcon.setImageResource(R.drawable.icon_setup);
                    settingText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    break;
                case R.id.collect_layout:
                    collectionIcon.setImageResource(R.drawable.icon_collect);
                    collectionText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                default:
                    break;
            }

        }

        /*
        触摸释放后改变 tab背景和 字体颜色
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.share_layout:
                    signIcon.setImageResource(R.drawable.icon_share_click);
                    signText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.advice_layout:
                    adviceIcon.setImageResource(R.drawable.icon_suggest_click);
                    adviceText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.help_layout:
                    helpIcon.setImageResource(R.drawable.icon_help_cilck);
                    helpText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.setting_layout:
                    settingIcon.setImageResource(R.drawable.icon_setup_click);
                    settingText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                    break;
                case R.id.collect_layout:
                    collectionIcon.setImageResource(R.drawable.icon_collect_click);
                    collectionText.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                default:
                    break;
            }

        }

        return false;
    }

    /*
   Handler
    */
    static class MyHandler extends Handler {
        private WeakReference<TabMenuActivity> navigationMenuActivityWeakReference;

        public MyHandler(TabMenuActivity eduMainActivity) {
            navigationMenuActivityWeakReference = new WeakReference<>(eduMainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TabMenuActivity navigationMenuActivity = navigationMenuActivityWeakReference.get();
            if (navigationMenuActivity != null) {
                switch (msg.what) {
                    case SHOW_NEXT_PAGE:
                        navigationMenuActivity.autoNextPage(msg.arg1);
                        break;
                    default:
                        break;

                }

            }
        }
    }

    @Override
    public void onStyleChanged() {
        finish();
    }

    /**
     * 获取分类列表
     */
    protected StringRequest getCatoRequest(int... type) {
        return new StringRequest(BaseURLUtil.getConsultCato(type), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONArray jsonArray = object.getJSONObject("datas").getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ConsultCato consultCato = new ConsultCato();
                            consultCato.setCodeId(jsonObject.optInt("codeId"));
                            consultCato.setCodeValue(jsonObject.optString("codeValue"));
                            consultCato.setCodeType(jsonObject.optInt("codeType"));
                            int savedSize = DataSupport.where("codeType = ? and codeId = ?"
                                    , String.valueOf(consultCato.getCodeType())
                                    , String.valueOf(consultCato.getCodeId())).find(ConsultCato.class).size();
                            if (savedSize == 0) {
                                consultCato.save();
                            } else {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("codeId", consultCato.getCodeId());
                                contentValues.put("codeValue", consultCato.getCodeValue());
                                DataSupport.updateAll(ConsultCato.class, contentValues, "codeType = ? and codeId = ?"
                                        , String.valueOf(consultCato.getCodeType())
                                        , String.valueOf(consultCato.getCodeId()));
                            }
                        }
                        setCatoView();

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


    protected void setCatoView() {
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AppContext.screenWidth / 5, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(3, 5, 3, 5);

        TextView jzTxt = (TextView) findViewById(R.id.cato_jz);
        TextView zyTxt = (TextView) findViewById(R.id.cato_zy);
        TextView kwTxt = (TextView) findViewById(R.id.cato_kw);
        jzTxt.setLayoutParams(params);
        jzTxt.setText("建筑");
        jzTxt.setTextColor(Color.WHITE);
        jzTxt.setGravity(Gravity.CENTER);
        jzTxt.setBackgroundResource(R.drawable.bg_cato_parent_rect);
        zyTxt.setLayoutParams(params);
        zyTxt.setText("专业");
        zyTxt.setTextColor(Color.WHITE);
        zyTxt.setGravity(Gravity.CENTER);
        zyTxt.setBackgroundResource(R.drawable.bg_cato_parent_rect);
        kwTxt.setLayoutParams(params);
        kwTxt.setText("关键词");
        kwTxt.setTextColor(Color.WHITE);
        kwTxt.setGravity(Gravity.CENTER);
        kwTxt.setBackgroundResource(R.drawable.bg_cato_parent_rect);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cato_jz:                         //建筑
                        Intent intent1 = new Intent(TabMenuActivity.this, ConsultCatoMainActivity.class);
                        intent1.putExtra("expand_index", 0);
                        startActivity(intent1);
                        break;
                    case R.id.cato_zy:                         //专业
                        Intent intent2 = new Intent(TabMenuActivity.this, ConsultCatoMainActivity.class);
                        intent2.putExtra("expand_index", 1);
                        startActivity(intent2);
                        break;
                    case R.id.cato_kw:                        //关键词
                        Intent intent3 = new Intent(TabMenuActivity.this, ConsultCatoMainActivity.class);
                        intent3.putExtra("expand_index", 2);
                        startActivity(intent3);
                        break;
                    default:
                        break;

                }
            }
        };

        jzTxt.setOnClickListener(onClickListener);
        zyTxt.setOnClickListener(onClickListener);
        kwTxt.setOnClickListener(onClickListener);

        LinearLayout jzLayout = (LinearLayout) findViewById(R.id.jz_layout);
        LinearLayout zyLayout = (LinearLayout) findViewById(R.id.zy_layout);
        LinearLayout kwLayout = (LinearLayout) findViewById(R.id.kw_layout);
        if (jzLayout.getChildCount() > 0) {
            jzLayout.removeAllViews();
        }
        if (zyLayout.getChildCount() > 0) {
            zyLayout.removeAllViews();
        }
        if (kwLayout.getChildCount() > 0) {
            kwLayout.removeAllViews();
        }

        final List<ConsultCato> allCatoJZ = DataSupport.where("codeType = ?", String.valueOf(ConsultCatoMainActivity.CATO_JZ)).find(ConsultCato.class);
        List<ConsultCato> allCatoZY = DataSupport.where("codeType = ?", String.valueOf(ConsultCatoMainActivity.CATO_ZY)).find(ConsultCato.class);
        List<ConsultCato> allCatoKW = DataSupport.where("codeType = ?", String.valueOf(ConsultCatoMainActivity.CATO_KW)).find(ConsultCato.class);

        int length1 = allCatoJZ.size();
        for (int i = 0; i < length1; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.WHITE);
            final ConsultCato consultCato = allCatoJZ.get(i);
            textView.setText(consultCato.getCodeValue());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_cato_child_rect);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TabMenuActivity.this, CatoConsultListActivity.class);
                    intent.putExtra("codeId", consultCato.getCodeId());
                    intent.putExtra("codeValue", consultCato.getCodeValue());
                    startActivity(intent);
                }
            });
            jzLayout.addView(textView);
        }
        int length2 = allCatoZY.size();
        for (int i = 0; i < length2; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.WHITE);
            final ConsultCato consultCato = allCatoZY.get(i);
            textView.setText(consultCato.getCodeValue());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_cato_child_rect);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TabMenuActivity.this, CatoConsultListActivity.class);
                    intent.putExtra("codeId", consultCato.getCodeId());
                    intent.putExtra("codeValue", consultCato.getCodeValue());
                    startActivity(intent);
                }
            });
            zyLayout.addView(textView);
        }

        int length3 = allCatoKW.size();
        for (int i = 0; i < length3; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.WHITE);
            final ConsultCato consultCato = allCatoKW.get(i);
            textView.setText(consultCato.getCodeValue());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_cato_child_rect);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TabMenuActivity.this, CatoConsultListActivity.class);
                    intent.putExtra("codeId", consultCato.getCodeId());
                    intent.putExtra("codeValue", consultCato.getCodeValue());
                    startActivity(intent);
                }
            });
            kwLayout.addView(textView);
        }
    }

}