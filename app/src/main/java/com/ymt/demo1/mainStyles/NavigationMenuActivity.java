package com.ymt.demo1.mainStyles;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ymt.demo1.R;
import com.ymt.demo1.adapter.CyclePagerAdapter;
import com.ymt.demo1.customViews.CircleImageView;
import com.ymt.demo1.customViews.IndicatorView;
import com.ymt.demo1.main.advice.AdviceActivity;
import com.ymt.demo1.main.help.HelpActivity;
import com.ymt.demo1.main.setting.SettingActivity;
import com.ymt.demo1.main.sign.SignInActivity;
import com.ymt.demo1.main.sign.SignUpActivity;
import com.ymt.demo1.plates.MoreCatoActivity;
import com.ymt.demo1.plates.eduPlane.EduMainActivity;
import com.ymt.demo1.plates.hub.FireHubPagerTabActivity;
import com.ymt.demo1.plates.knowledge.KnowledgeMainActivity;
import com.ymt.demo1.plates.personal.PersonalPagerTabActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Dan on 2015/5/8
 */
public class NavigationMenuActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private static boolean ALWAYS_ON = true;
    private static final int SHOW_NEXT_PAGE = 0;
    private ViewPager adViewPager;
    private final MyHandler myHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);
        initView();
    }

    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.icon_float_logo72);
        toolbar.setTitle("新消防");// 标题的文字需在setSupportActionBar之前，不然会无效
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
        ArrayList<View> views = new ArrayList<>();
        views.add(inflater.inflate(R.layout.edu_pager1, null));
        views.add(inflater.inflate(R.layout.edu_pager2, null));
        views.add(inflater.inflate(R.layout.edu_pager3, null));
        adPagerAdapter.setViews(views);
        //指示器Indicator
        final IndicatorView indicator = (IndicatorView) findViewById(R.id.myPointIndicator);
        indicator.updateTotal(adPagerAdapter.getCount());   //设置指示器显示item个数（适配adapter中元素个数）
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
                indicator.setCurr(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:

                        break;
                    case ViewPager.SCROLL_STATE_IDLE:

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
                        Toast.makeText(NavigationMenuActivity.this, "这里应加入view", Toast.LENGTH_SHORT).show();
                        continue;
                    }

                    int toPosition;
                    int curPosition = adViewPager.getCurrentItem();
                    if (curPosition < adViewPager.getChildCount() - 1) {
                        toPosition = curPosition + 1;
                    } else {
                        toPosition = 0;             //从page尾跳到page头
                    }

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
        }).start();

    }

    /**
     * 划出菜单view 及其事件
     */
    protected void initDrawerMenuView() {
        //头像
        CircleImageView personIconBtn = (CircleImageView) findViewById(R.id.personal_icon_btn);
        //注册
        View signUp = findViewById(R.id.sign_up);
        //帮助
        View help = findViewById(R.id.help);
        //建议
        View advice = findViewById(R.id.advice);
        //设置
        View setting = findViewById(R.id.setting);
        //收藏
        View collect = findViewById(R.id.collect);
        //登录
        View singIn = findViewById(R.id.sign_in);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.personal_icon_btn:
                        mDrawerLayout.closeDrawers();                             //个人中心
                        startActivity(new Intent(NavigationMenuActivity.this, PersonalPagerTabActivity.class));
                        break;
                    case R.id.sign_up:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(NavigationMenuActivity.this, SignUpActivity.class));       //注册
                        break;
                    case R.id.help:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(NavigationMenuActivity.this, HelpActivity.class));         //帮助
                        break;
                    case R.id.advice:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(NavigationMenuActivity.this, AdviceActivity.class));       //建议
                        break;
                    case R.id.setting:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(NavigationMenuActivity.this, SettingActivity.class));      //设置
                        break;
                    case R.id.collect:
                        //todo 收藏
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.sign_in:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(NavigationMenuActivity.this, SignInActivity.class));       //登录
                        break;
                    default:
                        break;

                }

            }
        };

        personIconBtn.setOnClickListener(onClickListener);
        signUp.setOnClickListener(onClickListener);
        help.setOnClickListener(onClickListener);
        advice.setOnClickListener(onClickListener);
        setting.setOnClickListener(onClickListener);
        collect.setOnClickListener(onClickListener);
        singIn.setOnClickListener(onClickListener);
    }

    protected void initMainView() {
        //咨询分类GridView
        GridView consultGrid = (GridView) findViewById(R.id.consult_cato_grid);
        //咨询分类“显示全部”
        TextView viewAllConsultBtn = (TextView) findViewById(R.id.all_consult_cato_text);
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
                    case R.id.all_consult_cato_text:
                        //todo 显示全部 咨询分类
                        Toast.makeText(NavigationMenuActivity.this, "全部咨询分类", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.img_news:
                        //todo 显示全部 咨询分类
                        Toast.makeText(NavigationMenuActivity.this, "资讯平台", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.img_export:
                        //todo 显示全部 咨询分类
                        Toast.makeText(NavigationMenuActivity.this, "专家咨询", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.img_edu:
                        startActivity(new Intent(NavigationMenuActivity.this, EduMainActivity.class));              //教育平台
                        break;
                    case R.id.img_knowledge:
                        startActivity(new Intent(NavigationMenuActivity.this, KnowledgeMainActivity.class));              //知识平台
                        break;
                    case R.id.img_hub:
                        startActivity(new Intent(NavigationMenuActivity.this, FireHubPagerTabActivity.class));              //论坛
                        break;
                    case R.id.img_more:
                        startActivity(new Intent(NavigationMenuActivity.this, MoreCatoActivity.class));              //更多
                        break;
                    default:
                        break;

                }
            }
        };
        viewAllConsultBtn.setOnClickListener(onClickListener);
        newsBtn.setOnClickListener(onClickListener);
        exportBtn.setOnClickListener(onClickListener);
        moreBtn.setOnClickListener(onClickListener);
        eduBtn.setOnClickListener(onClickListener);
        knowledgeBtn.setOnClickListener(onClickListener);
        hubBtn.setOnClickListener(onClickListener);

        /*
        咨询分类表格item 点击事件
         */
        consultGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //todo 咨询分类 适配器
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

}
