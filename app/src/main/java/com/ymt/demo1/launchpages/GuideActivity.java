package com.ymt.demo1.launchpages;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.ymt.demo1.customViews.IndicatorView;
import com.ymt.demo1.customViews.obsScrollview.CacheFragmentStatePagerAdapter;
import com.ymt.demo1.R;

import java.lang.ref.WeakReference;

/**
 * Created by Moses on 2015
 */
public class GuideActivity extends FragmentActivity {
    private static final int SHOW_NEXT_PAGE = 0;
    private ViewPager guideVPager;
    private MyHandler myHandler = new MyHandler(this);
    private boolean onChange = true;
    private SharedPreferences screenWidthPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_guide);
        initViews();
        SharedPreferences mSharedPreferences = getSharedPreferences(MainActivity.SETTING_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(MainActivity.FIRST_LAUNCH_KEY, false);
        editor.apply();
    }

    protected void initViews() {
        guideVPager = (ViewPager) findViewById(R.id.cf_guide_view_pager);
        screenWidthPreference = getSharedPreferences("screen_width", MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //屏幕尺寸这这里保存到AppContext供全局使用
                int screenWidth = guideVPager.getRootView().getWidth();
                SharedPreferences.Editor editor = screenWidthPreference.edit();
                editor.putInt("screen_width", screenWidth);
                editor.apply();
            }
        }).start();

        FragmentManager manager = getSupportFragmentManager();
        GuidePageAdapter pageAdapter = new GuidePageAdapter(manager);

        guideVPager.setAdapter(pageAdapter);
        guideVPager.setOffscreenPageLimit(3);

        //指示器Indicator
        final IndicatorView indicator = (IndicatorView) findViewById(R.id.myPointIndicator);
        indicator.updateTotal(pageAdapter.getCount());   //设置指示器显示item个数（适配adapter中元素个数）
        indicator.setCurr(0);

         /*
        开启线程，让使Viewpager轮播
         */

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (onChange) {
                    int toPosition;
                    int curPosition = guideVPager.getCurrentItem();
                    if (curPosition == 4) {
                        return;
                    }
                    toPosition = curPosition + 1;
                    Message msg = Message.obtain();
                    msg.what = SHOW_NEXT_PAGE;
                    msg.arg1 = toPosition;
                    myHandler.sendMessage(msg);

                    try {
                        Thread.sleep(5000);         //每3.2s切换到下一page
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        guideVPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurr(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */
    private class GuidePageAdapter extends CacheFragmentStatePagerAdapter {

        public GuidePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment pageFragment = null;
            switch (position) {
                case 0:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide1);
                    break;
                case 1:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide2);
                    break;
                case 2:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide3);
                    break;
                case 3:
                    pageFragment = GuidePageFragment.newInstance(R.drawable.guide4);
                    break;
                case 4:
                    pageFragment = GuidePageFragment3.newInstance(R.drawable.guide5);
                    break;
                default:
                    break;

            }
            return pageFragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }
    }

    /*
   Handler
    */
    static class MyHandler extends Handler {
        private WeakReference<GuideActivity> guideActivityWeakReference;

        public MyHandler(GuideActivity guideActivity) {
            guideActivityWeakReference = new WeakReference<>(guideActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GuideActivity guideActivity = guideActivityWeakReference.get();
            if (guideActivity != null) {
                switch (msg.what) {
                    case SHOW_NEXT_PAGE:
                        guideActivity.autoNextPage(msg.arg1);
                        break;
                    default:
                        break;

                }

            }
        }
    }

    /**
     * ViewPager轮播
     */
    protected void autoNextPage(int toPosition) {
        guideVPager.setCurrentItem(toPosition);
    }


}
