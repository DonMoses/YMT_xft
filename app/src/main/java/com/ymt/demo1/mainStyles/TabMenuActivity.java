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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
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
import com.ymt.demo1.customKeyBoard.ConsultActivity;
import com.ymt.demo1.customViews.IndicatorView;
import com.ymt.demo1.main.advice.AdviceActivity;
import com.ymt.demo1.main.help.HelpActivity;
import com.ymt.demo1.main.search.SearchActivity;
import com.ymt.demo1.main.setting.ManageAppearanceActivity;
import com.ymt.demo1.main.setting.SettingActivity;
import com.ymt.demo1.main.sign.SignUpFragment;
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
    private FrameLayout pagerParentLayout;

    private ImageView signIcon, adviceIcon, helpIcon, settingIcon, collectionIcon;
    private TextView signText, adviceText, helpText, settingText, collectionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        catoList = new ArrayList<>();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        mQueue.add(getCatoRequest(BaseURLUtil.PUB_ZX_JZ));
        mQueue.add(getCatoRequest(BaseURLUtil.PUB_ZX_ZY));
        mQueue.add(getCatoRequest(BaseURLUtil.PUB_ZX_GJC));
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

        //pager外部layout
        pagerParentLayout = (FrameLayout) findViewById(R.id.pager_layout);

        /*
        更新数据源（Views）
         */
        LayoutInflater inflater = LayoutInflater.from(this);
        final ArrayList<View> views = new ArrayList<>();
        TableLayout gateView = (TableLayout) inflater.inflate(R.layout.tab_pager_gate, null);
        //顶部板块入口
        initPagerGate(gateView);
        views.add(gateView);         //各个板块的入口
        views.add(inflater.inflate(R.layout.banner_page_1, null));
        views.add(inflater.inflate(R.layout.banner_page_2, null));
        views.add(inflater.inflate(R.layout.banner_page_3, null));

        adPagerAdapter.setViews(views);
        adViewPager.setCurrentItem(0);
        //指示器Indicator
        final IndicatorView indicator = (IndicatorView) findViewById(R.id.myPointIndicator);
        indicator.updateTotal(adPagerAdapter.getCount() - 1);   //设置指示器显示item个数（适配adapter中元素个数）
        if (adViewPager.getCurrentItem() == 0) {
            indicator.setVisibility(View.INVISIBLE);
        }

        //获取pager的margin值
        final LinearLayout.LayoutParams pagerParams = (LinearLayout.LayoutParams) pagerParentLayout.getLayoutParams();
        final int l = pagerParams.leftMargin;
        final int t = pagerParams.topMargin;
        final int r = pagerParams.rightMargin;
        final int b = pagerParams.bottomMargin;

        /*
        pager 滑动事件
         */
        adViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                int pageIndex = position;
