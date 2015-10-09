package com.ymt.demo1.mainStyles;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ymt.demo1.R;
import com.ymt.demo1.adapter.CyclePagerAdapter;
import com.ymt.demo1.beams.consult_cato.ConsultCato;
import com.ymt.demo1.beams.news.NewsSummary;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.customViews.IndicatorView;
import com.ymt.demo1.main.CollectActivity;
import com.ymt.demo1.main.ShareActivity;
import com.ymt.demo1.plates.news.NewsDetailActivity;
import com.ymt.demo1.utils.AppContext;
import com.ymt.demo1.utils.BaseURLUtil;
import com.ymt.demo1.main.search.SearchActivity;
import com.ymt.demo1.main.advice.AdviceActivity;
import com.ymt.demo1.main.help.HelpActivity;
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
import com.ymt.demo1.plates.personal.PersonalPagerTabActivity;
import com.ymt.demo1.utils.BitmapCutUtil;
import com.ymt.demo1.utils.ConnectionActReceiver;
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
public class NavigationMenuActivity extends ActionBarActivity implements ManageAppearanceActivity.StyleChangeListener, ConnectionActReceiver.OnConnectionChangeListener {

    private DrawerLayout mDrawerLayout;
    private static boolean ALWAYS_ON = true;
    private static final int SHOW_NEXT_PAGE = 0;
    private ViewPager adViewPager;
    private final MyHandler myHandler = new MyHandler(this);
    public static ManageAppearanceActivity.StyleChangeListener styleChangeListener;
    private CircleImageView personIconBtn;
    private TextView userName;
    private boolean doAutoChange;
    private RequestQueue mQueue;
    private ConnectionActReceiver connectionActReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        catoList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);
        //3个显示在主页的图片新闻
        imgSummary1 = new NewsSummary();
        imgSummary2 = new NewsSummary();
        imgSummary3 = new NewsSummary();
        //注册网络广播监听
        connectionActReceiver = new ConnectionActReceiver();
        connectionActReceiver.setOnConnectionChangeListener(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionActReceiver, filter);

        mQueue.add(getCatoRequest(BaseURLUtil.PUB_ZX_JZ));
        mQueue.add(getCatoRequest(BaseURLUtil.PUB_ZX_ZY));
        mQueue.add(getCatoRequest(BaseURLUtil.PUB_ZX_GJC));
//        mQueue.add(getImageNewsPic(BaseURLUtil.BASE_URL + "/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_news_photo" + "&order=new&start=" + String.valueOf(1)));
        //界面风格
        styleChangeListener = this;
        //自动轮播开关
        doAutoChange = true;
        setContentView(R.layout.activity_navigation_menu);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectionActReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        personIconBtn.setImageBitmap(AppContext.headerPic);
        userName.setText(AppContext.now_user_name);
    }

    protected void initView() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