//                int indicatorIndex;
//                if (position == 0) {
//                    pageIndex = views.size() - 2;
//                    indicatorIndex = 2;
//                } else if (position == views.size() - 1) {
//                    pageIndex = 1;
//                    indicatorIndex = 0;
//                } else {
//                    indicatorIndex = position - 1;
//                }
//                adViewPager.setCurrentItem(pageIndex, false);
//                indicator.setCurr(indicatorIndex);
                if (position == 0) {
                    indicator.setVisibility(View.INVISIBLE);
                    pagerParams.setMargins(l, t, r, b);
                } else {
                    indicator.setVisibility(View.VISIBLE);
                    indicator.setCurr(position - 1);
                    pagerParams.setMargins(0, 0, 0, 0);
                }

                pagerParentLayout.setLayoutParams(pagerParams);

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

                    if (adViewPager.getCurrentItem() == 0) {
                        doAutoChange = false;
                    }

                    if (doAutoChange) {
                        int toPosition;
                        int curPosition = adViewPager.getCurrentItem();
                        if (curPosition != 0) {
                            if (curPosition == adPagerAdapter.getCount() - 1) {
                                toPosition = 1;
                            } else {
                                toPosition = curPosition + 1;
                            }

                            Message msg = Message.obtain();
                            msg.what = SHOW_NEXT_PAGE;
                            msg.arg1 = toPosition;
                            myHandler.sendMessage(msg);
                        }

                    }

                    try {
                        Thread.sleep(6000);         //每6 s切换到下一page
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    protected void initMainView() {
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
        View menuSign = findViewById(R.id.sign_layout);
        View menuAdvice = findViewById(R.id.advice_layout);
        View menuHelp = findViewById(R.id.help_layout);
        View menuSetting = findViewById(R.id.setting_layout);
        View menuCollection = findViewById(R.id.collect_layout);

        signIcon = (ImageView) menuSign.findViewById(R.id.sign_icon);
        signText = (TextView) menuSign.findViewById(R.id.sign_text);
        adviceIcon = (ImageView) menuAdvice.findViewById(R.id.advice_icon);
        adviceText = (TextView) menuAdvice.findViewById(R.id.advice_text);
        helpIcon = (ImageView) menuHelp.findViewById(R.id.help_icon);
        helpText = (TextView) menuHelp.findViewById(R.id.help_text);
        settingIcon = (ImageView) menuSetting.findViewById(R.id.setting_icon);
        settingText = (TextView) menuSetting.findViewById(R.id.setting_text);
        collectionIcon = (ImageView) findViewById(R.id.collect_icon);
        collectionText = (TextView) findViewById(R.id.collect_text);

        menuSign.setOnTouchListener(this);
        menuAdvice.setOnTouchListener(this);
        menuHelp.setOnTouchListener(this);
        menuSetting.setOnTouchListener(this);
        menuCollection.setOnTouchListener(this);
        menuSign.setOnClickListener(this);
        menuAdvice.setOnClickListener(this);
        menuHelp.setOnClickListener(this);
        menuSetting.setOnClickListener(this);
        menuCollection.setOnClickListener(this);
    }

    /**
     * 引入菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * ViewPager轮播
     */
    protected void autoNextPage(int toPosition) {
        adViewPager.setCurrentItem(toPosition);
    }

    public void onSearchBtnClicked(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "action_search":
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_layout:
                //跳转到注册界面
                startActivity(new Intent(this, SignUpFragment.class));
                break;
            case R.id.advice_layout:
                //跳转到意见反馈
                startActivity(new Intent(this, AdviceActivity.class));
                break;
            case R.id.help_layout:
                //跳转到帮助中心
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case R.id.setting_layout:
                //跳转到设置
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.collect_layout:
                //跳转到收藏
                startActivity(new Intent(this, ConsultActivity.class));
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
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.sign_layout:
                    signIcon.setImageResource(R.drawable.icon_register_click);
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

        /*
        触摸释放后改变 tab背景和 字体颜色
         */
        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (v.getId()) {
                case R.id.sign_layout:
                    signIcon.setImageResource(R.drawable.icon_register);
                    signText.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                    break;
                case R.id.advice_layout:
                    adviceIcon.setImageResource(R.drawable.icon_suggest);
                    adviceText.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                    break;
                case R.id.help_layout:
                    helpIcon.setImageResource(R.drawable.icon_help);
                    helpText.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                    break;
                case R.id.setting_layout:
                    settingIcon.setImageResource(R.drawable.icon_setup);
                    settingText.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
                    break;
                case R.id.collect_layout:
                    collectionIcon.setImageResource(R.drawable.icon_collect);
                    collectionText.setTextColor(getResources().getColor(R.color.material_blue_grey_800));
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
    protected StringRequest getCatoRequest(String type) {
        return new StringRequest(BaseURLUtil.doTypeAction(type), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.getString("result").equals("Y")) {
                        JSONArray jsonArray = object.getJSONArray("listData");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ConsultCato consultCato = new ConsultCato();
                            consultCato.setCode(jsonObject.getString("code"));
                            consultCato.setNote(jsonObject.getString("note"));
                            int savedSize = DataSupport.where("code = ?", jsonObject.getString("code")).find(ConsultCato.class).size();
                            if (savedSize == 0) {
                                consultCato.save();
                            } else {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("note", jsonObject.getString("note"));
                                DataSupport.updateAll(ConsultCato.class, contentValues, "code = ?", jsonObject.getString("code"));
                            }
                        }
                        setCatoView();

                    }
                } catch (JSONException e) {
                    Toast.makeText(TabMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(TabMenuActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void setCatoView() {
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AppContext.screenWidth / 5, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(6, 10, 6, 10);

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

        final List<ConsultCato> allCatoJZ = DataSupport.where("code like ?", "j%").find(ConsultCato.class);
        List<ConsultCato> allCatoZY = DataSupport.where("code like ?", "z%").find(ConsultCato.class);
        List<ConsultCato> allCatoKW = DataSupport.where("code like ?", "g%").find(ConsultCato.class);
        int length1 = allCatoJZ.size();
        for (int i = 0; i < length1; i++) {
            TextView textView = new TextView(this);
            textView.setLayoutParams(params);
            textView.setTextColor(Color.WHITE);
            final ConsultCato consultCato = allCatoJZ.get(i);
            textView.setText(consultCato.getNote());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_cato_child_rect);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TabMenuActivity.this, CatoConsultListActivity.class);
                    intent.putExtra("search_key_word", consultCato.getNote());
                    intent.putExtra("code", consultCato.getCode());
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
            textView.setText(consultCato.getNote());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_cato_child_rect);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TabMenuActivity.this, CatoConsultListActivity.class);
                    intent.putExtra("search_key_word", consultCato.getNote());
                    intent.putExtra("code", consultCato.getCode());
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
            textView.setText(consultCato.getNote());
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.bg_cato_child_rect);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TabMenuActivity.this, CatoConsultListActivity.class);
                    intent.putExtra("search_key_word", consultCato.getNote());
                    intent.putExtra("code", consultCato.getCode());
                    startActivity(intent);
                }
            });
            kwLayout.addView(textView);
        }
    }

}