//        toolbar.setLogo(R.drawable.icon_float_logo72);
        toolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        toolbar.setLogo(R.drawable.logo_tb);
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open_content_desc,
                R.string.drawer_close_content_desc);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        initDrawerMenuView();
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

    /**
     * 划出菜单view 及其事件
     */
    protected void initDrawerMenuView() {
        //头像
        personIconBtn = (CircleImageView) findViewById(R.id.personal_icon_btn);
        userName = (TextView) findViewById(R.id.user_name);
        //分享
        View recommend = findViewById(R.id.recommend);
        //帮助
        View help = findViewById(R.id.help);
        //收藏
        View collect = findViewById(R.id.collect);
        //建议
        View advice = findViewById(R.id.advice);
        //设置
        View setting = findViewById(R.id.setting);
        //电话
        TextView xxfPhone = (TextView) findViewById(R.id.xxf_phone_line);
        xxfPhone.setText("咨询热线: " + getString(R.string.xxf_phone));
        //qq
        TextView xxfQQ = (TextView) findViewById(R.id.xxf_qq);
        xxfQQ.setText("咨询QQ: " + getString(R.string.xxf_qq));
        //网址
        TextView xxfUrl = (TextView) findViewById(R.id.xxf_url);
        xxfUrl.setText("网址链接: " + getString(R.string.xxf_url));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.personal_icon_btn:
                        startActivity(new Intent(NavigationMenuActivity.this, PersonalPagerTabActivity.class));
                        mDrawerLayout.closeDrawers();                                                       //个人中心
                        break;
                    case R.id.recommend:            //分享
                        startActivity(new Intent(NavigationMenuActivity.this, ShareActivity.class));         //分享
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.help:                 //帮助
                        startActivity(new Intent(NavigationMenuActivity.this, HelpActivity.class));         //帮助
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.collect:              //收藏
                        startActivity(new Intent(NavigationMenuActivity.this, CollectActivity.class));
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.advice:               //建议
                        startActivity(new Intent(NavigationMenuActivity.this, AdviceActivity.class));       //建议
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.setting:              //设置
                        startActivity(new Intent(NavigationMenuActivity.this, SettingActivity.class));      //设置
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.xxf_phone_line:
                        // 拨打客服电话
                        Uri uri = Uri.parse("tel:" + getString(R.string.xxf_phone));
                        startActivity(new Intent(Intent.ACTION_DIAL, uri));
                        break;
                    case R.id.xxf_qq:
                        // 连线新消防QQ
                        try {
                            openQQApp("com.tencent.qq");
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.xxf_url:
                        //打开新消防主页
                        Uri uri1 = Uri.parse("http:" + getString(R.string.xxf_url));
                        startActivity(new Intent(Intent.ACTION_VIEW, uri1));
                        break;
                    default:
                        break;

                }

            }
        };

        personIconBtn.setOnClickListener(onClickListener);
        help.setOnClickListener(onClickListener);
        advice.setOnClickListener(onClickListener);
        setting.setOnClickListener(onClickListener);
        recommend.setOnClickListener(onClickListener);
        collect.setOnClickListener(onClickListener);
        xxfPhone.setOnClickListener(onClickListener);
        xxfQQ.setOnClickListener(onClickListener);
        xxfUrl.setOnClickListener(onClickListener);
    }

    private void openQQApp(String packageName) throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo pi = pm.getPackageInfo(packageName, 0);

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String pkgName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(pkgName, className);

            intent.setComponent(cn);
            startActivity(intent);
        }
    }

    protected void initMainView() {
        picViewI = (ImageView) findViewById(R.id.img_news1);
        picViewII = (ImageView) findViewById(R.id.img_news2);
        picViewIII = (ImageView) findViewById(R.id.img_news3);

        //咨询分类GridView
//        GridView consultGrid = (GridView) findViewById(R.id.consult_cato_table);
        //咨询平台 入口
        ImageView newsBtn = (ImageView) findViewById(R.id.img_news);
        //专家咨询 入口
        ImageView exportBtn = (ImageView) findViewById(R.id.img_export);
        //教育平台 入口
        ImageView eduBtn = (ImageView) findViewById(R.id.img_edu);
        //知识平台 入口
        ImageView knowledgeBtn = (ImageView) findViewById(R.id.img_knowledge);
        //论坛 入口
        ImageView hubBtn = (ImageView) findViewById(R.id.img_hub);
        //更多 入口
        ImageView moreBtn = (ImageView) findViewById(R.id.img_more);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_news:
                        startActivity(new Intent(NavigationMenuActivity.this, NewsTabActivity.class));             //资讯平台
                        break;
                    case R.id.img_export:
                        startActivity(new Intent(NavigationMenuActivity.this, ExportConsultMainActivity.class));    //专家咨询
                        break;
                    case R.id.img_edu:
                        startActivity(new Intent(NavigationMenuActivity.this, EduMainActivity.class));              //教育平台
                        break;
                    case R.id.img_knowledge:
                        startActivity(new Intent(NavigationMenuActivity.this, KnowledgeMainActivity.class));        //知识平台
                        break;
                    case R.id.img_hub:
                        startActivity(new Intent(NavigationMenuActivity.this, FireHubMainActivity.class));      //论坛
                        break;
                    case R.id.img_more:
                        startActivity(new Intent(NavigationMenuActivity.this, MoreCatoActivity.class));             //更多
                        break;
                    case R.id.img_news1:
                        if (TextUtils.isEmpty(imgSummary1.getArticle_title())) {
                            return;
                        }
                        Intent intent1 = new Intent(NavigationMenuActivity.this, NewsDetailActivity.class);
                        intent1.putExtra("summary", imgSummary1);
                        startActivity(intent1);
                        break;
                    case R.id.img_news2:
                        if (TextUtils.isEmpty(imgSummary2.getArticle_title())) {
                            return;
                        }
                        Intent intent2 = new Intent(NavigationMenuActivity.this, NewsDetailActivity.class);
                        intent2.putExtra("summary", imgSummary2);
                        startActivity(intent2);
                        break;
                    case R.id.img_news3:
                        if (TextUtils.isEmpty(imgSummary3.getArticle_title())) {
                            return;
                        }
                        Intent intent3 = new Intent(NavigationMenuActivity.this, NewsDetailActivity.class);
                        intent3.putExtra("summary", imgSummary3);
                        startActivity(intent3);
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
        picViewI.setOnClickListener(onClickListener);
        picViewII.setOnClickListener(onClickListener);
        picViewIII.setOnClickListener(onClickListener);

        setCatoView();
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

    public void onScanBtnClicked(MenuItem item) {
        switch (item.getTitle().toString()) {
            case "action_scan":
                Toast.makeText(this, "扫二维码", Toast.LENGTH_SHORT).show();
                //todo 二维码扫描
                Intent scanIntent = new Intent(NavigationMenuActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, 12345);
                break;
        }
    }

    /*
   Handler
    */
    static class MyHandler extends Handler {
        private WeakReference<NavigationMenuActivity> navigationMenuActivityWeakReference;

        public MyHandler(NavigationMenuActivity eduMainActivity) {
            navigationMenuActivityWeakReference = new WeakReference<>(eduMainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NavigationMenuActivity navigationMenuActivity = navigationMenuActivityWeakReference.get();
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
                    Toast.makeText(NavigationMenuActivity.this, "数据错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(NavigationMenuActivity.this, "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void setCatoView() {
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AppContext.screenWidth / 5, LinearLayout.LayoutParams.MATCH_PARENT);
//        Log.e("TAG", "screenWidth>>>>>>>>>>" + AppContext.screenWidth);
        params.setMargins(0, 0, 2, 3);

        TextView jzTxt = (TextView) findViewById(R.id.cato_jz);
        TextView zyTxt = (TextView) findViewById(R.id.cato_zy);
        TextView kwTxt = (TextView) findViewById(R.id.cato_kw);
        jzTxt.setLayoutParams(params);
        jzTxt.setText("建筑");
        jzTxt.setTextColor(Color.WHITE);
        jzTxt.setGravity(Gravity.CENTER);
        jzTxt.setBackgroundResource(R.drawable.bg_cato_parent);
        zyTxt.setLayoutParams(params);
        zyTxt.setText("专业");
        zyTxt.setTextColor(Color.WHITE);
        zyTxt.setGravity(Gravity.CENTER);
        zyTxt.setBackgroundResource(R.drawable.bg_cato_parent);
        kwTxt.setLayoutParams(params);
        kwTxt.setText("关键词");
        kwTxt.setTextColor(Color.WHITE);
        kwTxt.setGravity(Gravity.CENTER);
        kwTxt.setBackgroundResource(R.drawable.bg_cato_parent);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cato_jz:                         //建筑
                        Intent intent1 = new Intent(NavigationMenuActivity.this, ConsultCatoMainActivity.class);
                        intent1.putExtra("expand_index", 0);
                        startActivity(intent1);
                        break;
                    case R.id.cato_zy:                         //专业
                        Intent intent2 = new Intent(NavigationMenuActivity.this, ConsultCatoMainActivity.class);
                        intent2.putExtra("expand_index", 1);
                        startActivity(intent2);
                        break;
                    case R.id.cato_kw:                        //关键词
                        Intent intent3 = new Intent(NavigationMenuActivity.this, ConsultCatoMainActivity.class);
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
            textView.setBackgroundResource(R.drawable.bg_cato_child);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationMenuActivity.this, CatoConsultListActivity.class);
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
            textView.setBackgroundResource(R.drawable.bg_cato_child);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationMenuActivity.this, CatoConsultListActivity.class);
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
            textView.setBackgroundResource(R.drawable.bg_cato_child);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NavigationMenuActivity.this, CatoConsultListActivity.class);
                    intent.putExtra("search_key_word", consultCato.getNote());
                    intent.putExtra("code", consultCato.getCode());
                    startActivity(intent);
                }
            });
            kwLayout.addView(textView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 12345 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            if (scanResult == null)
                return;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(scanResult));
            startActivity(intent);
        }
    }


    private ImageView picViewI;
    private ImageView picViewII;
    private ImageView picViewIII;
    private NewsSummary imgSummary1;
    private NewsSummary imgSummary2;
    private NewsSummary imgSummary3;

    private String[] imgUrls = new String[3];

    /**
     * 最近的三张图片新闻（显示在主界面）
     */
    private StringRequest getImageNewsPic(String urlStr) {

        return new StringRequest(urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray summaryArray = jsonObject.getJSONObject("datas").getJSONArray("listData");
                    //图片
                    int length = summaryArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = summaryArray.getJSONObject(i);
                        NewsSummary summary = new NewsSummary();
                        summary.setContent(object.optString("content"));
                        summary.setCreate_time(object.optString("create_time"));
                        summary.setArticle_title(object.optString("article_title"));
                        summary.setHitnum(object.optString("hitnum"));
                        summary.setThe_id(object.optString("id"));
                        summary.setFk_create_user_id(object.optString("fk_create_user_id"));
                        summary.setSource(object.optString("source"));
                        summary.setEditor(object.optString("editor"));
                        summary.setAuthor(object.optString("author"));
                        summary.setStatus(object.optString("status"));
                        summary.setPic(BaseURLUtil.BASE_URL + object.opt("pic"));

                        if (i == 0) {
                            imgSummary1 = summary;
                            if (picViewI.getBackground() != null) {
                                picViewI.setBackgroundDrawable(null);
                            }
                            imgUrls[0] = summary.getPic();
                        } else if (i == 1) {
                            imgSummary2 = summary;
                            if (picViewII.getBackground() != null) {
                                picViewII.setBackgroundDrawable(null);
                            }
                            imgUrls[1] = summary.getPic();
                        } else if (i == 2) {
                            imgSummary3 = summary;
                            if (picViewIII.getBackground() != null) {
                                picViewIII.setBackgroundDrawable(null);
                            }
                            imgUrls[2] = summary.getPic();
                        } else {
                            break;
                        }
                    }
                    //读取图片
                    getImageNews();

                } catch (JSONException e) {
//                    e.printStackTrace();
                    Toast.makeText(NavigationMenuActivity.this, "网络错误，请稍后重试！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    public void onConnected() {
        mQueue.add(getImageNewsPic(BaseURLUtil.BASE_URL + "/fw?controller=com.xfsm.action.ArticleAction&m=list&type=" + "xf_article_h_news_photo" + "&order=new&start=" + String.valueOf(1)));
    }

    //读取、截图
    private void getImageNews() {
        //todo 根据控件、图片的尺寸进行剪裁。   【目前的方式值适用于控件宽:控件高 近似于 图片宽:图片高】
        for (int i = 0; i < 3; i++) {
            String urls = imgUrls[i];
            if (i == 0) {
                ImageRequest request = new ImageRequest(urls, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bitmap1 = BitmapCutUtil.getBitmapCutByViewSize(picViewI, bitmap);
                        picViewI.setImageBitmap(bitmap1);
                    }
                }, picViewI.getWidth(), picViewI.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                mQueue.add(request);
            } else if (i == 1) {
                ImageRequest request = new ImageRequest(urls, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bitmap1 = BitmapCutUtil.getBitmapCutByViewSize(picViewII, bitmap);
                        picViewII.setImageBitmap(bitmap1);
                    }
                }, picViewII.getWidth(), picViewII.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                mQueue.add(request);
            } else if (i == 2) {
                ImageRequest request = new ImageRequest(urls, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Bitmap bitmap1 = BitmapCutUtil.getBitmapCutByViewSize(picViewIII, bitmap);
                        picViewIII.setImageBitmap(bitmap1);
                    }
                }, picViewIII.getWidth(), picViewIII.getHeight(), Bitmap.Config.RGB_565, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                mQueue.add(request);
            }

        }
    }

}